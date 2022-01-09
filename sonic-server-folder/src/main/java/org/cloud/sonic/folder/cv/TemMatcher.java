package org.cloud.sonic.folder.cv;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.opencv.opencv_core.*;
import org.cloud.sonic.folder.models.FindResult;
import org.cloud.sonic.folder.tools.FileTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

/**
 * @author ZhouYiXun
 * @des 模板匹配
 * @date 2022/1/4 21:49
 */
@Component
public class TemMatcher {
    private final Logger logger = LoggerFactory.getLogger(TemMatcher.class);
    @Autowired
    private FileTool fileTool;

    public FindResult getTemMatchResult(File temFile, File beforeFile) throws IOException {
        try {
            Mat sourceColor = imread(beforeFile.getAbsolutePath());
            Mat sourceGrey = new Mat(sourceColor.size(), CV_8UC1);
            cvtColor(sourceColor, sourceGrey, COLOR_BGR2GRAY);
            Mat template = imread(temFile.getAbsolutePath(), IMREAD_GRAYSCALE);
            Size size = new Size(sourceGrey.cols() - template.cols() + 1, sourceGrey.rows() - template.rows() + 1);
            Mat result = new Mat(size, CV_32FC1);

            long start = System.currentTimeMillis();
            matchTemplate(sourceGrey, template, result, TM_CCORR_NORMED);
            DoublePointer minVal = new DoublePointer();
            DoublePointer maxVal = new DoublePointer();
            Point min = new Point();
            Point max = new Point();
            minMaxLoc(result, minVal, maxVal, min, max, null);
            rectangle(sourceColor, new Rect(max.x(), max.y(), template.cols(), template.rows()), randColor(), 2, 0, 0);
            FindResult findResult = new FindResult();
            findResult.setTime((int) (System.currentTimeMillis() - start));
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            File folder = new File("imageFiles" + File.separator
                    + sf.format(Calendar.getInstance().getTimeInMillis()));
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File fileName = new File(folder.getPath() + File.separator +
                    UUID.randomUUID() + ".jpg");
            imwrite(fileName.getAbsolutePath(), sourceColor);
            findResult.setX(max.x() + template.cols() / 2);
            findResult.setY(max.y() + template.rows() / 2);
            findResult.setUrl(fileTool.upload(fileName));
            return findResult;
        } finally {
            temFile.delete();
            beforeFile.delete();
        }
    }

    public static Scalar randColor() {
        int b, g, r;
        b = ThreadLocalRandom.current().nextInt(0, 255 + 1);
        g = ThreadLocalRandom.current().nextInt(0, 255 + 1);
        r = ThreadLocalRandom.current().nextInt(0, 255 + 1);
        return new Scalar(b, g, r, 0);
    }
}