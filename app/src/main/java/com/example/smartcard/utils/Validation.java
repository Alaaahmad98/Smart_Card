package com.example.smartcard.utils;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;

import com.example.smartcard.R;

import java.util.regex.Pattern;

public class Validation {
    private static final Pattern PASSWORD_PA = Pattern.compile("^" +
            "(?=.*[0-9])" +
            "(?=.*[a-z])" +
            "(?=.*[A-Z])" +
            ".{8,}");

    public static boolean validName(Context context, EditText EditText) {
        String name = EditText.getText().toString().trim();
        if (name.isEmpty()) {
            EditText.setError(context.getResources().getString(R.string.empty));
            EditText.setFocusable(true);
            return false;
        }
        if (!Pattern.matches("[^,!@#$%^&*()_+?/<>0-9]+$", name.trim())) {
            EditText.setError(context.getResources().getString(R.string.symbols_or_number));
            return false;
        }

        if (name.length() < 2) {
            EditText.setError(context.getResources().getString(R.string.min_name));
            EditText.setFocusable(true);
            return false;
        } else {
            EditText.setError(null);
            return true;
        }
    }

    public static boolean validEmail(Context context, EditText EditText) {
        String email = EditText.getText().toString().trim();
        if (email.isEmpty()) {
            EditText.setError(context.getResources().getString(R.string.empty));
            EditText.setFocusable(true);
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EditText.setError(context.getResources().getString(R.string.valid_email));
            EditText.setFocusable(true);
            return false;
        } else {
            EditText.setError(null);
            return true;
        }
    }

    public static boolean validPassword(Context context, EditText EditText) {
        String pass = EditText.getText().toString();
        if (pass.isEmpty()) {
            EditText.setError(context.getResources().getString(R.string.empty));
            EditText.setFocusable(true);
            return false;
        }
        if (!Pattern.matches("[^,.!@#$%^&*()_+?/<>]+$", pass.trim()) || !PASSWORD_PA.matcher(pass).matches()) {
            EditText.setError(context.getResources().getString(R.string.valid_password));
            return false;
        }
        EditText.setError(null);
        return true;
    }

    public static boolean validConfirmPassword(Context context, EditText textInputOne, EditText textInputTow) {
        String confirmPassword = textInputOne.getText().toString().trim();
        if (confirmPassword.isEmpty()) {
            textInputOne.setError(context.getResources().getString(R.string.empty));
            textInputOne.setFocusable(true);
            return false;
        }
        if (!Pattern.matches("[^,.!@#$%^&*()_+?/<>]+$", confirmPassword.trim()) || !PASSWORD_PA.matcher(confirmPassword).matches()) {
            textInputOne.setError(context.getResources().getString(R.string.valid_password));
            return false;
        }
        if (!confirmPassword.equals(textInputTow.getText().toString())) {
            textInputOne.setError(context.getResources().getString(R.string.confirm_password));
            textInputOne.setFocusable(true);
            return false;
        } else {
            textInputOne.setError(null);
            return true;
        }
    }


    public static boolean validReq(Context context, EditText EditText) {
        String pass = EditText.getText().toString();
        if (pass.isEmpty()) {
            EditText.setError(context.getResources().getString(R.string.empty));
            EditText.setFocusable(true);
            return false;
        }
        EditText.setError(null);
        return true;
    }
}
