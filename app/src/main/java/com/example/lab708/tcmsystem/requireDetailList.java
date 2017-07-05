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

public class requireDetailList extends ArrayAdapter<String>{
    private final Activity context;
    ArrayList<String> reqMedName = new ArrayList<>();
    ArrayList<Integer> reqExp = new ArrayList<>();
    public requireDetailList(Activity context, ArrayList<String> rMName, ArrayList<Integer> rMExp) {
        super(context, R.layout.list_require, rMName);
        this.context = context;
        this.reqMedName = rMName;
        this.reqExp = rMExp;
        System.out.println("pos.size()"+reqMedName.size());

    }
    void setAdapter(Activity context,
                    ArrayList<String> p, ArrayList<Integer> i){
        this.reqMedName = p;
        System.out.println("pos.size()"+reqMedName.size());
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_requiredetail, null, true);

        TextView name = (TextView) rowView.findViewById(R.id.med_name);
        TextView exp = (TextView) rowView.findViewById(R.id.med_exp);

        name.setText(reqMedName.get(position).toString());
        exp.setText(Integer.toString(reqExp.get(position)));

        return rowView;
    }

}

