package com.flysafe;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeatService {
    //to talk to database, the service needs the repository
    private final SeatRepository seatRepository;

    //Constructor for Dependency injection
    /* It assigns the parameters coming from Repository to the instance field.
    This allows Spring to pass access to DB directly into the service when the application runs.
     */
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Transactional
    public boolean bookSeat(String name, int row, int col) {
        /* Since the repository only accepts row and column, how are we getting name?
        When we  call seatRepository.findByRowAndCol(row, col),we  aren't just verifying if that position exists.
         The database looks at that coordinate and  pulls the entire row record and gives a Java Seat object instance
         inside that Optional container box.
         */


        /* We don't need a try-catch here
          1. Method runs and `save()` only queues changes in memory in Hibernate's Persistence queue
          2. when the method returns, the internal try-catch is destroyed.
          3. Spring tries to queued changes to the database.
          4. DB detects race collision -> Throws locking exception AFTER method is already dead!
         So the exception is handled in the catch block in the FlightBookingApplication.
         */

            Seat seat = seatRepository.findByRowAndCol(row, col).orElseThrow(()->new RuntimeException("Seat position do not exist!"));
            //checking if the seat is already occupied
            if(seat.getBooked()){
                System.out.println("Seat already taken by someone else!");
                return false;
            }
            //booking it for the current customer
            seat.setBooked(true);
            seat.setName(name);
            seatRepository.save(seat);

        return true;
    }

    public boolean updateBooking(long id, String newName) {
        Seat seat = seatRepository.findById(id).orElseThrow(()-> new RuntimeException("No seat found with the ID: " + id));
        if(seat.getBooked()) {
            seat.setName(newName);
            seatRepository.save(seat);
            return true;
        }
        else{
            System.out.println("No booking exists for this seat!");
            return false;
        }
        }

    public List<Seat> findAll() {
    return seatRepository.findAll();
    }

    public void deleteBooking(long id) {
        Seat seat = seatRepository.findById(id).orElseThrow(()-> new RuntimeException("No seat found with the ID: " + id));
        seat.setName(null);
        seatRepository.save(seat);
    }
}
