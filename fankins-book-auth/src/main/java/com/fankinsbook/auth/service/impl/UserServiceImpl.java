package com.fankinsbook.auth.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.fankins.book.framework.stater.biz.context.holder.LoginUserContextHolder;
import com.fankinsbook.auth.domain.dataobject.RoleDO;
import com.fankinsbook.auth.domain.dataobject.UserDO;
import com.fankinsbook.auth.domain.dataobject.UserRoleRelDO;
import com.fankinsbook.auth.domain.mapper.RoleDOMapper;
import com.fankinsbook.auth.domain.mapper.UserDOMapper;
import com.fankinsbook.auth.domain.mapper.UserRoleRelDOMapper;
import com.fankinsbook.auth.model.vo.user.UpdatePasswordReqVO;
import com.fankinsbook.auth.model.vo.user.UserLoginReqVO;
import com.fankinsbook.auth.service.UserService;
import com.fankinsbook.auth.shared.constant.RedisKeyConstants;
import com.fankinsbook.auth.shared.constant.RoleConstants;
import com.fankinsbook.auth.shared.enums.LoginTypeEnum;
import com.fankinsbook.auth.shared.enums.ResponseCodeEnum;
import com.fankinsbook.framework.common.exception.BizException;
import com.fankinsbook.framework.common.response.Response;
import com.fankinsbook.framework.common.shared.enums.DeletedEnum;
import com.fankinsbook.framework.common.shared.enums.StatusEnum;
import com.fankinsbook.framework.common.shared.util.JsonUtils;
import com.google.common.base.Preconditions;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserDOMapper userDOMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserRoleRelDOMapper userRoleRelDOMapper;

    @Resource
    private RoleDOMapper roleDOMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private PasswordEncoder passwordEncoder;


    /**
     * 登录与注册
     *
     * @param userLoginReqVO
     * @return
     */
    @Override
    public Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO) {
        String phone = userLoginReqVO.getPhone();
        Integer type = userLoginReqVO.getType();

        LoginTypeEnum loginTypeEnum = LoginTypeEnum.valueOf(type);

        Long userId = null;

        // 判断登录类型
        switch (Objects.requireNonNull(loginTypeEnum)) {
            case VERIFICATION_CODE: // 验证码登录
                String verificationCode = userLoginReqVO.getCode();

                // 校验入参验证码是否为空
                Preconditions.checkArgument(StringUtils.isNotBlank(verificationCode), "验证码不能为空");

                // 构建验证码 Redis Key
                String key = RedisKeyConstants.buildVerificationCodeKey(phone);
                // 查询存储在 Redis 中该用户的登录验证码
                String sentCode = (String) redisTemplate.opsForValue().get(key);

                // 判断用户提交的验证码，与 Redis 中的验证码是否一致
                if (!StringUtils.equals(verificationCode, sentCode)) {
                    throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
                }

                // 通过手机号查询记录
                UserDO userDO = userDOMapper.selectByPhone(phone);

                log.info("==> 用户是否注册, phone: {}, userDO: {}", phone, JsonUtils.toJsonString(userDO));

                // 判断是否注册
                if (Objects.isNull(userDO)) {
                    // 若此用户还没有注册，系统自动注册该用户
                    userId = registerUser(phone);
                } else {
                    // 已注册，则获取其用户 ID
                    userId = userDO.getId();
                }
                break;
            case PASSWORD: // 密码登录
                // todo

                break;
            default:
                break;
        }

        // SaToken 登录用户，并返回 token 令牌
        StpUtil.login(userId);

        // 获取 Token 令牌
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 返回 Token 令牌
        return Response.success(tokenInfo.tokenValue);
    }

    @Override
    public Response<?> logout() {
        // 获取当前登录用户 ID
        Long userId = LoginUserContextHolder.getUserId();
        // 退出登录 (指定用户 ID)
        StpUtil.logout(userId);
        return Response.success();
    }


    /**
     * 系统自动注册用户
     * @param phone
     * @return
     */
    private Long registerUser(String phone) {
        return transactionTemplate.execute(status -> {
            try {
                // 获取全局自增的小哈书 ID
                Long fankinsId = redisTemplate.opsForValue().increment(RedisKeyConstants.FANKINS_BOOK_ID_GENERATOR_KEY);

                UserDO userDO = UserDO.builder()
                        .phone(phone)
                        .fankinsId(String.valueOf(fankinsId)) // 自动生成小红书号 ID
                        .nickname("小番薯" + fankinsId) // 自动生成昵称, 如：小红薯10000
                        .status(StatusEnum.ENABLE.getValue()) // 状态为启用
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(DeletedEnum.NO.getValue()) // 逻辑删除
                        .build();

                // 添加入库
                userDOMapper.insert(userDO);

                // 获取刚刚添加入库的用户 ID
                Long userId = userDO.getId();

                // 给该用户分配一个默认角色
                UserRoleRelDO userRoleRelDO = UserRoleRelDO.builder()
                        .userId(userId)
                        .roleId(RoleConstants.COMMON_USER_ROLE_ID)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(DeletedEnum.NO.getValue())
                        .build();
                userRoleRelDOMapper.insert(userRoleRelDO);

                RoleDO roleDO = roleDOMapper.selectByPrimaryKey(RoleConstants.COMMON_USER_ROLE_ID);

                // 将该用户的角色 ID 存入 Redis 中，指定初始容量为 1，这样可以减少在扩容时的性能开销
                List<String> roles = new ArrayList<>(1);
                roles.add(roleDO.getRoleKey());

                String userRolesKey = RedisKeyConstants.buildUserRoleKey(userId);
                redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roles));

                return userId;
            } catch (Exception e) {
                status.setRollbackOnly(); // 标记事务为回滚
                log.error("==> 系统注册用户异常: ", e);
                throw new BizException(ResponseCodeEnum.SYSTEM_ERROR);
            }
        });
    }

    @Override
    public Response<?> updatePassword(UpdatePasswordReqVO updatePasswordReqVO) {
        // 新密码
        String newPassword = updatePasswordReqVO.getNewPassword();
        // 密码加密
        String encodePassword = passwordEncoder.encode(newPassword);

        // 获取当前请求对应的用户 ID
        Long userId = LoginUserContextHolder.getUserId();

        UserDO userDO = UserDO.builder()
                .id(userId)
                .password(encodePassword)
                .updateTime(LocalDateTime.now())
                .build();
        // 更新密码
        userDOMapper.updateByPrimaryKeySelective(userDO);

        return Response.success();
    }

}