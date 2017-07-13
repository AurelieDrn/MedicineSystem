package com.example.lab708.tcmsystem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import java.io.Serializable;
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
    Button add_btn;

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

                add_btn = (Button) findViewById(R.id.new_requirement_add);
                add_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NewRequirementActivity.this, ScanActivity.class);
                        intent.putExtra("toFunction", "NewRequirementActivity");
                        Bundle myBundle = new Bundle();
                        myBundle.putSerializable("newRequirementList", (Serializable) newRequirementList);
                        intent.putExtras(myBundle);
                        startActivity(intent);
                    }
                });
            }
            else {
                // Alert dialog error database
                Intent intent = new Intent(NewRequirementActivity.this, ScanActivity.class);
                intent.putExtra("toFunction", "NewRequirementActivity");
                Bundle myBundle = new Bundle();
                myBundle.putSerializable("newRequirementList", (Serializable) newRequirementList);
                intent.putExtras(myBundle);
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

        for(final NewRequirement nr : newRequirementList) {
            final TableRow row = (TableRow) View.inflate(NewRequirementActivity.this, R.layout.row_layout_new_pickup_requirement, null);
            table.addView(row, index);
            this.index++;

            final TextView medicineName_tv = (TextView) row.findViewById(R.id.row_layout_name);
            final TextView medicineNumber_tv = (TextView) row.findViewById(R.id.row_layout_number);
            final Spinner medicineQuantity_tv = (Spinner) row.findViewById(R.id.row_layout_quantity);
            delete_btn = (Button) row.findViewById(R.id.row_layout_button);

            medicineNumber_tv.setText(nr.getMedicineNumber());
            medicineName_tv.setText(nr.getMedicineName());

            String expQuantity = "";
            MedicineDAO medicineDAO = DAOFactory.getMedicineDAO();
            try {
                if (medicineDAO.find(this.code)) {
                    Medicine medicine = new Medicine();
                    try {
                        medicine = medicineDAO.select(code);
                         expQuantity = medicine.getExperienceQuantity();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Spinner
            List<String> spinnerArray =  new ArrayList<String>();
            int exp = Integer.valueOf(expQuantity);

            for(int i = 5; i <= 20; i+=5) {
                spinnerArray.add(i+"");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            medicineQuantity_tv.setAdapter(adapter);

            int spinnerPosition = adapter.getPosition(String.valueOf(nr.getQuantity()));
            medicineQuantity_tv.setSelection(spinnerPosition);

            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    table.removeView(row);
                    newRequirementList.remove(nr);
                }
            });

            // Update newRequirementList when quantity changes
            medicineQuantity_tv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String q = medicineQuantity_tv.getSelectedItem().toString();
                    nr.setQuantity(Integer.valueOf(q));
                    Log.d("Q", q);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    }
}
