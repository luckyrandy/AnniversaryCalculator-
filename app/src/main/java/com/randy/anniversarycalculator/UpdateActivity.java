package com.randy.anniversarycalculator;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity {
    private static final String TAG = "MYD - UpdateActivity";

    Calendar calendar = Calendar.getInstance();
    private int iYear, iMonth, iDay;

    String mUserInput = "";
    String mDate = "";

    private int mId;

    private TextView text_sentence;
    private EditText edit_title;
    private Button btn_date;
    private Toolbar sub_toolbar;
    private ScrollView scrollView;

    private String mEnabledColor = "#4169E1";
    private String mDisabledColor = "#A8A8A8";

    DBHandler db = new DBHandler(this);

    private Common mCommon = new Common();


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
        setContentView(R.layout.update_layout);

        setupViews();

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


        Intent intent = getIntent();

        mId = intent.getIntExtra("id", -1);

        mDate = intent.getStringExtra("date");

        setCalendar();

        mUserInput = intent.getStringExtra("sentence");
        edit_title.setText(mUserInput);

    }


    private void setupViews() {
        text_sentence = (TextView) findViewById(R.id.text_sentence);
        edit_title    = (EditText) findViewById(R.id.edit_title);
        btn_date      = (Button) findViewById(R.id.btn_set_date);

        sub_toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        setSupportActionBar(sub_toolbar);
        getSupportActionBar().setTitle(R.string.update_title);

        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        scrollView.getBackground().setAlpha(100);
    }


    public void onButtonClick(View view) {
        // 버튼 ID를 가져온다.
        switch (view.getId()) {
            case R.id.btn_set_date:       // 날짜 설정 버튼
                new DatePickerDialog(UpdateActivity.this,
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
                updateItem();
                //키보드를 없앤다.
                //imm.hideSoftInputFromWindow(edit_title.getWindowToken(), 0);
                break;
        }
    }


    public void setCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");    // 월은 반드시 대문자 MM
        Date setDate = null;
        try {
            setDate = sdf.parse(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(setDate);

        String sDate = "";

        iYear = calendar.get(Calendar.YEAR);
        iMonth = calendar.get(Calendar.MONTH) + 1;
        iDay = calendar.get(Calendar.DAY_OF_MONTH);

        sDate = String.valueOf(iYear) + "/" + String.valueOf(iMonth) + "/" + String.valueOf(iDay);

        btn_date.setText(sDate);
    }


    public void updateItem() {
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

        db.updateItem(mId, new Item(mDate, mUserInput, 0, 0, 0, 0));

        Toast.makeText(UpdateActivity.this, R.string.update_ok, Toast.LENGTH_SHORT).show();

        finish();           // Activity 종료
    }
}





