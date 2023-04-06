package net.siehe.wiki.service.impl;

import net.siehe.wiki.domain.Demo;
import net.siehe.wiki.mapper.DemoMapper;
import net.siehe.wiki.service.DemoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 测试 服务实现类
 * </p>
 *
 * @author zy
 * @since 2023-04-05
 */
@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, Demo> implements DemoService {

}
