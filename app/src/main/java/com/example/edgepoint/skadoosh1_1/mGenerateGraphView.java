package com.example.edgepoint.skadoosh1_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
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
import com.github.mikephil.charting.utils.ColorTemplate;
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

public class mGenerateGraphView extends AppCompatActivity {
    private ArrayList<ListModel> PartyList;
    BarChart barChart;
    String[] legendName = {};
    int[] COLOURS ={};
    private LinearLayout graph_layout;
    String[] governorlegendName = {"Abuy, Benilda", "Alcala, Kulit", "Dator, Serafin" ,
            "Pulgar, Sony","Suarez, Danny","Villena, Ding","Undecided","No Entry"};

    String[] vicegovernorlegendName = {"Capina, Teodorico", "Estacio, Geoffrey", "Malite, Arcie" ,"Nantes, Sam","Undecided","No Entry"};

    String[] congressmanlegendName = {"Alcala, Procy", "Masilang, Boyet", "Seneres, Christain" ,"Suarez, Amadeo",
            "Suarez, David","Undecided","No Entry"};

    String[] lucena_mayorlegendName = {"Alcala, Dondon", "Talaga, Mon","Undecided","No Entry"};
    String[] sariaya_mayorlegendName = {"De La Roca, Jun", "Gayeta, Marceng","Undecided","No Entry"};
    String[] candelaria_mayorlegendName = {"Boongaling, Macky", "Maliwanag, Bong","Undecided","No Entry"};
    String[] tiaong_mayorlegendName = {"Castillo, Romano", "Preza, Ramon","Undecided","No Entry"};
    String[] dolores_mayorlegendName = {"Calayag, Orlan", "Milan, Jr","Undecided","No Entry"};
    String[] sanantonio_mayorlegendName = {"Hernandez, Rodante", "Wagan, Erick","Undecided","No Entry"};

    String[] bokallegendName = {"Alcala, Eming", "Alejandrino, Boyet","Casulla, Roger","Gonzales, Teddy"
            ,"Liwanag, Yna","Sio, Beth","Talaga, Mano","Tanada, Michael","No Entry"};

    int[] COLOURS_GOV_CONG = { Color.parseColor("#e6194B"), Color.parseColor("#f58231"), Color.parseColor("#ffe119"),Color.parseColor("#4363d8"),
            Color.parseColor("#3cb44b"), Color.parseColor("#911eb4"), Color.parseColor("#42d4f4"),Color.parseColor("#f032e6"),
            Color.parseColor("#000075"),Color.parseColor("#fabebe"),Color.parseColor("#9A6324"),Color.parseColor("#469990"),
            Color.parseColor("#bfef45"),Color.parseColor("#800000"),Color.parseColor("#aaffc3"),Color.parseColor("#808000")};

    int[] COLOURS_VGOV = { Color.parseColor("#e6194B"), Color.parseColor("#f58231"), Color.parseColor("#ffe119"),Color.parseColor("#3cb44b"),
            Color.parseColor("#4363d8"), Color.parseColor("#911eb4"), Color.parseColor("#42d4f4"),Color.parseColor("#f032e6"),
            Color.parseColor("#000075"),Color.parseColor("#fabebe"),Color.parseColor("#9A6324"),Color.parseColor("#469990"),
            Color.parseColor("#bfef45"),Color.parseColor("#800000"),Color.parseColor("#aaffc3"),Color.parseColor("#808000")};

    int[] COLOURS_MAYOR = { Color.parseColor("#ffe119"),Color.parseColor("#3cb44b"),Color.parseColor("#4363d8"), Color.parseColor("#911eb4")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_generate_graph_view);

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

        TextView date_tv = (TextView) findViewById(R.id.Mdate_id);

        TextView connection_tv = (TextView) findViewById(R.id.Mconnection_id);
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

        if (label.equals("Governor")){
            legendName = governorlegendName;
            COLOURS = COLOURS_GOV_CONG;
        }
        else if (label.equals("ViceGovernor")){
            legendName = vicegovernorlegendName;
            COLOURS = COLOURS_VGOV;
        }
        else if (label.equals("Congressman")){
            legendName = congressmanlegendName;
            COLOURS = COLOURS_GOV_CONG;
        }
        else if (label.equals("Mayor")){
            String city_municipality = intent.getStringExtra("City");
            COLOURS = COLOURS_MAYOR;
            if (city_municipality.equals("Candelaria")){
                legendName = candelaria_mayorlegendName;
            }
            else if (city_municipality.equals("Dolores")){
                legendName = dolores_mayorlegendName;
            }
            else if (city_municipality.equals("Lucena City")){
                legendName = lucena_mayorlegendName;
            }
            else if (city_municipality.equals("San Antonio")){
                legendName = sanantonio_mayorlegendName;
            }
            else if (city_municipality.equals("Sariaya")){
                legendName = sariaya_mayorlegendName;
            }
            else if (city_municipality.equals("Tiaong")){
                legendName = tiaong_mayorlegendName;
            }
        }
        else if (label.equals("BoardMember")){
            legendName = bokallegendName;
        }
        else if (label.equals("PartyList")){

            DatabaseAccess insertAscending = DatabaseAccess.getInstance(mGenerateGraphView.this,"voters.db"); //insert data
            insertAscending.open();
            PartyList = insertAscending.PartyList(tableGraph);
            ListModel listModel;
            ArrayList<String> arraylist=new ArrayList<>();
            for(int i=0; i<PartyList.size(); i++){
                listModel = PartyList.get(i);
                String names = listModel.getName();
                arraylist.add(names);
            }
            String[] partyList = arraylist.toArray(new String[arraylist.size()]);
            legendName = partyList;
            insertAscending.close();
            COLOURS = COLOURS_GOV_CONG;
        }

        TextView label_tv = (TextView) findViewById(R.id.Mgraphlabel_tv_id);
        label_tv.setText(label);

        barChart = (BarChart) findViewById(R.id.mbarchart);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this,"voters.db");
        databaseAccess.open();
        ArrayList<BarEntry> barEntries = databaseAccess.getBarEntriesM(tableGraph);

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        databaseAccess.close();

        barDataSet.setColors(COLOURS);

        barDataSet.setValueFormatter(new MyValueFormatter());

        Legend legend = barChart.getLegend();
        legend.setEnabled(true);
        legend.setWordWrapEnabled(false);
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

        DatabaseAccess datacountM = DatabaseAccess.getInstance(mGenerateGraphView.this,"voters.db"); //count the names
        datacountM.open();
        List<Integer> quaters2 = datacountM.getEntriesYM(tableGraph);
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
            float cmptmp = ((float) comp / (float) div)  * 100;
            DecimalFormat df = new DecimalFormat("###,###,###.#");
            String dft = df.format(cmptmp);

            String comptemp = Float.toString(cmptmp);
            //Toast.makeText(mGenerateGraphView.this,comptemp,Toast.LENGTH_LONG).show();
            quarters1.add(dft+"%");
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
        xAxis.setLabelCount(legendName.length);

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
            //float mvalue = (value/105364)*100;
            //String xformat = mvalue1 + " % " + mvalue2;
            //return mFormat.format(value) + " (" + mFormat.format(mvalue) + "%" +")";
            return mFormat.format(value);
        }
    }

    public void backGraphViewButton(){
        Button prev2button = (Button) findViewById(R.id.Mbackgraphviewbtn);
        prev2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void saveImageButton(){
        graph_layout = (LinearLayout) findViewById(R.id.mgraphview_layout);
        Button saveImagebutton = (Button) findViewById(R.id.Mssbuton);
        saveImagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder setbuilder = new AlertDialog.Builder(mGenerateGraphView.this);
                setbuilder.setTitle("Enter File Name:");
                final EditText filenameEdit = new EditText(mGenerateGraphView.this);
                setbuilder.setView(filenameEdit);

                setbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String filename = filenameEdit.getText().toString();

                        if (!filename.isEmpty()){
                            saveImage(graph_layout,filename);
                        }
                        else {
                            Toast.makeText(mGenerateGraphView.this, "Enter a file name", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(mGenerateGraphView.this, "Image saved", Toast.LENGTH_SHORT).show();

        } catch (Throwable e) {
            Toast.makeText(mGenerateGraphView.this, "Image not save", Toast.LENGTH_SHORT).show();
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
}
