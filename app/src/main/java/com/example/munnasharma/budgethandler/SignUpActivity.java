package com.example.munnasharma.budgethandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import classes.Constant;
import classes.Encryption;
import classes.User;

public class SignUpActivity extends Activity {

    private FirebaseUser user;
    private Intent i;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int RC_SIGN_IN=1;
    private String encrypt;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Log.i(Constant.LOGTAG,"Activity "+getApplicationContext().toString()+" startted");
        Initialize();
    }
    private void Initialize(){
        //Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    createUser(user);
                    i=new Intent(getApplicationContext(),ShowExpenses.class);
                    startActivity(i);

                } else {
                    // User is signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RC_SIGN_IN) {
                if (resultCode == RESULT_OK) {
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();

                    if(user!=null) {

                        createUser(user);

                        i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);

                        Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {

            Log.i(Constant.LOGTAG,e.toString());
        }
    }


    private void createUser(FirebaseUser user) {
        //final String newFriendEncodedEmail = encodeEmail(newFriendEmail);

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
            final DatabaseReference userRef = mFirebaseDatabase.getReference(Constant.UserDetails+"/"+encodeEmail(user.getEmail()));
            Map<String, Object> map3 = new HashMap<>();
            User usr =new User(user.getDisplayName(),user.getEmail());
            usr.setUid(encrypt);
            map3.put(Constant.UserData,usr);
            userRef.updateChildren(map3);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
            Log.i(Constant.LOGTAG,e.toString());
        }
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
*/
    @Override
    public void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    public void onStop(){
        super.onStop();
        if(mAuthStateListener!=null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }


    private static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
}
