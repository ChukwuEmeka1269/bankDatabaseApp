package com.rexco;

import com.zaxxer.hikari.HikariDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;

import javax.sql.DataSource;

import java.sql.*;
import java.time.LocalDateTime;

import static com.rexco.AppConstant.*;

public class BankAppV14 {
    public static final String INSERT_QUERY = "INSERT INTO users(first_name, last_name, registration_date) VALUES (?,?,?)";

    public static final String UPDATE_QUERY_DEDUCT_BALANCE = "UPDATE users SET balance = (balance - ?) WHERE id = ?";
    public static final String UPDATE_QUERY_INCREASE_BALANCE = "UPDATE users SET balance = (balance + ?) WHERE id = ?";
    public static final String INSERT_TRANSFER_QUERY = "INSERT INTO transfers (sender, receiver, amount) VALUES (?,?,?)";



    public static void main(String[] args) throws SQLException {
        DataSource dataSource = createDataSource();

        User sender = new User("Greg", "Eze", LocalDateTime.now());
        User receiver = new User("Receiver", "Ben", LocalDateTime.now());
        int amount = 50;

        Connection connection = dataSource.getConnection();

        try (connection) {

            connection.setAutoCommit(false);

            int senderId = createUser(connection, sender);
            int receiverId = createUser(connection, receiver);
            int transferId = sendMoney(connection, senderId, receiverId, amount);
            System.out.println("Created users with senderId = " + senderId + "," +
                    "receiverId = " + receiverId + " and transfer with id = " +
                    transferId);

            connection.commit();

        }catch (SQLException exception){
            connection.rollback();
        }
    }

    private static int sendMoney(Connection connection, int senderId, int receiverId, int amount)throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY_DEDUCT_BALANCE, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1, amount);
            statement.setInt(2, senderId);

            statement.executeUpdate();
        }

        try(PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY_INCREASE_BALANCE, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1, amount);
            statement.setInt(2, receiverId);

            statement.executeUpdate();
        }

        try(PreparedStatement statement = connection.prepareStatement(INSERT_TRANSFER_QUERY, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1, senderId);
            statement.setInt(2, receiverId);
            statement.setInt(3, amount);

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            return generatedKeys.getInt(1);
        }
    }

    private static int createUser(Connection connection, User user) throws SQLException{
        try(PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setObject(3, user.getRegistrationDate());

            statement.executeUpdate();

            final ResultSet generatedKeyResultSet = statement.getGeneratedKeys();
            generatedKeyResultSet.next();

            return generatedKeyResultSet.getInt(1);
        }
    }

    private static DataSource createDataSource(){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(URL_CONNECTION);
        hikariDataSource.setUsername(DB_USERNAME);
        hikariDataSource.setPassword(DB_PASSWORD);

        return ProxyDataSourceBuilder.create(hikariDataSource).logQueryToSysOut().build();
    }
}



