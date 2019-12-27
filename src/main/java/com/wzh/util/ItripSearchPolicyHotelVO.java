package com.wzh.util;



/**
 * 返回前端-酒店政策VO（酒店详情页）
 * Created by donghai on 2017/5/11.
 */
/*@ApiModel(value = "ItripSearchPolicyHotelVO",description = "查询酒店的政策")*/
public class ItripSearchPolicyHotelVO {
    /*[必填] 酒店政策")*/
    private String hotelPolicy;

    public String getHotelPolicy() {
        return hotelPolicy;
    }

    public void setHotelPolicy(String hotelPolicy) {
        this.hotelPolicy = hotelPolicy;
    }
}
