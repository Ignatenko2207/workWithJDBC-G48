package ua.mainacademy.dao;

import ua.mainacademy.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Objects.isNull;

public class UserDAO {

    private static final Logger LOG = Logger.getLogger(UserDAO.class.getName());

    public static User getUserByLoginAndPassword(String login, String password) {
        String sql = "" +
                "SELECT * " +
                "FROM users " +
                "WHERE login=? " +
                "AND password=?";
        try (
                Connection connection = ConnectionToDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return User.builder()
                        .id(resultSet.getInt("id"))
                        .login(resultSet.getString("login"))
                        .password(resultSet.getString("password"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("User with login %s and password %s was not found", login, password));
    }

    public static User save(String login, String password, String firstName, String lastName) {
        String sql = "" +
                "INSERT INTO users(login, password, first_name, last_name) " +
                "VALUES(?,?,?,?)";
        String sequenceSql = "" +
                "SELECT currval('users_id_seq')";

        try (
                Connection connection = ConnectionToDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                PreparedStatement sequenceStatement = connection.prepareStatement(sequenceSql)
        ) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.executeUpdate();
            ResultSet resultSet = sequenceStatement.executeQuery();
            Integer id = null;
            while (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            return User.builder()
                    .id(id)
                    .login(login)
                    .password(password)
                    .firstName(firstName)
                    .lastName(lastName)
                    .build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("User with login %s and password %s was not created", login, password));
    }

    public static User update (Integer id, String login, String password, String firstName, String lastName) {
        if(isNull(id)) {
            throw new RuntimeException("id is null, update is impossible");
        }
        String sql = "" +
                "UPDATE users " +
                "SET login=?, password=?, first_name=?, last_name=? " +
                "WHERE id=?";
        try (
                Connection connection = ConnectionToDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();
            return User.builder()
                    .id(id)
                    .login(login)
                    .password(password)
                    .firstName(firstName)
                    .lastName(lastName)
                    .build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("User with id %d was not updated", id));
    }

    public static Optional<User> findById (Integer id) {
        String sql = "" +
                "SELECT * FROM users " +
                "WHERE id=?";
        try (
                Connection connection = ConnectionToDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getInt("id"))
                        .login(resultSet.getString("login"))
                        .password(resultSet.getString("password"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .build();
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static void delete (Integer id) {
        if(isNull(id)) {
            throw new RuntimeException("id is null, delete is impossible");
        }
        String sql = "DELETE " +
                "FROM users " +
                "WHERE id=?";
        try (
                Connection connection = ConnectionToDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
