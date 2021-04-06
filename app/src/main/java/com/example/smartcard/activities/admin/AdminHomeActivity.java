package com.example.smartcard.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.smartcard.R;
import com.example.smartcard.activities.LoginActivity;
import com.example.smartcard.adapter.AdminHomeAdapter;

import com.example.smartcard.helper.AdminHomeHelper;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private AdminHomeAdapter adapter;
    private List<AdminHomeHelper> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        getSupportActionBar().setTitle("Home");

        initiateView();
        fullRecyclerView();
        implementInterFace();

    }


    private void initiateView() {
        recyclerView = findViewById(R.id.recycler_add_card);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
    }

    private void fullRecyclerView() {

        list.add(new AdminHomeHelper("Add Card", R.drawable.ic_add));

        adapter = new AdminHomeAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    private void implementInterFace() {
        adapter.setOnItemClickListener(new AdminHomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String name = list.get(position).getName();
                switch (name) {
                    case "Add Card":
                        startActivity(new Intent(AdminHomeActivity.this, AddCardActivity.class));
                        break;
                }
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
}