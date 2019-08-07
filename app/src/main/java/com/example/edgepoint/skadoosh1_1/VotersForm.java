package com.example.edgepoint.skadoosh1_1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VotersForm extends Activity implements View.OnClickListener  {
    Map<String, Integer> mapIndex;
    ListView votersList;
    String precinct_form, barangay_form,city_form;
    Form form;
    private VotersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voters_form);
                votersForm();
                form3PrevButton();
                submitButton();
//                graphButton();
        DatabaseAccess databaseUserAccess = DatabaseAccess.getInstance(VotersForm.this,"voters.db");
        databaseUserAccess.open();
        String UserAccess = databaseUserAccess.getUserAccess();
        databaseUserAccess.close();
        if (UserAccess.equals("skadoosh")){
            homeButton();
        }else {
            homeButton2();
        }
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        votersForm();
    }

    private void votersForm(){

        Intent intent = getIntent();

        form = intent.getParcelableExtra("Form");
        city_form = form.getCity();
        precinct_form = form.getPrecinct();
        barangay_form = form.getBarangay();

        DatabaseAccess databaseAccessVotersname = DatabaseAccess.getInstance(VotersForm.this,"voters.db");
        databaseAccessVotersname.open();

        final List<String> votersname = databaseAccessVotersname.getVotersName(city_form,barangay_form,precinct_form,"City_Municipality=? AND Barangay=? AND Precinct=?","VotersName");
        final List<String> indicator = databaseAccessVotersname.getIndicator(city_form,barangay_form,precinct_form,"City_Municipality=? AND Barangay=? AND Precinct=?","VotersName");
        final List<String> deceased = databaseAccessVotersname.getDeceased(city_form,barangay_form,precinct_form,"City_Municipality=? AND Barangay=? AND Precinct=?","VotersName");
        databaseAccessVotersname.close();

        final String[] votersArr = votersname.toArray(new String[votersname.size()]);

        votersList = (ListView) findViewById(R.id.list_voters);

        mAdapter = new VotersAdapter(this,votersname,indicator,deceased);
        votersList.setAdapter(mAdapter);

        votersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mAdapter.notifyDataSetChanged();
                String voters_form =  (String) votersList.getItemAtPosition(position);

                form = new Form(city_form,barangay_form,precinct_form,voters_form);

                Intent intent = new Intent(VotersForm.this, QuestionForm_new.class);
                intent.putExtra("Form",form);
                startActivity(intent);

            }
        });

        getIndexList(votersArr);

        displayIndex();
    }

    private void getIndexList(String[] fruits) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < fruits.length; i++) {
            String fruit = fruits[i];
            String index = fruit.substring(0, 1);

            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }
    }

    private void displayIndex() {
        LinearLayout indexLayout = (LinearLayout) findViewById(R.id.side_index);

        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        for (String index : indexList) {
            textView = (TextView) getLayoutInflater().inflate(
                    R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(this);
            indexLayout.addView(textView);
        }
    }

    public void form3PrevButton(){
        Button prev3button = (Button) findViewById(R.id.prev3btn_id);
        prev3button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onClick(View view) {
        TextView selectedIndex = (TextView) view;
        votersList.setSelection(mapIndex.get(selectedIndex.getText()));
    }

    private void homeButton() {
        Button btnBack = (Button) findViewById(R.id.homebtn);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VotersForm.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }
    private void homeButton2() {
        Button btnBack = (Button) findViewById(R.id.homebtn);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VotersForm.this, Main2Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    private void submitButton() {
        Button btnSubmit = (Button) findViewById(R.id.submitbtn);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(VotersForm.this, UploadToServer.class);
                startActivity(i);

            }
        });
    }

//    private void graphButton() {
//        Button btnSubmit = (Button) findViewById(R.id.graphbutton);
//
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent i = new Intent(VotersForm.this, GraphActivity3.class);
//                startActivity(i);
//
//            }
//        });
//    }

}
