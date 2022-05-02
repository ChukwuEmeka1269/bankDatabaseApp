package com.rexco;

import com.zaxxer.hikari.HikariDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;


import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;


import static com.rexco.AppConstant.*;

public class BankApp {
    public static final String INSERT_USER_QUERY = "INSERT INTO users(first_name, last_name, registration_date) " +
            "VALUES (?,?,?)";

    public static final String SELECT_USERS_QUERY = "SELECT * FROM users WHERE id = ?";


    public static void main(String[] args) throws SQLException{

        DataSource ds = createDataSource();
       try(Connection connection = ds.getConnection()){
           System.out.println("Connection.isValid(0) = " + connection.isValid(0));

           try(PreparedStatement statement = connection.prepareStatement(SELECT_USERS_QUERY,Statement.RETURN_GENERATED_KEYS)){
               statement.setInt(1, 6);
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
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL_CONNECTION);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);

        return ProxyDataSourceBuilder.create(dataSource).logQueryToSysOut().build();
    }
}
