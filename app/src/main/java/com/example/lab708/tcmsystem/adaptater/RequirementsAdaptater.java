package com.example.lab708.tcmsystem.adaptater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lab708.tcmsystem.R;
import com.example.lab708.tcmsystem.dao.Medicine;

import java.util.ArrayList;

/**
 * Created by Aurelie on 07/07/2017.
 */

public class RequirementsAdaptater extends ArrayAdapter<Requirement>
{
    public RequirementsAdaptater(Context context, ArrayList<Requirement> requirements) {
        super(context, 0, requirements);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Requirement requirement = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_require, parent, false);
        }
        // Lookup view for data population
        TextView tvNum = (TextView) convertView.findViewById(R.id.check_pic_num);
        TextView tvSta = (TextView) convertView.findViewById(R.id.check_pic_sta);
        TextView tvName = (TextView) convertView.findViewById(R.id.check_pic_medname);

        // Populate the data into the template view using the data object
        tvNum.setText(requirement.getNumber());
        if(requirement.getEmergency() == 1) {
            tvSta.setText("緊急");
        }
        else {
            tvSta.setText("不緊急");
        }

        String medNames = new String();
        for(Medicine med : requirement.getMedicines()) {
            medNames += med.getName()+"\n";
        }
        tvName.setText(medNames);
        // Return the completed view to render on screen
        return convertView;
    }
}
