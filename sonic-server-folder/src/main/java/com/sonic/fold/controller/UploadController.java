package com.sonic.fold.controller;

import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.fold.tools.FileTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Calendar;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private FileTool fileTool;

    @WebAspect
    @PostMapping
    public RespModel<String> uploadFiles(@RequestParam(name = "file") MultipartFile file,
                                         @RequestParam(name = "type") String type) throws IOException {
        String url = fileTool.upload(type, Calendar.getInstance().getTimeInMillis(), file);
        if (url != null) {
            return new RespModel(RespEnum.UPLOAD_OK, url);
        } else {
            return new RespModel(RespEnum.UPLOAD_ERROR);
        }
    }

    @WebAspect
    @PostMapping("/recordFiles")
    public RespModel<String> uploadRecord(@RequestParam(name = "file") MultipartFile file,
                                          @RequestParam(name = "uuid") String uuid,
                                          @RequestParam(name = "num") int num,
                                          @RequestParam(name = "total") int total,
                                          @RequestParam(name = "time") String time) throws IOException {
        //先创建对应uuid的文件夹
        File uuidFolder = new File("recordFiles" + File.separator + uuid);
        if (!uuidFolder.exists()) {
            uuidFolder.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        String newName = fileName.substring(0, fileName.indexOf(".mp4")) + "-" + num + ".mp4";
        File local = new File(uuidFolder.getPath() + File.separator + newName);
        RespModel<String> responseModel;
        try {
            file.transferTo(local.getAbsoluteFile());
            responseModel = new RespModel(RespEnum.UPLOAD_OK);
        } catch (FileAlreadyExistsException e) {
            responseModel = new RespModel(RespEnum.UPLOAD_ERROR);
        }
        //如果当前是最后一个，就开始合并录像文件
        if (num == total - 1) {
            responseModel.setData(fileTool.merge(uuid, file.getOriginalFilename(), total, time));
        }
        return responseModel;
    }
}
