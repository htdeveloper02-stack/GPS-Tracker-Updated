package gps.trackerid.location.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import gps.trackerid.location.models.State;

public class Sqlhandler extends SQLiteOpenHelper {
    public Sqlhandler(Context context) {
        super(context, "mobileNumberfinderdatabase", (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE mobileNumberfinder(mobilenumber INTEGER PRIMARY KEY,operatorname TEXT,statename TEXT,iconval INTEGER, lat TEXT,lang TEXT)");
        sQLiteDatabase.execSQL("CREATE TABLE stdcodes(city TEXT,stdcode INTEGER )");
        sQLiteDatabase.execSQL("CREATE TABLE isdcodes(country TEXT,isdcode INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS mobileNumberfinder");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS stdcodes");
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS isdcodes");
        onCreate(sQLiteDatabase);
    }


    public List<State> getMobileData() {
        ArrayList arrayList = new ArrayList();
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT mobilenumber,operatorname, statename,iconval,lat,lang FROM mobileNumberfinder ", null);
        if (rawQuery.moveToFirst()) {
            do {
                State state = new State();
                state.setSetMobileno(rawQuery.getInt(0));
                state.setOperator(rawQuery.getString(1));
                state.setstate(rawQuery.getString(2));
                state.setIcon(Integer.parseInt(rawQuery.getString(3)));
                state.setLat(rawQuery.getString(4));
                state.setLang(rawQuery.getString(5));
                arrayList.add(state);
            } while (rawQuery.moveToNext());
            return arrayList;
        }
        return arrayList;
    }


    public List<State> getStdcodeBAseoncity(String str) {
        ArrayList arrayList = new ArrayList();
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT stdcode FROM stdcodes WHERE city LIKE '" + str + "'", null);
        if (rawQuery.moveToFirst()) {
            do {
                State state = new State();
                state.setIcon(Integer.parseInt(rawQuery.getString(0)));
                arrayList.add(state);
            } while (rawQuery.moveToNext());
            return arrayList;
        }
        return arrayList;
    }


    public List<State> getCityFromStdcode(String str) {
        ArrayList arrayList = new ArrayList();
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT city FROM stdcodes WHERE stdcode =" + str, null);
        if (rawQuery.moveToFirst()) {
            do {
                State state = new State();
                state.setstate(rawQuery.getString(0));
                arrayList.add(state);
            } while (rawQuery.moveToNext());
            return arrayList;
        }
        return arrayList;
    }


    public String[] getstate() {
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT city FROM stdcodes", null);
        int i = 0;
        if (rawQuery.getCount() > 0) {
            String[] strArr = new String[rawQuery.getCount()];
            while (rawQuery.moveToNext()) {
                strArr[i] = rawQuery.getString(rawQuery.getColumnIndex("city"));
                i++;
            }
            return strArr;
        }
        return new String[0];
    }


    public String[] getStdCode() {
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT stdcode FROM stdcodes", null);
        int i = 0;
        if (rawQuery.getCount() > 0) {
            String[] strArr = new String[rawQuery.getCount()];
            while (rawQuery.moveToNext()) {
                strArr[i] = rawQuery.getString(rawQuery.getColumnIndex("stdcode"));
                i++;
            }
            return strArr;
        }
        return new String[0];
    }


    public List<State> getSelectIsdCode(String str) {
        ArrayList arrayList = new ArrayList();
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT isdcode FROM isdcodes WHERE country LIKE '" + str + "'", null);
        if (rawQuery.moveToFirst()) {
            do {
                State c0820e = new State();
                c0820e.setIcon(Integer.parseInt(rawQuery.getString(0)));
                arrayList.add(c0820e);
            } while (rawQuery.moveToNext());
            return arrayList;
        }
        return arrayList;
    }


    public List<State> getIsdCode(String str) {
        ArrayList arrayList = new ArrayList();
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT country FROM isdcodes WHERE isdcode =" + str, null);
        if (rawQuery.moveToFirst()) {
            do {
                State state = new State();
                state.setstate(rawQuery.getString(0));
                arrayList.add(state);
            } while (rawQuery.moveToNext());
            return arrayList;
        }
        return arrayList;
    }


    public String[] GetCountryIsd() {
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT country FROM isdcodes", null);
        int i = 0;
        if (rawQuery.getCount() > 0) {
            String[] strArr = new String[rawQuery.getCount()];
            while (rawQuery.moveToNext()) {
                strArr[i] = rawQuery.getString(rawQuery.getColumnIndex("country"));
                i++;
            }
            return strArr;
        }
        return new String[0];
    }


    public String[] GetIsdCode() {
        Cursor rawQuery = getWritableDatabase().rawQuery("SELECT isdcode FROM isdcodes", null);
        int i = 0;
        if (rawQuery.getCount() > 0) {
            String[] strArr = new String[rawQuery.getCount()];
            while (rawQuery.moveToNext()) {
                strArr[i] = rawQuery.getString(rawQuery.getColumnIndex("isdcode"));
                i++;
            }
            return strArr;
        }
        return new String[0];
    }
}
