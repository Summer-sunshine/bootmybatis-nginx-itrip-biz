package com.wzh.service.serviceImpl;

import com.wzh.mapper.ItripHotelRoomMapper;
import com.wzh.po.ItripHotelRoom;
import com.wzh.service.ItripHotelRoomService;
import com.wzh.util.ItripHotelRoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class ItripHotelRoomServiceImpl implements ItripHotelRoomService {
    @Autowired
    private ItripHotelRoomMapper itripHotelRoomMapper;

    public ItripHotelRoomMapper getItripHotelRoomMapper() {
        return itripHotelRoomMapper;
    }

    public void setItripHotelRoomMapper(ItripHotelRoomMapper itripHotelRoomMapper) {
        this.itripHotelRoomMapper = itripHotelRoomMapper;
    }

    @Override
    public List<ItripHotelRoomVO> getItripHotelRoomListByMap(Map<String, Object> param) throws Exception {
        return itripHotelRoomMapper.getItripHotelRoomListByMap(param);
    }

    @Override
    public ItripHotelRoom getItripHotelRoomById(Long id) throws Exception {
        return itripHotelRoomMapper.getItripHotelRoomById(id);
    }
}
