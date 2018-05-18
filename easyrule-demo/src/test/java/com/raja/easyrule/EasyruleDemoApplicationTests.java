package com.raja.easyrule;

import org.jeasy.rules.api.Facts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EasyruleDemoApplicationTests {

	@Test
	public void contextLoads() {
		Facts facts = new Facts();
		Boolean past = true;
		facts.put("fact", past);
		//rulesEngine.fire(rules, facts);
		//filterChain.doFilter(request, response);
	}

}
