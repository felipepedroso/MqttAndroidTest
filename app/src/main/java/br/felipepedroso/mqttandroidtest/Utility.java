package br.felipepedroso.mqttandroidtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by FelipeAugusto on 26/06/2016.
 */
public class Utility {
    public static String getServerUri(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.pref_server_uri_key),
                context.getString(R.string.pref_server_uri_default));
    }
}
