package com.fankinsbook.auth.test;

import cn.dev33.satoken.stp.StpUtil;
import com.fankinsbook.framework.biz.stater.operationlog.aspect.ApiOperationLog;
import com.fankinsbook.framework.common.response.Response;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author <a href="https://github.com/hola1009">fancier</a>
 **/
@RestController
@MapperScan("com.fankinsbook.auth.domain.mapper")
@RequestMapping("/test")
public class TestController {
    @GetMapping("/mes")
    @ApiOperationLog(description = "测试接口")
    public Response<String> getMessage() {
        return Response.success("hello world");
    }

    @PostMapping("/test2")
    @ApiOperationLog(description = "测试接口2")
    public Response<User> test2(@RequestBody @Validated User user) {
        return Response.success(user);
    }

    // 测试登录，浏览器访问： http://localhost:8080/user/doLogin?username=zhang&password=123456
    @RequestMapping("/user/doLogin")
    public String doLogin(String username, String password) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if("zhang".equals(username) && "123456".equals(password)) {
            StpUtil.login(10001);
            return "登录成功";
        }
        return "登录失败";
    }

    // 查询登录状态，浏览器访问： http://localhost:8080/user/isLogin
    @RequestMapping("/user/isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }
}
