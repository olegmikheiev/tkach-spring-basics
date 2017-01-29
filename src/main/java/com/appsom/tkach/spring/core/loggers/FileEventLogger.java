package com.appsom.tkach.spring.core.loggers;

import com.appsom.tkach.spring.core.events.Event;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Component
public class FileEventLogger extends AbstractEventLogger {
    private File file;

    @Value("${events.file:target/events.log}")
    private String fileName;

    public FileEventLogger() {
    }

    public FileEventLogger(String fileName) {
        this.fileName = fileName;
    }

    @PostConstruct
    private void init() throws IOException {
        file = new File(fileName);
        if (file.exists() && !file.canWrite()) {
            throw new IllegalArgumentException("Cannot write to file " + fileName);
        } else if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
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

    @Value("File logger")
    @Override
    protected void setName(String name) {
        this.name = name;
    }
}
