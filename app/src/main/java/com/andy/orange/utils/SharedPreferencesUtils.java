package com.andy.orange.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Andy Lau on 2017/8/10.
 */

public class SharedPreferencesUtils {
    private static final String TAG ="Andy";
    private static SharedPreferences sp;

    private static void init(Context context) {
        if (sp == null) {
            Log.d(TAG,context.toString());
            sp=context.getSharedPreferences("login_preferences",Context.MODE_PRIVATE);
        }
    }

    public static void setSharedData(Context context, String key, Object value, String type){

        if (type.isEmpty()) {
            return;
        }

        if (sp == null) {
            init(context);
        }

        if (type.equals("int")){
            sp.edit().putInt(key,(int)value).commit();
        }else if (type.equals("string")){
            sp.edit().putString(key,(String)value).commit();
        }else if (type.equals("boolean")){
            sp.edit().putBoolean(key,(boolean)value).commit();
        }else if (type.equals("float")){
            sp.edit().putFloat(key,(float)value).commit();
        }else {
            sp.edit().putLong(key,(long)value).commit();
        }
    }

    public static Object readSharedData(Context context, String key, String type){

        Object result = null;

        if (type.isEmpty()) {
            return null;
        }

        if (sp == null) {
            init(context);
        }

        if (type.equals("int")){
            result=sp.getInt(key,0);
        }else if (type.equals("string")){
            result=sp.getString(key,"");
        }else if (type.equals("boolean")){
            result=sp.getBoolean(key,false);
        }else if (type.equals("float")){
            result=sp.getFloat(key,0f);
        }else {
            result=sp.getLong(key,0L);
        }

        return result;
    }
}
