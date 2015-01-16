package com.lxnj.car_filter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by yiguo on 1/12/15.
 */
public class Car {
    String make;
    String model;
    int year;
    String vin;
    int odometer;
    double condition;
    String type;
    double price;
    String engine;
    int ln;
    int run;
    String saleDate;
    String color;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public double getCondition() {
        return condition;
    }

    public void setCondition(double condition) {
        this.condition = condition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public int getLn() {
        return ln;
    }

    public void setLn(int ln) {
        this.ln = ln;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Car(ResultSet rs){
        try {
            make = rs.getString("make");
            model = rs.getString("model");
            year = rs.getInt("year");
            vin = rs.getString("vin");
            odometer = rs.getInt("miles");
            condition = rs.getDouble("condition");
            type = rs.getString("type");
            color = rs.getString("color");
            price = rs.getDouble("price");
            engine = rs.getString("engine");
            ln = rs.getInt("lane");
            saleDate = rs.getDate("saleDate").toString();
            run = rs.getInt("run");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
