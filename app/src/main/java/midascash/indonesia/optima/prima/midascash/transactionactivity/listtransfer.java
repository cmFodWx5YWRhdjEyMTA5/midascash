package midascash.indonesia.optima.prima.midascash.transactionactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.text.CollationElementIterator;
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

import midascash.indonesia.optima.prima.midascash.MainActivity;
import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.formula.calculatordialog;
import midascash.indonesia.optima.prima.midascash.generator;
import midascash.indonesia.optima.prima.midascash.objects.income;
import midascash.indonesia.optima.prima.midascash.objects.transfer;
import midascash.indonesia.optima.prima.midascash.recycleview.adapterviewtransferlist;

public class listtransfer extends AppCompatActivity{

    RecyclerView recycler;

    adapterviewtransferlist adaptertransfer;

    List<transfer> datatransfer;

    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        db = FirebaseFirestore.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recycler = findViewById(R.id.recyclertransfer);
        datatransfer = new ArrayList<>();

        db.collection("transfer")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document1 : task.getResult()) {
                                transfer data = new transfer();
                                data.setTransferdoc(document1.getId());
                                data.setTransfer_amount(Double.parseDouble(document1.getData().get("transfer_amount").toString()));
                                data.setTransfer_rate(Double.parseDouble(document1.getData().get("transfer_rate").toString()));
                                data.setTransfer_date(document1.getData().get("transfer_date").toString());
                                data.setTransfer_notes(document1.getData().get("transfer_notes").toString());
                                data.setTransfer_src(document1.getData().get("transfer_src").toString());
                                data.setTransfer_dest(document1.getData().get("transfer_dest").toString());


                                datatransfer.add(data);
                                Collections.sort(datatransfer);
                                Collections.reverse(datatransfer);
                            }

                            adaptertransfer = new adapterviewtransferlist(listtransfer.this,datatransfer);

                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(listtransfer.this, 1);
                            recycler.setLayoutManager(mLayoutManager);
                            recycler.setItemAnimator(new DefaultItemAnimator());
                            recycler.setAdapter(adaptertransfer);
                        } else {
                            Log.d("data", "Error getting documents: ", task.getException());
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

    public class adapterviewtransferlist extends RecyclerView.Adapter<adapterviewtransferlist.MyViewHolder> {

        DecimalFormat formatter= new DecimalFormat("###,###,###.00");
        Context contexts;
        int times=0;
        FirebaseFirestore db;
        int locationimage=0;
        int totalcircle=0;

        private List<transfer> transferlis;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView trfsrc,trfdest,trfdate,trfvalue,trfrate,trfnotes;
            public LinearLayout linear;

            public MyViewHolder(View view) {
                super(view);
                trfsrc = view.findViewById(R.id.trfsrc);
                trfdest = view.findViewById(R.id.trfdest);
                trfdate= view.findViewById(R.id.trfdate);
                trfdate = view.findViewById(R.id.trfdate);
                trfnotes = view.findViewById(R.id.trfnotes);
                trfvalue = view.findViewById(R.id.trfvalue);
                trfrate = view.findViewById(R.id.trfrate);
                linear = view.findViewById(R.id.trflinear);
            }
        }

        public adapterviewtransferlist(Context context, List<transfer> lis) {
            contexts=context;
            transferlis=lis;
            db = FirebaseFirestore.getInstance();
        }

        @Override
        public adapterviewtransferlist.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_layout_recycler_transfer, parent, false);

            return new adapterviewtransferlist.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final adapterviewtransferlist.MyViewHolder holder, int position) {
            holder.trfrate.setText(formatter.format(transferlis.get(position).getTransfer_rate()));
            holder.trfvalue.setText(formatter.format(transferlis.get(position).getTransfer_amount()));
            holder.trfdate.setText(transferlis.get(position).getTransfer_date());
            holder.trfnotes.setText(transferlis.get(position).getTransfer_notes());
            holder.trfsrc.setText(transferlis.get(position).getTransfer_src());
            holder.trfdest.setText(transferlis.get(position).getTransfer_dest());

            holder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(contexts, holder.linear);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.menu_default);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edititem:

                                    TextView chosenacc,currchosen,currdef,chosendate,allcurrencyselected;
                                    Spinner choseacc;
                                    EditText inputrate,trfvalue,notesdata;


                                    ArrayList<String> account = new ArrayList<>();


                                    List<ExtendedCurrency> currencies = ExtendedCurrency.getAllCurrencies(); //List of all currencies


                                    ExtendedCurrency[] currencieses = ExtendedCurrency.CURRENCIES; //Array of all currencies

                                    //     for (int i=0;i<currencieses.length;i++){
                                    //     Log.e("Currency List", "Nama" + currencieses[i].getName() );
                                    //      Log.e("Currency List", "Symbol" + currencieses[i].getSymbol() );
                                    //       Log.e("Currency List", "Code" + currencieses[i].getCode() );
                                    //   }



                                    LayoutInflater inflater = LayoutInflater.from(listtransfer.this);

                                    LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.layout_transactionstransfer,null);

                                    chosenacc = layout.findViewById(R.id.chosenacc);
                                    choseacc = layout.findViewById(R.id.choseacc);
                                    currchosen = layout.findViewById(R.id.chosencurr);
                                    currdef = layout.findViewById(R.id.currdef);

                                    chosendate = layout.findViewById(R.id.trfdateselect);

                                    notesdata = layout.findViewById(R.id.trfnotes);

                                    inputrate = layout.findViewById(R.id.inputrate);
                                    trfvalue = layout.findViewById(R.id.input_value);
                                    allcurrencyselected = layout.findViewById(R.id.allcurrency);

                                    chosenacc.setText(holder.trfsrc.getText().toString());
                                    inputrate.setText(holder.trfrate.getText().toString());
                                    trfvalue.setText(holder.trfvalue.getText().toString());

                                    notesdata.setText(holder.trfnotes.getText().toString());




                                    inputrate.addTextChangedListener(new com.fake.shopee.shopeefake.formula.commaedittext(inputrate));
                                    trfvalue.addTextChangedListener(new com.fake.shopee.shopeefake.formula.commaedittext(trfvalue));

                                    ImageView calc = layout.findViewById(R.id.transcalc);

                                    chosendate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

                                    calc.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            TypedValue typedValue = new TypedValue();
                                            listtransfer.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
                                            int color = typedValue.data;

                                            int[][] states = new int[][] {
                                                    new int[] { android.R.attr.state_enabled}, // enabled
                                                    new int[] {-android.R.attr.state_enabled}, // disabled
                                                    new int[] {-android.R.attr.state_checked}, // unchecked
                                                    new int[] { android.R.attr.state_pressed}  // pressed
                                            };

                                            int[] colors = new int[] {
                                                    color,
                                                    Color.WHITE,
                                                    Color.GREEN,
                                                    Color.BLUE
                                            };

                                            ColorStateList myList = new ColorStateList(states, colors);

                                            calculatordialog calculatorchoice = new calculatordialog(listtransfer.this, inputrate, myList);
                                            calculatorchoice.showcalculatordialog();
                                        }
                                    });


                                    currdef.setText(generator.defaultcurrency);

                                    choseacc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                           @Override
                                                                           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                               db.collection("account")
                                                                                       .whereEqualTo("account_name", choseacc.getSelectedItem().toString())
                                                                                       .get()
                                                                                       .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                           @Override
                                                                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                                               if (task.isSuccessful()) {
                                                                                                   for (DocumentSnapshot document : task.getResult()) {

                                                                                                       String[] parts = document.getData().get("account_fullcurency").toString().split("-");
                                                                                                       String part1 = parts[0]; // 004
                                                                                                       String part2 = parts[1]; // 034556
                                                                                                       currdef.setText(part2.replace(" ", ""));

                                                                                                       if(currdef.getText().toString().equals(currchosen.getText().toString().replace(" ",""))){
                                                                                                           inputrate.setText("1.00");
                                                                                                           inputrate.setEnabled(false);
                                                                                                       }
                                                                                                       else {
                                                                                                           inputrate.setEnabled(true);
                                                                                                       }
                                                                                                   }
                                                                                               } else {
                                                                                                   Log.e("", "Error getting documents.", task.getException());
                                                                                               }

                                                                                           }
                                                                                       });
                                                                           }
                                                                           @Override
                                                                           public void onNothingSelected(AdapterView<?> adapterView) {

                                                                           }
                                                                       }
                                    );




                                    chosendate.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            generator.chosedate(listtransfer.this,chosendate);
                                            Log.e("data curr",currdef.getText().toString());
                                            Log.e("data curr1",currchosen.getText().toString().replace(" ",""));
                                            if(currdef.getText().toString().equals(currchosen.getText().toString().replace(" ",""))){
                                                inputrate.setText("1.00");
                                                inputrate.setEnabled(false);
                                            }
                                            else {
                                                inputrate.setEnabled(true);
                                            }
                                        }
                                    });

                                    db.collection("account")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                    if (task.isSuccessful()) {
                                                        for (DocumentSnapshot document : task.getResult()) {
                                                            Log.e("getting data", document.getId() + " => " + document.getData());
                                                            account.add(document.getData().get("account_name").toString());

                                                            if(document.getData().get("account_name").toString().equals(holder.trfsrc.getText().toString())){
                                                                for (int i=0;i<currencieses.length;i++){
                                                                        if(currencieses[i].getCode().equals(document.getData().get("account_currency").toString())){
                                                                            currchosen.setText(currencieses[i].getSymbol().toUpperCase());
                                                                            allcurrencyselected.setText(currencieses[i].getSymbol().toUpperCase());
                                                                        }


                                                                   //      Log.e("Currency List", "Nama" + currencieses[i].getName() );
                                                                   //      Log.e("Currency List", "Symbol" + currencieses[i].getSymbol() );
                                                                    //     Log.e("Currency List", "Code" + currencieses[i].getCode() );
                                                                }
                                                            }

                                                            if(document.getData().get("account_name").toString().equals(holder.trfdest.getText().toString())){
                                                                for (int i=0;i<currencieses.length;i++){
                                                                    if(currencieses[i].getCode().equals(document.getData().get("account_currency").toString())){
                                                                        currdef.setText(currencieses[i].getSymbol().toUpperCase());
                                                                    }


                                                                    //      Log.e("Currency List", "Nama" + currencieses[i].getName() );
                                                                    //      Log.e("Currency List", "Symbol" + currencieses[i].getSymbol() );
                                                                    //     Log.e("Currency List", "Code" + currencieses[i].getCode() );
                                                                }
                                                            }

                                                        }
                                                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(listtransfer.this, android.R.layout.simple_spinner_item, account);
                                                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        choseacc.setAdapter(spinnerArrayAdapter);



                                                        if (choseacc != null) {
                                                            int spinnerPosition = spinnerArrayAdapter.getPosition(holder.trfdest.getText().toString());
                                                            choseacc.setSelection(spinnerPosition);
                                                        }

                                                        if(currdef.getText().toString().equals(currchosen.getText().toString().replace(" ",""))){
                                                            inputrate.setText("1.00");
                                                            inputrate.setEnabled(false);
                                                        }
                                                        else {
                                                            inputrate.setEnabled(true);
                                                        }
                                                    } else {
                                                        Log.e("", "Error getting documents.", task.getException());
                                                    }

                                                }
                                            });


                                    chosenacc.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            generator.choseaccount1(listtransfer.this,chosenacc,allcurrencyselected,currchosen);
                                            if(currdef.getText().toString().equals(currchosen.getText().toString().replace(" ",""))){
                                                inputrate.setText("1.00");
                                                inputrate.setEnabled(false);
                                            }
                                            else {
                                                inputrate.setEnabled(true);
                                            }
                                        }
                                    });

                                    AlertDialog build = new AlertDialog.Builder(listtransfer.this,R.style.AppCompatAlertDialogStyle).setNegativeButton("Cancel",null).setPositiveButton("Save", null).setTitle("Transfer").create();

                                    build.setCancelable(false);



                                    build.setOnShowListener(new DialogInterface.OnShowListener() {
                                        @Override
                                        public void onShow(DialogInterface dialogInterface) {
                                            Button button = ((AlertDialog) build).getButton(AlertDialog.BUTTON_POSITIVE);
                                            button.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View view) {
                                                    // TODO Do something

                                                    if(currdef.getText().toString().equals(currchosen.getText().toString().replace(" ",""))){
                                                        inputrate.setText("1.00");
                                                        inputrate.setEnabled(false);
                                                    }
                                                    else {
                                                        inputrate.setEnabled(true);
                                                    }

                                                    //verify data transfer

                                                    if(trfvalue.getText().toString().equals("")){
                                                        Toast.makeText(listtransfer.this, "Input Transfer Amount", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        if(chosenacc.getText().toString().equals("Account")){
                                                            Toast.makeText(listtransfer.this, "Select Source Account by tapping Account Text", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else {
                                                            //Dismiss once everything is OK.
                                                            Toast.makeText(listtransfer.this, "Saving Transfer", Toast.LENGTH_SHORT).show();

                                                            Map<String,Object> mapdata = new HashMap<>();

                                                            Date date22 = Calendar.getInstance().getTime();

                                                            Date today22 = new Date();
                                                            SimpleDateFormat format22 = new SimpleDateFormat("dd/MM/yyyy");
                                                            Date chosendated=null;
                                                            try {
                                                                chosendated = format22.parse(chosendate.getText().toString());
                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }
                                                            String temp ="1";

                                                            if(chosendated.after(date22)) {
                                                                temp = "0";
                                                            }

                                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                                            mapdata.put("transfer_createdate",date22);
                                                            mapdata.put("transfer_amount",trfvalue.getText().toString().replace(",",""));
                                                            mapdata.put("transfer_rate",inputrate.getText().toString().replace(",",""));
                                                            mapdata.put("transfer_dest",choseacc.getSelectedItem().toString());
                                                            mapdata.put("transfer_src",chosenacc.getText().toString());
                                                            mapdata.put("transfer_notes",notesdata.getText().toString());
                                                            mapdata.put("transfer_date",chosendate.getText().toString());
                                                            mapdata.put("transfer_isdated","0");
                                                            mapdata.put("transfer_isdone",temp);
                                                            // mapdata.put("transfer_repeat_time",repeattimedata);
                                                            // mapdata.put("transfer_repeat_period",repeatperioddata);
                                                            //  mapdata.put("transfer_repeat_count",repeatcountdata);
                                                            mapdata.put("username", generator.userlogin);


                                                       /*     db.collection("account")
                                                                    .whereEqualTo("account_name", selectedaccount.getText().toString())
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot document1 : task.getResult()) {
                                                                                    String temp = "1";

                                                                                    Date date = Calendar.getInstance().getTime();

                                                                                    Date today = new Date();
                                                                                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                                                                    Date chosendated=null;
                                                                                    try {
                                                                                        chosendated = format.parse(selectedate.getText().toString());
                                                                                    } catch (ParseException e) {
                                                                                        e.printStackTrace();
                                                                                    }

                                                                                    Log.d("Documentdata", document1.getId() + " => " + document1.getData());
                                                                                    accdoc[0] = document1.getId();
                                                                                    calculate = generator.makedouble(document1.getData().get("account_balance_current").toString());
                                                                                    if(isdone[0]==1){

                                                                                        if(chosendated.after(date)) {
                                                                                            temp = "0";
                                                                                            if(generator.makedouble(inputvalue.getText().toString().replace(",",""))>comparer){
                                                                                                Map<String, Object> data = new HashMap<>();
                                                                                                result=generator.makedouble(inputvalue.getText().toString().replace(",",""))-comparer;
                                                                                                data.put("account_balance_current", String.valueOf(calculate+result+comparer) );

                                                                                                db.collection("account").document(accdoc[0])
                                                                                                        .set(data, SetOptions.merge());



                                                                                            }
                                                                                            else if(generator.makedouble(inputvalue.getText().toString().replace(",",""))<comparer){
                                                                                                Map<String, Object> data = new HashMap<>();
                                                                                                result=comparer-generator.makedouble(inputvalue.getText().toString().replace(",",""));
                                                                                                data.put("account_balance_current", String.valueOf(calculate-result+generator.makedouble(inputvalue.getText().toString().replace(",",""))) );

                                                                                                db.collection("account").document(accdoc[0])
                                                                                                        .set(data, SetOptions.merge());


                                                                                            }
                                                                                            else {
                                                                                                Map<String, Object> data = new HashMap<>();
                                                                                                data.put("account_balance_current", String.valueOf(calculate+generator.makedouble(inputvalue.getText().toString().replace(",",""))));
                                                                                                db.collection("account").document(accdoc[0])
                                                                                                        .set(data, SetOptions.merge());
                                                                                            }
                                                                                        }
                                                                                        else {
                                                                                            temp="1";
                                                                                            if(generator.makedouble(inputvalue.getText().toString().replace(",",""))>comparer){
                                                                                                Map<String, Object> data = new HashMap<>();
                                                                                                result=generator.makedouble(inputvalue.getText().toString().replace(",",""))-comparer;
                                                                                                data.put("account_balance_current", String.valueOf(calculate-result) );

                                                                                                db.collection("account").document(accdoc[0])
                                                                                                        .set(data, SetOptions.merge());
                                                                                            }
                                                                                            else if(generator.makedouble(inputvalue.getText().toString().replace(",",""))<comparer){
                                                                                                Map<String, Object> data = new HashMap<>();
                                                                                                result=comparer-generator.makedouble(inputvalue.getText().toString().replace(",",""));
                                                                                                data.put("account_balance_current", String.valueOf(calculate+result) );

                                                                                                db.collection("account").document(accdoc[0])
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
                                                                                                data.put("account_balance_current", String.valueOf(calculate-result+comparer) );

                                                                                                db.collection("account").document(accdoc[0])
                                                                                                        .set(data, SetOptions.merge());



                                                                                            }
                                                                                            else if(generator.makedouble(inputvalue.getText().toString().replace(",",""))<comparer){
                                                                                                Map<String, Object> data = new HashMap<>();
                                                                                                result=comparer-generator.makedouble(inputvalue.getText().toString().replace(",",""));
                                                                                                data.put("account_balance_current", String.valueOf(calculate+result-comparer));

                                                                                                db.collection("account").document(accdoc[0])
                                                                                                        .set(data, SetOptions.merge());


                                                                                            }
                                                                                            else {
                                                                                                Map<String, Object> data = new HashMap<>();
                                                                                                data.put("account_balance_current", String.valueOf(calculate-comparer));
                                                                                                db.collection("account").document(accdoc[0])
                                                                                                        .set(data, SetOptions.merge());
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    Toast.makeText(editexpense.this, "Income Edited", Toast.LENGTH_SHORT).show();
                                                                                    reloaddata();
                                                                                    if(generator.adapter!=null){
                                                                                        generator.adapter.notifyDataSetChanged();
                                                                                    }


                                                                                    Map<String, Object> mapdata = new HashMap<>();
                                                                                    mapdata.put("expense_amount",inputvalue.getText().toString().replace(",",""));
                                                                                    mapdata.put("expense_account",selectedaccount.getText().toString());
                                                                                    mapdata.put("expense_category",selectedcategory.getText().toString());
                                                                                    mapdata.put("expense_notes", expnote.getText().toString());
                                                                                    mapdata.put("expense_date",selectedate.getText().toString());
                                                                                    mapdata.put("expense_isdone",temp);
                                                                                    mapdata.put("expense_datesys",generator.incdatesys);
                                                                                    mapdata.put("expense_to",expto.getText().toString());
                                                                                    mapdata.put("expense_lastedit", Calendar.getInstance().getTimeInMillis());

                                                                                    db.collection("expense").document(getIntent().getStringExtra("document"))
                                                                                            .set(mapdata, SetOptions.merge());


                                                                                    finish();
                                                                                    generator.incdatesys=0;



                                                                                }
                                                                            } else {
                                                                                Log.d("Documentdata", "Error getting documents: ", task.getException());
                                                                            }
                                                                        }
                                                                    });
                                                                                                    */
                                                        }
                                                    }

                                                }
                                            });
                                        }
                                    });

                                    List<MainActivity.accountobject> allaccount=new ArrayList<MainActivity.accountobject>();

                                    build.setView(layout);


                                    build.show();

                                    return true;
                                case R.id.deleteitem:

                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(contexts,R.style.AppCompatAlertDialogStyle);
                                    builder.setTitle("Confirm");
                                    builder.setMessage("Are you sure to delete Transfer on "+transferlis.get(position).getTransfer_date()+" with "+formatter.format(transferlis.get(position).getTransfer_amount())+" amount and uses "+transferlis.get(position).getTransfer_src()+" and "+transferlis.get(position).getTransfer_dest()+" with Currency rate "+formatter.format(transferlis.get(position).getTransfer_rate())+" ?");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            db.collection("transfer").document(transferlis.get(position).getTransferdoc())
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            db.collection("account")
                                                                    .whereEqualTo("account_name", holder.trfsrc.getText().toString())
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                                            if (task1.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot document : task1.getResult()) {

                                                                                    Map<String, Object> data = new HashMap<>();
                                                                                    Double result=generator.makedouble(holder.trfvalue.getText().toString().replace(",",""))*generator.makedouble(holder.trfrate.getText().toString().replace(",",""));
                                                                                    data.put("account_balance_current", String.valueOf(document.getData().get("account_balance_current").toString()+generator.makedouble(holder.trfvalue.getText().toString().replace(",",""))));

                                                                                    db.collection("account").document(document.getId())
                                                                                            .set(data, SetOptions.merge());

                                                                                    db.collection("account")
                                                                                            .whereEqualTo("account_name", holder.trfdest.getText().toString())
                                                                                            .get()
                                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                    if (task.isSuccessful()) {
                                                                                                        for (QueryDocumentSnapshot document1 : task.getResult()) {

                                                                                                            Map<String, Object> data = new HashMap<>();
                                                                                                            Double result=generator.makedouble(holder.trfvalue.getText().toString().replace(",",""))*generator.makedouble(holder.trfrate.getText().toString().replace(",",""));
                                                                                                            data.put("account_balance_current", String.valueOf(generator.makedouble(document1.getData().get("account_balance_current").toString())-result));
                                                                                                            db.collection("account").document(document1.getId())
                                                                                                                    .set(data, SetOptions.merge());

                                                                                                        }
                                                                                                    } else {
                                                                                                        Log.d("Documentdata", "Error getting documents: ", task.getException());
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                }
                                                                            } else {
                                                                                Log.d("Documentdata", "Error getting documents: ", task1.getException());
                                                                            }
                                                                        }
                                                                    });

                                                            datatransfer.clear();
                                                            adaptertransfer.notifyDataSetChanged();
                                                            reloaddata();

                                                            if(generator.adapter!=null){
                                                                generator.adapter.notifyDataSetChanged();
                                                            }
                                                            Toast.makeText(contexts, "Deleted selected Transfer Data", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(contexts, "Fail Delete selected Transfer data", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    });

                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });

                                    builder.show();

                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    //displaying the popup
                    popup.show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return transferlis.size();
        }

        public void reloaddata(){
            db.collection("transfer")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                datatransfer.clear();
                                if(adaptertransfer!=null){
                                    adaptertransfer.notifyDataSetChanged();
                                }
                                for (QueryDocumentSnapshot document1 : task.getResult()) {
                                    transfer data = new transfer();
                                    data.setTransferdoc(document1.getId());
                                    data.setTransfer_amount(Double.parseDouble(document1.getData().get("transfer_amount").toString()));
                                    data.setTransfer_rate(Double.parseDouble(document1.getData().get("transfer_rate").toString()));
                                    data.setTransfer_date(document1.getData().get("transfer_date").toString());
                                    data.setTransfer_notes(document1.getData().get("transfer_notes").toString());
                                    data.setTransfer_src(document1.getData().get("transfer_src").toString());
                                    data.setTransfer_dest(document1.getData().get("transfer_dest").toString());


                                    datatransfer.add(data);
                                    Collections.sort(datatransfer);
                                    Collections.reverse(datatransfer);
                                }

                                if(adaptertransfer!=null){
                                    adaptertransfer.notifyDataSetChanged();
                                }

                            } else {
                                Log.d("data", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }


}
