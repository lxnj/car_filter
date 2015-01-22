package com.lxnj.car_filter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by yiguo on 1/12/15.
 */
public class GetCarsServlet extends HttpServlet {

    private static PropertiesReader propertiesReader = PropertiesReader.getInstance();
    private Connection conn = null;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (conn == null) {
            conn = DBConnector.getConnection();
        }

        String make = request.getParameter("make");
        String model = request.getParameter("model");
        int year = 0;
        if(request.getParameter("year") != null){
            year = Integer.parseInt(request.getParameter("year"));
        }

        int odometer_min = propertiesReader.getOdometerMin();
        if(request.getParameter("odometer_min") != null) {
            odometer_min = Math.max(Integer.parseInt(request.getParameter("odometer_min")), odometer_min);
        }
        int odometer_max = propertiesReader.getOdometerMax();
        if(request.getParameter("odometer_max") != null){
            odometer_max = Math.min(Integer.parseInt(request.getParameter("odometer_max")), odometer_max);
        }

        String[] colors = request.getParameterValues("color");

        double condition_min = propertiesReader.getConditionMin();
        if(request.getParameter("condition_min") != null) {
            condition_min = Math.max(Double.parseDouble(request.getParameter("condition_min")), condition_min);
        }
        double condition_max = propertiesReader.getConditionMax();
        if(request.getParameter("condition_max") != null){
            condition_max = Math.min(Double.parseDouble(request.getParameter("condition_max")), condition_max);
        }

        String type = request.getParameter("type");

        double price_min = propertiesReader.getPriceMin();
        if(request.getParameter("price_min") != null) {
            price_min = Math.max(Double.parseDouble(request.getParameter("price_min")), price_min);
        }
        double price_max = propertiesReader.getPriceMax();
        if(request.getParameter("price_max") != null){
            price_max = Math.min(Double.parseDouble(request.getParameter("price_max")), price_max);
        }

        StringBuilder querySB = new StringBuilder();
        querySB.append("select * from car\nwhere ");

        if (make != null) {
            querySB.append("lower(make) = " + "'" + make + "' and\n");
        }

        if (model != null) {
            querySB.append("lower(model) = " + "'" + model + "' and\n");
        }

        if (year != 0) {
            if(year == -2006){
                querySB.append("year <= 2006 and\n");
            }
            else
                querySB.append("year >= " + year + " and\n");
        }

        querySB.append("miles >= " + odometer_min + " and\n");
        querySB.append("miles <= " + odometer_max + " and\n");

        if (colors != null) {
            querySB.append("(\n");
            for(int i = 0; i < colors.length - 1; i++){
                querySB.append("lower(color) = " + "'" + colors[i] + "' or\n");
            }
            querySB.append("lower(color) = " + "'" + colors[colors.length - 1] + "'\n) and \n");
        }

        querySB.append("car.condition >= " + condition_min + " and\n");
        querySB.append("car.condition <= " + condition_max + " and\n");

        if (type != null) {
            querySB.append("lower(type) = " + "'" + type + "' and\n");
        }

        querySB.append("price >= " + price_min + " and\n");
        querySB.append("price <= " + price_max + " and\n");

        String saleDate = request.getParameter("sale_date");
        if(saleDate == null){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = calendar.getTime();
            saleDate = dateFormat.format(tomorrow);
        }
        querySB.append("saledate = " + "'" + saleDate + "';\n");

        Statement stmt;
        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(querySB.toString().toLowerCase());

            int rowNum = 0;
            if (rs.last()) {
                rowNum = rs.getRow();
                rs.first();
            }

            Car[] candidateCars = new Car[rowNum];

            for (int i = 0; i < rowNum; i++) {
                candidateCars[i] = new Car(rs);
                rs.next();
            }
            mapper.writeValue(writer, candidateCars);
            writer.flush();
            writer.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
