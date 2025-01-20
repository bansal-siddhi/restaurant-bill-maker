package com.restaurant.billGenerator.dao.impl;

import com.restaurant.billGenerator.dao.MenuItemDAO;
import com.restaurant.billGenerator.dto.MenuCategory;
import com.restaurant.billGenerator.dto.MenuItem;
import com.restaurant.billGenerator.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemImpl implements MenuItemDAO {

    private static final String TABLE_NAME = "\"Restaurant\".menu";
    private static final String GET_MENU = "select * from " + TABLE_NAME;
    private static final String GET_MENU_ITEM = GET_MENU + " where menu_category_name = (?)";
    private static final String INSERT_INTO_TABLE = "INSERT INTO " + TABLE_NAME + " (item_name, item_price, menu_category_name) values(?,?,?)";
    private static final String DELETE_MENU_ITEM = "DELETE FROM " + TABLE_NAME + " where item_name = (?)";

    private static MenuItemImpl instance;

    public static synchronized MenuItemImpl getInstance() {
        if (instance == null)
            instance = new MenuItemImpl();
        return instance;
    }

    @Override
    public List<MenuItem> getMenuItemByCategoryName(String categoryName) throws Exception {
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_MENU_ITEM);
        ) {
            statement.setString(1, categoryName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                menuItems.add(new MenuItem(resultSet.getString(2), resultSet.getDouble(3), MenuCategory.valueOf(resultSet.getString(4))));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error getting all menu items: " + e.getMessage(), e);
        }
        return menuItems;
    }

    @Override
    public List<MenuItem> getAllMenuItems() throws Exception {
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_MENU);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                menuItems.add(new MenuItem(resultSet.getString(2), resultSet.getDouble(3), MenuCategory.valueOf(resultSet.getString(4))));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error getting all menu items: " + e.getMessage(), e);
        }
        return menuItems;
    }

    @Override
    public boolean insertMenuItem(MenuItem menuItem) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_INTO_TABLE)) {
            connection.setAutoCommit(false);
            try {
                statement.setString(1, menuItem.getItemName());
                statement.setDouble(2, menuItem.getItemPrice());
                statement.setString(3, menuItem.getMenuCategory().toString());
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
    public boolean deleteMenuItem(String itemName) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_MENU_ITEM)) {

            connection.setAutoCommit(false);
            try {
                statement.setString(1, itemName);
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
