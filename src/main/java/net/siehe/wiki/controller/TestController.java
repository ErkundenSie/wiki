package net.siehe.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

//设置Auto Import==》Optimize imports on the fly打钩自动删除没用的
//import java.util.Map;

//RestController返回字符串 Controller返回页面，一般前后端分离用前者
@RestController
public class TestController {
    //应用自定义配置项设置默认值TEST
//    @Value("${test.hello:TEST}")
    @Value("${test.hello:TEST}")
    private String testHello;
    /**
     * 常用GET查,POST增,PUT改,DELETE删
     * 传统/user?id=1
     * rest风格/user/1
     * @return
     */
//    @PutMapping
//    @DeleteMapping
//    @PostMapping
//    @RequestMapping(value = "/user/1",method = RequestMethod.GET)
//    @RequestMapping(value = "/user/1",method = RequestMethod.DELETE)
    //浏览器直接访问发送的是GET请求用POST请求会报405错误
    @GetMapping("/hello")
    public String hello() {
        return "Hello World , "+testHello;
    }
    @PostMapping("/hello/post")
    public String helloPost(String name) {
        return "Hello World! Post" + name;
    }
}
