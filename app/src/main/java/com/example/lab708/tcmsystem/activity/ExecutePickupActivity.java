package com.example.lab708.tcmsystem.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.lab708.tcmsystem.ClientThread;
import com.example.lab708.tcmsystem.classe.Pickup;
import com.example.lab708.tcmsystem.R;
import com.example.lab708.tcmsystem.dao.DAOFactory;
import com.example.lab708.tcmsystem.dao.PileDAO;
import com.example.lab708.tcmsystem.dao.RequirementDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import static com.example.lab708.tcmsystem.AppConstants.IP;
import static com.example.lab708.tcmsystem.AppConstants.PORT;

public class ExecutePickupActivity extends AppCompatActivity {

    private ClientHandler clientHandler;
    private ClientThread clientThread;
    private Button goPickUp;

    private ArrayList<Pickup> pickupList;
    private int id; // requirement id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_pickup);

        goPickUp = (Button) findViewById(R.id.execute_pu_go);

        Bundle data = getIntent().getExtras();
        pickupList = (ArrayList) data.getParcelableArrayList("pickupList");
        id = getIntent().getExtras().getInt("id");

        Collections.sort(pickupList);
        Log.d("pickupList", pickupList.toString());

        clientHandler = new ClientHandler(this);

        // connect
        clientThread = new ClientThread(IP, PORT, clientHandler);
        clientThread.start();

        goPickUp.setOnClickListener(buttonSendOnClickListener);
    }

    View.OnClickListener buttonSendOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(clientThread != null){
                sendRequirement();
            }
        }
    };

    private void sendRequirement() {
        clientThread.txMsg(pickupList.get(0).getSerialNumber());
        clientThread.txMsg(pickupList.get(0).getMedicineName());
        clientThread.txMsg(pickupList.get(0).getLocation());
        clientThread.txMsg(pickupList.get(0).getQuantityStock()+"");
        clientThread.txMsg(pickupList.get(0).getQuantityToPick()+"");
    }

    private void updateState(String state){

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
                    clientThread.txMsg("END");

                    Intent intent = new Intent(ExecutePickupActivity.this, CheckPickupActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    sendRequirement();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private void clientEnd(){
        clientThread = null;

        // buttonConnect.setEnabled(true);
        //buttonDisconnect.setEnabled(false);
        clientThread = new ClientThread(IP, PORT, clientHandler);
        clientThread.start();

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ExecutePickupActivity.this, CheckPickupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
