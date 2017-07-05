package com.example.lab708.tcmsystem;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;


public class CheckPickup_Detail extends Activity {
    requireDetailList adapter;
    ListView list;
    ArrayList<String> reqMedName;//需要檢的藥品名稱
    ArrayList<Integer> reqMedEx;//需要檢的藥品經驗數量


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpickupdetail);

        Bundle getNameArr = this.getIntent().getExtras();
        Bundle getExpArr = this.getIntent().getExtras();
        reqMedName = ((ArrayList<String>) getNameArr.getSerializable("arrayListName"));
        reqMedEx = ((ArrayList<Integer>) getExpArr.getSerializable("arrayListExp"));

        adapter = new requireDetailList(CheckPickup_Detail.this, reqMedName, reqMedEx);
        list=(ListView)findViewById(R.id.check_listDetail);
        list.setAdapter(adapter);
     }


}
