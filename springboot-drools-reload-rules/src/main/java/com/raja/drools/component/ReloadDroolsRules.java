package com.raja.drools.component;

import com.raja.drools.utils.KieUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Created by WXD on 2018/5/18.
 */

@Component
public class ReloadDroolsRules {
    Log log = LogFactory.getLog(ReloadDroolsRules.class);

    @Autowired
    private ResourceLoader resourceLoader;

    //重新加载规则文件
    public void reload() throws IOException {
        KieServices kieServices = getKieServices();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        //将新的规则文件写进一个临时文件中，规则每次从临时文件加载
        kfs.write("src/main/resources/rules/temp.drl", loadRules());
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            throw new IllegalStateException("规则检验失败："+results.getMessages());
        }
        KieUtils.setKieContainer(kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId()));
        log.info("新规则重新加载成功");
    }

    private String loadRules() throws IOException {
        // 从数据库加载的规则
//        return "package plausibcheck.adress\n\n rule \"Postcode 6 numbers\"\n\n    when\n  then\n        System.out.println(\"规则2中打印日志：校验通过!\");\n end";
        //从文本获取内容
        return IOUtils.toString(resourceLoader.getResource("classpath:resources/price.txt").getInputStream());
//       return  ResourceUtils.getFile("classpath:rules/price.drl").toString();
    }

    private KieServices getKieServices() {
        return KieServices.Factory.get();
    }

    //使用KieHelper来实现动态加载
    public void reloadByHelper() throws IOException {

        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(loadRules(),ResourceType.DRL);

        Results results = kieHelper.verify();
        if (results.hasMessages(Message.Level.ERROR)) {
            throw new IllegalStateException("规则校验出错："+results.getMessages());
        }

//        KieBase kieBase = kieHelper.build();
        KieContainer kieContainer = kieHelper.getKieContainer();
        KieUtils.setKieContainer(kieContainer);
        System.out.println("新规则重载成功");
    }

}
