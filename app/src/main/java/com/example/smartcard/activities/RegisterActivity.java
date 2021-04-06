package com.example.smartcard.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcard.R;
import com.example.smartcard.helper.RegisterHelper;
import com.example.smartcard.utils.Dialogs;
import com.example.smartcard.utils.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edFirstName, edLastName, edEmail, edPassword, edConPassword;
    private Button bnSignUp;
    private ProgressBar progressBar;
    private TextView tvSignIn;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    List<String> listEmail = new ArrayList<>();

    private RegisterHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initiateView();
        clickView();
        getAllEmailInDataBase();
    }


    //declare all component
    private void initiateView() {
        edFirstName = findViewById(R.id.ed_first_name);
        edLastName = findViewById(R.id.ed_last_name);
        edEmail = findViewById(R.id.ed_email);
        edPassword = findViewById(R.id.ed_password);
        edConPassword = findViewById(R.id.ed_confirm_password);
        bnSignUp = findViewById(R.id.button_sign_up);
        progressBar = findViewById(R.id.progressBar);
        tvSignIn = findViewById(R.id.tv_sign_in);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    //click listener
    private void clickView() {
        bnSignUp.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
    }

    private void getAllEmailInDataBase() {

        firebaseDatabase.getReference("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    listEmail.add(child.child("email").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_up:
                if (!validateName(edFirstName) | !validateName(edLastName) | !validateEmail() | !validatePassword() | !validateConfirmPassword()) {
                    return;
                } else {
                    if (checkEmail()) {
                        closeKeyboard(this);
                        createAccount();
                    } else {
                        closeKeyboard(this);
                        Dialogs.dialogError(this, "Email already found.");
                    }
                }
                break;
            case R.id.tv_sign_in:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    boolean checkEmail() {
        System.out.println(listEmail.size());
        String email = edEmail.getText().toString().trim();
        for (int i = 0; i < listEmail.size(); i++) {
            if (email.equals(listEmail.get(i))) {
                return false;
            } else {
                continue;
            }
        }
        return true;
    }

    //create account
    private void createAccount() {

        bnSignUp.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        // get all value
        String firstName = edFirstName.getText().toString().trim();
        String lastName = edLastName.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String confirmPassword = edConPassword.getText().toString().trim();

        helper = new RegisterHelper(firstName, lastName, email, password, confirmPassword);


        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String user_id = firebaseAuth.getCurrentUser().getUid();

                    DatabaseReference current_user_dp = reference.child(user_id);
                    current_user_dp.setValue(helper);

                    progressBar.setVisibility(View.INVISIBLE);
                    bnSignUp.setVisibility(View.VISIBLE);

                    Toast.makeText(RegisterActivity.this,"Operation Done Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    bnSignUp.setVisibility(View.VISIBLE);

                    String message = task.getException().getMessage();
                    Dialogs.dialogError(RegisterActivity.this, message);
                }
            }
        });
    }


    //validation filed
    private boolean validateName(EditText editText) {
        return Validation.validName(this, editText);
    }

    private boolean validateEmail() {
        return Validation.validEmail(this, edEmail);
    }

    private boolean validatePassword() {
        return Validation.validPassword(this, edPassword);
    }

    private boolean validateConfirmPassword() {
        return Validation.validConfirmPassword(this, edConPassword, edPassword);
    }

    public void closeKeyboard(Context context) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}