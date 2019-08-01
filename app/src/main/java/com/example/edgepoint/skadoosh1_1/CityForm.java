package com.example.edgepoint.skadoosh1_1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CityForm  extends Activity implements View.OnClickListener {

    Map<String, Integer> mapIndex;
    ListView cityList;
    Form form;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_form);
                cityForm();
                displayIndex();
                formPrevButton();
                homeButton ();
    }
    private void cityForm(){

        String[] cityArr = getResources().getStringArray(R.array.city_municipality);

        cityList = (ListView) findViewById(R.id.list_voters);
        cityList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, cityArr));
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String city_form =  (String) cityList.getItemAtPosition(position);

                form = new Form(city_form,"","","");

                Intent intent = new Intent(CityForm.this, BarangayForm.class);
                intent.putExtra("Form",form);
                startActivity(intent);

            }
        });

        getIndexList(cityArr);
    }

    private void homeButton() {
        Button btnBack = (Button) findViewById(R.id.homebtn);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CityForm.this, MainActivity.class);
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
                Intent i = new Intent(CityForm.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

    }

    public void onClick(View view) {
        TextView selectedIndex = (TextView) view;
        cityList.setSelection(mapIndex.get(selectedIndex.getText()));
    }
}
