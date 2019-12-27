package com.wzh.service;

import com.wzh.po.ItripAreaDic;

import java.util.List;
import java.util.Map;

public interface ItripAreaDicService {
    //根据热门城市查询区域--根据父级id（城市ID）查询商圈
    public List<ItripAreaDic> getItripAreaDicListByMap(Map<String,Object> param)throws Exception;

}
