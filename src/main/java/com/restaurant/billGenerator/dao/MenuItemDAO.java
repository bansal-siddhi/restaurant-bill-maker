package com.restaurant.billGenerator.dao;

import com.restaurant.billGenerator.dto.MenuItem;

import java.util.List;

public interface MenuItemDAO {
    List<MenuItem> getMenuItemByCategoryName(String categoryName) throws Exception;

    List<MenuItem> getAllMenuItems() throws Exception;

    boolean insertMenuItem(MenuItem menuItem) throws Exception;

    boolean deleteMenuItem(String itemName) throws Exception;
}
