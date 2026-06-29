package com.flysafe.prototype;



public class FlightSeatMap{
    private final boolean[][] seat = new boolean [7][3];

    public boolean bookSeat(String name, int row, int col){
        if (row>=0 && row<7 && col>=0 && col<3){

            /* In order to avoid the Race Condition, we put a lock in this critical section.
            This locks the current object instance and allows only one thread inside at a time.This
             forces the others to wait outside until the thread inside is out of the synchronized block.
             */
            synchronized (this) {
                if (!seat[row][col]) {    //checking if the seat is empty
                    //stimulating processing delay
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    //book the seat
                    seat[row][col] = true;

                    return true;
                } else {
                    //already occupied!
                    return false;
                }
            }
        }
        System.out.println("Invalid seat!");
        return false;
    }

    public void printSeatMap(){
        for(int i=0; i<7; i++){
            for(int j=0; j<3; j++){
                if(!seat[i][j]){
                    System.out.print( "[0] ");
                }
                else{
                    System.out.print( "[X] ");
                }
            }
            System.out.print("\n");
        }
    }
}
