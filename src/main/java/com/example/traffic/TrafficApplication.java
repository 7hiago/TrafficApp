package com.example.traffic;

import com.example.traffic.entities.*;
import com.example.traffic.repositories.AirplaneRepository;
import com.example.traffic.services.PlaneCalculation;
import com.example.traffic.services.Scheduler;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class TrafficApplication implements ApplicationRunner{
    private final PlaneCalculation planeCalculation;
    private final Scheduler scheduler;
    private final AirplaneRepository repository;

    public TrafficApplication(PlaneCalculation planeCalculation, Scheduler scheduler, AirplaneRepository repository) {
        this.planeCalculation = planeCalculation;
        this.scheduler = scheduler;
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(TrafficApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        AirplaneCharacteristics characteristics1 = new AirplaneCharacteristics(55,7,5,15);
        Airplane airplane1 = new Airplane(1L, characteristics1);
        repository.save(airplane1);

        List<WayPoint> wayPoints1 = new LinkedList<>();
        wayPoints1.add(new WayPoint(50.51224223153982, 30.49879854469698, 100, 50));
        wayPoints1.add(new WayPoint(50.51229855910583, 30.494013089272446, 80, 20));

        Flight newFlight1 = new Flight(1L, wayPoints1);
        airplane1.addFlight(newFlight1);

        List<TemporaryPoint> points1 = planeCalculation.calculateRoute(airplane1.getAirplaneCharacteristics(), wayPoints1);

        FlightTask flightTask1 = new FlightTask(1, airplane1, points1, repository);

        scheduler.addFlightTask(flightTask1);

        AirplaneCharacteristics characteristics2 = new AirplaneCharacteristics(100,10,10,10);
        Airplane airplane2 = new Airplane(2L, characteristics2);
        repository.save(airplane2);

        List<WayPoint> wayPoints2 = new LinkedList<>();
        wayPoints2.add(new WayPoint(50.51224223153982, 30.49879854469698, 100, 50));
        wayPoints2.add(new WayPoint(50.51229855910583, 30.494013089272446, 80, 20));
        wayPoints2.add(new WayPoint(50.5133882468757, 30.498530764186633, 150, 100));

        Flight newFlight2 = new Flight(2L, wayPoints2);
        airplane2.addFlight(newFlight2);

        List<TemporaryPoint> points2 = planeCalculation.calculateRoute(airplane2.getAirplaneCharacteristics(), wayPoints2);

        FlightTask flightTask2 = new FlightTask(2, airplane2, points2, repository);

        scheduler.addFlightTask(flightTask2);

        AirplaneCharacteristics characteristics3 = new AirplaneCharacteristics(120,17,8,12);
        Airplane airplane3 = new Airplane(3L, characteristics3);
        repository.save(airplane3);

        List<WayPoint> wayPoints3 = new LinkedList<>();
        wayPoints3.add(new WayPoint(50.51224223153982, 30.49879854469698, 100, 50));
        wayPoints3.add(new WayPoint(50.51229855910583, 30.494013089272446, 80, 20));
        wayPoints3.add(new WayPoint(50.5133882468757, 30.498530764186633, 150, 100));
        wayPoints3.add(new WayPoint(50.5110024583276, 30.498530764186633, 120, 75));

        Flight newFlight3 = new Flight(3L, wayPoints3);
        airplane3.addFlight(newFlight3);

        List<TemporaryPoint> points3 = planeCalculation.calculateRoute(airplane3.getAirplaneCharacteristics(), wayPoints3);

        FlightTask flightTask3 = new FlightTask(3, airplane3, points3, repository);

        scheduler.addFlightTask(flightTask3);
    }
}
