package com.example.edgepoint.skadoosh1_1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class QuestionForm_new extends AppCompatActivity {

    public Spinner gov_spinner, vgov_spinner, cong_spinner, mayor_spinner, education_spinner, income_spinner,spinYear1,spinYear2,spinYear3,spinYear4;
    String city_form,votersname_form,barangay_form,precinct_form, congressman="", mayor="", governor="", vicegovernor="";
    public TextView mDisplayDate;
    public EditText Age,address,phonenumber,email;
    public DatePickerDialog.OnDateSetListener mDateSetListener;
    public AutoCompleteTextView PartyListtextView;
    AppCompatCheckBox[] rcheck_bokal;
    int lenghtCount = 0;
    int day, month, year;
    Button submit;
    ProgressDialog progressDialog;
    List<String> listahan = new ArrayList<>();
    String[] ListArr,choiceLucenaMayor,choiceSariayaMayor,choiceCandelariaMayor,choiceDoloresMayor,choiceTiaongMayor,choiceStAntonioMayor,mayorArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_form_new);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        education_spinner = (Spinner) findViewById(R.id.educ_spinner);
        income_spinner = (Spinner) findViewById(R.id.income_spinner);
        gov_spinner = (Spinner) findViewById(R.id.gov_spinner);
        vgov_spinner = (Spinner) findViewById(R.id.vgov_spinner);
        cong_spinner = (Spinner) findViewById(R.id.cong_spinner);
        mayor_spinner = (Spinner) findViewById(R.id.mayor_spinner);
        PartyListtextView = (AutoCompleteTextView) findViewById(R.id.partylist_id);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        Age = (EditText) findViewById(R.id.age_id);
        address = (EditText) findViewById(R.id.address_id);
        phonenumber = (EditText) findViewById(R.id.mobile_id);
        email = (EditText) findViewById(R.id.email_id);
        spinYear1 = (Spinner)findViewById(R.id.year1_spinner);
        spinYear2 = (Spinner)findViewById(R.id.year2_spinner);
        spinYear3 = (Spinner)findViewById(R.id.year3_spinner);
        spinYear4 = (Spinner)findViewById(R.id.year4_spinner);

        Intent intent = getIntent();

        Form form = intent.getParcelableExtra("Form");

        city_form = form.getCity();
        precinct_form = form.getPrecinct();
        barangay_form = form.getBarangay();
        votersname_form = form.getName();

        TextView vname_tv = (TextView) findViewById(R.id.vname_tv_id);
        vname_tv.setText("("+votersname_form+")");

        DatabaseAccess databaseAccessDeceased = DatabaseAccess.getInstance(QuestionForm_new.this,"voters.db");
        databaseAccessDeceased.open();
        String deceased = databaseAccessDeceased.getDeceasedStr(votersname_form);
        databaseAccessDeceased.close();

        if (deceased.equals("yes")){
            Toast.makeText(getApplicationContext(), votersname_form+" already deceased", Toast.LENGTH_LONG).show();
            restoreDeceased();
        }
        else if (deceased.equals("no")){
            runQuestionForm("not empty");
        }
        else {
            showDeceasedDialog();
        }

    }

    public void restoreDeceased(){
        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(QuestionForm_new.this);
        confirmationDialog.setTitle("Restore "+votersname_form+"?");

        confirmationDialog.setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        progressDialog = new ProgressDialog(QuestionForm_new.this);
                        progressDialog.setMessage("Restoring Data...");
                        progressDialog.show();
                        savingtoDatabase(votersname_form,"","","","","","","",
                                "","","","","","off","","",
                                "","","","","","","","");
                        DatabaseAccess databaseAccessDeceased = DatabaseAccess.getInstance(QuestionForm_new.this,"voters.db");
                        databaseAccessDeceased.open();
                        databaseAccessDeceased.deleteRestore(votersname_form);
                        databaseAccessDeceased.close();
                    }
                });

        confirmationDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
                dialog.cancel();
            }
        });

        confirmationDialog.show();
    }

    public void showDeceasedDialog(){

        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(QuestionForm_new.this);
        confirmationDialog.setTitle("Is "+votersname_form+" deceased?");

        confirmationDialog.setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AlertDialog.Builder setbuilder = new AlertDialog.Builder(QuestionForm_new.this);
                        setbuilder.setTitle("Enter Encoder's Name");
                        final EditText encoder = new EditText(QuestionForm_new.this);
                        setbuilder.setView(encoder);

                        setbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                String encodername = encoder.getText().toString();

                                if (!encodername.isEmpty()){
                                    progressDialog = new ProgressDialog(QuestionForm_new.this);
                                    progressDialog.setMessage("Saving Data...");
                                    progressDialog.show();
                                    savingtoDatabase(votersname_form,"","","","","","","",
                                            "","","","","","on","yes","",
                                            encodername,"","","","","","","");
                                    dialog.dismiss();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Input Encoder's  Name", Toast.LENGTH_LONG).show();
                                }


                            }
                        });

                        setbuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // what ever you want to do with No option.
                                finish();
                                dialog.dismiss();
                            }
                        });

                        AlertDialog setDialog = setbuilder.create();
                        setDialog.show();



                    }
                });

        confirmationDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                runQuestionForm("empty");
                dialog.cancel();
            }
        });

        confirmationDialog.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
                dialog.cancel();
            }
        });

        final AlertDialog alert = confirmationDialog.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button btnNegative = alert.getButton(Dialog.BUTTON_NEGATIVE);
                btnNegative.setTextSize(20);

            }
        });

        alert.show();
    }

    public void runQuestionForm(final String ch){
        progressDialog = new ProgressDialog(QuestionForm_new.this);
        progressDialog.setMessage("Loading Questions...");
        progressDialog.show();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                QuestionForm(votersname_form,ch);
                submitForm();
                form4PrevButton();
            }
        },1000);
    }

    public void QuestionForm(String votersName, String choose){

        if (choose.equals("not empty")) {

            DatabaseAccess databaseAccessInfo = DatabaseAccess.getInstance(this, "voters.db");
            databaseAccessInfo.open();
            for (int x = 4; x < 30; x++) {
                String StringList = databaseAccessInfo.getList(votersName, city_form, barangay_form, precinct_form, "VotersName=? AND City_Municipality=? AND Barangay=? AND Precinct=?", null, x);
                if (x == 4 || x >= 10) {
                    listahan.add(StringList);
                }
            }
            databaseAccessInfo.close();
            ListArr = listahan.toArray(new String[listahan.size()]);
            address.setText(ListArr[0]);
            phonenumber.setText(ListArr[15]);
            email.setText(ListArr[18]);
            PartyListtextView.setText(ListArr[13]);
            String bday = ListArr[16];
            String age = ListArr[17];
            BirthDatePicker(bday,age);
        }else {
            BirthDatePicker("","");
        }

        String[] educArr = getResources().getStringArray(R.array.Highest_education);
        ArrayAdapter<String> adapterEducation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, educArr);
        education_spinner.setAdapter(adapterEducation);
        if (choose.equals("not empty")) {
            education_spinner.setSelection(adapterEducation.getPosition(ListArr[19]));
        }

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
        income_spinner.setAdapter(adapterIncome);
        if (choose.equals("not empty")) {
            income_spinner.setSelection(adapterIncome.getPosition(ListArr[20]));
        }

        String[] govArr = getResources().getStringArray(R.array.QuezProv_ch_governor);
        ArrayAdapter<String> adapterGovernor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, govArr);
        gov_spinner.setAdapter(adapterGovernor);
        if (choose.equals("not empty")) {
            for (int i = 0; i < govArr.length; i++) {
                String[] splitshoice = govArr[i].split(",");
                if (ListArr[1].equals(splitshoice[0])) {
                    gov_spinner.setSelection(adapterGovernor.getPosition(govArr[i]));
                }
            }
        }

        String[] vgovArr = getResources().getStringArray(R.array.QuezProv_ch_vicegovernor);
        ArrayAdapter<String> adapterViceGovernor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vgovArr);
        vgov_spinner.setAdapter(adapterViceGovernor);
        if (choose.equals("not empty")) {
            for (int i = 0; i < vgovArr.length; i++) {
                String[] splitshoice = vgovArr[i].split(",");
                if (ListArr[2].equals(splitshoice[0])) {
                    vgov_spinner.setSelection(adapterViceGovernor.getPosition(vgovArr[i]));
                }
            }
        }

        String[] congArr = getResources().getStringArray(R.array.QuezProv_ch_2ndcongressman);
        ArrayAdapter<String> adapterCongressman = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, congArr);
        cong_spinner.setAdapter(adapterCongressman);
        if (choose.equals("not empty")) {
            for (int i = 0; i < congArr.length; i++) {
                String[] splitshoice = congArr[i].split("[,\\s]");

                if (ListArr[3].equals(splitshoice[0])) {
                    cong_spinner.setSelection(adapterCongressman.getPosition(congArr[i]));
                } else if (ListArr[3].equals("SUAREZ_AMADEO") || ListArr[3].equals("SUAREZ_DAVID")) {
                    String[] splitListArr = ListArr[3].split("_");
                    String Combine = splitListArr[0] + ", " + splitListArr[1];
                    if (congArr[i].equals(Combine)) {
                        cong_spinner.setSelection(adapterCongressman.getPosition(congArr[i]));
                    }
                }
            }
        }

        String[] partylist = getResources().getStringArray(R.array.Party_list);
        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, partylist);
        PartyListtextView.setAdapter(adapter);
        PartyListtextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imn = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imn.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        });

        choiceLucenaMayor = getResources().getStringArray(R.array.QuezProv_lucena_ch_mayor);
        choiceSariayaMayor = getResources().getStringArray(R.array.QuezProv_sariaya_ch_mayor);
        choiceCandelariaMayor = getResources().getStringArray(R.array.QuezProv_candelaria_ch_mayor);
        choiceDoloresMayor = getResources().getStringArray(R.array.QuezProv_dolores_ch_mayor);
        choiceTiaongMayor = getResources().getStringArray(R.array.QuezProv_tiaong_ch_mayor);
        choiceStAntonioMayor = getResources().getStringArray(R.array.QuezProv_sanantonio_ch_mayor);

        if (city_form.equals("Lucena City")){
            mayorArr = choiceLucenaMayor;
        }
        else if (city_form.equals("Sariaya")){
            mayorArr = choiceSariayaMayor;
        }
        else if (city_form.equals("Candelaria")){
            mayorArr = choiceCandelariaMayor;
        }
        else if (city_form.equals("Dolores")){
            mayorArr = choiceDoloresMayor;
        }
        else if (city_form.equals("Tiaong")){
            mayorArr = choiceTiaongMayor;
        }
        else if (city_form.equals("San Antonio")){
            mayorArr = choiceStAntonioMayor;
        }

        TextView mayor_tv = (TextView) findViewById(R.id.mayor_tv);
        mayor_tv.setText("Mayor ("+city_form+")");
        ArrayAdapter<String> adapterMayor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mayorArr);
        mayor_spinner.setAdapter(adapterMayor);
        if (choose.equals("not empty")) {
            for (int i = 0; i < mayorArr.length; i++) {
                String[] splitshoice = mayorArr[i].split(",");
                if (ListArr[4].equals(splitshoice[0])) {
                    mayor_spinner.setSelection(adapterMayor.getPosition(mayorArr[i]));
                }
            }
        }

        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.LinearLayout_bokal);

        CompoundButton.OnCheckedChangeListener checker = new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
                if(lenghtCount > 2){
                    cb.setChecked(false);
                    lenghtCount=3;
                }

                if(isChecked){
                    lenghtCount++;
//                    Toast.makeText(getApplicationContext(), lenghtCount+" oncheck", Toast.LENGTH_LONG).show();
                }else if(!isChecked){
                    lenghtCount--;
//                    Toast.makeText(getApplicationContext(), lenghtCount+" uncheck", Toast.LENGTH_LONG).show();
                }
                if (lenghtCount == 4){
                    Toast.makeText(getApplicationContext(), "You've already chosen 3 Board Members", Toast.LENGTH_LONG).show();
                }
            }
        };

        String[] bokalArr = getResources().getStringArray(R.array.QuezProv_ch_bokal);
        rcheck_bokal = new AppCompatCheckBox[bokalArr.length];
        int y = 5;
        for (int i = 0; i < bokalArr.length; i++) {
            rcheck_bokal[i] = new AppCompatCheckBox(this);
            rcheck_bokal[i].setTextSize(18);
            rcheck_bokal[i].setText(bokalArr[i]);
            rcheck_bokal[i].setId(i);
            if (choose.equals("not empty")) {
                Boolean kgwd = Boolean.valueOf(ListArr[y]);
                if (kgwd == true) {
                    lenghtCount++;
                }

                rcheck_bokal[i].setChecked(kgwd);
            }
            rcheck_bokal[i].setOnCheckedChangeListener(checker);
            mLinearLayout.addView(rcheck_bokal[i]);
            y++;
        }

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
            years.add("");
        for (int i = 1900; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapterYear1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        spinYear1.setAdapter(adapterYear1);

        ArrayAdapter<String> adapterYear2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        spinYear2.setAdapter(adapterYear2);

        ArrayAdapter<String> adapterYear3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        spinYear3.setAdapter(adapterYear3);

        ArrayAdapter<String> adapterYear4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        spinYear4.setAdapter(adapterYear4);

        progressDialog.dismiss();
    }

    public void BirthDatePicker(final String bdstring, final String agestring){

        if (!bdstring.isEmpty()){
            mDisplayDate.setText(bdstring);
            Age.setText(agestring);
        }

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bdstring != ""){
                    String[] arrayString = bdstring.split("/");
                    month = Integer.parseInt(arrayString[0]);
                    day = Integer.parseInt(arrayString[1]);
                    year = Integer.parseInt(arrayString[2]);
                }
                else {
                    Calendar cal = Calendar.getInstance();
                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH);
                    day = cal.get(Calendar.DAY_OF_MONTH);
                }

                DatePickerDialog dialog = new DatePickerDialog(QuestionForm_new.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year_set, int month_set, int day_set) {
                month_set = month_set + 1;

                String date = month_set + "/" + day_set + "/" + year_set;
                mDisplayDate.setText(date);

                String age_result = getAge(year_set,month_set,day_set);
                Age.setText(age_result);
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

                AlertDialog.Builder setbuilder = new AlertDialog.Builder(QuestionForm_new.this);
                setbuilder.setTitle("Enter Encoder's Name");
                final EditText encoder = new EditText(QuestionForm_new.this);
                setbuilder.setView(encoder);

                setbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String encodername = encoder.getText().toString();

                        if (!encodername.isEmpty()) {
                            progressDialog = new ProgressDialog(QuestionForm_new.this);
                            progressDialog.setMessage("Saving Data...");

                            String address_submit = address.getText().toString();
                            String stringBirthday = mDisplayDate.getText().toString();
                            String age_submit = Age.getText().toString();
                            String email_submit = email.getText().toString();
                            String contentPhone = phonenumber.getText().toString();
                            String PartyList = PartyListtextView.getText().toString();
                            String high_education = education_spinner.getSelectedItem().toString();
                            String annual_income = income_spinner.getSelectedItem().toString();
                            String governor = gov_spinner.getSelectedItem().toString();
                            String vice_governor = vgov_spinner.getSelectedItem().toString();
                            String congressman = cong_spinner.getSelectedItem().toString();
                            String mayor = mayor_spinner.getSelectedItem().toString();

                            String alcalaBokal = String.valueOf(rcheck_bokal[0].isChecked());
                            String alejandrinoBokal = String.valueOf(rcheck_bokal[1].isChecked());
                            String casullaBokal = String.valueOf(rcheck_bokal[2].isChecked());
                            String gonzalesBokal = String.valueOf(rcheck_bokal[3].isChecked());
                            String liwanagBokal = String.valueOf(rcheck_bokal[4].isChecked());
                            String sioBokal = String.valueOf(rcheck_bokal[5].isChecked());
                            String talagaBokal = String.valueOf(rcheck_bokal[6].isChecked());
                            String tanadaBokal = String.valueOf(rcheck_bokal[7].isChecked());
                            String indicator_on_off = "on";
                            String deceased_yes_no = "no";

//                            Toast.makeText(getApplicationContext(), address_submit+"\n"+age_submit+"\n"+
//                                    email_submit+"\n"+contentPhone+"\n"+PartyList+"\n"+stringBirthday+"\n"+
//                                    high_education+"\n"+annual_income+"\n"+governor+"\n"+vice_governor+"\n"+
//                                    congressman+"\n"+mayor, Toast.LENGTH_LONG).show();

                            if (governor.isEmpty() || vice_governor.isEmpty() || congressman.isEmpty() || mayor.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Please fill-up all questions", Toast.LENGTH_LONG).show();
                            } else {
                                progressDialog.show();
                                String save_congressman = "";
                                if (congressman.equals("SUAREZ, AMADEO")) {
                                    save_congressman = "SUAREZ_AMADEO";
                                } else if (congressman.equals("SUAREZ, DAVID")) {
                                    save_congressman = "SUAREZ_DAVID";
                                } else {
                                    String[] nameListcongressman = congressman.split(",");
                                    save_congressman = nameListcongressman[0];
                                }

                                String[] nameListmayor = mayor.split(",");
                                String[] nameListgovernor = governor.split(",");
                                String[] nameListvicegovernor = vice_governor.split(",");

                                savingtoDatabase(votersname_form, nameListgovernor[0], nameListvicegovernor[0], save_congressman, nameListmayor[0],
                                        alcalaBokal, alejandrinoBokal, casullaBokal, gonzalesBokal, liwanagBokal, sioBokal, talagaBokal, tanadaBokal,
                                        indicator_on_off, deceased_yes_no, PartyList, encodername,contentPhone,address_submit,stringBirthday,age_submit,
                                        email_submit,high_education,annual_income);
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

            }

        });
    }

    public void savingtoDatabase(String voters,String governor,String vicegovernor,String congressman,String mayor,String alcalaBokal,String alejandrinoBokal, String casullaBokal,
                                 String gonzalesBokal,String liwanagBokal,String sioBokal,String talagaBokal,String tanadaBokal,String indicator_on_off,String deceased_yes_no,
                                 String partylist,String encodername,String contact,String address_save,String birthday_save,String age_save,String email_save,String education_save,String income_save){

        DatabaseAccess databaseAccessSubmit = DatabaseAccess.getInstance(QuestionForm_new.this,"voters.db");
        databaseAccessSubmit.open();

        boolean checkDuplicate = databaseAccessSubmit.checkDuplicateUpload(votersname_form);

        if(checkDuplicate == true){
            boolean submitUpdate = databaseAccessSubmit.updateData(voters,governor,vicegovernor,congressman,mayor,alcalaBokal,alejandrinoBokal,casullaBokal,
                    gonzalesBokal,liwanagBokal,sioBokal,talagaBokal,tanadaBokal,indicator_on_off,deceased_yes_no,partylist,encodername,contact,address_save,
                    birthday_save,age_save,email_save,education_save,income_save);


            if(submitUpdate == true) {
                Toast.makeText(QuestionForm_new.this,"Data Saved.",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                finish();
            }
            else {
                Toast.makeText(QuestionForm_new.this, "Data not Save", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }
        else {
            boolean submitInsert= databaseAccessSubmit.insertOnConflictData(voters,governor,vicegovernor,congressman,mayor,alcalaBokal,alejandrinoBokal,
                    casullaBokal,gonzalesBokal,liwanagBokal,sioBokal,talagaBokal,tanadaBokal,indicator_on_off,deceased_yes_no,partylist,encodername,contact,address_save,
                    birthday_save,age_save,email_save,education_save,income_save);


            if(submitInsert == true) {
                Toast.makeText(QuestionForm_new.this,"Data Saved.",Toast.LENGTH_LONG).show();
                finish();
                progressDialog.dismiss();
            }
            else {
                Toast.makeText(QuestionForm_new.this, "Data not Submitted", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
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
