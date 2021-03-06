package com.example.lab708.tcmsystem.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.lab708.tcmsystem.CustomDialog;
import com.example.lab708.tcmsystem.R;
import com.example.lab708.tcmsystem.dao.DAOFactory;
import com.example.lab708.tcmsystem.dao.MedicineDAO;
import com.example.lab708.tcmsystem.dao.RequirementDAO;
import com.example.lab708.tcmsystem.model.Medicine;
import com.example.lab708.tcmsystem.model.NewRequirement;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewRequirementActivity extends AppCompatActivity {

    Bundle bundle;
    String code;

    Button delete_btn;
    Button add_btn;
    Button submit_btn;

    TableLayout table;

    int index;

    List<NewRequirement> newRequirementList;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_requirement);

        progress = null;

        // Get extras
        bundle = this.getIntent().getExtras();
        code = bundle.getString("Code_Num");

        newRequirementList = ((ArrayList<NewRequirement>) bundle.getSerializable("newRequirementList"));
        if(newRequirementList == null) {
            newRequirementList = new ArrayList<>();
        }

        /*
        Intent to go back to scan activity.
        We just prepare the intent first.
        */
        Intent backToScan = new Intent(NewRequirementActivity.this, ScanActivity.class);
        backToScan.putExtra("toFunction", "NewRequirementActivity");
        Bundle myBundle1 = new Bundle();
        myBundle1.putSerializable("newRequirementList", (Serializable) newRequirementList);
        backToScan.putExtras(myBundle1);

        // The medicine has already been scanned.
        if(elementExists(code)) {
            addRows();
            CustomDialog.showSimpleErrorDialog(this, "Error", "Medicine already scanned");
        }
        else {
            MedicineDAO medicineDAO = DAOFactory.getMedicineDAO();
            try {
                if(medicineDAO.find(this.code)) {
                    Medicine medicine = new Medicine();
                    try {
                        medicine = medicineDAO.select(code);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    // Add the new requirement to the list
                    newRequirementList.add(new NewRequirement(medicine.getName(), medicine.getSerialNumber(), Integer.valueOf(medicine.getExperienceQuantity())));

                    addRows();
                }
                else {
                    // Alert dialog error database
                    Intent intent = new Intent(NewRequirementActivity.this, ScanActivity.class);
                    intent.putExtra("toFunction", "NewRequirementActivity");
                    Bundle myBundle = new Bundle();
                    myBundle.putSerializable("newRequirementList", (Serializable) newRequirementList);
                    intent.putExtras(myBundle);
                    CustomDialog.showErrorDialogOneOption(NewRequirementActivity.this, "Error!", "查無此藥品，請至資料庫新增! " + code + " not in database", intent, R.string.back);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Go back to scan to add a new requirement
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

        // Submit
        submit_btn = (Button) findViewById(R.id.new_requirement_submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RequirementDAO requirementDAO = DAOFactory.getRequirementDAO();
                CheckBox checkBox = (CheckBox) findViewById(R.id.new_requirement_emergency);
                int emergency = 0;

                if(checkBox.isChecked()) {
                    emergency = 1;
                }
                else {
                    emergency = 0;
                }
                try {
                    Log.d("submit requirement list", newRequirementList.toString());
                    requirementDAO.createNewRequirements(newRequirementList, emergency);
                    showSuccessDialog();
                } catch (SQLException e) {
                    e.printStackTrace();
                    showErrorDialog();
                }
            }
        });
    }

    /**
     * Add the rows of scanned medicines on the page.
     */
    private void addRows() {
        table = (TableLayout) findViewById(R.id.new_requirement_tl);
        this.index = table.indexOfChild(findViewById(R.id.new_requirement_row));
        this.index++;

        for(int i = 0; i<newRequirementList.size(); i++) {
            final NewRequirement nr = newRequirementList.get(i);

            final TableRow row = (TableRow) View.inflate(NewRequirementActivity.this, R.layout.row_layout_new_pickup_requirement, null);
            table.addView(row, index);
            this.index++;

            final TextView medicineName_tv = (TextView) row.findViewById(R.id.row_layout_name);
            final TextView medicineNumber_tv = (TextView) row.findViewById(R.id.row_layout_number);
            final Spinner medicineQuantity_et = (Spinner) row.findViewById(R.id.row_layout_quantity);

            delete_btn = (Button) row.findViewById(R.id.row_layout_button);

            medicineNumber_tv.setText(nr.getMedicineNumber());
            medicineName_tv.setText(nr.getMedicineName());

            // Set default quantity
            String quantity_string = String.valueOf(nr.getQuantity());
            // Substract 49 because -1 for index starting at 0 and -48 to convert char to int
            int quantity_index = Integer.valueOf(quantity_string.charAt(0))-49;
            medicineQuantity_et.setSelection(quantity_index);

            // Delete a scanned medicine
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    table.removeView(row);
                    newRequirementList.remove(nr);
                }
            });

            // Get the quantity changes from spinner
            final int finalI = i;
            medicineQuantity_et.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String s = parent.getItemAtPosition(position).toString();
                    if(s.toString().equals("") == false) {
                        newRequirementList.get(finalI).setQuantity(Integer.valueOf(s.toString()));
                        //nr.setQuantity(Integer.valueOf(s.toString()));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private void showSuccessDialog() {
        Intent intent1 = new Intent(NewRequirementActivity.this, HomeActivity.class);
        Intent intent2 = new Intent(NewRequirementActivity.this, ScanActivity.class);
        intent2.putExtra("toFunction", "NewRequirementActivity");
        int button1 = R.string.back_home;
        int button2 = R.string.keep_scanning;
        CustomDialog.showSuccessDialogTwoOptions(NewRequirementActivity.this, "Success!", "Your requirement was successfully sent", intent2, intent1, button2, button1);
    }

    private void showErrorDialog() {
        Intent intent = new Intent(NewRequirementActivity.this, HomeActivity.class);
        CustomDialog.showErrorDialogOneOption(NewRequirementActivity.this, "Error!", "Database error", intent, R.string.back_home);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(NewRequirementActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(NewRequirementActivity.this);
        }
        builder.setCancelable(false);
        builder.setTitle("Warning!")
                .setMessage("Are you sure you want to leave this page? If you press yes, all the information will be lost")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(NewRequirementActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    @Override
    public void onPause(){
        super.onPause();
        if(progress != null)
            progress.dismiss();
    }

    private boolean elementExists(String serialNumber) {
        boolean b = false;

        for(NewRequirement nr : newRequirementList) {
            if(nr.getMedicineNumber().equals(serialNumber)) {
                b = true;
                break;
            }
        }
        return b;
    }
}
