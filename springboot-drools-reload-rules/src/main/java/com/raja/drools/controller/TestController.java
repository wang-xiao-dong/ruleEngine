package com.raja.drools.controller;

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
    @RequestMapping("/address")
    public void test(){
        KieSession kieSession = KieUtils.getKieContainer().newKieSession();

        OrderFormInfo orderFormInfo = new OrderFormInfo();
        orderFormInfo.setCompanyId(1);
        orderFormInfo.setGroupId(1);
        orderFormInfo.setElectricity(BigDecimal.valueOf(5));
        orderFormInfo.setStartTime(DateUtils.getDawnPlusTime(new Date(),8,0));
        orderFormInfo.setEndTime(DateUtils.getDawnPlusTime(new Date(),12,0));
        orderFormInfo.setIsRajaUser(true);
        orderFormInfo.setOrderId("order1");
        orderFormInfo.setUserGrade(1);
        orderFormInfo.setPrice(BigDecimal.valueOf(0));

        AddressCheckResult result = new AddressCheckResult();
        kieSession.insert(orderFormInfo);

        Map<String,BigDecimal> queryMap = new HashMap();
        kieSession.insert(result);
        int ruleFiredCount = kieSession.fireAllRules();
        System.out.println("触发了" + ruleFiredCount + "条规则");
        if(result.isPostCodeResult()){
            System.out.println("规则校验通过");
        }
        kieSession.dispose();
    }

    @ResponseBody
    @RequestMapping("/reload")
    public String reload() throws IOException {
        rules.reload();
        return "ok";
    }
}
