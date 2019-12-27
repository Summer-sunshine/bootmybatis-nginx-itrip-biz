package com.wzh.service;

import com.wzh.po.ItripLabelDic;
import com.wzh.util.ItripLabelDicVO;

import java.util.List;
import java.util.Map;

public interface ItripLabelDicService {
    //查询酒店特色文字
    public List<ItripLabelDic> getItripLabelDicListByMap(Map<String,Object> param)throws Exception;
    //根据床型父级id查询床型列表
    public List<ItripLabelDicVO> getItripLabelDicByParentId(Long parentId)throws Exception;
}
