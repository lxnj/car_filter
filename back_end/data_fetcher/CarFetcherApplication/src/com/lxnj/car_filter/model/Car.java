package com.lxnj.car_filter.model;

import com.lxnj.car_filter.utils.CarUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yiguo on 1/12/15.
 */
public class Car {
    protected String make;
    protected String model;
    protected int year;
    protected String vin;
    protected int odometer;
    protected double condition;
    protected String type;
    protected String price;
    protected String engine;
    protected int ln;
    protected int run;
    protected String saleDate;
    protected String color;
    protected String abstractStr;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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

    public String getAbstractStr() {
        return abstractStr;
    }

    public void setAbstractStr(String abstractStr) {
        this.abstractStr = abstractStr;
    }

    public Car(ResultSet rs){
        try {
            make = rs.getString("make");
            model = WordUtils.capitalizeFully(rs.getString("model"), ' ', '-');
            year = rs.getInt("year");
            vin = rs.getString("vin");
            odometer = rs.getInt("miles");
            condition = rs.getDouble("condition");
            type = rs.getString("type");
            color = rs.getString("color");

            if(color.toLowerCase().equals("not avail"))
                color = "Not Available";

            price = rs.getString("price");
            if(price.toLowerCase().contains("not ava")){
                price = "Not Available";
            }
            else {
                int extra = rs.getInt("auctionfee");
                int finalPrince = Integer.parseInt(price) + extra;
                price = finalPrince + "";
            }

            engine = rs.getString("engine");
            ln = rs.getInt("lane");
            run = rs.getInt("run");
            abstractStr = trimAbstract(model, rs.getString("abstract"));

            saleDate = rs.getDate("saleDate").toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected String trimAbstract(String model, String abstractStr){
        HashMap<String, Boolean> abstractMap = new HashMap<String, Boolean>();
        HashMap<String, String> modelMap = new HashMap<String, String>();

        String[] modelTerms = model.split(" ");
        for(String modelTerm : modelTerms){
            modelMap.put(modelTerm.toLowerCase(), modelTerm);
        }

        StringBuilder sb = new StringBuilder();
        String[] terms = abstractStr.split(" ");
        for(String term : terms){
            if(modelMap.containsKey(term.toLowerCase())){
                term = modelMap.get(term.toLowerCase());
            }
            if(!abstractMap.containsKey(term)){
                sb.append(term + " ");
                abstractMap.put(term, true);
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
