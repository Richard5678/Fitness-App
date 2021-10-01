package com.example.fitness.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;


    public Session(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //store if the user is logged in
    public void setLoggedin(boolean loggedin) {
        editor.putBoolean("loggedIn", loggedin);
        editor.commit();
    }

    //return a boolean that tells if the user is logged in
    public boolean loggedin() {
        return sharedPreferences.getBoolean("loggedIn", false);
    }

    //store the username for auto login
    public void setUsername(String username) {
        editor.putString("username", username);
        editor.commit();
    }

    //return the username
    public String getUsername() {
        return sharedPreferences.getString("username", "");
    }
}
