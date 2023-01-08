package com.example.traffic.entities;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Flight {
    @NonNull
    private Long number;

    @NonNull
    private List<WayPoint> wayPoints;
    private List<TemporaryPoint> passedPoints = new LinkedList<>();

    public void addPassedPoint (TemporaryPoint newPassedPoint) {
        this.passedPoints.add(newPassedPoint);
    }
}
