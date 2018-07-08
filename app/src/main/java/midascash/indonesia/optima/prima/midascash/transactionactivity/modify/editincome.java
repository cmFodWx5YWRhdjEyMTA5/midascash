package midascash.indonesia.optima.prima.midascash.transactionactivity.modify;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.generator;
import midascash.indonesia.optima.prima.midascash.objects.income;

public class editincome extends AppCompatActivity {
    FirebaseFirestore fdb;

    View layoutitems;
    Button save,cancel;


    Double calculate=0.0d,comparer=0.0d,result=0.0d;

    DecimalFormat formatter = new DecimalFormat("###,###,###.00");

    TextView incnote,incfrom;

    EditText inputvalue;

    LinearLayout accountselect,categoryselect,dateselect;
    TextView selectedaccount,selectedcategory,selectedate,currencytext;

    CircleImageView chosenimage;

    List<income> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_edit);

        generator.incdatesys=0;

        data = new ArrayList<>();
        fdb = FirebaseFirestore.getInstance();

        final String[] accdoc = {""};

        final int[] isdone = {4};

        layoutitems = findViewById(R.id.viewpagerexpense);

        categoryselect = layoutitems.findViewById(R.id.inccattap);
        accountselect = layoutitems.findViewById(R.id.incacctap);
        dateselect = layoutitems.findViewById(R.id.incdatetap);

        chosenimage = layoutitems.findViewById(R.id.inccatpic);

        selectedcategory = layoutitems.findViewById(R.id.inccatname);
        selectedate = layoutitems.findViewById(R.id.incdateselect);
        selectedaccount = layoutitems.findViewById(R.id.incacctxt);

        inputvalue = layoutitems.findViewById(R.id.input_value);

        inputvalue.addTextChangedListener(new com.fake.shopee.shopeefake.formula.commaedittext(inputvalue));

        incfrom = layoutitems.findViewById(R.id.incfromtxt);
        incnote = layoutitems.findViewById(R.id.incnotetxt);

        save = findViewById(R.id.btnsaveincomeedit);
        cancel = findViewById(R.id.btncancelincomeedit);


        currencytext = layoutitems.findViewById(R.id.allcurrency);



        categoryselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generator.chosecategory(editincome.this,selectedcategory,chosenimage);
            }
        });

        accountselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generator.choseaccount(editincome.this,selectedaccount,currencytext);
            }
        });

        dateselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generator.chosedate(editincome.this,selectedate);
            }
        });




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputvalue.getText().toString().equals("")){
                    Toast.makeText(editincome.this, "Value Required", Toast.LENGTH_SHORT).show();
                }else {


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
                                            calculate = generator.makedouble(document1.getData().get("account_balance_current").toString());

                                            Date date = Calendar.getInstance().getTime();

                                            Date today = new Date();
                                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                            Date chosendated=null;
                                            try {
                                                chosendated = format.parse(selectedate.getText().toString());
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            String temp="1";

                                            if(isdone[0]==1){

                                                if(chosendated.after(date)) {
                                                    temp = "0";
                                                    if(generator.makedouble(inputvalue.getText().toString().replace(",",""))>comparer){
                                                        Map<String, Object> data = new HashMap<>();
                                                        result=generator.makedouble(inputvalue.getText().toString().replace(",",""))-comparer;
                                                        data.put("account_balance_current", String.valueOf(calculate-result+comparer) );

                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());



                                                    }
                                                    else if(generator.makedouble(inputvalue.getText().toString().replace(",",""))<comparer){
                                                        Map<String, Object> data = new HashMap<>();
                                                        result=comparer-generator.makedouble(inputvalue.getText().toString().replace(",",""));
                                                        data.put("account_balance_current", String.valueOf(calculate-result+comparer));

                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());


                                                    }
                                                    else {
                                                        Map<String, Object> data = new HashMap<>();
                                                        data.put("account_balance_current", String.valueOf(calculate-generator.makedouble(inputvalue.getText().toString().replace(",",""))));
                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());
                                                    }
                                                }
                                                else {
                                                    temp="1";
                                                    if(generator.makedouble(inputvalue.getText().toString().replace(",",""))>comparer){
                                                        Map<String, Object> data = new HashMap<>();
                                                        result=generator.makedouble(inputvalue.getText().toString().replace(",",""))-comparer;
                                                        Log.e("balance current", String.valueOf(calculate+result) );
                                                        data.put("account_balance_current",String.valueOf(calculate+result)  );

                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());
                                                    }
                                                    else if(generator.makedouble(inputvalue.getText().toString().replace(",",""))<comparer){
                                                        Map<String, Object> data = new HashMap<>();
                                                        result=comparer-generator.makedouble(inputvalue.getText().toString().replace(",",""));
                                                        data.put("account_balance_current", String.valueOf(calculate-result) );

                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());
                                                    }
                                                    else {

                                                    }
                                                }
                                                isdone[0]=4;
                                            }
                                            else {
                                                temp = "0";

                                                if(chosendated.after(date)) {

                                                }else {
                                                    temp = "1";
                                                    if(generator.makedouble(inputvalue.getText().toString().replace(",",""))>comparer){
                                                        Map<String, Object> data = new HashMap<>();
                                                        result=generator.makedouble(inputvalue.getText().toString().replace(",",""))-comparer;
                                                        data.put("account_balance_current", String.valueOf(calculate+result+comparer) );

                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());



                                                    }
                                                    else if(generator.makedouble(inputvalue.getText().toString().replace(",",""))<comparer){
                                                        Map<String, Object> data = new HashMap<>();
                                                        result=comparer-generator.makedouble(inputvalue.getText().toString().replace(",",""));
                                                        data.put("account_balance_current", String.valueOf(calculate+result));

                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());


                                                    }
                                                    else {
                                                        Map<String, Object> data = new HashMap<>();
                                                        data.put("account_balance_current", String.valueOf(calculate+comparer));
                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());
                                                    }
                                                }
                                            }

                                            Toast.makeText(editincome.this, "Income Edited", Toast.LENGTH_SHORT).show();
                                            reloaddata();
                                            if(generator.adapter!=null){
                                                generator.adapter.notifyDataSetChanged();
                                            }

                                            Map<String, Object> mapdata = new HashMap<>();
                                            mapdata.put("income_amount",inputvalue.getText().toString().replace(",",""));
                                            mapdata.put("income_account",selectedaccount.getText().toString());
                                            mapdata.put("income_category",selectedcategory.getText().toString());
                                            mapdata.put("income_notes", incnote.getText().toString());
                                            mapdata.put("income_isdone", temp);
                                            mapdata.put("income_date",selectedate.getText().toString());
                                            mapdata.put("income_datesys",generator.incdatesys);
                                            mapdata.put("income_from",incfrom.getText().toString());
                                            mapdata.put("income_lastedit", Calendar.getInstance().getTimeInMillis());

                                            fdb.collection("income").document(getIntent().getStringExtra("document"))
                                                    .set(mapdata, SetOptions.merge());

                                            generator.incdatesys=0;
                                            finish();
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



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DocumentReference docRef = fdb.collection("income").document(getIntent().getStringExtra("document"));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Documentdata", "DocumentSnapshot data: " + document.getData());
                        income values = new income();
                        values.setIncomedoc(document.getId());

                        isdone[0] = Integer.parseInt(document.getData().get("income_isdone").toString());

                        values.setIncome_account(document.getData().get("income_account").toString());

                        selectedaccount.setText(document.getData().get("income_account").toString());

                        values.setIncome_amount(Double.parseDouble(document.getData().get("income_amount").toString()));

                        comparer = generator.makedouble(document.getData().get("income_amount").toString());

                        String temp =formatter.format(Double.parseDouble(document.getData().get("income_amount").toString()));
                        inputvalue.setText(temp);
                        fdb.collection("category")
                                .whereEqualTo("category_name", document.getData().get("income_category").toString())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document1 : task.getResult()) {
                                                Log.d("Documentdata", document1.getId() + " => " + document.getData());
                                                Drawable resImg = editincome.this.getResources().getDrawable(generator.images[Integer.parseInt(document1.getData().get("category_image").toString())-1]);
                                                chosenimage.setImageDrawable(resImg);
                                                values.setIncome_category(document.getData().get("income_category").toString());
                                            }
                                        } else {
                                            Log.d("Documentdata", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                        
                        selectedcategory.setText(document.getData().get("income_category").toString());

                        values.setIncome_date(document.getData().get("income_date").toString());
                        selectedate.setText(document.getData().get("income_date").toString());

                        values.setIncome_type("D");

                        values.setIncome_notes(document.getData().get("income_notes").toString());

                        incnote.setText(document.getData().get("income_notes").toString());

                        values.setIncome_from(document.getData().get("income_from").toString());

                        incfrom.setText(document.getData().get("income_from").toString());

                    } else {
                        Log.d("Documentdata", "No such document");
                    }
                } else {
                    Log.d("Documentdata", "get failed with ", task.getException());
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
            this.finish();
        }
        return true;
    }

    public void reloaddata(){
        fdb.collection("income")
                .whereEqualTo("income_isdated", "0")
                .orderBy("income_datesys", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            generator.showdataincome.clear();
                            if(generator.showadapterincome!=null){
                                generator.showadapterincome.notifyDataSetChanged();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                income values = new income();
                                values.setIncomedoc(document.getId());
                                values.setIncome_account(document.getData().get("income_account").toString());
                                values.setIncome_amount(Double.parseDouble(document.getData().get("income_amount").toString()));
                                values.setIncome_category(document.getData().get("income_category").toString());
                                values.setIncome_date(document.getData().get("income_date").toString());
                                values.setIncome_type("D");
                                values.setIncome_notes(document.getData().get("income_notes").toString());
                                values.setIncome_from(document.getData().get("income_from").toString());
                                fdb.collection("category")
                                        .whereEqualTo("category_name", document.getData().get("income_category").toString())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                                        Log.e("category datas", document1.getId() + " => " + document1.getData());
                                                        values.setIncome_image(Integer.parseInt(document1.getData().get("category_image").toString()));
                                                        generator.showdataincome.add(values);
                                                    }
                                                    generator.showadapterincome.notifyDataSetChanged();
                                                    Collections.sort(generator.showdataincome);
                                                    Collections.reverse(generator.showdataincome);
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
