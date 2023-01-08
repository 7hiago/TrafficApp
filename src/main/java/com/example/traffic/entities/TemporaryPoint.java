package com.example.traffic.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class TemporaryPoint implements Point{
    private double latitude;         // deg
    private double longitude;        // deg
    private double flightAltitude;   // m
    private double flightSpeed;      // m/s
    private double direction;        // degree
}
