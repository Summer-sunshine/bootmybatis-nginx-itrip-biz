package com.wzh.util;



/**
 * 前端输入-修改常用联系人VO
 * Created by donghai on 2017/5/10.
 */
/*@ApiModel(value = "ItripModifyUserLinkUserVO",description = "修改常用联系人")*/
public class ItripModifyUserLinkUserVO {

   /* [必填] 主键")*/
    private Long id;
  /*  [必填] 常用刚联系人姓名")*/
    private String linkUserName;
    /*[必填] 证件类型")*/
    private Integer linkIdCardType;
  /*  [必填] 证件号码")*/
    private String linkIdCard;
   /* [非必填] 联系电话")*/
    private String linkPhone;
    /*[必填] 用户ID")*/
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
