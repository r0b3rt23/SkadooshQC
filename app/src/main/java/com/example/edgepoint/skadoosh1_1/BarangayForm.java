package com.example.edgepoint.skadoosh1_1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BarangayForm extends Activity implements View.OnClickListener {

    Map<String, Integer> mapIndex;
    ListView barangayList;
    Form form;
    String city_form;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barangay_form);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                barangayForm();
                displayIndex();
                formPrevButton();
                DatabaseAccess databaseUserAccess = DatabaseAccess.getInstance(BarangayForm.this,"voters.db");
                databaseUserAccess.open();
                String UserAccess = databaseUserAccess.getUserAccess();
                databaseUserAccess.close();
                if (UserAccess.equals("skadoosh")){
                    homeButton();
                }else {
                    homeButton2();
                }
            }
        },1000);

    }

    private void barangayForm(){

        Intent intent = getIntent();

        form = intent.getParcelableExtra("Form");

        city_form = form.getCity();

        DatabaseAccess databaseAccessBarangay = DatabaseAccess.getInstance(this,"voters.db");
        databaseAccessBarangay.open();
        List<String> barangays = databaseAccessBarangay.getBarangay(city_form,"City_Municipality=?","Barangay");
        databaseAccessBarangay.close();

        String[] barangayArr = barangays.toArray(new String[barangays.size()]);

        barangayList = (ListView) findViewById(R.id.list_voters);
        barangayList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, barangays));
        progressDialog.dismiss();
        barangayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String barangay_form =  (String) barangayList.getItemAtPosition(position);

                form = new Form(city_form,barangay_form,"","");

                Intent intent = new Intent(BarangayForm.this, PrecinctForm.class);
                intent.putExtra("Form",form);
                startActivity(intent);

            }
        });

        getIndexList(barangayArr);
    }

    private void homeButton() {
        Button btnBack = (Button) findViewById(R.id.homebtn);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BarangayForm.this, MainActivity.class);
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
                Intent i = new Intent(BarangayForm.this, Main2Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
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

    private void formPrevButton(){
        Button prevbutton = (Button) findViewById(R.id.prev2btn_id);
        prevbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void onClick(View view) {
        TextView selectedIndex = (TextView) view;
        barangayList.setSelection(mapIndex.get(selectedIndex.getText()));
    }
}
