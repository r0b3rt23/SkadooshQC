package com.example.edgepoint.skadoosh1_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CollectData extends AppCompatActivity {
    String City;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_data);
        Intent intent = getIntent();
        City = intent.getStringExtra("City");
        homeButton ();
        formPrevButton();

    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.collect_btn) {
            if (City.equals("empty")) {
                Intent intent = new Intent(CollectData.this, CityForm.class);
                intent.putExtra("RegisteredChoice", "Registered");
                startActivity(intent);
            }else {
                Form form = new Form(City,"","","");
                Intent intent = new Intent(CollectData.this, BarangayForm.class);
                intent.putExtra("RegisteredChoice", "Registered");
                intent.putExtra("Form", form);
                startActivity(intent);
            }
        }

        if (v.getId() == R.id.collectunreg_btn) {
            if (City.equals("empty")) {
                Intent intent = new Intent(CollectData.this, CityForm.class);
                intent.putExtra("RegisteredChoice", "UnRegistered");
                startActivity(intent);
            }else {
                Form form = new Form(City,"","","");
                Intent intent = new Intent(CollectData.this, BarangayForm.class);
                intent.putExtra("RegisteredChoice", "UnRegistered");
                intent.putExtra("Form", form);
                startActivity(intent);
            }
        }

        if (v.getId() == R.id.addreg_btn) {
            Intent intent = new Intent(CollectData.this, NewRegister.class);
            intent.putExtra("City", City);
            startActivity(intent);
        }

    }

    private void homeButton() {
        Button btnBack = (Button) findViewById(R.id.homebtn);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
}
