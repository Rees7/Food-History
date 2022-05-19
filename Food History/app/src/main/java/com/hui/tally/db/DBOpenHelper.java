package com.hui.tally.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hui.tally.R;

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(@Nullable Context context) {
        super(context,"tally.db" , null, 1);
    }

    /** create database */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table typetb(id integer primary key autoincrement,typename varchar(10),imageId integer,sImageId integer,kind integer)";
        db.execSQL(sql);
        insertType(db);

        sql = "create table accounttb(id integer primary key autoincrement,typename varchar(10),sImageId integer,beizhu varchar(80),money float," +
                "time varchar(60),year integer,month integer,day integer,kind integer)";
        db.execSQL(sql);
    }

    private void insertType(SQLiteDatabase db) {
        /** the showing elements in form */
        String sql = "insert into typetb (typename,imageId,sImageId,kind) values (?,?,?,?)";

        db.execSQL(sql, new Object[]{"Others", R.mipmap.out_others, R.mipmap.out_others_select, 1});
        db.execSQL(sql, new Object[]{"Football", R.mipmap.football, R.mipmap.football_select, 1});
        db.execSQL(sql, new Object[]{"Basketball", R.mipmap.basketball, R.mipmap.basketball_select, 1});
        db.execSQL(sql, new Object[]{"Volleyball", R.mipmap.volleyball, R.mipmap.volleyball_select, 1});
        db.execSQL(sql, new Object[]{"Weight-lift", R.mipmap.weightlift, R.mipmap.weightlift_select, 1});
        db.execSQL(sql, new Object[]{"Jogging", R.mipmap.jogging, R.mipmap.jogging_select, 1});
        db.execSQL(sql, new Object[]{"Skipping", R.mipmap.skipping, R.mipmap.skipping_select, 1});
        db.execSQL(sql, new Object[]{"Ping-Pong", R.mipmap.pingpong, R.mipmap.pingpong_select, 1});
        db.execSQL(sql, new Object[]{"Badminton", R.mipmap.badminton, R.mipmap.badminton_select, 1});

        db.execSQL(sql, new Object[]{"Others", R.mipmap.in_others, R.mipmap.in_others_select, 0});
        db.execSQL(sql, new Object[]{"Egg", R.mipmap.egg, R.mipmap.egg_select, 0});
        db.execSQL(sql, new Object[]{"Bread", R.mipmap.bread, R.mipmap.bread_select, 0});
        db.execSQL(sql, new Object[]{"Chocolate", R.mipmap.chocolate, R.mipmap.chocolate_select, 0});
        db.execSQL(sql, new Object[]{"Biscuit", R.mipmap.biscuit, R.mipmap.biscuit_select, 0});
        db.execSQL(sql, new Object[]{"Chicken", R.mipmap.chicken, R.mipmap.chicken_select, 0});
        db.execSQL(sql, new Object[]{"Hamburger", R.mipmap.humburger, R.mipmap.humburger_select, 0});
        db.execSQL(sql, new Object[]{"Pizza", R.mipmap.pizza, R.mipmap.pizza_select, 0});

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
