package com.example.smartcard.helper;

import com.google.firebase.database.Exclude;

public class AddCardHelper {

    private String nameCard, mImageUri, mKey;

    public AddCardHelper(String nameCard, String mImageUri) {
        this.nameCard = nameCard;
        this.mImageUri = mImageUri;
    }

    public String getNameCard() {
        return nameCard;
    }

    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }

    public String getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }

    @Exclude
    public String getmKey() {
        return mKey;
    }

    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
