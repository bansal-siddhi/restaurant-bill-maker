package com.restaurant.billGenerator.dao;

import com.restaurant.billGenerator.dto.MenuCategory;

import java.util.List;

public interface MenuCategoryDAO {
    String getCategoryById(int id) throws Exception;

    List<String> getAllCategories() throws Exception;

    boolean insertCategory(String menuCategoryDAO) throws Exception;

    boolean deleteCategory(String category) throws Exception;
}
