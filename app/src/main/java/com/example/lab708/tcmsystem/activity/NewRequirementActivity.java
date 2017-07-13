package com.example.lab708.tcmsystem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.lab708.tcmsystem.CustomDialog;
import com.example.lab708.tcmsystem.R;
import com.example.lab708.tcmsystem.classe.Medicine;
import com.example.lab708.tcmsystem.classe.NewRequirement;
import com.example.lab708.tcmsystem.dao.DAOFactory;
import com.example.lab708.tcmsystem.dao.MedicineDAO;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewRequirementActivity extends AppCompatActivity {

    Bundle bundle;
    String code;

    TextView medicineNumber_tv;
    TextView medicineName_tv;
    Spinner medicineQuantity_tv;
    Button delete_btn;

    TableRow row;
    TableLayout table;

    int index;

    List<NewRequirement> newRequirementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_requirement);

        // Get extras
        bundle = this.getIntent().getExtras();
        code = bundle.getString("Code_Num");
        newRequirementList = ((ArrayList<NewRequirement>) bundle.getSerializable("newRequirementList"));
        if(newRequirementList == null) {
            newRequirementList = new ArrayList<>();
        }

        MedicineDAO medicineDAO = DAOFactory.getMedicineDAO();
        try {
            if(medicineDAO.find(this.code)) {
                Medicine medicine = new Medicine();
                try {
                    medicine = medicineDAO.select(code);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                newRequirementList.add(new NewRequirement(medicine.getName(), medicine.getSerialNumber(), Integer.valueOf(medicine.getExperienceQuantity())));
                addRows();
            }
            else {
                // Alert dialog error database
                Intent intent = new Intent(NewRequirementActivity.this, ScanActivity.class);
                intent.putExtra("toFunction", "NewRequirementActivity");
                CustomDialog.showErrorMessage(NewRequirementActivity.this, "上架作業", "查無此藥品，請至資料庫新增! " + code + " not in database", intent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addRows() {
        table = (TableLayout) findViewById(R.id.new_requirement_tl);
        this.index = table.indexOfChild(findViewById(R.id.new_requirement_row));
        this.index++;

        for(NewRequirement nr : newRequirementList) {
            row = (TableRow) View.inflate(NewRequirementActivity.this, R.layout.row_layout_new_pickup_requirement, null);
            table.addView(row, index);
            this.index++;

            medicineName_tv = (TextView) row.findViewById(R.id.row_layout_name);
            medicineNumber_tv = (TextView) row.findViewById(R.id.row_layout_number);
            medicineQuantity_tv = (Spinner) row.findViewById(R.id.row_layout_quantity);
            delete_btn = (Button) row.findViewById(R.id.row_layout_button);

            medicineNumber_tv.setText(nr.getMedicineNumber());
            medicineName_tv.setText(nr.getMedicineName());

            List<String> spinnerArray =  new ArrayList<String>();
            int quant = Integer.valueOf(nr.getQuantity());
            spinnerArray.add(String.valueOf(nr.getQuantity()));
            for(int i = 0; i < 4; i++) {
                quant += 10;
                spinnerArray.add(quant+"");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            medicineQuantity_tv.setAdapter(adapter);

            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    table.removeView(row);
                }
            });
        }

    }
}
