package com.rexco;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;


public class BankApp4 {
    public static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = (select max(id))";
    public static final String SELECT_USERS_QUERY  = "SELECT * FROM users";

    public static void main(String[] args) throws SQLException {

        DataSource dataSource = createDataSource();

        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(DELETE_USER_QUERY, Statement.RETURN_GENERATED_KEYS)){

                int deletedRow = statement.executeUpdate();

                System.out.println("Successfully deleted " + deletedRow + " row(s) from users table");
            }

            try(PreparedStatement statement = connection.prepareStatement(SELECT_USERS_QUERY, Statement.RETURN_GENERATED_KEYS)){
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String firstname = resultSet.getString("first_name");
                    String lastname = resultSet.getString("last_name");
                    LocalDateTime registrationDate = resultSet.getObject("registration_date", LocalDateTime.class);

                    System.out.println("Found user: " + id + " | " + firstname +
                            " | " + lastname + " | " + registrationDate);
                }
            }
        }


    }


    private static DataSource createDataSource(){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(AppConstant.URL_CONNECTION);
        hikariDataSource.setUsername(AppConstant.DB_USERNAME);
        hikariDataSource.setPassword(AppConstant.DB_PASSWORD);

        return hikariDataSource;
    }
}
