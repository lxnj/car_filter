package com.lxnj.car_filter.servlets;

import com.lxnj.car_filter.model.Car;
import com.lxnj.car_filter.model.DetailedCar;
import com.lxnj.car_filter.utils.DBConnector;
import com.lxnj.car_filter.utils.ObjectMapperFactory;
import com.lxnj.car_filter.utils.PropertiesReader;
import org.apache.commons.lang3.text.WordUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yiguo on 1/12/15.
 */
public class GetSingleCarServlet extends HttpServlet {

    private static PropertiesReader propertiesReader = PropertiesReader.getInstance();
    private Connection conn = null;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    //wget http://127.0.0.1:5000/fetch/cardetail?vehicleUniqueId=SIMULCAST.RAA.12409935 -O a.txt
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnector.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String vehicleUniqueId = request.getParameter("vehicleUniqueId").toLowerCase();

        String queryStr = "select * \nfrom cardetail \nwhere lower(vehicleId) = '" + vehicleUniqueId + "'\n;";

        Statement stmt;
        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = ObjectMapperFactory.getMapperInstance();

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(queryStr.toLowerCase());
            int rowNum = 0;
            if (rs.last()) {
                rowNum = rs.getRow();
                rs.first();
            }

            if(rowNum == 0){
                Process p = Runtime.getRuntime().exec("wget http://127.0.0.1:5000/fetch/cardetail?vehicleUniqueId=" + vehicleUniqueId + " -O .txt");
                p.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line;
                while ((line = reader.readLine())!= null) {
                    System.out.println(line);
                }

                rs = stmt.executeQuery(queryStr.toLowerCase());
                if (rs.last()) {
                    rowNum = rs.getRow();
                    rs.first();
                }
            }

            DetailedCar[] candidateCars = new DetailedCar[rowNum];

            for (int i = 0; i < rowNum; i++) {
                candidateCars[i] = new DetailedCar(rs);
                rs.next();
            }
            mapper.writeValue(writer, candidateCars);
            writer.flush();
            writer.close();

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
