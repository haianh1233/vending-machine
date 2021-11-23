package com.hai.machine;

import com.hai.machine.core.VendingMachine;
import com.hai.machine.core.impl.VendingMachineContext;
import com.hai.machine.model.Inventory;
import com.hai.machine.util.Item;
import com.hai.machine.util.Note;

import java.time.LocalDate;
import java.util.*;

public class App {
    private static final int PROMOTION_STEP = 3;
    private static final int PROMOTION_VALUE = 1;
    private static final int MAX_PROMOTION_COUNT = 50000;
    private static final double ADDITIONAL_PROMOTION = 0.5;
    private static double promotionPercentage = 0.1;
    private static int promotionCount = 0;
    private static LocalDate currentDate = LocalDate.of(2021, 11, 22);

    public static void main(String[] args) {
        List<String> welcomeOption = new ArrayList<>();
        List<String> drinkOption = new ArrayList<>();
        List<String> noteOption = new ArrayList<>();
        VendingMachine vendingMachine = new VendingMachineContext();

        welcomeScreen(welcomeOption);
        selectDrink(drinkOption);
        selectNote(noteOption);

        while (true) {
            printOption(welcomeOption, vendingMachine);
            showCart(vendingMachine);
            System.out.println("Select your option:");
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    insertMoney(vendingMachine, noteOption);
                    break;

                case 2:
                    makeChoice(vendingMachine, drinkOption);
                    break;

                case 3:
                    getPromotion(vendingMachine, promotionCount, currentDate);
                    if(currentDate.isBefore(LocalDate.now()))
                        currentDate = LocalDate.now();
                    break;

                case 4:
                    refund(vendingMachine);

            }
            System.out.println("current count " + promotionCount);
        }
    }

    private static void getPromotion(VendingMachine vendingMachine,int currentCount,LocalDate currentDate) {
        Random random = new Random();
        VendingMachineContext context = (VendingMachineContext) vendingMachine;
        Inventory<Item> promotionItem = new Inventory<>();

        if(currentDate.isBefore(LocalDate.now())) {
            currentDate.with(LocalDate.now());
            if(currentCount <= MAX_PROMOTION_COUNT)
                promotionPercentage += ADDITIONAL_PROMOTION;
            else
                promotionCount = 0;
        }
        context.getCurrentItemInventory().getInventory()
                .forEach((key, value) -> {
                    int number = value - PROMOTION_STEP;
                    while (number >= 0) {
                        double r = random.nextDouble();
                        if(number >= 0
                                && context.getItemInventory().deduct(key, PROMOTION_VALUE)
                                && r <= promotionPercentage
                                && currentCount <= MAX_PROMOTION_COUNT
                        ) {
                            promotionItem.put(key, PROMOTION_VALUE);
                            number = number - PROMOTION_STEP;
                            promotionCount++;
                        }
                    }
                });
        promotionItem.getInventory()
                .forEach((key, value) -> System.out.println(String.format("Congratulation you receive %d %s by our promotion", value, key.getName())));

    }

    private static void insertMoney(VendingMachine vendingMachine, List<String> option) {
        printOption(option, vendingMachine);
        System.out.println("Select your option:");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                vendingMachine.insertMoney(Note.NOTE_200K);
                break;
            case 2:
                vendingMachine.insertMoney(Note.NOTE_100K);
                break;
            case 3:
                vendingMachine.insertMoney(Note.NOTE_50K);
                break;
            case 4:
                vendingMachine.insertMoney(Note.NOTE_20K);
                break;
            case 5:
                vendingMachine.insertMoney(Note.NOTE_10K);
                break;
            default:
                break;
        }
    }

    private static Item makeChoice(VendingMachine vendingMachine, List<String> option) {
        printOption(option, vendingMachine);
        System.out.println("Select your option:");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                return vendingMachine.makeChoice(Item.COKE).orElse(null);
            case 2:
                return vendingMachine.makeChoice(Item.PEPSI).orElse(null);
            case 3:
                return vendingMachine.makeChoice(Item.SODA).orElse(null);
            default:
                return null;
        }
    }

    private static void showCart(VendingMachine vendingMachine) {
        if(vendingMachine.getCart().isPresent()) {
            System.out.println("You have:");
            vendingMachine.getCart().get().getInventory()
                    .forEach((key, value) -> System.out.println(String.format("%s: %d ", key.getName(), value)));
        }
    }

    private static void takeDrink(VendingMachine vendingMachine) {
        vendingMachine.getSelectedItem();
    }

    private static void refund(VendingMachine vendingMachine) {
        vendingMachine.refund();
    }

    private static void status(VendingMachine vendingMachine) {
        long currentBalance = ((VendingMachineContext) vendingMachine).getCurrentBalance();
        System.out.println(String.format("Your current balance is %d", currentBalance));
    }

    private static void printOption(List<String> option, VendingMachine vendingMachine) {
        System.out.println("---------------------------------");
        for(int i = 0 ; i < option.size() ; i ++) {
            System.out.println(String.format("%d. %s", i + 1, option.get(i)));
        }
        status(vendingMachine);
    }

    private static void welcomeScreen(List<String> option) {
        option.add("Insert money.");
        option.add("Select your drink.");
        option.add("Take your selected drink.");
        option.add("Refund.");
    }

    public static void selectNote(List<String> option) {
        Arrays.stream(Note.values())
                        .forEach(t -> option.add(t.getName()));
    }

    public static void selectDrink(List<String> option) {
        Arrays.stream(Item.values())
                        .forEach(t -> option.add(t.getName()));
    }
}
