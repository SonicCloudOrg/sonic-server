package org.cloud.sonic.folder.controller;

import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.folder.tools.FileTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/files")
@Slf4j
public class FilesController {
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    @Autowired
    private FileTool fileTool;

    /**
     * @param day 文件保留天数
     * @return org.cloud.sonic.common.http.RespModel
     * @author ZhouYiXun
     * @des 删除本地文件
     * @date 2021/8/21 21:47
     */
    @WebAspect
    @DeleteMapping
    public RespModel<String> delete(@RequestParam(name = "day") int day) {
        long timeMillis = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        List<String> fileList = Arrays.asList("imageFiles", "recordFiles", "logFiles", "packageFiles");
        cachedThreadPool.execute(() -> {
            for (String fileType : fileList) {
                File f = new File(fileType);
                File[] type = f.listFiles();
                if (type != null) {
                    for (File dateFile : type) {
                        try {
                            if (timeMillis - sf.parse(dateFile.getName()).getTime()
                                    > day * 86400000L) {
                                log.info("clean begin! " + dateFile.getPath());
                                fileTool.deleteDir(dateFile);
                            }
                        } catch (ParseException e) {
                            log.info("Parse file name error, cause: " + dateFile.getPath());
                            log.error(e.getMessage());
                        }
                    }
                }
            }
        });
        return new RespModel<>(2000, "file.clean");
    }
}
