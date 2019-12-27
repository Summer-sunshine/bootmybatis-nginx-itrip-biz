package com.wzh.service.serviceImpl;

import com.wzh.mapper.ItripHotelMapper;
import com.wzh.po.ItripAreaDic;
import com.wzh.po.ItripHotel;
import com.wzh.po.ItripLabelDic;
import com.wzh.service.ItripHotelService;
import com.wzh.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItripHotelServiceImpl implements ItripHotelService {
    @Autowired
    private ItripHotelMapper itripHotelMapper;

    public ItripHotelMapper getItripHotelMapper() {
        return itripHotelMapper;
    }

    public void setItripHotelMapper(ItripHotelMapper itripHotelMapper) {
        this.itripHotelMapper = itripHotelMapper;
    }

    @Override
    public List<ItripSearchDetailsHotelVO> queryHotelDetails(Long id) throws Exception {
        //通用字典集合
        List<ItripLabelDic> itripLabelDics=null;
        itripLabelDics=itripHotelMapper.getHotelFeatureByHotelId(id);
        //往前台封装数据
        List<ItripSearchDetailsHotelVO> itripSearchDetailsHotelVOS=new ArrayList<>();
        //获取前台返回类型
        ItripSearchDetailsHotelVO itripSearchDetailsHotelVO=new ItripSearchDetailsHotelVO();
        //设置第一个属性
        itripSearchDetailsHotelVO.setName("酒店介绍");
        itripSearchDetailsHotelVO.setDescription(itripHotelMapper.getItripHotelById(id).getDetails());
        itripSearchDetailsHotelVOS.add(itripSearchDetailsHotelVO);
        //如果是多个介绍的情况
        for (ItripLabelDic itripLabelDic:itripLabelDics){
            //获取前台返回类型
            ItripSearchDetailsHotelVO itripSearchDetailsHotelVO1=new ItripSearchDetailsHotelVO();
            itripSearchDetailsHotelVO1.setName(itripLabelDic.getName());
            itripSearchDetailsHotelVO1.setDescription(itripLabelDic.getDescription());
            itripSearchDetailsHotelVOS.add(itripSearchDetailsHotelVO1);
        }

        return itripSearchDetailsHotelVOS;
    }

    @Override
    public ItripSearchFacilitiesHotelVO getItripHotelFacilitiesById(Long id) throws Exception {
        return itripHotelMapper.getItripHotelFacilitiesById(id);
    }

    @Override
    public ItripSearchPolicyHotelVO queryHotelPolicy(Long id) throws Exception {
        return itripHotelMapper.queryHotelPolicy(id);
    }

    @Override
    public HotelVideoDescVO getVideoDescById(Long id) throws Exception {
        HotelVideoDescVO hotelVideoDescVO=new HotelVideoDescVO();
        //查询区域
        List<ItripAreaDic> itripAreaDics=new ArrayList<>();
        //查询tese
        List<ItripLabelDic> itripLabelDics=new ArrayList<>();
        //处理区域
        itripAreaDics=itripHotelMapper.getHotelAreaByHotelId(id);
        List<String> list1=new ArrayList<>();
        if (EmptyUtils.isNotEmpty(itripAreaDics)){
            for (ItripAreaDic dic:itripAreaDics){
                list1.add(dic.getName());
            }
            hotelVideoDescVO.setTradingAreaNameList(list1);
        }
        //处理特色
        itripLabelDics=itripHotelMapper.getHotelFeatureByHotelId(id);
        List<String> list2=new ArrayList<>();
        if (EmptyUtils.isNotEmpty(itripLabelDics)){
            for (ItripLabelDic dic:itripLabelDics){
                list2.add(dic.getName());
            }
            hotelVideoDescVO.setHotelFeatureList(list2);
        }
        //处理酒店名称
        hotelVideoDescVO.setHotelName(itripHotelMapper.getItripHotelById(id).getHotelName());
        return hotelVideoDescVO;
    }

    @Override
    public ItripHotel getItripHotelById(Long id) throws Exception {
        return itripHotelMapper.getItripHotelById(id);
    }
}
