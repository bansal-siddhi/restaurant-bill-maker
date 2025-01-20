package com.restaurant.billGenerator.service;

import com.restaurant.billGenerator.dao.impl.MenuCategoryImpl;

public class MenuCategoryService {
    private final MenuCategoryImpl menuCategory;

    public MenuCategoryService(MenuCategoryImpl menuCategory) {
        this.menuCategory = menuCategory;
    }
}
