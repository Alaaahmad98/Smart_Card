package com.example.smartcard.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.smartcard.R;
import com.example.smartcard.activities.admin.AddTypeCategoryActivity;
import com.example.smartcard.activities.admin.CardInfoActivity;
import com.example.smartcard.adapter.AdminHomeAdapter;
import com.example.smartcard.adapter.NumberCardAdapter;
import com.example.smartcard.helper.AdminHomeHelper;
import com.example.smartcard.helper.NumberCardHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CardNumberActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NumberCardAdapter adapter;
    private List<NumberCardHelper> list;

    private FloatingActionButton floatingActionButton;
    private TextView tvNotFound;

    private DatabaseReference reference;

    private Intent intent;
    private String child;

    private SharedPreferences preferences;
    private String root, parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_number);

        getSupportActionBar().setTitle("Number Card");

        preferences = getSharedPreferences("NAME_ROOT", Context.MODE_PRIVATE);
        root = preferences.getString("ROOT", null);
        parent = preferences.getString("PARENT", null);

        initiateView();
        fullRecyclerView();
    }

    private void initiateView() {

        intent = getIntent();
        child = intent.getStringExtra("NAME_CARD");
        recyclerView = findViewById(R.id.recycler_all_number_Card);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        floatingActionButton = findViewById(R.id.fab);
        tvNotFound = findViewById(R.id.tv_not_found);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CardNumberActivity.this, CardInfoActivity.class);
                intent.putExtra("NAME_CARD", child);
                startActivity(intent);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Card/" + root + "/" + parent + "/" + child);
    }

    private void fullRecyclerView() {
        final Query userQuery = reference;

        userQuery.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String name = snapshot.getKey();
                            System.out.println("name " + name);
                            if (name.equals("CardNumber")) {

                                reference.child(name).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot child : snapshot.getChildren()) {
                                            String numberCard = child.getValue().toString();
                                            list.add(new NumberCardHelper(numberCard));
                                        }
                                        adapter = new NumberCardAdapter(CardNumberActivity.this, list);
                                        recyclerView.setAdapter(adapter);
                                        if (list.isEmpty()) {
                                            tvNotFound.setVisibility(View.VISIBLE);
                                        } else {
                                            tvNotFound.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
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