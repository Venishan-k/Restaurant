package com.rest.service;

import com.rest.models.MenuItem;
import com.rest.models.Order;
import com.rest.models.Order.OrderStatus;
import com.rest.models.Table;
import com.rest.repository.MenuItemRepository;
import com.rest.repository.OrderRepository;
import com.rest.repository.TableRepository;
import java.util.List;

public class OrderService {
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderService(OrderRepository orderRepository, TableRepository tableRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.tableRepository = tableRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public Order createOrder(int tableId) {
        Table table = tableRepository.findById(tableId);
        if (table == null) {
            throw new IllegalArgumentException("Table with ID " + tableId + " does not exist.");
        }

        Order existingActiveOrder = orderRepository.findActiveOrderByTableId(tableId);
        if (existingActiveOrder != null) {
            throw new IllegalStateException("Table " + tableId + " already has an active order (Order #" + existingActiveOrder.getId() + ").");
        }

        table.occupyTable();
        int orderId = orderRepository.getNextOrderId();
        Order order = new Order(orderId, tableId);
        orderRepository.save(order);
        return order;
    }

    public boolean addItemToOrder(int orderId, int menuItemId, int quantity) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order #" + orderId + " not found.");
        }
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot modify order with status: " + order.getStatus());
        }

        MenuItem item = menuItemRepository.findById(menuItemId);
        if (item == null) {
            throw new IllegalArgumentException("Menu item with ID " + menuItemId + " not found.");
        }
        if (!item.isAvailable()) {
            throw new IllegalStateException("Menu item '" + item.getName() + "' is currently unavailable.");
        }

        order.addItem(item, quantity);
        orderRepository.save(order);
        return true;
    }

    public boolean removeItemFromOrder(int orderId, int menuItemId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order #" + orderId + " not found.");
        }
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot modify order with status: " + order.getStatus());
        }

        boolean removed = order.removeItem(menuItemId);
        if (removed) {
            orderRepository.save(order);
        }
        return removed;
    }

    public Order getOrderById(int orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getActiveOrderByTable(int tableId) {
        return orderRepository.findActiveOrderByTableId(tableId);
    }

    public boolean updateOrderStatus(int orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            return false;
        }

        order.setStatus(newStatus);
        if (newStatus == OrderStatus.COMPLETED || newStatus == OrderStatus.CANCELLED) {
            Table table = tableRepository.findById(order.getTableId());
            if (table != null) {
                // Free table if no other active order exists for this table
                Order activeOrder = orderRepository.findActiveOrderByTableId(table.getId());
                if (activeOrder == null || activeOrder.getId() == orderId) {
                    table.freeTable();
                }
            }
        }
        orderRepository.save(order);
        return true;
    }

    public String getBill(int orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            return "Order #" + orderId + " not found.";
        }
        return order.getFormattedBill();
    }
}
