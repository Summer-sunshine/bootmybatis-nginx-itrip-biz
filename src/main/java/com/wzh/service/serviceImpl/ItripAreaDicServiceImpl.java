package com.wzh.service.serviceImpl;

import com.wzh.mapper.ItripAreaDicMapper;
import com.wzh.po.ItripAreaDic;
import com.wzh.service.ItripAreaDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
@Service
public class ItripAreaDicServiceImpl implements ItripAreaDicService {
    @Autowired
    private ItripAreaDicMapper itripAreaDicMapper;

    public ItripAreaDicMapper getItripAreaDicMapper() {
        return itripAreaDicMapper;
    }

    public void setItripAreaDicMapper(ItripAreaDicMapper itripAreaDicMapper) {
        this.itripAreaDicMapper = itripAreaDicMapper;
    }

    @Override
    public List<ItripAreaDic> getItripAreaDicListByMap(Map<String, Object> param) throws Exception {
        return itripAreaDicMapper.getItripAreaDicListByMap(param);
    }
}
