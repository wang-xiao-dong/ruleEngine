package com.raja.easyrule.po;





import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by WXD on 2018/5/18.
 * 定义实体类，包含规则的判断元素和返回值
 */
@Data
public class OrderFormInfo {
    //订单开始时间
    Date startTime;
    //订单结束时间
    Date endTime;
    //订单充电量
    BigDecimal electricity;
    //订单价格
    BigDecimal price;

}
