package net.siehe.wiki.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CategorySaveReq {
    private Long id;

    private String parent;
    @NotNull(message = "【名称】不能为空")
    private String name;
    @NotNull(message = "【排序】不能为空")
    private Integer sort;

}