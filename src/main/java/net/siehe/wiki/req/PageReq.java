package net.siehe.wiki.req;

import lombok.Data;

//将传进来的参数封装成一个类，请求参数封装
@Data
public class PageReq {
    private Integer page;
    private Integer size;
}