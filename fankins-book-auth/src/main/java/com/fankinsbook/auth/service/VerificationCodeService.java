package com.fankinsbook.auth.service;

import com.fankinsbook.auth.model.vo.verificationCode.SendVerificationCodeReqVO;
import com.fankinsbook.framework.common.response.Response;

public interface VerificationCodeService {

    /**
     * 发送短信验证码
     *
     * @param sendVerificationCodeReqVO
     * @return
     */
    Response<?> send(SendVerificationCodeReqVO sendVerificationCodeReqVO);
}