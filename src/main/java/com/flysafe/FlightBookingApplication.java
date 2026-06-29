package com.flysafe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// CommandLineRunner interface instructs Spring Boot to execute the logic in this class
// only after the database components and application context are fully loaded.
@SpringBootApplication
public class FlightBookingApplication implements CommandLineRunner {

    /* We can't instantiate the fields manually using "new". Springboot will lose track of it.
    It will simply be an unmanaged Java object and won't have automatic features like @Transactional.
    They are dependency injectiion fields. The IOC container automatically supplies dependent objects to this class.
     */
    private final SeatRepository seatRepository;

    //Through this constructor the framework passes the instances of SeatRepository and SeatService into this runner class.
    public FlightBookingApplication(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    /* Step 1: JVM enters main
       Step 2: executes SpringApplication.run()
       Step 3: SpringBoot configures H2 DB and sets up Repo and Service
       Step 4: set-up finishes and springboot goes to overridden run method and does seat generation
     */
    public static void main(String[] args) {
        SpringApplication.run(FlightBookingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
      /*defined by the CommandLineInterface. It is used to initialize default seat rows into the temporary database
       before any concurrent transactions or threads are launched.
       */
        for (int row=0; row<9; row++){
            for(int col=0; col<3; col++){
                Seat seat = new Seat();
                seat.setName(null);
                seat.setBooked(false);
                seat.setCol(col);
                seat.setRow(row);
                seatRepository.save(seat);
            }
        }
        System.out.println("Database initialization done with empty seats!");
    }
}