package com.inventorySystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String DATABAS_URL = "jdbc:postgresql://localhost:5432/inventoryDb";
    private static final String DATABASE_USERNAME = "postgres";
    private static final String DATABASE_PASSWORD = "";

    private DatabaseConfig(){

    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABAS_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
    }



}
