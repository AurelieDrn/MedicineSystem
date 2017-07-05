package com.example.lab708.tcmsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class ExecuteShelvesActivity extends AppCompatActivity{


    TextView med_num,med_name;
    EditText editquantity;
    NumberPicker editlocationone, editlocationtwo, editlocationthree;
    Button newnew,endend;
    String code;
    String staffacc;
    private DatePicker editdate;

    java.sql.Connection con;
    Statement stmt;
    ResultSet rs;
    private String selectedText;
    String worktime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_shelves);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

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

        //spinner
        /*ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,new String[]{"A", "B", "C", "D", "E", "F", "G", "H"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editlocationone.setAdapter(adapter);
        editlocationone.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                // Toast.makeText(MainActivity.this, "您選擇(You choose)"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                selectedText = adapterView.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(ExecuteShelvesActivity.this, "您沒有選擇任何項目(You have not selected any items)", Toast.LENGTH_LONG).show();
            }
        });*/
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        worktime = sDateFormat.format(new java.util.Date());

        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mariadb://120.105.161.89/TCMSystem", "TCMSystem", "TCMSystem");
            // Toast connected
            //Toast.makeText(ExecuteShelvesActivity.this, "已連線!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String qry = "SELECT * FROM Medicine WHERE med_num = \'"+code+"\'";
            System.out.println("---------sql--------"+qry);
            stmt = con.createStatement();
            int re = 0;
            rs = stmt.executeQuery(qry);
            while(rs.next()){
                re++;
            }
            String ree = String.valueOf(re);
            if(ree.equals("0")){//If the database does not have this information
                Toast.makeText(ExecuteShelvesActivity.this, "查無此藥品，請至資料庫新增!", Toast.LENGTH_SHORT).show();//Toast If you do not have this medicine, please go to the database
                Intent intent = new Intent() ;//back to scan
                intent.setClass(ExecuteShelvesActivity.this, ScanActivity.class) ;
                Bundle bacc = new Bundle();
                bacc.putString("toFunction", "ExecuteShelvesActivity");
                bacc.putString("staffacc", staffacc);
                intent.putExtras(bacc);
                startActivity(intent);
            } else {//else get  information
                String qry1 = "SELECT * FROM Medicine WHERE med_num = '"+code+"'";
                stmt = con.createStatement();
                rs = stmt.executeQuery(qry1);
                while (rs.next()) {
                    med_num.setText(rs.getString("med_num"));
                    med_name.setText(rs.getString("med_name"));
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        newnew.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          //datepicker
                                          int year = editdate.getYear();
                                          int month = editdate.getMonth()+1;
                                          int day = editdate.getDayOfMonth();
                                          final String wdate = year + "-" + month + "-" + day;

                                          try {
                                              if(editquantity.getText().toString().matches("")|| String.valueOf(editlocationtwo.getValue()).matches("")|| String.valueOf(editlocationthree.getValue()).toString().matches("")){
                                                  Toast toast = Toast.makeText(ExecuteShelvesActivity.this, "欄位不能為空!", Toast.LENGTH_SHORT);
                                                  toast.show();// Toast edittext can not be empty
                                              }else {


                                                  String quantity = String.valueOf(editquantity.getText());
                                                  String locationtwo = String.valueOf(editlocationtwo.getValue());
                                                  String locationthree = String.valueOf(editlocationthree.getValue());
                                                  //String locationone = selectedText;
                                                  String locationone = String.valueOf(editlocationone.getValue());
                                                  int intquantity = Integer.valueOf(quantity);
                                                  String location = locationone+locationtwo+locationthree;


                                                  String qry1 = "INSERT INTO Pile VALUES (NULL,'"+code+"',"+intquantity+",'"+location+"','"+wdate+"','"+staffacc+"')";
                                                  stmt.executeUpdate(qry1);

                                                  Toast toastsucess = Toast.makeText(ExecuteShelvesActivity.this, "上架成功!", Toast.LENGTH_SHORT);
                                                  toastsucess.show();
                                                  //The shelves are successful and empty edittext and then you can re-execute the medicine shelves to different locations


                                                  editquantity.setText("");

                                                  //editlocationtwo.setText("");
                                                  editlocationtwo.setValue(1);
                                                  //editlocationthree.setText("");
                                                  editlocationthree.setValue(1);
                                              }
                                          } catch(Exception e){

                                          }

                                      }
                                  }
        );

        endend.setOnClickListener(new View.OnClickListener() {//Scan another drug
                                        @Override
                                      public void onClick(View v) {
                                          try {
                                              stmt.close();
                                              con.close();
                                          }catch(SQLException e){

                                          }
                                          Intent endintent = new Intent() ;
                                          endintent.setClass(ExecuteShelvesActivity.this, ScanActivity.class) ;
                                          Bundle baccc = new Bundle();
                                          baccc.putString("toFunction", "ExecuteShelvesActivity");
                                          baccc.putString("staffacc", staffacc);
                                          endintent.putExtras(baccc);
                                          startActivity(endintent);


                                      }
                                  }
        );

    }



}
