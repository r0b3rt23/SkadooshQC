package com.example.edgepoint.skadoosh1_1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashActivity extends Activity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashfile);


        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DatabaseAccess databaseUserAccess = DatabaseAccess.getInstance(SplashActivity.this,"voters.db");
                databaseUserAccess.open();
                String getUserAccess = databaseUserAccess.getUserAccess();


                if (getUserAccess.isEmpty()) {
                    Intent intent = new Intent(SplashActivity.this, Login_Activity.class);
                    startActivity(intent);
                    finish();
                }else {
                    boolean UserAccess = databaseUserAccess.userAccess(getUserAccess);

                    if (UserAccess == true){
                        if (getUserAccess.equals("skadoosh")) {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(SplashActivity.this, Main2Activity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else {
                        Toast.makeText(SplashActivity.this,"Invalid Username or Password!", Toast.LENGTH_LONG).show();
                    }
                }
                databaseUserAccess.close();
            }
        },8000);
    }
}
