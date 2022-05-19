package com.hui.tally.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hui.tally.utils.FloatUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class DBManager {

    private static SQLiteDatabase db;
    /* initialize*/
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);  //get helper obj
        db = helper.getWritableDatabase();      //get db obj
    }

    public static List<TypeBean>getTypeList(int kind){
        List<TypeBean>list = new ArrayList<>();
        //read from typetb
        String sql = "select * from typetb where kind = "+kind;
        Cursor cursor = db.rawQuery(sql, null);
//  read content and store
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind);
            list.add(typeBean);
        }
        return list;
    }

    /**
    * insert one record
    * */
    public static void insertItemToAccounttb(AccountBean bean){
        ContentValues values = new ContentValues();
        values.put("typename",bean.getTypename());
        values.put("sImageId",bean.getsImageId());
        values.put("beizhu",bean.getBeizhu());
        values.put("money",bean.getMoney());
        values.put("time",bean.getTime());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("kind",bean.getKind());
        db.insert("accounttb",null,values);
    }
    /*
    * get daily record
    * */
    public static List<AccountBean>getAccountListOneDayFromAccounttb(int year,int month,int day){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        //read all records
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    /*
     * get monthly record
     * */
    public static List<AccountBean>getAccountListOneMonthFromAccounttb(int year,int month){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        //read all records
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }
    /**
     * read records on specific day  kind：out==0    in===1
     * */
    public static float getSumMoneyOneDay(int year,int month,int day,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }
    /**
     * read records in specific month kind：out==0    in===1
     * */
    public static float getSumMoneyOneMonth(int year,int month,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }
    /** num of in or out records */
    public static int getCountItemOneMonth(int year,int month,int kind){
        int total = 0;
        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndex("count(money)"));
            total = count;
        }
        return total;
    }
    /**
     * total amount within one year
     * */
    public static float getSumMoneyOneYear(int year,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    /**
    * delete accounttb data
    * */
    public static int deleteItemFromAccounttbById(int id){
        int i = db.delete("accounttb", "id=?", new String[]{id + ""});
        return i;
    }
    /** search function */
    public static List<AccountBean>getAccountListByRemarkFromAccounttb(String beizhu){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where beizhu like '%"+beizhu+"%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String bz = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, bz, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    /**
     * read the information of years (no use)
     * */
    public static List<Integer>getYearListFromAccounttb(){
        List<Integer>list = new ArrayList<>();
        String sql = "select distinct(year) from accounttb order by year asc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        return list;
    }

    /**
    * delete all accounttb data
    * */
    public static void deleteAllAccount(){
        String sql = "delete from accounttb";
        db.execSQL(sql);
    }

    /**
     * get record in specific type (eat)
     * */
    public static List<ChartItemBean>getChartListFromAccounttb(int year,int month,int kind){
        List<ChartItemBean>list = new ArrayList<>();
        float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind);
        String sql = "select typename,sImageId,sum(money)as total from accounttb where year=? and month=? and kind=? group by typename " +
                "order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            float total = cursor.getFloat(cursor.getColumnIndex("total"));
            //get percentage  total /sumMonth
            float ratio = FloatUtils.div(total,sumMoneyOneMonth);
            ChartItemBean bean = new ChartItemBean(sImageId, typename, ratio, total);
            list.add(bean);
        }
        return list;
    }

    /**
    * get MAX_OUT
     * */
    public static float getMaxMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            return money;
        }
        return 0;
    }

    /** read the records every day in specific month*/
    public static List<BarChartItemBean>getSumMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select day,sum(money) from accounttb where year=? and month=? and kind=? group by day";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        List<BarChartItemBean>list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            float smoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            BarChartItemBean itemBean = new BarChartItemBean(year, month, day, smoney);
            list.add(itemBean);
        }
        return list;
    }

}
