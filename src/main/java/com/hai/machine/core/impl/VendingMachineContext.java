package com.hai.machine.core.impl;

import com.hai.machine.core.VendingMachine;
import com.hai.machine.model.Inventory;
import com.hai.machine.state.State;
import com.hai.machine.state.impl.WaitingForChoiceState;
import com.hai.machine.state.impl.WaitingForMoneyState;
import com.hai.machine.util.Item;
import com.hai.machine.util.Note;

import java.util.Arrays;
import java.util.Optional;

public class VendingMachineContext implements VendingMachine {
    private final WaitingForMoneyState waitingForMoneyState;
    private final WaitingForChoiceState waitingForChoiceState;

    private Inventory<Note> cashInventory = new Inventory<>();
    private Inventory<Item> itemInventory = new Inventory<>();
    private Inventory<Item> currentItemInventory = new Inventory<>();
    private long totalSales;
    private long currentBalance;
    private State currentState;

    public VendingMachineContext() {
        init();
        this.waitingForMoneyState = new WaitingForMoneyState(this);
        this.waitingForChoiceState = new WaitingForChoiceState(this);;
        currentState = this.waitingForMoneyState;
    }

    private void init() {
        Arrays.stream(Note.values())
                .forEach(n -> cashInventory.put(n, 50000));

        Arrays.stream(Item.values())
                .forEach(i -> itemInventory.put(i , 500000));
    }


    @Override
    public Optional<Item> makeChoice(Item item) {
        return currentState.makeChoice(item);
    }

    @Override
    public void insertMoney(Note note) {
        currentState.insertMoney(note);
    }

    @Override
    public void getSelectedItem() {
        currentState.getSelectedItem();
    }

    @Override
    public void refund() {
        currentState.refund();
    }

    @Override
    public Optional<Inventory<Item>> getCart() {
        return currentState.getCart();
    }

    public WaitingForMoneyState getWaitingForMoneyState() {
        return waitingForMoneyState;
    }

    public WaitingForChoiceState getWaitingForChoiceState() {
        return waitingForChoiceState;
    }

    public Inventory<Note> getCashInventory() {
        return cashInventory;
    }

    public void setCashInventory(Inventory<Note> cashInventory) {
        this.cashInventory = cashInventory;
    }

    public Inventory<Item> getItemInventory() {
        return itemInventory;
    }

    public void setItemInventory(Inventory<Item> itemInventory) {
        this.itemInventory = itemInventory;
    }

    public long getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(long totalSales) {
        this.totalSales = totalSales;
    }

    public long getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(long currentBalance) {
        this.currentBalance = currentBalance;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public Inventory<Item> getCurrentItemInventory() {
        return currentItemInventory;
    }

    public void setCurrentItemInventory(Inventory<Item> currentItemInventory) {
        this.currentItemInventory = currentItemInventory;
    }
}
