package com.example.autowaiter.Views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autowaiter.Adapter.MenuAdapter;
import com.example.autowaiter.Models.MenuModel;
import com.example.autowaiter.Models.OrderModel;
import com.example.autowaiter.Progress_Dialog;
import com.example.autowaiter.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<MenuModel, MenuAdapter> adapter;
    private Progress_Dialog progress_dialog;
    private RecyclerView recyclermenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");

        initView();
        GetMenu();
    }


    private void GetMenu(){


        adapter=new FirebaseRecyclerAdapter<MenuModel, MenuAdapter>(MenuModel.class,R.layout.menu_item,MenuAdapter.class,databaseReference) {
            @Override
            protected void populateViewHolder(MenuAdapter menuAdapter, final MenuModel menuModel, int i) {


                menuAdapter.MenuName.setText(menuModel.getMenuName());

                Picasso.with(MainActivity.this).load(menuModel.getMenuImage()).into(menuAdapter.MenuImage);


                menuAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Create_Meal(menuModel.getMenuName());
                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        recyclermenu.setAdapter(adapter);
    }


    private void Create_Meal(final String Name) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText=new EditText(this);
        editText.setHint("Enter Table Number");
        builder.setView(editText);


        builder.setCancelable(false);
        builder.setPositiveButton("Create Order", null);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog mAlertDialog = builder.create();

        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button Positive = mAlertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                Button Cancel = mAlertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);

                Positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        progress_dialog.pDialog.show();

                        OrderModel menuModel=new OrderModel(Name, Integer.valueOf(editText.getText().toString()));
                        databaseReference = FirebaseDatabase.getInstance().getReference("Meals");
                        databaseReference.child(String.valueOf(System.currentTimeMillis())).setValue(menuModel);
                        mAlertDialog.dismiss();
                        progress_dialog.pDialog.dismiss();
                        progress_dialog.pDialog2.show();



                    }
                });

                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlertDialog.dismiss();

                    }
                });
            }
        });

        mAlertDialog.show();
        mAlertDialog.getWindow().setBackgroundDrawableResource(R.drawable.card_back_without_border);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adminmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.admin) {

            startActivity(new Intent(MainActivity.this, Login.class));
        }


        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        recyclermenu = (RecyclerView) findViewById(R.id.recyclermenu);
        recyclermenu.setHasFixedSize(true);
        recyclermenu.setLayoutManager(new LinearLayoutManager(this));
        progress_dialog=new Progress_Dialog(MainActivity.this);
        progress_dialog.SweetAlertDialog();
        progress_dialog.SweetAlertDialogDone();
    }
}
