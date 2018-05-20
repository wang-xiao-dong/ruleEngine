package com.raja.easyrule.RuleGroup;

import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.support.ConditionalRuleGroup;

/**
 * Created by WXD on 2018/5/18.
 * 规则的组合（叠加）
 * 以组合中优先级最高的规则作为触发条件（按优先级数字大小为准，1最小）:
 * 如果优先级最高的规则满足，才依次执行其他规则，执行顺序为按优先级从高到底。????
 */
public class ConditionalRuleGroupPriceRule extends ConditionalRuleGroup {

    public ConditionalRuleGroupPriceRule(Object... rules) {
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
