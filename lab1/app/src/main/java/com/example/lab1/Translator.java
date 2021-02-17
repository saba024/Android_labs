package com.example.lab1;

public class Translator {
    static public String convert(String data, Item first_field, Item second_filed){
        if(first_field.getCategory() == second_filed.getCategory() && data.equals("") == false){
            return String.valueOf(( second_filed.getCoefficient()/first_field.getCoefficient() ) * Double.parseDouble(data));
        }
        return "";
    }
}