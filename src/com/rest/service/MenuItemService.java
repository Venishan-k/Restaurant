package com.rest.service;

import com.rest.models.MenuItem;
import com.rest.repository.MenuItemRepository;
import java.util.List;

public class MenuItemService {
    private final MenuItemRepository repository;

    public MenuItemService() {
        this.repository = new MenuItemRepository();
    }

    public MenuItemService(MenuItemRepository repository) {
        this.repository = repository;
    }

    public List<MenuItem> getAllMenuItems() {
        return repository.findAll();
    }

    public MenuItem getMenuItemById(int id) {
        return repository.findById(id);
    }

    public MenuItem addMenuItem(String name, String category, double price) {
        int id = repository.getNextId();
        MenuItem item = new MenuItem(id, name, category, price);
        repository.addMenuItem(item);
        return item;
    }

    public boolean removeMenuItem(int id) {
        return repository.removeMenuItem(id);
    }

    public MenuItemRepository getRepository() {
        return repository;
    }
}
