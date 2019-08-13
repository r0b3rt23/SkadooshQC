package com.example.edgepoint.skadoosh1_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CollectData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_data);
        homeButton ();
        formPrevButton();

    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.collect_btn) {
            Intent i = new Intent(CollectData.this, CityForm.class);
            startActivity(i);
        }

        if (v.getId() == R.id.addreg_btn) {
            Intent i = new Intent(CollectData.this, NewRegister.class);
            startActivity(i);
        }

    }

    private void homeButton() {
        Button btnBack = (Button) findViewById(R.id.homebtn);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CollectData.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    private void formPrevButton(){
        Button prevbutton = (Button) findViewById(R.id.prev2btn_id);
        prevbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CollectData.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

    }
}
