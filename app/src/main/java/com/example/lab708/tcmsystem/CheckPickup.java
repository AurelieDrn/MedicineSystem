package com.example.lab708.tcmsystem;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lab708.tcmsystem.activity.HomeActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class CheckPickup extends Activity {
    requireList adapter;
    ListView list;
    ArrayList<Integer> reqNum = new ArrayList<Integer>();
    ArrayList<String> reqSta = new ArrayList<String>();
    ArrayList<String> reqPro = new ArrayList<String>();
    static final String username="TCMSystem";
    static final String password="TCMSystem";
    final String URL="jdbc:mariadb://120.105.161.89/TCMSystem";
    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpickup);

        //Bundle getacc = this.getIntent().getExtras();
        //final String acc = getacc.getString("staffacc");
        final String acc = "staff";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try{Class.forName("org.mariadb.jdbc.Driver").newInstance();
            try{
                    conn = DriverManager.getConnection(URL,username,password);
                    Toast.makeText(CheckPickup.this, "已連線!", Toast.LENGTH_SHORT).show();//to show a toase if coonection to Database is okay
                    String qry = "SELECT * FROM Pickup WHERE pic_pro = 0" ;
                    stmt = conn.createStatement();
                    int re = 0;//the number of pieces of data that get back from database
                    rs = stmt.executeQuery(qry);
                    while(rs.next()){//add each pickup requirements to the arraylist
                        reqNum.add(re,rs.getInt("pic_num"));//arraylist for serial number of pickup requirement
                        reqSta.add(re,rs.getString("pic_sta"));//arraylist for atatus of pickup requirement,1 means emergency, 0 means not
                        reqPro.add(re,rs.getString("pic_pro"));//arraylist for progress number of pickup requirement,1 means the requirement has been finished, 0 means not
                        re++;
                    }System.out.println("have "+re+" req");
                        if(re==0){//if get back nothing from database, make a toast to show the user that there is no requirement in database, and return to HomeActivity view
                            Toast.makeText(CheckPickup.this, "查無檢藥需求", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent() ;
                            intent.setClass(CheckPickup.this, HomeActivity.class) ;
                            //Bundle bacc = new Bundle();
                            //bacc.putString("accin", acc);
                            //intent.putExtras(bacc);
                            startActivity(intent);
                        }else {// if there are some requirements, show it on list. The program needs to call an adapter(the requireList.java) to show the list of data.
                            adapter = new requireList(CheckPickup.this, reqNum, reqSta,reqPro,acc,conn);
                            list=(ListView)findViewById(R.id.check_list);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    Toast.makeText(CheckPickup.this, "You Clicked at " +reqNum.get(+ position), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
            }catch (SQLException e) {
                        e.printStackTrace();
            }
        }catch(Exception k){
            k.printStackTrace();
        }




    }

}
