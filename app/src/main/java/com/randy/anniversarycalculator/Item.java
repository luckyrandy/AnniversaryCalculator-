package com.randy.anniversarycalculator;

public class Item {
    private int iId;
    private String sDate;
    private String sSentence;
    private int iNotiFlag;
    private int iNotiInterval;
    private int iHour;
    private int iMin;
    private String sListText;

    public Item(){}

    public Item(String sDate, String sSentence, int iNotiFlag, int iNotiInterval, int iHour, int iMin) {
        super();
        this.sDate = sDate;
        this.sSentence = sSentence;
        this.iNotiFlag = iNotiFlag;
        this.iNotiInterval = iNotiInterval;
        this.iHour = iHour;
        this.iMin = iMin;
    }


    public int getId() {
        return iId;
    }

    public void setId(int iId) {
        this.iId = iId;
    }

    public String getDate() {
        return sDate;
    }

    public void setDate(String sDate) {
        this.sDate = sDate;
    }

    public String getSentence() {
        return sSentence;
    }

    public void setSentence(String sSentence) {
        this.sSentence = sSentence;
    }

    public int getNotiFlag() {
        return iNotiFlag;
    }

    public void setNotiFlag(int iNotiFlag) {
        this.iNotiFlag = iNotiFlag;
    }

    public int getiNotiInterval() {
        return iNotiInterval;
    }

    public void setiNotiInterval(int iNotiInterval) {
        this.iNotiInterval = iNotiInterval;
    }

    public int getHour() {
        return iHour;
    }

    public void setHour(int iHour) {
        this.iHour = iHour;
    }

    public int getMin() {
        return iMin;
    }

    public void setMin(int iMin) {
        this.iMin = iMin;
    }

    public String getsListText() {
        return sListText;
    }

    public void setsListText(String sListText) {
        this.sListText = sListText;
    }
}