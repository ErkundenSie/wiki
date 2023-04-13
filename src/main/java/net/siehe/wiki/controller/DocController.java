package net.siehe.wiki.controller;

import net.siehe.wiki.req.DocQueryReq;
import net.siehe.wiki.req.DocSaveReq;
import net.siehe.wiki.resp.DocQueryResp;
import net.siehe.wiki.resp.CommonResp;
import net.siehe.wiki.resp.PageResp;
import net.siehe.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {
    @Resource
    private DocService docService;

    /**
     * 查询所有
     * @param
     * @return
     */
    @GetMapping("/all")
    public CommonResp all() {
        CommonResp<List<DocQueryResp>> objectCommonResp = new CommonResp<>();
        List<DocQueryResp> list = docService.all();
        objectCommonResp.setContent(list);
        return objectCommonResp;
    }

    //修改统一返回类型CommonResp
    //@Valid 开启之前在PageReq的校验规则
    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq docReq) {
        //并不是所有时候都返回Doc，可能只是返回一部分，如含有密码等需要过滤 定义DocResp类
        //在controller中不要出现domain实体doc
        CommonResp<PageResp<DocQueryResp>> objectCommonResp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(docReq);
        objectCommonResp.setContent(list);
        return objectCommonResp;
    }

    /**
     * 新增或保存前端返回的数据
     * 用PostMapping加@RequestBody对应json方式的提交，form方式不用
     * @param docReq
     * @return
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq docReq) {
        CommonResp objectCommonResp = new CommonResp();//不需要返回直接不用泛型
        docService.save(docReq);
        return objectCommonResp;
    }

    @DeleteMapping ("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp objectCommonResp = new CommonResp();//不需要返回直接不用泛型
        docService.delete(id);
        return objectCommonResp;
    }
}
