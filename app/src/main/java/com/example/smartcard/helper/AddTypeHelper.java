package com.example.smartcard.helper;



public class AddTypeHelper {

    private String  picCard,price;

    public AddTypeHelper(String picCard, String price) {

        this.picCard = picCard;
        this.price=price;
    }

    public String getPicCard() {
        return picCard;
    }

    public void setPicCard(String picCard) {
        this.picCard = picCard;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
