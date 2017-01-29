package com.appsom.tkach.spring.core.events;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Scope("prototype")
public class Event {
    private static final AtomicInteger AUTO_ID = new AtomicInteger(0);

    private int id;
    private String message;
    @Value("#{new java.util.Date()}")
    private Date date;
    @Value("#{T(java.text.DateFormat).getDateTimeInstance()}")
    private DateFormat dateFormat;

    public static void initAutoId(int id) {
        AUTO_ID.set(id);
    }

    public static boolean isDailyTime(int start, int end) {
        LocalTime time = LocalTime.now();
        return time.getHour() > start && time.getHour() < end;
    }

    public Event() {
        this.id = AUTO_ID.getAndIncrement();
    }

    public Event(Date date, DateFormat dateFormat) {
        this();
        this.date = date;
        this.dateFormat = dateFormat;
    }

    public Event(Integer id, Date date, String message) {
        this.id = id;
        this.date = date;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Event{id=" + id + ", message='" + message + "'" +
                ", date=" + (dateFormat != null ? dateFormat.format(date) : date) + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Event other = (Event) obj;

        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
