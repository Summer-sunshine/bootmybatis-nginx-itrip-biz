package com.wzh.controller;

import com.alibaba.fastjson.JSONObject;
import com.wzh.po.Dto;
import com.wzh.po.ItripUser;
import com.wzh.po.ItripUserLinkUser;
import com.wzh.service.ItripUserLinkUserService;
import com.wzh.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping(value = "/api/userinfo")
public class ItripUserLinkUserController {
    @Autowired
    private ItripUserLinkUserService itripUserLinkUserService;

    public ItripUserLinkUserService getItripUserLinkUserService() {
        return itripUserLinkUserService;
    }

    public void setItripUserLinkUserService(ItripUserLinkUserService itripUserLinkUserService) {
        this.itripUserLinkUserService = itripUserLinkUserService;
    }
    //缓存
    Jedis jedis=new Jedis("127.0.0.1",6379);
    /*  新增常用联系人接口, httpMethod = "POST",
              protocols = "HTTP",produces = "application/json",
             response = Dto.class,notes = "新增常用联系人信息"+
              "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
              "<p>错误码：</p>"+
              "<p>100411 : 新增常用联系人失败 </p>"+
              "<p>100412 : 不能提交空，请填写常用联系人信息</p>"+
              "<p>100000 : token失效，请重登录 </p>")*/
    @RequestMapping(value = "/adduserlinkuser")
    public Dto addUserLinkUser(HttpServletRequest request, HttpServletResponse response, @RequestBody ItripAddUserLinkUserVO itripAddUserLinkUserVO) {
        System.out.println("新增常用联系人..........................");
        System.out.println("前台获取的数据："+itripAddUserLinkUserVO.toString());
        //获取前台的数据
        ItripUserLinkUser itripUserLinkUser=new ItripUserLinkUser();
        itripUserLinkUser.setLinkUserName(itripAddUserLinkUserVO.getLinkUserName());
        itripUserLinkUser.setLinkIdCard(itripAddUserLinkUserVO.getLinkIdCard());
        itripUserLinkUser.setLinkPhone(itripAddUserLinkUserVO.getLinkPhone());
        //从redis中获取登录的账号ID
        String token=jedis.get("token");
        String[] userid=token.split("-");
        itripUserLinkUser.setUserId(Long.parseLong(userid[2]));
        itripUserLinkUser.setLinkIdCardType(itripAddUserLinkUserVO.getLinkIdCardType());
        itripUserLinkUser.setCreationDate(new Date());
        itripUserLinkUser.setCreatedBy(Long.parseLong(userid[2]));
        try {
            int rows=itripUserLinkUserService.addUsersLinkUser(itripUserLinkUser);
            if(rows>0){
                return DtoUtil.returnSuccess("添加成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DtoUtil.returnFail("新增常用联系人失败", "100411");
    }
    /*查询常用联系人*/
    /**
     * 根据UserId,联系人姓名查询常用联系人-add by donghai
     * @return
     * @throws Exception
     */
   /* @ApiOperation(value = "查询常用联系人接口", httpMethod = "POST",
            protocols = "HTTP",produces = "application/json",
            response = Dto.class,notes = "查询常用联系人信息(可根据联系人姓名进行模糊查询)"+
            "<p>若不根据联系人姓名进行查询，不输入参数即可 | 若根据联系人姓名进行查询，须进行相应的入参，比如：{\"linkUserName\":\"张三\"}</p>" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>"+
            "<p>100401 : 获取常用联系人信息失败 </p>"+
            "<p>100000 : token失效，请重登录</p>")*/
    @RequestMapping(value = "/queryuserlinkuser")
    public  Dto<ItripUserLinkUser> queryUserLinkUser(HttpServletRequest request, HttpServletResponse response, @RequestBody ItripSearchUserLinkUserVO itripSearchUserLinkUserVO){
        System.out.println("查询全部常用用户..................................");
        //定义集合接收查询的数据
        List<ItripUserLinkUser> userList=new ArrayList<ItripUserLinkUser>();
        //从redis中获取登录的账号ID
        String token=jedis.get("token");
        JSONObject jsonObject=JSONObject.parseObject(jedis.get(token).toString());
        //将jsonObject字符串转化为user对象
        ItripUser user=JSONObject.toJavaObject(jsonObject,ItripUser.class);
        String linkUserName=itripSearchUserLinkUserVO.getLinkUserName()==null?null:itripSearchUserLinkUserVO.getLinkUserName();
        if(user!=null){
            Map map=new HashMap();
            map.put("userId",user.getId());
            map.put("linkUserName",linkUserName);
            try {
                userList=itripUserLinkUserService.findAllUserLinkUserByMap(map);
                for (int i=0;i<userList.size();i++){
                    System.out.println(userList.get(i).toString());
                }
                return DtoUtil.returnSuccess("查询成功",userList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            return DtoUtil.returnFail("获取常用联系人信息失败 ","100401");
        }
        return null;
    }
    //修改
   /* @ApiOperation(value = "修改常用联系人接口", httpMethod = "POST",
            protocols = "HTTP",produces = "application/json",
            response = Dto.class,notes = "修改常用联系人信息"+
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>"+
            "<p>100421 : 修改常用联系人失败 </p>"+
            "<p>100422 : 不能提交空，请填写常用联系人信息</p>"+
            "<p>100000 : token失效，请重登录 </p>")*/
    @RequestMapping(value="/modifyuserlinkuser")
    public Dto<Object> updateUserLinkUser(@RequestBody ItripModifyUserLinkUserVO itripModifyUserLinkUserVO, HttpServletRequest request, HttpServletResponse response){
        System.out.println("用户信息修改..................");
        System.out.println(itripModifyUserLinkUserVO.toString());
        if(itripModifyUserLinkUserVO!=null){
        //获取前台的数据
        ItripUserLinkUser itripUserLinkUser=new ItripUserLinkUser();
        itripUserLinkUser.setLinkUserName(itripModifyUserLinkUserVO.getLinkUserName());
        itripUserLinkUser.setLinkIdCard(itripModifyUserLinkUserVO.getLinkIdCard());
        itripUserLinkUser.setLinkPhone(itripModifyUserLinkUserVO.getLinkPhone());
        //从redis中获取登录的账号ID
        String token=jedis.get("token");
        String[] userid=token.split("-");
        itripUserLinkUser.setLinkIdCardType(itripModifyUserLinkUserVO.getLinkIdCardType());
        itripUserLinkUser.setId(itripModifyUserLinkUserVO.getId());
        itripUserLinkUser.setModifiedBy(Long.parseLong(userid[2]));
        itripUserLinkUser.setUserId(Long.parseLong(userid[2]));
        itripUserLinkUser.setModifyDate(new Date());
            try {
                int rows=itripUserLinkUserService.updateItripUserLinkUserById(itripUserLinkUser);
                if(rows>0){
                    return DtoUtil.returnSuccess("修改成功");
                }else {
                    return DtoUtil.returnFail("修改常用联系人失败","100421");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            return DtoUtil.returnFail("不能提交空，请填写常用联系人信息","100422");
        }
        return null;
    }
    /*删除*/
   /* @ApiOperation(value = "删除常用联系人接口", httpMethod = "GET",
            protocols = "HTTP",produces = "application/json",
            response = Dto.class,notes = "删除常用联系人信息"+
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>"+
            "<p>100431 : 所选的常用联系人中有与某条待支付的订单关联的项，无法删除 </p>"+
            "<p>100432 : 删除常用联系人失败 </p>"+
            "<p>100433 : 请选择要删除的常用联系人</p>"+
            "<p>100000 : token失效，请重登录 </p>")*/
    @RequestMapping(value="/deluserlinkuser")
    @ResponseBody
    public Dto<Object> delUserLinkUser(HttpServletRequest request, HttpServletResponse response,Long[] ids) {
        System.out.println("删除用户.........");
        String token=jedis.get("token");
        if(token!=null&&ids!=null){
            try {
                itripUserLinkUserService.deleteUserLinkUserByIds(ids);
                return DtoUtil.returnSuccess("删除常用联系人成功");
            } catch (Exception e) {
                return DtoUtil.returnFail("删除常用联系人失败","100432");
            }
        }else if(token!=null&&ids==null){
            return DtoUtil.returnFail("请选择要删除的常用联系人","100433");
        }
        return null;
    }
}
