package com.fankinsbook.auth.test;

import com.fankinsbook.framework.biz.operationlog.aspect.ApiOperationLog;
import com.fankinsbook.framework.common.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/hola1009">fancier</a>
 **/
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/mes")
    @ApiOperationLog(description = "测试接口")
    public Response<String> getMessage() {
        return Response.success("hello world");
    }
}
