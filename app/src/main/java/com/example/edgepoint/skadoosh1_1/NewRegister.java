package com.example.edgepoint.skadoosh1_1;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class NewRegister extends AppCompatActivity {

    public Spinner registered_spinner,city_spinner,barangay_spinner,precinct_spinner,add_education_spinner,add_income_spinner,add_prefix_spinner,add_ext_spinner;
    public EditText firstname,lastname,add_age,add_address,add_phonenumber,add_email,add_facebook;
    LinearLayout linearlayout_precinct,linearlayout_city;
    public TextView add_birthDate;
    String cityspin,bgyspin,regspin,City;
    Button submit;
    int day, month, year;
    public DatePickerDialog.OnDateSetListener mDateSetListener;
    ProgressDialog progressDialog;
    int checkedItem = -1;
    String setLeader = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ProfileSheet();
                form4PrevButton();
                submitForm();

            }
        },1000);

    }

    public void ProfileSheet(){
        Intent intent = getIntent();
        City = intent.getStringExtra("City");

        firstname = (EditText) findViewById(R.id.add_fname_id);
        lastname = (EditText) findViewById(R.id.add_lname_id);
        registered_spinner = (Spinner) findViewById(R.id.registered_spinner);
        city_spinner = (Spinner) findViewById(R.id.add_city_spinner);
        barangay_spinner = (Spinner) findViewById(R.id.add_barangay_spinner);
        precinct_spinner = (Spinner) findViewById(R.id.add_precinct_spinner);
        add_education_spinner = (Spinner) findViewById(R.id.add_educ_spinner);
        add_income_spinner = (Spinner) findViewById(R.id.add_income_spinner);
        add_birthDate = (TextView) findViewById(R.id.birthDate);
        add_age = (EditText) findViewById(R.id.add_age_id);
        add_address = (EditText) findViewById(R.id.add_address_id);
        add_phonenumber = (EditText) findViewById(R.id.add_mobile_id);
        add_email = (EditText) findViewById(R.id.add_email_id);
        add_facebook = (EditText) findViewById(R.id.add_facebook_id);
        add_prefix_spinner = (Spinner)findViewById(R.id.add_prefix_spinner);
        add_ext_spinner = (Spinner)findViewById(R.id.add_ext_spinner);
        linearlayout_precinct = (LinearLayout) findViewById(R.id.linearlayout_precinct);
        linearlayout_city = (LinearLayout) findViewById(R.id.Linearlayout_city);

        String[] regvoterArr = getResources().getStringArray(R.array.registered_voter);
        ArrayAdapter<String> adapterRegistered = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, regvoterArr);
        registered_spinner.setAdapter(adapterRegistered);
        registered_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                regspin = registered_spinner.getSelectedItem().toString();
                if (regspin.equals("No")) {
                    linearlayout_precinct.setVisibility(View.GONE);
                }else {
                    linearlayout_precinct.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String[] addcityArr = getResources().getStringArray(R.array.city_municipality);
        ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, addcityArr);
        city_spinner.setAdapter(adapterCity);
        if (!City.equals("empty")){
            city_spinner.setSelection(adapterCity.getPosition(City));
            city_spinner.setEnabled(false);
            city_spinner.setClickable(false);
        }

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                cityspin = city_spinner.getSelectedItem().toString();

                DatabaseAccess databaseAccessBarangay = DatabaseAccess.getInstance(NewRegister.this,"voters.db");
                databaseAccessBarangay.open();
                List<String> barangays = databaseAccessBarangay.getBarangay(cityspin,"City_Municipality=?","Barangay");
                databaseAccessBarangay.close();

                ArrayAdapter<String> adapterBarangay = new ArrayAdapter<String>(NewRegister.this,android.R.layout.simple_spinner_item, barangays);
                adapterBarangay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapterBarangay.notifyDataSetChanged();
                barangay_spinner.setAdapter(adapterBarangay);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        barangay_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                bgyspin = barangay_spinner.getSelectedItem().toString();

                DatabaseAccess databaseAccessPrecinct = DatabaseAccess.getInstance(NewRegister.this,"voters.db");
                databaseAccessPrecinct.open();
                List<String> precinct = databaseAccessPrecinct.getPrecinct(cityspin,bgyspin,"City_Municipality=? AND Barangay=?","Precinct");
                databaseAccessPrecinct.close();

                ArrayAdapter<String> adapterPrecinct = new ArrayAdapter<String>(NewRegister.this,android.R.layout.simple_spinner_item, precinct);
                adapterPrecinct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapterPrecinct.notifyDataSetChanged();
                precinct_spinner.setAdapter(adapterPrecinct);
                progressDialog.dismiss();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        BirthDatePicker();

        String[] prefixArr = getResources().getStringArray(R.array.prefix);
        ArrayAdapter<String> adapterPrefix = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prefixArr);
        add_prefix_spinner.setAdapter(adapterPrefix);

        String[] extensionArr = getResources().getStringArray(R.array.email_extension);
        ArrayAdapter<String> adapterExtension = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, extensionArr);
        add_ext_spinner.setAdapter(adapterExtension);

        String[] educArr = getResources().getStringArray(R.array.Highest_education);
        ArrayAdapter<String> adapterEducation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, educArr);
        add_education_spinner.setAdapter(adapterEducation);

        String[] incomeArr = getResources().getStringArray(R.array.Annual_income);

        ArrayAdapter<String> adapterIncome = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,android.R.id.text1,incomeArr)
        {
            @Override
            public View getDropDownView(final int position,final View convertView,final ViewGroup parent)
            {
                final View v=super.getDropDownView(position,convertView,parent);
                v.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ((TextView)v.findViewById(android.R.id.text1)).setSingleLine(false);
                    }
                });
                return v;
            }
        };
        adapterIncome.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_income_spinner.setAdapter(adapterIncome);

    }

    public void BirthDatePicker(){

        add_birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(NewRegister.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year_set, int month_set, int day_set) {
                month_set = month_set + 1;

                String date = month_set + "/" + day_set + "/" + year_set;
                add_birthDate.setText(date);

                String age_result = getAge(year_set,month_set,day_set);
                add_age.setText(age_result);
            }
        };
    }

    private String getAge(int year_age, int month_age, int day_age){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year_age, month_age, day_age);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public void submitForm(){
        submit = (Button) findViewById(R.id.qp_submit_id);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!firstname.getText().toString().isEmpty() && !lastname.getText().toString().isEmpty()) {
                    final String[] leaderArr = getResources().getStringArray(R.array.barangay_leader);
                    AlertDialog.Builder setbuilder = new AlertDialog.Builder(NewRegister.this);
                    setbuilder.setTitle("Choose Barangay or Precinct leader");

                    setbuilder.setSingleChoiceItems(leaderArr, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedItemId) {
                            setLeader = leaderArr[selectedItemId];
                            checkedItem = selectedItemId;
                        }
                    });

                    setbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String encodername = setLeader;

                            if (!encodername.isEmpty()) {
                                progressDialog = new ProgressDialog(NewRegister.this);
                                progressDialog.setMessage("Saving Data...");

                                String registered = registered_spinner.getSelectedItem().toString();
                                String votername = lastname.getText().toString().toUpperCase()+", "+ firstname.getText().toString().toUpperCase();
                                String address_submit = add_address.getText().toString();
                                String stringBirthday = add_birthDate.getText().toString();
                                String age_submit = add_age.getText().toString();
                                String email_submit = add_email.getText().toString() +  add_ext_spinner.getSelectedItem().toString();
                                String facebook_submit = add_facebook.getText().toString();
                                String contentPhone = add_prefix_spinner.getSelectedItem().toString() + add_phonenumber.getText().toString();
                                String high_education = add_education_spinner.getSelectedItem().toString();
                                String annual_income = add_income_spinner.getSelectedItem().toString();
                                String city = city_spinner.getSelectedItem().toString();
                                String barangay = barangay_spinner.getSelectedItem().toString();
                                String precinct = "";
                                String indicator_on_off = "off";
                                String deceased_yes_no = "no";
                                if (registered.equals("Yes")) {
                                    indicator_on_off = "on";
                                    precinct = precinct_spinner.getSelectedItem().toString();
                                }



                                if (votername.isEmpty() || city.isEmpty() || barangay.isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "Please fill-up some questions", Toast.LENGTH_LONG).show();
                                } else {
                                    progressDialog.show();

                                    savingtoDatabase(registered,votername,address_submit,city,barangay,precinct,indicator_on_off, deceased_yes_no, encodername,
                                            contentPhone,stringBirthday,age_submit,email_submit,facebook_submit,high_education,annual_income);
                                }

                                dialog.dismiss();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Input Encoder's Name", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    setbuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // what ever you want to do with No option.
                            dialog.dismiss();
                        }
                    });

                    AlertDialog setDialog = setbuilder.create();
                    setDialog.show();
                }else {
                    Toast.makeText(getApplicationContext(), "Fill-Up Full Name", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    public void savingtoDatabase(String registered,String votersname,String address,String city,String barangay,String precinct,String indicator_on_off,String deceased_yes_no,
                                 String encodername,String contact,String birthday_save,String age_save,String email_save,String facebook_save,String education_save,String income_save){

        DatabaseAccess databaseAccessSubmit = DatabaseAccess.getInstance(NewRegister.this,"voters.db");
        databaseAccessSubmit.open();

        boolean checkDuplicate = databaseAccessSubmit.checkDuplicateUpload(votersname);

        if(checkDuplicate == false){
            boolean submitInsert = databaseAccessSubmit.insertNewProfileData(registered,votersname,address,city,barangay,precinct,indicator_on_off,
                    deceased_yes_no,encodername,contact, birthday_save,age_save,email_save,facebook_save,education_save,income_save);


            if(submitInsert == true) {
                Toast.makeText(NewRegister.this,"Data Saved.",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                finish();
            }
            else {
                Toast.makeText(NewRegister.this, "Data not Save", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
        else {
            Toast.makeText(NewRegister.this,"Profile already saved.",Toast.LENGTH_LONG).show();
        }

        databaseAccessSubmit.close();
    }

    public void form4PrevButton(){
        Button prev4button = (Button) findViewById(R.id.qp_prev4btn_id);
        prev4button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
