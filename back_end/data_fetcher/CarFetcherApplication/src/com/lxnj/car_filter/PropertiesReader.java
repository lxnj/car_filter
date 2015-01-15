package com.lxnj.car_filter;

import java.io.*;
import java.util.Properties;

/**
 * Created by yiguo on 1/12/15.
 */
public class PropertiesReader {
    private final static String propertyFilePath = "/etc/luke.properties";
    private static Properties properties;
    private static PropertiesReader propertiesReader;

    private PropertiesReader(){
        properties = new Properties();
        InputStream fin;
        try {
            fin = PropertiesReader.class.getResourceAsStream(propertyFilePath);
            properties.load(fin);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(new File(".").getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertiesReader getInstance(){
        if(propertiesReader == null)
            propertiesReader = new PropertiesReader();
        return propertiesReader;
    }

    public String getJDBCDriver(){
        return properties.getProperty("JDBC_DRIVER");
    }

    public String getConnectionStr(){
        return properties.getProperty("DB_CONNECTION_STR");
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
        return Double.parseDouble(properties.getProperty("PRICE_MIN"));
    }

    public double getPriceMax(){
        return Double.parseDouble(properties.getProperty("PRICE_MAX"));
    }
}
