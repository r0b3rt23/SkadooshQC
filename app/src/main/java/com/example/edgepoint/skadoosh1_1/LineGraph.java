package com.example.edgepoint.skadoosh1_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class LineGraph extends AppCompatActivity {

    private static final String TAG = "LineGraph";
    private LinearLayout graph_layout;
    private LineChart lchart;
    String[] mayorlegendName = {};
    String[] lucena_mayorlegendName = {"Alcala, Dondon", "Talaga, Mon"};
    String[] sariaya_mayorlegendName = {"De La Roca, Jun", "Gayeta, Marceng"};
    String[] candelaria_mayorlegendName = {"Boongaling, Macky", "Maliwanag, Bong"};
    String[] tiaong_mayorlegendName = {"Castillo, Romano", "Preza, Ramon"};
    String[] dolores_mayorlegendName = {"Calayag, Orlan", "Milan, Jr"};
    String[] sanantonio_mayorlegendName = {"Hernandez, Rodante", "Wagan, Erick"};

    String[] governorlegendName = {"Abuy, Benilda", "Alcala, Kulit", "Dator, Serafin","Pulgar, Sony","Suarez, Danny","Villena, Ding"};
    String[] vicegovernorlegendName = {"Capina, Teodorico", "Estacio, Geoffrey", "Malite, Arcie" ,"Nantes, Sam"};
    String[] congressmanlegendName = {"Alcala, Procy", "Masilang, Boyet", "Seneres, Christain" ,"Suarez, Amadeo","Suarez, David"};

    String LineChartName;

    int[] COLOURS_GOV_CONG = { Color.parseColor("#e6194B"), Color.parseColor("#f58231"), Color.parseColor("#ffe119"),Color.parseColor("#4363d8"),
            Color.parseColor("#3cb44b"), Color.parseColor("#911eb4"), Color.parseColor("#42d4f4"),Color.parseColor("#f032e6"),
            Color.parseColor("#000075"),Color.parseColor("#fabebe"),Color.parseColor("#9A6324"),Color.parseColor("#469990"),
            Color.parseColor("#bfef45"),Color.parseColor("#800000"),Color.parseColor("#aaffc3"),Color.parseColor("#808000")};

    int[] COLOURS_VGOV = { Color.parseColor("#e6194B"), Color.parseColor("#f58231"), Color.parseColor("#ffe119"),Color.parseColor("#3cb44b"),
            Color.parseColor("#4363d8"), Color.parseColor("#911eb4"), Color.parseColor("#42d4f4"),Color.parseColor("#f032e6"),
            Color.parseColor("#000075"),Color.parseColor("#fabebe"),Color.parseColor("#9A6324"),Color.parseColor("#469990"),
            Color.parseColor("#bfef45"),Color.parseColor("#800000"),Color.parseColor("#aaffc3"),Color.parseColor("#808000")};

    int[] COLOURS_MAYOR = { Color.parseColor("#ffe119"),Color.parseColor("#3cb44b"),Color.parseColor("#4363d8"), Color.parseColor("#911eb4")};

    int[] COLOURS_BOKAL = { Color.parseColor("#e6194B"), Color.parseColor("#f58231"), Color.parseColor("#ffe119"),Color.parseColor("#4363d8"),
            Color.parseColor("#3cb44b"), Color.parseColor("#911eb4"), Color.parseColor("#4cb728"),Color.parseColor("#f032e6"),
            Color.parseColor("#000075"),Color.parseColor("#fabebe"),Color.parseColor("#9A6324"),Color.parseColor("#469990"),
            Color.parseColor("#bfef45"),Color.parseColor("#800000"),Color.parseColor("#aaffc3"),Color.parseColor("#808000")};

    int[] COLOURS = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        backGraphViewButton();
        saveImageButton();
        Intent intent = getIntent();
        String label = intent.getStringExtra("Label");
        String tableGraph = intent.getStringExtra("Table");
        String city = intent.getStringExtra("City");

        TextView label_tv = (TextView) findViewById(R.id.graphlabel_tv_id);
        label_tv.setText(label);

        lchart = (LineChart) findViewById(R.id.linechart);

        if (label.equals("Governor")){
            COLOURS = COLOURS_GOV_CONG;
            Governor(tableGraph);
        }
        else if (label.equals("ViceGovernor")){
            COLOURS = COLOURS_VGOV;
            ViceGovernor(tableGraph);
        }
        else if (label.equals("Congressman")){
            COLOURS = COLOURS_GOV_CONG;
            Congressman(tableGraph);
        }
        else if (label.equals("Mayor")){
            COLOURS = COLOURS_MAYOR;
            Mayor(tableGraph,city);
        }
        else if (label.equals("BoardMember")) {
            COLOURS = COLOURS_BOKAL;
            BoardMember(tableGraph);
        }

        lchart.setTouchEnabled(true);
        lchart.setDragEnabled(true);
        lchart.setScaleEnabled(true);
        lchart.animateX(2000);
        lchart.getAxisLeft().setDrawGridLines(false);
        lchart.getAxisRight().setDrawGridLines(false);

        lchart.setEnabled(true);
        lchart.setDescription(null);
        //  lchart.setVisibleXRange(0f,6f);

        String[] values = new String[] { "Batch 1", "Batch 2", "Batch 3", "Batch 4", "Batch 5", "Batch 6", "Batch 7", "Batch 8","Batch 9", "Batch 10", "Batch 11", "Batch 12"};

        XAxis xAxis = lchart.getXAxis();
        xAxis.setAxisMinimum(0f);
        xAxis.setValueFormatter(new MyXaxisValuesFormatter(values));
        xAxis.setGranularity(1f);
        xAxis.setTextSize(11f);

        lchart.invalidate();
    }

    public class MyXaxisValuesFormatter implements IAxisValueFormatter{

        public String[] mValues;
        public MyXaxisValuesFormatter(String[] values){
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axisBase){
            return mValues[(int)value];
        }


    }

    public void Governor(String table_Graph){

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this,"voters.db");
        databaseAccess.open();
        ArrayList<Entry>  LineEntries1 = databaseAccess.getLineEntries(table_Graph, 1);
        ArrayList<Entry>  LineEntries2 = databaseAccess.getLineEntries(table_Graph, 2);
        ArrayList<Entry>  LineEntries3 = databaseAccess.getLineEntries(table_Graph, 3);
        ArrayList<Entry>  LineEntries4 = databaseAccess.getLineEntries(table_Graph, 4);
        ArrayList<Entry>  LineEntries5 = databaseAccess.getLineEntries(table_Graph, 5);
        ArrayList<Entry>  LineEntries6 = databaseAccess.getLineEntries(table_Graph, 6);
        databaseAccess.close();

        LineDataSet set1, set2,set3,set4, set5,set6;

        set1 = new LineDataSet(LineEntries1, governorlegendName[0]);
        set1.setFillAlpha(110);

        set2 = new LineDataSet(LineEntries2, governorlegendName[1]);
        set2.setFillAlpha(110);

        set3 = new LineDataSet(LineEntries3, governorlegendName[2]);
        set3.setFillAlpha(110);

        set4 = new LineDataSet(LineEntries4, governorlegendName[3]);
        set4.setFillAlpha(110);

        set5 = new LineDataSet(LineEntries5, governorlegendName[4]);
        set5.setFillAlpha(110);

        set6 = new LineDataSet(LineEntries6, governorlegendName[5]);
        set6.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);
        dataSets.add(set5);
        dataSets.add(set6);

        LineData data = new LineData(dataSets);

        set1.setColor(COLOURS[0]);
        set1.setCircleColor(COLOURS[0]);
        set1.setCircleHoleColor(COLOURS[0]);

        set2.setColor(COLOURS[1]);
        set2.setCircleColor(COLOURS[1]);
        set2.setCircleHoleColor(COLOURS[1]);

        set3.setColor(COLOURS[2]);
        set3.setCircleColor(COLOURS[2]);
        set3.setCircleHoleColor(COLOURS[2]);

        set4.setColor(COLOURS[3]);
        set4.setCircleColor(COLOURS[3]);
        set4.setCircleHoleColor(COLOURS[3]);

        set5.setColor(COLOURS[4]);
        set5.setCircleColor(COLOURS[4]);
        set5.setCircleHoleColor(COLOURS[4]);

        set6.setColor(COLOURS[5]);
        set6.setCircleColor(COLOURS[5]);
        set6.setCircleHoleColor(COLOURS[5]);

        lchart.setData(data);

    }

    public void ViceGovernor(String table_Graph){

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this,"voters.db");
        databaseAccess.open();
        ArrayList<Entry>  LineEntries1 = databaseAccess.getLineEntries(table_Graph, 7);
        ArrayList<Entry>  LineEntries2 = databaseAccess.getLineEntries(table_Graph, 8);
        ArrayList<Entry>  LineEntries3 = databaseAccess.getLineEntries(table_Graph, 9);
        ArrayList<Entry>  LineEntries4 = databaseAccess.getLineEntries(table_Graph, 10);
        databaseAccess.close();

        LineDataSet set1, set2,set3,set4;

        set1 = new LineDataSet(LineEntries1, vicegovernorlegendName[0]);
        set1.setFillAlpha(110);

        set2 = new LineDataSet(LineEntries2, vicegovernorlegendName[1]);
        set2.setFillAlpha(110);

        set3 = new LineDataSet(LineEntries3, vicegovernorlegendName[2]);
        set3.setFillAlpha(110);

        set4 = new LineDataSet(LineEntries4, vicegovernorlegendName[3]);
        set4.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);

        LineData data = new LineData(dataSets);

        set1.setColor(COLOURS[0]);
        set1.setCircleColor(COLOURS[0]);
        set1.setCircleHoleColor(COLOURS[0]);

        set2.setColor(COLOURS[1]);
        set2.setCircleColor(COLOURS[1]);
        set2.setCircleHoleColor(COLOURS[1]);

        set3.setColor(COLOURS[2]);
        set3.setCircleColor(COLOURS[2]);
        set3.setCircleHoleColor(COLOURS[2]);

        set4.setColor(COLOURS[3]);
        set4.setCircleColor(COLOURS[3]);
        set4.setCircleHoleColor(COLOURS[3]);

        lchart.setData(data);

    }

    public void Congressman(String table_Graph){

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this,"voters.db");
        databaseAccess.open();
        ArrayList<Entry>  LineEntries1 = databaseAccess.getLineEntries(table_Graph, 11);
        ArrayList<Entry>  LineEntries2 = databaseAccess.getLineEntries(table_Graph, 12);
        ArrayList<Entry>  LineEntries3 = databaseAccess.getLineEntries(table_Graph, 13);
        ArrayList<Entry>  LineEntries4 = databaseAccess.getLineEntries(table_Graph, 14);
        ArrayList<Entry>  LineEntries5 = databaseAccess.getLineEntries(table_Graph, 15);
        databaseAccess.close();

        LineDataSet set1, set2,set3,set4, set5;

        set1 = new LineDataSet(LineEntries1, congressmanlegendName[0]);
        set1.setFillAlpha(110);

        set2 = new LineDataSet(LineEntries2, congressmanlegendName[1]);
        set2.setFillAlpha(110);

        set3 = new LineDataSet(LineEntries3, congressmanlegendName[2]);
        set3.setFillAlpha(110);

        set4 = new LineDataSet(LineEntries4, congressmanlegendName[3]);
        set4.setFillAlpha(110);

        set5 = new LineDataSet(LineEntries5, congressmanlegendName[4]);
        set5.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);
        dataSets.add(set5);

        LineData data = new LineData(dataSets);

        set1.setColor(COLOURS[0]);
        set1.setCircleColor(COLOURS[0]);
        set1.setCircleHoleColor(COLOURS[0]);

        set2.setColor(COLOURS[1]);
        set2.setCircleColor(COLOURS[1]);
        set2.setCircleHoleColor(COLOURS[1]);

        set3.setColor(COLOURS[2]);
        set3.setCircleColor(COLOURS[2]);
        set3.setCircleHoleColor(COLOURS[2]);

        set4.setColor(COLOURS[3]);
        set4.setCircleColor(COLOURS[3]);
        set4.setCircleHoleColor(COLOURS[3]);

        set5.setColor(COLOURS[4]);
        set5.setCircleColor(COLOURS[4]);
        set5.setCircleHoleColor(COLOURS[4]);

        lchart.setData(data);

    }

    public void Mayor(String table_Graph, String city_mun){

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this,"voters.db");
        databaseAccess.open();
        ArrayList<Entry>  LineEntries1 = new ArrayList<Entry>();
        ArrayList<Entry>  LineEntries2 = new ArrayList<Entry>();
        if (city_mun.equals("Lucena City")){
            LineEntries1 = databaseAccess.getLineEntries(table_Graph, 16);
            LineEntries2 = databaseAccess.getLineEntries(table_Graph, 17);
            mayorlegendName = lucena_mayorlegendName;
        }
        else if (city_mun.equals("Sariaya")){
            LineEntries1 = databaseAccess.getLineEntries(table_Graph, 18);
            LineEntries2 = databaseAccess.getLineEntries(table_Graph, 19);
            mayorlegendName = sariaya_mayorlegendName;
        }
        else if (city_mun.equals("Candelaria")){
            LineEntries1 = databaseAccess.getLineEntries(table_Graph, 20);
            LineEntries2 = databaseAccess.getLineEntries(table_Graph, 21);
            mayorlegendName = candelaria_mayorlegendName;
        }
        else if (city_mun.equals("Tiaong")){
            LineEntries1 = databaseAccess.getLineEntries(table_Graph, 22);
            LineEntries2 = databaseAccess.getLineEntries(table_Graph, 23);
            mayorlegendName = tiaong_mayorlegendName;
        }
        else if (city_mun.equals("Dolores")){
            LineEntries1 = databaseAccess.getLineEntries(table_Graph, 24);
            LineEntries2 = databaseAccess.getLineEntries(table_Graph, 25);
            mayorlegendName = dolores_mayorlegendName;
        }
        else if (city_mun.equals("San Antonio")){
            LineEntries1 = databaseAccess.getLineEntries(table_Graph, 26);
            LineEntries2 = databaseAccess.getLineEntries(table_Graph, 27);
            mayorlegendName = sanantonio_mayorlegendName;
        }
        databaseAccess.close();

        LineDataSet set1, set2;

        set1 = new LineDataSet(LineEntries1, mayorlegendName[0]);
        set1.setFillAlpha(110);

        set2 = new LineDataSet(LineEntries2, mayorlegendName[1]);
        set2.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);

        LineData data = new LineData(dataSets);

        set1.setColor(Color.YELLOW);
        set1.setCircleColor(Color.YELLOW);
        set1.setCircleHoleColor(Color.YELLOW);

        set2.setColor(Color.RED);
        set2.setCircleColor(Color.RED);
        set2.setCircleHoleColor(Color.RED);

        lchart.setData(data);

    }

    public void BoardMember(String table_Graph){

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this,"voters.db");
        databaseAccess.open();
        ArrayList<Entry>  LineEntries1 = databaseAccess.getLineEntries(table_Graph, 28);
        ArrayList<Entry>  LineEntries2 = databaseAccess.getLineEntries(table_Graph, 29);
        ArrayList<Entry>  LineEntries3 = databaseAccess.getLineEntries(table_Graph, 30);
        ArrayList<Entry>  LineEntries4 = databaseAccess.getLineEntries(table_Graph, 31);
        ArrayList<Entry>  LineEntries5 = databaseAccess.getLineEntries(table_Graph, 32);
        ArrayList<Entry>  LineEntries6 = databaseAccess.getLineEntries(table_Graph, 33);
        ArrayList<Entry>  LineEntries7 = databaseAccess.getLineEntries(table_Graph, 34);
        ArrayList<Entry>  LineEntries8 = databaseAccess.getLineEntries(table_Graph, 35);
        databaseAccess.close();

        LineDataSet set1, set2, set3, set4, set5, set6, set7, set8;

        String[] bokallegendName = {"Alcala,E", "Alejandrino,B","Casulla,R","Gonzales,T"
                ,"Liwanag,Y","Sio,B","Talaga,M","Tanada,M"};

        set1 = new LineDataSet(LineEntries1, bokallegendName[0]);
        set1.setFillAlpha(255);

        set2 = new LineDataSet(LineEntries2, bokallegendName[1]);
        set2.setFillAlpha(255);

        set3 = new LineDataSet(LineEntries3, bokallegendName[2]);
        set3.setFillAlpha(255);

        set4 = new LineDataSet(LineEntries4, bokallegendName[3]);
        set4.setFillAlpha(255);

        set5 = new LineDataSet(LineEntries5, bokallegendName[4]);
        set5.setFillAlpha(255);

        set6 = new LineDataSet(LineEntries6, bokallegendName[5]);
        set6.setFillAlpha(255);

        set7 = new LineDataSet(LineEntries7, bokallegendName[6]);
        set7.setFillAlpha(225);

        set8 = new LineDataSet(LineEntries8, bokallegendName[7]);
        set8.setFillAlpha(225);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            dataSets.add(set2);
            dataSets.add(set3);
            dataSets.add(set4);
            dataSets.add(set5);
            dataSets.add(set6);
            dataSets.add(set7);
            dataSets.add(set8);

            LineData data = new LineData(dataSets);
            //data.setValueTextSize(10);

        set1.setColor(COLOURS[0]);
        set1.setCircleColor(COLOURS[0]);
        set1.setCircleHoleColor(COLOURS[0]);

        set2.setColor(COLOURS[1]);
        set2.setCircleColor(COLOURS[1]);
        set2.setCircleHoleColor(COLOURS[1]);

        set3.setColor(COLOURS[2]);
        set3.setCircleColor(COLOURS[2]);
        set3.setCircleHoleColor(COLOURS[2]);

        set4.setColor(COLOURS[3]);
        set4.setCircleColor(COLOURS[3]);
        set4.setCircleHoleColor(COLOURS[3]);

        set5.setColor(COLOURS[4]);
        set5.setCircleColor(COLOURS[4]);
        set5.setCircleHoleColor(COLOURS[4]);

        set6.setColor(COLOURS[5]);
        set6.setCircleColor(COLOURS[5]);
        set6.setCircleHoleColor(COLOURS[5]);

        set7.setColor(COLOURS[6]);
        set7.setCircleColor(COLOURS[6]);
        set7.setCircleHoleColor(COLOURS[6]);

        set8.setColor(COLOURS[7]);
        set8.setCircleColor(COLOURS[7]);
        set8.setCircleHoleColor(COLOURS[7]);

        lchart.setVisibleXRangeMaximum(16);

        lchart.setData(data);

    }


    public void backGraphViewButton(){
        Button prev2button = (Button) findViewById(R.id.backgraphviewbtn);
        prev2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void saveImageButton(){
        graph_layout = (LinearLayout) findViewById(R.id.lineview_layout);
        Button saveImagebutton = (Button) findViewById(R.id.ssbutonline);
        saveImagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder setbuilder = new AlertDialog.Builder(LineGraph.this);
                setbuilder.setTitle("Enter File Name:");
                final EditText filenameEdit = new EditText(LineGraph.this);
                setbuilder.setView(filenameEdit);

                setbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String filename = filenameEdit.getText().toString();

                        if (!filename.isEmpty()){
                            saveImage(graph_layout,filename);
                        }
                        else {
                            Toast.makeText(LineGraph.this, "Enter a file name", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

                setbuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                        dialog.dismiss();
                    }
                });

                AlertDialog setDialog = setbuilder.create();
                setDialog.show();

            }
        });
    }
    private void saveImage(View v,String File_name){
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDate = formatter.format(new Date());

        try {
            // create bitmap screen capture
            v.setDrawingCacheEnabled(true);
            v.setDrawingCacheBackgroundColor(0xfffafafa);
            Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
            v.setDrawingCacheEnabled(false);

            File directory = Environment.getExternalStorageDirectory();
            File childDirectory = new File(directory, "Skadoosh Graphs");
            if (!childDirectory.exists()) {
                childDirectory.mkdirs();
            }
            String filename = File_name+" "+strDate + ".jpeg";
            // Create imageDir
            File mypath = new File(childDirectory, filename);

            FileOutputStream fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            Toast.makeText(LineGraph.this, "Image saved", Toast.LENGTH_SHORT).show();

        } catch (Throwable e) {
            Toast.makeText(LineGraph.this, "Image not save", Toast.LENGTH_SHORT).show();
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

}
