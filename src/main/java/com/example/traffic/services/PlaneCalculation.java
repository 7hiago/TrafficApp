package com.example.traffic.services;

import com.example.traffic.entities.AirplaneCharacteristics;
import com.example.traffic.entities.Point;
import com.example.traffic.entities.TemporaryPoint;
import com.example.traffic.entities.WayPoint;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Data
@Service
public class PlaneCalculation {
    private int R = 6372795;    // m

    @Value("${properties.timeInterval}")
    private double timeInterval;

    private double calculateDistanceBetweenPoint(Point pointFrom, Point pointTo) {
        double lat1 = Math.toRadians(pointFrom.getLatitude());
        double lat2 = Math.toRadians(pointTo.getLatitude());
        double dLat = Math.toRadians(pointTo.getLatitude() - pointFrom.getLatitude());
        double dLong = Math.toRadians(pointTo.getLongitude() - pointFrom.getLongitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return c * R;
    }

    private double calculateAzimuth (Point pointFrom, Point pointTo) {
        double y = Math.sin(pointTo.getLongitude()-pointFrom.getLongitude()) * Math.cos(pointTo.getLatitude());
        double x = Math.cos(pointFrom.getLatitude())*Math.sin(pointTo.getLatitude()) -
                Math.sin(pointFrom.getLatitude())*Math.cos(pointTo.getLatitude())*Math.cos(pointTo.getLongitude()-pointFrom.getLongitude());
        double a = Math.atan2(y, x);

        return (a*180/Math.PI + 360) % 360;
    }

    private TemporaryPoint calculateTemporaryPoint (Point pointFrom, double azimuth, double distance, double flightAltitude, double flightSpeed) {
        double startLatitudeRad = Math.toRadians(pointFrom.getLatitude());
        double startLongitudeRad = Math.toRadians(pointFrom.getLongitude());
        double angular = distance/R;
        double azimuthRad = Math.toRadians(azimuth);

        double latitude = Math.asin( Math.sin(startLatitudeRad)*Math.cos(angular) + Math.cos(startLatitudeRad)*Math.sin(angular)*Math.cos(azimuthRad) );
        double a = Math.atan2(Math.sin(azimuthRad)*Math.sin(angular)*Math.cos(startLatitudeRad),
                Math.cos(angular)-Math.sin(startLatitudeRad)*Math.sin(latitude));
        double longitude = (startLongitudeRad + a + 3 * Math.PI) % (2 * Math.PI) - Math.PI;

        return new TemporaryPoint(Math.toDegrees(latitude), Math.toDegrees(longitude), flightAltitude, flightSpeed, azimuth);
    }

    private List<TemporaryPoint> calculateSingleSegment(Point startPoint, Point endPoint, AirplaneCharacteristics characteristics) {
        List<TemporaryPoint> temporaryPoints = new LinkedList<>();
        double time = timeInterval/1000; // s
        double distance = calculateDistanceBetweenPoint(startPoint, endPoint);
        double azimuth = calculateAzimuth(startPoint, endPoint);
        double currentAltitude = startPoint.getFlightAltitude();
        double endAltitude = endPoint.getFlightAltitude();
        double altitudeChangeRate = characteristics.getAltitudeChangeRate();
        double currentSpeed = startPoint.getFlightSpeed();
        double endSpeed = endPoint.getFlightSpeed();
        double speedChangeRate = characteristics.getSpeedChangeRate();
        double path = 0;
        do {
            if (endSpeed >= currentSpeed + speedChangeRate * time) {
                path += currentSpeed * time + (speedChangeRate * time * time)/2;
                currentSpeed = currentSpeed * time + speedChangeRate * time;
            }
            else if (endSpeed <= currentSpeed - speedChangeRate * time) {
                path += currentSpeed * time - (speedChangeRate * time * time)/2;
                currentSpeed = currentSpeed * time - speedChangeRate * time;
            }
            else {
                path += currentSpeed * time;
            }
            if (endAltitude >= currentAltitude + altitudeChangeRate * time) {
                currentAltitude = currentAltitude + altitudeChangeRate * time;
            }
            else if (endAltitude <= currentAltitude - altitudeChangeRate * time) {
                currentAltitude = currentAltitude - altitudeChangeRate * time;
            }
            TemporaryPoint temporaryPoint = calculateTemporaryPoint(startPoint, azimuth, path, currentAltitude, currentSpeed);
            temporaryPoints.add(temporaryPoint);
        } while (distance > path);
        return temporaryPoints;
    }

    public List<TemporaryPoint> calculateRoute(AirplaneCharacteristics characteristics, List<WayPoint> wayPoints) {
        List<TemporaryPoint> temporaryPoints = new LinkedList<>();
        for (int i = 1; i < wayPoints.size(); i++) {
            temporaryPoints.addAll(calculateSingleSegment(wayPoints.get(i-1), wayPoints.get(i), characteristics));
        }
        return temporaryPoints;
    }
}
