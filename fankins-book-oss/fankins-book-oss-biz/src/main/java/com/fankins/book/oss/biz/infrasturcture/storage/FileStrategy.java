package com.fankins.book.oss.biz.infrasturcture.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStrategy {

    /**
     * 文件上传
     *
     * @param file
     */
    String uploadFile(MultipartFile file);

}
