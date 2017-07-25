package com.example.lab708.tcmsystem.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.lab708.tcmsystem.model.Requirement;

import java.util.ArrayList;

/**
 * Created by Aurelie on 12/07/2017.
 */

public class ExecutePickupAdapter extends ArrayAdapter<Requirement> {

    public ExecutePickupAdapter(Context context, ArrayList<Requirement> requirements) {
        super(context, 0, requirements);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Requirement requirement = getItem(position);


        return super.getView(position, convertView, parent);
    }
}
