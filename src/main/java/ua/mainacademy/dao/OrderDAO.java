package ua.mainacademy.dao;

import ua.mainacademy.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static java.util.Objects.isNull;

public class OrderDAO {

    public static Order save(Order order) {
        String sql = "" +
                "INSERT INTO orders(item_id , cart_id, amount) " +
                "VALUES(?,?,?)";
        String sequenceSql = "" +
                "SELECT currval('orders_id_seq')";

        try (
                Connection connection = ConnectionToDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                PreparedStatement sequenceStatement = connection.prepareStatement(sequenceSql)
        ) {
            preparedStatement.setInt(1, order.getItemId());
            preparedStatement.setInt(2, order.getCartId());
            preparedStatement.setInt(3, order.getAmount());
            preparedStatement.executeUpdate();
            ResultSet resultSet = sequenceStatement.executeQuery();
            Integer id = null;
            while (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            return Order.builder()
                    .id(id)
                    .cartId(order.getCartId())
                    .itemId(order.getItemId())
                    .amount(order.getAmount())
                    .build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("Order with cardId %d and itemId %d was not created", order.getCartId(), order.getItemId()));
    }

    public static Order update(Order order) {
        if (isNull(order.getId())) {
            throw new RuntimeException("id is null, update is impossible");
        }
        String sql = "" +
                "UPDATE orders " +
                "SET item_id=?, cart_id=?, amount=? " +
                "WHERE id=?";
        try (
                Connection connection = ConnectionToDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, order.getItemId());
            preparedStatement.setInt(2, order.getCartId());
            preparedStatement.setInt(3, order.getAmount());
            preparedStatement.setInt(4, order.getId());
            preparedStatement.executeUpdate();
            return order;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("Order with id %d was not updated", order.getId()));
    }

    public static Optional<Order> findById(Integer id) {
        String sql = "" +
                "SELECT * FROM orders " +
                "WHERE id=?";
        try (
                Connection connection = ConnectionToDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Order order = Order.builder()
                        .id(resultSet.getInt("id"))
                        .itemId(resultSet.getInt("item_id"))
                        .cartId(resultSet.getInt("cart_id"))
                        .amount(resultSet.getInt("amount"))
                        .build();
                return Optional.of(order);
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
                "FROM orders " +
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
