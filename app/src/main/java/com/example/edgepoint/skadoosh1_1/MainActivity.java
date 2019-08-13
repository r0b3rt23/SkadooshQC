package com.example.edgepoint.skadoosh1_1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageButton showMenu;
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        if (ContextCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
////            Toast.makeText(MainActivity.this, "You have already granted this permission!",
////                    Toast.LENGTH_SHORT).show();
//        }
//        else {
//            requestStoragePermission();
//        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(MainActivity.this, "You have already granted this permission!",
//                    Toast.LENGTH_SHORT).show();
        }
        else {
            writeStoragePermission();
        }

        showMenu = (ImageButton) findViewById(R.id.menubtn2);
        showMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), showMenu);
                dropDownMenu.getMenuInflater().inflate(R.menu.drop_down_menu, dropDownMenu.getMenu());
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String ch_menu = menuItem.getTitle().toString();

                        if (ch_menu.equals("Logout")) {
                            DatabaseAccess databaseUserAccess = DatabaseAccess.getInstance(MainActivity.this,"voters.db");
                            databaseUserAccess.open();
                            boolean resetUserAccess = databaseUserAccess.resetUserAccess();
                            databaseUserAccess.close();

                            if (resetUserAccess == true) {
                                Intent intent = new Intent(MainActivity.this, Login_Activity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else if (ch_menu.equals("Reset All Data")){
                            showDialog();
                        }
                        return true;
                    }
                });
                dropDownMenu.show();
            }
        });


    }



    private void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Are you sure you want to RESET ALL DATA ?");

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseAccess databaseAccessSave= DatabaseAccess.getInstance(MainActivity.this,"voters.db");
                        databaseAccessSave.open();
                        boolean deleteData = databaseAccessSave.resetAllData();
                        if(deleteData == true){

                            Toast.makeText(MainActivity.this,"Data has been reset",Toast.LENGTH_LONG).show();
                        }
                        databaseAccessSave.close();
                    }
                });

        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });

        alertDialogBuilder.show();

    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.Bfillform) {
            Intent i = new Intent(MainActivity.this, CollectData.class);
            startActivity(i);
        }

        if (v.getId() == R.id.Bviewgraphs) {
//            showLoginDialog("graph");
            Intent i = new Intent(MainActivity.this, GraphMenu.class);
            startActivity(i);
        }

        if (v.getId() == R.id.Bviewprofile) {
//            showLoginDialog("profile");
            Intent i = new Intent(MainActivity.this, RecyclerSearchView.class);
            startActivity(i);
        }

        if (v.getId() == R.id.datamanagement) {
            Intent i = new Intent(MainActivity.this, DataManagement.class);
            startActivity(i);
        }

    }

//    private void requestStoragePermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//            new AlertDialog.Builder(this)
//                    .setTitle("Permission needed")
//                    .setMessage("This permission is needed because of this and that")
//                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions(MainActivity.this,
//                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//                        }
//                    })
//                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .create().show();
//
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//        }
//    }

    private void writeStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private void showLoginDialog(final String ch) {
//        LayoutInflater li = LayoutInflater.from(this);
//        View prompt = li.inflate(R.layout.login_dialog, null);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setView(prompt);
//        final EditText user = (EditText) prompt.findViewById(R.id.login_name);
//        final EditText pass = (EditText) prompt.findViewById(R.id.login_password);
//        alertDialogBuilder.setTitle("ADMIN LOGIN");
//        alertDialogBuilder.setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        String password = pass.getText().toString();
//                        String username = user.getText().toString();
//                        try {
//
//                            DatabaseAccess databaseloginInfo = DatabaseAccess.getInstance(MainActivity.this,"voters.db");
//                            databaseloginInfo.open();
//
//                            if (ch == "graph"){
//
//                                boolean LoginInfo = databaseloginInfo.loginInfo(username,password,"graph_login");
//
//                                if (LoginInfo == true){
//                                    Intent i = new Intent(MainActivity.this, GraphMenu.class);
//                                    i.putExtra("username",username);
//                                    i.putExtra("password",password);
//                                    startActivity(i);
//                                }
//                                else {
//                                    Toast.makeText(MainActivity.this,"Invalid Username or Password!", Toast.LENGTH_LONG).show();
//                                }
//
//
//                            }
//                            else if (ch == "profile"){
//                                boolean LoginInfo = databaseloginInfo.loginInfo(username,password,"admin_table");
//
//                                if (LoginInfo == true){
//                                    Intent i = new Intent(MainActivity.this, RecyclerSearchView.class);
//                                    startActivity(i);
//                                }
//                                else {
//                                    Toast.makeText(MainActivity.this,"Invalid Username or Password!", Toast.LENGTH_LONG).show();
//                                }
//
//                            }
//                            databaseloginInfo.close();
//
//                        } catch (Exception e) {
//                            Toast.makeText(MainActivity.this,"Invalid Username or Password!", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//
//        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//
//            }
//        });
//
//        alertDialogBuilder.show();
//
//    }

}
