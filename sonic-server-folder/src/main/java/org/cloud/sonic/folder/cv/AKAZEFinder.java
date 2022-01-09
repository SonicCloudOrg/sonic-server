package org.cloud.sonic.folder.cv;

import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_features2d.AKAZE;
import org.bytedeco.opencv.opencv_flann.Index;
import org.bytedeco.opencv.opencv_flann.IndexParams;
import org.bytedeco.opencv.opencv_flann.LshIndexParams;
import org.bytedeco.opencv.opencv_flann.SearchParams;
import org.cloud.sonic.folder.models.FindResult;
import org.cloud.sonic.folder.tools.FileTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.bytedeco.opencv.global.opencv_calib3d.CV_RANSAC;
import static org.bytedeco.opencv.global.opencv_calib3d.findHomography;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_flann.FLANN_DIST_HAMMING;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvSaveImage;

/**
 * @author ZhouYiXun
 * @des akaze算法
 * @date 2022/1/4 21:49
 */
@Component
public class AKAZEFinder {
    private final Logger logger = LoggerFactory.getLogger(AKAZEFinder.class);
    IplImage objectImage = null;
    AKAZE detector = AKAZE.create();
    double distanceThreshold = 0.6;
    int matchesMin = 4;
    double ransacReprojThreshold = 1.0;
    boolean useFLANN = false;
    KeyPointVector objectKeypoints = null, imageKeypoints = null;
    Mat objectDescriptors = null, imageDescriptors = null;
    Mat indicesMat, distsMat;
    Index flannIndex = null;
    IndexParams indexParams = null;
    SearchParams searchParams = null;
    Mat pt1 = null, pt2 = null, mask = null, H = null;
    ArrayList<Integer> ptpairs = null;
    @Autowired
    private FileTool fileTool;

    public void init() {
        objectKeypoints = new KeyPointVector();
        objectDescriptors = new Mat();
        detector.detectAndCompute(cvarrToMat(objectImage),
                new Mat(), objectKeypoints, objectDescriptors, false);

        int total = (int) objectKeypoints.size();
        if (useFLANN) {
            indicesMat = new Mat(total, 2, CV_32SC1);
            distsMat = new Mat(total, 2, CV_32FC1);
            flannIndex = new Index();
            indexParams = new LshIndexParams(12, 20, 2); // using LSH Hamming distance
            searchParams = new SearchParams(64, 0, true); // maximum number of leafs checked
            searchParams.deallocate(false); // for some reason FLANN seems to do it for us
        }
        pt1 = new Mat(total, 1, CV_32FC2);
        pt2 = new Mat(total, 1, CV_32FC2);
        mask = new Mat(total, 1, CV_8UC1);
        H = new Mat(3, 3, CV_64FC1);
        ptpairs = new ArrayList<Integer>(2 * objectDescriptors.rows());
        logger.info("模版图一共有" + total + "个特征点");
    }

    public double[] find(IplImage image) {
        if (objectDescriptors.rows() < matchesMin) {
            return null;
        }
        imageKeypoints = new KeyPointVector();
        imageDescriptors = new Mat();
        detector.detectAndCompute(cvarrToMat(image),
                new Mat(), imageKeypoints, imageDescriptors, false);
        if (imageDescriptors.rows() < matchesMin) {
            return null;
        }

        int total = (int) imageKeypoints.size();
        logger.info("原图一共有" + total + "个特征点");

        int w = objectImage.width();
        int h = objectImage.height();
        double[] srcCorners = {0, 0, w, 0, w, h, 0, h};
        double[] dstCorners = locatePlanarObject(objectKeypoints, objectDescriptors,
                imageKeypoints, imageDescriptors, srcCorners);
        return dstCorners;
    }

    private static final int[] bits = new int[256];

    static {
        for (int i = 0; i < bits.length; i++) {
            for (int j = i; j != 0; j >>= 1) {
                bits[i] += j & 0x1;
            }
        }
    }

    private int compareDescriptors(ByteBuffer d1, ByteBuffer d2, int best) {
        int totalCost = 0;
        assert d1.limit() - d1.position() == d2.limit() - d2.position();
        while (d1.position() < d1.limit()) {
            totalCost += bits[(d1.get() ^ d2.get()) & 0xFF];
            if (totalCost > best)
                break;
        }
        return totalCost;
    }

    private int naiveNearestNeighbor(ByteBuffer vec, ByteBuffer modelDescriptors) {
        int neighbor = -1;
        int d, dist1 = Integer.MAX_VALUE, dist2 = Integer.MAX_VALUE;
        int size = vec.limit() - vec.position();

        for (int i = 0; i * size < modelDescriptors.capacity(); i++) {
            ByteBuffer mvec = (ByteBuffer) modelDescriptors.position(i * size).limit((i + 1) * size);
            d = compareDescriptors((ByteBuffer) vec.reset(), mvec, dist2);
            if (d < dist1) {
                dist2 = dist1;
                dist1 = d;
                neighbor = i;
            } else if (d < dist2) {
                dist2 = d;
            }
        }
        if (dist1 < distanceThreshold * dist2)
            return neighbor;
        return -1;
    }

    private void findPairs(Mat objectDescriptors, Mat imageDescriptors) {
        int size = imageDescriptors.cols();
        ByteBuffer objectBuf = objectDescriptors.createBuffer();
        ByteBuffer imageBuf = imageDescriptors.createBuffer();

        for (int i = 0; i * size < objectBuf.capacity(); i++) {
            ByteBuffer descriptor = (ByteBuffer) objectBuf.position(i * size).limit((i + 1) * size).mark();
            int nearestNeighbor = naiveNearestNeighbor(descriptor, imageBuf);
            if (nearestNeighbor >= 0) {
                ptpairs.add(i);
                ptpairs.add(nearestNeighbor);
            }
        }
    }

    private void flannFindPairs(Mat objectDescriptors, Mat imageDescriptors) {
        int length = objectDescriptors.rows();
        flannIndex.build(imageDescriptors, indexParams, FLANN_DIST_HAMMING);
        flannIndex.knnSearch(objectDescriptors, indicesMat, distsMat, 2, searchParams);
        IntBuffer indicesBuf = indicesMat.createBuffer();
        IntBuffer distsBuf = distsMat.createBuffer();
        for (int i = 0; i < length; i++) {
            if (distsBuf.get(2 * i) < distanceThreshold * distsBuf.get(2 * i + 1)) {
                ptpairs.add(i);
                ptpairs.add(indicesBuf.get(2 * i));
            }
        }
    }

    private double[] locatePlanarObject(KeyPointVector objectKeypoints, Mat objectDescriptors,
                                        KeyPointVector imageKeypoints, Mat imageDescriptors, double[] srcCorners) {
        ptpairs.clear();
        if (useFLANN) {
            flannFindPairs(objectDescriptors, imageDescriptors);
        } else {
            findPairs(objectDescriptors, imageDescriptors);
        }
        int n = ptpairs.size() / 2;
        logger.info("筛选后共有" + n + "个吻合点");
        if (n < matchesMin) {
            return null;
        }

        pt1.resize(n);
        pt2.resize(n);
        mask.resize(n);
        FloatBuffer pt1Idx = pt1.createBuffer();
        FloatBuffer pt2Idx = pt2.createBuffer();
        for (int i = 0; i < n; i++) {
            Point2f p1 = objectKeypoints.get(ptpairs.get(2 * i)).pt();
            pt1Idx.put(2 * i, p1.x());
            pt1Idx.put(2 * i + 1, p1.y());
            Point2f p2 = imageKeypoints.get(ptpairs.get(2 * i + 1)).pt();
            pt2Idx.put(2 * i, p2.x());
            pt2Idx.put(2 * i + 1, p2.y());
        }

        H = findHomography(pt1, pt2, CV_RANSAC, ransacReprojThreshold, mask, 2000, 0.995);
        if (H.empty() || countNonZero(mask) < matchesMin) {
            return null;
        }

        double[] h = (double[]) H.createIndexer(false).array();
        double[] dstCorners = new double[srcCorners.length];
        for (int i = 0; i < srcCorners.length / 2; i++) {
            double x = srcCorners[2 * i], y = srcCorners[2 * i + 1];
            double Z = 1 / (h[6] * x + h[7] * y + h[8]);
            double X = (h[0] * x + h[1] * y + h[2]) * Z;
            double Y = (h[3] * x + h[4] * y + h[5]) * Z;
            dstCorners[2 * i] = X;
            dstCorners[2 * i + 1] = Y;
        }
        return dstCorners;
    }

    public static Scalar randColor() {
        int b, g, r;
        b = ThreadLocalRandom.current().nextInt(0, 255);
        g = ThreadLocalRandom.current().nextInt(0, 255);
        r = ThreadLocalRandom.current().nextInt(0, 255);
        return new Scalar(b, g, r, 0);
    }

    public FindResult getAKAZEFindResult(File temFile, File beforeFile) throws IOException {
        IplImage object = cvLoadImage(temFile.getAbsolutePath(), IMREAD_GRAYSCALE);
        IplImage image = cvLoadImage(beforeFile.getAbsolutePath(), IMREAD_GRAYSCALE);
        logger.info("原图宽：" + image.width());
        logger.info("原图高：" + image.height());
        if (object == null || image == null) {
            logger.error("读取图片失败！");
            temFile.delete();
            beforeFile.delete();
            return null;
        }

        IplImage correspond = IplImage.create(image.width() + object.width(), image.height(), 8, 1);
        cvSetImageROI(correspond, cvRect(0, 0, image.width(), correspond.height()));
        cvCopy(image, correspond);
        cvSetImageROI(correspond, cvRect(image.width(), 0, object.width(), object.height()));
        cvCopy(object, correspond);
        cvResetImageROI(correspond);

        objectImage = object;
        useFLANN = true;
        ransacReprojThreshold = 3;
        init();

        long start = System.currentTimeMillis();
        double[] dst_corners = find(image);
        FindResult findResult = new FindResult();
        findResult.setTime((int) (System.currentTimeMillis() - start));
        logger.info("特征匹配时间: " + (System.currentTimeMillis() - start) + " ms");

        IplImage correspondColor = IplImage.create(correspond.width(), correspond.height(), 8, 3);
        cvCvtColor(correspond, correspondColor, CV_GRAY2BGR);
        cvSetImageROI(correspondColor, cvRect(0, 0, image.width(), correspondColor.height()));
        cvCopy(cvLoadImage(beforeFile.getAbsolutePath(), IMREAD_COLOR), correspondColor);
        cvSetImageROI(correspondColor, cvRect(image.width(), 0, object.width(), object.height()));
        cvCopy(cvLoadImage(temFile.getAbsolutePath(), IMREAD_COLOR), correspondColor);
        cvResetImageROI(correspondColor);

        if (dst_corners != null) {
            int resultX = 0;
            int resultY = 0;
            for (int i = 0; i < 4; i++) {
                int j = (i + 1) % 4;
                int x1 = (int) Math.round(dst_corners[2 * i]);
                int y1 = (int) Math.round(dst_corners[2 * i + 1]);
                int x2 = (int) Math.round(dst_corners[2 * j]);
                int y2 = (int) Math.round(dst_corners[2 * j + 1]);
                line(cvarrToMat(correspondColor), new Point(x1, y1),
                        new Point(x2, y2),
                        Scalar.RED, 2, CV_AA, 0);
                if (i == 0) {
                    resultX = (x1 + x2) / 2;
                }
                if (i == 1) {
                    resultY = (y1 + y2) / 2;
                }
            }
            if (resultX == 0 && resultY == 0) {
                temFile.delete();
                beforeFile.delete();
                return null;
            }
            findResult.setX(resultX);
            findResult.setY(resultY);
            logger.info("结果坐标为（" + resultX + "," + resultY + ")");
        }

        if (objectKeypoints != null) {
            for (int i = 0; i < objectKeypoints.size(); i++) {
                KeyPoint r = objectKeypoints.get(i);
                Point center = new Point(Math.round(r.pt().x()) + image.width(), Math.round(r.pt().y()));
                int radius = Math.round(r.size() / 2);
                circle(cvarrToMat(correspondColor), center, radius, randColor(), 1, CV_AA, 0);
            }
        }

        if (imageKeypoints != null) {
            for (int i = 0; i < imageKeypoints.size(); i++) {
                KeyPoint r = imageKeypoints.get(i);
                Point center = new Point(Math.round(r.pt().x()), Math.round(r.pt().y()));
                int radius = Math.round(r.size() / 2);
                circle(cvarrToMat(correspondColor), center, radius, randColor(), 1, CV_AA, 0);
            }
        }

        for (int i = 0; i < ptpairs.size(); i += 2) {
            Point2f pt1 = objectKeypoints.get(ptpairs.get(i)).pt();
            Point2f pt2 = imageKeypoints.get(ptpairs.get(i + 1)).pt();
            line(cvarrToMat(correspondColor), new Point(Math.round(pt1.x()) + image.width(), Math.round(pt1.y())),
                    new Point(Math.round(pt2.x()), Math.round(pt2.y())),
                    randColor(), 1, CV_AA, 0);
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        File folder = new File("imageFiles" + File.separator
                + sf.format(Calendar.getInstance().getTimeInMillis()));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File fileName = new File(folder.getPath() + File.separator +
                UUID.randomUUID() + ".jpg");
        cvSaveImage(fileName.getAbsolutePath(), correspondColor);
        findResult.setUrl(fileTool.upload(fileName));
        temFile.delete();
        beforeFile.delete();
        return findResult;
    }
}