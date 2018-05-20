package com.raja.drools.utils;

import org.kie.api.runtime.KieContainer;

/**
* Created by WXD on 2018/5/18.
 */
public class KieUtils {

    private static KieContainer kieContainer;

    public static KieContainer getKieContainer() {
        return kieContainer;
    }

    public static void setKieContainer(KieContainer kieContainer) {
        KieUtils.kieContainer = kieContainer;
    }

}
