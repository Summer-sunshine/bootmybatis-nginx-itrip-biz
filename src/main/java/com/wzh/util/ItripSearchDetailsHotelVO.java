package com.wzh.util;



/**
 * 返回前端-酒店特色和介绍VO
 * Created by donghai on 2017/5/11.
 */
/*@ApiModel(value = "ItripSearchDetailsHotelVO",description = "查询酒店的特色和介绍")*/

public class ItripSearchDetailsHotelVO {
   /* [必填] 特色名称")*/
    private String name;
   /* [必填] 特色描述")*/
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
