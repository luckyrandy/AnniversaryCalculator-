package com.randy.anniversarycalculator;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class InputActivity extends AppCompatActivity {
    private static final String TAG = "MYD - InputActivity";

    Calendar calendar = Calendar.getInstance();
    private int iYear, iMonth, iDay;
    private int interval = 0;
    private int iHour, iMin;
    private int isTimeSet = 0;
    private int insert_result = 0;

    String mUserInput = "";
    String mDate = "";

    private TextView text_sentence;
    private EditText edit_title;
    private Button btn_date;
    private Toolbar sub_toolbar;
    //private Switch alarm_switch;
    //private Spinner spinner_interval;
    //private Button btn_time;
    private ScrollView scrollView;

    private String mEnabledColor = "#4169E1";
    private String mDisabledColor = "#A8A8A8";

    ArrayAdapter spinnerAdapter;

    DBHandler db = new DBHandler(this);

    private Common mCommon = new Common();

    private InputMethodManager imm;

    private AlarmManager mAlarmMgr;

    DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    calendar.set(year, monthOfYear, dayOfMonth);

                    String sDate = "";

                    iYear = calendar.get(Calendar.YEAR);
                    iMonth = calendar.get(Calendar.MONTH) + 1;
                    iDay = calendar.get(Calendar.DAY_OF_MONTH);

                    // for Database
                    mDate = "";
                    mDate = String.format("%04d", iYear) + String.format("%02d", iMonth) + String.format("%02d", iDay);
                    //Log.d(TAG, "Anniversary : " + mDate);

                    sDate = String.valueOf(iYear) + "/" + String.valueOf(iMonth) + "/" + String.valueOf(iDay);

                    btn_date.setText(sDate);

                    text_sentence.setText(mCommon.setTextSentence(edit_title.getText().toString(), mDate, getString(R.string.surfix)));
                }
            };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_layout);

        setupViews();

        //초기 시간 설정
        final Calendar c = Calendar.getInstance();
        iHour = c.get(Calendar.HOUR_OF_DAY);
        iMin = c.get(Calendar.MINUTE);

        // for Alarm
        mAlarmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);


        edit_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
                text_sentence.setText(mCommon.setTextSentence(edit_title.getText().toString(), mDate, getString(R.string.surfix)));
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
                //textSentence.setText(setTextSentence(editTitle.getText().toString()));
            }
        });


        /*
        //attach a listener to check for changes in state
        alarm_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Toast.makeText(SettingActivity.this, "Switch is currently ON", Toast.LENGTH_SHORT).show();
                    spinner_interval.setEnabled(true);
                    btn_time.setEnabled(true);
                    btn_time.setTextColor(Color.parseColor(mEnabledColor));
                    //mTextView.setText(com.randy.mygoodsaying.R.string.set_alarm_msg);
                } else {
                    //Toast.makeText(SettingActivity.this, "Switch is currently OFF", Toast.LENGTH_SHORT).show();
                    spinner_interval.setEnabled(false);
                    btn_time.setEnabled(false);
                    btn_time.setTextColor(Color.parseColor(mDisabledColor));
                    btn_time.setText(R.string.btn_time);

                    isTimeSet = 0;
                    //mTextView.setText(com.randy.mygoodsaying.R.string.set_alarm_off_msg);
                    //Toast.makeText(SettingActivity.this, com.randy.mygoodsaying.R.string.set_alarm_off_msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        */
    }


    public void onButtonClick(View view) {
        // 버튼 ID를 가져온다.
        switch (view.getId()) {
            case R.id.btn_set_date:       // 날짜 설정 버튼
                new DatePickerDialog(InputActivity.this,
                        dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            /*
            case R.id.btn_time:                // 시간 설정 버튼
                Dialog dlgTime = new TimePickerDialog(this, myTimeSetListener, iHour,
                        iMin, false);
                dlgTime.show();

                break;
                */
            case R.id.btn_save:         // 저장 버튼 설정
                insertItem();
                //키보드를 없앤다.
                //imm.hideSoftInputFromWindow(edit_title.getWindowToken(), 0);
                break;
        }
    }


    private void setupViews() {
        text_sentence = (TextView) findViewById(R.id.text_sentence);
        edit_title    = (EditText) findViewById(R.id.edit_title);
        btn_date      = (Button) findViewById(R.id.btn_set_date);

        sub_toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        setSupportActionBar(sub_toolbar);
        getSupportActionBar().setTitle(R.string.insert_title);

        /*
        alarm_switch = (Switch) findViewById(R.id.switch_alarm);
        alarm_switch.setChecked(false);

        spinner_interval = (Spinner) findViewById(R.id.spinner_interval);
        spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.interval, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_interval.setAdapter(spinnerAdapter);
        spinner_interval.setEnabled(false);

        btn_time = (Button) findViewById(R.id.btn_time);
        btn_time.setEnabled(false);
        btn_time.setTextColor(Color.parseColor(mDisabledColor));
        */

        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        scrollView.getBackground().setAlpha(100);
    }


    /*
    private TimePickerDialog.OnTimeSetListener myTimeSetListener
            = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            isTimeSet = 1;

            iHour = hourOfDay;
            iMin = minute;

            String time = String.format("%02d", iHour) + ":" +
                    String.format("%02d", iMin) + " " +
                    getString(R.string.set_alarm_done_msg);

            btn_time.setText(time);

            //Toast.makeText(InputActivity.this, time, Toast.LENGTH_SHORT).show();
        }
    };
    */


    public void insertItem() {
        // check Date
        if (mDate == null || mDate.length() == 0) {
            Toast.makeText(this, R.string.warn_date, Toast.LENGTH_SHORT).show();
            btn_date.requestFocus();         // focus on
            return;
        }

        // check user input
        mUserInput = edit_title.getText().toString();

        if (mUserInput == null || mUserInput.length() == 0) {
            Toast.makeText(this, R.string.warn_title, Toast.LENGTH_SHORT).show();
            edit_title.requestFocus();       // focus on
            return;
        }

        /*
        // check alarm setting
        if (alarm_switch.isChecked()) {                 // alarm is enable
            interval = (int) spinner_interval.getSelectedItemId();

            if (isTimeSet == 0) {                       // Time is not set.
                Toast.makeText(this, R.string.warn_set_time, Toast.LENGTH_SHORT).show();
                return;
            }

            // insert to DB
            insert_result = (int) db.insertItem(new Item(mDate, mUserInput, 1, interval, iHour, iMin));
            if (insert_result > 0) {
                setAlarm();
                Toast.makeText(this, R.string.insert_ok, Toast.LENGTH_SHORT).show();
                finish();       // 저장 후 엑티비티 종료
            } else {
                Toast.makeText(this, R.string.insert_fail, Toast.LENGTH_SHORT).show();
            }
        } else {                                        // alarm is disable
            // insert to DB
            insert_result = (int) db.insertItem(new Item(mDate, mUserInput, 0, 0, 0, 0));
            if (insert_result > 0) {
                cancelAlarm();
                Toast.makeText(this, R.string.insert_ok, Toast.LENGTH_SHORT).show();
                finish();       // 저장 후 엑티비티 종료
            } else {
                Toast.makeText(this, R.string.insert_fail, Toast.LENGTH_SHORT).show();
            }
        }
        */

        insert_result = (int) db.insertItem(new Item(mDate, mUserInput, 0, 0, 0, 0));
        if (insert_result > 0) {
            //cancelAlarm();
            Toast.makeText(this, R.string.insert_ok, Toast.LENGTH_SHORT).show();
            finish();       // 저장 후 엑티비티 종료
        } else {
            Toast.makeText(this, R.string.insert_fail, Toast.LENGTH_SHORT).show();
        }

        return;
    }


    public void setAlarm() {
        long triggerTime = 0;
        long intervalTime = 24 * 60 * 60 * 1000;    // 24시간

        // 우선 알람 해제  ... 먼저 설정되엇던 것이 잇을 지도 모르니...
        cancelAlarm();

        // Alarm 등록
        Intent intent = new Intent(this, AlarmReceive.class);   //AlarmReceive.class이클레스는 따로 만들꺼임 알람이 발동될때 동작하는 클레이스임
        intent.putExtra("id", insert_result);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        triggerTime = setTriggerTime();

        //알람 예약
        mAlarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, intervalTime, pIntent);
    }


    private void cancelAlarm()
    {
        Intent intent = new Intent(this, AlarmReceive.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmMgr.cancel(pIntent);
    }


    private long setTriggerTime()
    {
        // current Time
        long atime = System.currentTimeMillis();

        // timepicker
        Calendar curTime = Calendar.getInstance();
        curTime.set(Calendar.HOUR_OF_DAY, iHour);
        curTime.set(Calendar.MINUTE, iMin);
        curTime.set(Calendar.SECOND, 0);
        curTime.set(Calendar.MILLISECOND, 0);

        long btime = curTime.getTimeInMillis();
        long triggerTime = btime;
        if (atime > btime)
            triggerTime += 1000 * 60 * 60 * 24;

        return triggerTime;
    }
}





