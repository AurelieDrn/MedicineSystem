package com.example.lab708.tcmsystem;

import android.os.Message;
import android.util.Log;

import com.example.lab708.tcmsystem.activity.ExecutePickupActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Aurelie on 17/07/2017.
 */

public class ClientThread extends Thread {

    String dstAddress;
    int dstPort;
    private volatile boolean running;
    ExecutePickupActivity.ClientHandler handler;

    Socket socket;
    PrintWriter printWriter;
    BufferedReader bufferedReader;

    private boolean tryToReconnect = true;
    private Thread heartbeatThread = null;
    private long heartbeatDelayMillis = 5000;


    public ClientThread(String addr, int port, ExecutePickupActivity.ClientHandler handler) {
        super();
        dstAddress = addr;
        dstPort = port;
        this.handler = handler;
    }

    public void setRunning(boolean running){
        this.running = running;
    }

    private void sendState(String state){
        handler.sendMessage(
                Message.obtain(handler,
                        ExecutePickupActivity.ClientHandler.UPDATE_STATE, state));
    }

    public void txMsg(String msgToSend){
        if(printWriter != null){
            printWriter.println(msgToSend);
        }
    }

    @Override
    public void run() {
        sendState("connecting...");

        running = true;
        boolean scanning = true;

        while (scanning) {
            try {
                socket = new Socket(dstAddress, dstPort);

                scanning = false;
                sendState("connected");

                OutputStream outputStream = socket.getOutputStream();
                printWriter = new PrintWriter(outputStream, true);

                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader =
                        new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                while(running){
                    /*while(this.socket.getInputStream().read() == -1) {
                        sendState("connecting...");
                        socket = new Socket(dstAddress, dstPort);
                    }
                    sendState("connected");*/

                    //bufferedReader block the code
                    String line = bufferedReader.readLine();
                    if(line != null){
                        handler.sendMessage(
                                Message.obtain(handler,
                                        ExecutePickupActivity.ClientHandler.UPDATE_MSG, line));
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(bufferedReader != null){
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(printWriter != null){
                    printWriter.close();
                }

                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        handler.sendEmptyMessage(ExecutePickupActivity.ClientHandler.UPDATE_END);
    }

    public void reconnect() throws IOException {
        while(this.socket.getInputStream().read() == -1) {
            sendState("connecting...");
            socket = new Socket(dstAddress, dstPort);
        }
    }
}
