package com.randy.anniversarycalculator;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Common {

    private static final String TAG = "MYD - DBHandler";

    public Common(){}


    public String setTextSentence(String sentence, String date, String surfix) {
        String sInput;

        sInput = sentence + " " + getDiffDay(date) + " " + surfix;

        return sInput;
    }


    public long getDiffDay(String date) {
        long result = 0;
        long rawResult = 0;
        Calendar curCal = Calendar.getInstance();
        Calendar setCal = Calendar.getInstance();

        //Log.d(TAG, "mDate : " + mDate);

        // Convert String to Calendar
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");    // 월은 반드시 대문자 MM
            Date setDate = sdf.parse(date);
            //Log.d(TAG, "setDate : " + setDate);

            setCal.setTime(setDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        //종료날짜와 시작날짜의 차를 구합니다.
        //두날짜간의 차를 얻으려면 getTimeMills()를 이용하여 천분의 1초 단위로 변환하여야 합니다.
        rawResult = (curCal.getTimeInMillis() - setCal.getTimeInMillis()) / 1000;

        result = rawResult/(60*60*24);

        return result + 1;
    }
}