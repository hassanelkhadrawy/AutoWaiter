package com.example.autowaiter.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autowaiter.Adapter.MenuAdapterAdmin;
import com.example.autowaiter.CheckInternet;
import com.example.autowaiter.Models.MenuModel;
import com.example.autowaiter.Progress_Dialog;
import com.example.autowaiter.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Dashboard extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<MenuModel, MenuAdapterAdmin> adapter;
    private RecyclerView recyclermenuDash;
    Uri saveuri;
    ImageView ImageMeal;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    Progress_Dialog progress_dialog;
    private EditText email;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
        storageReference = FirebaseStorage.getInstance().getReference("images/");
        firebaseAuth = FirebaseAuth.getInstance();


        initView();
        GetMenu();
    }

    private void GetMenu() {


        adapter = new FirebaseRecyclerAdapter<MenuModel, MenuAdapterAdmin>(MenuModel.class, R.layout.menu_item_admin, MenuAdapterAdmin.class, databaseReference) {
            @Override
            protected void populateViewHolder(MenuAdapterAdmin menuAdapterAdmin, MenuModel menuModel, final int position) {

                menuAdapterAdmin.MenuName.setText(menuModel.getMenuName());

                Picasso.with(Dashboard.this).load(menuModel.getMenuImage()).into(menuAdapterAdmin.MenuImage);
                menuAdapterAdmin.Delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Query applesQuery = databaseReference.child(adapter.getRef(position).getKey());

                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                    adapter.notifyDataSetChanged();
                                    recyclermenuDash.setAdapter(adapter);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                    }
                });


            }
        };
        adapter.notifyDataSetChanged();
        recyclermenuDash.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {

            Create_Meal();
        } else if (id == R.id.orderslist) {

            startActivity(new Intent(this, Orders.class));
        } else if (id == R.id.add_admin) {
            Aleart();
        }


        return super.onOptionsItemSelected(item);
    }


    private void uploadData(final String Name) {

        Random rand = new Random();
        String txt = String.valueOf(rand.nextInt(10000 - 0));
//        databaseReference = FirebaseDatabase.getInstance().getReference("Orders").child(txt);

        if (saveuri != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading ...");
            dialog.show();

            String imgName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imgName);
            imageFolder.putFile(saveuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Toast.makeText(Dashboard.this, "Done", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    ArrayList<String> users = new ArrayList<String>();
                                    MenuModel menuModel = new MenuModel();

                                    if (TextUtils.isEmpty(uri.toString())) {

                                        menuModel = new MenuModel(Name, uri.toString());
                                    } else {
                                        menuModel = new MenuModel(Name, uri.toString());


                                    }
                                    databaseReference.child(String.valueOf(System.currentTimeMillis())).setValue(menuModel);
                                    Toast.makeText(Dashboard.this, "New item was added", Toast.LENGTH_SHORT).show();

                                    //hide keyboard
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(Dashboard.this, "hhhhhh" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int) (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            dialog.setMessage("Upload      " + progress + "    %  ");
                        }
                    });

        }
    }


    private void Create_Meal() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.addmeal, null);

        Button UpLoad_Image = view.findViewById(R.id.upload_meal);
        ImageMeal = view.findViewById(R.id.image_meal);
        final TextView Titel_Meal = view.findViewById(R.id.meal_tittle);

        builder.setView(view);


        builder.setCancelable(false);
        builder.setPositiveButton("Share", null);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog mAlertDialog = builder.create();

        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button Positive = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button Cancel = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                Positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        uploadData(Titel_Meal.getText().toString());
                        mAlertDialog.dismiss();

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


        UpLoad_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImage();
            }
        });


    }


    private void ChooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK && requestCode == 1) {

            saveuri = data.getData();
            ImageMeal.setImageURI(saveuri);


        }
    }


    private void Aleart() {
        Context context;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.add_admin, null);
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        login = (Button) view.findViewById(R.id.login);
        builder.setView(view);
        final AlertDialog mAlertDialog = builder.create();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (email.getText().toString().length() == 0) {
                    email.setError("name required");
                    email.requestFocus();
                } else if (password.getText().toString().length() == 0) {
                    password.setError("password required");
                    password.requestFocus();

                } else if (CheckInternet.isConnectToInternet(getBaseContext())) {

                    AddAdmin(email.getText().toString(), password.getText().toString());

                    mAlertDialog.dismiss();

                } else {
                    mAlertDialog.dismiss();
                    Toast.makeText(Dashboard.this, "Check your internet Connection !!!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        mAlertDialog.show();
    }

    private void AddAdmin(String Email, String Password) {






        firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // FirebaseUser user = firebaseAuth.getCurrentUser();
                            progress_dialog.pDialog2.show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Dashboard.this, "Faild", Toast.LENGTH_SHORT).show();

                        }

                    }
                });



    }


    private void initView() {
        recyclermenuDash = (RecyclerView) findViewById(R.id.recyclermenu_dash);
        recyclermenuDash.setHasFixedSize(true);
        recyclermenuDash.setLayoutManager(new LinearLayoutManager(this));
        progress_dialog = new Progress_Dialog(this);
        progress_dialog.SweetAlertDialogDone();

    }
}
