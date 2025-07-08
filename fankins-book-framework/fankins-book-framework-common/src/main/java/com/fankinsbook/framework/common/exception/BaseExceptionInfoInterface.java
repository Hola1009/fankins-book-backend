package com.fankinsbook.framework.common.exception;

/**
 * @author <a href="https://github.com/hola1009">fancier</a>
 **/
public interface BaseExceptionInfoInterface {

	// 获取异常码
    String getErrorCode();

	// 获取异常信息
    String getErrorMessage();
}