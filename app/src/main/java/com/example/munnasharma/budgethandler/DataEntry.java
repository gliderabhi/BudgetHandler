package com.example.munnasharma.budgethandler;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.models.Image;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import classes.Constant;
import classes.ExpensesOrAddition;
import classes.categoryDetails;

public class DataEntry extends AppCompatActivity {

    private TextView done,amount,categoryText;
    private EditText dateText;
    private ImageView back, categoryImg,dateImg;
    private Intent i;
    private String date,category,imagUrl;
    private String value,tableName;
    private ProgressDialog pr;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference expensesList;
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
     back = (ImageView) findViewById(R.id.BackButton);
     back.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             i = new Intent(DataEntry.this, ShowExpenses.class);
             startActivity(i);
         }
     });

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



    private void submitDetails(){
       value=amount.getText().toString();

        expensesList=FirebaseDatabase.getInstance().getReference().child(tableName);

        ExpensesOrAddition expensesOrAddition=new ExpensesOrAddition();
        expensesOrAddition.setAmount(Float.valueOf(value));
        expensesOrAddition.setCategoryName(category);
        expensesOrAddition.setCategoryImageUrl(imagUrl);
        expensesOrAddition.setTimeStamp(date);


        HashMap<String,Object> expenseObject=new HashMap<>();
        expenseObject.put(expensesList.push().getKey(),expensesOrAddition);
        expensesList.updateChildren(expenseObject);
 }

}
