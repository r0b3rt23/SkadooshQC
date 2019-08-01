package com.example.edgepoint.skadoosh1_1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    String UserAccess;
    ImageButton showMenu;
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        if (ContextCompat.checkSelfPermission(Main2Activity.this,
//                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
////            Toast.makeText(MainActivity.this, "You have already granted this permission!",
////                    Toast.LENGTH_SHORT).show();
//        }
//        else {
//            requestStoragePermission();
//        }

        if (ContextCompat.checkSelfPermission(Main2Activity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(MainActivity.this, "You have already granted this permission!",
//                    Toast.LENGTH_SHORT).show();
        }
        else {
            writeStoragePermission();
        }

        DatabaseAccess databaseUserAccess = DatabaseAccess.getInstance(Main2Activity.this,"voters.db");
        databaseUserAccess.open();
        UserAccess = databaseUserAccess.getUserAccess();
        databaseUserAccess.close();

        TextView mainlabel = (TextView) findViewById(R.id.main2label);
        mainlabel.setText(UserAccess);

        showMenu = (ImageButton) findViewById(R.id.menubtn);
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
                            Intent intent = new Intent(Main2Activity.this, Login_Activity.class);
                            startActivity(intent);
                            finish();
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Main2Activity.this);
        alertDialogBuilder.setTitle("Are you sure you want to RESET ALL DATA ?");

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseAccess databaseAccessSave= DatabaseAccess.getInstance(Main2Activity.this,"voters.db");
                        databaseAccessSave.open();
                        boolean deleteData = databaseAccessSave.resetAllData();
                        if(deleteData == true){

                            Toast.makeText(Main2Activity.this,"Data has been reset",Toast.LENGTH_LONG).show();
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
        if (v.getId() == R.id.Bfillform2) {
            Form form = new Form(UserAccess,"","","");
            Intent intent = new Intent(Main2Activity.this, BarangayForm.class);
            intent.putExtra("Form",form);
            startActivity(intent);
        }

        if (v.getId() == R.id.Bviewprofile2) {
            Intent intent = new Intent(Main2Activity.this, RecyclerSearchView.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.datamanagement2) {
//            String submit = "main_submit";
            Intent intent = new Intent(Main2Activity.this, UploadToServer.class);
            startActivity(intent);
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
//                            ActivityCompat.requestPermissions(Main2Activity.this,
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
                            ActivityCompat.requestPermissions(Main2Activity.this,
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
}
