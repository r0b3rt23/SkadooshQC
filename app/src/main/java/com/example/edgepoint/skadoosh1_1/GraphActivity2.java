package com.example.edgepoint.skadoosh1_1;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphActivity2 extends AppCompatActivity {
    private ArrayList<ListModel> SortPartyList;
    Spinner barangay_spinner,city_spinner;
    Spinner graph_spinner1;
    public ArrayAdapter adapterGraph1,cityAdapter;
    public static String Bgypsin;
    public static String ListSpinner1;
    ProgressDialog progressDialog, loading;
    BroadcastReceiver broadcastReceiver;

    public static final String URL_COUNT = "http://ramores.com/rema/php_skadoosh/getCountBarangay.php";
    public static final String URL_LineCOUNT = "http://ramores.com/rema/php_skadoosh/getCountLineBarangay.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph2);

        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        progressDialog = new ProgressDialog(GraphActivity2.this);
        progressDialog.setMessage("Generating Graph...");

        loading = new ProgressDialog(GraphActivity2.this);
        loading.setMessage("Loading Data...");
        loading.show();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DatabaseAccess databaseUserAccess = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db");
                databaseUserAccess.open();
                String UserAccess = databaseUserAccess.getUserAccess();
                databaseUserAccess.close();
                if (UserAccess.equals("skadoosh")){
                    homeButton();
                }else {
                    homeButton2();
                }
                graphactivity2();
                backGraphButton();
                generateGraph2();
                generateMissingGraph2();
                generateLineGraph();
            }
        },1500);

    }

    public void graphactivity2(){

        city_spinner = findViewById(R.id.city_spinner2);
        barangay_spinner = findViewById(R.id.graph_spinner2);
        graph_spinner1 = findViewById(R.id.graph_spinner2a);

        cityAdapter = ArrayAdapter.createFromResource(this, R.array.city_municipality, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_spinner.setAdapter(cityAdapter);


        adapterGraph1 = ArrayAdapter.createFromResource(this, R.array.graph_quezon_column, android.R.layout.simple_spinner_item);
        adapterGraph1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graph_spinner1.setAdapter(adapterGraph1);

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String citySpinner = city_spinner.getSelectedItem().toString();

                DatabaseAccess databaseAccessBarangay = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db");
                databaseAccessBarangay.open();
                List<String> barangays = databaseAccessBarangay.getBarangay(citySpinner,"City_Municipality=?","Barangay");
                databaseAccessBarangay.close();

                ArrayAdapter<String> adapterBarangay = new ArrayAdapter<>(GraphActivity2.this,android.R.layout.simple_spinner_item, barangays);
                adapterBarangay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapterBarangay.notifyDataSetChanged();
                barangay_spinner.setAdapter(adapterBarangay);
                loading.dismiss();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void generateGraph2(){

        Button generategraphbtn = findViewById(R.id.generategraphbtn2);
        generategraphbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        graph();
                    }
                },1000);
            }
        });
    }

    public void graph(){
        String citySpinner = city_spinner.getSelectedItem().toString();
        Bgypsin = barangay_spinner.getSelectedItem().toString();
        ListSpinner1 = graph_spinner1.getSelectedItem().toString();

        //Toast.makeText(GraphActivity2.this,Bgypsin, Toast.LENGTH_LONG).show();
        List<String> labels = new ArrayList<>();
        String table_graph = "";

        switch (ListSpinner1){
            case "Governor":
                labels.add("ABUY");
                labels.add("ALCALA");
                labels.add("DATOR");
                labels.add("PULGAR");
                labels.add("SUAREZ");
                labels.add("VILLENA");
                labels.add("Indefinite");
                table_graph = "governor_graph";
                break;

            case "ViceGovernor":
                labels.add("CAPINA");
                labels.add("ESTACIO");
                labels.add("MALITE");
                labels.add("NANTES");
                labels.add("Indefinite");
                table_graph = "vicegovernor_graph";
                break;

            case "Congressman":
                labels.add("ALCALA");
                labels.add("MASILANG");
                labels.add("SENERES");
                labels.add("SUAREZ_AMADEO");
                labels.add("SUAREZ_DAVID");
                labels.add("Indefinite");
                table_graph = "congressman_graph";
                break;

            case "Mayor":
                switch (citySpinner){
                    case "Candelaria":
                        labels.add("BOONGALING");
                        labels.add("MALIWANAG");
                        labels.add("Indefinite");
                        table_graph = "candelaria_mayor_graph";
                        break;

                    case "Dolores":
                        labels.add("CALAYAG");
                        labels.add("MILAN");
                        labels.add("Indefinite");
                        table_graph = "dolores_mayor_graph";
                        break;

                    case "Lucena City":
                        labels.add("ALCALA");
                        labels.add("TALAGA");
                        labels.add("Indefinite");
                        table_graph = "lucena_mayor_graph";
                        break;

                    case "San Antonio":
                        labels.add("HERNANDEZ");
                        labels.add("WAGAN");
                        labels.add("Indefinite");
                        table_graph = "sanantonio_mayor_graph";
                        break;

                    case "Sariaya":
                        labels.add("DE LA ROCA");
                        labels.add("GAYETA");
                        labels.add("Indefinite");
                        table_graph = "sariaya_mayor_graph";
                        break;

                    case "Tiaong":
                        labels.add("CASTILLO");
                        labels.add("PREZA");
                        labels.add("Indefinite");
                        table_graph = "tiaong_mayor_graph";
                        break;

                }
                break;

            case "BoardMember":
                labels.add("ALCALA");
                labels.add("ALEJANDRINO");
                labels.add("CASULLA");
                labels.add("GONZALES");
                labels.add("LIWANAG");
                labels.add("SIO");
                labels.add("TALAGA");
                labels.add("TANADA");
                table_graph = "bokal_graph";
                break;

            case "PartyList":
                table_graph = "PartyList_graph";
                break;

            case "Highest Education":
                ListSpinner1 = "Education";
                labels.add("Elementary level");
                labels.add("Elementary graduate");
                labels.add("High Schoool level");
                labels.add("High Schoool graduate");
                labels.add("College level");
                labels.add("College graduate");
                labels.add("Post graduate");
                table_graph = "highest_education_graph";
                break;

            case "Annual HH Income":
                ListSpinner1 = "Income";
                labels.add("(7,890 monthly or less)");
                labels.add("(7,891 to 15,780 monthly)");
                labels.add("(15,781 to 31,560 monthly)");
                labels.add("(31,561 to 78,900 monthly)");
                labels.add("(78,901 to 118,350 monthly)");
                labels.add("(118,351 to 157,800)");
                labels.add("(157,801 and more)");
                table_graph = "annual_income_graph";
                break;
        }

        if ( checkNetworkConnection()){
            CountInServer(ListSpinner1,Bgypsin,table_graph,citySpinner,"online","withoutMissing");
        }
        else
        {
            CountInLocal(labels,ListSpinner1,Bgypsin,table_graph,"offline",citySpinner);
        }

    }

    public void CountInLocal(List<String> labelsLocal,String candidateSpinner, String barangaySpinner, String table_graph_local, String connection_local, String cityIntent){
        List<Integer> count = new ArrayList<>();
        List<String> partyList = new ArrayList<>();
        if (candidateSpinner.equals("PartyList")){
            DatabaseAccess createPartyListTable = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db");
            createPartyListTable.open();
            createPartyListTable.CreateDynamicTables(table_graph_local);
            //ArrayList<ListModel> SortPartyList = createPartyListTable.SortPartyList();
            createPartyListTable.close();
        }

        DatabaseAccess datacount = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db"); //count the names
        datacount.open();
        if (candidateSpinner.equals("PartyList")){
//            partyList = datacount.getDistinctName2(cityIntent,barangaySpinner);
            partyList = datacount.getDistinctName();
            for (int i=0; i<partyList.size();i++)
            {
                count.add(datacount.getcountnamePartylist2(partyList.get(i),candidateSpinner,cityIntent, barangaySpinner));
            }
        }
        else{
            for (int i=0; i<labelsLocal.size();i++)
            {
                if (candidateSpinner.equals("BoardMember")){
                    count.add(datacount.getcountname2("true",labelsLocal.get(i),cityIntent,barangaySpinner));
                }
                else {count.add(datacount.getcountname2(labelsLocal.get(i),candidateSpinner,cityIntent,barangaySpinner));} //
            }
        }
        datacount.close();

        DatabaseAccess insertdata = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db"); //insert data
        insertdata.open();
        if (candidateSpinner.equals("PartyList")){
            for (int i=0; i<count.size();i++)
            {
                insertdata.insertPartyList(i, count.get(i),partyList.get(i), table_graph_local);
            }
        }
        else{
            for (int i=0; i<count.size();i++)
            {
                insertdata.insertCount(i, labelsLocal.get(i), count.get(i), table_graph_local);
            }
        }
        insertdata.close();
        if (candidateSpinner.equals("PartyList")){
            DatabaseAccess createPartyListTableSort = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db");
            createPartyListTableSort.open();
            createPartyListTableSort.CreateDynamicTables("sample_table");
            createPartyListTableSort.close();

            DatabaseAccess insertAscending = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db"); //insert data
            insertAscending.open();
            SortPartyList = insertAscending.SortPartyList(table_graph_local);
            ListModel listModel;
            for(int i=0; i<SortPartyList.size(); i++){
                listModel = SortPartyList.get(i);
                //Toast.makeText(GraphActivity.this,listModel.getResult()+" "+listModel.getNameList(),Toast.LENGTH_LONG).show();
                insertAscending.insertPartyList(i, listModel.getCount(),listModel.getName(), "sample_table");
            }
            insertAscending.close();
        }
        progressDialog.dismiss();

        if (candidateSpinner.equals("BoardMember")) {
            openKagawadGraph(candidateSpinner, table_graph_local, connection_local);
        }
        else if(candidateSpinner.equals("PartyList")){
            openGraph2(candidateSpinner, "sample_table", connection_local,cityIntent);
        }
        else {
            openGraph2(candidateSpinner, table_graph_local, connection_local,cityIntent);
        }
    }

    public void generateMissingGraph2(){

        Button miss_generategraphbtn = findViewById(R.id.generategraphbtn_m2);
        miss_generategraphbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Mgraph();
                    }
                },1000);

            }
        });
    }

    public void Mgraph(){
        Bgypsin = barangay_spinner.getSelectedItem().toString();
        ListSpinner1 = graph_spinner1.getSelectedItem().toString();
        String citySpinner = city_spinner.getSelectedItem().toString();

        List<String> labels = new ArrayList<>();
        List<String> mlabels = new ArrayList<>();
        String table_graph = "";

        switch (ListSpinner1){
            case "Governor":
                labels.add("ABUY");
                labels.add("ALCALA");
                labels.add("DATOR");
                labels.add("PULGAR");
                labels.add("SUAREZ");
                labels.add("VILLENA");
                labels.add("Indefinite");
                labels.add("");
                mlabels.add("ABUY");
                mlabels.add("ALCALA");
                mlabels.add("DATOR");
                mlabels.add("PULGAR");
                mlabels.add("SUAREZ");
                mlabels.add("VILLENA");
                mlabels.add("Indefinite");
                mlabels.add("Missing System");
                table_graph = "governor_Mgraph";
                break;

            case "ViceGovernor":
                labels.add("CAPINA");
                labels.add("ESTACIO");
                labels.add("MALITE");
                labels.add("NANTES");
                labels.add("Indefinite");
                labels.add("");
                mlabels.add("CAPINA");
                mlabels.add("ESTACIO");
                mlabels.add("MALITE");
                mlabels.add("NANTES");
                mlabels.add("Indefinite");
                mlabels.add("Missing System");
                table_graph = "vicegovernor_Mgraph";
                break;

            case "Congressman":
                labels.add("ALCALA");
                labels.add("MASILANG");
                labels.add("SENERES");
                labels.add("SUAREZ_AMADEO");
                labels.add("SUAREZ_DAVID");
                labels.add("Indefinite");
                labels.add("");
                mlabels.add("ALCALA");
                mlabels.add("MASILANG");
                mlabels.add("SENERES");
                mlabels.add("SUAREZ_AMADEO");
                mlabels.add("SUAREZ_DAVID");
                mlabels.add("Indefinite");
                mlabels.add("Missing System");
                table_graph = "congressman_Mgraph";
                break;

            case "Mayor":
                switch (citySpinner){
                    case "Candelaria":
                        labels.add("BOONGALING");
                        labels.add("MALIWANAG");
                        labels.add("Indefinite");
                        labels.add("");
                        mlabels.add("BOONGALING");
                        mlabels.add("MALIWANAG");
                        mlabels.add("Indefinite");
                        mlabels.add("Missing System");
                        table_graph = "candelaria_mayor_Mgraph";
                        break;

                    case "Dolores":
                        labels.add("CALAYAG");
                        labels.add("MILAN");
                        labels.add("Indefinite");
                        labels.add("");
                        mlabels.add("CALAYAG");
                        mlabels.add("MILAN");
                        mlabels.add("Indefinite");
                        mlabels.add("Missing System");
                        table_graph = "dolores_mayor_Mgraph";
                        break;

                    case "Lucena City":
                        labels.add("ALCALA");
                        labels.add("TALAGA");
                        labels.add("Indefinite");
                        labels.add("");
                        mlabels.add("ALCALA");
                        mlabels.add("TALAGA");
                        mlabels.add("Indefinite");
                        mlabels.add("Missing System");
                        table_graph = "lucena_mayor_Mgraph";
                        break;

                    case "San Antonio":
                        labels.add("HERNANDEZ");
                        labels.add("WAGAN");
                        labels.add("Indefinite");
                        labels.add("");
                        mlabels.add("HERNANDEZ");
                        mlabels.add("WAGAN");
                        mlabels.add("Indefinite");
                        mlabels.add("Missing System");
                        table_graph = "sanantonio_mayor_Mgraph";
                        break;

                    case "Sariaya":
                        labels.add("DE LA ROCA");
                        labels.add("GAYETA");
                        labels.add("Indefinite");
                        labels.add("");
                        mlabels.add("DE LA ROCA");
                        mlabels.add("GAYETA");
                        mlabels.add("Indefinite");
                        mlabels.add("Missing System");
                        table_graph = "sariaya_mayor_Mgraph";
                        break;

                    case "Tiaong":
                        labels.add("CASTILLO");
                        labels.add("PREZA");
                        labels.add("Indefinite");
                        labels.add("");
                        mlabels.add("CASTILLO");
                        mlabels.add("PREZA");
                        mlabels.add("Indefinite");
                        mlabels.add("Missing System");
                        table_graph = "tiaong_mayor_Mgraph";
                        break;

                }
                break;

            case "BoardMember":
                labels.add("ALCALA");
                labels.add("ALEJANDRINO");
                labels.add("CASULLA");
                labels.add("GONZALES");
                labels.add("LIWANAG");
                labels.add("SIO");
                labels.add("TALAGA");
                labels.add("TANADA");
                labels.add("");
                mlabels.add("ALCALA");
                mlabels.add("ALEJANDRINO");
                mlabels.add("CASULLA");
                mlabels.add("GONZALES");
                mlabels.add("LIWANAG");
                mlabels.add("SIO");
                mlabels.add("TALAGA");
                mlabels.add("TANADA");
                mlabels.add("Missing System");
                table_graph = "bokal_Mgraph";
                break;

            case "PartyList":
                table_graph = "PartyList_graph";
                break;

            case "Highest Education":
                ListSpinner1 = "Education";
                labels.add("Elementary level");
                labels.add("Elementary graduate");
                labels.add("High Schoool level");
                labels.add("High Schoool graduate");
                labels.add("College level");
                labels.add("College graduate");
                labels.add("Post graduate");
                labels.add("");
                mlabels.add("Elementary level");
                mlabels.add("Elementary graduate");
                mlabels.add("High Schoool level");
                mlabels.add("High Schoool graduate");
                mlabels.add("College level");
                mlabels.add("College graduate");
                mlabels.add("Post graduate");
                mlabels.add("Missing System");
                table_graph = "highest_education_Mgraph";
                break;

            case "Annual HH Income":
                ListSpinner1 = "Income";
                labels.add("(7,890 monthly or less)");
                labels.add("(7,891 to 15,780 monthly)");
                labels.add("(15,781 to 31,560 monthly)");
                labels.add("(31,561 to 78,900 monthly)");
                labels.add("(78,901 to 118,350 monthly)");
                labels.add("(118,351 to 157,800)");
                labels.add("(157,801 and more)");
                labels.add("");
                mlabels.add("(7,890 monthly or less)");
                mlabels.add("(7,891 to 15,780 monthly)");
                mlabels.add("(15,781 to 31,560 monthly)");
                mlabels.add("(31,561 to 78,900 monthly)");
                mlabels.add("(78,901 to 118,350 monthly)");
                mlabels.add("(118,351 to 157,800)");
                mlabels.add("(157,801 and more)");
                mlabels.add("Missing System");
                table_graph = "annual_income_Mgraph";
                break;
        }

        if ( checkNetworkConnection()){
            CountInServer(ListSpinner1,Bgypsin,table_graph,citySpinner,"online","Missing");
        }
        else
        {
            MCountInLocal(labels,mlabels,ListSpinner1,Bgypsin,table_graph,"offline",citySpinner);
        }

    }

    public void MCountInLocal(List<String> labelsLocal,List<String> MlabelsLocal,String candidateSpinner,String barangaySpinner, String table_graph_local ,String connection_local, String cityIntent){

        List<Integer> count = new ArrayList<>();
        List<String> MpartyList = new ArrayList<>();

        if (candidateSpinner.equals("PartyList")){
            DatabaseAccess createPartyListTable = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db");
            createPartyListTable.open();
            createPartyListTable.CreateDynamicTables(table_graph_local);
            //ArrayList<ListModel> SortPartyList = createPartyListTable.SortPartyList();
            createPartyListTable.close();
        }

        DatabaseAccess datacount = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db"); //count the names
        datacount.open();
        if (candidateSpinner.equals("PartyList")){
            MpartyList = datacount.getDistinctName();
            MpartyList.add("");
            for (int i=0; i<MpartyList.size();i++)
            {
                count.add(datacount.getcountnamePartylist2(MpartyList.get(i),candidateSpinner,cityIntent, barangaySpinner));
            }
        }
        else{
            for (int i=0; i<labelsLocal.size();i++)
            {
                if (candidateSpinner.equals("BoardMember")){
                    if (labelsLocal.get(i).equals("")){
                        count.add(datacount.getcountname2(labelsLocal.get(i),"ALCALA",cityIntent, barangaySpinner));
                    }
                    else count.add(datacount.getcountname2("true",labelsLocal.get(i),cityIntent, barangaySpinner));
                }
                else {count.add(datacount.getcountname2(labelsLocal.get(i),candidateSpinner,cityIntent, barangaySpinner));}
            }
        }
        datacount.close();

        DatabaseAccess insertdata = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db"); //insert data
        insertdata.open();
        if (candidateSpinner.equals("PartyList")){
            for (int i=0; i<count.size();i++)
            {
                if (MpartyList.get(i) == ""){
                    insertdata.insertPartyList(i, count.get(i),"NO ENTRY", table_graph_local);
                }else {
                    insertdata.insertPartyList(i, count.get(i),MpartyList.get(i), table_graph_local);
                }

            }
        }
        else{
            for (int i=0; i<MlabelsLocal.size();i++)
            {
                insertdata.minsertCount(i, MlabelsLocal.get(i), count.get(i), table_graph_local);
            }
        }
        insertdata.close();
        int missCount=0;
        if (candidateSpinner.equals("PartyList")){
            DatabaseAccess createPartyListTableSort = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db");
            createPartyListTableSort.open();
            for (int i=0; i<count.size();i++) {
                if (MpartyList.get(i) == "") {
                    missCount = count.get(i);
                }
            }
            createPartyListTableSort.CreateDynamicTablesStatic("sample_table",missCount, "No Entry");
            createPartyListTableSort.close();

            DatabaseAccess insertAscending = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db"); //insert data
            insertAscending.open();
            SortPartyList = insertAscending.SortPartyListRaw();
            ListModel listModel;
            for(int i=0; i<SortPartyList.size(); i++){
                listModel = SortPartyList.get(i);
                insertAscending.insertPartyList(i+1, listModel.getCount(),listModel.getName(), "sample_table");
            }
            insertAscending.close();
        }
        progressDialog.dismiss();

        if (candidateSpinner.equals("BoardMember")) {
            openKagawadGraphM(candidateSpinner, table_graph_local, connection_local);
        }
        else if(candidateSpinner.equals("PartyList")){
            openMissingGraph2(candidateSpinner, "sample_table", connection_local,cityIntent);
        }
        else {
            //openMissingGraph(ListSpinner, table_graph);
            openMissingGraph2(candidateSpinner ,table_graph_local, connection_local,cityIntent);
        }
    }


    public void CountInServer(final String candidateSpinner,  final String barangaySpinner, final String table_graph_server,final String cityIntent, final String connection_server, final String generate){
        RequestQueue queue = Volley.newRequestQueue(GraphActivity2.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_COUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(GraphActivity2.this, response+"", Toast.LENGTH_LONG).show();
                        try {
                            if (candidateSpinner.equals("PartyList")){
                                DatabaseAccess createPartyListTable = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db");
                                createPartyListTable.open();
                                createPartyListTable.CreateDynamicTables(table_graph_server);
                                //ArrayList<ListModel> SortPartyList = createPartyListTable.SortPartyList();
                                createPartyListTable.close();
                            }
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);


                            int countArr = array.length();
                            if (countArr != 0){
                                //traversing through all the object
                                DatabaseAccess insertdata = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db"); //insert data
                                insertdata.open();
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject datalist = array.getJSONObject(i);

                                    String CandidateName = datalist.getString("CandidateName");
                                    int Count = datalist.getInt("Count");
                                    switch (generate){
                                        case "withoutMissing":
                                            if (candidateSpinner.equals("PartyList")){
                                                insertdata.insertPartyList(i, Count,CandidateName, table_graph_server);
                                            }
                                            else {
                                                insertdata.insertCount(i, CandidateName, Count, table_graph_server);
                                            }
                                            break;
                                        case "Missing":
                                            if (candidateSpinner.equals("PartyList")){
                                                if (CandidateName.equals("")){
                                                    insertdata.insertPartyList(i, Count,"NO ENTRY", table_graph_server);
                                                }
                                                else{
                                                    insertdata.insertPartyList(i, Count,CandidateName, table_graph_server);
                                                }
                                            }
                                            else {
                                                if (CandidateName.equals("")){
                                                    CandidateName = "Missing System";
                                                    insertdata.minsertCount(i, CandidateName, Count, table_graph_server);
                                                }
                                                else {
                                                    insertdata.minsertCount(i, CandidateName, Count, table_graph_server);
                                                }
                                            }

                                            break;
                                    }

                                }
                                insertdata.close();

                                if (candidateSpinner.equals("PartyList")){
                                    DatabaseAccess createPartyListTableSort = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db");
                                    createPartyListTableSort.open();
                                    createPartyListTableSort.CreateDynamicTables("sample_table");
                                    createPartyListTableSort.close();

                                    DatabaseAccess insertAscending = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db"); //insert data
                                    insertAscending.open();
                                    SortPartyList = insertAscending.SortPartyList(table_graph_server);
                                    ListModel listModel;
                                    for(int i=0; i<SortPartyList.size(); i++){
                                        listModel = SortPartyList.get(i);
                                        //Toast.makeText(GraphActivity.this,listModel.getResult()+" "+listModel.getNameList(),Toast.LENGTH_LONG).show();
                                        insertAscending.insertPartyList(i, listModel.getCount(),listModel.getName(), "sample_table");
                                    }
                                    insertAscending.close();
                                }
                                progressDialog.dismiss();
                                switch (generate){
                                    case "withoutMissing":
                                        if (candidateSpinner.equals("BoardMember")) {
                                            openKagawadGraph(candidateSpinner, table_graph_server,connection_server);
                                        }
                                        else if(candidateSpinner.equals("PartyList")){
                                            openGraph2(candidateSpinner, "sample_table", connection_server,cityIntent);
                                        }
                                        else {
                                            openGraph2(candidateSpinner, table_graph_server,connection_server,cityIntent);
                                        }
                                        break;
                                    case "Missing":
                                        if (candidateSpinner.equals("BoardMember")) {
                                            openKagawadGraphM(candidateSpinner, table_graph_server,connection_server);
                                        }
                                        else if(candidateSpinner.equals("PartyList")){
                                            openMissingGraph2(candidateSpinner, "sample_table", connection_server,cityIntent);
                                        }
                                        else {
                                            openMissingGraph2(candidateSpinner, table_graph_server,connection_server,cityIntent);
                                        }
                                        break;
                                }

                            }
                            else{
                                Toast.makeText(GraphActivity2.this, "Data is Empty!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GraphActivity2.this, e+"", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GraphActivity2.this, error+"", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("candidate", candidateSpinner);
                params.put("city", cityIntent);
                params.put("barangay", barangaySpinner);
                params.put("generate", generate);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void openGraph2(String label , String table_graph, String connection, String city){

        Intent intent = new Intent(this, GenerateGraphView.class);
        intent.putExtra("Label",label);
        intent.putExtra("Table",table_graph);
        intent.putExtra("Connection",connection);
        intent.putExtra("City",city);
        startActivity(intent);
    }

    public void openMissingGraph2(String label , String table_graph, String connection, String city){

        Intent intent = new Intent(this, mGenerateGraphView.class);
        intent.putExtra("Label",label);
        intent.putExtra("Table",table_graph);
        intent.putExtra("Connection",connection);
        intent.putExtra("City",city);
        startActivity(intent);
    }

    public void openKagawadGraph(String label, String table_graph, String connection){

        Intent intent = new Intent(this, Graph_Kagawad.class);
        intent.putExtra("Label",label);
        intent.putExtra("Table",table_graph);
        intent.putExtra("Connection",connection);
        startActivity(intent);
    }

    public void openKagawadGraphM(String label, String table_graph, String connection){

        Intent intent = new Intent(this, Graph_KagawadM.class);
        intent.putExtra("Label",label);
        intent.putExtra("Table",table_graph);
        intent.putExtra("Connection",connection);
        startActivity(intent);
    }

    public boolean checkNetworkConnection()
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());

    }

    public void generateLineGraph(){
        Button LineGraph = findViewById(R.id.linechartbgy_btn);
        LineGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Linegraph();
                    }
                },1000);

            }
        });
    }

    public void Linegraph(){
        Bgypsin = barangay_spinner.getSelectedItem().toString();
        ListSpinner1 = graph_spinner1.getSelectedItem().toString();
        final String citySpinnerLine = city_spinner.getSelectedItem().toString();

        DatabaseAccess databaseAccessBatch = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db");
        databaseAccessBatch.open();
        int BatchCount = databaseAccessBatch.getBatchCount();
        databaseAccessBatch.close();

        if (BatchCount == 1){
            progressDialog.dismiss();
            Toast.makeText(GraphActivity2.this, "Cannot Generate Line Graph. Batch should be more than 1.", Toast.LENGTH_SHORT).show();
        }
        else {
            if ( checkNetworkConnection()){

                if (ListSpinner1.equals("PartyList")) {
                    progressDialog.dismiss();
                    Toast.makeText(GraphActivity2.this, "Chart not available at the moment", Toast.LENGTH_SHORT).show();
                }
                else {

                    for (int x = 1; x <= BatchCount; x++) {
                        String batchName = "batch" + x + "_list";
                        CountLineServer(ListSpinner1, Bgypsin, "LineGraph_bgy", batchName, citySpinnerLine);
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            openLineGraph(ListSpinner1, "LineGraph_bgy", citySpinnerLine);
                        }
                    }, 10000);
                }
            }
            else
            {
                progressDialog.dismiss();
                Toast.makeText(GraphActivity2.this, "Connect to network to generate line chart.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void openLineGraph(String label, String table_graph, String city){
        Intent intent = new Intent(this, LineGraph.class);
        intent.putExtra("Label",label);
        intent.putExtra("Table",table_graph);
        intent.putExtra("City",city);
        startActivity(intent);
    }

    public void CountLineServer(final String canSpinner, final String bgySpinner, final String table_lineGraph,final String batch_name,final String cityIntent){
        RequestQueue queue = Volley.newRequestQueue(GraphActivity2.this);
        StringRequest stringRequestLine = new StringRequest(Request.Method.POST, URL_LineCOUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            int countArr = array.length();
                            if (countArr != 0){
                                DatabaseAccess insertdata = DatabaseAccess.getInstance(GraphActivity2.this,"voters.db"); //insert data
                                insertdata.open();
                                for (int i = 0; i < array.length(); i++) {

                                    //getting product object from json array
                                    JSONObject datalist = array.getJSONObject(i);
                                    String CandidateName = datalist.getString("CandidateName");
                                    String BatchName = datalist.getString("BatchName");
                                    int Count = datalist.getInt("Count");
                                    insertdata.insertLineCount(Count,CandidateName,BatchName,table_lineGraph);

                                }
                                insertdata.close();
                            }
                            else{
                                Toast.makeText(GraphActivity2.this, "Data is Empty!", Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(UploadToServer.this, "Sending Failed! No Internet Connection.", Toast.LENGTH_LONG).show();
//                        progressDialog.dismiss();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("candidate", canSpinner);
                params.put("barangay", bgySpinner);
                params.put("city", cityIntent);
                params.put("batch_name", batch_name);
                return params;
            }
        };
        stringRequestLine.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequestLine);
    }

    private void homeButton() {
        Button btnBack = (Button) findViewById(R.id.homebutton);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GraphActivity2.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    private void homeButton2() {
        Button btnBack = (Button) findViewById(R.id.homebutton);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GraphActivity2.this, Main2Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    private void backGraphButton(){
        Button prevbutton = findViewById(R.id.backgraphbtn2);
        prevbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
