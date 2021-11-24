package com.hai.machine.state.impl;

import com.hai.machine.core.impl.VendingMachineContext;
import com.hai.machine.model.Inventory;
import com.hai.machine.state.State;
import com.hai.machine.util.Item;
import com.hai.machine.util.Note;

import java.util.Optional;

public class WaitingForMoneyState implements State {
    private final VendingMachineContext context;

    public WaitingForMoneyState(VendingMachineContext context) {
        this.context = context;
    }

    @Override
    public void insertMoney(Note note) {
        long currentBalance = context.getCurrentBalance();
        context.setCurrentBalance(currentBalance + note.getValue());
        context.getCashInventory().add(note);
        context.setCurrentState(context.getWaitingForChoiceState());
    }

    @Override
    public Optional<Item> makeChoice(Item item) {
        System.out.println("You have to enter money first");
        return Optional.empty();
    }

    @Override
    public void getSelectedItem() {
        System.out.println("You have to enter money first");
    }

    @Override
    public void refund() {
        System.out.println("You have to enter money first");
    }

    @Override
    public Optional<Inventory<Item>> getCart() {
        return Optional.empty();
    }
}
