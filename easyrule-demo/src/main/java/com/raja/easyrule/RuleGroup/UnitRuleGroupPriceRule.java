package com.raja.easyrule.RuleGroup;

import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.support.UnitRuleGroup;

/**
 * Created by Administrator on 2018/5/18.
 * 规则的组合（叠加）
 * 注入的规则，全部满足时，才依次执行各规则（按优先级，1最大）
 */
public class UnitRuleGroupPriceRule extends UnitRuleGroup{
    public UnitRuleGroupPriceRule(Object... rules) {
        for (Object rule : rules) {
            if (rule instanceof Rules) {
                Rules tempRules = (Rules)rule;
                for (Rule tempRule: tempRules) {
                    addRule(tempRule);
                }
            } else {
                addRule(rule);
            }
        }
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
