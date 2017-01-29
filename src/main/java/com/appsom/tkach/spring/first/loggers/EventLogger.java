package com.appsom.tkach.spring.first.loggers;

import com.appsom.tkach.spring.first.events.Event;

public interface EventLogger {
    void logEvent(Event event);
}
