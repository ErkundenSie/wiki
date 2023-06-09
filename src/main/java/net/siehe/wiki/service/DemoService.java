package net.siehe.wiki.service;

import net.siehe.wiki.domain.Demo;
import net.siehe.wiki.mapper.DemoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DemoService {
    @Resource
    private DemoMapper demoMapper;
    public List<Demo> list(){
        return demoMapper.selectByExample(null);
    }
}
