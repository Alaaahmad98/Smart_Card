package com.example.smartcard.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcard.R;

import com.example.smartcard.helper.CardInfHelper;
import com.example.smartcard.utils.Validation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CardInfoActivity extends AppCompatActivity {

    private EditText edNumberCard, edPriceCard;
    private Button bnCreate;

    private Dialog dialogConfirm;
    private TextView tvCardName, tvCategoryName, tvTypeName, tvConfirm;

    private String strCardName, strCardCategory, strCardType;

    private SharedPreferences preferences;
    private String key;

    private DatabaseReference referenceCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        getSupportActionBar().setTitle("Add Card 4/4");

        edNumberCard = findViewById(R.id.ed_number_card);
        edPriceCard = findViewById(R.id.ed_price);
        bnCreate = findViewById(R.id.button_create);

        preferences = getSharedPreferences("KEYS", MODE_PRIVATE);
        key = preferences.getString("key", null);

        referenceCard = FirebaseDatabase.getInstance().getReference("Card");

        getDataCard();

        bnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validNumberCard() | !validPriceCard()) {
                    return;
                } else {
                    openDialogConfirm();
                }
            }
        });


    }

    private void getDataCard() {
        strCardName = preferences.getString("card_name", null);
        strCardCategory = preferences.getString("card_category", null);
        strCardType = preferences.getString("card_type", null);
    }

    private void openDialogConfirm() {
        dialogConfirm = new Dialog(CardInfoActivity.this);
        dialogConfirm.setContentView(R.layout.dialog_confirm_admin);
        tvCardName = dialogConfirm.findViewById(R.id.tv_card_name);
        tvCategoryName = dialogConfirm.findViewById(R.id.tv_card_category);
        tvTypeName = dialogConfirm.findViewById(R.id.tv_card_type);
        tvConfirm = dialogConfirm.findViewById(R.id.tv_confirm);

        tvCardName.setText(strCardName);
        tvCategoryName.setText(strCardCategory);
        tvTypeName.setText(strCardType);


        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardInfHelper helper = new CardInfHelper(edNumberCard.getText().toString(), edPriceCard.getText().toString().trim());
                referenceCard.child(key).child("cardInfo").setValue(helper);
                dialogConfirm.dismiss();
                Toast.makeText(CardInfoActivity.this, "Operation Done Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CardInfoActivity.this, AdminHomeActivity.class));
                finish();
            }
        });
        dialogConfirm.show();

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
}