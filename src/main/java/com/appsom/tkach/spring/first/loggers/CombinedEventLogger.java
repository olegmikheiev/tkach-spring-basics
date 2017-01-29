package com.appsom.tkach.spring.first.loggers;

import com.appsom.tkach.spring.first.events.Event;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component("combinedEventLogger")
public class CombinedEventLogger implements EventLogger {
    private Collection<EventLogger> loggers;

    public CombinedEventLogger(Collection<EventLogger> loggers) {
        this.loggers = new ArrayList<>(loggers);
    }

    public void logEvent(Event event) {
        loggers.forEach(l -> l.logEvent(event));
    }
}
