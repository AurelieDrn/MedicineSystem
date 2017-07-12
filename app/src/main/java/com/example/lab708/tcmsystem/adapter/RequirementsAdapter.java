package com.example.lab708.tcmsystem.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.lab708.tcmsystem.classe.Pickup;
import com.example.lab708.tcmsystem.activity.CheckPickupDetailsActivity;
import com.example.lab708.tcmsystem.R;
import com.example.lab708.tcmsystem.activity.ExecutePickupActivity;
import com.example.lab708.tcmsystem.classe.QuantityLocation;
import com.example.lab708.tcmsystem.classe.Requirement;
import com.example.lab708.tcmsystem.classe.RequirementDetail;
import com.example.lab708.tcmsystem.dao.DAOFactory;
import com.example.lab708.tcmsystem.classe.Medicine;
import com.example.lab708.tcmsystem.dao.PileDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aurelie on 07/07/2017.
 */

public class RequirementsAdapter extends ArrayAdapter<Requirement>
{
    public RequirementsAdapter(Context context, ArrayList<Requirement> requirements) {
        super(context, 0, requirements);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Requirement requirement = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_check_pickup, parent, false);
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

        // Button more details
        Button btnMDetails = (Button) convertView.findViewById(R.id.more_details_btn);
        // Cache row position inside the button using `setTag`
        btnMDetails.setTag(position);
        btnMDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                // Access the row position here to get the correct data item
                List<RequirementDetail> reqDetailList = new ArrayList<RequirementDetail>();
                Requirement req = getItem(position);

                for(Medicine med : req.getMedicines()) {
                    RequirementDetail reqDetail = new RequirementDetail();
                    reqDetail.setName(med.getName());
                    PileDAO pileDAO = DAOFactory.getPileDAO();
                    try {
                        reqDetail.setQuantityLocationList(pileDAO.getQuantLocations(Integer.valueOf(med.getExperienceQuantity()), med.getSerialNumber()));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    reqDetailList.add(reqDetail);
                }
                Intent checkPickupDetailsActivity = new Intent();
                checkPickupDetailsActivity.setClass(view.getContext(), CheckPickupDetailsActivity.class);
                checkPickupDetailsActivity.putParcelableArrayListExtra("reqDetailList", (ArrayList<? extends Parcelable>) reqDetailList);
                view.getContext().startActivity(checkPickupDetailsActivity);
            }
       });

        // Button execute pickup
        Button btnExecutePickup = (Button) convertView.findViewById(R.id.buttonExecute);
        btnExecutePickup.setTag(position);
        btnExecutePickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                // Access the row position here to get the correct data item
                List<RequirementDetail> reqDetailList = new ArrayList<RequirementDetail>();
                Requirement req = getItem(position);

                for(Medicine med : req.getMedicines()) {
                    RequirementDetail reqDetail = new RequirementDetail();
                    reqDetail.setName(med.getName());
                    reqDetail.setSerialNumber(med.getSerialNumber());
                    PileDAO pileDAO = DAOFactory.getPileDAO();
                    try {
                        reqDetail.setQuantityLocationList(pileDAO.getQuantLocations(Integer.valueOf(med.getExperienceQuantity()), med.getSerialNumber()));
                        reqDetail.setQuantityInStock(pileDAO.getTotalQuantity(med.getSerialNumber()));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    reqDetailList.add(reqDetail);
                }

                List<Pickup> pickupList = new ArrayList<Pickup>();
                for(RequirementDetail rd : reqDetailList) {
                    for(QuantityLocation ql : rd.getQuantityLocationList()) {
                        pickupList.add(new Pickup(ql.getLocation(), rd.getName(), rd.getQuantityInStock(), ql.getQuantity(), rd.getSerialNumber()));
                    }
                }

                Intent executePickupActivity = new Intent();
                executePickupActivity.setClass(v.getContext(), ExecutePickupActivity.class);
                executePickupActivity.putParcelableArrayListExtra("pickupList", (ArrayList<? extends Parcelable>) pickupList);
                v.getContext().startActivity(executePickupActivity);

            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
