package net.siehe.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.siehe.wiki.domain.Doc;
import net.siehe.wiki.domain.DocExample;
import net.siehe.wiki.mapper.DocMapper;
import net.siehe.wiki.req.DocQueryReq;
import net.siehe.wiki.req.DocSaveReq;
import net.siehe.wiki.resp.DocQueryResp;
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
public class DocService {
    // @Resource是jdk自带的，@Autowired是spring自带的
    @Resource
    private DocMapper docMapper;
    @Resource
    private SnowFlake snowFlake;
    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);//打印总行数页数日志

    /**
     * 查询所有
     */
    public List<DocQueryResp> all() {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc"); //升序排列
        List<Doc> docList = docMapper.selectByExample(docExample);
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class); //列表复制
        return list;
    }

    /**
     * 有条件查询
     * @param docReq
     * @return
     */
    public PageResp<DocQueryResp> list(DocQueryReq docReq) {

//        return docMapper.selectByExample(null);
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc"); //升序排列
        DocExample.Criteria criteria = docExample.createCriteria(); //先创建docExample在调用内部类创建条件criteria相当于where条件
        //导入PageHelper依赖，调用PageHelper，注意此插件页码从1开始不是0，而且只对最近的select起作用对下面的docList=不起作用
        PageHelper.startPage(docReq.getPage(), docReq.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);
//        docList = docMapper.selectByExample(docExample);
//        private static final Logger LOG = LoggerFactory.getLogger(DocService.class);//打印总行数页数日志
        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数：{}", pageInfo.getTotal());//一般返回总行数
        LOG.info("总页数：{}", pageInfo.getPages());

//        List<DocResp> respList = new ArrayList<>(); //创建一个数组docList的部分放进去
//        //两种for循环 fori、iter
//        //respList.add(doc);//类型不一致不能加
//        for (Doc doc : docList) {
//            //将doc内容复制到新的docResp中然后将docResp放入respList
////            DocResp docResp = new DocResp();
////            BeanUtils.copyProperties(doc,docResp);//BeanUtils.copyProperties()数组复制，或者 docResp.setId(doc.getId());+
//            /*可以提取成工具类方便调用eg1对象复制*/
//            DocResp docResp = CopyUtil.copy(doc, DocResp.class);
//            respList.add(docResp);
//        }
//        return respList;

        /*可以提取成工具类方便调用eg2列表复制*/
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
//        return list;
        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    /**
     * 保存
     *
     * @param docReq
     */
    public void save(DocSaveReq docReq) {
        Doc doc = CopyUtil.copy(docReq, Doc.class);
        //根据id是否为空判断新增或更新
        //id有几种算法，一种是最简单的自增，还有一种是uuid，还有就是雪花算法
        if (ObjectUtils.isEmpty(docReq.getId())) {
            //调用雪花算法
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
        } else {
            docMapper.updateByPrimaryKey(doc);
        }
    }

    /**
     * 删除
     *
     * @param
     */
    public void delete(long id) {
        docMapper.deleteByPrimaryKey(id);
    }
    public void delete(List<String> list) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(list);
        docMapper.deleteByExample(docExample);
    }
}
