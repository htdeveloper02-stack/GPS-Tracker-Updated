package gps.trackerid.location.ads;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;

public class Preference {
    private static final String PREF_NAME = "GPSpref";

    public SharedPreferences sharedPreferences;

    public SharedPreferences.Editor editor;


    public Preference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        this.sharedPreferences = sharedPreferences;
        this.editor = sharedPreferences.edit();
    }

    public void clearPrefrence() {
        this.editor.clear();
        this.editor.commit();
    }

    public Boolean getBoolean(String str) {
        return Boolean.valueOf(this.sharedPreferences.getBoolean(str, true));
    }

    public Boolean getBoolean(String str, boolean z) {
        return Boolean.valueOf(this.sharedPreferences.getBoolean(str, z));
    }

    public Integer getInteger(String str) {
        return Integer.valueOf(this.sharedPreferences.getInt(str, 0));
    }

    public Integer getInteger(String str, int i) {
        return Integer.valueOf(this.sharedPreferences.getInt(str, i));
    }

    public String getString(String str) {
        return this.sharedPreferences.getString(str, (String) null);
    }

    public String getString(String str, String str2) {
        return this.sharedPreferences.getString(str, str2);
    }

    public void setBoolean(String str, Boolean bool) {
        this.editor.putBoolean(str, bool.booleanValue());
        this.editor.commit();
    }

    public void setInteger(String str, Integer num) {
        this.editor.putInt(str, num.intValue());
        this.editor.commit();
    }

    public void setString(String str, String str2) {
        this.editor.putString(str, str2);
        this.editor.commit();
    }

    public void setArrayList(String str, ArrayList<String> arrayList) {
        String delimiter = ",";

        StringBuilder stringBuilder = new StringBuilder();
        for (String item : arrayList) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(delimiter);
            }
            stringBuilder.append(item);
        }
        String arrayListString = stringBuilder.toString();
        editor.putString(str, arrayListString);
        editor.commit();
    }

    public ArrayList<String> getArrayList(String str) {
        String arrayListString = sharedPreferences.getString(str, null);
        String delimiter = ",";
        String[] array = arrayListString.split(delimiter);
        ArrayList<String> retrievedArrayList = new ArrayList<>(Arrays.asList(array));
        return retrievedArrayList;
    }

    public void setLong(String vpnstartime, long l) {
        this.editor.putLong(vpnstartime, l);
        this.editor.commit();
    }

    public long getLong(String vpnstartime) {
        return this.sharedPreferences.getLong(vpnstartime, 0);
    }

    public String getSavedLanguage() {
        if (sharedPreferences != null) {
            return this.sharedPreferences.getString("Lang", "en");
        } else {
            return "en";
        }
    }

    public void saveLanguage(String lang) {
        this.editor.putString("Lang", lang);
        this.editor.commit();
    }

}