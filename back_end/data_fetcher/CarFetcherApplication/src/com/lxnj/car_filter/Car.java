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
    int odometer;
    double condition;
    String type;
    double price;
    String engine;
    int ln;
    int run;

    public Car(ResultSet rs){
        try {
            make = rs.getString("make");
            model = rs.getString("model");
            year = rs.getInt("year");
            odometer = rs.getInt("odometer");
            condition = rs.getDouble("condition");
            type = rs.getString("type");
            price = rs.getDouble("price");
            engine = rs.getString("engine");
            ln = rs.getInt("ln");
            run = rs.getInt("run");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
