package ua.mainacademy.dao;

import ua.mainacademy.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

public class ItemDAO {

    public static Item save(Item item) {
        String sql = "" +
                "INSERT INTO items(name , item_code, price, init_price) " +
                "VALUES(?,?,?,?)";
        String sequenceSql = "" +
                "SELECT currval('items_id_seq')";

        try (
                Connection connection = ConnectionToDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                PreparedStatement sequenceStatement = connection.prepareStatement(sequenceSql)
        ) {
            preparedStatement.setString(1, item.getName());
            preparedStatement.setString(2, item.getItemCode());
            preparedStatement.setInt(3, item.getPrice());
            preparedStatement.setInt(4, item.getInitPrice());
            preparedStatement.executeUpdate();
            ResultSet resultSet = sequenceStatement.executeQuery();
            Integer id = null;
            while (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            return Item.builder()
                    .id(id)
                    .name(item.getName())
                    .itemCode(item.getItemCode())
                    .price(item.getPrice())
                    .initPrice(item.getInitPrice())
                    .build();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("Item with code %s was not created", item.getItemCode()));
    }

    public static Item update(Item item) {
        if (isNull(item.getId())) {
            throw new RuntimeException("id is null, update is impossible");
        }
        String sql = "" +
                "UPDATE items " +
                "SET name=?, item_code=?, price=?, init_price=? " +
                "WHERE id=?";
        try (
                Connection connection = ConnectionToDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, item.getName());
            preparedStatement.setString(2, item.getItemCode());
            preparedStatement.setInt(3, item.getPrice());
            preparedStatement.setInt(4, item.getInitPrice());
            preparedStatement.setInt(5, item.getId());
            preparedStatement.executeUpdate();
            return item;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("Item with id %d was not updated", item.getId()));
    }

    public static Optional<Item> findById(Integer id) {
        String sql = "" +
                "SELECT * FROM items " +
                "WHERE id=?";
        try (
                Connection connection = ConnectionToDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Item item = Item.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .itemCode(resultSet.getString("item_code"))
                        .price(resultSet.getInt("price"))
                        .initPrice(resultSet.getInt("init_price"))
                        .build();
                return Optional.of(item);
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
                "FROM items " +
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

    public static List<Item> findPayedItemsByUserAndPeriod(Integer userId, Long dateFrom, Long dateTo) {
        List<Item> result = new ArrayList<>();
        String sql = "" +
                "SELECT i.* FROM items i " +
                "JOIN orders o ON i.id=o.item_id " +
                "JOIN carts c ON o.cart_id=c.id " +
                "AND c.user_id=? " +
                "WHERE c.creation_time>=? " +
                "AND c.creation_time<=? " +
                "AND c.status=1"; // cart must be closed
        try (
                Connection connection = ConnectionToDB.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setLong(2, dateFrom);
            preparedStatement.setLong(3, dateTo);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Item item = Item.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .itemCode(resultSet.getString("item_code"))
                        .price(resultSet.getInt("price"))
                        .initPrice(resultSet.getInt("init_price"))
                        .build();
                result.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }
}
