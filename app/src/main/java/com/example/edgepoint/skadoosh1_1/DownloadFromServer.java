package com.example.edgepoint.skadoosh1_1;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadFromServer extends AppCompatActivity {

    Button buttonDownload;
    private static final String URL_DATABASE = "http://162.144.86.26/skadoosh_quezon/getDatabase.php";
    private static final String URL_DATE = "http://162.144.86.26/skadoosh_quezon/getUpdatedDate.php";
    private BroadcastReceiver broadcastReceiver;
    ArrayAdapter<String> adapterDownloadList;
    ListView DownloadList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_from_server);

        buttonDownload = findViewById(R.id.downloadFile_btn);
        DownloadList =  findViewById(R.id.download_listview);
        HomeButton();
        BackButton();



        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ( checkNetworkConnection()){
                    DatabaseAccess databaseAccessDate = DatabaseAccess.getInstance(DownloadFromServer.this,"voters.db");
                    databaseAccessDate.open();
                    long DateMilliSeconds = databaseAccessDate.getUploadDate();
                    databaseAccessDate.close();

                    DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(DateMilliSeconds);
                    final String Date = dateformat.format(calendar.getTime());

                    getDownloadFile(Date);

                    buttonDownload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            loadDatabaseServer(Date);
                        }
                    });

                }
                else
                {
                    buttonDownload.setVisibility(View.INVISIBLE);
                    DownloadList.setAdapter(null);
                    Toast.makeText(DownloadFromServer.this, "Unable to Connect. No Internet Connection.", Toast.LENGTH_LONG).show();
                }
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    private void getDownloadFile(final String dateFile) {

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Updating Download Data...");
        progress.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            int count = array.length();
                            if (count != 0){
                                //traversing through all the object
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject datalist = array.getJSONObject(i);

                                    String dateFromServer = datalist.getString("UploadedAt");

                                    if (dateFromServer.equals(dateFile)){
                                        Toast.makeText(DownloadFromServer.this, "Your Data is Updated", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(DownloadFromServer.this, "New Data is Available. Download It Now.", Toast.LENGTH_SHORT).show();
                                        downloadlist(dateFromServer);
                                    }
                                }

                                progress.dismiss();
                            }
                            else{
                                Toast.makeText(DownloadFromServer.this, "Data is Empty!", Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }


                        } catch (JSONException e) {

                            e.printStackTrace();
                            progress.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DownloadFromServer.this, "Unable to Connect. No Internet Connection.", Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void downloadlist(String dateArr){
        buttonDownload.setVisibility(View.VISIBLE);
        List<String> dateName = new ArrayList<>();
        dateName.add(dateArr);

        adapterDownloadList = new ArrayAdapter<String>(DownloadFromServer.this,android.R.layout.simple_list_item_1, dateName);
        DownloadList.setAdapter(adapterDownloadList);
    }

    private void loadDatabaseServer(final String dateString) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATABASE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            int count = array.length();
                            if (count != 0){
                                String message = "";
                                //traversing through all the object
                                DatabaseAccess databaseAccessSubmit = DatabaseAccess.getInstance(DownloadFromServer.this,"voters.db");
                                databaseAccessSubmit.open();
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject datalist = array.getJSONObject(i);
                                    String uploadedat = datalist.getString("UploadedAt");
                                    String votersname = datalist.getString("VotersName");
                                    String governor = datalist.getString("Governor");
                                    String vicegovernor = datalist.getString("ViceGovernor");
                                    String congressman = datalist.getString("Congressman");
                                    String mayor = datalist.getString("Mayor");
                                    String alcala = datalist.getString("ALCALA");
                                    String alejandrino = datalist.getString("ALEJANDRINO");
                                    String casulla = datalist.getString("CASULLA");
                                    String gonzales = datalist.getString("GONZALES");
                                    String liwanag = datalist.getString("LIWANAG");
                                    String sio = datalist.getString("SIO");
                                    String talaga = datalist.getString("TALAGA");
                                    String tanada = datalist.getString("TANADA");
                                    String indicator = datalist.getString("Indicator");
                                    String deceased = datalist.getString("Deceased");
                                    String partylist = datalist.getString("PartyList");
                                    String encoder = datalist.getString("Encoder");
                                    String contact = datalist.getString("Contact");

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                    try {
                                        Date date = sdf.parse(uploadedat);
                                        long uploadedatmillis = date.getTime();

                                        boolean submitUpdate = databaseAccessSubmit.updateFromServer(uploadedatmillis,votersname,governor,
                                                vicegovernor,congressman,mayor,alcala,alejandrino,casulla,gonzales,liwanag,sio,talaga,tanada,indicator,deceased,partylist,encoder,contact);

                                        if(submitUpdate == true) {
                                            message="Data Saved!";
                                        }
                                        else message="Error! Data not save.";
                                    }
                                    catch(Exception e) {
                                        message="Exception Error!";
                                    }

                                }

                                Toast.makeText(DownloadFromServer.this,message, Toast.LENGTH_LONG).show();
                                DownloadList.setAdapter(null);
                                databaseAccessSubmit.close();
                                progressDialog.dismiss();
                            }
                            else{
                                Toast.makeText(DownloadFromServer.this, "Data is Empty!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DownloadFromServer.this, "Data not Updated! No Internet!", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("date", dateString);
                    return params;
                }
            };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);

    }

    public boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());

    }

    private void HomeButton(){
        Button homebtn = (Button) findViewById(R.id.downHome_btn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DownloadFromServer.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

    }
    private void BackButton(){
        Button prevbutton = (Button) findViewById(R.id.backdown_btn);
        prevbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
