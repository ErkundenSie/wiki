package net.siehe.wiki.controller;

import net.siehe.wiki.domain.Ebook;
import net.siehe.wiki.req.EbookReq;
import net.siehe.wiki.resp.CommonResp;
import net.siehe.wiki.resp.EbookResp;
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
    public CommonResp list(EbookReq ebookReq) {
        //并不是所有时候都返回Ebook，可能只是返回一部分，如含有密码等需要过滤 定义EbookResp类
        //在controller中不要出现domain实体ebook
        CommonResp<List<EbookResp>> objectCommonResp = new CommonResp<>();
        List<EbookResp> list = ebookService.list(ebookReq);
        objectCommonResp.setContent(list);
        return objectCommonResp;
    }
}
