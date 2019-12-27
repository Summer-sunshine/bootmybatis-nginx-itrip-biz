package com.wzh.service.serviceImpl;

import com.wzh.mapper.ItripImageMapper;
import com.wzh.po.ItripImage;
import com.wzh.service.ItripImageService;
import com.wzh.util.ItripImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
public class ItripImageServiceImpl implements ItripImageService {
    @Autowired
    private ItripImageMapper itripImageMapper;

    public ItripImageMapper getItripImageMapper() {
        return itripImageMapper;
    }

    public void setItripImageMapper(ItripImageMapper itripImageMapper) {
        this.itripImageMapper = itripImageMapper;
    }

    @Override
    public List<ItripImageVO> getItripImageListByMap(Map<String, Object> param) throws Exception {
        return itripImageMapper.getItripImageListByMap(param);
    }

    @Override
    public Integer itriptxAddItripComment(ItripImage itripImage) throws Exception {
        itripImage.setModifyDate(new Date());
        return itripImageMapper.insertItripImage(itripImage);
    }
}
