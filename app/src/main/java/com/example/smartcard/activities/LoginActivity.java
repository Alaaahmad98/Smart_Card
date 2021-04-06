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

import com.example.smartcard.R;
import com.example.smartcard.activities.admin.AdminHomeActivity;
import com.example.smartcard.activities.user.UserHomeActivity;
import com.example.smartcard.utils.Dialogs;
import com.example.smartcard.utils.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edEmail, edPassword;
    private TextView tvSignUp;
    private Button bnSignIn;
    private ProgressBar progressBar;
    private Intent intent;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        initiateView();
        clickView();

    }

    //declare all component
    private void initiateView() {
        edEmail = findViewById(R.id.ed_email);
        edPassword = findViewById(R.id.ed_password);
        bnSignIn = findViewById(R.id.button_sign_in);
        progressBar = findViewById(R.id.progressBar);
        tvSignUp = findViewById(R.id.tv_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    //click listener
    private void clickView() {
        tvSignUp.setOnClickListener(this);
        bnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_in:
                if (!validEmail() | !validPassword()) {
                    return;
                } else {
                    closeKeyboard(this);
                    signIn();
                }
                break;
            case R.id.tv_sign_up:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    // sign in
    private void signIn() {
        bnSignIn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        final String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();


        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    bnSignIn.setVisibility(View.VISIBLE);

                    updateUI(email);

                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    bnSignIn.setVisibility(View.VISIBLE);

                    String message = task.getException().getMessage();
                    Dialogs.dialogError(LoginActivity.this, message);
                }
            }
        });
    }

    // move to home activity.
    private void updateUI(String email) {
        if (email.equals("admin@mail.com")) {
            intent = new Intent(this, AdminHomeActivity.class);
        } else {
            intent = new Intent(this, UserHomeActivity.class);
        }
        startActivity(intent);
        finish();
    }

    //validation
    public boolean validEmail() {
        return Validation.validEmail(this, edEmail);
    }

    public boolean validPassword() {
        return Validation.validReq(this, edPassword);
    }

    public void closeKeyboard(Context context) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}