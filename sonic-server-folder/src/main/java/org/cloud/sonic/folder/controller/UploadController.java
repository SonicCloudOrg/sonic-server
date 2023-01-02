/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.cloud.sonic.folder.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.common.config.WebAspect;
import org.cloud.sonic.common.http.RespEnum;
import org.cloud.sonic.common.http.RespModel;
import org.cloud.sonic.folder.tools.FileTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

@Api(tags = "文件上传")
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {
    @Autowired
    private FileTool fileTool;

    @WebAspect
    @ApiOperation(value = "上传文件", notes = "上传文件到服务器")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "file", value = "文件", dataTypeClass = MultipartFile.class),
            @ApiImplicitParam(name = "type", value = "文件类型(只能为keepFiles、imageFiles、recordFiles、logFiles、packageFiles)", dataTypeClass = String.class),
    })
    @PostMapping
    public RespModel<String> uploadFiles(@RequestParam(name = "file") MultipartFile file,
                                         @RequestParam(name = "type") String type) throws IOException {
        String url = fileTool.upload(type, file);
        if (url != null) {
            return new RespModel(RespEnum.UPLOAD_OK, url);
        } else {
            return new RespModel(RespEnum.UPLOAD_FAIL);
        }
    }

    @WebAspect
    @ApiOperation(value = "上传文件v2", notes = "上传文件到服务器")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "file", value = "文件", dataTypeClass = MultipartFile.class),
            @ApiImplicitParam(name = "type", value = "文件类型(只能为keepFiles、imageFiles、recordFiles、logFiles、packageFiles)", dataTypeClass = String.class),
    })
    @PostMapping("/v2")
    public RespModel<String> uploadFilesV2(@RequestParam(name = "file") MultipartFile file,
                                           @RequestParam(name = "type") String type) throws IOException {
        String url = fileTool.uploadV2(type, file);
        if (url != null) {
            return new RespModel(RespEnum.UPLOAD_OK, url);
        } else {
            return new RespModel(RespEnum.UPLOAD_FAIL);
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
    @PostMapping(value = "/recordFiles")
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
            responseModel = new RespModel<>(RespEnum.UPLOAD_OK);
        } catch (FileAlreadyExistsException e) {
            responseModel = new RespModel<>(RespEnum.UPLOAD_FAIL);
        }
        //如果当前是最后一个，就开始合并录像文件
        if (index == total - 1) {
            responseModel.setData(fileTool.merge(uuid, file.getOriginalFilename(), total));
        }
        return responseModel;
    }
}
