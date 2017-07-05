package com.example.lab708.tcmsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class requireList extends ArrayAdapter<String>{
    private final Activity context;
    int ord=1;
    ArrayList<Integer> order = new ArrayList<>();
    ArrayList<Integer> reqNum;
    ArrayList<String> reqSta;
    ArrayList<String> reqPro;
    ArrayList<String> reqMedNum = new ArrayList<>();//the serial numbers of each pickup requirements
    ArrayList<ArrayList> reqMedName = new ArrayList<>();//the names of each pickup requirements. One requirement maybe contains many names of medicines, so here use the arraylist contain arralist.
    ArrayList<ArrayList> reqMedEx = new ArrayList<>();//the experience quantity of each pickup requirements. As above, one requirement maybe contains many experience quantity of medicines, so here use the arraylist contain arralist.
    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;
    String acc;
    public requireList(Activity context, ArrayList<Integer> rNum, ArrayList<String> rSta, ArrayList<String> rPro,String ac,Connection c) {
        super(context, R.layout.list_require, rSta);
        this.context = context;
        this.reqNum = rNum;
        this.reqSta = rSta;
        this.reqPro = rPro;
        acc=ac;
        conn=c;
        System.out.println("pos.size()"+reqSta.size());

    }
    void setAdapter(Activity context,
                    ArrayList<String> p, ArrayList<Integer> i){
        this.reqSta = p;
        System.out.println("pos.size()"+reqSta.size());
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_require, null, true);

        if(reqPro.get(position).equals("0")) {
            order.add(position,ord);

            TextView num = (TextView) rowView.findViewById(R.id.check_pic_num);
            TextView sta = (TextView) rowView.findViewById(R.id.check_pic_sta);
            TextView medname = (TextView) rowView.findViewById(R.id.check_pic_medname);

            num.setText(Integer.toString(ord));
            if (reqSta.get(position).equals("1")) {//to show whether or not the requirement is emergent
                sta.setText("緊急");//is emergent
            } else {
                sta.setText("不緊急");//is not emergent
            }

            String mname="";

            try {

                String qry = "SELECT * FROM PickupMed WHERE pic_num = "+ reqNum.get(position);//use the requirement serial numbers to get the medicine's serial number included in the requirement
                stmt = conn.createStatement();
                int re = 0;
                rs = stmt.executeQuery(qry);
                while (rs.next()) {
                        reqMedNum.add(re, rs.getString("pickup_mednum"));
                        re++;
                }
                ArrayList<String> rMedName = new ArrayList<>();
                ArrayList<Integer> rMedEx = new ArrayList<>();

                for(int i=0;i<re;i++){
                    qry = "SELECT * FROM Medicine WHERE med_num = "+ "\'"+reqMedNum.get(i)+"\'";
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(qry);
                    while (rs.next()) {
                        rMedName.add(i, rs.getString("med_name"));
                        rMedEx.add(i, rs.getInt("med_expquan"));
                        mname+=rs.getString("med_name")+"\n";
                    }
                    System.out.println("press---------------------"+re);
                    System.out.println("press---------------------"+position);
                    reqMedName.add(position, rMedName);
                    reqMedEx.add(position, rMedEx);
                }



            }catch(SQLException e){
                e.printStackTrace();}

            medname.setText(mname);//reqMedName.get(position));

            Button b = (Button) rowView.findViewById(R.id.button);
            b.setOnClickListener(new View.OnClickListener() {//press to call CheckPickup_Detail.java to show the detail of requirement
                @Override
                public void onClick(View v) {
                    System.out.println("press---------------------"+position);

                    Intent intent = new Intent();
                    intent.setClass(context, CheckPickup_Detail.class);

                    Bundle nameArr = new Bundle();
                    nameArr.putSerializable("arrayListName", reqMedName.get(position));
                    Bundle expArr = new Bundle();
                    expArr.putSerializable("arrayListExp", reqMedEx.get(position));
                    intent.putExtras(nameArr);
                    intent.putExtras(expArr);
                    System.out.println(Integer.toString(ord));
                    context.startActivity(intent);
                }
            });

            Button be = (Button) rowView.findViewById(R.id.buttonExecute);
            be.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent intent = new Intent();
                    intent.setClass(context, 執行檢藥.class);
                    Bundle bacc = new Bundle();
                    bacc.putString("staffacc", acc);//帳號

                    Bundle reqNum = new Bundle();
                    String reqNumS = reqNum.toString();//需求序號
                    reqNum.putString("reqNum", reqNumS);

                    intent.putExtras(bacc);
                    intent.putExtras(reqNum);
                    Activity a = new Activity();
                    a.startActivity(intent);*/
                }
            });
            ord++;
        }

        return rowView;
    }

}

