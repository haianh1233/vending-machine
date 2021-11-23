package com.hai.machine.core;

import com.hai.machine.model.Inventory;
import com.hai.machine.util.Item;
import com.hai.machine.util.Note;

import java.util.Optional;

public interface VendingMachine {
    Optional<Item> makeChoice(Item item);

    void insertMoney(Note note);

    void getSelectedItem();

    void refund();

    Optional<Inventory<Item>> getCart();
}
