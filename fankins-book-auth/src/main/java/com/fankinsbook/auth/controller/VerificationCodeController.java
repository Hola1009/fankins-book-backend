package com.fankinsbook.auth.controller;

import com.fankinsbook.auth.model.vo.verificationCode.SendVerificationCodeReqVO;
import com.fankinsbook.auth.service.VerificationCodeService;
import com.fankinsbook.framework.biz.stater.operationlog.aspect.ApiOperationLog;
import com.fankinsbook.framework.common.response.Response;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class VerificationCodeController {

    @Resource
    private VerificationCodeService verificationCodeService;

    @PostMapping("/verification/code/send")
    @ApiOperationLog(description = "发送短信验证码")
    public Response<?> send(@Validated @RequestBody SendVerificationCodeReqVO sendVerificationCodeReqVO) {
        return verificationCodeService.send(sendVerificationCodeReqVO);
    }

}