package com.example.traffic.entities;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.util.LinkedList;
import java.util.List;

@Data
@RequiredArgsConstructor
@Document("airplane")
public class Airplane {

    @NonNull
    @Id
    private Long id;

    @NonNull
    private AirplaneCharacteristics airplaneCharacteristics;
    private TemporaryPoint position;
    private int amountOfFlights = 0;
    private long flightsDuration = 0;
    private List<Flight> flights = new LinkedList<>();

    public void addFlight (Flight newFlight) {
        this.flights.add(newFlight);
    }

    public void updateAmountOfFlights () {
        this.amountOfFlights++;
    }

    public void updateFlightsDuration (int numberOfSec) {
        this.flightsDuration += numberOfSec;
    }

    @Override
    public String toString() {
        return "Airplan " +id +
                ", position " + position +
                ", amountOfFlights " + amountOfFlights +
                ", flightsDuration " + flightsDuration + " sec";
    }
}
