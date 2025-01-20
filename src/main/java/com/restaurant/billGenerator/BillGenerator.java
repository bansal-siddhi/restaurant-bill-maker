package com.restaurant.billGenerator;

import com.restaurant.billGenerator.dao.impl.BillDAOImpl;
import com.restaurant.billGenerator.dao.impl.MenuItemImpl;
import com.restaurant.billGenerator.dto.MenuItem;
import com.restaurant.billGenerator.service.BillService;
import com.restaurant.billGenerator.service.MenuItemService;

import java.util.*;

public class BillGenerator {
    public static int count = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("Enter owner or customer: ");
        Scanner sc = new Scanner(System.in);
        String choice = sc.next();
        switch (choice) {
            case "o":
                System.out.println("Do you want to enter a new category or menu item?(Enter c or m): ");
                String menuOption = sc.next();
                if (menuOption.trim().equals("m")) {
                    MenuItemService menuItemService = new MenuItemService(MenuItemImpl.getInstance());
                    menuItemService.addMenuItem();
                }
            case "c":
                BillService billService = new BillService(BillDAOImpl.getInstance(), MenuItemImpl.getInstance());
                List<MenuItem> menu = billService.generateMenu();
                billService.generateBill(menu);
            default:
                break;
        }
    }
}
