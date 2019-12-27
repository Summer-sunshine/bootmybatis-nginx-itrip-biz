package com.wzh.service;


import com.wzh.po.ItripImage;
import com.wzh.util.ItripImageVO;
import java.util.List;
import java.util.Map;

public interface ItripImageService {
    //根据酒店ID和评论ID和房型id查询图片
    public List<ItripImageVO> getItripImageListByMap(Map<String,Object> param)throws Exception;
    //新增评论
    public Integer itriptxAddItripComment(ItripImage itripImage)throws Exception;
}
