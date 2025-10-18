package de.ait.training.consultation.oop;

public class Vehicle {
    protected String model;

    public Vehicle(String model) {
        this.model = model;
    }

    public void move() {
        System.out.println(model + " is moving");
    }
}
