package com.example.smartcard.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcard.R;
import com.example.smartcard.activities.admin.AddCardActivity;
import com.example.smartcard.activities.admin.AddCategoryActivity;
import com.example.smartcard.activities.admin.AdminHomeActivity;
import com.example.smartcard.adapter.AdminHomeAdapter;
import com.example.smartcard.helper.AdminHomeHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminHomeAdapter adapter;
    private List<AdminHomeHelper> list;

    private FloatingActionButton floatingActionButton;
    private TextView tvNotFound;

    private DatabaseReference reference;

    private Intent intent;
    private String nameParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        getSupportActionBar().setTitle("Category");

        initiateView();
        fullRecyclerView();
    }

    private void initiateView() {

        intent = getIntent();
        nameParent = intent.getStringExtra("NAME_CARD");
        recyclerView = findViewById(R.id.recycler_all_category);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        list = new ArrayList<>();

        floatingActionButton = findViewById(R.id.fab);
        tvNotFound = findViewById(R.id.tv_not_found);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, AddCategoryActivity.class);
                intent.putExtra("NAME_CARD", nameParent);
                startActivity(intent);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Card/" + nameParent);
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
                            if (!name.equals("picCard")) {
                                String imageView = snapshot.child("picCard").getValue().toString();
                                list.add(new AdminHomeHelper(name, imageView));
                            }
                        }
                        adapter = new AdminHomeAdapter(CategoryActivity.this, list);
                        recyclerView.setAdapter(adapter);
                        if (list.isEmpty()) {
                            tvNotFound.setVisibility(View.VISIBLE);
                        } else {
                            tvNotFound.setVisibility(View.GONE);
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
        startActivity(new Intent(CategoryActivity.this, AdminHomeActivity.class));
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CategoryActivity.this, AdminHomeActivity.class));
        finish();
    }
}