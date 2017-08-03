package com.example.lab708.tcmsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab708.tcmsystem.CustomDialog;
import com.example.lab708.tcmsystem.MyNumberPicker;
import com.example.lab708.tcmsystem.R;
import com.example.lab708.tcmsystem.model.Medicine;
import com.example.lab708.tcmsystem.model.Pile;
import com.example.lab708.tcmsystem.dao.DAOFactory;
import com.example.lab708.tcmsystem.dao.MedicineDAO;
import com.example.lab708.tcmsystem.dao.PileDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExecuteShelvesActivity extends AppCompatActivity{

    private TextView serialNumber_tv, medName_tv;
    private EditText quantity_et;
    private NumberPicker shelf_np, layer_np, pile_np;
    private Button submit_btn, add_btn;
    private DatePicker expDate_dp;
    private TableLayout table;

    private String code;
    private String staffacc;

    private int index;

    private List<MyNumberPicker> numberPickerList;
    private List<EditText> editTextList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_shelves);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.table = (TableLayout) findViewById(R.id.execute_shelves_tl);

        // Index of the row I want to insert after
        this.index = table.indexOfChild(findViewById(R.id.lastRow));
        this.index++;

        numberPickerList = new ArrayList<>();
        editTextList = new ArrayList<>();

        serialNumber_tv = (TextView) findViewById(R.id.execute_shelves_med_number) ;
        medName_tv = (TextView) findViewById(R.id.execute_shelve_med_name) ;
        expDate_dp = (DatePicker) findViewById(R.id.execute_shelves_date);
        quantity_et = (EditText) findViewById(R.id.execute_shelves_quantity) ;
        shelf_np = (NumberPicker) findViewById(R.id.execute_shelves_shelf);
        layer_np = (NumberPicker) findViewById(R.id.execute_shelves_layer) ;
        pile_np = (NumberPicker) findViewById(R.id.execute_shelves_pile) ;
        submit_btn = (Button) findViewById(R.id.execute_shelves_submit) ;
        add_btn = (Button) findViewById(R.id.execute_shelves_add) ;

        // Get scanned code
        Bundle bcode = this.getIntent().getExtras();
        code = bcode.getString("Code_Num");

        /*
        Bundle bstaffacc = this.getIntent().getExtras();
        staffacc = bstaffacc.getString("staffacc");
        */
        shelf_np.setMinValue(1);
        shelf_np.setMaxValue(10);
        layer_np.setMinValue(1);
        layer_np.setMaxValue(10);
        pile_np.setMinValue(1);
        pile_np.setMaxValue(10);

       showInformation();

        // Submit
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(missingFields()) {
                    // Some fields are empty
                    Toast.makeText(ExecuteShelvesActivity.this, R.string.field_empty, Toast.LENGTH_SHORT).show();
                }
                else if(wrongLocations()) {
                    Toast.makeText(ExecuteShelvesActivity.this, R.string.all_locations_different, Toast.LENGTH_SHORT).show();
                }
                else {
                    int year = expDate_dp.getYear();
                    int month = expDate_dp.getMonth()+1;
                    int day = expDate_dp.getDayOfMonth();

                    String date = checkDigit(year) + "-" + checkDigit(month) + "-" + checkDigit(day);

                    int quantity = Integer.valueOf(String.valueOf(quantity_et.getText()));

                    String locationShelf = String.valueOf(shelf_np.getValue());
                    String locationLayer = String.valueOf(layer_np.getValue());
                    String locationPile = String.valueOf(pile_np.getValue());
                    String location = locationShelf+locationLayer+locationPile;

                    insertInDatabase(location, quantity, date);
                }

            }
        });

        // Add more location
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewLocation();
            }
        });
    }

    public String checkDigit(int number) {
        return number <=9 ? "0" + number : String.valueOf(number);
    }
    // Display the medicine information
    private void showInformation() {
        MedicineDAO medicineDAO = DAOFactory.getMedicineDAO();
        // OK
        try {
            if(medicineDAO.find(this.code)) {
                Medicine m = medicineDAO.select(this.code);
                serialNumber_tv.setText(m.getSerialNumber());
                medName_tv.setText(m.getName());
            }
            else {
                // Alert dialog error database
                Intent intent = new Intent(ExecuteShelvesActivity.this, ScanActivity.class);
                intent.putExtra("toFunction", "ExecuteShelvesActivity");
                CustomDialog.showErrorMessage(ExecuteShelvesActivity.this, "Error", "查無此藥品，請至資料庫新增! "+code+" not in database", intent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        final NumberPicker location1 = (NumberPicker) row.findViewById(R.id.location1);
        final NumberPicker location2 = (NumberPicker) row.findViewById(R.id.location2);
        final NumberPicker location3 = (NumberPicker) row.findViewById(R.id.location3);

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
        numberPickerList.add(myNumberPicker);
        editTextList.add(additionalQuant);

        // Delete the row
        Button deleteBtn = (Button) row.findViewById(R.id.delete_row);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table.removeView(row);
                editTextList.remove(additionalQuant);
                numberPickerList.remove(myNumberPicker);
                index--;
            }
        });
    }

    // Check if the user did not fill in all the fields
    private boolean missingFields() {
        if(quantity_et.getText().toString().matches("")) {
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

    // Check if the user entered the same location multiple times
    private boolean wrongLocations() {
        List<String> locations = new ArrayList<>();

        for(int i = 0; i < numberPickerList.size(); i++) {
            MyNumberPicker myNumberPicker_tmp = numberPickerList.get(i);
            String location1_tmp = String.valueOf(myNumberPicker_tmp.getLocation1().getValue());
            String location2_tmp = String.valueOf(myNumberPicker_tmp.getLocation2().getValue());
            String location3_tmp = String.valueOf(myNumberPicker_tmp.getLocation3().getValue());
            String location_tmp = location1_tmp + location2_tmp + location3_tmp;
            locations.add(location_tmp);
        }

        // Get the first number pickers values
        NumberPicker nbPicker1 = (NumberPicker) findViewById(R.id.execute_shelves_shelf);
        NumberPicker nbPicker2 = (NumberPicker) findViewById(R.id.execute_shelves_layer);
        NumberPicker nbPicker3 = (NumberPicker) findViewById(R.id.execute_shelves_pile);
        String location01 = String.valueOf(nbPicker1.getValue());
        String location02 = String.valueOf(nbPicker2.getValue());
        String location03 = String.valueOf(nbPicker3.getValue());

        String first = location01+location02+location03;

        // Compare the first location value with the additional ones
        for(String l : locations) {
            if(first.equals(l)) {
                return true;
            }
        }
        return false;
    }

    // Execute requests to insert in database
    private void insertInDatabase(String location, int quantity, String date) {
        Pile p = new Pile(location, code, quantity, date);
        PileDAO pileDAO = DAOFactory.getPileDAO();
        try {
            pileDAO.create(p);
        } catch (SQLException e) {
            showErrorDialog();
            e.printStackTrace();
        }

        // Execute requests for additional locations
        for(int i = 0; i < editTextList.size(); i++) {
            MyNumberPicker myNumberPicker_tmp = numberPickerList.get(i);
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

    // Alert dialog error inserting in database
    private void showErrorDialog() {
        Intent intent = new Intent(ExecuteShelvesActivity.this, HomeActivity.class);
        CustomDialog.showErrorDialogOneOption(ExecuteShelvesActivity.this, "Error!", "Error inserting in database", intent, R.string.back_home);
    }

    // Alert dialog success back to home or keep scanning
    private void showSuccessDialog() {
        Intent intent1 = new Intent(ExecuteShelvesActivity.this, HomeActivity.class);
        Intent intent2 = new Intent(ExecuteShelvesActivity.this, ScanActivity.class);
        intent2.putExtra("toFunction", "ExecuteShelvesActivity");
        CustomDialog.showSuccessDialogTwoOptions(ExecuteShelvesActivity.this, "Success!", "上架成功!", intent2, intent1, R.string.keep_scanning, R.string.back_home);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ExecuteShelvesActivity.this, ScanActivity.class);
        intent.putExtra("toFunction", "ExecuteShelvesActivity");
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}