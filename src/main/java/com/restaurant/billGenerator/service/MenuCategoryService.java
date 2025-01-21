package com.restaurant.billGenerator.service;

import com.restaurant.billGenerator.dao.impl.MenuCategoryImpl;

import java.util.Scanner;

public class MenuCategoryService {
    private final MenuCategoryImpl menuCategory;

    public MenuCategoryService(MenuCategoryImpl menuCategory) {
        this.menuCategory = menuCategory;
    }

    public void addCategory() throws Exception {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter Category");
            String category = sc.nextLine();
            menuCategory.insertCategory(category);
            System.out.println("Do you want to enter another category(y or n)?");
            String choice = sc.nextLine();
            if (choice.trim().equalsIgnoreCase("n")) break;
        }
    }
}
