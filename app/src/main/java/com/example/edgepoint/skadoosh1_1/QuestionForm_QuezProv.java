package com.example.edgepoint.skadoosh1_1;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionForm_QuezProv extends AppCompatActivity {

    ListView simpleList;
    String[] questions,ListArr,choiceGov,choiceViceGov,choiceCongr,choiceLucenaMayor,choiceSariayaMayor,choiceCandelariaMayor
            ,choiceDoloresMayor,choiceTiaongMayor,choiceStAntonioMayor,choiceBokal,choiceMayor;
    List<String> listahan = new ArrayList<>();
    String city_form,votersname_form,barangay_form,precinct_form, congressman="", mayor="", governor="", vicegovernor="";
    Button submit;
    AlertDialog.Builder alert;
    RadioGroup rgroup_governor,rgroup_vicegovernor,rgroup_congressman,rgroup_mayor;
    AppCompatCheckBox[] rcheck_bokal;
    ProgressDialog progressDialog;
    AutoCompleteTextView PartyListtextView;
    EditText phonenumber;
    int lenghtCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_form__quez_prov);


        Intent intent = getIntent();

        Form form = intent.getParcelableExtra("Form");

        city_form = form.getCity();
        precinct_form = form.getPrecinct();
        barangay_form = form.getBarangay();
        votersname_form = form.getName();

        TextView vname_tv = (TextView) findViewById(R.id.vname_tv_id);
        vname_tv.setText("("+votersname_form+")");

        DatabaseAccess databaseAccessDeceased = DatabaseAccess.getInstance(QuestionForm_QuezProv.this,"voters.db");
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
        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(QuestionForm_QuezProv.this);
        confirmationDialog.setTitle("Restore "+votersname_form+"?");

        confirmationDialog.setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        progressDialog = new ProgressDialog(QuestionForm_QuezProv.this);
                        progressDialog.setMessage("Restoring Data...");
                        progressDialog.show();
                        savingtoDatabase(votersname_form,"","","","","","","",
                                "","","","","","off","","","","");
                        DatabaseAccess databaseAccessDeceased = DatabaseAccess.getInstance(QuestionForm_QuezProv.this,"voters.db");
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

        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(QuestionForm_QuezProv.this);
        confirmationDialog.setTitle("Is "+votersname_form+" deceased?");

        confirmationDialog.setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AlertDialog.Builder setbuilder = new AlertDialog.Builder(QuestionForm_QuezProv.this);
                        setbuilder.setTitle("Enter Encoder's Name");
                        final EditText encoder = new EditText(QuestionForm_QuezProv.this);
                        setbuilder.setView(encoder);

                        setbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                String encodername = encoder.getText().toString();

                                if (!encodername.isEmpty()){
                                    progressDialog = new ProgressDialog(QuestionForm_QuezProv.this);
                                    progressDialog.setMessage("Saving Data...");
                                    progressDialog.show();
                                    savingtoDatabase(votersname_form,"","","","","","","",
                                            "","","","","","on","yes","",encodername,"");
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
        progressDialog = new ProgressDialog(QuestionForm_QuezProv.this);
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
        DatabaseAccess databaseAccessInfo = DatabaseAccess.getInstance(this,"voters.db");
        databaseAccessInfo.open();
        for (int x=10; x<25; x++){
            String StringList = databaseAccessInfo.getList(votersName,city_form,barangay_form,precinct_form,"VotersName=? AND City_Municipality=? AND Barangay=? AND Precinct=?",null , x);
            listahan.add(StringList);
        }

        databaseAccessInfo.close();

        ListArr = listahan.toArray(new String[listahan.size()]);

        questions = getResources().getStringArray(R.array.QuezProv_questions);
        choiceGov = getResources().getStringArray(R.array.QuezProv_ch_governor);
        choiceViceGov = getResources().getStringArray(R.array.QuezProv_ch_vicegovernor);
        choiceCongr = getResources().getStringArray(R.array.QuezProv_ch_2ndcongressman);
        choiceLucenaMayor = getResources().getStringArray(R.array.QuezProv_lucena_ch_mayor);
        choiceSariayaMayor = getResources().getStringArray(R.array.QuezProv_sariaya_ch_mayor);
        choiceCandelariaMayor = getResources().getStringArray(R.array.QuezProv_candelaria_ch_mayor);
        choiceDoloresMayor = getResources().getStringArray(R.array.QuezProv_dolores_ch_mayor);
        choiceTiaongMayor = getResources().getStringArray(R.array.QuezProv_tiaong_ch_mayor);
        choiceStAntonioMayor = getResources().getStringArray(R.array.QuezProv_sanantonio_ch_mayor);
        choiceBokal = getResources().getStringArray(R.array.QuezProv_ch_bokal);

        if (city_form.equals("Lucena City")){
            choiceMayor = choiceLucenaMayor;
        }
        else if (city_form.equals("Sariaya")){
            choiceMayor = choiceSariayaMayor;
        }
        else if (city_form.equals("Candelaria")){
            choiceMayor = choiceCandelariaMayor;
        }
        else if (city_form.equals("Dolores")){
            choiceMayor = choiceDoloresMayor;
        }
        else if (city_form.equals("Tiaong")){
            choiceMayor = choiceTiaongMayor;
        }
        else if (city_form.equals("San Antonio")){
            choiceMayor = choiceStAntonioMayor;
        }

//        String str_kgwd = "";
//        for (int i = 3; i < choiceBokal.length; i++) {
//            Boolean kgwd = Boolean.valueOf( ListArr[i]);
//            str_kgwd = String.valueOf(kgwd);
//            if (str_kgwd.equals("true")){
//                lenghtCount++;
//            }
//        }
//
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

        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.qp_linear);
        for (int k = 0; k < questions.length; k++) {
            //create text button
            TextView title = new TextView(this);
            if (k == 4){
                title.setText(questions[k]+" ("+city_form+")");
            }else {
                title.setText(questions[k]);
            }
            title.setAllCaps(true);
            title.setTextSize(18);
            title.setTypeface(null, Typeface.BOLD);
            title.setTextColor(Color.parseColor("#FF0099CC"));
            mLinearLayout.addView(title);
            // create radio button

            if (k == 0){
                final AppCompatRadioButton[] rbutton_governor = new AppCompatRadioButton[choiceGov.length];
                rgroup_governor = new RadioGroup(this);
                rgroup_governor.setOrientation(RadioGroup.VERTICAL);
                for (int i = 0; i < choiceGov.length; i++) {
                    rbutton_governor[i] = new AppCompatRadioButton(this);
                    rbutton_governor[i].setTextSize(18);
                    rgroup_governor.addView(rbutton_governor[i]);
                    rbutton_governor[i].setText(choiceGov[i]);
                    String[] splitshoice = choiceGov[i].split(",");
                    if (ListArr[k].equals(splitshoice[0])){
                        rbutton_governor[i].setChecked(true);
                    }
                }
                mLinearLayout.addView(rgroup_governor);
            }

            else if (k == 1){
                final AppCompatRadioButton[] rbutton_vicegovernor = new AppCompatRadioButton[choiceViceGov.length];
                rgroup_vicegovernor = new RadioGroup(this);
                rgroup_vicegovernor.setOrientation(RadioGroup.VERTICAL);
                for (int i = 0; i < choiceViceGov.length; i++) {
                    rbutton_vicegovernor[i] = new AppCompatRadioButton(this);
                    rbutton_vicegovernor[i].setTextSize(18);
                    rgroup_vicegovernor.addView(rbutton_vicegovernor[i]);
                    rbutton_vicegovernor[i].setText(choiceViceGov[i]);
                    String[] splitshoice = choiceViceGov[i].split(",");
                    if (ListArr[k].equals(splitshoice[0])){
                        rbutton_vicegovernor[i].setChecked(true);
                    }
                }
                mLinearLayout.addView(rgroup_vicegovernor);
            }

            else if (k == 2){
                final AppCompatRadioButton[] rbutton_congressman = new AppCompatRadioButton[choiceCongr.length];
                rgroup_congressman = new RadioGroup(this);
                rgroup_congressman.setOrientation(RadioGroup.VERTICAL);
                for (int i = 0; i < choiceCongr.length; i++) {
                    rbutton_congressman[i] = new AppCompatRadioButton(this);
                    rbutton_congressman[i].setTextSize(18);
                    rgroup_congressman.addView(rbutton_congressman[i]);
                    rbutton_congressman[i].setText(choiceCongr[i]);
                    String[] splitshoice = choiceCongr[i].split("[,\\s]");

                    if (ListArr[k].equals(splitshoice[0])){
                        rbutton_congressman[i].setChecked(true);
                    }
                    else if (ListArr[k].equals("SUAREZ_AMADEO") || ListArr[k].equals("SUAREZ_DAVID")){
                        String[] splitListArr = ListArr[k].split("_");
                        String Combine = splitListArr[0]+", "+splitListArr[1];
                        if (choiceCongr[i].toString().equals(Combine)){
                            rbutton_congressman[i].setChecked(true);
                        }
                    }
                }
                mLinearLayout.addView(rgroup_congressman);
            }

            else if (k == 3){
                rcheck_bokal = new AppCompatCheckBox[choiceBokal.length];
                int y = k+1;
                for (int i = 0; i < choiceBokal.length; i++) {
                    rcheck_bokal[i] = new AppCompatCheckBox(this);
                    rcheck_bokal[i].setTextSize(18);
                    rcheck_bokal[i].setText(choiceBokal[i]);
                    rcheck_bokal[i].setId(i);
                    Boolean kgwd = Boolean.valueOf( ListArr[y]);
                    if(kgwd == true){
                        lenghtCount++;
                    }
                    rcheck_bokal[i].setChecked(kgwd);
                    rcheck_bokal[i].setOnCheckedChangeListener(checker);
                    mLinearLayout.addView(rcheck_bokal[i]);
                    y++;
                }

            }
            else if (k == 4){
                final AppCompatRadioButton[] rbutton_mayor = new AppCompatRadioButton[choiceMayor.length];
                rgroup_mayor = new RadioGroup(this);
                rgroup_mayor.setOrientation(RadioGroup.VERTICAL);
                for (int i = 0; i < choiceMayor.length; i++) {
                    rbutton_mayor[i] = new AppCompatRadioButton(this);
                    rbutton_mayor[i].setTextSize(18);
                    rgroup_mayor.addView(rbutton_mayor[i]);
                    rbutton_mayor[i].setText(choiceMayor[i]);
                    String[] splitshoice = choiceMayor[i].split(",");
                    if (ListArr[k-1].equals(splitshoice[0])){
                        rbutton_mayor[i].setChecked(true);
                    }
                }
                mLinearLayout.addView(rgroup_mayor);
            }
            else if (k == 5){
                PartyListtextView = new AutoCompleteTextView(this);
                PartyListtextView.setHintTextColor(getResources().getColor(R.color.colorHint));
                PartyListtextView.setThreshold(1);
                PartyListtextView.setText(ListArr[12]);
                PartyListtextView.setHint("Enter a party list");

                // Get the string array
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
                mLinearLayout.addView(PartyListtextView);
            }
            else if (k == 6){
                phonenumber = new EditText(this);
                phonenumber.setText(ListArr[14]);
                phonenumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                phonenumber.setHintTextColor(getResources().getColor(R.color.colorHint));
                phonenumber.setHint("Enter Phone Number");
                mLinearLayout.addView(phonenumber);
            }

        }
        progressDialog.dismiss();
    }

    public void submitForm(){
        submit = (Button) findViewById(R.id.qp_submit_id);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder setbuilder = new AlertDialog.Builder(QuestionForm_QuezProv.this);
                setbuilder.setTitle("Enter Encoder's Name");
                final EditText encoder = new EditText(QuestionForm_QuezProv.this);
                setbuilder.setView(encoder);

                setbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String encodername = encoder.getText().toString();

                        if (!encodername.isEmpty()) {

                            progressDialog = new ProgressDialog(QuestionForm_QuezProv.this);
                            progressDialog.setMessage("Saving Data...");

                            RadioButton govradio = ((RadioButton) findViewById(rgroup_governor.getCheckedRadioButtonId()));
                            RadioButton vicegovradio = ((RadioButton) findViewById(rgroup_vicegovernor.getCheckedRadioButtonId()));
                            RadioButton congradio = ((RadioButton) findViewById(rgroup_congressman.getCheckedRadioButtonId()));
                            RadioButton mayorradio = ((RadioButton) findViewById(rgroup_mayor.getCheckedRadioButtonId()));

                            String alcalaBokal = String.valueOf(rcheck_bokal[0].isChecked());
                            String alejandrinoBokal = String.valueOf(rcheck_bokal[1].isChecked());
                            String casullaBokal = String.valueOf(rcheck_bokal[2].isChecked());
                            String gonzalesBokal = String.valueOf(rcheck_bokal[3].isChecked());
                            String liwanagBokal = String.valueOf(rcheck_bokal[4].isChecked());
                            String sioBokal = String.valueOf(rcheck_bokal[5].isChecked());
                            String talagaBokal = String.valueOf(rcheck_bokal[6].isChecked());
                            String tanadaBokal = String.valueOf(rcheck_bokal[7].isChecked());

                            String PartyList = PartyListtextView.getText().toString();
                            String contentPhone = phonenumber.getText().toString();
                            String indicator_on_off = "on";
                            String deceased_yes_no = "no";

                            if (congradio == null || mayorradio == null || govradio == null || vicegovradio == null) {
                                Toast.makeText(getApplicationContext(), "Please fill-up all questions", Toast.LENGTH_LONG).show();
                            } else {
                                progressDialog.show();
                                congressman = congradio.getText().toString();
                                mayor = mayorradio.getText().toString();
                                governor = govradio.getText().toString();
                                vicegovernor = vicegovradio.getText().toString();
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
                                String[] nameListvicegovernor = vicegovernor.split(",");

                                savingtoDatabase(votersname_form, nameListgovernor[0], nameListvicegovernor[0], save_congressman, nameListmayor[0],
                                        alcalaBokal, alejandrinoBokal, casullaBokal, gonzalesBokal, liwanagBokal, sioBokal, talagaBokal, tanadaBokal,
                                        indicator_on_off, deceased_yes_no, PartyList, encodername,contentPhone);
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
                                 String partylist,String encodername,String contact){

//        DatabaseAccess databaseAccessSubmit = DatabaseAccess.getInstance(QuestionForm_QuezProv.this,"voters.db");
//        databaseAccessSubmit.open();
//
//        boolean checkDuplicate = databaseAccessSubmit.checkDuplicateUpload(votersname_form);
//
//        if(checkDuplicate == true){
//            boolean submitUpdate = databaseAccessSubmit.updateData(voters,governor,vicegovernor,congressman,mayor,alcalaBokal,alejandrinoBokal,casullaBokal,
//                    gonzalesBokal,liwanagBokal,sioBokal,talagaBokal,tanadaBokal,indicator_on_off,deceased_yes_no,partylist,encodername,contact);
//
//
//            if(submitUpdate == true) {
//                Toast.makeText(QuestionForm_QuezProv.this,"Data Saved.",Toast.LENGTH_LONG).show();
//                progressDialog.dismiss();
//                finish();
//            }
//            else {
//                Toast.makeText(QuestionForm_QuezProv.this, "Data not Save", Toast.LENGTH_LONG).show();
//                progressDialog.dismiss();
//            }
//        }
//        else {
//            boolean submitInsert= databaseAccessSubmit.insertOnConflictData(voters,governor,vicegovernor,congressman,mayor,alcalaBokal,alejandrinoBokal,
//                    casullaBokal,gonzalesBokal,liwanagBokal,sioBokal,talagaBokal,tanadaBokal,indicator_on_off,deceased_yes_no,partylist,encodername,contact);
//
//
//            if(submitInsert == true) {
//                Toast.makeText(QuestionForm_QuezProv.this,"Data Saved.",Toast.LENGTH_LONG).show();
//                finish();
//                progressDialog.dismiss();
//            }
//            else {
//                Toast.makeText(QuestionForm_QuezProv.this, "Data not Submitted", Toast.LENGTH_LONG).show();
//                progressDialog.dismiss();
//            }
//        }
//
//        databaseAccessSubmit.close();
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

