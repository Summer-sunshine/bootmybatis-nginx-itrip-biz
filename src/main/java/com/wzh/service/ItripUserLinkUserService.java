package com.wzh.service;

import com.wzh.po.ItripUserLinkUser;

import java.util.List;
import java.util.Map;

public interface ItripUserLinkUserService {
    //增加常用联系人
    public int addUsersLinkUser(ItripUserLinkUser linkUser)throws Exception;
    //查询常用联系人
    public List<ItripUserLinkUser> findAllUserLinkUserByMap(Map<String, Object> param)throws Exception;
    //删除常用联系人
    public int deleteUserLinkUserByIds(Long[] ids)throws Exception;
    //修改常用联系人
    public int updateUsersLinkUser(ItripUserLinkUser linkUser)throws Exception;
    //修改用户信息
    public int updateItripUserLinkUserById(ItripUserLinkUser itripUserLinkUser);
}
