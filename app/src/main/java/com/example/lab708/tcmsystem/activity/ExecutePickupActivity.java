package com.example.lab708.tcmsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lab708.tcmsystem.R;
import com.example.lab708.tcmsystem.dao.DAOFactory;
import com.example.lab708.tcmsystem.dao.PileDAO;
import com.example.lab708.tcmsystem.dao.RequirementDAO;
import com.example.lab708.tcmsystem.model.Pickup;
import com.example.lab708.tcmsystem.threads.ClientThread;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.lab708.tcmsystem.AppConstants.IP;
import static com.example.lab708.tcmsystem.AppConstants.IP2;
import static com.example.lab708.tcmsystem.AppConstants.IP3;
import static com.example.lab708.tcmsystem.AppConstants.PORT;

public class ExecutePickupActivity extends AppCompatActivity {

    private ClientHandler clientHandler;
    private ClientHandler clientHandler2;
    private ClientHandler clientHandler3;

    private ClientThread clientThread;
    private ClientThread clientThread2;
    private ClientThread clientThread3;
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

        // Button to execute the requirement
        goPickUp = (Button) findViewById(R.id.execute_pu_go);
        // Text view to show if the app is connected to the RPI
        state = (TextView) findViewById(R.id.state);

        // Get the data
        Bundle data = getIntent().getExtras();
        pickupList = (ArrayList) data.getParcelableArrayList("pickupList");
        id = getIntent().getExtras().getInt("id");  // requirement id

        /*
        Sort the pickup list
        In the Pickup class, we have implemented the compareTo method.
        So the pickups are sorted according to the locations.
        */
        Collections.sort(pickupList);

        initializeThreads();

        goPickUp.setOnClickListener(buttonSendOnClickListener);
    }

    View.OnClickListener buttonSendOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_execute_pickup);

            send(clientThreads.get(clientThreadIndex));
        }
    };

    /**
     * Set the index of the thread in the thread list.
     * It reads the first pickup in the pickup list and get the first number of the location.
     * The location is written following this format : n-n-n where n is a number.
     * The whole location is a String value.
     */
    private void setClientThreadIndex() {
        String location = pickupList.get(0).getLocation();
        char firstNumber = location.charAt(0);
        this.clientThreadIndex = Integer.parseInt(String.valueOf(firstNumber))-1;
    }

    /**
     * Send message to a client thread
     * @param c
     */
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

    /**
     * Receives a message from the RPI and moves on to the next medicine to pick up or end the function.
     * @param rxmsg The message it receives from a RPI.
     */
    private void updateRxMsg(String rxmsg) {
        // There are more medicines to pick up to complete the requirement.
        if(!pickupList.isEmpty()) {
            PileDAO pileDAO = DAOFactory.getPileDAO();
            try {
                /*
                Execute the pickup.
                rxmsg is the quantity the pharmacist decided to pick up.
                 */
                pileDAO.executePickUp(pickupList.get(0), Integer.valueOf(rxmsg));
                pickupList.remove(0);

                /*
                Check of the pickup list is empty again (maybe it was the last one).
                If it is empty, we finished completing the requirement.
                */
                if(pickupList.isEmpty()) {
                    RequirementDAO requirementDAO = DAOFactory.getRequirementDAO();
                    try {
                        requirementDAO.deleteRequirement(id);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // Something strange, check the Java program on the RPI to understand.
                    for(ClientThread c : clientThreads) {
                        if(!c.equals(this.clientThreads.get(this.clientThreadIndex))) {
                            c.txMsg("RESTART");
                        }
                    }
                    this.clientThreads.get(this.clientThreadIndex).txMsg("END");

                    Intent intent = new Intent(ExecutePickupActivity.this, CheckPickupActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    setClientThreadIndex();

                    for(ClientThread c : clientThreads) {
                        if(c.equals(this.clientThreads.get(clientThreadIndex))) {
                            c.txMsg("RESTART");
                        }
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

    /**
     * Initialize all the threads to communicate with the RPI.
     * You may try to use only one clientHandler.
     * We need one client thread per RPI.
     */
    private void initializeThreads() {
        // Set the client thread index to know which thread we need to send a message
        setClientThreadIndex();

        // Initialization of the client handlers
        clientHandler = new ClientHandler(this);
        clientHandler2 = new ClientHandler(this);
        clientHandler3 = new ClientHandler(this);

        // Initialization of the client threads
        clientThread = new ClientThread(IP, PORT, clientHandler);
        clientThread2 = new ClientThread(IP2, PORT, clientHandler2);
        clientThread3 = new ClientThread(IP3, PORT, clientHandler3);

        // Add the client threads to the client thread list
        clientThreads.add(clientThread);
        clientThreads.add(clientThread2);
        clientThreads.add(clientThread3);

        // Start all the threads
        for(ClientThread c : clientThreads) {
            c.start();
        }
    }

    // From there I did not change anything. It is the code from Internet.


    private void clientEnd(){}

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

}