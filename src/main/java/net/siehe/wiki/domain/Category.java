package net.siehe.wiki.domain;

import lombok.Data;

@Data
public class Category {
    private Long id;

    private String parent;

    private String name;

    private Integer sort;

}