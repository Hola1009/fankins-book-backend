package com.fankinsbook.auth.service;

import com.fankinsbook.auth.model.vo.user.UpdatePasswordReqVO;
import com.fankinsbook.auth.model.vo.user.UserLoginReqVO;
import com.fankinsbook.framework.common.response.Response;

/**
 * @author <a href="https://github.com/hola1009">fancier</a>
 **/
public interface UserService {

    /**
     * 登录与注册
     * @param userLoginReqVO
     * @return
     */
    Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO);

    /**
     * 退出登录
     * @return
     */
    Response<?> logout();

    /**
     * 修改密码
     * @param updatePasswordReqVO
     * @return
     */
    Response<?> updatePassword(UpdatePasswordReqVO updatePasswordReqVO);

}