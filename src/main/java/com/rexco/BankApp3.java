package com.rexco;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

public class BankApp3 {
    public static final String UPDATE_USER_QUERY = ("update " +
            "users set first_name = concat(first_name,?) where id > ? ");
    public static final String SELECT_USERS_QUERY = "SELECT * FROM users";

    public static void main(String[] args) throws SQLException {

        DataSource dataSource = createDataSource();

        try(Connection connection = dataSource.getConnection()){

            int updatedRows;
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, "-Buhari");

                preparedStatement.setInt(2, 8);

                updatedRows = preparedStatement.executeUpdate();
            }

            System.out.println("I just updated " + updatedRows + " rows");


            try(PreparedStatement statement = connection.prepareStatement(SELECT_USERS_QUERY, Statement.RETURN_GENERATED_KEYS)){
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    LocalDateTime registrationDate = resultSet.getObject("registration_date", LocalDateTime.class);

                    System.out.println("Found user: " + id + " | " + firstName +
                            " | " + lastName + " | " + registrationDate);
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
