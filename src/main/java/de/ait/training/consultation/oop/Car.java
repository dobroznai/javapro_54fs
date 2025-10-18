package de.ait.training.consultation.oop;


public class Car extends Vehicle {
    public Car(String model) {
        super(model); // отсылка к родителю
    }

    // переписан метод под себя
    @Override
    public void move() {
        System.out.println(model + " drives");
    }

}
