package com.lxnj.car_filter;

import java.sql.Connection;
import java.sql.SQLException;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * Created by yiguo on 1/15/15.
 */
public class DBConnector {

    private static PropertiesReader propertiesReader = PropertiesReader.getInstance();
    private static final String JDBC_DRIVER = propertiesReader.getJDBCDriver();
    private static final String DBConnectionStr = propertiesReader.getConnectionStr();
    private static final String User = propertiesReader.getUser();
    private static final String Password = propertiesReader.getPassword();
    private static final int MinConnectionPerPartition = 5;
    private static final int MaxConnectionPerPartition = 10;
    private static final int partitionCount = 1;
    private static BoneCP connectionPool;

    private static void connectToDB() {
        try {

            Class.forName(JDBC_DRIVER).newInstance();
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(DBConnectionStr);
            config.setUsername(User);
            config.setPassword(Password);
            config.setMinConnectionsPerPartition(MinConnectionPerPartition);
            config.setMaxConnectionsPerPartition(MaxConnectionPerPartition);
            config.setPartitionCount(partitionCount);
            connectionPool = new BoneCP(config); // setup the connection pool

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connectionPool == null) {
            connectToDB();
        }
        try {
            return connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnectionPool() {
        connectionPool.close();
    }
}
