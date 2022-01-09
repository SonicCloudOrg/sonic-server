package org.cloud.sonic.folder.cv;

import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_features2d.FlannBasedMatcher;
import org.bytedeco.opencv.opencv_xfeatures2d.SIFT;
import org.cloud.sonic.folder.models.FindResult;
import org.cloud.sonic.folder.tools.FileTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.bytedeco.opencv.global.opencv_features2d.drawMatchesKnn;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

/**
 * @author ZhouYiXun
 * @des sift算法
 * @date 2022/1/4 21:49
 */
@Component
public class SIFTFinder {
    private final Logger logger = LoggerFactory.getLogger(SIFTFinder.class);
    @Autowired
    private FileTool fileTool;

    public FindResult getSIFTFindResult(File temFile, File beforeFile) throws IOException {
        Mat image01 = imread(beforeFile.getAbsolutePath());
        Mat image02 = imread(temFile.getAbsolutePath());

        Mat image1 = new Mat();
        Mat image2 = new Mat();
        cvtColor(image01, image1, COLOR_BGR2GRAY);
        cvtColor(image02, image2, COLOR_BGR2GRAY);

        KeyPointVector keyPointVector1 = new KeyPointVector();
        KeyPointVector keyPointVector2 = new KeyPointVector();
        Mat image11 = new Mat();
        Mat image22 = new Mat();

        long start = System.currentTimeMillis();
        SIFT sift = SIFT.create();
        sift.detectAndCompute(image1, image1, keyPointVector1, image11);
        sift.detectAndCompute(image2, image2, keyPointVector2, image22);

        FlannBasedMatcher flannBasedMatcher = new FlannBasedMatcher();
        DMatchVectorVector matchPoints = new DMatchVectorVector();

        flannBasedMatcher.knnMatch(image11, image22, matchPoints, 2);
        logger.info("处理前共有匹配数：" + matchPoints.size());
        DMatchVectorVector goodMatches = new DMatchVectorVector();

        List<Integer> xs = new ArrayList<>();
        List<Integer> ys = new ArrayList<>();
        for (long i = 0; i < matchPoints.size(); i++) {
            if (matchPoints.get(i).size() >= 2) {
                DMatch match1 = matchPoints.get(i).get(0);
                DMatch match2 = matchPoints.get(i).get(1);

                if (match1.distance() <= 0.6 * match2.distance()) {
                    xs.add((int) keyPointVector1.get(match1.queryIdx()).pt().x());
                    ys.add((int) keyPointVector1.get(match1.queryIdx()).pt().y());
                    goodMatches.push_back(matchPoints.get(i));
                }
            }
        }
        logger.info("处理后匹配数：" + goodMatches.size());
        if (goodMatches.size() <= 4) {
            temFile.delete();
            beforeFile.delete();
            return null;
        }
        FindResult findResult = new FindResult();
        findResult.setTime((int) (System.currentTimeMillis() - start));
        Mat result = new Mat();

        drawMatchesKnn(image01, keyPointVector1, image02, keyPointVector2, goodMatches, result);

        int resultX = majorityElement(xs);
        int resultY = majorityElement(ys);
        findResult.setX(resultX);
        findResult.setY(resultY);
        logger.info("结果坐标为（" + resultX + "," + resultY + ")");
        circle(result, new Point(resultX, resultY), 5, Scalar.RED, 10, CV_AA, 0);
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        File folder = new File("imageFiles" + File.separator
                + sf.format(Calendar.getInstance().getTimeInMillis()));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File fileName = new File(folder.getPath() + File.separator +
                UUID.randomUUID() + ".jpg");
        imwrite(fileName.getAbsolutePath(), result);
        findResult.setUrl(fileTool.upload(fileName));
        temFile.delete();
        beforeFile.delete();
        return findResult;
    }

    public static int majorityElement(List<Integer> nums) {
        double j;
        Collections.sort(nums);
        int size = nums.size();
        if (size % 2 == 1) {
            j = nums.get((size - 1) / 2);
        } else {
            j = (nums.get(size / 2 - 1) + nums.get(size / 2) + 0.0) / 2;
        }
        return (int) j;
    }
}
