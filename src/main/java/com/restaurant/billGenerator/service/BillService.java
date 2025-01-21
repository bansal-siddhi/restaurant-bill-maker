package com.restaurant.billGenerator.service;

import com.restaurant.billGenerator.dao.impl.BillDAOImpl;
import com.restaurant.billGenerator.dao.impl.MenuItemImpl;
import com.restaurant.billGenerator.dto.DiningOrder;
import com.restaurant.billGenerator.dto.MenuItem;
import com.restaurant.billGenerator.dto.OrderedItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BillService {

    private final BillDAOImpl billDAO;
    private final MenuItemImpl menuDAO;

    public BillService(BillDAOImpl billDAO, MenuItemImpl menuDAO) {
        this.billDAO = billDAO;
        this.menuDAO = menuDAO;
    }

    public List<MenuItem> generateMenu() throws Exception {
        return menuDAO.getAllMenuItems();
    }

    public void printMenu(List<MenuItem> menu) {
        System.out.println("Welcome to Mumma's Kitchen BLR.");
        System.out.println("-------------------------");
        System.out.println("Menu:");

        for (MenuItem item : menu) {
            System.out.println(item.getItemName() + "     " + item.getItemPrice());
        }
    }

    public void printBill(DiningOrder bill) throws Exception {
        bill.setTotalItems(bill.getItems().size());
        double subTotal = 0;
        for (OrderedItem item : bill.getItems()) {
            subTotal += item.getMenuItem().getItemPrice() * item.getQuantity();
        }
        bill.setSubTotal(subTotal);
        bill.setTax(0.05 * subTotal);
        bill.setTotalAmount(subTotal + bill.getTax());

        int billNumber = billDAO.saveBill(bill);
        billDAO.saveBillDetails(bill.getItems(), billNumber);

        System.out.println("\nYour Bill Number: " + bill.getBillNumber());
        System.out.println("----------------------------------");
        System.out.println("Item                Price             Quantity            TotalPrice");

        for (OrderedItem item : bill.getItems()) {
            System.out.println(item.getMenuItem().getItemName() + "   " + item.getMenuItem().getItemPrice() + "      " + item.getQuantity() + "      " + item.getQuantity() * item.getMenuItem().getItemPrice());
        }

        System.out.println("----------------------------------");
        System.out.println("Subtotal:       " + subTotal);

        System.out.println("Tax: " + bill.getTax());

        System.out.println("Total:" + bill.getTotalAmount());
        System.out.println("----------------------------------");
    }

    public void generateBill(List<MenuItem> menu) throws Exception {
        Scanner sc = new Scanner(System.in);
        printMenu(menu);
        while (true) {
            DiningOrder bill = new DiningOrder();
            Map<String, OrderedItem> itemNames = new HashMap<>();
            while (true) {
                System.out.print("Enter item number to order (0 to finish): ");
                int choice = sc.nextInt();

                if (choice == 0) break;

                if (choice > 0 && choice < menu.size()) {
                    System.out.println("Enter the quantity:");
                    int quantity = sc.nextInt();

                    String itemName = menu.get(choice - 1).getItemName();

                    itemNames.merge(itemName, new OrderedItem(menu.get(choice - 1), quantity),
                            (existingItem, newItem) -> {
                                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                                return existingItem;
                            });
                } else {
                    System.out.println("please enter proper menu item");
                }
            }
            itemNames.forEach((k, v) -> bill.getItems().add(v));
            printBill(bill);
            System.out.println("Generate another bill (y/n)?");
            if (!sc.next().trim().equalsIgnoreCase("y")) break;
        }

        System.out.println("Thank you for dining with us!");
        sc.close();
    }

    public void generateAndSaveBill() throws Exception {
        List<MenuItem> menu = generateMenu();
        generateBill(menu);
    }

}
