package net.siehe.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.siehe.wiki.domain.Category;
import net.siehe.wiki.domain.CategoryExample;
import net.siehe.wiki.mapper.CategoryMapper;
import net.siehe.wiki.req.CategoryQueryReq;
import net.siehe.wiki.req.CategorySaveReq;
import net.siehe.wiki.resp.CategoryQueryResp;
import net.siehe.wiki.resp.PageResp;
import net.siehe.wiki.util.CopyUtil;
import net.siehe.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {
    // @Resource是jdk自带的，@Autowired是spring自带的
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private SnowFlake snowFlake;
    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);//打印总行数页数日志

    /**
     * 查询所有
     */
    public List<CategoryQueryResp> all() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc"); //升序排列
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class); //列表复制
        return list;
    }

    /**
     * 有条件查询
     * @param categoryReq
     * @return
     */
    public PageResp<CategoryQueryResp> list(CategoryQueryReq categoryReq) {

//        return categoryMapper.selectByExample(null);
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc"); //升序排列
        CategoryExample.Criteria criteria = categoryExample.createCriteria(); //先创建categoryExample在调用内部类创建条件criteria相当于where条件
        //导入PageHelper依赖，调用PageHelper，注意此插件页码从1开始不是0，而且只对最近的select起作用对下面的categoryList=不起作用
        PageHelper.startPage(categoryReq.getPage(), categoryReq.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
//        categoryList = categoryMapper.selectByExample(categoryExample);
//        private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);//打印总行数页数日志
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数：{}", pageInfo.getTotal());//一般返回总行数
        LOG.info("总页数：{}", pageInfo.getPages());

//        List<CategoryResp> respList = new ArrayList<>(); //创建一个数组categoryList的部分放进去
//        //两种for循环 fori、iter
//        //respList.add(category);//类型不一致不能加
//        for (Category category : categoryList) {
//            //将category内容复制到新的categoryResp中然后将categoryResp放入respList
////            CategoryResp categoryResp = new CategoryResp();
////            BeanUtils.copyProperties(category,categoryResp);//BeanUtils.copyProperties()数组复制，或者 categoryResp.setId(category.getId());+
//            /*可以提取成工具类方便调用eg1对象复制*/
//            CategoryResp categoryResp = CopyUtil.copy(category, CategoryResp.class);
//            respList.add(categoryResp);
//        }
//        return respList;

        /*可以提取成工具类方便调用eg2列表复制*/
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
//        return list;
        PageResp<CategoryQueryResp> pageResp = new PageResp();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    /**
     * 保存
     *
     * @param categoryReq
     */
    public void save(CategorySaveReq categoryReq) {
        Category category = CopyUtil.copy(categoryReq, Category.class);
        //根据id是否为空判断新增或更新
        //id有几种算法，一种是最简单的自增，还有一种是uuid，还有就是雪花算法
        if (ObjectUtils.isEmpty(categoryReq.getId())) {
            //调用雪花算法
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        } else {
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    /**
     * 删除
     *
     * @param id
     */

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
