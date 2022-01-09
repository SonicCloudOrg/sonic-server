package org.cloud.sonic.folder.cv;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgproc.GaussianBlur;

/**
 * @author ZhouYiXun
 * @des 相似度比对
 * @date 2022/1/4 21:49
 */
@Component
public class SimilarityChecker {
    private final Logger logger = LoggerFactory.getLogger(SimilarityChecker.class);

    public double getSimilarMSSIMScore(File file1, File file2) {
        synchronized (SimilarityChecker.class) {
            Mat i1 = imread(file1.getAbsolutePath());
            Mat i2 = imread(file2.getAbsolutePath());
            if (i1.size().get() != i2.size().get()) {
                return 0;
            }
            double C1 = 6.5025, C2 = 58.5225;
            int d = opencv_core.CV_32F;
            Mat I1 = new Mat();
            Mat I2 = new Mat();
            i1.convertTo(I1, d);
            i2.convertTo(I2, d);
            Mat I2_2 = I2.mul(I2).asMat();
            Mat I1_2 = I1.mul(I1).asMat();
            Mat I1_I2 = I1.mul(I2).asMat();
            Mat mu1 = new Mat();
            Mat mu2 = new Mat();
            GaussianBlur(I1, mu1, new Size(11, 11), 1.5);
            GaussianBlur(I2, mu2, new Size(11, 11), 1.5);
            Mat mu1_2 = mu1.mul(mu1).asMat();
            Mat mu2_2 = mu2.mul(mu2).asMat();
            Mat mu1_mu2 = mu1.mul(mu2).asMat();
            Mat sigma1_2 = new Mat();
            Mat sigma2_2 = new Mat();
            Mat sigma12 = new Mat();
            GaussianBlur(I1_2, sigma1_2, new Size(11, 11), 1.5);
            sigma1_2 = subtract(sigma1_2, mu1_2).asMat();
            GaussianBlur(I2_2, sigma2_2, new Size(11, 11), 1.5);
            sigma2_2 = subtract(sigma2_2, mu2_2).asMat();
            GaussianBlur(I1_I2, sigma12, new Size(11, 11), 1.5);
            sigma12 = subtract(sigma12, mu1_mu2).asMat();
            Mat t1, t2, t3;
            t1 = add(multiply(2, mu1_mu2), Scalar.all(C1)).asMat();
            t2 = add(multiply(2, sigma12), Scalar.all(C2)).asMat();
            t3 = t1.mul(t2).asMat();
            t1 = add(add(mu1_2, mu2_2), Scalar.all(C1)).asMat();
            t2 = add(add(sigma1_2, sigma2_2), Scalar.all(C2)).asMat();
            t1 = t1.mul(t2).asMat();
            Mat ssim_map = new Mat();
            divide(t3, t1, ssim_map);
            Scalar mSsim = mean(ssim_map);
            file1.delete();
            file2.delete();
            return new BigDecimal((mSsim.get(0) + mSsim.get(1) + mSsim.get(2)) / 3).setScale(2, RoundingMode.DOWN).doubleValue();
        }
    }
}