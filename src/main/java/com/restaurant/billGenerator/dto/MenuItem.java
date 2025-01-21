package com.restaurant.billGenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuItem {
    private String itemName;
    private double itemPrice;
    private MenuCategory menuCategory;
}
