package com.example.edgepoint.skadoosh1_1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class QuestionForm extends AppCompatActivity {

    ListView simpleList;
    String[] questions,ListArr,choiceCongr,choiceMayor,choiceViceMayor,choiceKagawad;
    List<String> listahan = new ArrayList<>();
    String votersname_form, congressman="", mayor="", vicemayor="";
    Button submit;
    AlertDialog.Builder alert;
    RadioGroup rgroup_congressman,rgroup_mayor,rgroup_vicemayor;
    AppCompatCheckBox[] rcheck_kagawad;
    ProgressDialog progressDialog;
    int lenghtCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_form);

        Intent intent = getIntent();

        Form form = intent.getParcelableExtra("Form");

        votersname_form = form.getName();

        TextView vname_tv = (TextView) findViewById(R.id.vname_tv_id);
        vname_tv.setText("("+votersname_form+")");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Questions...");
        progressDialog.show();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                QuestionForm(votersname_form);
                submitForm();
                form4PrevButton();
            }
        },1500);

    }

//    public void QuestionForm(String votersName){
//        DatabaseAccess databaseAccessInfo = DatabaseAccess.getInstance(this,"voters.db");
//        databaseAccessInfo.open();
//        for (int x=7; x<26; x++){
//            String StringList = databaseAccessInfo.getList(votersName,"VotersName=?",null , x);
//            listahan.add(StringList);
//        }
//
//        databaseAccessInfo.close();
//
//        ListArr = listahan.toArray(new String[listahan.size()]);
//
//        questions = getResources().getStringArray(R.array.questions);
//        choiceCongr = getResources().getStringArray(R.array.choice_congressman);
//        choiceMayor = getResources().getStringArray(R.array.choice_mayor);
//        choiceViceMayor = getResources().getStringArray(R.array.choice_vicemayor);
//        choiceKagawad = getResources().getStringArray(R.array.choice_kagawad);
//
//        String str_kgwd = "";
//        for (int i = 3; i < choiceKagawad.length; i++) {
//            Boolean kgwd = Boolean.valueOf( ListArr[i]);
//            str_kgwd = String.valueOf(kgwd);
//            if (str_kgwd.equals("true")){
//                lenghtCount++;
//            }
//        }
//
//        CompoundButton.OnCheckedChangeListener checker = new CompoundButton.OnCheckedChangeListener(){
//            @Override
//            public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
//                if(lenghtCount > 9){
//                    cb.setChecked(false);
//                    lenghtCount=10;
//                    Toast.makeText(getApplicationContext(), "You've already choose 10 Kagawad", Toast.LENGTH_LONG).show();
//                }
//
//                if(isChecked){
//                    lenghtCount++;
////                    Toast.makeText(getApplicationContext(), lenghtCount+" oncheck", Toast.LENGTH_LONG).show();
//                }else if(!isChecked){
//                    lenghtCount--;
////                    Toast.makeText(getApplicationContext(), lenghtCount+" uncheck", Toast.LENGTH_LONG).show();
//                }
//            }
//        };
//
//        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.linear1);
//        for (int k = 0; k < 4; k++) {
//            //create text button
//            TextView title = new TextView(this);
//            title.setText(questions[k]);
//            title.setTextSize(18);
//            title.setTypeface(null, Typeface.BOLD);
//            title.setTextColor(Color.parseColor("#FF0099CC"));
//            mLinearLayout.addView(title);
//            // create radio button
//
//            if (k == 0){
//                final AppCompatRadioButton[] rbutton_congressman = new AppCompatRadioButton[4];
//                rgroup_congressman = new RadioGroup(this);
//                rgroup_congressman.setOrientation(RadioGroup.VERTICAL);
//                for (int i = 0; i < 4; i++) {
//                    rbutton_congressman[i] = new AppCompatRadioButton(this);
//                    rbutton_congressman[i].setTextSize(18);
//                    rgroup_congressman.addView(rbutton_congressman[i]);
//                    rbutton_congressman[i].setText(choiceCongr[i]);
//                    String[] splitshoice = choiceCongr[i].split(",");
//                    if (ListArr[k].equals(splitshoice[0])){
//                        rbutton_congressman[i].setChecked(true);
//                    }
//                }
//                mLinearLayout.addView(rgroup_congressman);
//            }
//            else if (k == 1){
//                final AppCompatRadioButton[] rbutton_mayor = new AppCompatRadioButton[3];
//                rgroup_mayor = new RadioGroup(this);
//                rgroup_mayor.setOrientation(RadioGroup.VERTICAL);
//                for (int i = 0; i < 3; i++) {
//                    rbutton_mayor[i] = new AppCompatRadioButton(this);
//                    rbutton_mayor[i].setTextSize(18);
//                    rgroup_mayor.addView(rbutton_mayor[i]);
//                    rbutton_mayor[i].setText(choiceMayor[i]);
//                    String[] splitshoice = choiceMayor[i].split(",");
//                    if (ListArr[k].equals(splitshoice[0])){
//                        rbutton_mayor[i].setChecked(true);
//                    }
//                }
//                mLinearLayout.addView(rgroup_mayor);
//            }
//            else if (k == 2){
//                final AppCompatRadioButton[] rbutton_vicemayor = new AppCompatRadioButton[4];
//                rgroup_vicemayor = new RadioGroup(this);
//                rgroup_vicemayor.setOrientation(RadioGroup.VERTICAL);
//                for (int i = 0; i < 4; i++) {
//                    rbutton_vicemayor[i] = new AppCompatRadioButton(this);
//                    rbutton_vicemayor[i].setTextSize(18);
//                    rgroup_vicemayor.addView(rbutton_vicemayor[i]);
//                    rbutton_vicemayor[i].setText(choiceViceMayor[i]);
//                    String[] splitshoice = choiceViceMayor[i].split(",");
//                    if (ListArr[k].equals(splitshoice[0])){
//                        rbutton_vicemayor[i].setChecked(true);
//                    }
//                }
//                mLinearLayout.addView(rgroup_vicemayor);
//            }
//            else if (k == 3){
//                rcheck_kagawad = new AppCompatCheckBox[16];
//                int y = k;
//                for (int i = 0; i < choiceKagawad.length; i++) {
//                    rcheck_kagawad[i] = new AppCompatCheckBox(this);
//                    rcheck_kagawad[i].setTextSize(18);
//                    rcheck_kagawad[i].setText(choiceKagawad[i]);
//                    rcheck_kagawad[i].setId(i);
//                    Boolean kgwd = Boolean.valueOf( ListArr[y]);
//                    rcheck_kagawad[i].setChecked(kgwd);
//                    rcheck_kagawad[i].setOnCheckedChangeListener(checker);
//                    mLinearLayout.addView(rcheck_kagawad[i]);
//                    y++;
//                }
//
//            }
//        }
//        progressDialog.dismiss();
//    }

    public void submitForm(){
        submit = (Button) findViewById(R.id.submit_id);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton congradio = ((RadioButton)findViewById(rgroup_congressman.getCheckedRadioButtonId()));
                RadioButton mayorradio = ((RadioButton)findViewById(rgroup_mayor.getCheckedRadioButtonId()));
                RadioButton viceradio = ((RadioButton)findViewById(rgroup_vicemayor.getCheckedRadioButtonId()));

                String abonalKagawad =  String.valueOf(rcheck_kagawad[0].isChecked());
                String albeusKagawad =  String.valueOf(rcheck_kagawad[1].isChecked());
                String arroyoKagawad =  String.valueOf(rcheck_kagawad[2].isChecked());
                String baldemoroKagawad =  String.valueOf(rcheck_kagawad[3].isChecked());
                String castilloKagawad =  String.valueOf(rcheck_kagawad[4].isChecked());
                String delcastilloKagawad =  String.valueOf(rcheck_kagawad[5].isChecked());
                String delrosarioKagawad =  String.valueOf(rcheck_kagawad[6].isChecked());
                String lavadiaKagawad =  String.valueOf(rcheck_kagawad[7].isChecked());
                String macaraigKagawad =  String.valueOf(rcheck_kagawad[8].isChecked());
                String perezKagawad =  String.valueOf(rcheck_kagawad[9].isChecked());
                String ranolaKagawad =  String.valueOf(rcheck_kagawad[10].isChecked());
                String rocoKagawad =  String.valueOf(rcheck_kagawad[11].isChecked());
                String rosalesKagawad =  String.valueOf(rcheck_kagawad[12].isChecked());
                String sanjuanKagawad =  String.valueOf(rcheck_kagawad[13].isChecked());
                String sergioKagawad =  String.valueOf(rcheck_kagawad[14].isChecked());
                String tuazonKagawad =  String.valueOf(rcheck_kagawad[15].isChecked());

                String indicator_on_off = "on";

                if (congradio == null || mayorradio == null || viceradio == null)
                {
                    Toast.makeText(getApplicationContext(), "Fill-Up All Question!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    congressman = congradio.getText().toString();
                    mayor = mayorradio.getText().toString();
                    vicemayor = viceradio.getText().toString();
                    String[] nameListcongressman = congressman.split(",");
                    String[] nameListmayor = mayor.split(",");
                    String[] nameListvicemayor = vicemayor.split(",");

//                    DatabaseAccess databaseAccessSubmit = DatabaseAccess.getInstance(QuestionForm.this,"voters.db");
//                    databaseAccessSubmit.open();
//                    boolean submitUpdate = databaseAccessSubmit.updateData(votersname_form,nameListcongressman[0],nameListmayor[0],nameListvicemayor[0],
//                            abonalKagawad,albeusKagawad,arroyoKagawad,baldemoroKagawad,castilloKagawad,delcastilloKagawad,delrosarioKagawad,lavadiaKagawad,
//                            macaraigKagawad,perezKagawad,ranolaKagawad,rocoKagawad,rosalesKagawad,sanjuanKagawad,sergioKagawad,tuazonKagawad,indicator_on_off);
//                    databaseAccessSubmit.close();

//                    if(submitUpdate == true) {
//                        Toast.makeText(QuestionForm.this,"Data Saved.",Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//                    else
//                        Toast.makeText(QuestionForm.this,"Data not Submitted",Toast.LENGTH_LONG).show();
                }

            }

        });
    }

    public void form4PrevButton(){
        Button prev4button = (Button) findViewById(R.id.prev4btn_id);
        prev4button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
