package com.example.smartcard.helper;



public class AddCardHelper {

    private String  picCard;

    public AddCardHelper( String picCard) {

        this.picCard = picCard;
    }


    public String getPicCard() {
        return picCard;
    }

    public void setPicCard(String picCard) {
        this.picCard = picCard;
    }
}
