package com.group5.restservice.group5restservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class ConnectionManager {
    /**
     * Opens a connection to the database
     * @return The database connection, or null if the connection could not be established
     * */
    public static Connection getConnection() {
        ConnectionInfo connInfo = getConnectionInfo("connection.properties");

        if (connInfo == null)
            return null;

        try {
            return DriverManager.getConnection(connInfo.getConnectionString(), connInfo.getUsername(), connInfo.getPassword());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;

        }
    }

    /**
     * Retrieves the connection info from a .properties resource file
     * @param fileName The .properties file to load
     * @return The connection info record generated from the file
     * */
    private static ConnectionInfo getConnectionInfo(String fileName) {
        System.out.println(ConnectionManager.class.getClassLoader().getName());
        InputStream iStream = ConnectionManager.class.getResourceAsStream(fileName);
        if (iStream == null){
            System.out.println("input stream is null");
            return null;
        }


        Properties props = new Properties();

        try {
            props.load(new BufferedReader(new InputStreamReader(iStream)));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }

        String dbProcotol = props.getProperty("dbProtocol");
        String host = props.getProperty("host");
        String port = props.getProperty("port");
        String db = props.getProperty("db");
        String user = props.getProperty("user");
        String password = props.getProperty("password");

        return new ConnectionInfo(dbProcotol, host, port, db, user, password);
    }
}
