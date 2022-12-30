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
package org.cloud.sonic.folder.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileAlreadyExistsException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

/**
 * @author ZhouYiXun
 * @des 合并录像工具类
 * @date 2021/8/18 20:36
 */
@Component
@RefreshScope
public class FileTool {
    private final Logger logger = LoggerFactory.getLogger(FileTool.class);
    @Value("${gateway.host}")
    private String host;

    /**
     * @param folderName 文件夹
     * @param file
     * @return java.lang.String
     * @author ZhouYiXun
     * @des 上传
     * @date 2021/8/18 20:41
     */
    public String upload(String folderName, MultipartFile file) throws IOException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        File folder = new File(folderName + File.separator
                + sf.format(Calendar.getInstance().getTimeInMillis()));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        //防止文件重名
        File local = new File(folder.getPath() + File.separator +
                UUID.randomUUID() + file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".")));
        try {
            file.transferTo(local.getAbsoluteFile());
        } catch (FileAlreadyExistsException e) {
            logger.error(e.getMessage());
        }
        host = host.replace(":80/", "/");
        return host + "/api/folder/" + local.getPath().replaceAll("\\\\", "/");
    }

    public String uploadV2(String folderName, MultipartFile file) throws IOException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        File folder = new File(folderName + File.separator
                + sf.format(Calendar.getInstance().getTimeInMillis()));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        //防止文件重名
        File local = new File(folder.getPath() + File.separator +
                UUID.randomUUID() + file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".")));
        try {
            file.transferTo(local.getAbsoluteFile());
        } catch (FileAlreadyExistsException e) {
            logger.error(e.getMessage());
        }
        return local.getPath().replaceAll("\\\\", "/");
    }

    /**
     * @param file
     * @return void
     * @author ZhouYiXun
     * @des 删除文件
     * @date 2021/8/18 23:27
     */
    public void deleteDir(File file) {
        if (!file.exists()) {
            logger.info("文件不存在");
            return;
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                deleteDir(f);
            } else {
                f.delete();
            }
        }
        file.delete();
    }

    /**
     * @param uuid
     * @param fileName
     * @param totalCount
     * @return java.io.File
     * @author ZhouYiXun
     * @des
     * @date 2021/8/18 20:14
     */
    public String merge(String uuid, String fileName, int totalCount) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        File files = new File("recordFiles" + File.separator
                + sf.format(Calendar.getInstance().getTimeInMillis()));
        if (!files.exists()) {
            files.mkdirs();
        }
        //结果file
        File file = new File(files.getPath() + File.separator + fileName);
        try {
            RandomAccessFile target = new RandomAccessFile(file, "rw");
            //获取碎片文件夹
            File uuidFolder = new File("recordFiles" + File.separator + uuid);
            int waitTime = 0;
            int fileCount = uuidFolder.listFiles().length;
            //如果碎片还没齐全，进行等待一段时间
            while (fileCount < totalCount && waitTime < 20) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                waitTime++;
            }
            for (int i = 0; i < fileCount; i++) {
                //开始读取碎片文件
                String newName = fileName.substring(0, fileName.indexOf(".mp4")) + "-" + i + ".mp4";
                File patchFile = new File(uuidFolder.getPath() + File.separator + newName);
                if (!patchFile.exists()) {
                    continue;
                }
                RandomAccessFile readFile = new RandomAccessFile(patchFile, "r");
                long readSize = readFile.length();
                //每次读取字节数，不用设置太大，防止内存溢出
                byte[] bytes = new byte[1024];
                int len;
                while ((len = readFile.read(bytes)) != -1) {
                    //如果文件长度大于本次读取，直接写入
                    if (readSize > len) {
                        target.write(bytes, 0, len);
                        //读完要减去本次读取len
                        readSize -= len;
                    } else {
                        //小于本次读取说明是余数，直接写入剩余的readSize即可
                        target.write(bytes, 0, (int) readSize);
                    }
                }
                readFile.close();
                patchFile.delete();
            }
            target.close();
            uuidFolder.delete();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        host = host.replace(":80/", "/");
        return host + "/api/folder/" + file.getPath();
    }
}
