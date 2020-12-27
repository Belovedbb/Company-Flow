package com.company.go.application.events.listener;

import com.company.go.application.events.CompanyPublisher;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class CompanyListener implements ApplicationListener<ApplicationEvent>, ApplicationContextAware {

    ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        initPublisher(applicationEvent);
    }

    private void initPublisher(ApplicationEvent event){
        if(event instanceof ContextRefreshedEvent){
            context.getBean(CompanyPublisher.class).publishCreateStaffPerformance();
            context.getBean(CompanyPublisher.class).publishCreateAccount();
        }
    }
}
