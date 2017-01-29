package com.appsom.tkach.spring.core.loggers;

import com.appsom.tkach.spring.core.events.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Component
public class CombinedEventLogger extends AbstractEventLogger {

    @Resource(name = "combinedLoggers")
    private Collection<EventLogger> loggers;

    /*
    public CombinedEventLogger(Collection<EventLogger> loggers) {
        this.loggers = new ArrayList<>(loggers);
    }
    */

    @Override
    public void logEvent(Event event) {
        loggers.forEach(l -> l.logEvent(event));
    }

    public Collection<EventLogger> getLoggers() {
        return Collections.unmodifiableCollection(loggers);
    }

    @Value("#{'Combined ' + combinedLoggers.![name].toString()}")
    @Override
    protected void setName(String name) {
        this.name = name;
    }
}
