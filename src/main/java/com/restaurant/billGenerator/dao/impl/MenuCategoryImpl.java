package com.restaurant.billGenerator.dao.impl;

import com.restaurant.billGenerator.dao.MenuCategoryDAO;
import com.restaurant.billGenerator.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuCategoryImpl implements MenuCategoryDAO {

    private static final String TABLE_NAME = "\"Restaurant\".menu_category";
    private static final String GET_CATEGORIES = "select * from " + TABLE_NAME;
    private static final String GET_CATEGORY = GET_CATEGORIES + " where id = (?)";
    private static final String INSERT_INTO_TABLE = "INSERT INTO " + TABLE_NAME + " (category_name) values(?)";
    private static final String DELETE_CATEGORY = "DELETE FROM " + TABLE_NAME + " where category_name = (?)";

    private static MenuCategoryImpl instance;

    public static synchronized MenuCategoryImpl getInstance() {
        if (instance == null)
            instance = new MenuCategoryImpl();
        return instance;
    }

    @Override
    public String getCategoryById(int id) throws Exception {
        String category = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_CATEGORY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    category = resultSet.getString("category_name");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error getting category: " + e.getMessage(), e);
        }
        return category;
    }

    @Override
    public List<String> getAllCategories() throws Exception {
        List<String> categories = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_CATEGORIES);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String categoryName = resultSet.getString(2);
                categories.add(categoryName);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error getting all categories: " + e.getMessage(), e);
        }

        return categories;
    }

    @Override
    public boolean insertCategory(String menuCategory) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_INTO_TABLE)) {
            connection.setAutoCommit(false);
            try {
                statement.setString(1, menuCategory);
                statement.executeUpdate();
                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                System.err.println("Transaction rolled back due to error: " + e.getMessage());
                throw new Exception("Error inserting menu category: " + e.getMessage(), e);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error preparing statement or getting connection: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteCategory(String category) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_CATEGORY)) {

            connection.setAutoCommit(false);
            try {
                statement.setString(1, category);
                int rowsAffected = statement.executeUpdate();
                connection.commit();
                return rowsAffected > 0;
            } catch (SQLException e) {
                connection.rollback();
                System.err.println("Transaction rolled back due to error: " + e.getMessage());
                throw new Exception("Error deleting category: " + e.getMessage(), e);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error preparing statement or getting connection: " + e.getMessage(), e);
        }
    }
}
