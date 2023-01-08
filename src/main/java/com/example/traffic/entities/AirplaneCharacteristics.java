package com.example.traffic.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AirplaneCharacteristics {
    private double maxSpeed;             // m/s
    private double speedChangeRate;      // m/s^2
    private double altitudeChangeRate;   // m/s
    private double directionChangeRate;  // deg/s
}
