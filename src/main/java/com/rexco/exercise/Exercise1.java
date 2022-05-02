package com.rexco.exercise;
import  org.vibur.dbcp.ViburDBCPDataSource;


import javax.sql.DataSource;
import java.sql.Connection;

import java.sql.SQLException;

import static com.rexco.exercise.Constant.*;

public class Exercise1 {
    public static void main(String[] args) throws SQLException {

        DataSource dataSource = createDataSource();

        try(Connection connection = dataSource.getConnection()){
            System.out.println("Connection.isValid(0) = "+ connection.isValid(0));
        }
    }

    private static DataSource createDataSource(){
        ViburDBCPDataSource dataSource = new ViburDBCPDataSource();

        dataSource.setJdbcUrl(URL_CONNECTION);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);

        dataSource.start();

        return dataSource;
    }
}
