package com.hai.machine.state;


import com.hai.machine.model.Inventory;
import com.hai.machine.util.Item;
import com.hai.machine.util.Note;

import java.util.Optional;

public interface State {
    void insertMoney(Note note);

    Optional<Item> makeChoice(Item item);

    void getSelectedItem();

    void refund();

    Optional<Inventory<Item>> getCart();
}
