package io.ylab.intensive.lesson04.persistentmap;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, методы которого надо реализовать
 */
public class PersistentMapImpl implements PersistentMap {

    private String mapName;
    private DataSource dataSource;
    private Connection connection;

    public PersistentMapImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(String name) {
        mapName = name;
    }

    @Override
    public boolean containsKey(String key) throws SQLException {
        final String containsKeyQuery = "SELECT VALUE FROM persistent_map WHERE map_name = ? AND KEY = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(containsKeyQuery)) {
            preparedStatement.setString(1, mapName);
            preparedStatement.setString(2, key);
            ResultSet valuesResult = preparedStatement.executeQuery();
            return valuesResult.next();
        }
    }

    @Override
    public List<String> getKeys() throws SQLException {
        final String getKeysQuery = "SELECT KEY FROM persistent_map WHERE map_name = ?";
        List<String> keys = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(getKeysQuery)) {
            preparedStatement.setString(1, mapName);
            ResultSet keysResult = preparedStatement.executeQuery();
            while (keysResult.next()) {
                keys.add(keysResult.getString(1));
            }
            return keys;
        }
    }

    @Override
    public String get(String key) throws SQLException, KeyNotFoundException {
        final String getQuery = "SELECT value FROM persistent_map WHERE map_name = ? AND key = ?";
        if (!containsKey(key)) {
            throw new KeyNotFoundException("Key doesn't exist");
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(getQuery)) {
            preparedStatement.setString(1, mapName);
            preparedStatement.setString(2, key);
            ResultSet value = preparedStatement.executeQuery();
            value.next();
            return value.getString(1);
        }
    }

    @Override
    public void remove(String key) throws SQLException {
        final String removeQuery = "DELETE FROM persistent_map WHERE map_name = ? AND KEY = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(removeQuery)) {
            preparedStatement.setString(1, mapName);
            preparedStatement.setString(2, key);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void put(String key, String value) throws SQLException {
        final String deleteKeyQuery = "DELETE FROM persistent_map WHERE map_name = ? AND KEY = ?";
        final String putKeyValueQuery = "INSERT INTO persistent_map (map_name, KEY, value) VALUES (?,?,?)";
        try (PreparedStatement deletePreparedStatement = connection.prepareStatement(deleteKeyQuery);
             PreparedStatement putPreparedStatement = connection.prepareStatement(putKeyValueQuery)) {
            deletePreparedStatement.setString(1, mapName);
            deletePreparedStatement.setString(2, key);
            deletePreparedStatement.executeUpdate();
            putPreparedStatement.setString(1, mapName);
            putPreparedStatement.setString(2, key);
            putPreparedStatement.setString(3, value);
            putPreparedStatement.executeUpdate();
        }
    }

    @Override
    public void clear() throws SQLException {
        final String deleteAllKeysQuery = "DELETE FROM persistent_map WHERE map_name = ?";
        try (PreparedStatement deletePreparedStatement = connection.prepareStatement(deleteAllKeysQuery)) {
            deletePreparedStatement.setString(1, mapName);
            deletePreparedStatement.executeUpdate();
        }
    }
}
