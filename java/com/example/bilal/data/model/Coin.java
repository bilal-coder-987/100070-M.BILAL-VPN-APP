package com.example.bilal.data.model;

public class Coin {
    private String name;
    private String symbol;
    private String price;
    private String change;
    private boolean isPositive;

    public Coin(String name, String symbol, String price, String change, boolean isPositive) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.change = change;
        this.isPositive = isPositive;
    }

    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public String getPrice() { return price; }
    public String getChange() { return change; }
    public boolean isPositive() { return isPositive; }
}
