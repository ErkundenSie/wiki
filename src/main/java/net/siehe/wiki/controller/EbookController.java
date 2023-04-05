package net.siehe.wiki.controller;

import net.siehe.wiki.domain.Ebook;
import net.siehe.wiki.resp.CommonResp;
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
    //修改统一返回类型CommonResp
    @GetMapping("/list")
    public CommonResp list() {
        CommonResp<List<Ebook>> objectCommonResp = new CommonResp<>();
        List<Ebook> list = ebookService.list();
        objectCommonResp.setContent(list);
        return objectCommonResp;
    }
}
