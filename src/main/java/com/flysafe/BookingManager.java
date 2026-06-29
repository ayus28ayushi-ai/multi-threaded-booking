package com.flysafe;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class BookingManager {
    private final SeatService seatService;

    public BookingManager(SeatService seatService){
        this.seatService = seatService;
    }

    public void runBookingTask(String threadName, int row, int col){
        try {
            seatService.bookSeat(Thread.currentThread().getName(), row, col);
            System.out.println("Seat booking successful for " + threadName + ".");
        }
        catch (
                ObjectOptimisticLockingFailureException e){
            //for catching Race Condition failures and version conflicts
            System.out.println("Booking Failed for "+threadName+". Seat already taken.\n");
        } catch (Exception e) {
            //for catching other errors.
            System.out.println("Booking Failed for "+threadName+".");
        }
    }
}


