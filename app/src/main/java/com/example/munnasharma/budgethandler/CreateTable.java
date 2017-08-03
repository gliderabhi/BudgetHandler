package com.example.munnasharma.budgethandler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import classes.Constant;
import classes.Tables;

public class CreateTable extends AppCompatActivity {

    private EditText TableName,CreditValue;
    private String tableName;
    private Float Credit;
    private DatabaseReference mFirebaseDatabase;
    private Tables table;
    private Calendar c;
    private Button addBtn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_table);

        Intialize();
    }

    private void Intialize() {
        TableName =(EditText)findViewById(R.id.NameBox);
        CreditValue=(EditText)findViewById(R.id.CreditBox);

        String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        mFirebaseDatabase=FirebaseDatabase.getInstance().getReference()
                .child(Constant.UserDetails+"/"+encodeEmail(email)+"/"+Constant.Tables);

        addBtn=(Button)findViewById(R.id.AddTableButton);
        sharedPreferences=getSharedPreferences(Constant.MYPref,MODE_PRIVATE);
        editor=sharedPreferences.edit();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addtable();
            }
        });

    }
    private static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    private void Addtable(){

        tableName=TableName.getText().toString();
        Credit=Float.valueOf(CreditValue.getText().toString());

        table=new Tables();
        table.setTablesName(tableName);
        table.setCredit(Credit);
        table.setBalance(Credit);
        table.setTableMonth(getName());

       try {
           HashMap<String, Object> tabl = new HashMap<>();
           tabl.put(tableName, table);
           mFirebaseDatabase.updateChildren(tabl);
       }catch (Exception e){
           Log.i(Constant.LOGTAG,e.toString());
       }
        mFirebaseDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i(Constant.LOGTAG,"Done Added");
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private String getName(){
        c=Calendar.getInstance();
        int mnth=c.get(Calendar.MONTH);
        String month=null;
        switch (mnth){
            case 0: month="January";break;
            case 1: month="February";break;
            case 2: month="March";break;
            case 3: month="April";break;
            case 4: month="May";break;
            case 5: month="June";break;
            case 6: month="July";break;
            case 7: month="August";break;
            case 8: month="September";break;
            case 9: month="October";break;
            case 10: month="November";break;
            case 11: month="December";break;
        }
        editor.putInt(Constant.Month,mnth);
        editor.commit();
        return  month;
    }
}
