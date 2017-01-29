package com.appsom.tkach.spring.first.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;

@Component("event")
public class Event {
    private static int counter = 0;
    @Getter
    private int id;
    @Getter
    @Setter
    @Value("")
    private String message;
    @Getter
    @Autowired
    private Date date;
    @Autowired
    private DateFormat dateFormat;

    public Event(Date date, DateFormat dateFormat) {
        this.date = date;
        this.dateFormat = dateFormat;
        this.id = getIdForNewInstance();
        this.message = "";
    }

    private static int getIdForNewInstance() {
        return counter++;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", date=" + dateFormat.format(date) +
                '}';
    }
}
