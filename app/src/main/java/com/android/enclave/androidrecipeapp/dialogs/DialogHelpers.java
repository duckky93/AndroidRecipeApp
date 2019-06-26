package com.android.enclave.androidrecipeapp.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class DialogHelpers {

    public static void showAlertDialog(Context context, String title, String message,
                                       DialogInterface.OnClickListener positive,
                                       DialogInterface.OnClickListener negative){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", positive);
        builder.setNegativeButton("Cancel", negative);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
