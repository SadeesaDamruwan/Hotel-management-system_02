package com.hotel.management.model;

public class FinancialRecord {
    private final String date;
    private final String type;
    private final String category;
    private final double amount;
    private final String description;


    public FinancialRecord(final String date, final String type, final String category, final double amount, final String description) {
        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    // Getters
    public String getDate() { return date; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
}
