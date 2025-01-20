package com.restaurant.billGenerator.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DiningOrder {
    private int billNumber;
    private List<OrderedItem> items = new ArrayList<>();
    private int totalItems;
    private double subTotal;
    private double tax;
    private double totalAmount;

    public DiningOrder() {
    }

    public DiningOrder(double subTotal, double tax, double totalAmount) {
        this.subTotal = subTotal;
        this.tax = tax;
        this.totalAmount = totalAmount;
    }
}
