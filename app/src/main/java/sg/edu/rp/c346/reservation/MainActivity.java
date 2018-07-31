package sg.edu.rp.c346.reservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etName;
    EditText etPhone;
    EditText etSize;
    CheckBox CheckBox;
    EditText etDay;
    EditText etTime;
    Button BtnReserve;
    Button BtnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.editTextName);
        etPhone = findViewById(R.id.editTextPhone);
        etSize = findViewById(R.id.editTextSize);
        CheckBox = findViewById(R.id.checkBoxSmoke);
        etDay = findViewById(R.id.editTextDay);
        etTime = findViewById(R.id.editTextTime);
        BtnReserve = findViewById(R.id.buttonReserve);
        BtnReset = findViewById(R.id.buttonReset);

        BtnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etName.getText().toString().trim().length()!=0 && etPhone.getText().toString().trim().length() !=0 && etSize.getText().toString().trim().length() !=0){
                    Calendar now = Calendar.getInstance();
                    Calendar res = Calendar.getInstance();
                    res.set(now.get(Calendar.YEAR) , (now.get(Calendar.MONTH)+1) , now.get(Calendar.DAY_OF_MONTH), res.get(Calendar.HOUR_OF_DAY) , res.get(Calendar.MINUTE));

                    if (now.after(res)){
                        Toast.makeText(MainActivity.this, "Please Select date & time after today" , Toast.LENGTH_LONG);
                        return;
                    }
                    String smoke = "";
                    if(CheckBox.isChecked())
                        smoke = "smoking";
                    else
                        smoke = "non-smoking";

                    Toast.makeText(MainActivity.this, "Hi, " + etName.getText().toString() +" You have Booked a " + etSize.getText().toString() + " person(s) " + smoke + " Table on " +  now.get(Calendar.DAY_OF_MONTH)+ "/" + now.get(Calendar.MONTH) + "/" + now.get(Calendar.YEAR) + res.get(Calendar.HOUR_OF_DAY) +":" + res.get(Calendar.MINUTE) + "Your Mobile: " + etPhone.getText().toString() + ".", Toast.LENGTH_LONG  ).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Please enter your Information again!" , Toast.LENGTH_LONG).show();
                }
            }
        });

        BtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                Calendar res = Calendar.getInstance();
                etName.setText("");
                etPhone.setText("");
                etSize.setText("");
                CheckBox.setChecked(false);
                etDay.setText(now.get(Calendar.DAY_OF_MONTH)+"/" + (now.get(Calendar.MONTH)+1) + "/" + now.get(Calendar.YEAR));
                etTime.setText(res.get(Calendar.HOUR_OF_DAY)+ ":" + res.get(Calendar.MINUTE));
                //datePicker.updateDate(2018, 5 ,1);
                //timePicker.setCurrentMinute(30);
                //timePicker.setCurrentHour(20);
            }
        });

        etDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etDay.setText("Date: " + dayOfMonth + "/" + (monthOfYear+1) + "/" +year);
                    }
                };

                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                int month = now.get(Calendar.MONTH)+1;
                int day = now.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog myDateDialog = new DatePickerDialog(MainActivity.this,myDateListener, year,month,day);
                myDateDialog.show();
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        etTime.setText("Time: " + hourOfDay + ":" + minute);
                    }
                };
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int minute = now.get(Calendar.MINUTE);

                TimePickerDialog myTimeDialog = new TimePickerDialog(MainActivity.this,myTimeListener,hour,minute,true);
                myTimeDialog.show();
            }
        });

        /*timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                if(hour <8){
                    Toast.makeText(MainActivity.this, "We open at 8am", Toast.LENGTH_LONG).show();
                }
                else if(hour >=21){
                    Toast.makeText(MainActivity.this, "We close at 9pm", Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }
    @Override
    protected void onPause() {
        super.onPause();

        String day = etDay.getText().toString();
        String time = etTime.getText().toString();
        String name = etName.getText().toString();
        String phone = etPhone.getText().toString();
        String size = etSize.getText().toString();
        Boolean check = CheckBox.isChecked();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = prefs.edit();
        prefEdit.putString("Day",day);
        prefEdit.putString("Time",time);
        prefEdit.putString("Name",name);
        prefEdit.putString("Phone",phone);
        prefEdit.putString("Size",size);
        prefEdit.putBoolean("Check",check);
        prefEdit.commit();
    }
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String day = prefs.getString("Day","");
        String time = prefs.getString("Time","");
        String name = prefs.getString("Name","");
        String phone = prefs.getString("Phone","");
        String size = prefs.getString("Size","");
        Boolean check = prefs.getBoolean("Check",false);
        etDay.setText(day);
        etTime.setText(time);
        etName.setText(name);
        etPhone.setText(phone);
        etSize.setText(size);
        CheckBox.setChecked(check);
    }
}