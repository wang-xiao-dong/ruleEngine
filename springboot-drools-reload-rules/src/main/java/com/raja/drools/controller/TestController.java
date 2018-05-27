package com.raja.drools.controller;

import com.raja.drools.enums.ElectricityPriceRange;
import com.raja.drools.utils.DateUtils;
import com.raja.drools.component.ReloadDroolsRules;
import com.raja.drools.model.OrderFormInfo;
import com.raja.drools.model.fact.AddressCheckResult;
import com.raja.drools.utils.KieUtils;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WXD on 2018/5/18.
 */
@RequestMapping("/test")
@Controller
public class TestController {

    @Resource
    private ReloadDroolsRules rules;

    @ResponseBody
    @RequestMapping("/price")
    public OrderFormInfo test(){
        KieSession kieSession = KieUtils.getKieContainer().newKieSession();
        OrderFormInfo orderFormInfo = new OrderFormInfo();
        orderFormInfo.setCompanyId(1);
        orderFormInfo.setGroupId(1);
        orderFormInfo.setElectricity(BigDecimal.valueOf(5));
        orderFormInfo.setStartTime(DateUtils.getDawnPlusTime(new Date(),8,0));
        orderFormInfo.setEndTime(DateUtils.getDawnPlusTime(new Date(),13,0));
        orderFormInfo.setIsRajaUser(true);
        orderFormInfo.setOrderId("order1");
        orderFormInfo.setUserGrade(1);
        orderFormInfo.setPrice(BigDecimal.valueOf(0));
        kieSession.insert(orderFormInfo);

        Map<String,Object> queryMap = new HashMap<>();
        long startPeakRange = DateUtils.getDawnPlusTime(orderFormInfo.getEndTime(), ElectricityPriceRange.PEAK_ONE.getStart(),0).getTime();
        long endPeakRange = DateUtils.getDawnPlusTime(orderFormInfo.getEndTime(),ElectricityPriceRange.PEAK_ONE.getEnd(),0).getTime();
        long startValleyRange = DateUtils.getDawnPlusTime(orderFormInfo.getEndTime(), ElectricityPriceRange.VALLEY_SECOND.getStart(),0).getTime();
        long endValleyRange = DateUtils.getDawnPlusTime(orderFormInfo.getEndTime(),ElectricityPriceRange.VALLEY_SECOND.getEnd(),0).getTime();
        queryMap.put("startPeakRange",startPeakRange);
        queryMap.put("endPeakRange",endPeakRange);
        queryMap.put("peakRangePrice",ElectricityPriceRange.PEAK_ONE.getPrice());
        queryMap.put("startValleyRange",startValleyRange);
        queryMap.put("endValleyRange",endValleyRange);
        queryMap.put("valleyRangePrice",ElectricityPriceRange.VALLEY_SECOND.getPrice());
        kieSession.insert(queryMap);
        int ruleFiredCount = kieSession.fireAllRules();
        System.out.println("触发了" + ruleFiredCount + "条规则");
        kieSession.dispose();
        return orderFormInfo;
    }

    @ResponseBody
    @RequestMapping("/reload")
    public String reload() throws IOException {
        rules.reload();
        return "新规则重新加载成功！！";
    }
}
