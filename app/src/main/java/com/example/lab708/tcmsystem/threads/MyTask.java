package com.example.lab708.tcmsystem.threads;

import android.app.ProgressDialog;
import android.os.AsyncTask;

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
        LEDClientThread emergencyLed = new LEDClientThread("192.168.0.5", 9090);
        emergencyLed.start();
        return null;
    }

    public void onPostExecute(Void unused) {
        progress.dismiss();
    }
}
