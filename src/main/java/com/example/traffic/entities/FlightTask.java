package com.example.traffic.entities;

import com.example.traffic.repositories.AirplaneRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Data
public class FlightTask {
    private static final Logger logger = LoggerFactory.getLogger(FlightTask.class);

    private Long id;
    private Airplane airplane;
    private List<TemporaryPoint> points;
    private AirplaneRepository repository;
    private boolean isStartExecution = true;
    private boolean isExecuted = false;

    public FlightTask(long id, Airplane airplane, List<TemporaryPoint> points, AirplaneRepository repository) {
        this.id = id;
        this.airplane = airplane;
        this.points = points;
        this.repository = repository;
    }

    public void taskToExecute () {
        if (isStartExecution) {
            isStartExecution = false;
            logger.info("Airplane " + airplane.getId() + ": amount of flights: " + airplane.getAmountOfFlights() + ',' + " flights duration: " + airplane.getFlightsDuration());
            logger.info("Airplane " + airplane.getId() + " start flight number " + airplane.getFlights().get(airplane.getFlights().size()-1).getNumber());
        }
        airplane.setPosition(points.get(0));
        airplane.getFlights().get(airplane.getFlights().size()-1).addPassedPoint(airplane.getPosition());
        airplane.updateFlightsDuration(1);
        logger.debug(airplane.toString());
        logger.debug("Amount of passed points: " + airplane.getFlights().get(airplane.getFlights().size()-1).getPassedPoints().size());
        points.remove(0);
        if (points.size() == 0) {
            isExecuted = true;
            logger.info("Airplane " + airplane.getId() + " finish flight number " + airplane.getFlights().get(airplane.getFlights().size()-1).getNumber());
            airplane.updateAmountOfFlights();
        }
        repository.save(airplane);
    }
}
