package com.example.munnasharma.budgethandler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

import classes.Constant;
import classes.ExpensesOrAddition;
import classes.Tables;

public class DataEntry extends AppCompatActivity {

    private TextView done,amount,categoryText;
    private EditText dateText;
    private ImageView back, categoryImg,dateImg;
    private Intent i;
    private int multiFac;
    private EditText comment;
    private String commentText;
    private float balance;
    private String date,category,imagUrl;
    private String value,tableName;
    private ProgressDialog pr;
    private float expenditure,credit;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference expensesList,tableRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        sharedPreferences=getApplicationContext().getSharedPreferences(Constant.MYPref,MODE_PRIVATE);
        date=sharedPreferences.getString(Constant.Date,"Today");
        category=sharedPreferences.getString(Constant.Category,"Food and beverages");
        imagUrl=sharedPreferences.getString(Constant.CategoryImage,null);
        value=sharedPreferences.getString(Constant.Amount,"0");
        tableName=sharedPreferences.getString(Constant.TableName,null);


        initialize();

    }
 private void initialize() {
     String userEmail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
     tableRef=FirebaseDatabase.getInstance().getReference()
             .child(Constant.UserDetails+"/"+encodeEmail(userEmail)+"/"+Constant.Tables);

     back = (ImageView) findViewById(R.id.BackButton);
     back.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             i = new Intent(DataEntry.this, ShowExpenses.class);
             startActivity(i);
         }
     });
     comment=(EditText)findViewById(R.id.Comment);

     done = (TextView) findViewById(R.id.ButtonDone);
     done.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             submitDetails();
         }
     });


     amount = (EditText) findViewById(R.id.AmountValue);

     dateText=(EditText)findViewById(R.id.DateSelectText);
     dateText.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             i=new Intent(getApplicationContext(),CalendarActivity.class);
             startActivity(i);
         }
     });

     categoryText=(TextView)findViewById(R.id.CategorySelectText);
     categoryText.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             i=new Intent(DataEntry.this,SelectCategory.class);
             editor=sharedPreferences.edit();
             editor.putString(Constant.Amount,amount.getText().toString());
             editor.commit();
             startActivity(i);
         }
     });
     dateImg = (ImageView) findViewById(R.id.calendar);
     dateImg.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
           pr=ProgressDialog.show(DataEntry.this,"wait","",true);

           i=new Intent(getApplicationContext(),CalendarActivity.class);
             startActivity(i);
             pr.dismiss();
         }
     });
     categoryText.setText(category);
     dateText.setText(date);
 }
    private static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }



    private void submitDetails(){
       value=amount.getText().toString();
        commentText=comment.getText().toString();
        expensesList=FirebaseDatabase.getInstance().getReference()
                .child(encodeEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())+tableName);

        ExpensesOrAddition expensesOrAddition=new ExpensesOrAddition();
        expensesOrAddition.setAmount(Float.valueOf(value));
        expensesOrAddition.setCategoryName(category);
        expensesOrAddition.setCategoryImageUrl(imagUrl);
        expensesOrAddition.setTimeStamp(date);
        expensesOrAddition.setExtraComment(commentText);

        balance=sharedPreferences.getFloat(Constant.Credit,0);
        credit=sharedPreferences.getFloat(Constant.Credit,00);
        multiFac=sharedPreferences.getInt(Constant.CategoryMultiply,1);
        expenditure=sharedPreferences.getFloat(Constant.Expenditure,00);

        expenditure+=Float.valueOf(value);

        if(multiFac==1){
            balance=balance-Float.valueOf(value);
        }
        if(multiFac==-1){
            credit+=Float.valueOf(value);
        }

        Tables table=new Tables();
        table.setTableMonth(sharedPreferences.getString(Constant.TableMonth,"February"));
        table.setTablesName(tableName);
        table.setCredit(credit);
        table.setBalance(balance);
        table.setExpenditure(expenditure);

        HashMap<String, Object> map=new HashMap<>();
        map.put(tableName,table);
        tableRef.updateChildren(map);

        Log.i(Constant.LOGTAG,String.valueOf(balance));
        HashMap<String,Object> expenseObject=new HashMap<>();
        expenseObject.put(expensesList.push().getKey(),expensesOrAddition);
        expensesList.updateChildren(expenseObject);

        expensesList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                chnageActivity();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                chnageActivity();
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
 private  void chnageActivity(){
     Intent i =new Intent(getApplicationContext(),ShowExpenses.class);
     startActivity(i);
 }

}
