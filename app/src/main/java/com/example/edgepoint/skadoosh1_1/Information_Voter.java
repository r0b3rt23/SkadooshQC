package com.example.edgepoint.skadoosh1_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Information_Voter extends AppCompatActivity {
    ArrayList<String> str_kagawad;
    String profString;
    @Override
    protected void onResume() {
        super.onResume();
        InformationMain();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information__voter);

        Intent intent = getIntent();
        profString = intent.getStringExtra("VotersName");

        InformationMain();
    }

    public void InformationMain(){
        DatabaseAccess databaseSearchInfo = DatabaseAccess.getInstance(Information_Voter.this,"voters.db");
        databaseSearchInfo.open();
        List<String> SearchInfo = databaseSearchInfo.setInfo(profString);
        databaseSearchInfo.close();

        TextView profile_display = (TextView) findViewById(R.id.info_area);
        String str_city = SearchInfo.get(0);
        String str_barangay = SearchInfo.get(1);
        String str_pricinct = SearchInfo.get(2);
        String str_votersname = SearchInfo.get(3);
        String str_address = SearchInfo.get(4);
        String str_facebook = SearchInfo.get(5);
        String str_contact = SearchInfo.get(24);
        String str_birthday = SearchInfo.get(25);
        String str_age = SearchInfo.get(26);
        String str_email = SearchInfo.get(27);
        String str_education = SearchInfo.get(28);
        String str_income = SearchInfo.get(29);

        String str_education_scholar = "";
        String str_medical_assistance = "";
        String str_agriculture = "";
        String str_cooperative = "";
        for (int x=31; x<SearchInfo.size(); x++){
            // Education / Scholarship
            if (x == 31){
                if (!SearchInfo.get(x).isEmpty()){
                    str_education_scholar = "  Service 1 : "+"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }
            else if (x == 39){
                if (!SearchInfo.get(x).isEmpty()){
                    str_education_scholar = str_education_scholar +"  Service 2 : "+"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }
            else if (x == 47){
                if (!SearchInfo.get(x).isEmpty()){
                    str_education_scholar = str_education_scholar +"  Service 3 : "+"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }
            else if (x == 55){
                if (!SearchInfo.get(x).isEmpty()){
                    str_education_scholar = str_education_scholar +"  Service 4 : "+"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }

            // Medical Assistance
            else if (x == 33){
                if (!SearchInfo.get(x).isEmpty()){
                    str_medical_assistance = "  Service 1 : "+"(" +"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }
            else if (x == 41){
                if (!SearchInfo.get(x).isEmpty()){
                    str_medical_assistance = str_medical_assistance +"  Service 2 : "+"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }
            else if (x == 49){
                if (!SearchInfo.get(x).isEmpty()){
                    str_medical_assistance = str_medical_assistance +"  Service 3 : "+"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }
            else if (x == 57){
                if (!SearchInfo.get(x).isEmpty()){
                    str_medical_assistance = str_medical_assistance +"  Service 4 : "+"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }

            // Agriculture
            else if (x == 35){
                if (!SearchInfo.get(x).isEmpty()){
                    str_agriculture = "  Service 1 : "+"(" +"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }
            else if (x == 43){
                if (!SearchInfo.get(x).isEmpty()){
                    str_agriculture = str_agriculture +"  Service 2 : "+"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }
            else if (x == 51){
                if (!SearchInfo.get(x).isEmpty()){
                    str_agriculture = str_agriculture +"  Service 3 : "+"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }
            else if (x == 59){
                if (!SearchInfo.get(x).isEmpty()){
                    str_agriculture = str_agriculture +"  Service 4 : "+"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }

            // Cooperative as member
            else if (x == 37){
                if (!SearchInfo.get(x).isEmpty()){
                    str_cooperative = "  Service 1 : "+"(" +"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }
            else if (x == 45){
                if (!SearchInfo.get(x).isEmpty()){
                    str_cooperative = str_cooperative +"  Service 2 : "+"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }
            else if (x == 53){
                if (!SearchInfo.get(x).isEmpty()){
                    str_cooperative = str_cooperative +"  Service 3 : "+"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }
            else if (x == 61){
                if (!SearchInfo.get(x).isEmpty()){
                    str_cooperative = str_cooperative +"  Service 4 : "+"(" +SearchInfo.get(x)+")"+" - "+SearchInfo.get(x+1)+"\n";
                }
            }
        }

        profile_display.setText("NAME : "+str_votersname+"\n\n"+
                "CITY/MUNICIPALITY : "+str_city+" \n\n"+
                "BARANGAY : "+str_barangay+" \n\n"+
                "ADDRESS : "+str_address+"\n\n"+
                "PRESINTO : "+str_pricinct+"\n\n"+
                "CONTACT NO. : "+str_contact+"\n\n"+
                "BIRTHDAY : "+str_birthday+"\n\n"+
                "AGE : "+str_age+"\n\n"+
                "EMAIL : "+str_email+"\n\n"+
                "FACEBOOK : "+str_facebook+"\n\n"+
                "EDUCATION : "+str_education+"\n\n"+
                "ANNUAL INCOME : "+str_income+"\n\n"+
                "EDUCATION / SCHOLARSHIP\n"+ str_education_scholar+"\n"+
                "MEDICAL / ASSISTANCE\n"+ str_medical_assistance+"\n"+
                "AGRICULTURE\n"+ str_agriculture+"\n"+
                "COOPERATIVE (as member)\n"+ str_cooperative);

        VotingPreferenceButton(profString);
        CollecDataButton(str_city,str_barangay,str_pricinct,str_votersname);
        BackButton();
    }

    public void VotingPreferenceButton(final String profString){
        Button preferencebutton = (Button) findViewById(R.id.preference_id);

        preferencebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder setbuilder = new AlertDialog.Builder(Information_Voter.this);
                setbuilder.setTitle("Enter Preferred Leaders Password");
                final EditText passwordtext = new EditText(Information_Voter.this);
                setbuilder.setView(passwordtext);

                setbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String passwordValue = passwordtext.getText().toString();

                        if (!passwordValue.isEmpty()){
                            DatabaseAccess databasePassword = DatabaseAccess.getInstance(Information_Voter.this,"voters,db");
                            databasePassword.open();
                            boolean checkPassword = databasePassword.votersPreference(passwordValue);
                            databasePassword.close();

                            if (checkPassword == true){
                                showPreferenceDialog(profString);
                            }
                            else {
                                Toast.makeText(Information_Voter.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(Information_Voter.this, "Enter a password", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
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

    private void showPreferenceDialog(String profString) {
        DatabaseAccess databaseSearchInfo = DatabaseAccess.getInstance(Information_Voter.this,"voters.db");
        databaseSearchInfo.open();
        List<String> SearchInfo = databaseSearchInfo.setInfo(profString);
        databaseSearchInfo.close();
        String str_governor = SearchInfo.get(10);
        String str_vicegovernor = SearchInfo.get(11);
        String str_congressman = SearchInfo.get(12);
        String str_mayor = SearchInfo.get(13);
        String str_vicemayor = SearchInfo.get(30);
        str_kagawad = new ArrayList<>();
        for (int x = 0; x<8; x++){
            if (SearchInfo.get(x+14).equals("true")){
                if (x==0){str_kagawad.add(" ALCALA, EMING");}
                else if (x==1){str_kagawad.add(" ALEJANDRINO, BOYET");}
                else if (x==2){str_kagawad.add(" CASULLA, ROGER");}
                else if (x==3){str_kagawad.add(" GONZALES, TEDDY");}
                else if (x==4){str_kagawad.add(" LIWANAG, YNA");}
                else if (x==5){str_kagawad.add(" SIO, BETH");}
                else if (x==6){str_kagawad.add(" TALAGA, MANO");}
                else if (x==7){str_kagawad.add(" TANADA, MICHAEL");}
            }
        }

        String message = "";
        for (int i=0; i < str_kagawad.size(); i++) {
            message = (message + "\n• " + str_kagawad.get(i).toString());
        }
        String votingPref = ("GOVERNOR : "+str_governor+"\n\n"+
                "VICE GOVERNOR : "+str_vicegovernor+"\n\n"+
                "CONGRESSMAN : "+str_congressman+"\n\n"+
                "BOARD MEMBER : "+message+"\n\n"+
                "MAYOR : "+str_mayor+"\n\n"+
                "VICE MAYOR : "+str_vicemayor);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Information_Voter.this);
        alertDialogBuilder.setTitle("Voting Preference");
        alertDialogBuilder.setMessage(votingPref);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.show();

    }

    public void CollecDataButton(final String city_form,final String barangay_form,final String precinct_form,final String voters_form ){
        Button backbutton = (Button) findViewById(R.id.collect_id);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder setbuilder = new AlertDialog.Builder(Information_Voter.this);
                setbuilder.setTitle("Enter Collect Data Password");
                final EditText passwordtext = new EditText(Information_Voter.this);
                setbuilder.setView(passwordtext);

                setbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String passwordValue = passwordtext.getText().toString();

                        if (!passwordValue.isEmpty()){
                            DatabaseAccess databasePassword = DatabaseAccess.getInstance(Information_Voter.this,"voters,db");
                            databasePassword.open();
                            boolean checkPassword = databasePassword.votersPreference(passwordValue);
                            databasePassword.close();

                            if (checkPassword == true){
                                Form form = new Form(city_form,barangay_form,precinct_form,voters_form);
                                Intent intent = new Intent(Information_Voter.this, QuestionForm_new.class);
                                intent.putExtra("Form",form);
                                startActivity(intent);
//                                  showLoginDialog(precinct_form,form);
                            }
                            else {
                                Toast.makeText(Information_Voter.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(Information_Voter.this, "Enter a password", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
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

    public void BackButton(){
        Button backbutton = (Button) findViewById(R.id.bckbtn);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
