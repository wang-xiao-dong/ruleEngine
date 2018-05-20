package com.raja.easyrule.RuleGroup;

import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.support.ActivationRuleGroup;

/**
 * Created by WXD on 2018/5/18.
 * 规则的组合（选一）
 * 触发第一个适用的规则并忽略组中的其他规则。
 * 规则首先按其自然顺序(1最大，缺省优先级)在组内进行排序。
 * 作用同，
 * rules.register(new GroupRule());
 *rules.register(new PeakTimeRangeRule());
 *rules.register(new ValleyTimeRangeRule());
 */
public class ActivationRuleGroupPriceRule extends ActivationRuleGroup {

    public ActivationRuleGroupPriceRule(Object... rules) {
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
