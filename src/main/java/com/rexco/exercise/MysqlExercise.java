package com.rexco.exercise;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MysqlExercise {
    public static final String URL_CONNECTION = "jdbc:mysql://localhost:3306/javabrainsdb";
    public static final String DB_NAME = "root";
    public static final String DB_PASSWORD = "Password1269";


    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();

        try(Connection connection = dataSource.getConnection()){
            System.out.println("connection.isValid(0) = "+ connection.isValid(0));
        }
    }


    private static DataSource createDataSource(){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(URL_CONNECTION);
        hikariDataSource.setUsername(DB_NAME);
        hikariDataSource.setPassword(DB_PASSWORD);

        return hikariDataSource;

    }
}
