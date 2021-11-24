package com.hai.machine.state.impl;

import com.hai.machine.core.VendingMachine;
import com.hai.machine.core.impl.VendingMachineContext;
import com.hai.machine.model.Inventory;
import com.hai.machine.state.State;
import com.hai.machine.util.Item;
import com.hai.machine.util.Note;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WaitingForChoiceState implements State {
    private final VendingMachineContext context;

    public WaitingForChoiceState(VendingMachineContext context) {
        this.context = context;
    }


    @Override
    public void insertMoney(Note note) {
        long currentBalance = context.getCurrentBalance();
        context.getCashInventory().add(note);
        context.setCurrentBalance(currentBalance + note.getValue());
    }

    @Override
    public Optional<Item> makeChoice(Item item) {
        Inventory<Item> currentItemInventory = context.getCurrentItemInventory();
        Inventory<Item> itemInventory = context.getItemInventory();
        long currentBalance = context.getCurrentBalance();
        boolean available = itemInventory.availableItem(item);
        if(!available)
            System.out.println("Item not available");
        else if(available && currentBalance < item.getPrice())
            System.out.println("Not enough money");
        else {
            context.setCurrentBalance(currentBalance - item.getPrice());
            itemInventory.deduct(item);
            currentItemInventory.add(item);
            System.out.println(item.getName() + " added!");
            return Optional.of(item);
        }

        return Optional.empty();
    }

    @Override
    public void getSelectedItem() {
        Inventory<Item> currentItemInventory = context.getCurrentItemInventory();
        currentItemInventory.getInventory()
                .forEach((key, value) -> {
                    System.out.println(String.format("%d %s is dispensed. Please collect it", value, key));
                });
        currentItemInventory.clear();
        refund();
        if(context.getCurrentBalance() == 0)
            context.setCurrentState(context.getWaitingForMoneyState());
        else
            context.setCurrentState(context.getWaitingForChoiceState());

    }

    @Override
    public void refund() {
        Inventory<Item> currentItemInventory = context.getCurrentItemInventory();
        Inventory<Item> itemInventory = context.getItemInventory();
        Inventory<Note> cashInventory = context.getCashInventory();
        currentItemInventory.getInventory()
                .forEach((key, value) -> {
                    itemInventory.put(key, value);
                });

        long currentBalance = context.getCurrentBalance();
        Inventory<Note> returnCrash = new Inventory<>();

        while (currentBalance != 0) {
            if(currentBalance >= Note.NOTE_200K.getValue() && cashInventory.availableItem(Note.NOTE_200K)) {
                currentBalance = currentBalance - Note.NOTE_200K.getValue();
                cashInventory.deduct(Note.NOTE_200K);
                returnCrash.add(Note.NOTE_200K);

            }else if(currentBalance >= Note.NOTE_100K.getValue() && cashInventory.availableItem(Note.NOTE_100K)) {
                currentBalance = currentBalance - Note.NOTE_100K.getValue();
                cashInventory.deduct(Note.NOTE_100K);
                returnCrash.add(Note.NOTE_100K);

            }else if(currentBalance >= Note.NOTE_50K.getValue() && cashInventory.availableItem(Note.NOTE_50K)) {
                currentBalance = currentBalance - Note.NOTE_50K.getValue();
                cashInventory.deduct(Note.NOTE_50K);
                returnCrash.add(Note.NOTE_50K);

            }else if(currentBalance >= Note.NOTE_20K.getValue() && cashInventory.availableItem(Note.NOTE_20K)) {
                currentBalance = currentBalance - Note.NOTE_20K.getValue();
                cashInventory.deduct(Note.NOTE_20K);
                returnCrash.add(Note.NOTE_20K);

            }else if(currentBalance >= Note.NOTE_10K.getValue() && cashInventory.availableItem(Note.NOTE_10K)) {
                currentBalance = currentBalance - Note.NOTE_10K.getValue();
                cashInventory.deduct(Note.NOTE_10K);
                returnCrash.add(Note.NOTE_10K);

            }else {
                returnCrash.getInventory()
                                .forEach((key, value) -> System.out.println(String.format("You receive %d %s", value, key)));
                System.out.println("But no current crash available, select a item instead please.");
                break;
            }
        }

        context.setCurrentBalance(currentBalance);
        if(context.getCurrentBalance() == 0)
            returnCrash.getInventory()
                    .forEach((key, value) -> System.out.println(String.format("You receive %d %s", value, key.getName())));
            context.setCurrentState(context.getWaitingForMoneyState());
    }

    @Override
    public Optional<Inventory<Item>> getCart() {
        return Optional.of(context.getCurrentItemInventory());
    }

}
