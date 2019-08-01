package com.example.edgepoint.skadoosh1_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private String[] columnsLogin = { "id" ,"username" ,"password"};
    private String[] columnsBatch = { "id" ,"batch_name","batch_count"};
    private String voters_table = "voters_table";
    private String upload_table = "upload_table";
    private String users_table = "users_table";
    private String admin_table = "admin_table";
    private String batch_table = "batch_table";
    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context, String database_name) {
        this.openHelper = new DatabaseOpenHelper(context,database_name);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context,String database_name) {
        if (instance == null) {
            instance = new DatabaseAccess(context,database_name);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all voters from the database.
     *
     * @return a List of voters
     */

    public boolean loginInfo(String username,String password, String table) {
        Cursor cursor = database.query(table, columnsLogin , "username=? AND password=?",new String[] {username,password}, null, null, null, null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public boolean userAccess(String userAccess) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_access",userAccess);
        database.update("access_table", contentValues, "id=?",new String[] {"1"});
        return true;
    }

    public String getUserAccess() {
        String userAccess = "";
        Cursor cursor = database.query("access_table", null , "id=?",new String[] {"1"}, null, null, null, null);
        cursor.moveToFirst();
        userAccess = cursor.getString(cursor.getColumnIndex("user_access"));
        cursor.moveToNext();
        cursor.close();
        return userAccess;
    }

    public boolean resetUserAccess(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_access","");
        database.update("access_table", contentValues, "id=?",new String[] {"1"});
        database.close();
        return true;
    }

    public int graphlevelcount(String username,String password, String table) {
        int count = 0;
        Cursor cursor = database.query(table, columnsLogin , "username=? AND password=?",new String[] {username,password}, null, null, null, null);
        cursor.moveToFirst();
        count = cursor.getInt(cursor.getColumnIndex("id"));
        cursor.moveToNext();
        cursor.close();
        return count;
    }

    public boolean votersPreference(String password) {
        Cursor cursor = database.query("admin_table", columnsLogin , "password=?",new String[] {password}, null, null, null, null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public List<String> getDatabase(String v ,String sel,String group,int x) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(voters_table, null , sel, new String[] {v}, group, null, group + " ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(group)));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public String getList(String votersname, String city ,String barangay ,String precinct,String sel,String group,int x) {
        String list = "";
        Cursor cursor = database.query(voters_table, null , sel, new String[] {votersname,city,barangay,precinct}, group, null, null, null);
        cursor.moveToFirst();
            list = cursor.getString(x);
            cursor.moveToNext();
        cursor.close();
        return list;
    }

    public List<String> setDatabase(String group,int x) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(true,voters_table, null , null,null, group, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(group)));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

//    ============================ getDatabase city, barangay, precinct, voters =====================================

    public List<String> getCity(String group) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(true,voters_table, null , null,null, group, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(group)));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getBarangay(String city ,String sel_city,String group) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(voters_table, null , sel_city, new String[] {city}, group, null, group + " ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(group)));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getPrecinct(String city ,String barangay ,String sel_city_barangay,String group) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(voters_table, null , sel_city_barangay, new String[] {city,barangay}, group, null, group + " ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(group)));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVotersName(String city ,String barangay ,String precinct ,String sel_city_barangay_precinct,String group) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(voters_table, null , sel_city_barangay_precinct, new String[] {city,barangay,precinct}, group, null, group + " ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(group)));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    public List<String> getIndicator(String city ,String barangay ,String precinct ,String sel_city_barangay_precinct,String group) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(voters_table, null , sel_city_barangay_precinct, new String[] {city,barangay,precinct}, group, null, group + " ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex("Indicator")));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    public List<String> getDeceased(String city ,String barangay ,String precinct ,String sel_city_barangay_precinct,String group) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(voters_table, null , sel_city_barangay_precinct, new String[] {city,barangay,precinct}, group, null, group + " ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex("Deceased")));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    public String getDeceasedStr(String votersname) {
        String str = "";
        Cursor cursor = database.query(voters_table, null , "VotersName=?", new String[] {votersname}, null, null, null, null);
        cursor.moveToFirst();
        str = cursor.getString(cursor.getColumnIndex("Deceased"));
        cursor.moveToNext();
        cursor.close();
        return str;
    }

//    ===============================================================================================================

    public List<String> setSeacrhDatabase(String group,int x) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(true,voters_table, null , null,null, group, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(x));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> setInfo(String v) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(voters_table, null , "VotersName=?",new String[] {v}, null, null, null, null);
        cursor.moveToFirst();
        for(int i=0; i<cursor.getColumnCount();i++)
        {
            list.add(cursor.getString(i));
        }
        cursor.close();
        return list;
    }

    public List<String> setUpload(String v) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(upload_table, null , "VotersName=?",new String[] {v}, null, null, null, null);
        cursor.moveToFirst();
        for(int i=0; i<cursor.getColumnCount();i++)
        {
            list.add(cursor.getString(i));
        }
        cursor.close();
        return list;
    }

    public String getBatchName() {
        String batchlist = "";
        Cursor cursor = database.query(batch_table, columnsBatch , "id=?",new String[] {"1"}, null, null, null, null);
        cursor.moveToFirst();
        batchlist = cursor.getString(1);
        cursor.moveToNext();
        cursor.close();
        return batchlist;
    }

    public int getBatchCount() {
        int batchlist = 0;
        Cursor cursor = database.query(batch_table, columnsBatch , "id=?",new String[] {"1"}, null, null, null, null);
        cursor.moveToFirst();
        batchlist = cursor.getInt(2);
        cursor.moveToNext();
        cursor.close();
        return batchlist;
    }

    public long getUploadDate() {

        Cursor cursor = database.rawQuery("SELECT MAX(UploadedAt) AS UploadedAt FROM voters_table WHERE UploadedAt !=''", null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex("UploadedAt");
        long Uploaddata = cursor.getLong(index);
        cursor.moveToNext();
        cursor.close();
        return Uploaddata;
    }

    public boolean updateBatch(String batch_name, int batch_count) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("batch_name",batch_name);
        contentValues.put("batch_count",batch_count);
        database.update(batch_table,contentValues,"id=?",new String[] {"1"});
        return true;
    }

    public boolean checkDuplicateBatch(String batch_name) {
        String[] tableColumns = new String[] {"Batch_Label"};
        Cursor cursor = database.query("LineGraph",tableColumns,"Batch_Label=?",new String[] {batch_name},null,null,null);

        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        // return all notes
        return false;
    }

    public boolean updateBatchFile(String batch_name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Batch_Label",batch_name);
        database.update("LineGraph",  contentValues, "Batch_Label=?",new String[] {batch_name});
        database.update("LineGraph_dsrct",  contentValues, "Batch_Label=?",new String[] {batch_name});
        database.update("LineGraph_bgy",  contentValues, "Batch_Label=?",new String[] {batch_name});
        database.update("LineGraph_pct",  contentValues, "Batch_Label=?",new String[] {batch_name});
        return true;
    }

    public boolean insertBatch(String batch_name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Batch_Label",batch_name);
        database.insertWithOnConflict("LineGraph", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        database.insertWithOnConflict("LineGraph_dsrct", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        database.insertWithOnConflict("LineGraph_bgy", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        database.insertWithOnConflict("LineGraph_pct", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public boolean checkDuplicateUpload(String votersname) {
        String[] tableColumns = new String[] {"VotersName"};
        Cursor cursor = database.query(upload_table,tableColumns,"VotersName=?",new String[] {votersname},null,null,null);

        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        // return all notes
        return false;
    }

    public boolean insertOnConflictData(String votersname,String Governor, String ViceGovernor,String Congressman,String Mayor,String ALCALA, String ALEJANDRINO,
                                        String CASULLA,String GONZALES,String LIWANAG,String SIO,String TALAGA,String TANADA, String Indicator, String Deceased,
                                        String PartyList, String Encoder, String Contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("VotersName",votersname);
        contentValues.put("Governor",Governor);
        contentValues.put("ViceGovernor",ViceGovernor);
        contentValues.put("Congressman",Congressman);
        contentValues.put("Mayor",Mayor);
        contentValues.put("ALCALA",ALCALA);
        contentValues.put("ALEJANDRINO",ALEJANDRINO);
        contentValues.put("CASULLA",CASULLA);
        contentValues.put("GONZALES",GONZALES);
        contentValues.put("LIWANAG",LIWANAG);
        contentValues.put("SIO",SIO);
        contentValues.put("TALAGA",TALAGA);
        contentValues.put("TANADA",TANADA);
        contentValues.put("Indicator",Indicator);
        contentValues.put("Deceased",Deceased);
        contentValues.put("PartyList",PartyList);
        contentValues.put("Encoder",Encoder);
        contentValues.put("Contact",Contact);
        database.update(voters_table, contentValues, "VotersName=?",new String[] {votersname});
        database.insertWithOnConflict(upload_table, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public boolean updateData(String votersname,String Governor, String ViceGovernor,String Congressman,String Mayor,String ALCALA, String ALEJANDRINO,
                              String CASULLA,String GONZALES,String LIWANAG,String SIO,String TALAGA,String TANADA, String Indicator, String Deceased,
                              String PartyList, String Encoder, String Contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("VotersName",votersname);
        contentValues.put("Governor",Governor);
        contentValues.put("ViceGovernor",ViceGovernor);
        contentValues.put("Congressman",Congressman);
        contentValues.put("Mayor",Mayor);
        contentValues.put("ALCALA",ALCALA);
        contentValues.put("ALEJANDRINO",ALEJANDRINO);
        contentValues.put("CASULLA",CASULLA);
        contentValues.put("GONZALES",GONZALES);
        contentValues.put("LIWANAG",LIWANAG);
        contentValues.put("SIO",SIO);
        contentValues.put("TALAGA",TALAGA);
        contentValues.put("TANADA",TANADA);
        contentValues.put("Indicator",Indicator);
        contentValues.put("Deceased",Deceased);
        contentValues.put("PartyList",PartyList);
        contentValues.put("Encoder",Encoder);
        contentValues.put("Contact",Contact);
            database.update(voters_table, contentValues, "VotersName=?",new String[] {votersname});
            database.update(upload_table, contentValues, "VotersName=?",new String[] {votersname});
        return true;
    }

    public boolean updateFromServer(long uploadedat,String votersname,String Governor, String ViceGovernor,String Congressman,String Mayor,
                                    String ALCALA, String ALEJANDRINO, String CASULLA,String GONZALES,String LIWANAG,String SIO,String TALAGA,String TANADA,
                                    String Indicator, String Deceased, String PartyList, String Encoder, String Contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("UploadedAt",uploadedat);
        contentValues.put("VotersName",votersname);
        contentValues.put("Governor",Governor);
        contentValues.put("ViceGovernor",ViceGovernor);
        contentValues.put("Congressman",Congressman);
        contentValues.put("Mayor",Mayor);
        contentValues.put("ALCALA",ALCALA);
        contentValues.put("ALEJANDRINO",ALEJANDRINO);
        contentValues.put("CASULLA",CASULLA);
        contentValues.put("GONZALES",GONZALES);
        contentValues.put("LIWANAG",LIWANAG);
        contentValues.put("SIO",SIO);
        contentValues.put("TALAGA",TALAGA);
        contentValues.put("TANADA",TANADA);
        contentValues.put("Indicator",Indicator);
        contentValues.put("Deceased",Deceased);
        contentValues.put("PartyList",PartyList);
        contentValues.put("Encoder",Encoder);
        contentValues.put("Contact",Contact);
        database.update(voters_table, contentValues, "VotersName = ? AND Governor != ? AND ViceGovernor != ? AND Congressman != ? AND Mayor != ? AND ALCALA != ? AND ALEJANDRINO != ?" +
                "AND CASULLA != ? AND GONZALES != ? AND LIWANAG != ? AND SIO != ? AND TALAGA != ? AND TANADA != ?",new String[] {votersname,Governor,ViceGovernor,
                Congressman,Mayor,ALCALA,ALEJANDRINO,CASULLA,GONZALES,LIWANAG,SIO,TALAGA,TANADA});

        return true;
    }

    public boolean updateLogin(int id,String username,String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("username",username);
        contentValues.put("password",password);
        database.update(admin_table, contentValues, "id="+id,null);
        return true;
    }

    public List<String> getUploadDatabase(String group,int x) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(upload_table, null , null,null, group, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(x));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<ViewInfoRecycler> getRecyclerVoterName(){
        ArrayList<ViewInfoRecycler> voters = new ArrayList<>();
        Cursor csr = database.query(voters_table,new String[]{"VotersName"},null,null,null,null,"VotersName ASC");
        while(csr.moveToNext()) {
            voters.add(new ViewInfoRecycler(csr.getString(csr.getColumnIndex("VotersName"))));
        }
        csr.close();
        return voters;
    }

    public ArrayList<ViewInfoRecycler> getRecyclerVoterNamebyCity(String City){
        ArrayList<ViewInfoRecycler> voters = new ArrayList<>();
        Cursor csr = database.query(voters_table,new String[]{"VotersName"},"City_Municipality=?",new String[] {City},null,null,"VotersName ASC");
        while(csr.moveToNext()) {
            voters.add(new ViewInfoRecycler(csr.getString(csr.getColumnIndex("VotersName"))));
        }
        csr.close();
        return voters;
    }

    public Integer getcountnameDistrict(String namex, String columnName) {
        int count = 0;
        Cursor mCount= database.rawQuery("select count(" + columnName + ") from voters_table where "+ columnName + "='" + namex + "' ", null);
        mCount.moveToFirst();
        count=mCount.getInt(0);
        mCount.close();
        return count;

    }

    public Integer getcountname(String namex, String columnName, String columnCity) {
        int count = 0;
        Cursor mCount= database.rawQuery("select count(" + columnName + ") from voters_table where City_Municipality = '"+columnCity+"' and "+ columnName + "='" + namex + "' ", null);
        mCount.moveToFirst();
        count=mCount.getInt(0);
        mCount.close();
        return count;

    }

    public Integer getcountname2(String namex, String columnName, String columnCity, String brgy) {
        int count = 0;
        String[] selectionArg = {columnCity,brgy, namex};
        Cursor mCount= database.rawQuery("select count(" + columnName + ") from voters_table where City_Municipality =? and Barangay = ? and " + columnName + "= ? ", selectionArg);
        mCount.moveToFirst();
        count=mCount.getInt(0);
        mCount.close();
        return count;

    }

    public Integer getcountname3(String namex, String columnName, String columnCity, String precincto) {
        int count = 0;
        String[] selectionArg = {columnCity,precincto, namex};
        Cursor mCount= database.rawQuery("select count(" + columnName + ") from voters_table where City_Municipality =? and Precinct = ? and " + columnName + "= ? ", selectionArg);
        mCount.moveToFirst();
        count=mCount.getInt(0);
        mCount.close();
        return count;

    }

    public boolean insertCount(int xindex, String distinctnames, int distinctcount, String table_graph){
        ContentValues cv = new ContentValues();
        cv.put("yAxis", distinctcount);
        cv.put("xAxis", xindex);

        database.update(table_graph, cv, "Label=?", new String[]{distinctnames});

        return true;
    }

    public boolean minsertCount(int mxindex, String mdistinctnames, int mdistinctcount, String table_graph){
        ContentValues cv = new ContentValues();
        cv.put("yAxis", mdistinctcount);
        cv.put("xAxis", mxindex);

        database.update(table_graph, cv, "Label=?", new String[]{mdistinctnames});

        return true;
    }

    public ArrayList<BarEntry> getBarEntries(String table_graph) {
        ArrayList<BarEntry> rv = new ArrayList<>();
        Cursor csr = database.query(table_graph,new String[]{"xAxis","yAxis","Label"},null,null,null,null,null);
        while(csr.moveToNext()) {
            rv.add(new BarEntry(csr.getInt(0), csr.getInt(1), csr.getString(2)));
        }
        csr.close();
        return rv;
    }


    public List<Integer> getEntriesY(String table_graph){
        List<Integer> list = new ArrayList<>();
        Cursor cursor = database.query(table_graph, new String[]{"xAxis", "yAxis", "Label"}, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            list.add(cursor.getInt(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<BarEntry> getBarEntriesM(String table_graph) {
        ArrayList<BarEntry> rv = new ArrayList<>();
        Cursor csr = database.query(table_graph,new String[]{"xAxis","yAxis","Label"},null,null,null,null,null);
        while(csr.moveToNext()) {
            rv.add(new BarEntry(csr.getInt(0), csr.getInt(1), csr.getString(2)));
        }
        csr.close();
        return rv;
    }

    public List<Integer> getEntriesYM(String table_graph){
        List<Integer> list = new ArrayList<>();
        Cursor cursor = database.query(table_graph, new String[]{"xAxis", "yAxis", "Label"}, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            list.add(cursor.getInt(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<Entry> getLineEntries(String table_graph, int x) {
        ArrayList<Entry> rv = new ArrayList<>();
        Cursor csr = database.query(table_graph,null,null,null,null,null,null);
        while(csr.moveToNext()) {
            rv.add(new Entry(csr.getInt(0),csr.getInt(x)));
        }
        csr.close();
        return rv;
    }

    public boolean insertLineCount(int LineCount, String LineColumn, String Batch_Name, String table_graph){
        ContentValues cv = new ContentValues();
        cv.put(LineColumn, LineCount);

        database.update(table_graph, cv, "Batch_Label=?", new String[]{Batch_Name});

        return true;
    }

    public boolean delete(){
        database.delete(upload_table,null,null);
        database.close();
        return true;
    }

    public boolean deleteRestore(String VoterN){
        database.delete(upload_table,"VotersName=?",new String[]{VoterN});
        database.close();
        return true;
    }

    public boolean resetAllData(){

        ContentValues cv_voters = new ContentValues();
        cv_voters.put("Governor","");
        cv_voters.put("ViceGovernor","");
        cv_voters.put("Congressman","");
        cv_voters.put("Mayor","");
        cv_voters.put("ALCALA","");
        cv_voters.put("ALEJANDRINO","");
        cv_voters.put("CASULLA","");
        cv_voters.put("GONZALES","");
        cv_voters.put("LIWANAG","");
        cv_voters.put("SIO","");
        cv_voters.put("TALAGA","");
        cv_voters.put("TANADA","");
        cv_voters.put("UploadedAt","");
        cv_voters.put("Indicator","off");
        cv_voters.put("Deceased","");
        cv_voters.put("PartyList","");
        cv_voters.put("Encoder","");
        cv_voters.put("Contact","");
        database.update(voters_table,cv_voters,"Governor != '' AND ViceGovernor != '' AND Congressman != '' AND Mayor != ''",null);

        database.close();
        return true;
    }

    public Integer getcountnamePartylist(String namex, String columnName, String columnCity) {
        int count = 0;
//      Cursor mCount= database.rawQuery("select count (PartyList) from voters_table where PartyList is ", null);
//      Cursor mCount= database.rawQuery("select count(" + columnName + ") from voters_table where "+ columnName + "='" + namex + "' and  ", null);
        Cursor mCount= database.rawQuery("select count(" + columnName + ") from voters_table where City_Municipality = '"+columnCity+"' and "+ columnName + "='" + namex + "' ", null);
        mCount.moveToFirst();
        count=mCount.getInt(0);
        mCount.close();
        return count;
    }

    public Integer getcountnamePartylist2(String namex, String columnName, String columnCity, String brgy) {
        int count = 0;
        String[] selectionArg = {columnCity,brgy, namex};
        Cursor mCount= database.rawQuery("select count(" + columnName + ") from voters_table where City_Municipality =? and Barangay = ? and " + columnName + "= ? ", selectionArg);
        mCount.moveToFirst();
        count=mCount.getInt(0);
        mCount.close();
        return count;

    }
    public Integer getcountnamePartylist3(String namex, String columnName, String columnCity, String precincto) {
        int count = 0;
        String[] selectionArg = {columnCity,precincto, namex};
        Cursor mCount= database.rawQuery("select count(" + columnName + ") from voters_table where City_Municipality =? and Precinct = ? and " + columnName + "= ? ", selectionArg);
        mCount.moveToFirst();
        count=mCount.getInt(0);
        mCount.close();
        return count;

    }

    public boolean insertPartyList(int xAxis, int count, String PartyListName, String table_graph){
        ContentValues contentValues = new ContentValues();
        contentValues.put("xAxis",xAxis);
        contentValues.put("yAxis",count);
        contentValues.put("Label",PartyListName);

        long result = database.insert(table_graph, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public List<String> getDistinctName(){
        List<String> list = new ArrayList<>();
//        Cursor cursor = database.query(true, voters_table, new String[]{"PartyList"} , null, null, null, null, null, null );
        Cursor cursor= database.rawQuery("select distinct PartyList from voters_table where PartyList != ''", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getDistinctName2(String namex, String columnName, String columnCity, String brgy){
        List<String> list = new ArrayList<>();
        String[] selectionArg = {"",columnCity,brgy, namex};
        Cursor cursor= database.rawQuery("select distinct PartyList from voters_table where PartyList != ? and City_Municipality =? and Barangay = ? and " + columnName + "= ? ", selectionArg);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void CreateDynamicTables(String table_graph)
    {
        database.execSQL("DROP TABLE IF EXISTS " + table_graph);
        String query = "CREATE TABLE " + table_graph + " (xAxis INTEGER, yAxis INTEGER, Label TEXT)";
        database.execSQL(query);
        database.close();
    }

    public ArrayList<ListModel> SortPartyList(String table_graph){
        ArrayList<ListModel> voters = new ArrayList<>();
        Cursor csr = database.query(table_graph,new String[]{"yAxis", "Label"},null,null,null,null,"yAxis DESC", "8");
        while(csr.moveToNext()) {
            voters.add(new ListModel(csr.getInt(0), csr.getString(1)));
        }
        csr.close();
        return voters;
    }

    public void CreateDynamicTablesStatic(String table_graph, int count, String Display_Name)
    {
        database.execSQL("DROP TABLE IF EXISTS " + table_graph);
        String query = "CREATE TABLE " + table_graph + " (xAxis INTEGER, yAxis INTEGER, Label TEXT)";
        database.execSQL(query);
//        database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("xAxis", 0);
        cv.put("yAxis", count);
        cv.put("Label", Display_Name);
        database.insert(table_graph, null, cv);
        database.close();
    }
    public ArrayList<ListModel> SortPartyListRaw(){
        ArrayList<ListModel> voters = new ArrayList<>();
        Cursor csr = database.rawQuery("SELECT yAxis, Label FROM PartyList_graph EXCEPT SELECT yAxis, Label FROM PartyList_graph WHERE Label IN ('NO ENTRY','ALONA') ORDER BY yAxis DESC LIMIT 8", null);
        while(csr.moveToNext()) {
            voters.add(new ListModel(csr.getInt(0), csr.getString(1)));
        }
        csr.close();
        return voters;
    }

    public ArrayList<ListModel> PartyList(String table_graph){
        ArrayList<ListModel> voters = new ArrayList<>();
        Cursor csr = database.query(table_graph,new String[]{"yAxis", "Label"},null,null,null,null,null, null);
        while(csr.moveToNext()) {
            voters.add(new ListModel(csr.getInt(0), csr.getString(1)));
        }
        csr.close();
        return voters;
    }

}
