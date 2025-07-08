package com.fankinsbook.framework.common.exception;

import lombok.Getter;
import lombok.Setter;
/**
 * @author <a href="https://github.com/hola1009">fancier</a>
 **/
@Getter
@Setter
public class BizException extends RuntimeException {
    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;

    public BizException(BaseExceptionInfoInterface baseExceptionInfoInterface) {
        this.errorCode = baseExceptionInfoInterface.getErrorCode();
        this.errorMessage = baseExceptionInfoInterface.getErrorMessage();
    }
}