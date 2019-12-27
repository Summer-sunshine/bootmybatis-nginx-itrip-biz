package com.wzh.service.serviceImpl;

import com.wzh.mapper.ItripLabelDicMapper;
import com.wzh.po.ItripLabelDic;
import com.wzh.service.ItripLabelDicService;
import com.wzh.util.ItripLabelDicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
@Service
public class ItripLabelDicServiceImpl implements ItripLabelDicService {
    @Autowired
    private ItripLabelDicMapper itripLabelDicMapper;

    public ItripLabelDicMapper getItripLabelDicMapper() {
        return itripLabelDicMapper;
    }

    public void setItripLabelDicMapper(ItripLabelDicMapper itripLabelDicMapper) {
        this.itripLabelDicMapper = itripLabelDicMapper;
    }

    @Override
    public List<ItripLabelDic> getItripLabelDicListByMap(Map<String, Object> param) throws Exception {
        return itripLabelDicMapper.getItripLabelDicListByMap(param);
    }

    @Override
    public List<ItripLabelDicVO> getItripLabelDicByParentId(Long parentId) throws Exception {
        return itripLabelDicMapper.getItripLabelDicByParentId(parentId);
    }
}
