package com.wzh.service;

import com.wzh.po.ItripHotel;
import com.wzh.util.HotelVideoDescVO;
import com.wzh.util.ItripSearchDetailsHotelVO;
import com.wzh.util.ItripSearchFacilitiesHotelVO;
import com.wzh.util.ItripSearchPolicyHotelVO;

import java.util.List;

public interface ItripHotelService {
    //根据酒店id查询酒店特色及介绍
    public List<ItripSearchDetailsHotelVO> queryHotelDetails(Long id)throws Exception;
    //根据酒店id查询酒店设施
    public ItripSearchFacilitiesHotelVO getItripHotelFacilitiesById(Long id)throws Exception;
    //根据酒店id查询酒店政策
    public ItripSearchPolicyHotelVO queryHotelPolicy(Long id)throws Exception;
    //根据酒店id查询视频中的酒店名称、位置、特色
    public HotelVideoDescVO getVideoDescById(Long id)throws Exception;
    //通过酒店ID查询酒店对象
    public ItripHotel getItripHotelById(Long id)throws Exception;
}
