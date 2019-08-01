package com.example.edgepoint.skadoosh1_1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchProfileActivity extends AppCompatActivity {
    private TextView profile_display;
    ProgressDialog progressDialog;
    ArrayList<String> str_kagawad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_profile);



        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Data...");
        progressDialog.setMessage("This may take a while. Please wait for a few minutes.");
        progressDialog.show();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AutoSearch();
                SearchBackButton();
            }
        },1500);

    }

    public void AutoSearch(){
        final AutoCompleteTextView AutoSearch = (AutoCompleteTextView)findViewById(R.id.search_info_id);

        ImageButton clear = (ImageButton)findViewById(R.id.clearbtn_id);

        DatabaseAccess databaseSearchInfo = DatabaseAccess.getInstance(SearchProfileActivity.this,"voters.db");
        databaseSearchInfo.open();
        List<String> SearchName = databaseSearchInfo.setSeacrhDatabase("VotersName" , 2);
        databaseSearchInfo.close();

        ArrayAdapter<String> searchAdapter = new ArrayAdapter<String>(SearchProfileActivity.this,android.R.layout.simple_dropdown_item_1line, SearchName);
        AutoSearch.setAdapter(searchAdapter);
        AutoSearch.setThreshold(2);

        progressDialog.dismiss();
        Toast.makeText(SearchProfileActivity.this, "Load Successful", Toast.LENGTH_LONG).show();

        profile_display = (TextView) findViewById(R.id.profile_view_id);

        AutoSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                String profString =  AutoSearch.getText().toString();

                InputMethodManager imn = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imn.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

                DatabaseAccess databaseSearchInfo = DatabaseAccess.getInstance(SearchProfileActivity.this,"voters.db");
                databaseSearchInfo.open();
                List<String> SearchInfo = databaseSearchInfo.setInfo(profString);
                databaseSearchInfo.close();

                String str_barangay = SearchInfo.get(0);
                String str_pricinct = SearchInfo.get(1);
                String str_votersname = SearchInfo.get(2);
                String str_address = SearchInfo.get(3);
                String str_congressman = SearchInfo.get(7);
                String str_mayor = SearchInfo.get(8);
                String str_vicemayor = SearchInfo.get(9);

                str_kagawad = new ArrayList<>();
                for (int x = 0; x<16; x++){
                    if (SearchInfo.get(x+10).equals("true")){
                        if (x==0){str_kagawad.add(" ABONAL, Greg");}
                        else if (x==1){str_kagawad.add(" ALBEUS, Jess");}
                        else if (x==2){str_kagawad.add(" ARROYO, Mila");}
                        else if (x==3){str_kagawad.add(" BALDEMORO, Elmer");}
                        else if (x==4){str_kagawad.add(" CASTILLO, Vidal");}
                        else if (x==5){str_kagawad.add(" DEL CASTILLO, Badong");}
                        else if (x==6){str_kagawad.add(" DEL ROSARIO, Lito");}
                        else if (x==7){str_kagawad.add(" LAVADIA, June");}
                        else if (x==8){str_kagawad.add(" MACARAIG, Areiz");}
                        else if (x==9){str_kagawad.add(" PEREZ, Joe");}
                        else if (x==10){str_kagawad.add(" RANOLA, Sonny");}
                        else if (x==11){str_kagawad.add(" ROCO, Bernadette");}
                        else if (x==12){str_kagawad.add(" ROSALES, Ghiel");}
                        else if (x==13){str_kagawad.add(" SAN JUAN, Pedro");}
                        else if (x==14){str_kagawad.add(" SERGIO, Nathan");}
                        else if (x==15){str_kagawad.add(" TUAZON, Jose");}
                    }
                }

                String message = "";
                for (int i=0; i < str_kagawad.size(); i++) {
                    message = (message + "\n" + str_kagawad.get(i).toString());
                }

                profile_display.setText("BOTANTE : "+str_votersname+"\n\n"+
                        "BARANGAY : "+str_barangay+" \n\n"+
                        "ADDRESS : "+str_address+"\n\n"+
                        "PRESINTO : "+str_pricinct+"\n\n"+
                        "CONGRESSMAN : "+str_congressman+"\n\n"+
                        "MAYOR : "+str_mayor+"\n\n"+
                        "VICE MAYOR : "+str_vicemayor+"\n\n"+
                        "KAGAWAD : "+message);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Clear EditText
                AutoSearch.getText().clear();

            }
        });
    }

    public void SearchBackButton(){
        Button prev2button = (Button) findViewById(R.id.backprofbtn);

        prev2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SearchProfileActivity.this);
                alertDialogBuilder.setTitle("Confirm Exit");
                alertDialogBuilder.setMessage("This will restart the loading process. Are you sure you want to EXIT?");
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick (DialogInterface dialogInterface,int i){
                        finish();
                    }

                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener()

                {
                    @Override
                    public void onClick (DialogInterface dialog,int which){
                    }

                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }
}
