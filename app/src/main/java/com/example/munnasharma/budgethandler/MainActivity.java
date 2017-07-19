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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       sharedPreferences=getSharedPreferences(Constant.MYPref, Context.MODE_PRIVATE);

        intialize();
        pr=ProgressDialog.show(MainActivity.this,"Wait","",true);
        PopulateList();
    }

    private void PopulateList() {
        mAdaptor = new FirebaseListAdapter<Tables>(this, Tables.class, R.layout.tables_item, tables) {
            @Override
            protected void populateView(final View view, Tables tables, final int position) {

                TextView tableName=(TextView)view.findViewById(R.id.TableName);
                String taleName=tables.getTableMonth();
                // taleName = taleName.substring(0, 1).toUpperCase() + taleName.substring(1);
                tableName.setText(taleName);

                tableRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.i("Error","Childd chnaged");
                        PopulateList();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.i("Error","Childd remoed");
                        PopulateList();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.i("Error","Childd moved");
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
                         editor.putFloat(Constant.Balance,tables.getBalance());
                         editor.commit();
                         startActivity(i);
                     } catch (Exception e) {
                         Log.i("Error",e.toString());
                     }
                 }

            }
        });
    }
    private void intialize(){
        tablesList=(ListView)findViewById(R.id.TablesList);

        tableList=FirebaseDatabase.getInstance();
        tables=tableList.getReference().child(Constant.Tables);
        tableRef=FirebaseDatabase.getInstance().getReference().child(Constant.Tables);


    }
}
