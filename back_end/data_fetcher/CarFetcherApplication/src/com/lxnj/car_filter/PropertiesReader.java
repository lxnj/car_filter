package com.lxnj.car_filter;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by yiguo on 1/12/15.
 */
public class PropertiesReader {
    private final static String propertyFilePath = "etc/luke.properties";
    private static Properties properties;
    private static PropertiesReader propertiesReader = new PropertiesReader();

    private PropertiesReader(){
        properties = new Properties();
        FileInputStream fin;
        try {
            fin = new FileInputStream(propertyFilePath);
            properties.load(fin);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertiesReader getInstance(){
        return propertiesReader;
    }

    public String getJDBCDriver(){
        return properties.getProperty("JDBC_DRIVER");
    }

    public String getConnectionStr(){
        return properties.getProperty("DBCONNECTINGSTR");
    }

    public String getUser(){
        return properties.getProperty("DB_USER");
    }

    public String getPassword(){
        return properties.getProperty("DB_PASSWORD");
    }

    public int getOdometerMin(){
        return Integer.parseInt(properties.getProperty("ODOMETER_MIN"));
    }

    public int getOdometerMax(){
        return Integer.parseInt(properties.getProperty("ODOMETER_MAX"));
    }

    public double getConditionMin(){
        return Double.parseDouble(properties.getProperty("CONDITION_MIN"));
    }

    public double getConditionMax(){
        return Double.parseDouble(properties.getProperty("CONDITION_MAX"));
    }

    public double getPriceMin(){
        return Double.parseDouble(properties.getProperty("PRICCE_MIN"));
    }

    public double getPriceMax(){
        return Double.parseDouble(properties.getProperty("PRICCE_MAX"));
    }
}
