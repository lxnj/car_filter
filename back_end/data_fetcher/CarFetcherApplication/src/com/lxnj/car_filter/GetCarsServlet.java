package com.lxnj.car_filter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * Created by yiguo on 1/12/15.
 */
public class GetCarsServlet extends HttpServlet {

    private static PropertiesReader propertiesReader = PropertiesReader.getInstance();
    private static final String JDBC_DRIVER = propertiesReader.getJDBCDriver();
    private static final String DBConnectionStr = propertiesReader.getConnectionStr();
    private static final String User = propertiesReader.getUser();
    private static final String Password = propertiesReader.getPassword();
    private Connection conn = null;

    private void connectToDB(){
        try {
            Class.forName(JDBC_DRIVER);
            this.conn = DriverManager.getConnection(DBConnectionStr, User, Password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(conn == null){
            connectToDB();
        }

        String make = request.getParameter("make");
        String model = request.getParameter("model");
        int year = Integer.parseInt(request.getParameter("year"));

        int odometer_min = Math.max(Integer.parseInt(request.getParameter("odometer_min")), propertiesReader.getOdometerMin());
        int odometer_max = Math.min(Integer.parseInt(request.getParameter("odometer_max")), propertiesReader.getOdometerMax());

        String color = request.getParameter("color");

        double condition_min = Math.max(Double.parseDouble(request.getParameter("condition_min")), propertiesReader.getConditionMin());
        double condition_max = Math.min(Double.parseDouble(request.getParameter("condition_max")), propertiesReader.getConditionMax());

        String type = request.getParameter("type");

        double price_min = Math.max(Double.parseDouble(request.getParameter("price_min")), propertiesReader.getPriceMin());
        double price_max = Math.min(Double.parseDouble(request.getParameter("price_max")), propertiesReader.getPriceMax());

        StringBuilder querySB = new StringBuilder();
        querySB.append("select * from car\nwhere ");

        if(make != null){
            querySB.append("make = " + make + "\n");
        }

        if(model != null){
            querySB.append("and model = " + model + "\n");
        }

        if(year != 0){
            querySB.append("and year = " + year + "\n");
        }

        querySB.append("and odometer >= " + odometer_min + "\n");
        querySB.append("and odometer <= " + odometer_max + "\n");

        if(color != null){
            querySB.append("and color = " + color + "\n");
        }

        querySB.append("and condition >= " + condition_min + "\n");
        querySB.append("and condition <= " + condition_max + "\n");

        if(type != null){
            querySB.append("and type = " + type + "\n");
        }

        querySB.append("and price_min >= " + price_min + "\n");
        querySB.append("and price_max <= " + price_max + "\n");

        Statement stmt;
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

            for (int i = 0; i < rowNum; i++){
                candidateCars[i] = new Car(rs);
                rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();
        //...
    }

    public static void main(String[] args) {



    }
}
