package com.example.munnasharma.budgethandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

import classes.Constant;
import classes.Encryption;
import classes.User;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private Intent i;
    private FirebaseUser user;
    private String encrypt;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                user = FirebaseAuth.getInstance().getCurrentUser();

                Log.i(Constant.LOGTAG,user.getEmail());
                if (user == null) {
                         i = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivity(i);
                    Log.i(Constant.LOGTAG,"happening");
                }else{
                    createUser(user);
                    i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    Log.i(Constant.LOGTAG,"NotHappening");
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    private static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }


    private void createUser(FirebaseUser user) {
       mFirebaseDatabase=FirebaseDatabase.getInstance();
        try {
            encrypt = Encryption.main(encodeEmail(user.getEmail()));
            sharedPreferences=getApplicationContext().getSharedPreferences(Constant.MYPref,MODE_PRIVATE);
            editor=sharedPreferences.edit();
            editor.putString(Constant.Encrypted,encrypt);
            editor.commit();

        }catch (Exception e){
            Log.i(Constant.LOGTAG,e.toString());
        }
        try{
             userRef =  mFirebaseDatabase.getReference(Constant.UserDetails+"/"+encodeEmail(user.getEmail()));
            HashMap<String, Object> map3 = new HashMap<>();
            User usr =new User(user.getDisplayName(),user.getEmail());
            usr.setUid(encrypt);
            map3.put(Constant.UserData,usr);
            userRef.updateChildren(map3);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
           // createUser(user);
            Log.i(Constant.LOGTAG,e.toString());
        }
    }

}
