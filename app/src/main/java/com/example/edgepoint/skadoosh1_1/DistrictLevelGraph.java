package com.example.edgepoint.skadoosh1_1;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class DistrictLevelGraph extends AppCompatActivity {
    Spinner graph_spinner;
    public ArrayAdapter adapterGraph;
    public static String ListSpinner;
    ProgressDialog progressDialog;
    BroadcastReceiver broadcastReceiver;
    private ArrayList<ListModel> SortPartyList;

    public static final String URL_COUNT = "http://ramores.com/rema/php_skadoosh/getCountDistrict.php";
    public static final String URL_LineCOUNT = "http://ramores.com/rema/php_skadoosh/getCountLineDistrict.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_level_graph);

        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        progressDialog = new ProgressDialog(DistrictLevelGraph.this);
        progressDialog.setMessage("Generating Graph...");

        graph_spinner = findViewById(R.id.district_spinner);
        adapterGraph = ArrayAdapter.createFromResource(this, R.array.graph_district_column, android.R.layout.simple_spinner_item);
        adapterGraph.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        graph_spinner.setAdapter(adapterGraph);

        backGraphButton();
        generateGraph();
        generateMissingGraph();
        generateLineGraph();

    }

    public void generateGraph(){

        Button generategraphbtn =  findViewById(R.id.generategraphbtn2);
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
        ListSpinner = graph_spinner.getSelectedItem().toString();
        List<String> labels = new ArrayList<>();
        String table_graph = "";
        switch (ListSpinner){

            case "Governor":
                labels.add("ABUY");
                labels.add("ALCALA");
                labels.add("DATOR");
                labels.add("PULGAR");
                labels.add("SUAREZ");
                labels.add("VILLENA");
                labels.add("Undecided");
                table_graph = "governor_graph";
                break;

            case "ViceGovernor":
                labels.add("CAPINA");
                labels.add("ESTACIO");
                labels.add("MALITE");
                labels.add("NANTES");
                labels.add("Undecided");
                table_graph = "vicegovernor_graph";
                break;

            case "Congressman":
                labels.add("ALCALA");
                labels.add("MASILANG");
                labels.add("SENERES");
                labels.add("SUAREZ_AMADEO");
                labels.add("SUAREZ_DAVID");
                labels.add("Undecided");
                table_graph = "congressman_graph";
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
        }

        if ( checkNetworkConnection()){
            CountInServer(ListSpinner,table_graph,"online","withoutMissing");
        }
        else
        {

            CountInLocal(labels,ListSpinner,table_graph,"offline");
        }

    }

    public void CountInLocal(List<String> labelsLocal,String candidateSpinner, String table_graph_local, String connection_local){
        List<Integer> count = new ArrayList<>();
        List<String> partyList = new ArrayList<>();

        if (candidateSpinner.equals("PartyList")){
            DatabaseAccess createPartyListTable = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db");
            createPartyListTable.open();
            createPartyListTable.CreateDynamicTables(table_graph_local);
            createPartyListTable.close();
        }

        DatabaseAccess datacount = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db"); //count the names
        datacount.open();

        if (candidateSpinner.equals("PartyList")){
            partyList = datacount.getDistinctName();
            for (int i=0; i<partyList.size();i++)
            {
                count.add(datacount.getcountnameDistrict(partyList.get(i),ListSpinner));
            }
        }
        else
        {
            for (int i=0; i<labelsLocal.size();i++)
            {
                if (candidateSpinner.equals("BoardMember")){
                    count.add(datacount.getcountnameDistrict("true",labelsLocal.get(i)));
                }
                else {count.add(datacount.getcountnameDistrict(labelsLocal.get(i),ListSpinner));}
            }
        }

        datacount.close();

        DatabaseAccess insertdata = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db"); //insert data
        insertdata.open();
        if (candidateSpinner.equals("PartyList")){
            for (int i=0; i<count.size();i++)
            {
                insertdata.insertPartyList(i, count.get(i),partyList.get(i), table_graph_local);
            }
        }
        else
        {
            for (int i=0; i<count.size();i++)
            {
                insertdata.insertCount(i, labelsLocal.get(i), count.get(i), table_graph_local);
            }
        }

        insertdata.close();

        if (candidateSpinner.equals("PartyList")){
            DatabaseAccess createPartyListTableSort = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db");
            createPartyListTableSort.open();
            createPartyListTableSort.CreateDynamicTables("sample_table");
            createPartyListTableSort.close();

            DatabaseAccess insertAscending = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db"); //insert data
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
            openGraph(candidateSpinner, "sample_table", connection_local);
        }
        else {
            openGraph(candidateSpinner, table_graph_local, connection_local);
        }

    }

    public void generateMissingGraph(){

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
        ListSpinner = graph_spinner.getSelectedItem().toString();
        List<String> labels = new ArrayList<>();
        List<String> mlabels = new ArrayList<>();
        String table_graph = "";
        switch (ListSpinner){
            case "Governor":
                labels.add("ABUY");
                labels.add("ALCALA");
                labels.add("DATOR");
                labels.add("PULGAR");
                labels.add("SUAREZ");
                labels.add("VILLENA");
                labels.add("Undecided");
                labels.add("");
                mlabels.add("ABUY");
                mlabels.add("ALCALA");
                mlabels.add("DATOR");
                mlabels.add("PULGAR");
                mlabels.add("SUAREZ");
                mlabels.add("VILLENA");
                mlabels.add("Undecided");
                mlabels.add("Missing System");
                table_graph = "governor_Mgraph";
                break;

            case "ViceGovernor":
                labels.add("CAPINA");
                labels.add("ESTACIO");
                labels.add("MALITE");
                labels.add("NANTES");
                labels.add("Undecided");
                labels.add("");
                mlabels.add("CAPINA");
                mlabels.add("ESTACIO");
                mlabels.add("MALITE");
                mlabels.add("NANTES");
                mlabels.add("Undecided");
                mlabels.add("Missing System");
                table_graph = "vicegovernor_Mgraph";
                break;

            case "Congressman":
                labels.add("ALCALA");
                labels.add("MASILANG");
                labels.add("SENERES");
                labels.add("SUAREZ_AMADEO");
                labels.add("SUAREZ_DAVID");
                labels.add("Undecided");
                labels.add("");
                mlabels.add("ALCALA");
                mlabels.add("MASILANG");
                mlabels.add("SENERES");
                mlabels.add("SUAREZ_AMADEO");
                mlabels.add("SUAREZ_DAVID");
                mlabels.add("Undecided");
                mlabels.add("Missing System");
                table_graph = "congressman_Mgraph";
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
        }

        if ( checkNetworkConnection()){
            CountInServer(ListSpinner,table_graph,"online","Missing");
        }
        else
        {
            MCountInLocal(labels,mlabels,ListSpinner,table_graph,"offline");
        }

    }

    public void MCountInLocal(List<String> labelsLocal,List<String> MlabelsLocal,String candidateSpinner, String table_graph_local, String connection_local){
        List<Integer> count = new ArrayList<>();
        List<String> MpartyList = new ArrayList<>();
        if (candidateSpinner.equals("PartyList")){
            DatabaseAccess createPartyListTable = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db");
            createPartyListTable.open();
            createPartyListTable.CreateDynamicTables(table_graph_local);
            createPartyListTable.close();
        }

        DatabaseAccess datacount = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db"); //count the names
        datacount.open();
        if (candidateSpinner.equals("PartyList")){
            MpartyList = datacount.getDistinctName();
            MpartyList.add("");
            for (int i=0; i<MpartyList.size();i++)
            {
                count.add(datacount.getcountnameDistrict(MpartyList.get(i),candidateSpinner));
            }
        }
        else {
            for (int i=0; i<labelsLocal.size();i++)
            {
                if (candidateSpinner.equals("BoardMember")){
                    if (labelsLocal.get(i).equals("")){
                        count.add(datacount.getcountnameDistrict(labelsLocal.get(i),"ALCALA"));
                    }
                    else count.add(datacount.getcountnameDistrict("true",labelsLocal.get(i)));
                }
                else {count.add(datacount.getcountnameDistrict(labelsLocal.get(i),candidateSpinner));}
            }
        }
        datacount.close();


        DatabaseAccess insertdata = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db"); //insert data
        insertdata.open();
        if (candidateSpinner.equals("PartyList")){
            for (int i=0; i<count.size();i++)
            {
                if (MpartyList.get(i) == ""){
                    insertdata.insertPartyList(i, count.get(i),"NO ENTRY", table_graph_local);
                }
                else{
                    insertdata.insertPartyList(i, count.get(i),MpartyList.get(i), table_graph_local);
                }
            }
        }
        else{
            for (int i=0; i<MlabelsLocal.size();i++)
            {
                insertdata.minsertCount(i, MlabelsLocal.get(i), count.get(i),table_graph_local);
            }
        }
        insertdata.close();

        int missCount=0;
        if (candidateSpinner.equals("PartyList")){
            DatabaseAccess createPartyListTableSort = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db");
            createPartyListTableSort.open();
            for (int i=0; i<count.size();i++) {
                if (MpartyList.get(i) == "") {
                    missCount = count.get(i);
                }
            }
            createPartyListTableSort.CreateDynamicTablesStatic("sample_table",missCount, "No Entry");
            createPartyListTableSort.close();

            DatabaseAccess insertAscending = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db"); //insert data
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
            openMissingGraph(candidateSpinner, "sample_table", connection_local);
        }
        else {
            openMissingGraph(candidateSpinner, table_graph_local, connection_local);
        }

    }

    public void CountInServer(final String candidateSpinner, final String table_graph_server, final String connection_server,final String generate){
        RequestQueue queue = Volley.newRequestQueue(DistrictLevelGraph.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_COUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (candidateSpinner.equals("PartyList")){
                                DatabaseAccess createPartyListTable = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db");
                                createPartyListTable.open();
                                createPartyListTable.CreateDynamicTables(table_graph_server);
                                createPartyListTable.close();
                            }
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            int countArr = array.length();
                            if (countArr != 0){
                                //traversing through all the object
                                DatabaseAccess insertdata = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db"); //insert data
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
                                    DatabaseAccess createPartyListTableSort = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db");
                                    createPartyListTableSort.open();
                                    createPartyListTableSort.CreateDynamicTables("sample_table");
                                    createPartyListTableSort.close();

                                    DatabaseAccess insertAscending = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db"); //insert data
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
                                            openGraph(candidateSpinner, "sample_table", connection_server);
                                        }
                                        else {
                                            openGraph(candidateSpinner, table_graph_server,connection_server);
                                        }
                                        break;
                                    case "Missing":
                                        if (candidateSpinner.equals("BoardMember")) {
                                            openKagawadGraphM(candidateSpinner, table_graph_server,connection_server);
                                        }
                                        else if(candidateSpinner.equals("PartyList")){
                                            openMissingGraph(candidateSpinner, "sample_table", connection_server);
                                        }
                                        else {
                                            openMissingGraph(candidateSpinner, table_graph_server,connection_server);
                                        }
                                        break;
                                }


                            }
                            else{
                                Toast.makeText(DistrictLevelGraph.this, "Data is Empty!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DistrictLevelGraph.this, error+" ", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("candidate", candidateSpinner);
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

    public void openGraph(String label, String table_graph, String connection){

        Intent intent = new Intent(this, GenerateGraphView.class);
        intent.putExtra("Label",label);
        intent.putExtra("Table",table_graph);
        intent.putExtra("Connection",connection);
        intent.putExtra("City","none");
        startActivity(intent);
    }

    public void openMissingGraph(String label, String table_graph, String connection){

        Intent intent = new Intent(this, mGenerateGraphView.class);
        intent.putExtra("Label",label);
        intent.putExtra("Table",table_graph);
        intent.putExtra("Connection",connection);
        intent.putExtra("City","none");
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

    private void backGraphButton(){
        Button prevbutton = findViewById(R.id.backgraphbtn2);
        prevbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
        ListSpinner = graph_spinner.getSelectedItem().toString();

        DatabaseAccess databaseAccessBatch = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db");
        databaseAccessBatch.open();
        int BatchCount = databaseAccessBatch.getBatchCount();
        databaseAccessBatch.close();

        if (BatchCount == 1){
            progressDialog.dismiss();
            Toast.makeText(DistrictLevelGraph.this, "Cannot Generate Line Graph. Batch should be more than 1.", Toast.LENGTH_SHORT).show();
        }
        else {
            if ( checkNetworkConnection()){

                if (ListSpinner.equals("PartyList")){
                    progressDialog.dismiss();
                    Toast.makeText(DistrictLevelGraph.this, "Chart not available at the moment", Toast.LENGTH_SHORT).show();
                }
                else {

                    for (int x = 1; x <= BatchCount; x++) {
                        String batchName = "batch" + x + "_list";
                        CountLineServer(ListSpinner, "LineGraph_dsrct", batchName);
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            openLineGraph(ListSpinner, "LineGraph_dsrct");
                        }
                    }, 10000);
                }

            }
            else
            {
                progressDialog.dismiss();
                Toast.makeText(DistrictLevelGraph.this, "Connect to network to generate line chart.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void openLineGraph(String label, String table_graph){
        Intent intent = new Intent(this, LineGraph.class);
        intent.putExtra("Label",label);
        intent.putExtra("Table",table_graph);
        intent.putExtra("City","none");
        startActivity(intent);
    }

    public void CountLineServer(final String canSpinner, final String table_lineGraph,final String batch_name){
        RequestQueue queue = Volley.newRequestQueue(DistrictLevelGraph.this);
        StringRequest stringRequestLine = new StringRequest(Request.Method.POST, URL_LineCOUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            int countArr = array.length();
                            if (countArr != 0){
                                DatabaseAccess insertdata = DatabaseAccess.getInstance(DistrictLevelGraph.this,"voters.db"); //insert data
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
                                Toast.makeText(DistrictLevelGraph.this, "Data is Empty!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DistrictLevelGraph.this, "Sending Failed! No Internet Connection.", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("candidate", canSpinner);
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

}
