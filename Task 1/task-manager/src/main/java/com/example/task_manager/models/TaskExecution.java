package com.example.task_manager.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Entity
public class TaskExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient // Do not store in DB
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Asia/Kolkata"));

    private Instant startTime;
    private Instant endTime;
    private String output;

    public String getStartTime() {
        return FORMATTER.format(startTime.atZone(ZoneId.of("Asia/Kolkata")));
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return FORMATTER.format(endTime.atZone(ZoneId.of("Asia/Kolkata")));
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
