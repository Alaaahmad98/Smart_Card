package com.example.smartcard.activities.admin;

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

import com.example.smartcard.helper.CardInfHelper;
import com.example.smartcard.utils.Validation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class CardInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edNumberCard, edPriceCard;
    private Button bnCreate;
    private DatabaseReference referenceCard, reference;


    private SharedPreferences preferences;
    private String root, parent;

    private Intent intent;
    private String child;

    private boolean flag = false;

    private String number, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        getSupportActionBar().setTitle("Add Card 4/4");

        initiateView();
        clickView();
        checkIsFullInfo();


    }


    private void clickView() {
        bnCreate.setOnClickListener(this);
    }


    private void initiateView() {
        preferences = getSharedPreferences("NAME_ROOT", Context.MODE_PRIVATE);
        root = preferences.getString("ROOT", null);
        parent = preferences.getString("PARENT", null);

        edNumberCard = findViewById(R.id.ed_number_card);
        edPriceCard = findViewById(R.id.ed_price);
        bnCreate = findViewById(R.id.button_create);

        intent = getIntent();
        child = intent.getStringExtra("NAME_CARD");

        referenceCard = FirebaseDatabase.getInstance().getReference("Card");
        reference = FirebaseDatabase.getInstance().getReference("Card/" + root + "/" + parent + "/" + child);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_create:
                if (flag) {
                    Toast.makeText(this, "The information has been pre-filled", Toast.LENGTH_SHORT).show();
                } else {
                    if (!validNumberCard() | !validPriceCard()) {
                        return;
                    } else {
                        addCardInfo();
                    }
                }
                break;
        }
    }


    private void checkIsFullInfo() {
        final Query userQuery = reference;

        userQuery.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String name = snapshot.getKey();
                            System.out.println("name " + name);

                            if (dataSnapshot.hasChild("CardInformation")) {
                                flag = true;
                                if (name.equals("CardInformation")) {
                                    number = snapshot.child("number").getValue().toString();
                                    price = snapshot.child("price").getValue().toString();
                                }
                            }


                        }
                        viewComponentPassedInFlag();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void viewComponentPassedInFlag() {
        if (flag) {
            edNumberCard.setText("Number Card :" + number);
            edPriceCard.setText("Price Card :" + price + " JD");
        }
    }

    private void addCardInfo() {
        CardInfHelper helper = new CardInfHelper(edNumberCard.getText().toString(), edPriceCard.getText().toString().trim());

        referenceCard.child(root).child(parent).child(child).child("CardInformation").setValue(helper);

        Toast.makeText(CardInfoActivity.this, "Operation Done Successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(CardInfoActivity.this, AdminHomeActivity.class));
        finish();
    }


    public boolean validNumberCard() {
        return Validation.validReq(this, edNumberCard);
    }

    public boolean validPriceCard() {
        return Validation.validReq(this, edPriceCard);
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