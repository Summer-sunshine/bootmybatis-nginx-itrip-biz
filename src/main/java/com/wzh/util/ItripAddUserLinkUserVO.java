package com.wzh.util;


import java.io.Serializable;

/**
 * 前端输入-新增常用联系人VO
 * Created by donghai on 2017/5/10.
 */

public class ItripAddUserLinkUserVO implements Serializable {

   /* ("[必填] 常用刚联系人姓名")*/
    private String linkUserName;
   /* [必填] 证件类型")*/
    private Integer linkIdCardType;
  /*  [必填] 证件号码")*/
    private String linkIdCard;
   /* [非必填] 联系电话")*/
    private String linkPhone;
    /*[必填] 用户ID")*/
    private Long userId;

    public ItripAddUserLinkUserVO() {
    }

    public void setLinkUserName (String  linkUserName){
        this.linkUserName=linkUserName;
    }

    public  String getLinkUserName(){
        return this.linkUserName;
    }
    public void setLinkIdCard (String  linkIdCard){
        this.linkIdCard=linkIdCard;
    }

    public Integer getLinkIdCardType() {
        return linkIdCardType;
    }

    public void setLinkIdCardType(Integer linkIdCardType) {
        this.linkIdCardType = linkIdCardType;
    }

    public  String getLinkIdCard(){
        return this.linkIdCard;
    }
    public void setLinkPhone (String  linkPhone){
        this.linkPhone=linkPhone;
    }

    public  String getLinkPhone(){
        return this.linkPhone;
    }
    public void setUserId (Long  userId){
        this.userId=userId;
    }

    public  Long getUserId(){
        return this.userId;
    }

    public ItripAddUserLinkUserVO(String linkUserName, Integer linkIdCardType, String linkIdCard, String linkPhone, Long userId) {
        this.linkUserName = linkUserName;
        this.linkIdCardType = linkIdCardType;
        this.linkIdCard = linkIdCard;
        this.linkPhone = linkPhone;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ItripAddUserLinkUserVO{" +
                "linkUserName='" + linkUserName + '\'' +
                ", linkIdCardType=" + linkIdCardType +
                ", linkIdCard='" + linkIdCard + '\'' +
                ", linkPhone='" + linkPhone + '\'' +
                ", userId=" + userId +
                '}';
    }
}
