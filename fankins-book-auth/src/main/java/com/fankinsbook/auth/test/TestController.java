package com.fankinsbook.auth.test;

import com.fankinsbook.framework.biz.stater.operationlog.aspect.ApiOperationLog;
import com.fankinsbook.framework.common.response.Response;
import org.mybatis.spring.annotation.MapperScan;
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
    public Response<User> test2(@RequestBody User user) {
        return Response.success(user);
    }
}
