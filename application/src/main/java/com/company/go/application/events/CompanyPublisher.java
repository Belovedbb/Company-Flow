package com.company.go.application.events;

import com.company.go.application.events.event.CreateAccountEvent;
import com.company.go.application.events.event.CreateStaffPerformanceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CompanyPublisher {

    private final ApplicationEventPublisher publisher;

    @Autowired
    CompanyPublisher(ApplicationEventPublisher publisher){
        this.publisher = publisher;
    }

    @Scheduled(cron = "0 0 0 28 * *")
    public void publishCreateStaffPerformance(){
        publisher.publishEvent(new CreateStaffPerformanceEvent(this));
    }

    @Scheduled(cron = "0 0 0 28 * *")
    public void publishCreateAccount(){
        publisher.publishEvent(new CreateAccountEvent(this));
    }
}