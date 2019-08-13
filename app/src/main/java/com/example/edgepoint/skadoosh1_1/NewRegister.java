package com.example.edgepoint.skadoosh1_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class NewRegister extends AppCompatActivity {

    public Spinner registered_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register);
        registered_spinner = (Spinner) findViewById(R.id.registered_spinner);

        String[] regvoterArr = getResources().getStringArray(R.array.registered_voter);
        ArrayAdapter<String> adapterRegistered = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, regvoterArr);
        registered_spinner.setAdapter(adapterRegistered);
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
