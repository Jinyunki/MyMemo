package com.example.mymemo.dao;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Memo {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "date")
    private long date;

    @ColumnInfo(name = "contents")
    private String contents;

    private boolean isChecked = false;

    public Memo() {}

    public Memo(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public Memo(int id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Memo(String title, long date, String contents) {
        this.title = title;
        this.date = date;
        this.contents = contents;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
