package com.example.lab708.tcmsystem.threads;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import static com.example.lab708.tcmsystem.AppConstants.IP_PC;
import static com.example.lab708.tcmsystem.AppConstants.PORT_PC;

/**
 * Created by Aurelie on 26/07/2017.
 */

public class MyTask extends AsyncTask<Void, Void, Void> {
    private final ProgressDialog progress;

    public MyTask(ProgressDialog progress) {
        this.progress = progress;
    }

    public void onPreExecute() {
        progress.show();
    }

    public Void doInBackground(Void... unused) {
        LEDClientThread emergencyLed = new LEDClientThread(IP_PC, PORT_PC);
        emergencyLed.start();
        return null;
    }

    public void onPostExecute(Void unused) {
        progress.dismiss();
    }
}
