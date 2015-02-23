package com.lxnj.car_filter;

import org.apache.commons.lang3.text.WordUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by yiguo on 1/15/15.
 */
public class GetModelsByMakeServlet extends HttpServlet {

    private Connection conn = null;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DBConnector.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String make = request.getParameter("make");

        String queryStr = "select distinct model\nfrom car \nwhere lower(make) = '" + make + "'\norder by model;";

        Statement stmt;
        PrintWriter writer = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(queryStr.toLowerCase());

            int rowNum = 0;
            if (rs.last()) {
                rowNum = rs.getRow();
                rs.first();
            }

            String[] models = new String[rowNum];

            for (int i = 0; i < rowNum; i++) {
                models[i] = WordUtils.capitalizeFully(rs.getString("model"), '-', ' ');
                rs.next();
            }
            mapper.writeValue(writer, models);
            writer.flush();
            writer.close();

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
