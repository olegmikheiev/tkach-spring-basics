package com.appsom.tkach.spring.core;

import com.appsom.tkach.spring.core.events.Event;
import com.appsom.tkach.spring.core.events.EventType;
import com.appsom.tkach.spring.core.loggers.EventLogger;
import com.appsom.tkach.spring.core.client.Client;
import com.appsom.tkach.spring.core.spring.ApplicationConfig;
import com.appsom.tkach.spring.core.spring.DatabaseConfig;
import com.appsom.tkach.spring.core.spring.LoggersConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service("App")
public class ApplicationImpl {

    @Autowired
    private Client client;

    @Value("#{ T(com.appsom.tkach.spring.core.events.Event).isDaty(8, 17) ? " +
            " consoleEventLogger : cacheFileEventLogger }")
    private EventLogger defaultLogger;

    @Resource(name = "loggerMap")
    private Map<EventType, EventLogger> loggers;

    @Value("#{ 'Hello user ' + " +
            "(systemProperties['os.name'].contains('Windows') ? " +
            " systemEnvironment['USERNAME'] : systemEnvironment['USER'] ) + " +
            " '. Default logger is ' + app.defaultLogger.name }")
    private String startupMessage;

    public ApplicationImpl() {}

    public ApplicationImpl(Client client, EventLogger defaultLogger, Map<EventType, EventLogger> loggers) {
        this.client = client;
        this.defaultLogger = defaultLogger;
        this.loggers = loggers;
    }

    public void logEvent(EventType eventType, Event event, String msg) {
        String message = msg.replaceAll(client.getId(), client.getFullName());
        event.setMessage(message);

        EventLogger logger = loggers.get(eventType);
        if (logger == null) {
            logger = defaultLogger;
        }
        logger.logEvent(event);
    }

    public EventLogger getDefaultLogger() {
        return defaultLogger;
    }

    public String getStartupMessage() {
        return startupMessage;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(ApplicationConfig.class);
        ctx.register(LoggersConfig.class);
        ctx.register(DatabaseConfig.class);
        ctx.scan("com.appsom.tkach.spring.core");
        ctx.refresh();

        ApplicationImpl app = (ApplicationImpl) ctx.getBean("App");

        System.out.println(app.getStartupMessage());

        Client client = ctx.getBean(Client.class);
        System.out.println("Client says: " + client.getGreeting());

        app.logEvents(ctx);

        ctx.close();
    }

    private void logEvents(AnnotationConfigApplicationContext ctx) {
        Event event = ctx.getBean(Event.class);
        logEvent(EventType.INFO, event, "Some event for 1");

        event = ctx.getBean(Event.class);
        logEvent(EventType.INFO, event, "One more event for 1");

        event = ctx.getBean(Event.class);
        logEvent(EventType.INFO, event, "And one more event for 1");

        event = ctx.getBean(Event.class);
        logEvent(EventType.ERROR, event, "Some event for 2");

        event = ctx.getBean(Event.class);
        logEvent(null, event, "Some event for 3");
    }
}
