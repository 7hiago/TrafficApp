package com.example.traffic.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WayPoint implements Point{
    private double latitude;         // deg
    private double longitude;        // deg
    private double flightAltitude;   // m
    private double flightSpeed;      // m/s
}
