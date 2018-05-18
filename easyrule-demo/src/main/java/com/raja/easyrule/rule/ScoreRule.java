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

import java.util.Date;

/**
 * Created by WXD on 2018/5/18.
 * 得分规则
 */
@Rule(name = "my rule", description = "my rule description", priority = 1)
public class ScoreRule {

    @Condition
    public boolean when(@Fact("orderInfo") OrderFormInfo orderInfo,@Fact("priceRange") ElectricityPriceRange priceRange) {
        //my rule conditions
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
        System.err.println("始终执行这个方法。。。。");
    }

    /*@Bean(name = "myFoo")
    public void foo() {
        return new Foo();
    }*/


    //传入的字符串格式为小时：分：09:30,拼上传入date的年月日，作为新日期返回
    private Date string2Date(String dateStr,Date date){
        String[] arr = dateStr.split(":");
        Integer hour = Integer.parseInt(arr[0]);//会自动去掉第一个0，比如09
        Integer minute = Integer.parseInt(arr[1]);
        return DateUtils.getDawnPlusTime(date,hour,minute);
    }

}

