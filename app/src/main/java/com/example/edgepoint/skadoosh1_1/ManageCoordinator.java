package com.example.edgepoint.skadoosh1_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ManageCoordinator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_coordinator);
        homeButton();
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.provincial_coordinator) {
//            Intent i = new Intent(ManageCoordinator.this, UploadToServer.class);
//            startActivity(i);
            Toast.makeText(ManageCoordinator.this,"Provincial Coordinators / Leaders", Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.municipal_coordinator) {
//            Intent i = new Intent(ManageCoordinator.this, DownloadBatch.class);
//            startActivity(i);
            Toast.makeText(ManageCoordinator.this,"Municipal Coordinators / Leaders", Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.barangay_coordinator) {
//            Intent i = new Intent(ManageCoordinator.this, EndCanvassing.class);
//            startActivity(i);
            Toast.makeText(ManageCoordinator.this,"Barangay Coordinators / Leaders", Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.precinct_coordinator) {
//            Intent i = new Intent(ManageCoordinator.this, DownloadFromServer.class);
//            startActivity(i);
            Toast.makeText(ManageCoordinator.this,"Precinct Coordinators / Leaders", Toast.LENGTH_SHORT).show();
        }
    }

    private void homeButton(){
        Button prevbutton = (Button) findViewById(R.id.coordinatorhome_btn);
        prevbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
