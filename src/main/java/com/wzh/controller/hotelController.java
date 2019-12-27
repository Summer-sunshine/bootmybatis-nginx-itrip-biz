package com.wzh.controller;

import com.wzh.po.Dto;
import com.wzh.po.ItripAreaDic;
import com.wzh.po.ItripLabelDic;
import com.wzh.service.ItripAreaDicService;
import com.wzh.service.ItripHotelService;
import com.wzh.service.ItripImageService;
import com.wzh.service.ItripLabelDicService;
import com.wzh.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/hotel")
public class hotelController {
    @Autowired
    private ItripAreaDicService itripAreaDicService;
    @Autowired
    private ItripLabelDicService itripLabelDicService;
    @Autowired
    private ItripHotelService itripHotelService;
    @Autowired
    private ItripImageService itripImageService;

    public ItripImageService getItripImageService() {
        return itripImageService;
    }

    public void setItripImageService(ItripImageService itripImageService) {
        this.itripImageService = itripImageService;
    }

    public ItripHotelService getItripHotelService() {
        return itripHotelService;
    }

    public void setItripHotelService(ItripHotelService itripHotelService) {
        this.itripHotelService = itripHotelService;
    }

    public ItripLabelDicService getItripLabelDicService() {
        return itripLabelDicService;
    }

    public void setItripLabelDicService(ItripLabelDicService itripLabelDicService) {
        this.itripLabelDicService = itripLabelDicService;
    }

    public ItripAreaDicService getItripAreaDicService() {
        return itripAreaDicService;
    }

    public void setItripAreaDicService(ItripAreaDicService itripAreaDicService) {
        this.itripAreaDicService = itripAreaDicService;
    }

    /****r
     * 查询热门城市
     * @param type
     * @return
     * @throws Exception
     */
   /* @ApiOperation(value = "查询热门城市", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "查询国内、国外的热门城市(1:国内 2:国外)" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>10201 : hotelId不能为空 </p>" +
            "<p>10202 : 系统异常,获取失败</p>")*/
    @RequestMapping(value = "/queryhotcity/{type}")
    @ResponseBody
    public Dto<ItripAreaDicVO> queryHotCity(@PathVariable Integer type) {
        System.out.println("查询热门城市。。。。");
        List<ItripAreaDic> itripAreaDics = null;
        List<ItripAreaDicVO> itripAreaDicVOS = null;
        try {
            //前台数据判空判null
            if (EmptyUtils.isNotEmpty(type)) {
                Map param = new HashMap();
                param.put("isHot", 1);
                param.put("isChina", type);
                itripAreaDics = itripAreaDicService.getItripAreaDicListByMap(param);
                //封装数据到前台
                if (EmptyUtils.isNotEmpty(itripAreaDics)) {
                    itripAreaDicVOS = new ArrayList<>();
                    for (ItripAreaDic dic : itripAreaDics) {
                        ItripAreaDicVO vo = new ItripAreaDicVO();
                        //属性对象赋值
                        BeanUtils.copyProperties(dic, vo);
                        itripAreaDicVOS.add(vo);
                    }
                } else {
                    return DtoUtil.returnFail("城市Id不能为空", "10201");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10202");
        }
        return DtoUtil.returnDataSuccess(itripAreaDicVOS);
    }

    /***
     * 查询酒店特色列表
     * @return
     * @throws Exception
     */
   /* @ApiOperation(value = "查询酒店特色列表", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "获取酒店特色(用于查询页列表)" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码: </p>" +
            "<p>10205: 系统异常,获取失败</p>")*/
    @RequestMapping(value = "/queryhotelfeature")
    @ResponseBody
    public Dto<ItripLabelDicVO> queryHotelFeature() {
        System.out.println("查询酒店特色列表。。。。");
        List<ItripLabelDic> itripLabelDics = null;
        List<ItripLabelDicVO> itripAreaDicVOS = null;
        try {
            Map param = new HashMap();
            param.put("parentId", 16);
            itripLabelDics = itripLabelDicService.getItripLabelDicListByMap(param);
            //判断itripLabelDics是否为空
            if (EmptyUtils.isNotEmpty(itripLabelDics)) {
                itripAreaDicVOS = new ArrayList<>();
                //遍历集合
                for (ItripLabelDic dic : itripLabelDics) {
                    ItripLabelDicVO vo = new ItripLabelDicVO();
                    BeanUtils.copyProperties(dic, vo);
                    itripAreaDicVOS.add(vo);
                }
            } else {
                return DtoUtil.returnFail("城市Id不能为空", "10201");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10205");
        }
        return DtoUtil.returnDataSuccess(itripAreaDicVOS);
    }

    /***
     * 根据酒店id查询酒店特色和介绍 -add by donghai
     *
     * @return
     * @throws Exception
     */
  /*  @ApiOperation(value = "根据酒店id查询酒店特色和介绍", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询酒店特色和介绍" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>10210: 酒店id不能为空</p>" +
            "<p>10211: 系统异常,获取失败</p>")*/
    @RequestMapping(value = "/queryhoteldetails/{id}")
    @ResponseBody
    public Dto<ItripSearchFacilitiesHotelVO> queryHotelDetails(@PathVariable Long id) {
        System.out.println("根据酒店id查询特色和介绍....");
        List<ItripSearchDetailsHotelVO> itripSearchDetailsHotelVOS=null;
        try {
            //判断前台数据是否为空
            if (EmptyUtils.isNotEmpty(id)){
                itripSearchDetailsHotelVOS=itripHotelService.queryHotelDetails(id);
                return DtoUtil.returnDataSuccess(itripSearchDetailsHotelVOS);
            }else {
                return DtoUtil.returnFail("城市Id不能为空", "10210");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10211");
        }
    }
    /***
     * 根据酒店id查询酒店设施 -add by donghai
     * @return
     * @throws Exception
     */
  /*  @ApiOperation(value = "根据酒店id查询酒店设施", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询酒店设施" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>10206: 酒店id不能为空</p>" +
            "<p>10207: 系统异常,获取失败</p>")*/
    @RequestMapping(value = "/queryhotelfacilities/{id}")
    @ResponseBody
    public Dto<ItripSearchFacilitiesHotelVO> queryHotelFacilities(@PathVariable Long id) {
        System.out.println("根据酒店id查询酒店设施....");
        ItripSearchFacilitiesHotelVO itripSearchFacilitiesHotelVO=null;
        try {
            //判断前台数据是否为空
            if(EmptyUtils.isNotEmpty(id)){
                itripSearchFacilitiesHotelVO=itripHotelService.getItripHotelFacilitiesById(id);
                return DtoUtil.returnDataSuccess(itripSearchFacilitiesHotelVO.getFacilities());
            }else {
                return DtoUtil.returnFail("酒店id不能为空", "10206");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10207");
        }
    }
    /***
     * 根据酒店id查询酒店政策 -add by donghai
     * @return
     * @throws Exception
     */
   /* @ApiOperation(value = "根据酒店id查询酒店政策", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据酒店id查询酒店政策" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>10208: 酒店id不能为空</p>" +
            "<p>10209: 系统异常,获取失败</p>")*/
    @RequestMapping(value = "/queryhotelpolicy/{id}")
    @ResponseBody
    public Dto<ItripSearchPolicyHotelVO> queryHotelPolicy(@PathVariable Long id) {
        System.out.println("根据酒店id查询酒店政策。。。。");
        ItripSearchPolicyHotelVO itripSearchPolicyHotelVO=null;
        try {
            //判断前台数据是否为空
            if (EmptyUtils.isNotEmpty(id)){
                itripSearchPolicyHotelVO=itripHotelService.queryHotelPolicy(id);
                return DtoUtil.returnDataSuccess(itripSearchPolicyHotelVO.getHotelPolicy());
            }else {
                return DtoUtil.returnFail("酒店id不能为空", "10208");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常,获取失败", "10209");
        }
    }
    /***
     * 查询商圈的接口
     *
     * @param cityId 根据城市查询商圈
     * @return
     * @throws Exception
     */
   /* @ApiOperation(value = "查询商圈", httpMethod = "GET",
            protocols = "HTTP", produces = "application/json",
            response = Dto.class, notes = "根据城市查询商圈" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>" +
            "<p>10203 : cityId不能为空 </p>" +
            "<p>10204 : 系统异常,获取失败</p>")*/
    @RequestMapping(value = "/querytradearea/{cityId}")
    public Dto<ItripAreaDicVO> queryTradeArea(@PathVariable Long cityId) {
        System.out.println("根据城市查询商圈....");
        List<ItripAreaDic> itripAreaDics=null;
        List<ItripAreaDicVO> itripAreaDicVOS=null;
        if(EmptyUtils.isNotEmpty(cityId)){
            Map param=new HashMap();
            param.put("isTradingArea",1);
            param.put("parent",cityId);
            try {
                itripAreaDics=itripAreaDicService.getItripAreaDicListByMap(param);
                if (EmptyUtils.isNotEmpty(itripAreaDics)){
                    itripAreaDicVOS=new ArrayList<>();
                    for (ItripAreaDic itripAreaDic:itripAreaDics){
                        ItripAreaDicVO itripAreaDicVO=new ItripAreaDicVO();
                        BeanUtils.copyProperties(itripAreaDic,itripAreaDicVO);
                        itripAreaDicVOS.add(itripAreaDicVO);
                    }
                    return DtoUtil.returnDataSuccess(itripAreaDicVOS);
                }else {
                    return DtoUtil.returnFail("cityId不能为空","10203");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("系统异常,获取失败","10204");
            }
        }
        return null;
    }
    /*   @ApiOperation(value = "根据酒店id查询酒店特色、商圈、酒店名称", httpMethod = "GET",
           protocols = "HTTP", produces = "application/json",
           response = Dto.class, notes = "根据酒店id查询酒店特色、商圈、酒店名称（视频文字描述）" +
           "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
           "<p>错误码：</p>" +
           "<p>100214 : 获取酒店视频文字描述失败 </p>" +
           "<p>100215 : 酒店id不能为空</p>")*/
    @RequestMapping(value = "/getvideodesc/{hotelId}")
    public Dto<Object> getVideoDescByHotelId(@PathVariable String hotelId) {
        System.out.println("根据酒店id查询酒店视频文字....");
        Dto<Object> dto=new Dto<>();
        if (EmptyUtils.isNotEmpty(hotelId)){
            HotelVideoDescVO hotelVideoDescVO=null;
            try {
                hotelVideoDescVO=itripHotelService.getVideoDescById(Long.valueOf(hotelId));
                dto=DtoUtil.returnSuccess("视频文字获取成功",hotelVideoDescVO);
            } catch (Exception e) {
                e.printStackTrace();
                dto=DtoUtil.returnFail("获取酒店视频文字描述失败","100214");
            }
        }else {
            dto=DtoUtil.returnFail("酒店id不能为空","100215");
        }
        return dto;
    }
    /* @ApiOperation(value = "根据targetId查询酒店图片(type=0)", httpMethod = "GET",
           protocols = "HTTP", produces = "application/json",
           response = Dto.class, notes = "根据酒店ID查询酒店图片" +
           "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
           "<p>错误码：</p>" +
           "<p>100212 : 获取酒店图片失败 </p>" +
           "<p>100213 : 酒店id不能为空</p>")*/
    @RequestMapping(value = "/getimg/{targetId}")
    public Dto<Object> getImgByTargetId(@PathVariable String targetId) {
        System.out.println("根据酒店id查询酒店图片....");
        Dto<Object> dto = new Dto<Object>();
        if (null != targetId && !"".equals(targetId)) {
            List<ItripImageVO> itripImageVOList = null;
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("type", "0");
            param.put("targetId", targetId);
            try {
                itripImageVOList = itripImageService.getItripImageListByMap(param);
                dto = DtoUtil.returnSuccess("获取酒店图片成功", itripImageVOList);
            } catch (Exception e) {
                e.printStackTrace();
                dto = DtoUtil.returnFail("获取酒店图片失败", "100212");
            }

        } else {
            dto = DtoUtil.returnFail("酒店id不能为空", "100213");
        }
        return dto;
    }

}

