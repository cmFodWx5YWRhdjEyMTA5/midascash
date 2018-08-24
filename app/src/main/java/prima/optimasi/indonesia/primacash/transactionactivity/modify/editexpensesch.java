package prima.optimasi.indonesia.primacash.transactionactivity.modify;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.commaedittext;
import prima.optimasi.indonesia.primacash.generator;
import prima.optimasi.indonesia.primacash.objects.expense;

public class editexpensesch extends AppCompatActivity {

    FirebaseFirestore fdb;

    View layoutitems;
    Button save,cancel;


    Double calculate=0.0d,comparer=0.0d,result=0.0d;

    DecimalFormat formatter = new DecimalFormat("###,###,###.00");

    TextView expnote,expto;

    EditText inputvalue;

   LinearLayout accountselect,categoryselect,dateselect;
    TextView selectedaccount,selectedcategory,selectedate,currencytext;

    CircleImageView chosenimage;

    List<expense> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_editsch);

        generator.incdatesys=0;

        data = new ArrayList<>();
        fdb = FirebaseFirestore.getInstance();

        final String[] accdoc = {""};

        final int[] isdone = {4};

        layoutitems = findViewById(R.id.viewpagerexpense);

        categoryselect = layoutitems.findViewById(R.id.linearcattap);
        accountselect = layoutitems.findViewById(R.id.linearacctap);
        dateselect = layoutitems.findViewById(R.id.linearaccdat);

        chosenimage = layoutitems.findViewById(R.id.expcatpic);

        selectedcategory = layoutitems.findViewById(R.id.expcat);
        selectedate = layoutitems.findViewById(R.id.expdat);
        selectedaccount = layoutitems.findViewById(R.id.expacc);

        inputvalue = layoutitems.findViewById(R.id.input_value);

        inputvalue.addTextChangedListener(new commaedittext(inputvalue));

        expto= layoutitems.findViewById(R.id.expto);
        expnote = layoutitems.findViewById(R.id.expnote);

        save = findViewById(R.id.btnsaveexpenseedit);
        cancel = findViewById(R.id.btncancelexpenseedit);


        currencytext = layoutitems.findViewById(R.id.allcurrency);



        categoryselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generator.chosecategory(editexpensesch.this,selectedcategory,chosenimage);
            }
        });

        accountselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generator.choseaccount(editexpensesch.this,selectedaccount,currencytext);
            }
        });

        dateselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generator.chosedate(editexpensesch.this,selectedate);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputvalue.getText().toString().equals("")){
                    Toast.makeText(editexpensesch.this, "Value Required", Toast.LENGTH_SHORT).show();
                }else {
                    Map<String, Object> mapdata = new HashMap<>();
                    mapdata.put("expense_amount",inputvalue.getText().toString().replace(",",""));
                    mapdata.put("expense_account",selectedaccount.getText().toString());
                    mapdata.put("expense_category",selectedcategory.getText().toString());
                    mapdata.put("expense_notes", expnote.getText().toString());
                    mapdata.put("expense_date",selectedate.getText().toString());
                    mapdata.put("expense_datesys",generator.incdatesys);
                    mapdata.put("expense_to",expto.getText().toString());
                    mapdata.put("expense_lastedit", Calendar.getInstance().getTimeInMillis());

                    fdb.collection("expense").document(getIntent().getStringExtra("document"))
                            .set(mapdata, SetOptions.merge());

                    fdb.collection("account")
                            .whereEqualTo("account_name", selectedaccount.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document1 : task.getResult()) {
                                            Log.d("Documentdata", document1.getId() + " => " + document1.getData());
                                            accdoc[0] = document1.getId();
                                            Toast.makeText(editexpensesch.this, "Income Edited", Toast.LENGTH_SHORT).show();
                                            reloaddatasch();
                                            if(generator.adapter!=null){
                                                generator.adapter.notifyDataSetChanged();
                                            }
                                            finish();
                                            generator.incdatesys=0;
                                        }
                                    } else {
                                        Log.d("Documentdata", "Error getting documents: ", task.getException());
                                    }
                                }
                            });


                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DocumentReference docRef = fdb.collection("expense").document(getIntent().getStringExtra("document"));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Documentdata", "DocumentSnapshot data: " + document.getData());
                        expense values = new expense();
                        values.setexpensedoc(document.getId());

                        isdone[0] = Integer.parseInt(document.getData().get("expense_isdone").toString());

                        values.setexpense_account(document.getData().get("expense_account").toString());

                        selectedaccount.setText(document.getData().get("expense_account").toString());

                        values.setexpense_amount(Double.parseDouble(document.getData().get("expense_amount").toString()));

                        comparer = generator.makedouble(document.getData().get("expense_amount").toString());

                        String temp =formatter.format(Double.parseDouble(document.getData().get("expense_amount").toString()));
                        inputvalue.setText(temp);
                        fdb.collection("category")
                                .whereEqualTo("category_name", document.getData().get("expense_category").toString())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document1 : task.getResult()) {
                                                Log.d("Documentdata", document1.getId() + " => " + document.getData());
                                                Drawable resImg = editexpensesch.this.getResources().getDrawable(generator.images[Integer.parseInt(document1.getData().get("category_image").toString())-1]);
                                                chosenimage.setImageDrawable(resImg);
                                                values.setexpense_category(document.getData().get("expense_category").toString());
                                            }
                                        } else {
                                            Log.d("Documentdata", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                        selectedcategory.setText(document.getData().get("expense_category").toString());

                        values.setexpense_date(document.getData().get("expense_date").toString());
                        selectedate.setText(document.getData().get("expense_date").toString());

                        values.setexpense_type("K");

                        values.setexpense_notes(document.getData().get("expense_notes").toString());

                        expnote.setText(document.getData().get("expense_notes").toString());

                        values.setexpense_to(document.getData().get("expense_to").toString());

                        expto.setText(document.getData().get("expense_to").toString());

                    } else {
                        Log.d("Documentdata", "No such document");
                    }
                } else {
                    Log.d("Documentdata", "get failed with ", task.getException());
                }
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            this.finish();
        }
        return true;
    }

    public void reloaddatasch(){
        generator.showdataexpensesch.clear();
        generator.showadapterexpensesch.notifyDataSetChanged();
        fdb.collection("expense")
                .whereEqualTo("expense_isdated", "1")
                .orderBy("expense_datesys", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                expense values = new expense();
                                values.setexpensedoc(document.getId());
                                values.setexpense_account(document.getData().get("expense_account").toString());
                                values.setexpense_amount(Double.parseDouble(document.getData().get("expense_amount").toString()));
                                values.setexpense_category(document.getData().get("expense_category").toString());
                                values.setexpense_date(document.getData().get("expense_date").toString());
                                values.setexpense_type("K");
                                values.setexpense_notes(document.getData().get("expense_notes").toString());
                                values.setexpense_to(document.getData().get("expense_to").toString());
                                fdb.collection("category")
                                        .whereEqualTo("category_name", document.getData().get("expense_category").toString())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                                        Log.e("category datas", document1.getId() + " => " + document1.getData());
                                                        values.setexpense_image(Integer.parseInt(document1.getData().get("category_image").toString()));
                                                        generator.showdataexpensesch.add(values);

                                                    }
                                                    Collections.sort(generator.showdataexpensesch);
                                                    Collections.reverse(generator.showdataexpensesch);
                                                    generator.showadapterexpensesch.notifyDataSetChanged();

                                                } else {
                                                    Log.d("data", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.w("data", "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
