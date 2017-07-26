package com.example.lab708.tcmsystem.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lab708.tcmsystem.threads.ClientThread;
import com.example.lab708.tcmsystem.R;
import com.example.lab708.tcmsystem.model.Pickup;
import com.example.lab708.tcmsystem.dao.DAOFactory;
import com.example.lab708.tcmsystem.dao.PileDAO;
import com.example.lab708.tcmsystem.dao.RequirementDAO;
import com.example.lab708.tcmsystem.threads.LEDClientThread;
import com.example.lab708.tcmsystem.threads.MyTask;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.lab708.tcmsystem.AppConstants.IP;
import static com.example.lab708.tcmsystem.AppConstants.IP2;
import static com.example.lab708.tcmsystem.AppConstants.PORT;

public class ExecutePickupActivity extends AppCompatActivity {

    private ClientHandler clientHandler;
    private ClientThread clientThread;
    private ClientThread clientThread2;
    private Button goPickUp;
    private TextView state;
    private List<ClientThread> clientThreads;

    private ArrayList<Pickup> pickupList;
    private int id; // requirement id
    private int clientThreadIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_pickup);

        clientThreads = new ArrayList<>();

        goPickUp = (Button) findViewById(R.id.execute_pu_go);
        // next = (Button) findViewById(R.id.execute_pu_next);
        // reset = (Button) findViewById(R.id.reset);
        state = (TextView) findViewById(R.id.state);


        Bundle data = getIntent().getExtras();
        pickupList = (ArrayList) data.getParcelableArrayList("pickupList");
        id = getIntent().getExtras().getInt("id");
        Collections.sort(pickupList);

        setClientThreadIndex();

        clientHandler = new ClientHandler(this);


        // connect
        clientThread = new ClientThread(IP, PORT, clientHandler);
        //clientThread.start();

        clientThread2 = new ClientThread(IP2, PORT, clientHandler);
        //clientThread2.start();

        clientThreads.add(clientThread);
        clientThreads.add(clientThread2);

        for(ClientThread c : clientThreads) {
            c.start();
        }


        goPickUp.setOnClickListener(buttonSendOnClickListener);
        //reset.setOnClickListener(buttonDisConnectOnClickListener);
    }

    View.OnClickListener buttonDisConnectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("client", clientThread.toString());
            if(clientThread != null){
                clientThread.setRunning(false);

            }
        }
    };

    View.OnClickListener buttonSendOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView stateTxt = (TextView) findViewById(R.id.state);
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_execute_pickup);
            Button button = (Button) layout.findViewById(R.id.execute_pu_go);
            /*
            if(stateTxt.getText().toString().equals("connected")) {
                layout.removeView(button);
                TextView textView =  (TextView) findViewById(R.id.execute_pu_txt);
                textView.setVisibility(View.VISIBLE);
            }
            else {
                button.setEnabled(false);
                button.setText(R.string.server_not_started);
            }*/
            //sendRequirement();
            send(clientThreads.get(clientThreadIndex));
        }
    };

    private void setClientThreadIndex() {
        String location = pickupList.get(0).getLocation();
        char firstNumber = location.charAt(0);
        this.clientThreadIndex = Integer.parseInt(String.valueOf(firstNumber))-1;
        Log.d("INDEX", String.valueOf(clientThreadIndex));
    }

    private void sendRequirement() {
        if(clientThread != null) {
            clientThread.txMsg(pickupList.get(0).getSerialNumber());
            clientThread.txMsg(pickupList.get(0).getMedicineName());
            clientThread.txMsg(pickupList.get(0).getLocation());
            clientThread.txMsg(pickupList.get(0).getQuantityStock() + "");
            clientThread.txMsg(pickupList.get(0).getQuantityToPick() + "");
        }
    }

    private void sendRequirement2() {
        if(clientThread2 != null) {
            clientThread2.txMsg(pickupList.get(0).getSerialNumber());
            clientThread2.txMsg(pickupList.get(0).getMedicineName());
            clientThread2.txMsg(pickupList.get(0).getLocation());
            clientThread2.txMsg(pickupList.get(0).getQuantityStock() + "");
            clientThread2.txMsg(pickupList.get(0).getQuantityToPick() + "");
        }
    }

    private void send(ClientThread c) {
        if(c != null) {
            c.txMsg(pickupList.get(0).getSerialNumber());
            c.txMsg(pickupList.get(0).getMedicineName());
            c.txMsg(pickupList.get(0).getLocation());
            c.txMsg(pickupList.get(0).getQuantityStock() + "");
            c.txMsg(pickupList.get(0).getQuantityToPick() + "");
        }
    }

    private void updateState(String state){
        this.state.setText(state);
    }

    private void updateRxMsg(String rxmsg){
        if(!pickupList.isEmpty()) {
            PileDAO pileDAO = DAOFactory.getPileDAO();
            try {
                pileDAO.executePickUp(pickupList.get(0), Integer.valueOf(rxmsg));
                pickupList.remove(0);

                if(pickupList.isEmpty()) {
                    RequirementDAO requirementDAO = DAOFactory.getRequirementDAO();
                    try {
                        requirementDAO.deleteRequirement(id);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    //clientThread.txMsg("END");
                    for(ClientThread c : clientThreads) {
                        if(!c.equals(this.clientThreads.get(this.clientThreadIndex))) {
                            c.txMsg("RESTART");
                        }
                    }
                    this.clientThreads.get(this.clientThreadIndex).txMsg("END");

                    /*if(clientThread != null){
                        clientThread.setRunning(false);
                    }*/
                    //this.clientThreads = new ArrayList<>();

                    Intent intent = new Intent(ExecutePickupActivity.this, CheckPickupActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else {
                    //sendRequirement2();
                    setClientThreadIndex();
                    //clientThread.txMsg("CLEAR");
                    //this.clientThreads.get(clientThreadIndex).txMsg("RESTART");
                    for(ClientThread c : clientThreads) {
                        c.txMsg("RESTART");
                    }
                    send(this.clientThreads.get(clientThreadIndex));

                    for(ClientThread c : clientThreads) {
                        if(!c.equals(this.clientThreads.get(clientThreadIndex))) {
                            c.txMsg("CLEAR");
                        }
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private void clientEnd(){
        //clientThread = null;
        // buttonConnect.setEnabled(true);
        //buttonDisconnect.setEnabled(false);
        //clientThread = new ClientThread(IP, PORT, clientHandler);
        //clientThread.start();

        //buttonSend.setEnabled(false);

    }

    public static class ClientHandler extends Handler {
        public static final int UPDATE_STATE = 0;
        public static final int UPDATE_MSG = 1;
        public static final int UPDATE_END = 2;
        private ExecutePickupActivity parent;

        public ClientHandler(ExecutePickupActivity parent) {
            super();
            this.parent = parent;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_STATE:
                    parent.updateState((String)msg.obj);
                    break;
                case UPDATE_MSG:
                    parent.updateRxMsg((String)msg.obj);
                    break;
                case UPDATE_END:
                    parent.clientEnd();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    // TRY
    /*
    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(ClientThread c : clientThreads) {
            c.interrupt();
        }
    }*/
}