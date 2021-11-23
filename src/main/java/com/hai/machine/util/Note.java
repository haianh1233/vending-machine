package com.hai.machine.util;

public enum Note {
    NOTE_200K("200.000 VNĐ", 200000),
    NOTE_100K("100.000 VNĐ", 100000),
    NOTE_50K("50.000 VNĐ", 50000),
    NOTE_20K("20.000 VNĐ", 20000),
    NOTE_10K("10.000 VNĐ", 10000);

    private String name;
    private int value;

    Note(String name, int price){
        this.name = name;
        this.value = price;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

}
