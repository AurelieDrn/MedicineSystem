package com.example.lab708.tcmsystem.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lab708.tcmsystem.R;
import com.example.lab708.tcmsystem.dao.DAOFactory;
import com.example.lab708.tcmsystem.dao.MedicineDAO;
import com.example.lab708.tcmsystem.model.QuantityLocation;
import com.example.lab708.tcmsystem.model.RequirementDetail;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Aurelie on 11/07/2017.
 */

public class RequirementDetailsAdapter extends ArrayAdapter<RequirementDetail> {

    public RequirementDetailsAdapter(Context context, ArrayList<RequirementDetail> requirements) {
        super(context, 0, requirements);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RequirementDetail requirementDetail = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_check_pickup_detail, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.pickup_details_name);
        TextView tvQuant = (TextView) convertView.findViewById(R.id.pickup_details_quantity);
        TextView tvLocation = (TextView) convertView.findViewById(R.id.pickup_details_location);

        String quantities = "";
        String locations = "";

        tvName.setText(requirementDetail.getName());
        MedicineDAO medicineDAO = DAOFactory.getMedicineDAO();
        int experienceQuantity = 0;
        try {
            experienceQuantity = medicineDAO.getExperienceQuantity(requirementDetail.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int quantityInStock = requirementDetail.getQuantityInStock();
        for(QuantityLocation ql : requirementDetail.getQuantityLocationList()) {
            if(experienceQuantity > quantityInStock) {
                quantities += "Not enough in stock("+ql.getQuantity()+"/"+experienceQuantity+")"+"\n";
            }
            else {
                quantities += ql.getQuantity()+"\n";
            }
            locations += ql.getLocation()+"\n";
        }

        if(quantities.isEmpty()) {
            tvQuant.setText("Out of stock");
            tvLocation.setText("Out of stock");
        }
        else {
            tvQuant.setText(quantities);
            tvLocation.setText(locations);
        }

        return convertView;
    }
}
