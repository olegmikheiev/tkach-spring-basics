package com.appsom.tkach.spring.core.loggers;

import com.appsom.tkach.spring.core.events.Event;

public interface EventLogger {
    void logEvent(Event event);
    String getName();
}
