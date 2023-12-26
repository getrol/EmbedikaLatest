package com.example.carservice.Entities;
import java.util.Objects;

public class Car {
    private String identificationNumber;
    private String model;
    private String color;
    private int bornYear;

    public Car(String identificationNumber, String model, String color, int bornYear) {
        this.identificationNumber = identificationNumber.toUpperCase()  ;
        this.model = model;
        this.color = color;
        this.bornYear = bornYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return bornYear == car.bornYear && Objects.equals(identificationNumber, car.identificationNumber) && Objects.equals(model, car.model) && Objects.equals(color, car.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificationNumber, model, color, bornYear);
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getBornYear() {
        return bornYear;
    }

    public void setBornYear(short bornYear) {
        this.bornYear = bornYear;
    }

    @Override
    public String toString() {
        return "Car{" +
                "identificationNumber='" + identificationNumber + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", bornYear=" + bornYear +
                '}';
    }
}
