package com.example.balante_midterm;

import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

        import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Gamedata.db", null, 1);
    }
    @Override

    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Gamedetails(id TEXT primary key, title TEXT, date TEXT, developer TEXT, publisher TEXT, genre TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("drop Table if exists Gamedetails");
        onCreate(DB);
    }
    public Boolean insertuserdata(String id, String title, String date, String developer, String publisher, String genre)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("title", title);
        contentValues.put("date", date);
        contentValues.put("developer", developer);
        contentValues.put("publisher", publisher);
        contentValues.put("genre", genre);
        long result=DB.insert("Gamedetails", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public Boolean updateuserdata(String id, String title, String date, String developer, String publisher, String genre)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("date", date);
        contentValues.put("developer", developer);
        contentValues.put("publisher", publisher);
        contentValues.put("genre", genre);
        Cursor cursor = DB.rawQuery("Select * from Productdetails where id = ?", new String[]{id});
        if (cursor.getCount() > 0) {
            long result = DB.update("Gamedetails", contentValues, "id=?", new String[]{id});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public Boolean deletedata (String id)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Gamedetails where id = ?", new String[]{id});
        if (cursor.getCount() > 0) {
            long result = DB.delete("Gamedetails", "id=?", new String[]{id});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Cursor getdata ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Gamedetails", null);
        return cursor;
    }
}