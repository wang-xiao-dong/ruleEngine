package com.raja.drools.config;

import com.raja.drools.utils.KieUtils;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
* Created by WXD on 2018/5/18.
*/
@Configuration
public class DroolsAutoConfiguration {
    
    private static final String RULES_PATH = "com/rules/";
    
    @Bean
    @ConditionalOnMissingBean(KieFileSystem.class)
    public KieFileSystem kieFileSystem() throws IOException {
        KieFileSystem kieFileSystem = getKieServices().newKieFileSystem();
        for (Resource file : getRuleFiles()) {
            kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_PATH + file.getFilename(), "UTF-8"));
        }
        return kieFileSystem;
    }

    private Resource[] getRuleFiles() throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        return resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "**/*.*");
    }
    
    @Bean
    @ConditionalOnMissingBean(KieContainer.class)
    public KieContainer kieContainer() throws IOException {
        KieServices kieServices =  getKieServices();
        final KieRepository kieRepository = kieServices.getRepository();
        
        kieRepository.addKieModule(new KieModule() {
            @Override
            public ReleaseId getReleaseId() {
                return kieRepository.getDefaultReleaseId();
            }
        });
        
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem());
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            throw new IllegalStateException("规则校验出错："+results.getMessages());
        }

        kieBuilder.buildAll();

        KieContainer kieContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
        KieUtils.setKieContainer(kieContainer);
        return kieContainer;
    }
    
    private KieServices getKieServices() {
        return KieServices.Factory.get();
    }
    
    @Bean
    @ConditionalOnMissingBean(KieBase.class)
    public KieBase kieBase() throws IOException {
        return kieContainer().getKieBase();
    }

    @Bean
    @ConditionalOnMissingBean(KModuleBeanFactoryPostProcessor.class)
    public KModuleBeanFactoryPostProcessor kiePostProcessor() {
        return new KModuleBeanFactoryPostProcessor();
    }
}
