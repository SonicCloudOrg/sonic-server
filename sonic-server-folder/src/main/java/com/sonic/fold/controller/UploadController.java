package com.sonic.fold.controller;

import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.fold.tools.FileTool;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private FileTool fileTool;

    @WebAspect
    @ApiOperation(value = "上传文件", notes = "上传文件到服务器")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "file", value = "文件", dataTypeClass = MultipartFile.class),
            @ApiImplicitParam(name = "type", value = "文件类型(只能为keepFiles、imageFiles、recordFiles、logFiles)", dataTypeClass = String.class),
    })
    @PostMapping
    public RespModel<String> uploadFiles(@RequestParam(name = "file") MultipartFile file,
                                         @RequestParam(name = "type") String type) throws IOException {
        String url = fileTool.upload(type, file);
        if (url != null) {
            return new RespModel(RespEnum.UPLOAD_OK, url);
        } else {
            return new RespModel(RespEnum.UPLOAD_ERROR);
        }
    }

    @WebAspect
    @ApiOperation(value = "上传文件（录像分段上传）", notes = "上传文件到服务器")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "file", value = "文件", dataTypeClass = MultipartFile.class),
            @ApiImplicitParam(name = "uuid", value = "文件uuid", dataTypeClass = String.class),
            @ApiImplicitParam(name = "index", value = "当前index", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "total", value = "index总数", dataTypeClass = Integer.class),
    })
    @PostMapping("/recordFiles")
    public RespModel<String> uploadRecord(@RequestParam(name = "file") MultipartFile file,
                                          @RequestParam(name = "uuid") String uuid,
                                          @RequestParam(name = "index") int index,
                                          @RequestParam(name = "total") int total) throws IOException {
        //先创建对应uuid的文件夹
        File uuidFolder = new File("recordFiles" + File.separator + uuid);
        if (!uuidFolder.exists()) {
            uuidFolder.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        String newName = fileName.substring(0, fileName.indexOf(".mp4")) + "-" + index + ".mp4";
        File local = new File(uuidFolder.getPath() + File.separator + newName);
        RespModel<String> responseModel;
        try {
            file.transferTo(local.getAbsoluteFile());
            responseModel = new RespModel(RespEnum.UPLOAD_OK);
        } catch (FileAlreadyExistsException e) {
            responseModel = new RespModel(RespEnum.UPLOAD_ERROR);
        }
        //如果当前是最后一个，就开始合并录像文件
        if (index == total - 1) {
            responseModel.setData(fileTool.merge(uuid, file.getOriginalFilename(), total));
        }
        return responseModel;
    }
}
