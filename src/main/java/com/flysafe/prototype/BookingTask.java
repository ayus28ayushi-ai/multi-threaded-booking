package com.flysafe.prototype;

/*This allows the task to be handed over to the threads .
  The thread grabs it and enters the run() method
  */
public class BookingTask implements Runnable {
    private final FlightSeatMap flightSeatMap;
    private final String name;
    private final int row;
    private final int col;

    //Constructor
    public BookingTask(FlightSeatMap flightSeatMap, String customerName, int row, int column){
        this.flightSeatMap = flightSeatMap;   //shared flight map that all the customer threads will interact with
        this.name = customerName;
        this.row = row;
        this.col = column;
    }

    @Override
    public void run(){
        System.out.printf("Passenger %s is checking seats.\n", name);
        //Attempts to atomically check and reserve the seat.
        /* In multi-threading, atomic operation means that operation happens
        all at once without interruption from other threads.
         */
        if(flightSeatMap.bookSeat(name, row, col)){
            System.out.printf("BOOKING SUCCESSFUL! for %s\n", name);
        }
        else{
            System.out.printf("BOOKING FAILED! for %s.\nSeat already taken.\n\n", name);
        }
    }
}
