package com.appsom.tkach.spring.first.loggers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class LoggersConfig {
    private static final String FILE_PATH = "logs/eventLogs.log";

    @Bean
    public EventLogger consoleEventLogger() {
        return new ConsoleEventLogger();
    }

    @Bean
    public EventLogger fileEventLogger() {
        return new FileEventLogger(FILE_PATH);
    }

    @Bean
    public EventLogger cacheFileEventLogger() {
        return new CacheFileEventLogger(FILE_PATH, 2);
    }

    @Bean
    public EventLogger combinedEventLogger() {
        return new CombinedEventLogger(Arrays.asList(
                consoleEventLogger(),
                fileEventLogger(),
                cacheFileEventLogger()
        ));
    }
}
