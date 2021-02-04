package com.example.baasumart.SessionManager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_EMAIL = "email";
    private static final  String KEY_USERNAME = "username";
    private static final  String KEY_PROFILEPIC = "profilePic";
    private static final  String KEY_ID = "id";
    private static final  String KEY_DISPLAYNAME = "displayName";
    private static final  String KEY_TOKEN = "token";

    public SessionManager(Context context) {
        this.context = context;
        userSession = context.getSharedPreferences("userLoginSession",Context.MODE_PRIVATE);
        editor = userSession.edit();
    }
    public void createLoginSession(String email, String username, String profilePic, String id, String displayName, String token){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PROFILEPIC, profilePic);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_DISPLAYNAME, displayName);
        editor.putString(KEY_TOKEN, token);
        editor.apply();

    }
    public HashMap<String, String> getUserDetailSession(){
        HashMap<String, String> userData = new HashMap<String, String>();
        userData.put(KEY_EMAIL,userSession.getString(KEY_EMAIL,null));
        userData.put(KEY_USERNAME,userSession.getString(KEY_USERNAME,null));
        userData.put(KEY_PROFILEPIC,userSession.getString(KEY_PROFILEPIC,null));
        userData.put(KEY_ID,userSession.getString(KEY_ID,null));
        userData.put(KEY_DISPLAYNAME,userSession.getString(KEY_DISPLAYNAME,null));
        userData.put(KEY_TOKEN,userSession.getString(KEY_TOKEN,null));
        return userData;
    }

    public  boolean checkLogin(){
        if(userSession.getBoolean(IS_LOGIN,false)){
            return true;
        }else{
            return false;
        }
    }
    public void logoutUserFromSession(){
        editor.clear();
        editor.apply();
    }
}
