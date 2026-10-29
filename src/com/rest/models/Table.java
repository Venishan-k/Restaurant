package com.rest.models;

public class Table {

    
    public enum Availability{
        FREE, OCCUPY
    }

    
    private int id;
    private int capacity;
    private Availability Status;

    public Table(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.Status = Availability.FREE;
    }

    public int getId(){
        return id;
    }

    public int getcapacity(){
        return capacity;
    }

    public Availability getStatus(){
        return Status;
    }

    public void freeTable(){
        this.Status = Availability.FREE;
    }

    public void occupyTable(){
        this.Status = Availability.OCCUPY;
    }

}