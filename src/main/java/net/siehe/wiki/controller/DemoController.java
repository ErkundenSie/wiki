package net.siehe.wiki.controller;

import net.siehe.wiki.domain.Demo;
import net.siehe.wiki.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Resource
    private DemoService demoService;
    //改一点代码确保新代码生效
    @GetMapping("/list")
    public List<Demo> list() {
        return demoService.list();
    }
}
