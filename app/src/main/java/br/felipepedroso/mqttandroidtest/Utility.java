package br.felipepedroso.mqttandroidtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.UnsupportedEncodingException;

/**
 * Created by FelipeAugusto on 26/06/2016.
 */
public class Utility {
    public static String getServerUri(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.pref_server_uri_key),
                context.getString(R.string.pref_server_uri_default));
    }

    public static byte[] encodeToUtf8(String str){
        byte[] encodedString;

        try {
            encodedString = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            encodedString = new byte[0];
        }

        return encodedString;
    }
}
