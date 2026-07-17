package com.flysafe.controller;

import com.flysafe.model.BookingRequest;
import com.flysafe.model.Seat;
import com.flysafe.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Marks the class as a REST API controller
@RestController
@RequestMapping("/api/seats")
public class SeatController {
    //Automatically injecting the SeatService business logic bean here

    private final SeatService seatService;

    public  SeatController(SeatService seatService){
        this.seatService = seatService;
    }
    //1. CREATE
    // Handles HTTP POST requests sent to base URL
    @PostMapping("/book")
    // Returns the HTTP status (e.g. 200 OK, 201 Created, 400 Bad Request, 404 Not Found etc.) along with the response data
    public ResponseEntity<String> webSeatBooking(
            @RequestParam String name,
            @RequestParam int row,
            @RequestParam int col){

        try{
           if(seatService.bookSeat(name, row, col)) {

               return ResponseEntity.ok("Seat booked successfully!");
           }
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Booking FAILED! Seat already taken.");
        }
        // Catches Race Condition and version conflicts
        catch(ObjectOptimisticLockingFailureException e){
           return ResponseEntity.status(HttpStatus.CONFLICT).body("Collision among bookings. Seat taken by someone else.");
        }
        catch(RuntimeException e){
            //catches other exceptions.
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); //ensures that whatever message your service throws will show up directly on the user's screen.
        }
    }

    //2. READ
    // Fetches all bookings and seat status
    @GetMapping("/all")
    public  List<Seat> getAllBookings(){
        return seatService.findAll();
    }

    //3. UPDATE
    // Changes customer's name
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBooking(@PathVariable long id, @RequestBody BookingRequest bookingRequest){
        boolean update = seatService.updateBooking(id, bookingRequest.getNewName());
        if (update){
        return ResponseEntity.ok("Booking UPDATED Successfully to" + bookingRequest.getNewName());
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Cannot update. Seat not found or not currently booked.");
    }
    }


    //4. DELETE
    //Cancel the booking
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<String> cancelBooking (@PathVariable long id){
        seatService.deleteBooking(id);
        return ResponseEntity.ok("Booking CANCELLED successfully1");
    }
}
