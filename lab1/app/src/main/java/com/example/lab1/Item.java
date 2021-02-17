package com.example.lab1;

public enum Item {
    METERS(ItemCategories.DISTANCE, 1.0),
    MILES(ItemCategories.DISTANCE, 0.000621371),
    FOOT(ItemCategories.DISTANCE, 3.28084),
    KG(ItemCategories.WEIGHT, 1.0),
    OUNCE(ItemCategories.WEIGHT, 35.274),
    GRAMM(ItemCategories.WEIGHT, 1000.0),
    BYN(ItemCategories.CURRENCY, 1.0),
    USD(ItemCategories.CURRENCY, 0.384),
    EUR(ItemCategories.CURRENCY, 0.3294);
    private ItemCategories category;
    private double coeff;
    Item(ItemCategories category, double coeff){
        this.category = category;
        this.coeff = coeff;
    }

    public ItemCategories getCategory(){
        return category;
    }

    public double getCoefficient(){
        return coeff;
    }
}


