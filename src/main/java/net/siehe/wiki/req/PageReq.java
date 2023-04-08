package net.siehe.wiki.req;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

//将传进来的参数封装成一个类，请求参数封装
@Data
public class PageReq {
    @NotNull(message = "【页码】 不能为空")
    private Integer page;

    // 集成Validation做参数校验不能为空，最大多少，然后再开启校验 @Valid才能启用eg在EbookController入参
    @NotNull(message = "【页码】 不能为空")
    @Max(value = 1000,message = "【每页条数】不能超过1000")
    private Integer size;
}