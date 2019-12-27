package com.wzh.service;

import com.wzh.po.ItripHotelRoom;
import com.wzh.util.ItripHotelRoomVO;

import java.util.List;
import java.util.Map;

public interface ItripHotelRoomService {
    //查询房间列表
    public List<ItripHotelRoomVO> getItripHotelRoomListByMap(Map<String,Object> param)throws Exception;
    //通过房型ID获取房型列表
    public ItripHotelRoom getItripHotelRoomById(Long id)throws Exception;
}
