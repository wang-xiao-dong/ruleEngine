package com.raja.drools;

import com.raja.drools.enums.ElectricityPriceRange;
import com.raja.drools.model.OrderFormInfo;
import com.raja.drools.model.fact.AddressCheckResult;
import com.raja.drools.utils.DateUtils;
import com.raja.drools.utils.KieUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PriceRuleTest {

	@Test
	public void priceRuleTest() {
		OrderFormInfo orderFormInfo = new OrderFormInfo();
		orderFormInfo.setCompanyId(1);
		orderFormInfo.setGroupId(1);
		orderFormInfo.setElectricity(BigDecimal.valueOf(5));
		orderFormInfo.setStartTime(DateUtils.getDawnPlusTime(new Date(),1,0));
		orderFormInfo.setEndTime(DateUtils.getDawnPlusTime(new Date(),6,0));
		orderFormInfo.setIsRajaUser(true);
		orderFormInfo.setOrderId("order1");
		orderFormInfo.setUserGrade(1);
		orderFormInfo.setPrice(BigDecimal.valueOf(0));
		KieSession kieSession = KieUtils.getKieContainer().newKieSession();
		AddressCheckResult result = new AddressCheckResult();
		kieSession.insert(orderFormInfo);
		kieSession.insert(result);

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
		if(result.isPostCodeResult()){
			System.out.println("规则校验通过");
		}
		kieSession.dispose();

		System.err.println("规则执行完毕，最终电价为："+orderFormInfo.getPrice());
	}
}
