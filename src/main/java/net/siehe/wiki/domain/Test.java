package net.siehe.wiki.domain;

import lombok.Data;

//可以叫domain、entity、POJO 总之这一层实体类就是和数据库表一一映射
@Data
public class Test {
    private Integer id;
    private String name;
    private String password;
}
