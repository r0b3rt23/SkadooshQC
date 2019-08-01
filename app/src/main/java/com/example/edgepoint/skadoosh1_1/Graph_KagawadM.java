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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Graph_KagawadM extends AppCompatActivity {

    BarChart barChart;
    private LinearLayout graph_layout;
    int legendcount = 0;
    String[] legendName ={};
    String[] bokallegendName = {"Alcala, Eming", "Alejandrino, Boyet","Casulla, Roger","Gonzales, Teddy"
            ,"Liwanag, Yna","Sio, Beth","Talaga, Mano","Tanada, Michael", "No Entry"};

    int[] COLOURS = { Color.parseColor("#e6194B"), Color.parseColor("#f58231"), Color.parseColor("#ffe119"),Color.parseColor("#4363d8"),
            Color.parseColor("#3cb44b"), Color.parseColor("#911eb4"), Color.parseColor("#4cb728"),Color.parseColor("#f032e6"),
            Color.parseColor("#000075"),Color.parseColor("#fabebe"),Color.parseColor("#9A6324"),Color.parseColor("#469990"),
            Color.parseColor("#bfef45"),Color.parseColor("#800000"),Color.parseColor("#aaffc3"),Color.parseColor("#808000")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph__kagawad_m);

        backGraphViewButton();
        saveImageButton();
        Intent intent = getIntent();
        String label = intent.getStringExtra("Label");
        String tableGraph = intent.getStringExtra("Table");
        String connection = intent.getStringExtra("Connection");

        DatabaseAccess databaseAccessDate = DatabaseAccess.getInstance(this,"voters.db");
        databaseAccessDate.open();
        long DateMilliSeconds = databaseAccessDate.getUploadDate();
        databaseAccessDate.close();

        DateFormat dateformat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(DateMilliSeconds);
        String Date = dateformat.format(calendar.getTime());

        TextView date_tv = (TextView) findViewById(R.id.Mkdate_id);

        TextView connection_tv = (TextView) findViewById(R.id.Mkconnection_id);
        if (connection.equals("online")){
            date_tv.setText("Data in Main Server");
            connection_tv.setTextColor(Color.parseColor("#00ff00"));
        }else {
            if (DateMilliSeconds == 0){
                date_tv.setText("Local Data");
            }
            else {
                date_tv.setText("As of "+Date);
            }
            connection_tv.setTextColor(Color.parseColor("#ff0000"));
        }
        connection_tv.setText("("+connection+")");

        legendName = bokallegendName;

        TextView label_tv = (TextView) findViewById(R.id.graphlabel_tv_id);
        label_tv.setText(label);

        barChart = (BarChart) findViewById(R.id.hbargraphM);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this,"voters.db");
        databaseAccess.open();
        ArrayList<BarEntry> barEntries = databaseAccess.getBarEntriesM(tableGraph);

        BarDataSet barDataSet = new BarDataSet(barEntries, "Count");
        databaseAccess.close();

        barDataSet.setColors(COLOURS);
        barDataSet.setValueFormatter(new MyValueFormatter());

        BarData theData = new BarData(barDataSet);
        theData.setValueTextSize(12);
        theData.setBarWidth(0.9f);

        barChart.setData(theData);
        barChart.setEnabled(true);
        barChart.setDescription(null);
        barChart.setDrawValueAboveBar(false);

        barChart.getXAxis().setDrawGridLines(false);

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleYEnabled(true);
        barChart.setScaleXEnabled(false);
        barChart.animateY(2000);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.setExtraOffsets(0, 5, 0, 20);
//        barChart.setVisibleXRange(9f,9f);
        barChart.invalidate();

        Legend legend = barChart.getLegend();
        legend.setEnabled(true);
        legend.setWordWrapEnabled(true);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTextSize(12);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(12);
        legend.setXEntrySpace(2);

        LegendEntry[] legendEntries = new LegendEntry[legendName.length];
        for (int i=0; i<legendEntries.length; i++)
        {
            LegendEntry entry = new LegendEntry();
            entry.label =  String.valueOf(legendName[i]);
            entry.formColor = (COLOURS[i]);
            legendEntries[i] = entry;
        }

        legend.setCustom(legendEntries);

        DatabaseAccess datacountM = DatabaseAccess.getInstance(Graph_KagawadM.this,"voters.db"); //count the names
        datacountM.open();
        List<Integer> quaters2 = datacountM.getEntriesY(tableGraph);
        datacountM.close();
        final Integer[] temp = quaters2.toArray(new Integer[quaters2.size()]);

        final List<String> quarters1 = new ArrayList<>();

        for (int i=0; i<temp.length;i++)
        {
            int comp = temp[i];
            int div =0;
            for (int x=0; x<legendName.length; x++){
                div = div + temp[x];
            }
            float cmptmp = ((float) comp / (float)div)  * 100;
            DecimalFormat df = new DecimalFormat("###,###,###.#");
            String dft = df.format(cmptmp);
            if (dft.equals("NaN")){ quarters1.add("0%"); }
            else quarters1.add(dft+"%");

        }


        final String[] quarters = quarters1.toArray(new String[quarters1.size()]);


        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        xAxis.setTextSize(12);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setLabelCount(9);

        YAxis yAxis = barChart.getAxisRight();
        yAxis.setEnabled(false);
        YAxis yAxis1 = barChart.getAxisLeft();
        yAxis1.setAxisMinimum(0f);

        barChart.invalidate();
    }
    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,###.#");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
//            float mvalue = (value/105364)*100;
//            String xformat = mvalue1 + " % " + mvalue2;
//            return mFormat.format(value) + " (" + mFormat.format(mvalue) + "%" +")";
            return mFormat.format(value);
        }
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
        graph_layout = (LinearLayout) findViewById(R.id.mkgraphview_layout);
        Button saveImagebutton = (Button) findViewById(R.id.mkssbuton);
        saveImagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder setbuilder = new AlertDialog.Builder(Graph_KagawadM.this);
                setbuilder.setTitle("Enter File Name:");
                final EditText filenameEdit = new EditText(Graph_KagawadM.this);
                setbuilder.setView(filenameEdit);

                setbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String filename = filenameEdit.getText().toString();

                        if (!filename.isEmpty()){
                            saveImage(graph_layout,filename);
                        }
                        else {
                            Toast.makeText(Graph_KagawadM.this, "Enter a file name", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(Graph_KagawadM.this, "Image saved", Toast.LENGTH_SHORT).show();

        } catch (Throwable e) {
            Toast.makeText(Graph_KagawadM.this, "Image not save", Toast.LENGTH_SHORT).show();
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }


}
