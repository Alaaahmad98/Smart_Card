package com.example.smartcard.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.example.smartcard.R;


import com.example.smartcard.utils.Validation;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class CardInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edNumberCard;
    private Button bnCreate;
    private DatabaseReference reference;


    private SharedPreferences preferences;
    private String root, parent;

    private Intent intent;
    private String child;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        getSupportActionBar().setTitle("Add Card 4/4");

        initiateView();
        clickView();


    }


    private void clickView() {
        bnCreate.setOnClickListener(this);
    }


    private void initiateView() {
        preferences = getSharedPreferences("NAME_ROOT", Context.MODE_PRIVATE);
        root = preferences.getString("ROOT", null);
        parent = preferences.getString("PARENT", null);

        edNumberCard = findViewById(R.id.ed_number_card);

        bnCreate = findViewById(R.id.button_create);

        intent = getIntent();
        child = intent.getStringExtra("NAME_CARD");


        reference = FirebaseDatabase.getInstance().getReference("Card/" + root + "/" + parent + "/" + child);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_create:
                if (!validNumberCard()) {
                    return;
                } else {
                    addCardInfo();
                }
                break;
        }
    }


    private void addCardInfo() {


        reference.child("CardNumber").push().setValue(edNumberCard.getText().toString());

        Toast.makeText(CardInfoActivity.this, "Operation Done Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(CardInfoActivity.this, AdminHomeActivity.class));
        finish();

    }


    public boolean validNumberCard() {
        return Validation.validReq(this, edNumberCard);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}