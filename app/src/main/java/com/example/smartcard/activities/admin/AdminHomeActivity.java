package com.example.smartcard.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcard.R;
import com.example.smartcard.activities.LoginActivity;
import com.example.smartcard.adapter.AdminHomeAdapter;

import com.example.smartcard.helper.AdminHomeHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private AdminHomeAdapter adapter;
    private List<AdminHomeHelper> list;

    private FloatingActionButton floatingActionButton;
    private TextView tvNotFound;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        getSupportActionBar().setTitle("Home");

        initiateView();


    }


    private void initiateView() {
        recyclerView = findViewById(R.id.recycler_all_card);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        list = new ArrayList<>();

        floatingActionButton = findViewById(R.id.fab);
        tvNotFound = findViewById(R.id.tv_not_found);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, AddCardActivity.class));
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Card");
    }

    private void fullRecyclerView() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    String name = postSnapShot.getKey();
                    String imageView = postSnapShot.child("picCard").getValue().toString();

                    list.add(new AdminHomeHelper(name, imageView));
                }
                adapter = new AdminHomeAdapter(AdminHomeActivity.this, list);
                recyclerView.setAdapter(adapter);

                if (list.isEmpty()) {
                    tvNotFound.setVisibility(View.VISIBLE);
                } else {
                    tvNotFound.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminHomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage("Are you sure you went to exist Smart Card")
                .setCancelable(false)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        dialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        list.clear();
        fullRecyclerView();
    }
}