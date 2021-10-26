package com.example.workMemo;

import java.sql.Date;

public class Record {
    //日付、work、メモを格納するクラス
    private Date date;
    private String work;
    private String memo;

    //空のコンストラクタ
    public Record(){ }

    //コンストラクタ
    public Record(String date, String work, String memo){
        this.date = java.sql.Date.valueOf(date);
        this.work = work;
        this.memo = memo;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString() {
        return String.valueOf(date);
    }

    public String getWork() {
        return work;
    }

    public String getMemo() {
        return memo;
    }

    public void appendMemo(String newString){
        this.memo = this.memo + "\n" + newString;
    }
}
