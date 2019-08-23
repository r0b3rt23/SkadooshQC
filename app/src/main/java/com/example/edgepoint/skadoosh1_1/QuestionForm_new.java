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

    public Spinner extension_spinner,prefix_spinner,gov_spinner, vgov_spinner, cong_spinner, mayor_spinner,vicemayor_spinner, education_spinner, income_spinner,
            educ_spinYear1,educ_spinYear2,educ_spinYear3,educ_spinYear4,
            medical_spinYear1,medical_spinYear2,medical_spinYear3,medical_spinYear4,
            agriculture_spinYear1,agriculture_spinYear2,agriculture_spinYear3,agriculture_spinYear4,
            cooperative_spinYear1,cooperative_spinYear2,cooperative_spinYear3,cooperative_spinYear4,
            educ_spinPhp1,educ_spinPhp2,educ_spinPhp3,educ_spinPhp4,
            medical_spinPhp1,medical_spinPhp2,medical_spinPhp3,medical_spinPhp4,
            agriculture_spinPhp1,agriculture_spinPhp2,agriculture_spinPhp3,agriculture_spinPhp4,
            cooperative_spinPhp1,cooperative_spinPhp2,cooperative_spinPhp3,cooperative_spinPhp4,
            educ_service_spinner,medical_service_spinner,agriculture_service_spinner,cooperative_service_spinner;

    String city_form,votersname_form,barangay_form,precinct_form, congressman="", mayor="", governor="", vicegovernor="";
    public TextView mDisplayDate,partylist_tv,vicegovernor_tv;
    public EditText Age,address,phonenumber,email,facebook;
    public DatePickerDialog.OnDateSetListener mDateSetListener;
    public AutoCompleteTextView PartyListtextView;
    AppCompatCheckBox[] rcheck_bokal;
    int lenghtCount = 0;
    int day, month, year;
    int checkedItem = -1;
    String setLeader = "";
    Button submit;
    ProgressDialog progressDialog;
    List<String> listahan = new ArrayList<>();
    String[] ListArr,choiceLucenaMayor,choiceSariayaMayor,choiceCandelariaMayor,choiceDoloresMayor,choiceTiaongMayor,choiceStAntonioMayor,choiceLucenaViceMayor,
            choiceSariayaViceMayor,choiceCandelariaViceMayor,choiceDoloresViceMayor,choiceTiaongViceMayor,choiceStAntonioViceMayor,mayorArr,vicemayorArr;

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
        vicemayor_spinner = (Spinner) findViewById(R.id.vicemayor_spinner);
        PartyListtextView = (AutoCompleteTextView) findViewById(R.id.partylist_id);
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
//        partylist_tv = (TextView) findViewById(R.id.partylist_id);
        vicegovernor_tv = (TextView) findViewById(R.id.vgov_tv);
        Age = (EditText) findViewById(R.id.age_id);
        address = (EditText) findViewById(R.id.address_id);
        phonenumber = (EditText) findViewById(R.id.mobile_id);
        email = (EditText) findViewById(R.id.email_id);
        facebook = (EditText) findViewById(R.id.facebook_id);
        prefix_spinner = (Spinner)findViewById(R.id.prefix_spinner);
        extension_spinner = (Spinner)findViewById(R.id.emailextension_spinner);

        educ_service_spinner = (Spinner)findViewById(R.id.educ_service_spinner);
        educ_spinYear1 = (Spinner)findViewById(R.id.educ_year1_spinner);
        educ_spinPhp1 = (Spinner)findViewById(R.id.educ_php1_spinner);
        educ_spinYear2 = (Spinner)findViewById(R.id.educ_year2_spinner);
        educ_spinPhp2 = (Spinner)findViewById(R.id.educ_php2_spinner);
        educ_spinYear3 = (Spinner)findViewById(R.id.educ_year3_spinner);
        educ_spinPhp3 = (Spinner)findViewById(R.id.educ_php3_spinner);
        educ_spinYear4 = (Spinner)findViewById(R.id.educ_year4_spinner);
        educ_spinPhp4 = (Spinner)findViewById(R.id.educ_php4_spinner);

        medical_service_spinner = (Spinner)findViewById(R.id.medical_service_spinner);
        medical_spinYear1 = (Spinner)findViewById(R.id.medical_year1_spinner);
        medical_spinPhp1 = (Spinner)findViewById(R.id.medical_php1_spinner);
        medical_spinYear2 = (Spinner)findViewById(R.id.medical_year2_spinner);
        medical_spinPhp2 = (Spinner)findViewById(R.id.medical_php2_spinner);
        medical_spinYear3 = (Spinner)findViewById(R.id.medical_year3_spinner);
        medical_spinPhp3 = (Spinner)findViewById(R.id.medical_php3_spinner);
        medical_spinYear4 = (Spinner)findViewById(R.id.medical_year4_spinner);
        medical_spinPhp4 = (Spinner)findViewById(R.id.medical_php4_spinner);

        agriculture_service_spinner = (Spinner)findViewById(R.id.agriculture_service_spinner);
        agriculture_spinYear1 = (Spinner)findViewById(R.id.agriculture_year1_spinner);
        agriculture_spinPhp1 = (Spinner)findViewById(R.id.agriculture_php1_spinner);
        agriculture_spinYear2 = (Spinner)findViewById(R.id.agriculture_year2_spinner);
        agriculture_spinPhp2 = (Spinner)findViewById(R.id.agriculture_php2_spinner);
        agriculture_spinYear3 = (Spinner)findViewById(R.id.agriculture_year3_spinner);
        agriculture_spinPhp3 = (Spinner)findViewById(R.id.agriculture_php3_spinner);
        agriculture_spinYear4 = (Spinner)findViewById(R.id.agriculture_year4_spinner);
        agriculture_spinPhp4 = (Spinner)findViewById(R.id.agriculture_php4_spinner);

        cooperative_service_spinner = (Spinner)findViewById(R.id.cooperative_service_spinner);
        cooperative_spinYear1 = (Spinner)findViewById(R.id.cooperative_year1_spinner);
        cooperative_spinPhp1 = (Spinner)findViewById(R.id.cooperative_php1_spinner);
        cooperative_spinYear2 = (Spinner)findViewById(R.id.cooperative_year2_spinner);
        cooperative_spinPhp2 = (Spinner)findViewById(R.id.cooperative_php2_spinner);
        cooperative_spinYear3 = (Spinner)findViewById(R.id.cooperative_year3_spinner);
        cooperative_spinPhp3 = (Spinner)findViewById(R.id.cooperative_php3_spinner);
        cooperative_spinYear4 = (Spinner)findViewById(R.id.cooperative_year4_spinner);
        cooperative_spinPhp4 = (Spinner)findViewById(R.id.cooperative_php4_spinner);

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
            runQuestionForm();
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
                                "","","","","","","","","",
                                "","","","","","","",
                                "","","","","","","","",
                                "","","","","","","","",
                                "","","","","","","","",
                                "","");
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
                                            encodername,"","","","","","","","",
                                            "","","","","","","",
                                            "","","","","","","","",
                                            "","","","","","","","",
                                            "","","","","","","","",
                                            "","");
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
                runQuestionForm();
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

    public void runQuestionForm(){
        progressDialog = new ProgressDialog(QuestionForm_new.this);
        progressDialog.setMessage("Loading Questions...");
        progressDialog.show();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                QuestionForm(votersname_form);
                submitForm();
                form4PrevButton();
            }
        },1000);
    }

    public void QuestionForm(String votersName){
        DatabaseAccess databaseAccessInfo = DatabaseAccess.getInstance(this, "voters.db");
        databaseAccessInfo.open();
        for (int x = 4; x < 63; x++) {
            String StringList = databaseAccessInfo.getList(votersName, city_form, barangay_form, precinct_form, "VotersName=? AND City_Municipality=? AND Barangay=? AND Precinct=?", null, x);
            if (x == 4 || x == 5 || x >= 10) {
                listahan.add(StringList);
            }
        }

        databaseAccessInfo.close();
        ListArr = listahan.toArray(new String[listahan.size()]);

        address.setText(ListArr[0]);
        facebook.setText(ListArr[1]);
//        PartyListtextView.setText(ListArr[13]);
        PartyListtextView.setVisibility(View.GONE);
//        partylist_tv.setVisibility(View.GONE);

        String bday = ListArr[17];
        String age = ListArr[18];
        BirthDatePicker(bday,age);

        String substr_phone="";
        String substr_prefix="";
        if(!ListArr[16].isEmpty()){
            substr_phone=ListArr[16].substring(4,11);
            substr_prefix=ListArr[16].substring(0,4);
        }
        phonenumber.setText(substr_phone);
        String[] prefixArr = getResources().getStringArray(R.array.prefix);
        ArrayAdapter<String> adapterPrefix = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, prefixArr);
        prefix_spinner.setAdapter(adapterPrefix);
        prefix_spinner.setSelection(adapterPrefix.getPosition(substr_prefix));


        String substr_email="";
        String substr_extension="";
        if(!ListArr[19].isEmpty()){
            String[] separated = ListArr[19].split("@");
            substr_email=separated[0];
            substr_extension=separated[1];

        }
        email.setText(substr_email);
        String[] extensionArr = getResources().getStringArray(R.array.email_extension);
        ArrayAdapter<String> adapterExtension = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, extensionArr);
        extension_spinner.setAdapter(adapterExtension);
        extension_spinner.setSelection(adapterExtension.getPosition("@"+substr_extension));

        String[] educArr = getResources().getStringArray(R.array.Highest_education);
        ArrayAdapter<String> adapterEducation = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,android.R.id.text1,educArr)
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
        adapterEducation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        education_spinner.setAdapter(adapterEducation);
        education_spinner.setSelection(adapterEducation.getPosition(ListArr[20]));

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
        income_spinner.setSelection(adapterIncome.getPosition(ListArr[21]));

        String[] govArr = getResources().getStringArray(R.array.QuezProv_ch_governor);
        ArrayAdapter<String> adapterGovernor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, govArr);
        gov_spinner.setAdapter(adapterGovernor);
            for (int i = 0; i < govArr.length; i++) {
                String[] splitshoice = govArr[i].split(",");
                if (ListArr[2].equals(splitshoice[0])) {
                    gov_spinner.setSelection(adapterGovernor.getPosition(govArr[i]));
                }
            }

        String[] vgovArr = getResources().getStringArray(R.array.QuezProv_ch_vicegovernor);
        ArrayAdapter<String> adapterViceGovernor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vgovArr);
        vgov_spinner.setAdapter(adapterViceGovernor);
            for (int i = 0; i < vgovArr.length; i++) {
                String[] splitshoice = vgovArr[i].split(",");
                if (ListArr[3].equals(splitshoice[0])) {
                    vgov_spinner.setSelection(adapterViceGovernor.getPosition(vgovArr[i]));
                }
            }

        String[] congArr = getResources().getStringArray(R.array.QuezProv_ch_2ndcongressman);
        ArrayAdapter<String> adapterCongressman = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, congArr);
        cong_spinner.setAdapter(adapterCongressman);
            for (int i = 0; i < congArr.length; i++) {
                String[] splitshoice = congArr[i].split("[,\\s]");

                if (ListArr[4].equals(splitshoice[0])) {
                    cong_spinner.setSelection(adapterCongressman.getPosition(congArr[i]));
                } else if (ListArr[4].equals("SUAREZ_AMADEO") || ListArr[4].equals("SUAREZ_DAVID")) {
                    String[] splitListArr = ListArr[4].split("_");
                    String Combine = splitListArr[0] + ", " + splitListArr[1];
                    if (congArr[i].equals(Combine)) {
                        cong_spinner.setSelection(adapterCongressman.getPosition(congArr[i]));
                    }
                }
            }

//        String[] partylist = getResources().getStringArray(R.array.Party_list);
//        // Create the adapter and set it to the AutoCompleteTextView
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, partylist);
//        PartyListtextView.setAdapter(adapter);
//        PartyListtextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
//                InputMethodManager imn = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                imn.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
//            }
//        });

        choiceLucenaMayor = getResources().getStringArray(R.array.QuezProv_lucena_ch_mayor);
        choiceSariayaMayor = getResources().getStringArray(R.array.QuezProv_sariaya_ch_mayor);
        choiceCandelariaMayor = getResources().getStringArray(R.array.QuezProv_candelaria_ch_mayor);
        choiceDoloresMayor = getResources().getStringArray(R.array.QuezProv_dolores_ch_mayor);
        choiceTiaongMayor = getResources().getStringArray(R.array.QuezProv_tiaong_ch_mayor);
        choiceStAntonioMayor = getResources().getStringArray(R.array.QuezProv_sanantonio_ch_mayor);

        choiceLucenaViceMayor = getResources().getStringArray(R.array.QuezProv_lucena_ch_vicemayor);
        choiceSariayaViceMayor = getResources().getStringArray(R.array.QuezProv_sariaya_ch_vicemayor);
        choiceCandelariaViceMayor = getResources().getStringArray(R.array.QuezProv_candelaria_ch_vicemayor);
        choiceDoloresViceMayor = getResources().getStringArray(R.array.QuezProv_dolores_ch_vicemayor);
        choiceTiaongViceMayor = getResources().getStringArray(R.array.QuezProv_tiaong_ch_vicemayor);
        choiceStAntonioViceMayor = getResources().getStringArray(R.array.QuezProv_sanantonio_ch_vicemayor);

        if (city_form.equals("Lucena City")){
            mayorArr = choiceLucenaMayor;
            vicemayorArr = choiceLucenaViceMayor;
        }
        else if (city_form.equals("Sariaya")){
            mayorArr = choiceSariayaMayor;
            vicemayorArr = choiceSariayaViceMayor;
        }
        else if (city_form.equals("Candelaria")){
            mayorArr = choiceCandelariaMayor;
            vicemayorArr = choiceCandelariaViceMayor;
        }
        else if (city_form.equals("Dolores")){
            mayorArr = choiceDoloresMayor;
            vicemayorArr = choiceDoloresViceMayor;
        }
        else if (city_form.equals("Tiaong")){
            mayorArr = choiceTiaongMayor;
            vicemayorArr = choiceTiaongViceMayor;
        }
        else if (city_form.equals("San Antonio")){
            mayorArr = choiceStAntonioMayor;
            vicemayorArr = choiceStAntonioViceMayor;
        }

        TextView mayor_tv = (TextView) findViewById(R.id.mayor_tv);
        mayor_tv.setText("Mayor ("+city_form+")");
        ArrayAdapter<String> adapterMayor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mayorArr);
        mayor_spinner.setAdapter(adapterMayor);
            for (int i = 0; i < mayorArr.length; i++) {
                String[] splitshoice = mayorArr[i].split(",");
                if (ListArr[5].equals(splitshoice[0])) {
                    mayor_spinner.setSelection(adapterMayor.getPosition(mayorArr[i]));
                }
            }

        TextView vicemayor_tv = (TextView) findViewById(R.id.vicemayor_tv);
        vicemayor_tv.setText("Vice Mayor ("+city_form+")");
        ArrayAdapter<String> adapterViceMayor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vicemayorArr);
        vicemayor_spinner.setAdapter(adapterViceMayor);
        for (int i = 0; i < vicemayorArr.length; i++) {
            String[] splitshoice = vicemayorArr[i].split(",");
            if (ListArr[22].equals(splitshoice[0])) {
                vicemayor_spinner.setSelection(adapterViceMayor.getPosition(vicemayorArr[i]));
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
        int y = 6;
        for (int i = 0; i < bokalArr.length; i++) {
            rcheck_bokal[i] = new AppCompatCheckBox(this);
            rcheck_bokal[i].setTextSize(18);
            rcheck_bokal[i].setText(bokalArr[i]);
            rcheck_bokal[i].setId(i);
                Boolean kgwd = Boolean.valueOf(ListArr[y]);
                if (kgwd == true) {
                    lenghtCount++;
                }

                rcheck_bokal[i].setChecked(kgwd);
            rcheck_bokal[i].setOnCheckedChangeListener(checker);
            mLinearLayout.addView(rcheck_bokal[i]);
            y++;
        }

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
            years.add("");
        for (int i = 2016; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }

        String[] yearsArr = years.toArray(new String[years.size()]);
        String[] phpArr = getResources().getStringArray(R.array.cost_of_service);
        String[] serviceArr = getResources().getStringArray(R.array.service);

        ArrayAdapter<String> adapterEduc_Service = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, serviceArr);
        educ_service_spinner.setAdapter(adapterEduc_Service);
        educ_service_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String spinnerchoice = educ_service_spinner.getSelectedItem().toString();

                if (spinnerchoice.equals("SERVICE 1")){
                    educ_spinYear1.setVisibility(View.VISIBLE);
                    educ_spinPhp1.setVisibility(View.VISIBLE);
                    educ_spinYear2.setVisibility(View.GONE);
                    educ_spinPhp2.setVisibility(View.GONE);
                    educ_spinYear3.setVisibility(View.GONE);
                    educ_spinPhp3.setVisibility(View.GONE);
                    educ_spinYear4.setVisibility(View.GONE);
                    educ_spinPhp4.setVisibility(View.GONE);
                }
                else if(spinnerchoice.equals("SERVICE 2")){
                    educ_spinYear1.setVisibility(View.GONE);
                    educ_spinPhp1.setVisibility(View.GONE);
                    educ_spinYear2.setVisibility(View.VISIBLE);
                    educ_spinPhp2.setVisibility(View.VISIBLE);
                    educ_spinYear3.setVisibility(View.GONE);
                    educ_spinPhp3.setVisibility(View.GONE);
                    educ_spinYear4.setVisibility(View.GONE);
                    educ_spinPhp4.setVisibility(View.GONE);
                }
                else if(spinnerchoice.equals("SERVICE 3")){
                    educ_spinYear1.setVisibility(View.GONE);
                    educ_spinPhp1.setVisibility(View.GONE);
                    educ_spinYear2.setVisibility(View.GONE);
                    educ_spinPhp2.setVisibility(View.GONE);
                    educ_spinYear3.setVisibility(View.VISIBLE);
                    educ_spinPhp3.setVisibility(View.VISIBLE);
                    educ_spinYear4.setVisibility(View.GONE);
                    educ_spinPhp4.setVisibility(View.GONE);
                }
                else if(spinnerchoice.equals("SERVICE 4")){
                    educ_spinYear1.setVisibility(View.GONE);
                    educ_spinPhp1.setVisibility(View.GONE);
                    educ_spinYear2.setVisibility(View.GONE);
                    educ_spinPhp2.setVisibility(View.GONE);
                    educ_spinYear3.setVisibility(View.GONE);
                    educ_spinPhp3.setVisibility(View.GONE);
                    educ_spinYear4.setVisibility(View.VISIBLE);
                    educ_spinPhp4.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapterEduc_Year1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        educ_spinYear1.setAdapter(adapterEduc_Year1);
        educ_spinYear1.setSelection(adapterEduc_Year1.getPosition(ListArr[23]));
        ArrayAdapter<String> adapterEduc_Year2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        educ_spinYear2.setAdapter(adapterEduc_Year2);
        educ_spinYear2.setSelection(adapterEduc_Year2.getPosition(ListArr[31]));
        ArrayAdapter<String> adapterEduc_Year3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        educ_spinYear3.setAdapter(adapterEduc_Year3);
        educ_spinYear3.setSelection(adapterEduc_Year3.getPosition(ListArr[39]));
        ArrayAdapter<String> adapterEduc_Year4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        educ_spinYear4.setAdapter(adapterEduc_Year4);
        educ_spinYear4.setSelection(adapterEduc_Year4.getPosition(ListArr[47]));

        ArrayAdapter<String> adapterEduc_Php1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        educ_spinPhp1.setAdapter(adapterEduc_Php1);
        educ_spinPhp1.setSelection(adapterEduc_Php1.getPosition(ListArr[24]));
        ArrayAdapter<String> adapterEduc_Php2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        educ_spinPhp2.setAdapter(adapterEduc_Php2);
        educ_spinPhp2.setSelection(adapterEduc_Php2.getPosition(ListArr[32]));
        ArrayAdapter<String> adapterEduc_Php3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        educ_spinPhp3.setAdapter(adapterEduc_Php3);
        educ_spinPhp3.setSelection(adapterEduc_Php3.getPosition(ListArr[40]));
        ArrayAdapter<String> adapterEduc_Php4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        educ_spinPhp4.setAdapter(adapterEduc_Php4);
        educ_spinPhp4.setSelection(adapterEduc_Php4.getPosition(ListArr[48]));

        ArrayAdapter<String> adapterMedicalService = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, serviceArr);
        medical_service_spinner.setAdapter(adapterMedicalService);
        medical_service_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String spinnerchoice = medical_service_spinner.getSelectedItem().toString();

                if (spinnerchoice.equals("SERVICE 1")){
                    medical_spinYear1.setVisibility(View.VISIBLE);
                    medical_spinPhp1.setVisibility(View.VISIBLE);
                    medical_spinYear2.setVisibility(View.GONE);
                    medical_spinPhp2.setVisibility(View.GONE);
                    medical_spinYear3.setVisibility(View.GONE);
                    medical_spinPhp3.setVisibility(View.GONE);
                    medical_spinYear4.setVisibility(View.GONE);
                    medical_spinPhp4.setVisibility(View.GONE);
                }
                else if(spinnerchoice.equals("SERVICE 2")){
                    medical_spinYear1.setVisibility(View.GONE);
                    medical_spinPhp1.setVisibility(View.GONE);
                    medical_spinYear2.setVisibility(View.VISIBLE);
                    medical_spinPhp2.setVisibility(View.VISIBLE);
                    medical_spinYear3.setVisibility(View.GONE);
                    medical_spinPhp3.setVisibility(View.GONE);
                    medical_spinYear4.setVisibility(View.GONE);
                    medical_spinPhp4.setVisibility(View.GONE);
                }
                else if(spinnerchoice.equals("SERVICE 3")){
                    medical_spinYear1.setVisibility(View.GONE);
                    medical_spinPhp1.setVisibility(View.GONE);
                    medical_spinYear2.setVisibility(View.GONE);
                    medical_spinPhp2.setVisibility(View.GONE);
                    medical_spinYear3.setVisibility(View.VISIBLE);
                    medical_spinPhp3.setVisibility(View.VISIBLE);
                    medical_spinYear4.setVisibility(View.GONE);
                    medical_spinPhp4.setVisibility(View.GONE);
                }
                else if(spinnerchoice.equals("SERVICE 4")){
                    medical_spinYear1.setVisibility(View.GONE);
                    medical_spinPhp1.setVisibility(View.GONE);
                    medical_spinYear2.setVisibility(View.GONE);
                    medical_spinPhp2.setVisibility(View.GONE);
                    medical_spinYear3.setVisibility(View.GONE);
                    medical_spinPhp3.setVisibility(View.GONE);
                    medical_spinYear4.setVisibility(View.VISIBLE);
                    medical_spinPhp4.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> adapterMedical_Year1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        medical_spinYear1.setAdapter(adapterMedical_Year1);
        medical_spinYear1.setSelection(adapterMedical_Year1.getPosition(ListArr[25]));
        ArrayAdapter<String> adapterMedical_Year2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        medical_spinYear2.setAdapter(adapterMedical_Year2);
        medical_spinYear2.setSelection(adapterMedical_Year2.getPosition(ListArr[33]));
        ArrayAdapter<String> adapterMedical_Year3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        medical_spinYear3.setAdapter(adapterMedical_Year3);
        medical_spinYear3.setSelection(adapterMedical_Year3.getPosition(ListArr[41]));
        ArrayAdapter<String> adapterMedical_Year4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        medical_spinYear4.setAdapter(adapterMedical_Year4);
        medical_spinYear4.setSelection(adapterMedical_Year4.getPosition(ListArr[49]));

        ArrayAdapter<String> adapterMedical_Php1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        medical_spinPhp1.setAdapter(adapterMedical_Php1);
        medical_spinPhp1.setSelection(adapterMedical_Php1.getPosition(ListArr[26]));
        ArrayAdapter<String> adapterMedical_Php2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        medical_spinPhp2.setAdapter(adapterMedical_Php2);
        medical_spinPhp2.setSelection(adapterMedical_Php2.getPosition(ListArr[34]));
        ArrayAdapter<String> adapterMedical_Php3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        medical_spinPhp3.setAdapter(adapterMedical_Php3);
        medical_spinPhp3.setSelection(adapterMedical_Php3.getPosition(ListArr[42]));
        ArrayAdapter<String> adapterMedical_Php4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        medical_spinPhp4.setAdapter(adapterMedical_Php4);
        medical_spinPhp4.setSelection(adapterMedical_Php4.getPosition(ListArr[50]));

        ArrayAdapter<String> adapterAgriService = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, serviceArr);
        agriculture_service_spinner.setAdapter(adapterAgriService);
        agriculture_service_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String spinnerchoice = agriculture_service_spinner.getSelectedItem().toString();

                if (spinnerchoice.equals("SERVICE 1")){
                    agriculture_spinYear1.setVisibility(View.VISIBLE);
                    agriculture_spinPhp1.setVisibility(View.VISIBLE);
                    agriculture_spinYear2.setVisibility(View.GONE);
                    agriculture_spinPhp2.setVisibility(View.GONE);
                    agriculture_spinYear3.setVisibility(View.GONE);
                    agriculture_spinPhp3.setVisibility(View.GONE);
                    agriculture_spinYear4.setVisibility(View.GONE);
                    agriculture_spinPhp4.setVisibility(View.GONE);
                }
                else if(spinnerchoice.equals("SERVICE 2")){
                    agriculture_spinYear1.setVisibility(View.GONE);
                    agriculture_spinPhp1.setVisibility(View.GONE);
                    agriculture_spinYear2.setVisibility(View.VISIBLE);
                    agriculture_spinPhp2.setVisibility(View.VISIBLE);
                    agriculture_spinYear3.setVisibility(View.GONE);
                    agriculture_spinPhp3.setVisibility(View.GONE);
                    agriculture_spinYear4.setVisibility(View.GONE);
                    agriculture_spinPhp4.setVisibility(View.GONE);
                }
                else if(spinnerchoice.equals("SERVICE 3")){
                    agriculture_spinYear1.setVisibility(View.GONE);
                    agriculture_spinPhp1.setVisibility(View.GONE);
                    agriculture_spinYear2.setVisibility(View.GONE);
                    agriculture_spinPhp2.setVisibility(View.GONE);
                    agriculture_spinYear3.setVisibility(View.VISIBLE);
                    agriculture_spinPhp3.setVisibility(View.VISIBLE);
                    agriculture_spinYear4.setVisibility(View.GONE);
                    agriculture_spinPhp4.setVisibility(View.GONE);
                }
                else if(spinnerchoice.equals("SERVICE 4")){
                    agriculture_spinYear1.setVisibility(View.GONE);
                    agriculture_spinPhp1.setVisibility(View.GONE);
                    agriculture_spinYear2.setVisibility(View.GONE);
                    agriculture_spinPhp2.setVisibility(View.GONE);
                    agriculture_spinYear3.setVisibility(View.GONE);
                    agriculture_spinPhp3.setVisibility(View.GONE);
                    agriculture_spinYear4.setVisibility(View.VISIBLE);
                    agriculture_spinPhp4.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> adapterAgriculture_Year1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        agriculture_spinYear1.setAdapter(adapterAgriculture_Year1);
        agriculture_spinYear1.setSelection(adapterAgriculture_Year1.getPosition(ListArr[27]));
        ArrayAdapter<String> adapterAgriculture_Year2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        agriculture_spinYear2.setAdapter(adapterAgriculture_Year2);
        agriculture_spinYear2.setSelection(adapterAgriculture_Year2.getPosition(ListArr[35]));
        ArrayAdapter<String> adapterAgriculture_Year3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        agriculture_spinYear3.setAdapter(adapterAgriculture_Year3);
        agriculture_spinYear3.setSelection(adapterAgriculture_Year3.getPosition(ListArr[43]));
        ArrayAdapter<String> adapterAgriculture_Year4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        agriculture_spinYear4.setAdapter(adapterAgriculture_Year4);
        agriculture_spinYear4.setSelection(adapterAgriculture_Year4.getPosition(ListArr[51]));

        ArrayAdapter<String> adapterAgri_Php1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        agriculture_spinPhp1.setAdapter(adapterAgri_Php1);
        agriculture_spinPhp1.setSelection(adapterAgri_Php1.getPosition(ListArr[28]));
        ArrayAdapter<String> adapterAgri_Php2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        agriculture_spinPhp2.setAdapter(adapterAgri_Php2);
        agriculture_spinPhp2.setSelection(adapterAgri_Php2.getPosition(ListArr[36]));
        ArrayAdapter<String> adapterAgri_Php3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        agriculture_spinPhp3.setAdapter(adapterAgri_Php3);
        agriculture_spinPhp3.setSelection(adapterAgri_Php3.getPosition(ListArr[44]));
        ArrayAdapter<String> adapterAgri_Php4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        agriculture_spinPhp4.setAdapter(adapterAgri_Php4);
        agriculture_spinPhp4.setSelection(adapterAgri_Php4.getPosition(ListArr[52]));

        ArrayAdapter<String> adapterCoopService = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, serviceArr);
        cooperative_service_spinner.setAdapter(adapterCoopService);
        cooperative_service_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String spinnerchoice = cooperative_service_spinner.getSelectedItem().toString();

                if (spinnerchoice.equals("SERVICE 1")){
                    cooperative_spinYear1.setVisibility(View.VISIBLE);
                    cooperative_spinPhp1.setVisibility(View.VISIBLE);
                    cooperative_spinYear2.setVisibility(View.GONE);
                    cooperative_spinPhp2.setVisibility(View.GONE);
                    cooperative_spinYear3.setVisibility(View.GONE);
                    cooperative_spinPhp3.setVisibility(View.GONE);
                    cooperative_spinYear4.setVisibility(View.GONE);
                    cooperative_spinPhp4.setVisibility(View.GONE);
                }
                else if(spinnerchoice.equals("SERVICE 2")){
                    cooperative_spinYear1.setVisibility(View.GONE);
                    cooperative_spinPhp1.setVisibility(View.GONE);
                    cooperative_spinYear2.setVisibility(View.VISIBLE);
                    cooperative_spinPhp2.setVisibility(View.VISIBLE);
                    cooperative_spinYear3.setVisibility(View.GONE);
                    cooperative_spinPhp3.setVisibility(View.GONE);
                    cooperative_spinYear4.setVisibility(View.GONE);
                    cooperative_spinPhp4.setVisibility(View.GONE);
                }
                else if(spinnerchoice.equals("SERVICE 3")){
                    cooperative_spinYear1.setVisibility(View.GONE);
                    cooperative_spinPhp1.setVisibility(View.GONE);
                    cooperative_spinYear2.setVisibility(View.GONE);
                    cooperative_spinPhp2.setVisibility(View.GONE);
                    cooperative_spinYear3.setVisibility(View.VISIBLE);
                    cooperative_spinPhp3.setVisibility(View.VISIBLE);
                    cooperative_spinYear4.setVisibility(View.GONE);
                    cooperative_spinPhp4.setVisibility(View.GONE);
                }
                else if(spinnerchoice.equals("SERVICE 4")){
                    cooperative_spinYear1.setVisibility(View.GONE);
                    cooperative_spinPhp1.setVisibility(View.GONE);
                    cooperative_spinYear2.setVisibility(View.GONE);
                    cooperative_spinPhp2.setVisibility(View.GONE);
                    cooperative_spinYear3.setVisibility(View.GONE);
                    cooperative_spinPhp3.setVisibility(View.GONE);
                    cooperative_spinYear4.setVisibility(View.VISIBLE);
                    cooperative_spinPhp4.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> adapterCooperative_Year1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        cooperative_spinYear1.setAdapter(adapterCooperative_Year1);
        cooperative_spinYear1.setSelection(adapterCooperative_Year1.getPosition(ListArr[29]));
        ArrayAdapter<String> adapterCooperative_Year2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        cooperative_spinYear2.setAdapter(adapterCooperative_Year2);
        cooperative_spinYear2.setSelection(adapterCooperative_Year2.getPosition(ListArr[37]));
        ArrayAdapter<String> adapterCooperative_Year3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        cooperative_spinYear3.setAdapter(adapterCooperative_Year3);
        cooperative_spinYear3.setSelection(adapterCooperative_Year3.getPosition(ListArr[45]));
        ArrayAdapter<String> adapterCooperative_Year4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearsArr);
        cooperative_spinYear4.setAdapter(adapterCooperative_Year4);
        cooperative_spinYear4.setSelection(adapterCooperative_Year4.getPosition(ListArr[53]));

        ArrayAdapter<String> adapterCoop_Php1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        cooperative_spinPhp1.setAdapter(adapterCoop_Php1);
        cooperative_spinPhp1.setSelection(adapterCoop_Php1.getPosition(ListArr[30]));
        ArrayAdapter<String> adapterCoop_Php2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        cooperative_spinPhp2.setAdapter(adapterCoop_Php2);
        cooperative_spinPhp2.setSelection(adapterCoop_Php2.getPosition(ListArr[38]));
        ArrayAdapter<String> adapterCoop_Php3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        cooperative_spinPhp3.setAdapter(adapterCoop_Php3);
        cooperative_spinPhp3.setSelection(adapterCoop_Php3.getPosition(ListArr[46]));
        ArrayAdapter<String> adapterCoop_Php4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phpArr);
        cooperative_spinPhp4.setAdapter(adapterCoop_Php4);
        cooperative_spinPhp4.setSelection(adapterCoop_Php4.getPosition(ListArr[54]));

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
                if (!bdstring.isEmpty()){
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
                final String[] leaderArr = getResources().getStringArray(R.array.barangay_leader);
                AlertDialog.Builder setbuilder = new AlertDialog.Builder(QuestionForm_new.this);
                setbuilder.setTitle("Choose Barangay or Precinct leader");

                //this will checked the item when user open the dialog
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
                            progressDialog = new ProgressDialog(QuestionForm_new.this);
                            progressDialog.setMessage("Saving Data...");
                            progressDialog.show();

                            String address_submit = address.getText().toString();
                            String stringBirthday = mDisplayDate.getText().toString();
                            String age_submit = Age.getText().toString();
                            String email_submit = email.getText().toString() +  extension_spinner.getSelectedItem().toString();
                            String contentPhone = prefix_spinner.getSelectedItem().toString() + phonenumber.getText().toString();
                            String facebook_submit = facebook.getText().toString();
//                            String PartyList = PartyListtextView.getText().toString();
                            String PartyList = "";
                            String high_education = education_spinner.getSelectedItem().toString();
                            String annual_income = income_spinner.getSelectedItem().toString();
                            String governor = gov_spinner.getSelectedItem().toString();
                            String vice_governor = vgov_spinner.getSelectedItem().toString();
                            String congressman = cong_spinner.getSelectedItem().toString();
                            String mayor = mayor_spinner.getSelectedItem().toString();
                            String vicemayor = vicemayor_spinner.getSelectedItem().toString();

                            String education_scholarship_year1 = educ_spinYear1.getSelectedItem().toString();
                            String education_scholarship_php1 = educ_spinPhp1.getSelectedItem().toString();
                            String medical_assistance_year1 = medical_spinYear1.getSelectedItem().toString();
                            String medical_assistance_php1 = medical_spinPhp1.getSelectedItem().toString();
                            String agriculture_year1 = agriculture_spinYear1.getSelectedItem().toString();
                            String agriculture_php1 = agriculture_spinPhp1.getSelectedItem().toString();
                            String cooperative_year1 = cooperative_spinYear1.getSelectedItem().toString();
                            String cooperative_php1 = cooperative_spinPhp1.getSelectedItem().toString();

                            String education_scholarship_year2 = educ_spinYear2.getSelectedItem().toString();
                            String education_scholarship_php2 = educ_spinPhp2.getSelectedItem().toString();
                            String medical_assistance_year2 = medical_spinYear2.getSelectedItem().toString();
                            String medical_assistance_php2 = medical_spinPhp2.getSelectedItem().toString();
                            String agriculture_year2 = agriculture_spinYear2.getSelectedItem().toString();
                            String agriculture_php2 = agriculture_spinPhp2.getSelectedItem().toString();
                            String cooperative_year2 = cooperative_spinYear2.getSelectedItem().toString();
                            String cooperative_php2 = cooperative_spinPhp2.getSelectedItem().toString();

                            String education_scholarship_year3 = educ_spinYear3.getSelectedItem().toString();
                            String education_scholarship_php3 = educ_spinPhp3.getSelectedItem().toString();
                            String medical_assistance_year3 = medical_spinYear3.getSelectedItem().toString();
                            String medical_assistance_php3 = medical_spinPhp3.getSelectedItem().toString();
                            String agriculture_year3 = agriculture_spinYear3.getSelectedItem().toString();
                            String agriculture_php3 = agriculture_spinPhp3.getSelectedItem().toString();
                            String cooperative_year3 = cooperative_spinYear3.getSelectedItem().toString();
                            String cooperative_php3 = cooperative_spinPhp3.getSelectedItem().toString();

                            String education_scholarship_year4 = educ_spinYear4.getSelectedItem().toString();
                            String education_scholarship_php4 = educ_spinPhp4.getSelectedItem().toString();
                            String medical_assistance_year4 = medical_spinYear4.getSelectedItem().toString();
                            String medical_assistance_php4 = medical_spinPhp4.getSelectedItem().toString();
                            String agriculture_year4 = agriculture_spinYear4.getSelectedItem().toString();
                            String agriculture_php4 = agriculture_spinPhp4.getSelectedItem().toString();
                            String cooperative_year4 = cooperative_spinYear4.getSelectedItem().toString();
                            String cooperative_php4 = cooperative_spinPhp4.getSelectedItem().toString();

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
                                String[] nameListvicemayor = vicemayor.split(",");
                                String[] nameListgovernor = governor.split(",");
                                String[] nameListvicegovernor = vice_governor.split(",");

                                savingtoDatabase(votersname_form, nameListgovernor[0], nameListvicegovernor[0], save_congressman, nameListmayor[0],alcalaBokal,
                                        alejandrinoBokal, casullaBokal, gonzalesBokal, liwanagBokal, sioBokal, talagaBokal, tanadaBokal,indicator_on_off, deceased_yes_no,
                                        PartyList, encodername,contentPhone,address_submit,stringBirthday,age_submit,email_submit,facebook_submit,high_education,annual_income,nameListvicemayor[0],
                                        education_scholarship_year1,education_scholarship_php1,medical_assistance_year1,medical_assistance_php1,agriculture_year1,agriculture_php1,cooperative_year1,cooperative_php1,
                                        education_scholarship_year2,education_scholarship_php2,medical_assistance_year2,medical_assistance_php2,agriculture_year2,agriculture_php2,cooperative_year2,cooperative_php2,
                                        education_scholarship_year3,education_scholarship_php3,medical_assistance_year3,medical_assistance_php3,agriculture_year3,agriculture_php3,cooperative_year3,cooperative_php3,
                                        education_scholarship_year4,education_scholarship_php4,medical_assistance_year4,medical_assistance_php4,agriculture_year4,agriculture_php4,cooperative_year4,cooperative_php4);
//                            }

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

    public void savingtoDatabase(String voters,String governor,String vicegovernor,String congressman,String mayor,String alcalaBokal,String alejandrinoBokal, String casullaBokal,String gonzalesBokal,
                                 String liwanagBokal,String sioBokal,String talagaBokal,String tanadaBokal,String indicator_on_off,String deceased_yes_no, String partylist,String encodername,
                                 String contact,String address_save,String birthday_save,String age_save,String email_save,String facebook_save, String education_save,String income_save,String vicemayor,
                                 String educ_scholar_year1,String educ_scholar_php1,String medical_assistance_year1,String medical_assistance_php1,String agriculture_year1,String agriculture_php1,String cooperative_year1,String cooperative_php1,
                                 String educ_scholar_year2,String educ_scholar_php2,String medical_assistance_year2,String medical_assistance_php2,String agriculture_year2,String agriculture_php2,String cooperative_year2,String cooperative_php2,
                                 String educ_scholar_year3,String educ_scholar_php3,String medical_assistance_year3,String medical_assistance_php3,String agriculture_year3,String agriculture_php3,String cooperative_year3,String cooperative_php3,
                                 String educ_scholar_year4,String educ_scholar_php4,String medical_assistance_year4,String medical_assistance_php4,String agriculture_year4,String agriculture_php4,String cooperative_year4,String cooperative_php4 ){

        DatabaseAccess databaseAccessSubmit = DatabaseAccess.getInstance(QuestionForm_new.this,"voters.db");
        databaseAccessSubmit.open();

        boolean checkDuplicate = databaseAccessSubmit.checkDuplicateUpload(votersname_form);

        if(checkDuplicate == true){
            boolean submitUpdate = databaseAccessSubmit.updateData(voters,governor,vicegovernor,congressman,mayor,alcalaBokal,alejandrinoBokal,casullaBokal,gonzalesBokal,liwanagBokal, sioBokal,
                    talagaBokal,tanadaBokal,indicator_on_off,deceased_yes_no,partylist,encodername,contact,address_save,birthday_save,age_save,email_save,facebook_save,education_save,income_save,vicemayor,
                    educ_scholar_year1,educ_scholar_php1,medical_assistance_year1,medical_assistance_php1,agriculture_year1,agriculture_php1,cooperative_year1,cooperative_php1,
                    educ_scholar_year2,educ_scholar_php2,medical_assistance_year2,medical_assistance_php2,agriculture_year2,agriculture_php2,cooperative_year2,cooperative_php2,
                    educ_scholar_year3,educ_scholar_php3,medical_assistance_year3,medical_assistance_php3,agriculture_year3,agriculture_php3,cooperative_year3,cooperative_php3,
                    educ_scholar_year4,educ_scholar_php4,medical_assistance_year4,medical_assistance_php4,agriculture_year4,agriculture_php4,cooperative_year4,cooperative_php4);


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
            boolean submitInsert= databaseAccessSubmit.insertOnConflictData(voters,governor,vicegovernor,congressman,mayor,alcalaBokal,alejandrinoBokal,casullaBokal,gonzalesBokal,liwanagBokal, sioBokal,
                    talagaBokal,tanadaBokal,indicator_on_off,deceased_yes_no,partylist,encodername,contact,address_save,birthday_save,age_save,email_save,facebook_save,education_save,income_save,vicemayor,
                    educ_scholar_year1,educ_scholar_php1,medical_assistance_year1,medical_assistance_php1,agriculture_year1,agriculture_php1,cooperative_year1,cooperative_php1,
                    educ_scholar_year2,educ_scholar_php2,medical_assistance_year2,medical_assistance_php2,agriculture_year2,agriculture_php2,cooperative_year2,cooperative_php2,
                    educ_scholar_year3,educ_scholar_php3,medical_assistance_year3,medical_assistance_php3,agriculture_year3,agriculture_php3,cooperative_year3,cooperative_php3,
                    educ_scholar_year4,educ_scholar_php4,medical_assistance_year4,medical_assistance_php4,agriculture_year4,agriculture_php4,cooperative_year4,cooperative_php4);


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
