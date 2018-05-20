package com.raja.easyrule.rule;

import com.google.common.base.Preconditions;
import com.raja.easyrule.enums.ElectricityPriceRange;
import com.raja.easyrule.po.OrderFormInfo;
import com.raja.easyrule.utils.DateUtils;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by WXD on 2018/5/18.
 * 充电站规则
 */
@Rule(name = "group rule", description = "针对充电站维度的规则", priority = 9)
public class GroupRule {

    @Condition
    public boolean when(@Fact("orderInfo") OrderFormInfo orderInfo) {
        Preconditions.checkNotNull(orderInfo.getCompanyId(), "订单必须包含所属公司Id");
        Preconditions.checkNotNull(orderInfo.getIsRajaUser(), "订单必须包含是否雅俊用户属性");
        Preconditions.checkNotNull(orderInfo.getPrice(), "传入的订单信息必须包含价格");
        //京东充电站，雅骏的车辆充电优惠A
        return orderInfo.getCompanyId().equals(1) && orderInfo.getIsRajaUser();
    }

    @Action(order = 1)
    public void then(@Fact("orderInfo") OrderFormInfo orderInfo) throws Exception {
        orderInfo.setPrice(orderInfo.getPrice().multiply(BigDecimal.valueOf(0.9)));
        System.err.println("===="+orderInfo.getPrice());
    }

    @Action(order = 2)
    public void finallyMethod() throws Exception {
        //my final actions
        System.err.println("满足了充电站的优惠规则！！");
    }
}

