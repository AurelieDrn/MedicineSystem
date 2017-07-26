package com.example.lab708.tcmsystem.threads;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Aurelie on 25/07/2017.
 */

public class LEDClientThread extends Thread {

    private Socket socket;

    public LEDClientThread(String ip, int port) {
        try {
            socket = new Socket(ip, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if(socket != null) {
            PrintStream writer = null;
            try {
                writer = new PrintStream(socket.getOutputStream());
                //BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                writer.println("1");
                writer.flush();

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
