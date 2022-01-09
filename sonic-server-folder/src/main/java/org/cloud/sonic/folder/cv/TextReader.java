package org.cloud.sonic.folder.cv;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.TessBaseAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

import static org.bytedeco.leptonica.global.lept.pixRead;

/**
 * @author ZhouYiXun
 * @des 文字识别（即将弃用）
 * @date 2022/1/4 21:49
 */
@Component
public class TextReader {
    private final Logger logger = LoggerFactory.getLogger(TextReader.class);

    public String getTessResult(File file, String language) throws Exception {
        BytePointer outText = null;
        TessBaseAPI api = new TessBaseAPI();
        String result = "";
        if (api.Init("language", language) != 0) {
            logger.info("找不到语言包！");
            return result;
        }
        try {
            PIX image = pixRead(file.getAbsolutePath());
            api.SetImage(image);
            outText = api.GetUTF8Text();
            result = outText.getString();
        } finally {
            file.delete();
            api.End();
            outText.deallocate();
        }
        return result;
    }
}
