package com.example.traffic.services;

import com.example.traffic.entities.FlightTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Scheduler {
    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);
    private List<FlightTask> flightTasks = new LinkedList<>();

    public void addFlightTask (FlightTask newTask) {
        this.flightTasks.add(newTask);
    }

    @Scheduled(fixedRateString = "${properties.timeInterval}")
    private void executeFlightsTasks() {
        logger.debug("Amount of task: " + flightTasks.size());
        if (flightTasks.size() > 0) {
            logger.info("Executed flights tasks time " + new Date());
            flightTasks.forEach(FlightTask::taskToExecute);
            flightTasks = flightTasks.stream().filter(flightTask -> !flightTask.isExecuted()).collect(Collectors.toList());
        }
    }
}
