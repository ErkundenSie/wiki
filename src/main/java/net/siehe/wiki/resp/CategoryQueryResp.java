package net.siehe.wiki.resp;

import lombok.Data;

@Data
public class CategoryQueryResp {
    private Long id;

    private String parent;

    private String name;

    private Integer sort;

}