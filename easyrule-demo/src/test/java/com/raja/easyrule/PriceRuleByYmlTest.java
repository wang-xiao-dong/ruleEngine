package com.raja.easyrule;

import com.raja.easyrule.RuleGroup.ConditionalRuleGroupPriceRule;
import com.raja.easyrule.enums.ElectricityPriceRange;
import com.raja.easyrule.po.OrderFormInfo;
import com.raja.easyrule.utils.DateUtils;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by WXD on 2018/5/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PriceRuleByYmlTest {
    @Test
    public void priceRuleByYmlTest( ) throws FileNotFoundException {
        OrderFormInfo orderFormInfo = new OrderFormInfo();
        orderFormInfo.setCompanyId(1);
        orderFormInfo.setGroupId(1);
        orderFormInfo.setElectricity(BigDecimal.valueOf(5));
        orderFormInfo.setStartTime(DateUtils.getDawnPlusTime(new Date(),1,0));
        orderFormInfo.setEndTime(DateUtils.getDawnPlusTime(new Date(),6,0));
        orderFormInfo.setIsRajaUser(true);
        orderFormInfo.setOrderId("order1");
        orderFormInfo.setUserGrade(1);
        orderFormInfo.setPrice(BigDecimal.ZERO);

        Facts facts = new Facts();
        facts.put("orderInfo", orderFormInfo);

        long startPeakRange = DateUtils.getDawnPlusTime(orderFormInfo.getEndTime(), ElectricityPriceRange.PEAK_ONE.getStart(),0).getTime();
        long endPeakRange = DateUtils.getDawnPlusTime(orderFormInfo.getEndTime(),ElectricityPriceRange.PEAK_ONE.getEnd(),0).getTime();
        long startValleyRange = DateUtils.getDawnPlusTime(orderFormInfo.getEndTime(), ElectricityPriceRange.VALLEY_SECOND.getStart(),0).getTime();
        long endValleyRange = DateUtils.getDawnPlusTime(orderFormInfo.getEndTime(),ElectricityPriceRange.VALLEY_SECOND.getEnd(),0).getTime();
        facts.put("startPeakRange", startPeakRange);
        facts.put("endPeakRange", endPeakRange);
        facts.put("startValleyRange", startValleyRange);
        facts.put("endValleyRange", endValleyRange);
        facts.put("peakPrice", ElectricityPriceRange.PEAK_ONE.getPrice());
        facts.put("valleyPrice", ElectricityPriceRange.PEAK_ONE.getPrice());
        facts.put("groupPrice", BigDecimal.valueOf(0.9));
        // create rules
        Rule groupRule = MVELRuleFactory.createRuleFrom(new FileReader(ResourceUtils.getFile("classpath:groupRule.yml")));
        Rule peakRule = MVELRuleFactory.createRuleFrom(new FileReader(ResourceUtils.getFile("classpath:peakTimeRule.yml")));
        Rule valleyRule = MVELRuleFactory.createRuleFrom(new FileReader(ResourceUtils.getFile("classpath:valleyTimeRule.yml")));
        Rule emptyRule = MVELRuleFactory.createRuleFrom(new FileReader(ResourceUtils.getFile("classpath:emptyRule.yml")));
        //一个文件，包含多个rule
//        Rules timeRules = MVELRuleFactory.createRulesFrom(new FileReader(ResourceUtils.getFile("classpath:valleyTimeRule.yml")));
        // create a rule set
        ConditionalRuleGroupPriceRule priceRule = new ConditionalRuleGroupPriceRule(groupRule,peakRule,valleyRule,emptyRule);
        Rules resultRule = new Rules();
        resultRule.register(priceRule);
        //create a default rules engine and fire rules on known facts
        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(resultRule, facts);
        System.err.println("规则执行完毕，最终电价为："+orderFormInfo.getPrice());
    }

}
