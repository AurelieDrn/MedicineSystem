package com.example.lab708.tcmsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
//catch   arrayListNewReq
//returnZbar staffacc="",arrayListNewReq

public class NewRequire extends Activity {
    NewRequireList adapter;
    ListView list;
    ArrayList<String> newReqMedNum=new ArrayList<String>();//需要檢的藥品num  med_num
    ArrayList<String> newReqMedName=new ArrayList<String>();//需要檢的藥品Name  med_name
    ArrayList<String> NewReqMedCon=new ArrayList<String>();//需要檢的藥品內容 med_con ingredient
    String code;
    static final String username="TCMSystem";
    static final String password="TCMSystem";
    final String URL="jdbc:mariadb://120.105.161.89/TCMSystem";
    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newrequire);

        Bundle getNumArr = this.getIntent().getExtras();
        newReqMedNum = ((ArrayList<String>) getNumArr.getSerializable("arrayListNewReq"));
        Bundle getBcode = this.getIntent().getExtras();
        code = getBcode.getString("Code_Num");
        String qry="";

        try{Class.forName("org.mariadb.jdbc.Driver").newInstance();
            try{
                conn = DriverManager.getConnection(URL,username,password);
                Toast.makeText(NewRequire.this, "已連線!", Toast.LENGTH_SHORT).show();//success

                qry = "SELECT * FROM Medicine WHERE med_num = \'"+code+"\'";
                System.out.println(qry);
                stmt = conn.createStatement();
                int re = 0;//number of piece of data get back from Database
                rs = stmt.executeQuery(qry);
                while(rs.next()){
                    re++;
                }
                String ree = String.valueOf(re);
                if(ree.equals("0")&&!code.equals("return")){
                    Toast.makeText(NewRequire.this, "查無此藥品，請至資料庫新增!", Toast.LENGTH_SHORT).show();//cannot search medicine, please new a data in Database
                    Intent intent = new Intent() ;
                    intent.setClass(NewRequire.this, ScanActivity.class) ;
                    Bundle bacc = new Bundle();
                    bacc.putString("toFunction", "NewRequire");
                    bacc.putString("staffacc", "");
                    bacc.putSerializable("arrayListNewReq", newReqMedNum);//arraylist
                    intent.putExtras(bacc);
                    startActivity(intent);
                }else {
                    if(newReqMedNum==null){System.out.println("---------------null");newReqMedNum.add("");}
                    if(!code.equals("return")){newReqMedNum.add(newReqMedNum.size(),code);}//when this activity is not started by NewRequireList
                    int change=0;
                    for(int i=0;i<newReqMedNum.size();i++){
                        if(newReqMedNum.get(0).equals("")){newReqMedNum.remove(0);change=1;continue;}
                        qry= "SELECT * FROM Medicine WHERE med_num = \'"+newReqMedNum.get(i-change)+"\'" ;
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery(qry);
                        while(rs.next()) {
                            newReqMedName.add(i, rs.getString("med_name"));
                            NewReqMedCon.add(i, rs.getString("med_con"));
                        }
                    }
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }catch(Exception k){
            k.printStackTrace();
        }

        if(newReqMedNum==null||newReqMedName==null||NewReqMedCon==null){
            Toast.makeText(NewRequire.this, "無檢藥藥品資料  回到掃描頁", Toast.LENGTH_SHORT).show();//requriment list has not data,please get back to scan page

            Intent newReq = new Intent();
            newReq.setClass(NewRequire.this,ScanActivity.class);

            Bundle bacc = new Bundle();
            bacc.putString("toFunction", "NewRequire");
            bacc.putString("staffacc", "");
            bacc.putSerializable("arrayListNewReq", newReqMedNum);
            newReq.putExtras(bacc);

            startActivity(newReq);
        }else{
            adapter = new NewRequireList(NewRequire.this, newReqMedNum, newReqMedName, NewReqMedCon);
            list=(ListView)findViewById(R.id.newreq_list);
            list.setAdapter(adapter);
        }
    }
}
