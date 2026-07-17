package com.flysafe.model;

import jakarta.persistence.*;

@Entity    //this tells Hibernate to create a DB table named Seat.
public class Seat {

    @Id     //this turns the field into a unique identifier for the table
    @GeneratedValue(strategy = GenerationType.IDENTITY)   //this automatically assigns ID
    private Long id;   //this is a wrapper object. long can't be null, its default is 0. This confuses Hibernate into thinking that you are updating the id to 0.
    private String name;
    @Column (name = "seat_row")       //row is a reserved keyword in sql.
    private int row;
    @Column (name = "seat_col")
    private int col;
    private Boolean isBooked;  //boolean will just be true/false. Boolean adds a default null for reserved/blocked seat.

    /* We use synchronized blocks to prevent Race condition but that only works
    because everything lives inside the java's temporary memory. For web application with database,
    this block will freeze the entire website.
    ------------------------------------
    We can use Optimistic Locking using @Version
     * Multiple threads reads isBooked and version concurrently.
     * Thread1 saves it first, DB verifies version = 1. This thread books the seat and DB increments version to 2.
     * Thread2 has the old version saved, DB sees version is now 2 so it rejects this thread.
     * Prevents double-booking and helps with Race Conditions/
     */
     @Version
    private long version;

    //Default empty constructor (Hibernate requires this to recreate objects from the database
    public Seat() {
    }

    //Constructor
    public Seat(int row, int col) {
        this.row = row;
        this.col = col;
    }

    //GETTERS AND SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean getBooked() {
        return isBooked;
    }

    public void setBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
