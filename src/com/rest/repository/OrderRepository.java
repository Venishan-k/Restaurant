package com.rest.repository;

import com.rest.models.Order;
import com.rest.models.Order.OrderStatus;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final List<Order> orders = new ArrayList<>();
    private int nextOrderId = 101;

    public int getNextOrderId() {
        return nextOrderId++;
    }

    public void save(Order order) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId() == order.getId()) {
                orders.set(i, order);
                return;
            }
        }
        orders.add(order);
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }

    public Order findById(int id) {
        for (Order order : orders) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    public List<Order> findByTableId(int tableId) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders) {
            if (order.getTableId() == tableId) {
                result.add(order);
            }
        }
        return result;
    }

    public Order findActiveOrderByTableId(int tableId) {
        for (Order order : orders) {
            if (order.getTableId() == tableId &&
                order.getStatus() != OrderStatus.COMPLETED &&
                order.getStatus() != OrderStatus.CANCELLED) {
                return order;
            }
        }
        return null;
    }
}
