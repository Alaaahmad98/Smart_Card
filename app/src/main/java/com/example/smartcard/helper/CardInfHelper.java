package com.example.smartcard.helper;

public class CardInfHelper {

    private String number, price;

    public CardInfHelper(String number, String price) {
        this.number = number;
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
