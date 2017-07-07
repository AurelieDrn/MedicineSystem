package com.example.lab708.tcmsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab708.tcmsystem.dao.DAO;
import com.example.lab708.tcmsystem.dao.DAOFactory;
import com.example.lab708.tcmsystem.dao.Medicine;
import com.example.lab708.tcmsystem.dao.Pile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExecuteShelvesActivity extends AppCompatActivity{

    private TextView med_num,med_name;
    private EditText editquantity;
    private NumberPicker editlocationone, editlocationtwo, editlocationthree;
    private Button newnew, endend;
    private DatePicker editdate;
    private TableLayout table;

    private String code;
    private String staffacc;
    private String worktime;

    private int index;

    private List<MyNumberPicker> myNumberPickerList;
    private List<EditText> editTextList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_shelves);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.table = (TableLayout) findViewById(R.id.table);

        // Index of the row I want to insert after
        this.index = table.indexOfChild(findViewById(R.id.lastRow));
        this.index++;

        myNumberPickerList = new ArrayList<>();
        editTextList = new ArrayList<>();

        med_num = (TextView) findViewById(R.id.v_MDnumber) ;
        med_name = (TextView) findViewById(R.id.v_MDname) ;
        editdate = (DatePicker) findViewById(R.id.v_MDdate);
        editquantity = (EditText) findViewById(R.id.quantity) ;
        editlocationone = (NumberPicker) findViewById(R.id.locationone);
        editlocationtwo = (NumberPicker) findViewById(R.id.locationtwo) ;
        editlocationthree = (NumberPicker) findViewById(R.id.locationthree) ;
        newnew = (Button) findViewById(R.id.newbutton) ;
        endend = (Button) findViewById(R.id.endbutton) ;

        //get Bundle Code_Num&staffac
        Bundle bcode = this.getIntent().getExtras();
        code = bcode.getString("Code_Num");

        //Bundle bstaffacc = this.getIntent().getExtras();
        //staffacc = bstaffacc.getString("staffacc");

        //System.out.println("staffacc  "+staffacc);

        editlocationone.setMinValue(1);
        editlocationone.setMaxValue(10);
        editlocationtwo.setMinValue(1);
        editlocationtwo.setMaxValue(10);
        editlocationthree.setMinValue(1);
        editlocationthree.setMaxValue(10);

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        worktime = sDateFormat.format(new java.util.Date());

        showInformation();

        // Submit
        newnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(missingFields()) {
                    // Some fields are empty
                    Toast.makeText(ExecuteShelvesActivity.this, "欄位不能為空!", Toast.LENGTH_SHORT).show();
                }
                else {
                    int year = editdate.getYear();
                    int month = editdate.getMonth()+1;
                    int day = editdate.getDayOfMonth();
                    String wdate = year + "-" + month + "-" + day;

                    String quantity = String.valueOf(editquantity.getText());
                    int intquantity = Integer.valueOf(quantity);

                    String locationone = String.valueOf(editlocationone.getValue());
                    String locationtwo = String.valueOf(editlocationtwo.getValue());
                    String locationthree = String.valueOf(editlocationthree.getValue());
                    String location = locationone+locationtwo+locationthree;

                    insertInDatabase(location, intquantity, wdate);
                }

            }
        });

        // Add more location
        endend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewLocation();
            }
        });
    }
    // Display the medicine information
    private void showInformation() {
        DAO<Medicine> medicineDAO = DAOFactory.getMedicineDAO();
        // OK
        if(medicineDAO.find(this.code)) {
            Medicine m = medicineDAO.select(this.code);
            med_num.setText(m.getSerialNumber());
            med_name.setText(m.getName());
        }
        else {
            // Alert dialog success
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(ExecuteShelvesActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(ExecuteShelvesActivity.this);
            }
            builder.setTitle("上架作業")
                    .setMessage("查無此藥品，請至資料庫新增! "+code+" not in database")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ExecuteShelvesActivity.this, ScanActivity.class);
                            intent.putExtra("toFunction", "ExecuteShelvesActivity");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    // Add new components to layout for Add new location fucntion
    private void addNewLocation() {
        // The row I created in an XML file that I want to insert
        final TableRow row = (TableRow) View.inflate(ExecuteShelvesActivity.this, R.layout.row_layout, null);

        // Add the row to the layout
        table.addView(row, index);

        this.index++;

        // Get the number pickers from the view
        final NumberPicker location1 = (NumberPicker) findViewById(R.id.location1);
        final NumberPicker location2 = (NumberPicker) findViewById(R.id.location2);
        final NumberPicker location3 = (NumberPicker) findViewById(R.id.location3);

        final EditText additionalQuant = (EditText) findViewById(R.id.additional_quantity);

        // Initialize the min and max value of the number pickers
        location1.setMinValue(1);
        location1.setMaxValue(10);
        location2.setMinValue(1);
        location2.setMaxValue(10);
        location3.setMinValue(1);
        location3.setMaxValue(10);

        // Add them to the list
        final MyNumberPicker myNumberPicker = new MyNumberPicker(location1, location2, location3);
        myNumberPickerList.add(myNumberPicker);
        editTextList.add(additionalQuant);

        // Delete the row
        Button deleteBtn = (Button) findViewById(R.id.delete_row);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table.removeView(row);
                editTextList.remove(additionalQuant);
                myNumberPickerList.remove(myNumberPicker);
            }
        });
    }

    // Check if the user did not fill in all the fields
    private boolean missingFields() {
        if(editquantity.getText().toString().matches("")) {
            return true;
        }
        else if(missingAdditionalQuantities()) {
            return true;
        }
        else {
            return false;
        }
    }

    // Check if the user did not fill in the additional quantities fields
    private boolean missingAdditionalQuantities() {
        for(EditText et : editTextList) {
            if(et.getText().toString().matches("")) {
                return true;
            }
        }
        return false;
    }

    // Execute requests to insert in database
    private void insertInDatabase(String location, int quantity, String date) {
        Pile p = new Pile(location, code, quantity, date);
        DAO<Pile> pileDAO = DAOFactory.getPileDAO();
        try {
            pileDAO.create(p);
        } catch (SQLException e) {
            showErrorDialog();
            e.printStackTrace();
        }

        // Execute requests for additional locations
        for(int i = 0; i < editTextList.size(); i++) {
            MyNumberPicker myNumberPicker_tmp = myNumberPickerList.get(i);
            String location1_tmp = String.valueOf(myNumberPicker_tmp.getLocation1().getValue());
            String location2_tmp = String.valueOf(myNumberPicker_tmp.getLocation2().getValue());
            String location3_tmp = String.valueOf(myNumberPicker_tmp.getLocation3().getValue());
            String location_tmp = location1_tmp+location2_tmp+location3_tmp;

            int quantity_tmp = Integer.valueOf(editTextList.get(i).getText().toString());
            Pile pile_tmp = new Pile(location_tmp, code, quantity_tmp, date);

            try {
                pileDAO.create(pile_tmp);
            } catch (SQLException e) {
                showErrorDialog();
                e.printStackTrace();
            }
        }

        // Alert dialog success
        showSuccessDialog();

    }

    private void showErrorDialog() {
        // Alert dialog success
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ExecuteShelvesActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(ExecuteShelvesActivity.this);
        }
        builder.setTitle("上架作業")
                .setMessage("Error inserting in database")
                .setPositiveButton(R.string.back_home, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ExecuteShelvesActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ExecuteShelvesActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(ExecuteShelvesActivity.this);
        }
        builder.setTitle("上架作業")
                .setMessage("上架成功!")
                .setPositiveButton(R.string.back_home, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ExecuteShelvesActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.keep_scanning, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ExecuteShelvesActivity.this, ScanActivity.class);
                        intent.putExtra("toFunction", "ExecuteShelvesActivity");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ExecuteShelvesActivity.this, ScanActivity.class);
        intent.putExtra("toFunction", "ExecuteShelvesActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
