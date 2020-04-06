package com.example.transportcontrol;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private static final String PREF_NAME = "TransportControl";

    private static final String KEY_USER_IS_REGISTERED = "userIsRegistered";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean isUserRegistered() {
        return pref.getBoolean(KEY_USER_IS_REGISTERED, false);
    }

    public void setUserRegistered(boolean value) {
        editor.putBoolean(KEY_USER_IS_REGISTERED, value);
        editor.apply();
    }
}
