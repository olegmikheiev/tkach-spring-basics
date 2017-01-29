package com.appsom.tkach.spring.first.loggers;

import com.appsom.tkach.spring.first.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Component("cacheFileEventLogger")
public class CacheFileEventLogger extends FileEventLogger {
    @Value("2")
    private int cacheSize;
    @Autowired
    private List<Event> cache;

    public CacheFileEventLogger(String fileName, int cacheSize) {
        super(fileName);
        this.cacheSize = cacheSize < 1 ? 1 : cacheSize;
        cache = new ArrayList<>(cacheSize);
    }

    @Override
    public void logEvent(Event event) {
        cache.add(event);
        if (cache.size() >= cacheSize) {
            writeEventsFromCache();
            cache.clear();
        }
    }

    @PreDestroy
    public void onDestroy() {
        if (!cache.isEmpty())
            writeEventsFromCache();
    }

    private void writeEventsFromCache() {
        cache.forEach(super::logEvent);
    }
}
