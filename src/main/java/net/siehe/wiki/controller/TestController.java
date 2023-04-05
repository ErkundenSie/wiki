package net.siehe.wiki.controller;

import org.springframework.web.bind.annotation.*;

//RestController返回字符串 Controller返回页面，一般前后端分离用前者
@RestController
public class TestController {
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
        return "Hello World";
    }
}
