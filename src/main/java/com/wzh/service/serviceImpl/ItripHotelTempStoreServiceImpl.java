package com.wzh.service.serviceImpl;

import com.wzh.mapper.ItripHotelTempStoreMapper;
import com.wzh.po.ItripHotelTempStore;
import com.wzh.service.ItripHotelTempStoreService;
import com.wzh.util.EmptyUtils;
import com.wzh.util.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class ItripHotelTempStoreServiceImpl implements ItripHotelTempStoreService {
    @Autowired
    private ItripHotelTempStoreMapper itripHotelTempStoreMapper;

    public ItripHotelTempStoreMapper getItripHotelTempStoreMapper() {
        return itripHotelTempStoreMapper;
    }

    public void setItripHotelTempStoreMapper(ItripHotelTempStoreMapper itripHotelTempStoreMapper) {
        this.itripHotelTempStoreMapper = itripHotelTempStoreMapper;
    }

    @Override
    public List<StoreVO> queryRoomStore(Map<String, Object> param) throws Exception {
        return itripHotelTempStoreMapper.queryRoomStore(param);
    }

    @Override
    public List<ItripHotelTempStore> getItripHotelTempStoreListByMap(Map<String, Object> param) throws Exception {
        return itripHotelTempStoreMapper.getItripHotelTempStoreListByMap(param);
    }

    @Override
    public boolean vailDateRoomStote(Map<String, Object> param) throws Exception {
        Integer count=(Integer) param.get("count");
        List<StoreVO> storeVOList=itripHotelTempStoreMapper.queryRoomStore(param);
        if (EmptyUtils.isNotEmpty(storeVOList)){
            return false;
        }
        for (StoreVO storeVO:storeVOList){
            if (storeVO.getStore()<count){
             return false;
            }
        }
        return true;
    }

    @Override
    public Integer updateRoomStore(Map<String, Object> param) throws Exception {
        return itripHotelTempStoreMapper.updateRoomStore(param);
    }
}
