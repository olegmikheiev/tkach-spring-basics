package com.appsom.tkach.spring.first.loggers;

import com.appsom.tkach.spring.first.events.Event;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Component("fileEventLogger")
public class FileEventLogger implements EventLogger {
    @Value("logs/eventLogs.log")
    private String fileName;
    private File file;

    public FileEventLogger(String fileName) {
        this.fileName = fileName;
        file = new File(fileName);
    }

    @PostConstruct
    private void init() throws IOException {
        if (!file.exists()) ;
            file.createNewFile();
        if (!file.canWrite())
            throw new IOException(String.format("Logs cannot be written into file <%s>.", fileName));
    }

    @Override
    public void logEvent(Event event) {
        writeToFile(event.toString());
    }

    private void writeToFile(String message) {
        try {
            FileUtils.writeStringToFile(file, message + "\n", "UTF-8", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
