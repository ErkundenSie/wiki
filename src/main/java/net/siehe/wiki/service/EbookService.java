package net.siehe.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.siehe.wiki.domain.Ebook;
import net.siehe.wiki.domain.EbookExample;
import net.siehe.wiki.mapper.EbookMapper;
import net.siehe.wiki.req.EbookQueryReq;
import net.siehe.wiki.req.EbookSaveReq;
import net.siehe.wiki.resp.EbookQueryResp;
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
public class EbookService {
    // @Resource是jdk自带的，@Autowired是spring自带的
    @Resource
    private EbookMapper ebookMapper;
    @Resource
    private SnowFlake snowFlake;
    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);//打印总行数页数日志

    public PageResp<EbookQueryResp> list(EbookQueryReq ebookReq){

//        return ebookMapper.selectByExample(null);
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria(); //先创建ebookExample在调用内部类创建条件
        // 动态SQL不为空在添加
        if(!ObjectUtils.isEmpty(ebookReq.getName())){
            criteria.andNameLike("%" + ebookReq.getName() +"%");//添加条件左右匹配
        }
        //导入PageHelper依赖，调用PageHelper，注意此插件页码从1开始不是0，而且只对最近的select起作用对下面的ebookList=不起作用
        PageHelper.startPage(ebookReq.getPage(),ebookReq.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
//        ebookList = ebookMapper.selectByExample(ebookExample);
//        private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);//打印总行数页数日志
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{}",pageInfo.getTotal());//一般返回总行数
        LOG.info("总页数：{}",pageInfo.getPages());

//        List<EbookResp> respList = new ArrayList<>(); //创建一个数组ebookList的部分放进去
//        //两种for循环 fori、iter
//        //respList.add(ebook);//类型不一致不能加
//        for (Ebook ebook : ebookList) {
//            //将ebook内容复制到新的ebookResp中然后将ebookResp放入respList
////            EbookResp ebookResp = new EbookResp();
////            BeanUtils.copyProperties(ebook,ebookResp);//BeanUtils.copyProperties()数组复制，或者 ebookResp.setId(ebook.getId());+
//            /*可以提取成工具类方便调用eg1对象复制*/
//            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
//            respList.add(ebookResp);
//        }
//        return respList;

        /*可以提取成工具类方便调用eg2列表复制*/
        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class);
//        return list;
        PageResp<EbookQueryResp> pageResp = new PageResp();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
    }

    /**
     * 保存
     * @param ebookReq
     */
    public void save(EbookSaveReq ebookReq) {
        Ebook ebook = CopyUtil.copy(ebookReq,Ebook.class);
        //根据id是否为空判断新增或更新
        //id有几种算法，一种是最简单的自增，还有一种是uuid，还有就是雪花算法
        if (ObjectUtils.isEmpty(ebookReq.getId())){
            //调用雪花算法
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        }else {
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }
}
