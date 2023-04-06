package net.siehe.wiki.service;

import com.fasterxml.jackson.databind.util.BeanUtil;
import net.siehe.wiki.domain.Ebook;
import net.siehe.wiki.domain.EbookExample;
import net.siehe.wiki.mapper.EbookMapper;
import net.siehe.wiki.req.EbookReq;
import net.siehe.wiki.resp.EbookResp;
import net.siehe.wiki.util.CopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;
    public List<EbookResp> list(EbookReq ebookReq){
//        return ebookMapper.selectByExample(null);
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria(); //先创建ebookExample在调用内部类创建条件
        criteria.andNameLike("%" + ebookReq.getName() +"%");//添加条件左右匹配
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

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
        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);
        return list;

    }
}
