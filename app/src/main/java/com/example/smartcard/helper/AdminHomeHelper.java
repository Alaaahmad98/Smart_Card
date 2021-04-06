package com.example.smartcard.helper;

public class AdminHomeHelper {

    private String name;
    private int imageView;

    public AdminHomeHelper(String name, int imageView) {
        this.name = name;
        this.imageView = imageView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }
}
