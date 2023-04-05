package net.siehe.wiki.mapper;

import net.siehe.wiki.domain.Test;

import java.util.List;
// 持久层mapper、dao
public interface TestMapper {
    public List<Test> list();
}
