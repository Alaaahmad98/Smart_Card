package com.example.smartcard.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.smartcard.R;

public class Dialogs {

    public static android.app.Dialog errorDialog;
    public static TextView tvErrorResponse,tvClose;


    public static void dialogError(Context context, String error) {
        errorDialog = new android.app.Dialog(context);
        errorDialog.setContentView(R.layout.dialog_error);
        tvErrorResponse = errorDialog.findViewById(R.id.tv_error_response);
        tvErrorResponse.setText(error);
        tvClose = errorDialog.findViewById(R.id.tv_close);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
            }
        });
        errorDialog.show();
    }
}
