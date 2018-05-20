package com.raja.easyrule.po;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/5/18.
 * 定义实体类，规则的返回值
 */
@Data
public class PriceResultInfo {
    //订单ID
    String orderId;
    //订单价格
    BigDecimal price;
}
