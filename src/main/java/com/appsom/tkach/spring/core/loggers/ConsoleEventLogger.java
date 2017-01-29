package com.appsom.tkach.spring.core.loggers;

import com.appsom.tkach.spring.core.events.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConsoleEventLogger extends AbstractEventLogger {
    @Override
    public void logEvent(Event event) {
        System.out.println(event.toString());
    }

    @Value("Console logger")
    @Override
    protected void setName(String name) {
        this.name = name;
    }
}
