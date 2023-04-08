package net.siehe.wiki.controller;

import net.siehe.wiki.req.EbookQueryReq;
import net.siehe.wiki.req.EbookSaveReq;
import net.siehe.wiki.resp.CommonResp;
import net.siehe.wiki.resp.EbookQueryResp;
import net.siehe.wiki.resp.PageResp;
import net.siehe.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Resource
    private EbookService ebookService;
    //修改统一返回类型CommonResp
    //@Valid 开启之前在PageReq的校验规则
    @GetMapping("/list")
    public CommonResp list(@Valid EbookQueryReq ebookReq) {
        //并不是所有时候都返回Ebook，可能只是返回一部分，如含有密码等需要过滤 定义EbookResp类
        //在controller中不要出现domain实体ebook
        CommonResp<PageResp<EbookQueryResp>> objectCommonResp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(ebookReq);
        objectCommonResp.setContent(list);
        return objectCommonResp;
    }

    /**
     * 新增或保存前端返回的数据
     * 用PostMapping加@RequestBody对应json方式的提交，form方式不用
     * @param ebookReq
     * @return
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody EbookSaveReq ebookReq) {
        CommonResp objectCommonResp = new CommonResp();//不需要返回直接不用泛型
        ebookService.save(ebookReq);
        return objectCommonResp;
    }

    @DeleteMapping ("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp objectCommonResp = new CommonResp();//不需要返回直接不用泛型
        ebookService.delete(id);
        return objectCommonResp;
    }
}
