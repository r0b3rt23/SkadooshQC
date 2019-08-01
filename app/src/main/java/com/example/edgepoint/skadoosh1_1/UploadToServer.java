package com.example.edgepoint.skadoosh1_1;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadToServer extends AppCompatActivity implements View.OnClickListener {

    public static final String URL_SAVE_NAME = "http://162.144.86.26/skadoosh_quezon/saveName.php";

    //View objects
    private Button buttonSave;
    private EditText editTextName;


    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "com.example.edgepoint.datasaved";

    //Broadcast receiver to know the sync status
    BroadcastReceiver broadcastReceiver;
    String[] VnameArr;
    ListView VnameList;
    ArrayAdapter<String> adapterVnameList;
    Runnable run;
    List<String> Vname;
    String responseMessage="";
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to_server);

        DatabaseAccess databaseUserAccess = DatabaseAccess.getInstance(UploadToServer.this,"voters.db");
        databaseUserAccess.open();
        String UserAccess = databaseUserAccess.getUserAccess();
        databaseUserAccess.close();
        if (UserAccess.equals("skadoosh")){
            HomeButton();
        }else {
            HomeButton2();
        }
        BackButton();

        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        buttonSave = (Button) findViewById(R.id.upload_btn);

        //adding click listener to button
        buttonSave.setOnClickListener(this);

        DatabaseAccess databaseAccessVname = DatabaseAccess.getInstance(this,"voters.db");
        databaseAccessVname.open();
        Vname = databaseAccessVname.getUploadDatabase("VotersName", 0);
        databaseAccessVname.close();

        VnameArr = Vname.toArray(new String[Vname.size()]);

        VnameList = (ListView) findViewById(R.id.upload_list);

        adapterVnameList = new ArrayAdapter<String>(UploadToServer.this,android.R.layout.simple_list_item_1, Vname);
        VnameList.setAdapter(adapterVnameList);

//        VnameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//
//                String voters_form =  (String) VnameList.getItemAtPosition(position);
//
//                Form form = new Form("","",voters_form);
//
//                Intent intent = new Intent(UploadToServer.this, QuestionForm.class);
//                intent.putExtra("Form",form);
//                startActivity(intent);
//
//            }
//        });

        if (VnameArr.length != 0){
            buttonSave.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {

        if ( checkNetworkConnection()){
            showLoginDialog();
        }
        else Toast.makeText(UploadToServer.this, "No Internet Connection.", Toast.LENGTH_LONG).show();

    }

    private void showLoginDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.sender_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(prompt);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Data...");

        final EditText sender = (EditText) prompt.findViewById(R.id.sender_name);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String sendername = sender.getText().toString();
                        try {
                            if (!sendername.isEmpty()){
                                if (VnameArr.length != 0){
                                    progressDialog.show();
                                    for(int x=0; x<VnameArr.length; x++)
                                    {
                                        Date date = new Date();
                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                                        String mac = wifiInfo.getMacAddress();
                                        String model = Build.MODEL;
                                        String version = Build.VERSION.RELEASE;

                                        String device = model+"/"+mac+"/"+version;

                                        DatabaseAccess databaseSearchUpload = DatabaseAccess.getInstance(UploadToServer.this,"voters.db");
                                        databaseSearchUpload.open();
                                        List<String> SearchUpload = databaseSearchUpload.setUpload(VnameArr[x]);
                                        String BatchName = databaseSearchUpload.getBatchName();
                                        databaseSearchUpload.close();

                                        String up_votersname = SearchUpload.get(0);
                                        String up_governor = SearchUpload.get(1);
                                        String up_vicegovernor = SearchUpload.get(2);
                                        String up_congressman = SearchUpload.get(3);
                                        String up_mayor = SearchUpload.get(4);
                                        String up_alcala = SearchUpload.get(5);
                                        String up_alejandrino = SearchUpload.get(6);
                                        String up_casulla = SearchUpload.get(7);
                                        String up_gonzales = SearchUpload.get(8);
                                        String up_liwanag = SearchUpload.get(9);
                                        String up_sio = SearchUpload.get(10);
                                        String up_talaga = SearchUpload.get(11);
                                        String up_tanada = SearchUpload.get(12);
                                        String up_indicator = SearchUpload.get(13);
                                        String up_deceased = SearchUpload.get(14);
                                        String up_partylist = SearchUpload.get(15);
                                        String up_encoder = SearchUpload.get(16);
                                        String up_contact = SearchUpload.get(17);

//                                        Toast.makeText(UploadToServer.this, BatchName, Toast.LENGTH_LONG).show();
                                        saveNameToServer(up_votersname,up_governor,up_vicegovernor,up_congressman,up_mayor,up_alcala,up_alejandrino,
                                                up_casulla,up_gonzales,up_liwanag,up_sio,up_talaga,up_tanada,formatter.format(date),device, sendername,
                                                BatchName,up_indicator,up_deceased,up_partylist,up_encoder,up_contact);
                                    }
                                    deleteList();

                                }
                                else {Toast.makeText(UploadToServer.this, "Empty List!", Toast.LENGTH_LONG).show();}

                            }
                            else Toast.makeText(UploadToServer.this, "Input Sender's Name", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            Toast.makeText(UploadToServer.this,"Error!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });

        alertDialogBuilder.show();

    }

    /*
     * this method is saving the name to the server
     * */
    private void saveNameToServer(final String Vname, final String governor, final String vicegovernor, final String congressman,
                                  final String mayor, final String alcala, final String alejandrino,final String casulla,
                                  final String gonzales , final String liwanag, final String sio,final String talaga,final String tanada,
                                  final String uploadedat, final String device, final String sender, final String batchname,
                                  final String indicator, final String deceased, final String partylist, final String encoder, final String contact) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(UploadToServer.this);
        alert.setTitle("Sending Failed...");

        RequestQueue queue = Volley.newRequestQueue(UploadToServer.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(UploadToServer.this, response, Toast.LENGTH_LONG).show();
                        if (response.equals("Success")){
//                            Toast.makeText(UploadToServer.this, "Success! Data Transfer.", Toast.LENGTH_LONG).show();
//                            progressDialog.dismiss();
//                            responseMessage = "success";
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(UploadToServer.this, "Sending Failed! No Internet Connection.", Toast.LENGTH_LONG).show();
//                        progressDialog.dismiss();

                    }
                })
            {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("vname", Vname);
                params.put("governor", governor);
                params.put("vicegovernor", vicegovernor);
                params.put("congressman", congressman);
                params.put("mayor", mayor);
                params.put("alcala", alcala);
                params.put("alejandrino", alejandrino);
                params.put("casulla", casulla);
                params.put("gonzales", gonzales);
                params.put("liwanag", liwanag);
                params.put("sio", sio);
                params.put("talaga", talaga);
                params.put("tanada", tanada);
                params.put("uploadedat", uploadedat);
                params.put("device", device);
                params.put("sender", sender);
                params.put("batchname", batchname);
                params.put("indicator", indicator);
                params.put("deceased", deceased);
                params.put("partylist", partylist);
                params.put("encoder", encoder);
                params.put("contact", contact);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private void deleteList(){

        DatabaseAccess databaseDelete = DatabaseAccess.getInstance(UploadToServer.this,"voters.db");
        databaseDelete.open();
        boolean deleteData = databaseDelete.delete();
        databaseDelete.close();

        if(deleteData == true) {
            Toast.makeText(UploadToServer.this, "Data transfer successful!", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            adapterVnameList.notifyDataSetChanged();
            finish();
        }
        else {
            Toast.makeText(UploadToServer.this, "Error Deleting Data!", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }


    public boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());

    }

    private void HomeButton(){
        Button homebtn = (Button) findViewById(R.id.bckupload_btn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UploadToServer.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
            }
        });

    }

    private void HomeButton2(){
        Button homebtn = (Button) findViewById(R.id.bckupload_btn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UploadToServer.this, Main2Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

    }
    private void BackButton(){
        Button prevbutton = (Button) findViewById(R.id.backup_btn);
        prevbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
