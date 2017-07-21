package com.example.munnasharma.budgethandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import classes.Constant;
import classes.ExpensesOrAddition;
import classes.Tables;
import classes.categoryDetails;

public class ShowExpenses extends AppCompatActivity {

    private ListView expenseList;
    private TextView creditText,BalanceText;
    private FirebaseDatabase mfirebsse;
    private DatabaseReference tablesData,expenseRef;
    private FirebaseListAdapter mAdaptor;
    private FirebaseAuth mAuth;
    private ProgressDialog pr;
    private String tableName;
    private float credit;
    private float balance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
     private FloatingActionButton fab;
    private Intent i;
    private String timeStamp;
    private Float amount;
    private String CategoryName,CategoryImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expenses);

        sharedPreferences=getSharedPreferences(Constant.MYPref, Context.MODE_PRIVATE);
        tableName=sharedPreferences.getString(Constant.TableName,null);
        credit=sharedPreferences.getFloat(Constant.Credit,00);
        balance=sharedPreferences.getFloat(Constant.Balance,00);

       Initialize();
        pr=ProgressDialog.show(ShowExpenses.this,"Wait","",true);
        PopulateList();
    }
    private void PopulateList() {
        mAdaptor = new FirebaseListAdapter<ExpensesOrAddition>(this, ExpensesOrAddition.class, R.layout.expense_list_item, tablesData) {
            @Override
            protected void populateView(final View view, ExpensesOrAddition expensesOrAddition, final int position) {

                TextView categoryName=(TextView)view.findViewById(R.id.Category);
                ImageView categoryImg=(ImageView)view.findViewById(R.id.CategoryImage);
                TextView amoount=(TextView)view.findViewById(R.id.AmountExpended);
                TextView timeStmp=(TextView)view.findViewById(R.id.TimeStamp);

                timeStamp=expensesOrAddition.getTimeStamp();
                amount=expensesOrAddition.getAmount();
                CategoryImageUrl=expensesOrAddition.getCategoryImageUrl();
                CategoryName=expensesOrAddition.getCategoryName();

                Log.i("Errro",timeStamp+" "+amount+ " "+CategoryName+" "+CategoryImageUrl);

                try {
                    if (CategoryName != null) {
                        categoryName.setText(CategoryName);
                    }
                    if (amount != null) {
                        amoount.setText(String.valueOf(amount));
                    }
                    if (timeStamp != null) {
                        timeStmp.setText(timeStamp);
                    }
                }catch (Exception e){

                    Log.i("Errro",e.toString());
                }
                expenseRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Log.i("Errrro","Added");
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

                        Toast.makeText(ShowExpenses.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        expenseList.setAdapter(mAdaptor);
        pr.dismiss();
        expenseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tableLoc=mAdaptor.getRef(position).toString();
                if(tableLoc!=null){


                }

            }
        });
    }

    private void Initialize(){
        expenseList=(ListView)findViewById(R.id.expenseList);
        creditText=(TextView) findViewById(R.id.CreditValue);
        BalanceText=(TextView) findViewById(R.id.BalanceText);
        fab=(FloatingActionButton)findViewById(R.id.FLoatingButtonAdd);

        creditText.setText(String.valueOf(credit));
        BalanceText.setText(String.valueOf(balance));

        mAuth=FirebaseAuth.getInstance();
        mfirebsse=FirebaseDatabase.getInstance();
        tablesData=FirebaseDatabase.getInstance().getReference(tableName);
        expenseRef=FirebaseDatabase.getInstance().getReference().child(tableName);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(getApplicationContext(),DataEntry.class);
                startActivity(i);
            }
        });
    }

    private static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
}
