package com.example.autowaiter.Views;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autowaiter.Adapter.OrderAdapter;
import com.example.autowaiter.Models.OrderModel;
import com.example.autowaiter.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Orders extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<OrderModel, OrderAdapter> adapter;
    private RecyclerView recyclerorders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        databaseReference = FirebaseDatabase.getInstance().getReference("Meals");

        initView();
        GetOrders();
    }

    private void GetOrders(){

        adapter=new FirebaseRecyclerAdapter<OrderModel, OrderAdapter>(OrderModel.class,R.layout.order_item,OrderAdapter.class,databaseReference) {
            @Override
            protected void populateViewHolder(OrderAdapter orderAdapter, OrderModel orderModel,final int position) {
                orderAdapter.OrderName.setText(orderModel.getOrderName());
                orderAdapter.TableNamber.setText(""+orderModel.getNumberTable());

                orderAdapter.Delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Query applesQuery = databaseReference.child(adapter.getRef(position).getKey());

                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                    adapter.notifyDataSetChanged();
                                    recyclerorders.setAdapter(adapter);
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
        recyclerorders.setAdapter(adapter);
    }

    private void initView() {
        recyclerorders = (RecyclerView) findViewById(R.id.recyclerorders);
        recyclerorders.setHasFixedSize(true);
        recyclerorders.setLayoutManager(new LinearLayoutManager(this));
    }
}
