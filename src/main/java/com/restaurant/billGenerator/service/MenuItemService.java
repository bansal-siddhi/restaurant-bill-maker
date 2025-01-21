package com.restaurant.billGenerator.service;

import com.restaurant.billGenerator.dao.impl.MenuItemImpl;
import com.restaurant.billGenerator.dto.MenuCategory;
import com.restaurant.billGenerator.dto.MenuItem;

import java.util.Scanner;

public class MenuItemService {
    private final MenuItemImpl menuDAO;

    public MenuItemService(MenuItemImpl menuDAO) {
        this.menuDAO = menuDAO;
    }

    public void addMenuItem() throws Exception {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter the item name: ");
            String itemName = sc.nextLine().trim();

            double itemPrice = 0.0;
            while (true) {
                try {
                    System.out.println("Enter the item price: ");
                    itemPrice = Double.parseDouble(sc.nextLine().trim());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price format. Please enter a valid number.");
                }
            }

            System.out.println("Enter the category number: ");
            MenuCategory selectedCategory = printCategory(sc);

            menuDAO.insertMenuItem(new MenuItem(itemName, itemPrice, selectedCategory));

            System.out.println("Do you want to enter another menu item (y/n)? ");
            String continueChoice = sc.nextLine().trim();
            if (!continueChoice.equalsIgnoreCase("y")) break;
        }
    }


    public MenuCategory printCategory(Scanner sc) {
        MenuCategory[] categories = MenuCategory.values();
        MenuCategory selectedCategory;
        while (true) {
            for (int i = 0; i < categories.length; i++) {
                System.out.println((i + 1) + ". " + categories[i]);
            }
            int category = sc.nextInt();
            sc.nextLine();
            if (category > 0 && category <= categories.length) {
                selectedCategory = categories[category - 1];
                System.out.println("You selected: " + selectedCategory);
                break;
            } else {
                System.out.println("Invalid choice. Please enter a number between 1 and " + categories.length);
            }
        }
        return selectedCategory;
    }

}
