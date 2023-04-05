package net.siehe.wiki.controller;

import net.siehe.wiki.domain.Ebook;
import net.siehe.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Resource
    private EbookService ebookService;
    //改一点代码确保新代码生效
    @GetMapping("/list")
    public List<Ebook> list() {
        return ebookService.list();
    }
}
