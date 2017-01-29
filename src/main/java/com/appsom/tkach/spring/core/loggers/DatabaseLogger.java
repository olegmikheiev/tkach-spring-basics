package com.appsom.tkach.spring.core.loggers;

import com.appsom.tkach.spring.core.events.Event;
import com.appsom.tkach.spring.core.loggers.AbstractEventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DatabaseLogger extends AbstractEventLogger {

    private static final String SQL_ERROR_STATE_SCHEMA_EXISTS = "X0Y68";
    private static final String SQL_ERROR_STATE_TABLE_EXISTS = "X0Y32";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${jdbc.username}")
    private String schema;

    @PostConstruct
    public void init() {
        createDatabaseSchema();
        createTableIfNotExists();
        updateEventAutoId();
    }

    @PreDestroy
    public void destroy() {
        int eventsCount = getEventsCount();
        System.out.println("Total events count in the database is " + eventsCount);

        List<Event> eventsList = getAllEvents();
        String eventsIds = eventsList.stream()
                .map(Event::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }

    @Override
    public void logEvent(Event event) {
        jdbcTemplate.update(
                "INSERT INTO t_event (id, date, message) VALUES (?, ?, ?)",
                event.getId(), event.getDate(), event.toString());
        System.out.println(String.format("Event with id <%d> saved into DB.", event.getId()));
    }

    public int getEventsCount() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM t_event", Integer.class);
        return count == null ? 0 : count;
    }

    public List<Event> getAllEvents() {
        List<Event> list = jdbcTemplate.query("SELECT * FROM t_event", new RowMapper<Event>() {
            @Override
            public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
                Integer id = rs.getInt("id");
                Date date = rs.getDate("date");
                String msg = rs.getString("message");
                return new Event(id, new Date(date.getTime()), msg);
            }
        });
        return list;
    }

    @Value("Database logger")
    @Override
    protected void setName(String name) {
        this.name = name;
    }

    private void createDatabaseSchema() {
        try {
            jdbcTemplate.update("CREATE SCHEMA " + schema);
        } catch (DataAccessException e) {
            Throwable causeException = e.getCause();
            if (causeException instanceof SQLException) {
                SQLException sqlException = (SQLException) causeException;
                if (SQL_ERROR_STATE_SCHEMA_EXISTS.equals(sqlException.getSQLState())) {
                    System.out.println(String.format("Schema <%s> already exists.", schema));
                } else {
                    throw e;
                }
            } else {
                throw e;
            }
        }
    }

    private void createTableIfNotExists() {
        try {
            jdbcTemplate.update("CREATE TABLE t_event (" +
                    "id INT NOT NULL PRIMARY KEY," +
                    "date TIMESTAMP," +
                    "message VARCHAR(255) )");
            System.out.println("Table <t_event> has been created.");
        } catch (DataAccessException e) {
            Throwable causeException = e.getCause();
            if (causeException instanceof SQLException) {
                SQLException sqlException = (SQLException) causeException;
                if (SQL_ERROR_STATE_TABLE_EXISTS.equals(sqlException.getSQLState())) {
                    System.out.println("Table <t_event> already exists.");
                } else {
                    throw e;
                }
            } else {
                throw e;
            }
        }
    }

    private void updateEventAutoId() {
        int maxId = getMaxId();
        Event.initAutoId(maxId + 1);
        System.out.println("Initialized Event.AUTO_ID with " + maxId);
    }

    private int getMaxId() {
        Integer maxId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM t_event", Integer.class);
        return maxId == null ? 0 : maxId;
    }
}
