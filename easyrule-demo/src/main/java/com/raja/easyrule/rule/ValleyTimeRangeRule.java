package com.raja.easyrule.rule;

import com.google.common.base.Preconditions;
import com.raja.easyrule.enums.ElectricityPriceRange;
import com.raja.easyrule.po.OrderFormInfo;
import com.raja.easyrule.utils.DateUtils;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

/**
 * Created by WXD on 2018/5/18.
 * 计价时间区间规则（0-7点）
 */
@Rule(name = "valley time range rule", description = "针对充电时间维度的规则", priority = 1)
public class ValleyTimeRangeRule {

    @Condition
    public boolean when(@Fact("orderInfo") OrderFormInfo orderInfo) {
        Preconditions.checkNotNull(orderInfo.getStartTime(), "订单开始时间为null");
        Preconditions.checkNotNull(orderInfo.getEndTime(), "订单结束时间为null");
        //某一计价区间刚好在指定分级电量区间中
        long startRange = DateUtils.getDawnPlusTime(orderInfo.getEndTime(),ElectricityPriceRange.VALLEY_SECOND.getStart(),0).getTime();
        long endRange = DateUtils.getDawnPlusTime(orderInfo.getEndTime(),ElectricityPriceRange.VALLEY_SECOND.getEnd(),0).getTime();
        return orderInfo.getStartTime().getTime()>=startRange && orderInfo.getEndTime().getTime()<=endRange;
    }

    @Action(order = 1)
    public void then(@Fact("orderInfo") OrderFormInfo orderInfo) throws Exception {
        orderInfo.setPrice(ElectricityPriceRange.VALLEY_SECOND.getPrice().multiply(orderInfo.getElectricity()));
    }

    @Action(order = 2)
    public void finallyMethod() throws Exception {
        //my final actions
        System.err.println("满足了谷值充电时间的计费规则！！");
    }

}

