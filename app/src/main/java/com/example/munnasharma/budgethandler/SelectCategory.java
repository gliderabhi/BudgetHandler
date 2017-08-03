package com.example.munnasharma.budgethandler;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import classes.Constant;
import classes.Tables;
import classes.categoryDetails;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class SelectCategory extends AppCompatActivity {

    private ListView CategoryList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference categoryList,tableRef;
    private FirebaseListAdapter mAdaptor;
    private StorageReference mStorage;
    private TextView waitText;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int multiFac=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
        waitText=(TextView)findViewById(R.id.WaitTextCat);
        try {
            mStorage = FirebaseStorage.getInstance().getReference();
            categoryList = FirebaseDatabase.getInstance().getReference().child(Constant.Category);
            tableRef = FirebaseDatabase.getInstance().getReference().child(Constant.Category);
            CategoryList = (ListView) findViewById(R.id.CategoryList);
            PopulateList();
        }catch(Exception e){
            Log.i(Constant.LOGTAG,e.toString());
        }
    }
    public void PopulateList(){
        mAdaptor = new FirebaseListAdapter<categoryDetails>(this, categoryDetails.class, R.layout.category_item, categoryList) {
            @Override
            protected void populateView(final View view, categoryDetails categoryDetails, final int position) {

               try {
                   TextView categoryName = (TextView) view.findViewById(R.id.CategoryName);
                   String catName = categoryDetails.getCategoryName();
                   categoryName.setText(catName);
                   ImageView categoryImage = (ImageView) view.findViewById(R.id.CategoryItemImage);
                   String imageUrl = categoryDetails.getImageurl();

                   switch(categoryDetails.getMultiFac()){
                       case "1": multiFac=1;break;
                       case "2": multiFac=-1;break;
                   }
                   mStorage = mStorage.child(imageUrl);
                   Glide.with(view.getContext())
                           .using(new FirebaseImageLoader())
                           .load(mStorage)
                           .bitmapTransform(new CropCircleTransformation(view.getContext()))
                           .into(categoryImage);
               }catch(Exception e){
                   Log.i(Constant.LOGTAG,e.toString());
               }
                tableRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.i(Constant.LOGTAG,"Childd chnaged");
                        PopulateList();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.i(Constant.LOGTAG,"Childd remoed");
                        PopulateList();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.i(Constant.LOGTAG,"Childd moved");
                        PopulateList();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(SelectCategory.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        waitText.setVisibility(View.GONE);
        CategoryList.setAdapter(mAdaptor);
        CategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoryDetails categoryDetails= (categoryDetails) mAdaptor.getItem(position);
                sharedPreferences=getApplicationContext().getSharedPreferences(Constant.MYPref,MODE_PRIVATE);
                editor=sharedPreferences.edit();
                editor.putString(Constant.Category,categoryDetails.getCategoryName());
                editor.putString(Constant.CategoryImage,categoryDetails.getImageurl());
                editor.putInt(Constant.CategoryMultiply,multiFac);
                editor.commit();
                Intent i =new Intent(SelectCategory.this,DataEntry.class);
                startActivity(i);
            }
        });
    }
}
