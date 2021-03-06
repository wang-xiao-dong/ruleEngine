package com.raja.easyrule.rule;

import com.google.common.base.Preconditions;
import com.raja.easyrule.enums.ElectricityPriceRange;
import com.raja.easyrule.po.OrderFormInfo;
import com.raja.easyrule.utils.DateUtils;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

import java.util.Date;

/**
 * Created by WXD on 2018/5/18.
 * 计价时间区间规则（7-24点）
 */
@Rule(name = "peak time range rule", description = "针对充电时间维度的规则", priority = 2)
public class PeakTimeRangeRule {

    @Condition
    public boolean when(@Fact("orderInfo") OrderFormInfo orderInfo) {
        Preconditions.checkNotNull(orderInfo.getStartTime(), "订单开始时间为null");
        Preconditions.checkNotNull(orderInfo.getEndTime(), "订单结束时间为null");
        //某一计价区间刚好在指定分级电量区间中
        long startRange = DateUtils.getDawnPlusTime(orderInfo.getEndTime(),ElectricityPriceRange.PEAK_ONE.getStart(),0).getTime();
        long endRange = DateUtils.getDawnPlusTime(orderInfo.getEndTime(),ElectricityPriceRange.PEAK_ONE.getEnd(),0).getTime();
        return orderInfo.getStartTime().getTime()>=startRange && orderInfo.getEndTime().getTime()<=endRange;
    }

    @Action(order = 1)
    public void then(@Fact("orderInfo") OrderFormInfo orderInfo) throws Exception {
        orderInfo.setPrice(ElectricityPriceRange.PEAK_ONE.getPrice().multiply(orderInfo.getElectricity()));
    }

    @Action(order = 2)
    public void finallyMethod() throws Exception {
        //my final actions
        System.err.println("满足了峰值充电时间的计费规则！！");
    }

}

