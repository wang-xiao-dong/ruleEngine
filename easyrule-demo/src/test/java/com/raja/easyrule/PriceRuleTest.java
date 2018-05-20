package com.raja.easyrule;

import com.raja.easyrule.RuleGroup.ConditionalRuleGroupPriceRule;
import com.raja.easyrule.RuleGroup.UnitRuleGroupPriceRule;
import com.raja.easyrule.po.OrderFormInfo;
import com.raja.easyrule.rule.EmptyTrueRule;
import com.raja.easyrule.rule.GroupRule;
import com.raja.easyrule.rule.PeakTimeRangeRule;
import com.raja.easyrule.rule.ValleyTimeRangeRule;
import com.raja.easyrule.utils.DateUtils;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PriceRuleTest {

   //注：各个规则的条件判断是并行执行的，不是串行执行,
   // 先判断条件再串行执行方法，输出结果
   //所以，各个规则的条件判断要单独作参数校验，不能依赖上一个规则判断，执行方法可依赖上一个规则
	@Test
	public void priceRuleTest() {

		RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstAppliedRule(true);
		RulesEngine rulesEngine = new DefaultRulesEngine(parameters);
		Rules rules = new Rules();
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
		Facts facts = new Facts();
		facts.put("orderInfo", orderFormInfo);
		//简单规则开始*****
		//简单规则，或关系，根据优先级，只要有一个满足，且执行正确，后面的规则便不执行（即使满足条件）
		//若某个规则满足条件，但执行过程报错，比如空指针，便跳过。执行后面满足条件的规则
//		rules.register(new GroupRule());
//		rules.register(new PeakTimeRangeRule());
//		rules.register(new ValleyTimeRangeRule());
		//简单规则结束*****
//		rules.register(new ActivationRuleGroupPriceRule(new PeakTimeRangeRule(), new ValleyTimeRangeRule(),new GroupRule()));
//		rules.register(new UnitRuleGroupPriceRule(new PeakTimeRangeRule(),new GroupRule()));
		rules.register(new ConditionalRuleGroupPriceRule(new GroupRule(),new PeakTimeRangeRule(), new ValleyTimeRangeRule(),new EmptyTrueRule()));
		rulesEngine.fire(rules, facts);
		System.err.println("规则执行完毕，最终电价为："+orderFormInfo.getPrice());
	}
}
