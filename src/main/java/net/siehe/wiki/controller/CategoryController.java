package net.siehe.wiki.controller;

import net.siehe.wiki.req.CategoryQueryReq;
import net.siehe.wiki.req.CategorySaveReq;
import net.siehe.wiki.resp.CommonResp;
import net.siehe.wiki.resp.CategoryQueryResp;
import net.siehe.wiki.resp.PageResp;
import net.siehe.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;
    //修改统一返回类型CommonResp
    //@Valid 开启之前在PageReq的校验规则
    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq categoryReq) {
        //并不是所有时候都返回Category，可能只是返回一部分，如含有密码等需要过滤 定义CategoryResp类
        //在controller中不要出现domain实体category
        CommonResp<PageResp<CategoryQueryResp>> objectCommonResp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(categoryReq);
        objectCommonResp.setContent(list);
        return objectCommonResp;
    }

    /**
     * 新增或保存前端返回的数据
     * 用PostMapping加@RequestBody对应json方式的提交，form方式不用
     * @param categoryReq
     * @return
     */
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq categoryReq) {
        CommonResp objectCommonResp = new CommonResp();//不需要返回直接不用泛型
        categoryService.save(categoryReq);
        return objectCommonResp;
    }

    @DeleteMapping ("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp objectCommonResp = new CommonResp();//不需要返回直接不用泛型
        categoryService.delete(id);
        return objectCommonResp;
    }
}
