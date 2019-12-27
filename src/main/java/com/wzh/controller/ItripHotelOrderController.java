package com.wzh.controller;

import com.alibaba.fastjson.JSONObject;
import com.wzh.po.*;
import com.wzh.service.ItripHotelOrderService;
import com.wzh.service.ItripHotelRoomService;
import com.wzh.service.ItripHotelService;
import com.wzh.service.ItripHotelTempStoreService;
import com.wzh.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/hotelorder")
public class ItripHotelOrderController {
    private Jedis jedis = new Jedis("127.0.0.1", 6379);
    @Autowired
    private ItripHotelTempStoreService itripHotelTempStoreService;
    @Autowired
    private ItripHotelRoomService itripHotelRoomService;
    @Autowired
    private ItripHotelService itripHotelService;
    @Autowired
    private ItripHotelOrderService itripHotelOrderService;

    public ItripHotelOrderService getItripHotelOrderService() {
        return itripHotelOrderService;
    }

    public void setItripHotelOrderService(ItripHotelOrderService itripHotelOrderService) {
        this.itripHotelOrderService = itripHotelOrderService;
    }

    public ItripHotelService getItripHotelService() {

        return itripHotelService;
    }

    public void setItripHotelService(ItripHotelService itripHotelService) {
        this.itripHotelService = itripHotelService;
    }

    public ItripHotelRoomService getItripHotelRoomService() {
        return itripHotelRoomService;
    }

    public void setItripHotelRoomService(ItripHotelRoomService itripHotelRoomService) {
        this.itripHotelRoomService = itripHotelRoomService;
    }

    public ItripHotelTempStoreService getItripHotelTempStoreService() {
        return itripHotelTempStoreService;
    }

    public void setItripHotelTempStoreService(ItripHotelTempStoreService itripHotelTempStoreService) {
        this.itripHotelTempStoreService = itripHotelTempStoreService;
    }

    /* @ApiOperation(value = "生成订单前,获取预订信息", httpMethod = "POST",
           protocols = "HTTP", produces = "application/json",
           response = Dto.class, notes = "生成订单前,获取预订信息" +
           "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
           "<p>错误码：</p>" +
           "<p>100000 : token失效，请重登录 </p>" +
           "<p>100510 : hotelId不能为空</p>" +
           "<p>100511 : roomId不能为空</p>" +
           "<p>100512 : 暂时无房</p>" +
           "<p>100513 : 系统异常</p>")*/
    @RequestMapping(value = "/getpreorderinfo")
    public Dto<RoomStoreVO> getPreOrderInfo(@RequestBody ValidateRoomStoreVO validateRoomStoreVO, HttpServletRequest request) {
        System.out.println("订单生成前，酒店信息获取展示。。。。");
        String token = request.getHeader("token");
        //通过token获取json格式用户对象
        JSONObject jsonObject = JSONObject.parseObject(jedis.get(token).toString());
        //将json对象转换为用户对象
        ItripUser itripUser = JSONObject.toJavaObject(jsonObject, ItripUser.class);
        //通过酒店ID和房型ID查询库存
        ItripHotel itripHotel = null;
        ItripHotelRoom itripHotelRoom = null;
        //返回到前台的对象
        RoomStoreVO roomStoreVO = null;
        if (EmptyUtils.isEmpty(itripUser)) {
            return DtoUtil.returnFail("token失效，请重新登陆", "100000");
        }
        if (EmptyUtils.isEmpty(validateRoomStoreVO.getHotelId())) {
            return DtoUtil.returnFail("hotelId不能为空", "100510");
        } else if (EmptyUtils.isEmpty(validateRoomStoreVO.getRoomId())) {
            return DtoUtil.returnFail("roomId不能为空", "100511");
        } else {
            roomStoreVO = new RoomStoreVO();
        }
        try {
            //酒店
            itripHotel = itripHotelService.getItripHotelById(validateRoomStoreVO.getHotelId());
            //房型
            itripHotelRoom = itripHotelRoomService.getItripHotelRoomById(validateRoomStoreVO.getRoomId());
            Map<String, Object> param = new HashMap<>();
            param.put("hotelId", validateRoomStoreVO.getHotelId());
            param.put("roomId", validateRoomStoreVO.getRoomId());
            param.put("startTime", validateRoomStoreVO.getCheckInDate());
            param.put("endTime", validateRoomStoreVO.getCheckOutDate());
            //传到前台的数据
            roomStoreVO.setCheckInDate(validateRoomStoreVO.getCheckInDate());
            roomStoreVO.setCheckOutDate(validateRoomStoreVO.getCheckOutDate());
            roomStoreVO.setHotelName(itripHotel.getHotelName());
            roomStoreVO.setHotelId(validateRoomStoreVO.getHotelId());
            roomStoreVO.setRoomId(itripHotelRoom.getId());
            roomStoreVO.setPrice(itripHotelRoom.getRoomPrice());
            roomStoreVO.setCount(1);
          /*Map<String,Object> map=new HashMap<>();
            map.put("hotelId", validateRoomStoreVO.getHotelId());
            map.put("roomId",validateRoomStoreVO.getRoomId());
            //查询该酒店所有预定状态的订单（与自己要预定的时间匹配，展示房间库存）
            List<ItripHotelOrder> itripHotelOrderList=itripHotelOrderService.getItripHotelOrderByhotelId(map);
            if (EmptyUtils.isNotEmpty(itripHotelOrderList)){
                for (ItripHotelOrder itripHotelOrder:itripHotelOrderList){
                    if (itripHotelOrder.getOrderStatus()!=4){
                    if (validateRoomStoreVO.getCheckInDate().getTime()==itripHotelOrder.getCheckInDate().getTime()&&validateRoomStoreVO.getCheckOutDate().getTime()==itripHotelOrder.getCheckOutDate().getTime()){
                        List<ItripHotelTempStore> itripHotelTempStoreList = itripHotelTempStoreService.getItripHotelTempStoreListByMap(param);
                        if (EmptyUtils.isNotEmpty(itripHotelTempStoreList)){
                            for (ItripHotelTempStore itripHotelTempStore:itripHotelTempStoreList){
                                if (itripHotelTempStore.getStore()!=20){
                                    String storeid=String.valueOf(itripHotelTempStore.getId());
                                    jedis.setex("storeid",7200,storeid);
                                    roomStoreVO.setStore(itripHotelTempStore.getStore());
                                }
                            }
                        }else {
                            return DtoUtil.returnFail("暂时无房", "100512");
                        }
                    }
                    }else {
                        List<ItripHotelTempStore> itripHotelTempStoreList = itripHotelTempStoreService.getItripHotelTempStoreListByMap(param);
                        if (EmptyUtils.isEmpty(itripHotelTempStoreList)) {
                            return DtoUtil.returnFail("暂时无房", "100512");
                        } else {
                            for (ItripHotelTempStore itripHotelTempStore : itripHotelTempStoreList) {
                                if (itripHotelTempStore.getStore()==20) {
                                    String storeid = String.valueOf(itripHotelTempStore.getId());
                                    jedis.setex("storeid", 7200, storeid);
                                    roomStoreVO.setStore(itripHotelTempStore.getStore());
                                    return DtoUtil.returnSuccess("获取成功", roomStoreVO);
                                }
                            }
                        }
                    }
                }*/
            List<ItripHotelTempStore> itripHotelTempStoreList = itripHotelTempStoreService.getItripHotelTempStoreListByMap(param);
            if (EmptyUtils.isEmpty(itripHotelTempStoreList)) {
                return DtoUtil.returnFail("暂时无房", "100512");
            } else {
                for (ItripHotelTempStore itripHotelTempStore : itripHotelTempStoreList) {

                        String storeid = String.valueOf(itripHotelTempStore.getId());
                        jedis.setex("storeid", 7200, storeid);
                        roomStoreVO.setStore(itripHotelTempStore.getStore());
                        return DtoUtil.returnSuccess("获取成功", roomStoreVO);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常", "100513");
        }
        return null;
    }

    /*@ApiOperation(value = "生成订单", httpMethod = "POST",
           protocols = "HTTP", produces = "application/json",
           response = Dto.class, notes = "生成订单" +
           "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
           "<p>错误码：</p>" +
           "<p>100505 : 生成订单失败 </p>" +
           "<p>100506 : 不能提交空，请填写订单信息 </p>" +
           "<p>100507 : 库存不足 </p>" +
           "<p>100000 : token失效，请重登录</p>")*/
    @RequestMapping(value = "/addhotelorder")
    public Dto<Object> addHotelOrder(@RequestBody ItripAddHotelOrderVO itripAddHotelOrderVO, HttpServletRequest request) {
        System.out.println("生成未付款订单。。。。");
        Dto<Object> dto = new Dto<Object>();
        String token=request.getHeader("token");
        //通过token获取json格式用户对象
        JSONObject jsonObject=JSONObject.parseObject(jedis.get(token).toString());
        //将json对象转换为用户对象
        ItripUser itripUser= JSONObject.toJavaObject(jsonObject,ItripUser.class);
        //条件字符串
        Map<String,Object> param=new HashMap<>();
        param.put("hotelId",itripAddHotelOrderVO.getHotelId());
        param.put("startTime",itripAddHotelOrderVO.getCheckInDate());
        param.put("endTime",itripAddHotelOrderVO.getCheckOutDate());
        param.put("roomId",itripAddHotelOrderVO.getRoomId());
        param.put("count",itripAddHotelOrderVO.getCount());
        List<ItripUserLinkUser> linkUserList=itripAddHotelOrderVO.getLinkUser();
        if (EmptyUtils.isEmpty(itripUser)){
            return DtoUtil.returnFail("token失效，请重新登录","100000");
        }
        try {
            //判断是否有库存
            boolean flag=itripHotelTempStoreService.vailDateRoomStote(param);
            if (flag&&itripAddHotelOrderVO!=null){
                //订单添加
                //计算预定天数
                Integer days=DateUtil.getBetweenDates(itripAddHotelOrderVO.getCheckInDate(),itripAddHotelOrderVO.getCheckOutDate()).size()-1;
                if(days<=0){
                    return DtoUtil.returnFail("退房时间必须大于入住时间","100505");
                }
                ItripHotelOrder itripHotelOrder=new ItripHotelOrder();
                itripHotelOrder.setId(itripAddHotelOrderVO.getId());
                itripHotelOrder.setUserId(itripUser.getId());
                itripHotelOrder.setOrderType(itripAddHotelOrderVO.getOrderType());
                itripHotelOrder.setHotelId(itripAddHotelOrderVO.getHotelId());
                itripHotelOrder.setHotelName(itripAddHotelOrderVO.getHotelName());
                itripHotelOrder.setRoomId(itripAddHotelOrderVO.getRoomId());
                itripHotelOrder.setCount(itripAddHotelOrderVO.getCount());
                itripHotelOrder.setCheckInDate(itripAddHotelOrderVO.getCheckInDate());
                itripHotelOrder.setCheckOutDate(itripAddHotelOrderVO.getCheckOutDate());
                itripHotelOrder.setNoticePhone(itripAddHotelOrderVO.getNoticePhone());
                itripHotelOrder.setNoticeEmail(itripAddHotelOrderVO.getNoticeEmail());
                itripHotelOrder.setSpecialRequirement(itripAddHotelOrderVO.getSpecialRequirement());
                itripHotelOrder.setIsNeedInvoice(itripAddHotelOrderVO.getIsNeedInvoice());
                itripHotelOrder.setInvoiceHead(itripAddHotelOrderVO.getInvoiceHead());
                itripHotelOrder.setInvoiceType(itripAddHotelOrderVO.getInvoiceType());
                itripHotelOrder.setCreatedBy(itripUser.getId());
                StringBuilder linkUserName=new StringBuilder();
                int size=linkUserList.size();
                for (int i=0;i<size;i++){
                    if (size-1>=1){
                        linkUserName.append(linkUserList.get(i).getLinkUserName()+",");
                    }else {
                        linkUserName.append(linkUserList.get(i).getLinkUserName());
                    }
                }
                itripHotelOrder.setLinkUserName(linkUserName.toString());
                itripHotelOrder.setBookingDays(days);
                if (token.startsWith("token:PC" )){
                    itripHotelOrder.setBookType(0);
                }
                //支付前生成订单初始状态（未支付）
                itripHotelOrder.setOrderStatus(0);
                StringBuilder md5String=new StringBuilder();
                md5String.append(Math.random()*1000000000);
                String md5=MD5Util.getMd5(md5String.toString(),6);
                StringBuilder orderNo=new StringBuilder();
                orderNo.append("D1000001");
                orderNo.append(DateUtil.format(new Date(),"yyyyMMddHHmmss"));
                orderNo.append(md5);
                itripHotelOrder.setOrderNo(orderNo.toString());
                //计算总金额
                itripHotelOrder.setPayAmount(itripHotelOrderService.getItripHotelOrderPayAmount(itripAddHotelOrderVO.getCount()*days,itripAddHotelOrderVO.getRoomId()));
                //添加
                Map<String,String> map=itripHotelOrderService.insertItripHotelOrder(itripHotelOrder,linkUserList);
                dto=DtoUtil.returnSuccess("订单生成成功",map);
                //处理库存
                if(map.size()>0){
                    Map<String,Object> map1=new HashMap<>();
                    Long id=Long.valueOf(jedis.get("storeid"));
                    map1.put("id",id);
                    map1.put("count",itripAddHotelOrderVO.getCount());
                    itripHotelTempStoreService.updateRoomStore(map1);
                }
            }else if (flag&&itripAddHotelOrderVO==null){
                dto=DtoUtil.returnFail("不能提交空，请填写订单信息","100506");
            }else {
                dto=DtoUtil.returnFail("库存不足","100507");
            }
        } catch (Exception e) {
            e.printStackTrace();
            dto=DtoUtil.returnFail("生成订单异常","100505");
        }
        return dto;
    }
    /* @ApiOperation(value = "根据个人订单列表，并分页显示", httpMethod = "POST",
           protocols = "HTTP", produces = "application/json",
           response = Dto.class, notes = "根据条件查询个人订单列表，并分页显示" +
           "<p>订单类型(orderType)（-1：全部订单 0:旅游订单 1:酒店订单 2：机票订单）：</p>" +
           "<p>订单状态(orderStatus)（0：待支付 1:已取消 2:支付成功 3:已消费 4：已点评）：</p>" +
           "<p>对于页面tab条件：</p>" +
           "<p>全部订单（orderStatus：-1）</p>" +
           "<p>未出行（orderStatus：2）</p>" +
           "<p>待付款（orderStatus：0）</p>" +
           "<p>待评论（orderStatus：3）</p>" +
           "<p>已取消（orderStatus：1）</p>" +
           "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
           "<p>错误码：</p>" +
           "<p>100501 : 请传递参数：orderType </p>" +
           "<p>100502 : 请传递参数：orderStatus </p>" +
           "<p>100503 : 获取个人订单列表错误 </p>" +
           "<p>100000 : token失效，请重登录 </p>")*/
    @RequestMapping(value = "/getpersonalorderlist")
    public Dto<Object> getPersonalOrderList(@RequestBody ItripSearchOrderVO itripSearchOrderVO,HttpServletRequest request) {
        System.out.println("根据用户信息查看订单列表，并分页显示");
        Dto<Object> dto=null;
        //订单类型（酒店订单：1）和状态
        Integer orderType=itripSearchOrderVO.getOrderType();
        Integer orderStatus=itripSearchOrderVO.getOrderStatus();
        //通过token获取用户信息
        String token=request.getHeader("token");
        //通过token获取json格式用户对象
        JSONObject jsonObject=JSONObject.parseObject(jedis.get(token).toString());
        //将json对象转换为用户对象
        ItripUser itripUser= JSONObject.toJavaObject(jsonObject,ItripUser.class);
        if (EmptyUtils.isNotEmpty(itripUser)){
            if (EmptyUtils.isEmpty(orderType)){
                dto=DtoUtil.returnFail("请传递参数：orderType","100501");
            }
            if (EmptyUtils.isEmpty(orderStatus)){
                dto=DtoUtil.returnFail("请传递参数：orderStatus","100502");
            }
            Map<String,Object> param=new HashMap<>();
            param.put("orderType",orderType==-1?null:orderType);
            param.put("orderStatus",orderStatus==-1?null:orderStatus);
            param.put("userId",itripUser.getId());
            param.put("orderNo",itripSearchOrderVO.getOrderNo());
            param.put("linkUserName",itripSearchOrderVO.getLinkUserName());
            param.put("startDate",itripSearchOrderVO.getStartDate());
            param.put("endDate",itripSearchOrderVO.getEndDate());
            try {
                Page page=itripHotelOrderService.getOrderListByMap(param,itripSearchOrderVO.getPageNo(),itripSearchOrderVO.getPageSize());
                List<ItripListHotelOrderVO> orderVOList=page.getRows();
                for (int i=0;i<orderVOList.size();i++){
                    ItripListHotelOrderVO listHotelOrderVO=new ItripListHotelOrderVO();
                    listHotelOrderVO=orderVOList.get(i);
                    listHotelOrderVO.setCreationDate(new SimpleDateFormat("yyyy-MM-dd").format(listHotelOrderVO.getCheckInString()));
                    listHotelOrderVO.setCheckInDate(new SimpleDateFormat("yyyy-MM-dd").format(listHotelOrderVO.getCreationString()));
                    orderVOList.set(i,listHotelOrderVO);
                }
                page.setRows(orderVOList);
                dto=DtoUtil.returnSuccess("获取订单信息成功",page);
            } catch (Exception e) {
                e.printStackTrace();
                dto=DtoUtil.returnFail("获取个人订单列表错误","100503");
            }
        }else {
            dto=DtoUtil.returnFail("token失效，请重登录","100000");
        }
        return dto;
    }
    /* @ApiOperation(value = "根据订单ID查看个人订单详情", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据订单ID查看个人订单详情" +
            "<p>订单状态(orderStatus)（0：待支付 1:已取消 2:支付成功 3:已消费 4：已点评）：</p>" +
            "<p>订单流程：</p>" +
            "<p>订单状态(0：待支付 2:支付成功 3:已消费 4:已点评)的流程：{\"1\":\"订单提交\",\"2\":\"订单支付\",\"3\":\"支付成功\",\"4\":\"入住\",\"5\":\"订单点评\",\"6\":\"订单完成\"}</p>" +
            "<p>订单状态(1:已取消)的流程：{\"1\":\"订单提交\",\"2\":\"订单支付\",\"3\":\"订单取消\"}</p>" +
            "<p>支持支付类型(roomPayType)：{\"1\":\"在线付\",\"2\":\"线下付\",\"3\":\"不限\"}</p>" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100525 : 请传递参数：orderId </p>" +
            "<p>100526 : 没有相关订单信息 </p>" +
            "<p>100527 : 获取个人订单信息错误 </p>" +
            "<p>100000 : token失效，请重登录 </p>")*/
    @RequestMapping(value = "/getpersonalorderinfo/{orderId}")
    public Dto<Object> getPersonalOrderInfo(@PathVariable String orderId, HttpServletRequest request) {
        System.out.println("根据订单ID,查看个人订单详情。。。。");
        Dto<Object> dto = null;
        //通过token获取用户信息
        String token=request.getHeader("token");
        //通过token获取json格式用户对象
        JSONObject jsonObject=JSONObject.parseObject(jedis.get(token).toString());
        //将json对象转换为用户对象
        ItripUser itripUser= JSONObject.toJavaObject(jsonObject,ItripUser.class);
        if (EmptyUtils.isNotEmpty(itripUser)){
            if (EmptyUtils.isNotEmpty(orderId)){
                try {
                    ItripHotelOrder itripHotelOrder=itripHotelOrderService.getItripHotelOrderById(Long.valueOf(orderId));
                    if (EmptyUtils.isNotEmpty(itripHotelOrder)){
                        ItripPersonalHotelOrderVO itripPersonalHotelOrderVO=new ItripPersonalHotelOrderVO();
                        itripPersonalHotelOrderVO.setId(itripHotelOrder.getId());
                        itripPersonalHotelOrderVO.setOrderNo(itripHotelOrder.getOrderNo());
                        itripPersonalHotelOrderVO.setCreationDate(new SimpleDateFormat("yyyy-MM-dd").format(itripHotelOrder.getCreationDate()));
                        itripPersonalHotelOrderVO.setPayAmount(itripHotelOrder.getPayAmount());
                        itripPersonalHotelOrderVO.setBookType(itripHotelOrder.getBookType());
                        itripPersonalHotelOrderVO.setNoticePhone(itripHotelOrder.getNoticePhone());
                        itripPersonalHotelOrderVO.setPayType(itripHotelOrder.getPayType());
                        //查询预定房间信息
                        ItripHotelRoom itripHotelRoom=itripHotelRoomService.getItripHotelRoomById(itripHotelOrder.getRoomId());
                        if (EmptyUtils.isNotEmpty(itripHotelRoom)){
                            itripPersonalHotelOrderVO.setRoomPayType(itripHotelRoom.getPayType());
                        }
                        //订单状态
                        Integer orderStatus=itripHotelOrder.getOrderStatus();
                        itripPersonalHotelOrderVO.setOrderStatus(orderStatus);
                        if (orderStatus==1){
                            itripPersonalHotelOrderVO.setProcessNode("3");//订单取消
                        }else  if (orderStatus==0){
                            itripPersonalHotelOrderVO.setProcessNode("2");//订单支付
                        }else  if (orderStatus==2){
                            itripPersonalHotelOrderVO.setProcessNode("3");//订单支付成功
                        }else  if (orderStatus==3){
                            itripPersonalHotelOrderVO.setProcessNode("5");//订单评论
                        }else  if (orderStatus==4){
                            itripPersonalHotelOrderVO.setProcessNode("6");//订单完成
                        }else {
                            itripPersonalHotelOrderVO.setProcessNode(null);
                            itripPersonalHotelOrderVO.setOrderProcess(null);
                        }
                        dto=DtoUtil.returnSuccess("订单信息获取成功",itripPersonalHotelOrderVO);
                    }else {
                        dto=DtoUtil.returnFail("没有相关订单信息","100526");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dto=DtoUtil.returnFail("获取个人订单信息错误","100527");
                }
            }else {
                dto=DtoUtil.returnFail("请传递参数：orderId ","100525");
            }
        }else {
            dto=DtoUtil.returnFail("token失效，请重登录","100000");
        }
        return dto;
    }
    /*@ApiOperation(value = "根据订单ID查看个人订单详情-房型相关信息", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据订单ID查看个人订单详情-房型相关信息" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100529 : 请传递参数：orderId </p>" +
            "<p>100530 : 没有相关订单房型信息 </p>" +
            "<p>100531 : 获取个人订单房型信息错误 </p>" +
            "<p>支持支付类型(roomPayType)：{\"1\":\"在线付\",\"2\":\"线下付\",\"3\":\"不限\"}</p>" +
            "<p>100000 : token失效，请重登录 </p>")*/
    @RequestMapping(value = "/getpersonalorderroominfo/{orderId}")
    public Dto<Object> getPersonalOrderRoomInfo(@PathVariable String orderId, HttpServletRequest request) {
        System.out.println("根据订单ID查看个人订单详情-房型相关信息。。。。");
        Dto<Object> dto = null;
        //通过token获取用户信息
        String token=request.getHeader("token");
        //通过token获取json格式用户对象
        JSONObject jsonObject=JSONObject.parseObject(jedis.get(token).toString());
        //将json对象转换为用户对象
        ItripUser itripUser= JSONObject.toJavaObject(jsonObject,ItripUser.class);
        if (EmptyUtils.isNotEmpty(itripUser)){
            if (EmptyUtils.isNotEmpty(orderId)){
                try {
                    ItripPersonalOrderRoomVO itripPersonalOrderRoom=itripHotelOrderService.getItripHotelOrderRoomInfoById(Long.valueOf(orderId));
                    if (EmptyUtils.isNotEmpty(itripPersonalOrderRoom)){
                        itripPersonalOrderRoom.setCheckInDate(itripPersonalOrderRoom.getCheckInDate().substring(0,10));
                        itripPersonalOrderRoom.setCheckOutDate(itripPersonalOrderRoom.getCheckOutDate().substring(0,10));
                        dto=DtoUtil.returnSuccess("获取订单房型信息成功",itripPersonalOrderRoom);
                    }else {
                        dto=DtoUtil.returnFail("没有相关订单房型信息","100530");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dto=DtoUtil.returnFail("获取个人订单房型信息错误","100531");
                }
            }else {
                dto=DtoUtil.returnFail("请传递参数：orderId ","100525");
            }
        }else {
            dto=DtoUtil.returnFail("token失效，请重登录","100000");
        }
        return dto;
    }
    /* @ApiOperation(value = "根据订单ID获取订单信息", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据订单ID获取订单信息" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>100533 : 没有查询到相应订单 </p>" +
            "<p>100534 : 系统异常 </p>")*/
    @RequestMapping(value = "/queryOrderById/{orderId}")
    public Dto<Object> queryOrderById(@PathVariable Long orderId,HttpServletRequest request) {
        System.out.println("订单修改。。。。");
        Dto<Object> dto = null;
        //通过token获取用户信息
        String token=request.getHeader("token");
        //通过token获取json格式用户对象
        JSONObject jsonObject=JSONObject.parseObject(jedis.get(token).toString());
        //将json对象转换为用户对象
        ItripUser itripUser= JSONObject.toJavaObject(jsonObject,ItripUser.class);
        if (EmptyUtils.isNotEmpty(itripUser)){
            if (EmptyUtils.isNotEmpty(orderId)){
                try {
                    ItripModifyHotelOrderVO itripModifyHotelOrderVO=new ItripModifyHotelOrderVO();
                    ItripHotelOrder itripHotelOrder=itripHotelOrderService.getItripHotelOrderById(orderId);
                    if (EmptyUtils.isNotEmpty(itripHotelOrder)){
                        BeanUtils.copyProperties(itripHotelOrder,itripModifyHotelOrderVO);
                        Map<String,Object> param=new HashMap<>();
                        param.put("orderId",orderId);
                        List<ItripOrderLinkUserVo> itripOrderLinkUserVos=itripHotelOrderService.getItripOrderLinkUserListByMap(param);
                        itripModifyHotelOrderVO.setItripOrderLinkUserList(itripOrderLinkUserVos);
                        dto=DtoUtil.returnSuccess("获取订单信息成功",itripModifyHotelOrderVO);
                    }else {
                        dto=DtoUtil.returnFail("没有查询到相应订单","100533");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dto=DtoUtil.returnFail("系统异常","100534");
                }
            }else {
                dto=DtoUtil.returnFail("请传递参数：orderId ","100525");
            }
        }else{
            dto=DtoUtil.returnFail("token失效，请重登录","100000");
        }
        return dto;
    }
}
