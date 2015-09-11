package com.lxnj.car_filter.model;

import java.sql.ResultSet;

/**
 * Created by Yiyong on 4/14/15.
 */
public class DetailedCar extends Car {
    private String trimLevel;
    //private String inServiceDate;
    private String fuelType;
    private String displacement;
    private String transmission;
    private String exteriorColor;
    private String interiorColor;
    //private String windowSticker;
    private String bodyStyle;
    private String doors;
    private String vehicleType;
    private String salvage;
    private boolean asIs;
    private String titleState;
    private String titleStatus;
    private String driveTrain;
    private String interiorType;
    private String topType;
    private String stereo;
    private String airBags;
    private String[] pictures;

    public DetailedCar(ResultSet rs) {
        super(rs);
    }

    public String getTrimLevel() {
        return trimLevel;
    }

    public void setTrimLevel(String trimLevel) {
        this.trimLevel = trimLevel;
    }

/*
    public String getInServiceDate() {
        return inServiceDate;
    }

    public void setInServiceDate(String inServiceDate) {
        this.inServiceDate = inServiceDate;
    }
*/

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getExteriorColor() {
        return exteriorColor;
    }

    public void setExteriorColor(String exteriorColor) {
        this.exteriorColor = exteriorColor;
    }

    public String getInteriorColor() {
        return interiorColor;
    }

    public void setInteriorColor(String interiorColor) {
        this.interiorColor = interiorColor;
    }

    /*    public String getWindowSticker() {
        return windowSticker;
    }

    public void setWindowSticker(String windowSticker) {
        this.windowSticker = windowSticker;
    }
    */

    public String getBodyStyle() {
        return bodyStyle;
    }

    public void setBodyStyle(String bodyStyle) {
        this.bodyStyle = bodyStyle;
    }

    public String getDoors() {
        return doors;
    }

    public void setDoors(String doors) {
        this.doors = doors;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getSalvage() {
        return salvage;
    }

    public void setSalvage(String salvage) {
        this.salvage = salvage;
    }

    public boolean isAsIs() {
        return asIs;
    }

    public void setAsIs(boolean asIs) {
        this.asIs = asIs;
    }

    public String getTitleState() {
        return titleState;
    }

    public void setTitleState(String titleState) {
        this.titleState = titleState;
    }

    public String getTitleStatus() {
        return titleStatus;
    }

    public void setTitleStatus(String titleStatus) {
        this.titleStatus = titleStatus;
    }

    public String getDriveTrain() {
        return driveTrain;
    }

    public void setDriveTrain(String driveTrain) {
        this.driveTrain = driveTrain;
    }

    public String getInteriorType() {
        return interiorType;
    }

    public void setInteriorType(String interiorType) {
        this.interiorType = interiorType;
    }

    public String getTopType() {
        return topType;
    }

    public void setTopType(String topType) {
        this.topType = topType;
    }

    public String getStereo() {
        return stereo;
    }

    public void setStereo(String stereo) {
        this.stereo = stereo;
    }

    public String getAirBags() {
        return airBags;
    }

    public void setAirBags(String airBags) {
        this.airBags = airBags;
    }

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }
}
