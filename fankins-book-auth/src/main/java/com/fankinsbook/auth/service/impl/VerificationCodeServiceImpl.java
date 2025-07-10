package com.fankinsbook.auth.service.impl;

@Service
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 发送短信验证码
     *
     * @param sendVerificationCodeReqVO
     * @return
     */
    @Override
    public Response<?> send(SendVerificationCodeReqVO sendVerificationCodeReqVO) {
        // 手机号
        String phone = sendVerificationCodeReqVO.getPhone();

        // 构建验证码 redis key
        String key = RedisKeyConstants.buildVerificationCodeKey(phone);

        // 判断是否已发送验证码
        boolean isSent = redisTemplate.hasKey(key);
        if (isSent) {
            // 若之前发送的验证码未过期，则提示发送频繁
            throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_SEND_FREQUENTLY);
        }

        // 生成 6 位随机数字验证码
        String verificationCode = RandomUtil.randomNumbers(6);

        // todo: 调用第三方短信发送服务

        log.info("==> 手机号: {}, 已发送验证码：【{}】", phone, verificationCode);

        // 存储验证码到 redis, 并设置过期时间为 3 分钟
        redisTemplate.opsForValue().set(key, verificationCode, 3, TimeUnit.MINUTES);

        return Response.success();
    }
}
