package ua.mainacademy.dao;

import ua.mainacademy.model.Cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Objects.isNull;

public class CartDAO {

    public static Cart save(Cart cart) {
        String sql = "" +
                "INSERT INTO carts(user_id , status, creation_time) " +
                "VALUES(?,?,?)";
        String sequenceSql = "" +
                "SELECT currval('carts_id_seq')";

        try (
                Connection connection = ConnectionToDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                PreparedStatement sequenceStatement = connection.prepareStatement(sequenceSql)
        ) {
            preparedStatement.setInt(1, cart.getUserId());
            preparedStatement.setInt(2, cart.getStatus().ordinal());
            preparedStatement.setLong(3, cart.getCreationTime());
            preparedStatement.executeUpdate();
            ResultSet resultSet = sequenceStatement.executeQuery();
            Integer id = null;
            while (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            return Cart.builder()
                    .id(id)
                    .userId(cart.getUserId())
                    .status(cart.getStatus())
                    .creationTime(cart.getCreationTime())
                    .build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("Cart with userId %d and was not created", cart.getUserId()));
    }

    public static Cart update(Cart cart) {
        if (isNull(cart.getId())) {
            throw new RuntimeException("id is null, update is impossible");
        }
        String sql = "" +
                "UPDATE carts " +
                "SET user_id=?, status=?, creation_time=? " +
                "WHERE id=?";
        try (
                Connection connection = ConnectionToDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, cart.getUserId());
            preparedStatement.setInt(2, cart.getStatus().ordinal());
            preparedStatement.setLong(3, cart.getCreationTime());
            preparedStatement.setInt(4, cart.getId());
            preparedStatement.executeUpdate();
            return cart;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("Cart with id %d was not updated", cart.getId()));
    }

    public static Optional<Cart> findById(Integer id) {
        String sql = "" +
                "SELECT * FROM carts " +
                "WHERE id=?";
        try (
                Connection connection = ConnectionToDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Cart cart = Cart.builder()
                        .id(resultSet.getInt("id"))
                        .userId(resultSet.getInt("user_id"))
                        .creationTime(resultSet.getLong("creation_time"))
//                        .status(Cart.Status.values()[resultSet.getInt("status")])
                        .status(Arrays.asList(Cart.Status.values()).get(resultSet.getInt("status")))
                        .build();
                return Optional.of(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static void delete(Integer id) {
        if (isNull(id)) {
            throw new RuntimeException("id is null, delete is impossible");
        }
        String sql = "DELETE " +
                "FROM carts " +
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
