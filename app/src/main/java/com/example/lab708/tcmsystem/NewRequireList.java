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

import java.util.ArrayList;

public class NewRequireList extends ArrayAdapter<String>{
    private final Activity context;
    ArrayList<String> newReqMedNum = new ArrayList<String>();//需要檢的藥品序號med_num
    ArrayList<String> newReqMedName = new ArrayList<String>();//需要檢的藥品名稱med_name
    ArrayList<String> newReqMedCon = new ArrayList<String>();//需要檢的藥品經驗數量med_con ingredient
    public NewRequireList(Activity context, ArrayList<String> nRNum, ArrayList<String> nRName, ArrayList<String> nRCon) {
        super(context, R.layout.list_newrequire, nRNum);

        this.context = context;// the activity which would include this list
        this.newReqMedNum = nRNum;// serial number of each medicine
        this.newReqMedName = nRName; //name of each medicine
        this.newReqMedCon = nRCon;//content of each medicine
    }
    void setAdapter(Activity context,
                    ArrayList<String> p, ArrayList<Integer> i){
        this.newReqMedNum = p;
        System.out.println("pos.size()"+newReqMedNum.size());
    }// is not used

    @Override
    public View getView(final int position, View view, ViewGroup parent) {//according to the arraylist size, put each data into the TextView and define the button meaning
        LayoutInflater inflater = context.getLayoutInflater();
        System.out.println("LayoutInflater");
        View rowView= inflater.inflate(R.layout.list_newrequire, null, true);
        System.out.println(".list_newrequire, null, true)");

        TextView num = (TextView) rowView.findViewById(R.id.newReq_num);
        TextView name = (TextView) rowView.findViewById(R.id.newReq_name);
        TextView con = (TextView) rowView.findViewById(R.id.newReq_con);
        System.out.println("findViewById");

        num.setText(newReqMedNum.get(position));
        name.setText(newReqMedName.get(position));
        con.setText(newReqMedCon.get(position));

        Button del = (Button) rowView.findViewById(R.id.buttonDelNewReq);//to delete one row data according to the position
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("press---------------------"+position);

                newReqMedNum.remove(position);

                Intent intent = new Intent();
                intent.setClass(context, NewRequire.class);
                Bundle delToNewReq = new Bundle();
                delToNewReq.putSerializable("arrayListExp", newReqMedNum);
                delToNewReq.putString("Code_Num","return");
                intent.putExtras(delToNewReq);
                context.startActivity(intent);
            }
        });
        return rowView;
    }
}

