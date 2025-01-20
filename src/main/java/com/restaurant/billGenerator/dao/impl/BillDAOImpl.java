package com.restaurant.billGenerator.dao.impl;

import com.restaurant.billGenerator.dao.BillDAO;
import com.restaurant.billGenerator.dto.DiningOrder;
import com.restaurant.billGenerator.dto.OrderedItem;
import com.restaurant.billGenerator.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BillDAOImpl implements BillDAO {

    private static final String BILL_ITEMS_TABLE = "\"Restaurant\".bill_items";
    private static final String BILL_TABLE = "\"Restaurant\".bill";
    private static final String INSERT_INTO_BILL_TABLE = "insert into " + BILL_TABLE + " (bill_date, sub_total, tax, total_amount) values (CURRENT_TIMESTAMP,?,?,?) RETURNING bill_id";
    private static final String INSERT_INTO_BILL_ITEMS = "insert into " + BILL_ITEMS_TABLE + "(bill_id, quantity, menu_item_name) values (?,?,?)";

    private static BillDAOImpl instance;

    public static synchronized BillDAOImpl getInstance() {
        if (instance == null)
            instance = new BillDAOImpl();
        return instance;
    }

    @Override
    public boolean saveBillDetails(List<OrderedItem> diningOrder, int billId) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_INTO_BILL_ITEMS)) {
            connection.setAutoCommit(false);

            for (OrderedItem order : diningOrder) {
                statement.setInt(1, billId);
                statement.setInt(2, order.getQuantity());
                statement.setString(3, order.getMenuItem().getItemName());
                statement.addBatch();
            }
            statement.executeBatch();

            connection.commit();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error preparing statement or getting connection: " + e.getMessage(), e);
        }
    }

    @Override
    public int saveBill(DiningOrder bill) throws Exception {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_INTO_BILL_TABLE)) {
            connection.setAutoCommit(false);
            try {
                statement.setDouble(1, bill.getSubTotal());
                statement.setDouble(2, bill.getTax());
                statement.setDouble(3, bill.getTotalAmount());
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int billId = resultSet.getInt("bill_id");
                        connection.commit();
                        return billId;
                    } else {
                        throw new SQLException("Failed to retrieve the generated bill_id.");
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                System.err.println("Transaction rolled back due to error: " + e.getMessage());
                throw new Exception("Error inserting bill: " + e.getMessage(), e);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new Exception("Error preparing statement or getting connection: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DiningOrder> getBillDetails() throws Exception {
        return List.of();
    }
}
