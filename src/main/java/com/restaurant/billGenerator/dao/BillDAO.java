package com.restaurant.billGenerator.dao;

import com.restaurant.billGenerator.dto.DiningOrder;
import com.restaurant.billGenerator.dto.OrderedItem;

import java.util.List;

public interface BillDAO {
    int saveBill(DiningOrder bill) throws Exception;

    boolean saveBillDetails(List<OrderedItem> diningOrder, int billId) throws Exception;

    List<DiningOrder> getBillDetails() throws Exception;
}
