package com.example.edgepoint.skadoosh1_1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class QuestionForm_new extends AppCompatActivity {

    public Spinner gov_spinner, vgov_spinner, cong_spinner, bokal_spinner, mayor_spinner, education_spinner, income_spinner;
    public String gov_str,vgov_str,cong_str,bokal_str, mayor_str,stringBirthday;
    String city_form,votersname_form,barangay_form,precinct_form, congressman="", mayor="", governor="", vicegovernor="";
    public TextView mDisplayDate;
    public EditText Age;
    public DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_form_new);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        education_spinner = (Spinner) findViewById(R.id.educ_spinner);
        income_spinner = (Spinner) findViewById(R.id.income_spinner);
        gov_spinner = (Spinner) findViewById(R.id.gov_spinner);
        vgov_spinner = (Spinner) findViewById(R.id.vgov_spinner);
        cong_spinner = (Spinner) findViewById(R.id.cong_spinner);
        mayor_spinner = (Spinner) findViewById(R.id.mayor_spinner);
        bokal_spinner = (Spinner) findViewById(R.id.bokal_spinner);

        Intent intent = getIntent();

        Form form = intent.getParcelableExtra("Form");

        city_form = form.getCity();
        precinct_form = form.getPrecinct();
        barangay_form = form.getBarangay();
        votersname_form = form.getName();

        TextView vname_tv = (TextView) findViewById(R.id.vname_tv_id);
        vname_tv.setText("("+votersname_form+")");

        BirthDatePicker();

        String[] educArr = getResources().getStringArray(R.array.Highest_education);
        ArrayAdapter<String> adapterEducation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, educArr);
        education_spinner.setAdapter(adapterEducation);

        String[] incomeArr = getResources().getStringArray(R.array.Annual_income);

        ArrayAdapter<String> adapterIncome = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,android.R.id.text1,incomeArr)
        {
            @Override
            public View getDropDownView(final int position,final View convertView,final ViewGroup parent)
            {
                final View v=super.getDropDownView(position,convertView,parent);
                v.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ((TextView)v.findViewById(android.R.id.text1)).setSingleLine(false);
                    }
                });
                return v;
            }
        };
        adapterIncome.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        income_spinner.setAdapter(adapterIncome);


        String[] govArr = getResources().getStringArray(R.array.QuezProv_ch_governor);
        ArrayAdapter<String> adapterGovernor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, govArr);
        gov_spinner.setAdapter(adapterGovernor);

        String[] vgovArr = getResources().getStringArray(R.array.QuezProv_ch_vicegovernor);
        ArrayAdapter<String> adapterViceGovernor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vgovArr);
        vgov_spinner.setAdapter(adapterViceGovernor);

        String[] congArr = getResources().getStringArray(R.array.QuezProv_ch_2ndcongressman);
        ArrayAdapter<String> adapterCongressman = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, congArr);
        cong_spinner.setAdapter(adapterCongressman);

        String[] mayorArr = getResources().getStringArray(R.array.QuezProv_lucena_ch_mayor);
        ArrayAdapter<String> adapterMayor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mayorArr);
        mayor_spinner.setAdapter(adapterMayor);

    }

    public void BirthDatePicker(){
//        stringBirthday = TextUtils.join(", ", bdstring);

        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        Age = (EditText) findViewById(R.id.age_id);
//        if (stringBirthday != ""){
//            mDisplayDate.setText(stringBirthday);
//        }

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(QuestionForm_new.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
                String age_result = getAge(year,month,day);
                Age.setText(age_result);
            }
        };
    }

    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
