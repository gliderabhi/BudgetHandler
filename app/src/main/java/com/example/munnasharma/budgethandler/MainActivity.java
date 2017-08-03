package com.example.munnasharma.budgethandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import classes.Constant;
import classes.Tables;

public class MainActivity extends AppCompatActivity {

    private ListView tablesList;
    private FirebaseDatabase tableList;
    private DatabaseReference tables;
    private FirebaseListAdapter mAdaptor;
    private DatabaseReference tableRef;
    private ProgressDialog pr;
    private TextView waitText;
    private Calendar c;
    private int month;
    private Intent i;
    private String currentUserEmail;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       sharedPreferences=getSharedPreferences(Constant.MYPref, Context.MODE_PRIVATE);
       //checkForNewMonth();
        intialize();
        pr=ProgressDialog.show(MainActivity.this,"Wait","",true);
       PopulateList();
    }

   private void checkForNewMonth() {
        c=Calendar.getInstance();
        int mnth=c.get(Calendar.MONTH);
       Log.i(Constant.LOGTAG,String.valueOf(mnth));
        month=sharedPreferences.getInt(Constant.Month,0);
        if(mnth==month){
           PopulateList();
        }else{
           i=new Intent(getApplicationContext(),CreateTable.class);
            startActivity(i);
        }
    }

    private void PopulateList() {
      try {
          mAdaptor = new FirebaseListAdapter<Tables>(this, Tables.class, R.layout.tables_item, tables) {
              @Override
              protected void populateView(final View view, Tables tables, final int position) {

                  TextView tableName = (TextView) view.findViewById(R.id.TableName);
                  TextView tableMonth=(TextView)view.findViewById(R.id.TableMonth);

                  String taleName = tables.getTablesName();
                  // taleName = taleName.substring(0, 1).toUpperCase() + taleName.substring(1);
                  tableName.setText(taleName);
                  tableMonth.setText(tables.getTableMonth());

                  tableRef.addChildEventListener(new ChildEventListener() {
                      @Override
                      public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                      }

                      @Override
                      public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                          Log.i(Constant.LOGTAG, "Childd chnaged");
                          PopulateList();
                      }

                      @Override
                      public void onChildRemoved(DataSnapshot dataSnapshot) {
                          Log.i(Constant.LOGTAG, "Childd remoed");
                          PopulateList();
                      }

                      @Override
                      public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                          Log.i(Constant.LOGTAG, "Childd moved");
                          PopulateList();
                      }

                      @Override
                      public void onCancelled(DatabaseError databaseError) {

                          Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                      }
                  });
              }
          };

          tablesList.setAdapter(mAdaptor);
          pr.dismiss();
      }catch (Exception e){
          Log.i(Constant.LOGTAG,e.toString());
      }
          tablesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tableLoc=mAdaptor.getRef(position).toString();
                 if(tableLoc!=null) {
                     try {
                         Tables tables = (Tables) mAdaptor.getItem(position);
                         Intent i = new Intent(getApplicationContext(), ShowExpenses.class);
                         SharedPreferences.Editor editor=sharedPreferences.edit();
                         editor.putString(Constant.TableName,tables.getTablesName());
                         editor.putFloat(Constant.Credit,tables.getCredit());
                         editor.putFloat(Constant.Expenditure,tables.getExpenditure());
                         editor.putString(Constant.TableMonth,tables.getTableMonth());
                         editor.putFloat(Constant.Balance,tables.getBalance());
                         editor.commit();
                         startActivity(i);
                     } catch (Exception e) {
                         Log.i(Constant.LOGTAG,e.toString());
                     }
                 }

            }
        });
    }
    private void intialize() {
        tablesList = (ListView) findViewById(R.id.TablesList);

        tableList = FirebaseDatabase.getInstance();
        currentUserEmail = encodeEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        if (currentUserEmail != null) {
            try {
                tables = FirebaseDatabase.getInstance().getReference()
                        .child(Constant.UserDetails + "/" + currentUserEmail + "/" + Constant.Tables);
                tableRef = FirebaseDatabase.getInstance().getReference()
                        .child(Constant.UserDetails + "/" + currentUserEmail + "/" + Constant.Tables);
            } catch (Exception e) {
                Log.i(Constant.LOGTAG, e.toString());
            }


        }else{
            Log.i(Constant.LOGTAG,"USerNull");
        }
    }
    private static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

}
