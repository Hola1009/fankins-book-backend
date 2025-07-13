package com.fankins.book.oss.biz.service.impl;

import com.fankins.book.oss.biz.infrasturcture.storage.config.MinioProperties;
import com.fankins.book.oss.biz.service.FileService;
import com.fankins.book.oss.biz.infrasturcture.storage.FileStrategy;
import com.fankinsbook.framework.common.response.Response;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RefreshScope
public class FileServiceImpl implements FileService {

    @Resource
    private FileStrategy fileStrategy;

    @Override
    public Response<?> uploadFile(MultipartFile file) {
        // 上传文件
        String url = fileStrategy.uploadFile(file);
        return Response.success(url);
    }
}