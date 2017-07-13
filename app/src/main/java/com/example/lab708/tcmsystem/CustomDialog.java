package com.example.lab708.tcmsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;

/**
 * Created by Aurelie on 12/07/2017.
 */

public class CustomDialog {

    public static void showSuccessMessage(final Context context, String title, String message, final Intent intent) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setCancelable(false);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void showErrorMessage(final Context context, String title, String message, final Intent intent) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setCancelable(false);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void showSimpleErrorMessage(final Context context, String title, String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setCancelable(false);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void executeShelvesSuccessDialog(final Context context, String title, String message, final Intent intent1, final Intent intent2) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setCancelable(false);
        builder.setTitle("上架作業")
                .setMessage("上架成功!")
                .setPositiveButton(R.string.back_home, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent1);
                    }
                })
                .setNegativeButton(R.string.keep_scanning, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent2);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}
