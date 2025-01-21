package com.restaurant.billGenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class OrderedItem {
    private MenuItem menuItem;
    private int quantity;
}
