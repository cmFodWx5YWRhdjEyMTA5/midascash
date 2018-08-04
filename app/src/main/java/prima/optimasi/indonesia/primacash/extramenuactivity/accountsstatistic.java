package prima.optimasi.indonesia.primacash.extramenuactivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.objects.incomeexpensetransfer;
import prima.optimasi.indonesia.primacash.recycleview.adapterlisttransactionaccount;

public class accountsstatistic extends AppCompatActivity {

    DecimalFormat formatter = new DecimalFormat("###,###,###.00");

    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    FirebaseFirestore db;
    LineChart mChart;

    RecyclerView recycler;

    adapterlisttransactionaccount adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_transaction);

        recycler = findViewById(R.id.lvalllist);

        Bundle bundle = getIntent().getExtras();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        List<incomeexpensetransfer> dataiet = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        db.collection("income")
                .whereEqualTo("income_account",bundle.getString("account_name"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                incomeexpensetransfer item = new incomeexpensetransfer();
                                item.setIncomedoc(document.getId());
                                item.setIncome_account(document.getData().get("income_account").toString());
                                item.setIncome_amount(Double.parseDouble(document.getData().get("income_amount").toString()));
                                item.setIncome_category(document.getData().get("income_category").toString());
                                item.setIncome_date(document.getData().get("income_date").toString());
                                item.setDatedata(document.getData().get("income_date").toString());
                                item.setIncome_type("D");
                                item.setIncome_notes(document.getData().get("income_notes").toString());
                                item.setIncome_from(document.getData().get("income_from").toString());

                                final int[] count = {0};

                                db.collection("category")
                                        .whereEqualTo("category_name", document.getData().get("income_category").toString())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                if (task1.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                                        Log.e("category datas", document1.getId() + " => " + document1.getData());
                                                        item.setIncome_image(Integer.parseInt(document1.getData().get("category_image").toString()));


                                                        count[0]++;
                                                    }
                                                    if(count[0]==0){
                                                        item.setIncome_image(37);
                                                    }
                                                    dataiet.add(item);
                                                    Collections.sort(dataiet);
                                                    Collections.reverse(dataiet);
                                                    if(adapter!=null){
                                                        adapter.notifyDataSetChanged();
                                                    }


                                                } else {
                                                    Log.d("data", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });


                            }
                            //expense

                            db.collection("expense")
                                    .whereEqualTo("expense_account",bundle.getString("account_name"))
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    incomeexpensetransfer item2 = new incomeexpensetransfer();
                                                    item2.setExpensedoc(document.getId());
                                                    item2.setExpense_account(document.getData().get("expense_account").toString());
                                                    item2.setExpense_amount(Double.parseDouble(document.getData().get("expense_amount").toString()));
                                                    item2.setExpense_category(document.getData().get("expense_category").toString());
                                                    item2.setDatedata(document.getData().get("expense_date").toString());
                                                    item2.setExpense_date(document.getData().get("expense_date").toString());
                                                    item2.setExpense_type("K");
                                                    item2.setExpense_notes(document.getData().get("expense_notes").toString());
                                                    item2.setExpense_to(document.getData().get("expense_to").toString());

                                                    final int[] count = {0};

                                                    db.collection("category")
                                                            .whereEqualTo("category_name", document.getData().get("expense_category").toString())
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                                    if (task.isSuccessful()) {
                                                                        for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                                                            Log.e("category datas", document1.getId() + " => " + document1.getData());
                                                                            item2.setExpense_image(Integer.parseInt(document1.getData().get("category_image").toString()));

                                                                            count[0]++;
                                                                        }
                                                                        if(count[0]==0){
                                                                            item2.setExpense_image(37);
                                                                        }
                                                                        dataiet.add(item2);
                                                                        Collections.sort(dataiet);
                                                                        Collections.reverse(dataiet);

                                                                        if(adapter!=null){
                                                                            adapter.notifyDataSetChanged();
                                                                        }
                                                                    } else {
                                                                        Log.d("data", "Error getting documents: ", task.getException());
                                                                    }
                                                                }
                                                            });

                                                }

                                                //transfer

                                                db.collection("transfer")
                                                        .whereEqualTo("transfer_src",bundle.getString("account_name"))
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    for (DocumentSnapshot document : task.getResult()) {
                                                                        incomeexpensetransfer data = new incomeexpensetransfer();
                                                                        data.setTransferdoc(document.getId());
                                                                        data.setTransfer_amount(Double.parseDouble(document.getData().get("transfer_amount").toString()));
                                                                        data.setTransfer_rate(Double.parseDouble(document.getData().get("transfer_rate").toString()));
                                                                        data.setTransfer_date(document.getData().get("transfer_date").toString());
                                                                        data.setDatedata(document.getData().get("transfer_date").toString());
                                                                        data.setTransfer_notes(document.getData().get("transfer_notes").toString());
                                                                        data.setTransfer_src(document.getData().get("transfer_src").toString());
                                                                        data.setTransfer_dest(document.getData().get("transfer_dest").toString());

                                                                        dataiet.add(data);
                                                                        Collections.sort(dataiet);
                                                                        Collections.reverse(dataiet);

                                                                        if(adapter!=null){
                                                                            adapter.notifyDataSetChanged();
                                                                        }
                                                                    }
                                                                    db.collection("transfer")
                                                                            .whereEqualTo("transfer_dest",bundle.getString("account_name"))
                                                                            .get()
                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        for (DocumentSnapshot document : task.getResult()) {
                                                                                            incomeexpensetransfer data = new incomeexpensetransfer();
                                                                                            data.setTransferdoc(document.getId());
                                                                                            data.setTransfer_amount(Double.parseDouble(document.getData().get("transfer_amount").toString()));
                                                                                            data.setTransfer_rate(Double.parseDouble(document.getData().get("transfer_rate").toString()));
                                                                                            data.setTransfer_date(document.getData().get("transfer_date").toString());
                                                                                            data.setDatedata(document.getData().get("transfer_date").toString());
                                                                                            data.setTransfer_notes(document.getData().get("transfer_notes").toString());
                                                                                            data.setTransfer_src(document.getData().get("transfer_src").toString());
                                                                                            data.setTransfer_dest(document.getData().get("transfer_dest").toString());

                                                                                            dataiet.add(data);
                                                                                            Collections.sort(dataiet);
                                                                                            Collections.reverse(dataiet);

                                                                                            if(adapter!=null){
                                                                                                adapter.notifyDataSetChanged();
                                                                                            }
                                                                                        }

                                                                                    } else {
                                                                                        Log.w("Get account error", "Error getting documents.", task.getException());
                                                                                    }
                                                                                }
                                                                            });

                                                                } else {
                                                                    Log.w("Get account error", "Error getting documents.", task.getException());
                                                                }
                                                            }
                                                        });

                                                //transfer

                                            } else {
                                                Log.w("Get account error", "Error getting documents.", task.getException());
                                            }
                                        }
                                    });

                            //expense

                            adapter = new adapterlisttransactionaccount(accountsstatistic.this,dataiet);
                            adapter.notifyDataSetChanged();
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(accountsstatistic.this, 1);
                            recycler.setLayoutManager(mLayoutManager);
                            recycler.setItemAnimator(new DefaultItemAnimator());
                            recycler.setAdapter(adapter);

                        } else {
                            Log.w("Get account error", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            finish();
        }
        return  true;
    }
}
