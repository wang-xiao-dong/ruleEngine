package com.raja.easyrule.rule;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Rule;

/**
 * Created by WXD on 2018/5/18.
 * 空规则，用于满足组合规则ConditionalRuleGroup的计算逻辑。
 * 可以灵活设置规则
 */
@Rule(name = "empty rule", description = "空规则，始终返回true", priority = 15)
public class EmptyTrueRule {
    @Condition
    public boolean when() {
        return true;
    }

    @Action(order = 1)
    public void then() throws Exception {}

    @Action(order = 2)
    public void finallyMethod() throws Exception {
        System.err.println("进入了空规则！！");
    }
}

