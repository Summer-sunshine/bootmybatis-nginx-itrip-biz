package com.wzh.controller;

import com.wzh.po.Dto;
import com.wzh.service.ItripHotelRoomService;
import com.wzh.service.ItripImageService;
import com.wzh.service.ItripLabelDicService;
import com.wzh.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping(value = "/api/hotelroom")
public class ItripHotelRoomController {
    @Autowired
    private ItripLabelDicService itripLabelDicService;
    @Autowired
    private ItripHotelRoomService itripHotelRoomService;
    @Autowired
    private ItripImageService itripImageService;

    public ItripImageService getItripImageService() {
        return itripImageService;
    }

    public void setItripImageService(ItripImageService itripImageService) {
        this.itripImageService = itripImageService;
    }

    public ItripHotelRoomService getItripHotelRoomService() {
        return itripHotelRoomService;
    }

    public void setItripHotelRoomService(ItripHotelRoomService itripHotelRoomService) {
        this.itripHotelRoomService = itripHotelRoomService;
    }

    public ItripLabelDicService getItripLabelDicService() {
        return itripLabelDicService;
    }

    public void setItripLabelDicService(ItripLabelDicService itripLabelDicService) {
        this.itripLabelDicService = itripLabelDicService;
    }
    /* @ApiOperation(value = "查询酒店房间床型列表", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "查询酒店床型列表" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100305 : 获取酒店房间床型失败</p>")*/
    @RequestMapping(value = "/queryhotelroombed")
    public Dto<Object> queryHotelRoomBed() {
        System.out.println("查询酒店房间床型列表.....");
        try {
            List<ItripLabelDicVO> itripLabelDicVOS=itripLabelDicService.getItripLabelDicByParentId(new Long(1));
            return DtoUtil.returnSuccess("获取成功",itripLabelDicVOS);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("获取酒店房间床型失败","100305");
        }
    }
    /*@ApiOperation(value = "查询酒店房间列表", httpMethod = "POST",
           protocols = "HTTP", produces = "application/json",
           response = Dto.class, notes = "查询酒店房间列表" +
           "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
           "<p>错误码：</p>" +
           "<p>100303 : 酒店id不能为空,酒店入住及退房时间不能为空,入住时间不能大于退房时间</p>" +
           "<p>100304 : 系统异常</p>")*/
    @RequestMapping(value = "/queryhotelroombyhotel")
    public Dto<List<ItripHotelRoomVO>> queryHotelRoomByHotel(@RequestBody SearchHotelRoomVO vo) {
        System.out.println("查询酒店房间列表....注意修改了mapper中集合判null");
        try {
            List<List<ItripHotelRoomVO>> hotellist=null;
            Map<String,Object> param=new HashMap<>();
            if (EmptyUtils.isEmpty(vo.getHotelId())){
                return DtoUtil.returnFail("酒店id不能为空","100303");
            }
            if (EmptyUtils.isEmpty(vo.getStartDate())||EmptyUtils.isEmpty(vo.getEndDate())){
                return DtoUtil.returnFail("酒店入住及退房时间不能为空","100303");
            }
            if (vo.getStartDate().getTime()>vo.getEndDate().getTime()){
                return DtoUtil.returnFail("入住时间不能大于退房时间","100303");
            }
            if (vo.getStartDate().getTime()<new Date().getTime()){
                return DtoUtil.returnFail("入住时间不能小于当前时间","100303");
            }
            if (vo.getEndDate().getTime()<vo.getStartDate().getTime()&&vo.getEndDate().getTime()<new Date().getTime()){
                return DtoUtil.returnFail("退房时间不能小于当前时间且不能小于入住时间","100303");
            }
            List<Date> dateList=DateUtil.getBetweenDates(vo.getStartDate(),vo.getEndDate());
            param.put("timesList",dateList);
            //处理前台数据
            vo.setIsHavingBreakfast(EmptyUtils.isEmpty(vo.getIsHavingBreakfast())?null:vo.getIsHavingBreakfast());
            vo.setIsTimelyResponse(EmptyUtils.isEmpty(vo.getIsTimelyResponse())?null:vo.getIsTimelyResponse());
            vo.setIsCancel(EmptyUtils.isEmpty(vo.getIsCancel())?null:vo.getIsCancel());
            vo.setRoomBedTypeId(EmptyUtils.isEmpty(vo.getRoomBedTypeId())?null:vo.getRoomBedTypeId());
            vo.setPayType(EmptyUtils.isEmpty(vo.getPayType())?null:vo.getPayType());
            vo.setIsBook(EmptyUtils.isEmpty(vo.getIsBook())?null:vo.getIsBook());
            param.put("hotelId",vo.getHotelId());
            param.put("isHavingBreakfast",vo.getIsHavingBreakfast());
            param.put("isTimelyResponse",vo.getIsTimelyResponse());
            param.put("isCancel",vo.getIsCancel());
            param.put("isBook",vo.getIsBook());
            param.put("roomBedTypeId",vo.getRoomBedTypeId());
            if (EmptyUtils.isEmpty(vo.getPayType())||vo.getPayType()==3){
                param.put("payType",null);
            }else {
                param.put("payType", vo.getPayType());
            }
            List<ItripHotelRoomVO> itripHotelRoomVOS=itripHotelRoomService.getItripHotelRoomListByMap(param);
            hotellist=new ArrayList<>();
            for (ItripHotelRoomVO itripHotelRoomVO:itripHotelRoomVOS){
                List<ItripHotelRoomVO> itripHotelRoomVOList=new ArrayList<>();
                itripHotelRoomVOList.add(itripHotelRoomVO);
                hotellist.add(itripHotelRoomVOList);
            }
            return DtoUtil.returnSuccess("获取成功",hotellist);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常","100304");
        }
    }
    /* @ApiOperation(value = "根据targetId查询酒店房型图片(type=1)", httpMethod = "GET",
           protocols = "HTTP", produces = "application/json",
           response = Dto.class, notes = "根据酒店房型ID查询酒店房型图片" +
           "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
           "<p>错误码：</p>" +
           "<p>100301 : 获取酒店房型图片失败 </p>" +
           "<p>100302 : 酒店房型id不能为空</p>")*/
    @RequestMapping(value = "/getimg/{targetId}")
    public Dto<Object> getImgByTargetId(@PathVariable String targetId) {
        System.out.println("根据targetId查询酒店房型图片...");
        Dto<Object> dto=new Dto<>();
        List<ItripImageVO> itripImageVOS=null;
        if (EmptyUtils.isNotEmpty(targetId)){
            Map<String,Object> param=new HashMap<>();
            param.put("type",1);
            param.put("targetId",targetId);
            try {
                itripImageVOS=itripImageService.getItripImageListByMap(param);
                dto=DtoUtil.returnDataSuccess(itripImageVOS);
            } catch (Exception e) {
                e.printStackTrace();
                dto=DtoUtil.returnFail("获取评论图片失败","100012");
            }
        }else {
            dto=DtoUtil.returnFail("评论id不能为空","100013");
        }
        return dto;
    }
}
