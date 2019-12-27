package com.wzh.service;

import com.wzh.po.ItripHotelOrder;
import com.wzh.po.ItripUserLinkUser;
import com.wzh.util.ItripOrderLinkUserVo;
import com.wzh.util.ItripPersonalOrderRoomVO;
import com.wzh.util.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ItripHotelOrderService {
    /*总金额*/
    public BigDecimal getItripHotelOrderPayAmount(int count,Long roomId)throws Exception;
    /*添加订单*/
    public Map<String,String> insertItripHotelOrder(ItripHotelOrder itripHotelOrder, List<ItripUserLinkUser> itripUserLinkUserList)throws Exception;
    /*通过用户数据查询订单该用户所有数据*/
    public Page getOrderListByMap(Map<String,Object> param,Integer pageNo,Integer pageSize)throws Exception;
    /*根据订单id查找个人订单信息*/
    public ItripHotelOrder getItripHotelOrderById(Long id)throws Exception;
    /*根据订单id查找房型信息*/
    public ItripPersonalOrderRoomVO getItripHotelOrderRoomInfoById(Long id)throws Exception;
    /*通过订单id查询订单用户关系数据*/
    public  List<ItripOrderLinkUserVo> getItripOrderLinkUserListByMap(Map<String,Object> param)throws Exception;
    //通过酒店id查询该酒店所有预定订单信息
    public  List<ItripHotelOrder> getItripHotelOrderByhotelId(Map<String,Object> param)throws Exception;
}
