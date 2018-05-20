package com.raja.drools.model;


import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by WXD on 2018/5/18.
 * 定义实体类，包含规则的判断元素
 */
@Data
public class OrderFormInfo{
    //订单ID
    String orderId;
    //订单开始时间
    Date startTime;
    //订单结束时间
    Date endTime;
    //订单充电量
    BigDecimal electricity;
    //用户所属客户
    Integer companyId;
    //订单来源-充电站
    Integer groupId;
    //是否雅俊客户
    Boolean isRajaUser;
    //用户所属等级
    Integer userGrade;
    //订单价格
    BigDecimal price;
}
