package com.hai.machine.model;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Inventory<T> {
    private Map<T, Integer> inventory = new HashMap<>();

    public int getQuantity(T item) {
        Optional<Integer> value = Optional.of(inventory.get(item));
        return !value.isPresent() ? 0 : value.get();
    }

    public void add(T item) {
        int count = inventory.getOrDefault(item, 0);
        inventory.put(item, count + 1);
    }

    public void put(T item, int quantity) {
        int count = inventory.getOrDefault(item, 0);
        inventory.put(item, count + quantity);
    }

    public boolean deduct(T item, int quantity) {
        int count = inventory.getOrDefault(item, 0);
        if(quantity > count)
            return false;
        inventory.put(item, count - quantity);
        return true;
    }

    public void deduct(T item) {
        if (availableItem(item)) {
            int count = inventory.get(item);
            inventory.put(item, count - 1);
        }
    }

    public boolean availableItem(T item) {
        return getQuantity(item) > 0;
    }

    public void clear() {
        inventory.clear();
    }

    public Map<T, Integer> getInventory() {
        return inventory;
    }
}
