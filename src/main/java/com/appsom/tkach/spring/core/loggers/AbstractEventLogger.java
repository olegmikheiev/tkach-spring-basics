package com.appsom.tkach.spring.core.loggers;

public abstract class AbstractEventLogger implements EventLogger {
    protected String name;

    @Override
    public String getName() {
        return name;
    }

    protected abstract void setName(String name);
}
