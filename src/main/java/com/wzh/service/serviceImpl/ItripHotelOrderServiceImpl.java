package com.wzh.service.serviceImpl;

import com.wzh.mapper.ItripHotelOrderMapper;
import com.wzh.mapper.ItripHotelRoomMapper;
import com.wzh.mapper.ItripOrderLinkUserMapper;
import com.wzh.po.ItripHotelOrder;
import com.wzh.po.ItripOrderLinkUser;
import com.wzh.po.ItripUserLinkUser;
import com.wzh.service.ItripHotelOrderService;
import com.wzh.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.math.BigDecimal.ROUND_DOWN;
@Service
public class ItripHotelOrderServiceImpl implements ItripHotelOrderService {
    @Autowired
    private ItripHotelOrderMapper itripHotelOrderMapper;
    @Autowired
    private ItripHotelRoomMapper itripHotelRoomMapper;
    @Autowired
    private ItripOrderLinkUserMapper itripOrderLinkUserMapper;

    public ItripOrderLinkUserMapper getItripOrderLinkUserMapper() {
        return itripOrderLinkUserMapper;
    }

    public void setItripOrderLinkUserMapper(ItripOrderLinkUserMapper itripOrderLinkUserMapper) {
        this.itripOrderLinkUserMapper = itripOrderLinkUserMapper;
    }

    public ItripHotelRoomMapper getItripHotelRoomMapper() {
        return itripHotelRoomMapper;
    }

    public void setItripHotelRoomMapper(ItripHotelRoomMapper itripHotelRoomMapper) {
        this.itripHotelRoomMapper = itripHotelRoomMapper;
    }

    public ItripHotelOrderMapper getItripHotelOrderMapper() {
        return itripHotelOrderMapper;
    }

    public void setItripHotelOrderMapper(ItripHotelOrderMapper itripHotelOrderMapper) {
        this.itripHotelOrderMapper = itripHotelOrderMapper;
    }

    @Override
    public BigDecimal getItripHotelOrderPayAmount(int count, Long roomId) throws Exception {
        BigDecimal payAmount=null;
        BigDecimal roomprice=itripHotelRoomMapper.getItripHotelRoomById(roomId).getRoomPrice();
        payAmount= BigDecimalUtil.OperationASMD(count,roomprice,BigDecimalUtil.BigDecimalOprations.multiply,2,ROUND_DOWN);
        return payAmount;
    }

    @Override
    public Map<String,String> insertItripHotelOrder(ItripHotelOrder itripHotelOrder, List<ItripUserLinkUser> itripUserLinkUserList) throws Exception {
        Map<String,String> param=new HashMap<>();
        if (EmptyUtils.isNotEmpty(itripHotelOrder)){
            int flag=0;
            itripHotelOrder.setCreationDate(new Date());
            flag=itripHotelOrderMapper.insertItripHotelOrder(itripHotelOrder);
            if (flag>0){
                Long orderId=itripHotelOrder.getId();
                //未添加之后做准备（为订单与常用联系人关系表添加记录）
                if (orderId>0){
                    for (ItripUserLinkUser itripUserLinkUser:itripUserLinkUserList){
                        ItripOrderLinkUser itripOrderLinkUser=new ItripOrderLinkUser();
                        itripOrderLinkUser.setOrderId(orderId);
                        itripOrderLinkUser.setLinkUserId(itripUserLinkUser.getId());
                        itripOrderLinkUser.setLinkUserName(itripUserLinkUser.getLinkUserName());
                        itripOrderLinkUser.setCreationDate(new Date());
                        itripOrderLinkUser.setCreatedBy(itripUserLinkUser.getCreatedBy());
                        itripOrderLinkUserMapper.insertItripOrderLinkUser(itripOrderLinkUser);
                    }
                }
                param.put("id",itripHotelOrder.getId().toString());
                param.put("orderNo",itripHotelOrder.getOrderNo());
                return param;
            }
        }
        return null;
    }

    @Override
    public Page getOrderListByMap(Map<String, Object> param, Integer pageNo, Integer pageSize) throws Exception {
        //总记录数
        Integer total=itripHotelOrderMapper.getOrderCountByMap(param);
        pageNo=EmptyUtils.isEmpty(pageNo)? Constants.DEFAULT_PAGE_NO:pageNo;
        pageSize=EmptyUtils.isEmpty(pageSize)?Constants.DEFAULT_PAGE_SIZE:pageSize;
        Page page=new Page(pageNo,pageSize,total);
        param.put("beginPos",page.getBeginPos());
        param.put("pageSize",page.getPageSize());
        List<ItripListHotelOrderVO> itripListHotelOrderVOList=itripHotelOrderMapper.getOrderListByMap(param);
        page.setRows(itripListHotelOrderVOList);
        return page;
    }

    @Override
    public ItripHotelOrder getItripHotelOrderById(Long id) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderById(id);
    }

    @Override
    public ItripPersonalOrderRoomVO getItripHotelOrderRoomInfoById(Long id) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderRoomInfoById(id);
    }

    @Override
    public List<ItripOrderLinkUserVo> getItripOrderLinkUserListByMap(Map<String, Object> param) throws Exception {
        return itripOrderLinkUserMapper.getItripOrderLinkUserListByMap(param);
    }

    @Override
    public List<ItripHotelOrder> getItripHotelOrderByhotelId(Map<String,Object> param) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderListByMap(param);
    }
}
