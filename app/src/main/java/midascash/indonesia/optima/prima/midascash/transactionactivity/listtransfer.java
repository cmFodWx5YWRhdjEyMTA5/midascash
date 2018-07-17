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

import org.w3c.dom.Document;

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

    View nothing;

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

        nothing = findViewById(R.id.layoutnothing);

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
                                data.setTransfer_isdone(document1.getData().get("transfer_isdone").toString());


                                datatransfer.add(data);
                                Collections.sort(datatransfer);
                                Collections.reverse(datatransfer);
                            }

                            adaptertransfer = new adapterviewtransferlist(listtransfer.this,datatransfer);

                            if(adaptertransfer.getItemCount()==0){
                                recycler.setVisibility(View.GONE);

                            }
                            else {
                                recycler.setVisibility(View.VISIBLE);
                            }

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
            String trfisdone="";

            public MyViewHolder(View view) {
                super(view);
                trfsrc = view.findViewById(R.id.trfsrc);
                trfdest = view.findViewById(R.id.trfdest);
                trfdate= view.findViewById(R.id.trfdate);
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
            holder.trfisdone=holder.trfisdone;

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

                                    chosendate.setText(holder.trfdate.getText().toString());

                                    String tempchosenacc=holder.trfsrc.getText().toString();
                                    String tempchoseacc=holder.trfdest.getText().toString();
                                    String tempdate = holder.trfdate.getText().toString();
                                    Double temprate= generator.makedouble(holder.trfrate.getText().toString().replace(",",""));
                                    Double temptrf = generator.makedouble(holder.trfvalue.getText().toString().replace(",",""));

                                    inputrate.addTextChangedListener(new com.fake.shopee.shopeefake.formula.commaedittext(inputrate));
                                    trfvalue.addTextChangedListener(new com.fake.shopee.shopeefake.formula.commaedittext(trfvalue));

                                    ImageView calc = layout.findViewById(R.id.transcalc);

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

                                                    if(trfvalue.getText().toString().equals("")){
                                                        Toast.makeText(listtransfer.this, "Input Transfer Amount", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        if (chosenacc.getText().toString().equals("Account")) {
                                                            Toast.makeText(listtransfer.this, "Select Source Account by tapping Account Text", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            if (choseacc.getSelectedItem().toString().equals(chosenacc.getText().toString())) {
                                                                Toast.makeText(listtransfer.this, "Transfer source and destination can't be same", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                //marker

                                                                db.collection("account")
                                                                        .whereEqualTo("account_name", chosenacc.getText().toString())
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                        Double check = generator.makedouble(document.getData().get("account_balance_current").toString());
                                                                                        if((generator.makedouble(inputrate.getText().toString())*generator.makedouble(trfvalue.getText().toString().replace(",","")))>check){
                                                                                            Toast.makeText(listtransfer.this, "Your Account Have insufficient Balance", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                        else {


                                                                                            //verify data transfer


                                                                                            //Dismiss once everything is OK.
                                                                                            Toast.makeText(listtransfer.this, "Saving Transfer", Toast.LENGTH_SHORT).show();

                                                                                            Map<String,Object> mapdata = new HashMap<>();

                                                                                            Date date22 = Calendar.getInstance().getTime();

                                                                                            Date today22 = new Date();
                                                                                            SimpleDateFormat format22 = new SimpleDateFormat("dd/MM/yyyy");
                                                                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                                            Date chosendated=null;
                                                                                            try {
                                                                                                chosendated = format22.parse(chosendate.getText().toString());
                                                                                            } catch (ParseException e) {
                                                                                                e.printStackTrace();
                                                                                            }
                                                                                            String temp ="1";

                                                                                            if(chosendated.after(date22)) {
                                                                                                temp ="0";
                                                                                            }



                                                                                            if(holder.trfisdone.equals("0")){
                                                                                                if(temp.equals("0")){
                                                                                                    Log.e("code transfer","0 0 " );
                                                                                                    mapdata.put("transfer_amount",trfvalue.getText().toString().replace(",",""));
                                                                                                    mapdata.put("transfer_rate",inputrate.getText().toString().replace(",",""));
                                                                                                    mapdata.put("transfer_dest",choseacc.getSelectedItem().toString());
                                                                                                    mapdata.put("transfer_src",chosenacc.getText().toString());
                                                                                                    mapdata.put("transfer_notes",notesdata.getText().toString());
                                                                                                    mapdata.put("transfer_date",chosendate.getText().toString());
                                                                                                    mapdata.put("transfer_datesys",chosendated.getTime());
                                                                                                    mapdata.put("transfer_isdated","0");
                                                                                                    mapdata.put("transfer_isdone",temp);
                                                                                                    // mapdata.put("transfer_repeat_time",repeattimedata);
                                                                                                    // mapdata.put("transfer_repeat_period",repeatperioddata);
                                                                                                    //  mapdata.put("transfer_repeat_count",repeatcountdata);
                                                                                                    mapdata.put("username", generator.userlogin);

                                                                                                    db.collection("transfer").document(transferlis.get(position).getTransferdoc())
                                                                                                            .set(mapdata, SetOptions.merge());
                                                                                                }
                                                                                                else {
                                                                                                    Log.e("code transfer","0 1 " );
                                                                                                    // transfer not processed and edited to be processes

                                                                                                    mapdata.put("transfer_amount",trfvalue.getText().toString().replace(",",""));
                                                                                                    mapdata.put("transfer_rate",inputrate.getText().toString().replace(",",""));
                                                                                                    mapdata.put("transfer_dest",choseacc.getSelectedItem().toString());
                                                                                                    mapdata.put("transfer_src",chosenacc.getText().toString());
                                                                                                    mapdata.put("transfer_notes",notesdata.getText().toString());
                                                                                                    mapdata.put("transfer_date",chosendate.getText().toString());
                                                                                                    mapdata.put("transfer_datesys",chosendated.getTime());
                                                                                                    mapdata.put("transfer_isdated","0");
                                                                                                    mapdata.put("transfer_isdone",temp);
                                                                                                    // mapdata.put("transfer_repeat_time",repeattimedata);
                                                                                                    // mapdata.put("transfer_repeat_period",repeatperioddata);
                                                                                                    // mapdata.put("transfer_repeat_count",repeatcountdata);
                                                                                                    mapdata.put("username", generator.userlogin);

                                                                                                    db.collection("transfer").document(transferlis.get(position).getTransferdoc())
                                                                                                            .set(mapdata, SetOptions.merge());

                                                                                                    db.collection("account")
                                                                                                            .whereEqualTo("account_name", chosenacc.getText().toString())
                                                                                                            .get()
                                                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                    if (task.isSuccessful()) {
                                                                                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                                                            Log.d("data", "Cached document data: " + document.getData());
                                                                                                                            Double source =  Double.parseDouble(document.getData().get("account_balance_current").toString());

                                                                                                                            Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                                            datasrc1.put("account_balance_current", String.valueOf(source-Double.parseDouble(trfvalue.getText().toString().replace(",",""))));

                                                                                                                            db.collection("account").document(document.getId())
                                                                                                                                    .set(datasrc1, SetOptions.merge());
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        Log.d("Documentdata", "Error getting documents: ", task.getException());
                                                                                                                    }
                                                                                                                }
                                                                                                            });

                                                                                                    db.collection("account")
                                                                                                            .whereEqualTo("account_name", choseacc.getSelectedItem().toString())
                                                                                                            .get()
                                                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                    if (task.isSuccessful()) {
                                                                                                                        for (QueryDocumentSnapshot document1 : task.getResult()) {
                                                                                                                            Log.d("data", "Cached document data: " + document1.getData());

                                                                                                                            Double destination =  Double.parseDouble(document1.getData().get("account_balance_current").toString());

                                                                                                                            Double rate = Double.parseDouble(inputrate.getText().toString().replace(",",""))*Double.parseDouble(trfvalue.getText().toString().replace(",",""));


                                                                                                                            Map<String, Object> datadest1 = new HashMap<>();
                                                                                                                            datadest1.put("account_balance_current", String.valueOf(destination+rate) );

                                                                                                                            db.collection("account").document(document1.getId())
                                                                                                                                    .set(datadest1, SetOptions.merge());

                                                                                                                            if(generator.adapter!=null){
                                                                                                                                generator.adapter.notifyDataSetChanged();
                                                                                                                            }
                                                                                                                            build.dismiss();
                                                                                                                            Toast.makeText(listtransfer.this, "Transfer Data Edited", Toast.LENGTH_SHORT).show();
                                                                                                                            Log.e("Add data", "DocumentSnapshot added with ID: " + document1.getId());
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        Log.d("Documentdata", "Error getting documents: ", task.getException());
                                                                                                                    }
                                                                                                                }
                                                                                                            });

                                                                                                }
                                                                                            }
                                                                                            else {

                                                                                                List<accountdata> dataaccount=new ArrayList<>();
                                                                                                dataaccount.clear();
                                                                                                String finalTemp = temp;
                                                                                                Date finalChosendated = chosendated;
                                                                                                db.collection("account")
                                                                                                        .get()
                                                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                if (task.isSuccessful()) {
                                                                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                                                        accountdata data = new accountdata();
                                                                                                                        data.setAccountdocument(document.getId());
                                                                                                                        data.setAccountname(document.getData().get("account_name").toString());
                                                                                                                        data.setAccountvalue(document.getData().get("account_balance_current").toString());
                                                                                                                        dataaccount.add(data);
                                                                                                                    }

                                                                                                                    if(finalTemp.equals("0")){
                                                                                                                        Log.e("code transfer","1 0 " );

                                                                                                                        //transfer is processed and edited to be not processed

                                                                                                                        Double srcvalue=0.0d,destvalue=0.0d,oldsrcvalue=0.0d,olddestvalue=0.0d;

                                                                                                                        for(int i=0;i<dataaccount.size();i++){
                                                                                                                            if(dataaccount.get(i).getAccountname().equals(tempchoseacc)) {
                                                                                                                                olddestvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                                                Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                                                datasrc1.put("account_balance_current", String.valueOf(olddestvalue-(temptrf*temprate)));

                                                                                                                                db.collection("account").document(dataaccount.get(i).getAccountdocument())
                                                                                                                                        .set(datasrc1, SetOptions.merge());
                                                                                                                            }
                                                                                                                        }
                                                                                                                        for(int i=0;i<dataaccount.size();i++){
                                                                                                                            if(dataaccount.get(i).getAccountname().equals(tempchosenacc)) {
                                                                                                                                oldsrcvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                                                Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                                                datasrc1.put("account_balance_current", String.valueOf(oldsrcvalue+temptrf));

                                                                                                                                db.collection("account").document(dataaccount.get(i).getAccountdocument())
                                                                                                                                        .set(datasrc1, SetOptions.merge());

                                                                                                                            }
                                                                                                                        }




                                                                                                                    }
                                                                                                                    else {
                                                                                                                        Log.e("code transfer","1 1 " );
                                                                                                                        //transfer is processed and edited to be reprocessed
                                                                                                                        Double srcvalue=0.0d,destvalue=0.0d,oldsrcvalue=0.0d,olddestvalue=0.0d;

                                                                                                                        for(int i=0;i<dataaccount.size();i++){
                                                                                                                            if(dataaccount.get(i).getAccountname().equals(tempchoseacc)) {
                                                                                                                                olddestvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                                                Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                                                datasrc1.put("account_balance_current", String.valueOf(olddestvalue-(temptrf*temprate)));

                                                                                                                                db.collection("account").document(dataaccount.get(i).getAccountdocument())
                                                                                                                                        .set(datasrc1, SetOptions.merge());
                                                                                                                            }
                                                                                                                        }
                                                                                                                        for(int i=0;i<dataaccount.size();i++){
                                                                                                                            if(dataaccount.get(i).getAccountname().equals(tempchosenacc)) {
                                                                                                                                oldsrcvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                                                Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                                                datasrc1.put("account_balance_current", String.valueOf(oldsrcvalue+temptrf));

                                                                                                                                db.collection("account").document(dataaccount.get(i).getAccountdocument())
                                                                                                                                        .set(datasrc1, SetOptions.merge());

                                                                                                                            }
                                                                                                                        }
                                                                                                                        for(int i=0;i<dataaccount.size();i++){
                                                                                                                            if(dataaccount.get(i).getAccountname().equals(choseacc.getSelectedItem().toString())) {
                                                                                                                                destvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                                                Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                                                datasrc1.put("account_balance_current", String.valueOf(destvalue+(generator.makedouble(trfvalue.getText().toString().replace(",",""))*generator.makedouble(inputrate.getText().toString().replace(",","")))));

                                                                                                                                db.collection("account").document(dataaccount.get(i).getAccountdocument())
                                                                                                                                        .set(datasrc1, SetOptions.merge());
                                                                                                                            }
                                                                                                                        }
                                                                                                                        for(int i=0;i<dataaccount.size();i++){
                                                                                                                            if(dataaccount.get(i).getAccountname().equals(chosenacc.getText().toString())) {
                                                                                                                                srcvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                                                Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                                                datasrc1.put("account_balance_current", String.valueOf(srcvalue-generator.makedouble(trfvalue.getText().toString().replace(",",""))));

                                                                                                                                db.collection("account").document(dataaccount.get(i).getAccountdocument())
                                                                                                                                        .set(datasrc1, SetOptions.merge());
                                                                                                                            }
                                                                                                                        }

                                                                                        /*if(!tempchoseacc.equals(choseacc.getSelectedItem().toString()) && !tempchosenacc.equals(chosenacc.getText().toString()))
                                                                                        {


                                                                                            for(int i=0;i<dataaccount.size();i++){
                                                                                                if(dataaccount.get(i).getAccountname().equals(tempchoseacc)) {
                                                                                                    olddestvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("account_balance_current", String.valueOf(olddestvalue-(temptrf*temprate)));

                                                                                                    db.collection("account").document(dataaccount.get(position).getAccountdocument())
                                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                                }
                                                                                            }
                                                                                            for(int i=0;i<dataaccount.size();i++){
                                                                                                if(dataaccount.get(i).getAccountname().equals(tempchosenacc)) {
                                                                                                    oldsrcvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("account_balance_current", String.valueOf(oldsrcvalue+temptrf));

                                                                                                    db.collection("account").document(dataaccount.get(position).getAccountdocument())
                                                                                                            .set(datasrc1, SetOptions.merge());

                                                                                                }
                                                                                            }
                                                                                            for(int i=0;i<dataaccount.size();i++){
                                                                                                if(dataaccount.get(i).getAccountname().equals(choseacc.getSelectedItem().toString())) {
                                                                                                    destvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("account_balance_current", String.valueOf(destvalue+(generator.makedouble(trfvalue.getText().toString().replace(",",""))*generator.makedouble(inputrate.getText().toString().replace(",","")))));

                                                                                                    db.collection("account").document(dataaccount.get(position).getAccountdocument())
                                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                                }
                                                                                            }
                                                                                            for(int i=0;i<dataaccount.size();i++){
                                                                                                if(dataaccount.get(i).getAccountname().equals(chosenacc.getText().toString())) {
                                                                                                    srcvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("account_balance_current", String.valueOf(srcvalue-generator.makedouble(trfvalue.getText().toString().replace(",",""))));

                                                                                                    db.collection("account").document(dataaccount.get(position).getAccountdocument())
                                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        // second
                                                                                        else if(tempchoseacc.equals(choseacc.getSelectedItem().toString()) && !tempchosenacc.equals(chosenacc.getText().toString()))
                                                                                        {
                                                                                            for(int i=0;i<dataaccount.size();i++){
                                                                                                if(dataaccount.get(i).getAccountname().equals(tempchoseacc)) {
                                                                                                    olddestvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("account_balance_current", String.valueOf(olddestvalue-(temptrf*temprate)));

                                                                                                    db.collection("account").document(dataaccount.get(position).getAccountdocument())
                                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                                }
                                                                                            }
                                                                                            for(int i=0;i<dataaccount.size();i++){
                                                                                                if(dataaccount.get(i).getAccountname().equals(tempchosenacc)) {
                                                                                                    oldsrcvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("account_balance_current", String.valueOf(oldsrcvalue+temptrf));

                                                                                                    db.collection("account").document(dataaccount.get(position).getAccountdocument())
                                                                                                            .set(datasrc1, SetOptions.merge());

                                                                                                }
                                                                                            }
                                                                                            for(int i=0;i<dataaccount.size();i++){
                                                                                                if(dataaccount.get(i).getAccountname().equals(choseacc.getSelectedItem().toString())) {
                                                                                                    destvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("account_balance_current", String.valueOf(destvalue+(generator.makedouble(trfvalue.getText().toString().replace(",",""))*generator.makedouble(inputrate.getText().toString().replace(",","")))));

                                                                                                    db.collection("account").document(dataaccount.get(position).getAccountdocument())
                                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                                }
                                                                                            }
                                                                                            for(int i=0;i<dataaccount.size();i++){
                                                                                                if(dataaccount.get(i).getAccountname().equals(chosenacc.getText().toString())) {
                                                                                                    srcvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("account_balance_current", String.valueOf(srcvalue-generator.makedouble(trfvalue.getText().toString().replace(",",""))));

                                                                                                    db.collection("account").document(dataaccount.get(position).getAccountdocument())
                                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        else if(!tempchoseacc.equals(choseacc.getSelectedItem().toString()) && tempchosenacc.equals(chosenacc.getText().toString()))
                                                                                        {
                                                                                            for(int i=0;i<dataaccount.size();i++){
                                                                                                if(dataaccount.get(i).getAccountname().equals(tempchoseacc)) {
                                                                                                    olddestvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("account_balance_current", String.valueOf(olddestvalue-(temptrf*temprate)));

                                                                                                    db.collection("account").document(dataaccount.get(position).getAccountdocument())
                                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                                }
                                                                                            }
                                                                                            for(int i=0;i<dataaccount.size();i++){
                                                                                                if(dataaccount.get(i).getAccountname().equals(tempchosenacc)) {
                                                                                                    oldsrcvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("account_balance_current", String.valueOf(oldsrcvalue+temptrf));

                                                                                                    db.collection("account").document(dataaccount.get(position).getAccountdocument())
                                                                                                            .set(datasrc1, SetOptions.merge());

                                                                                                }
                                                                                            }
                                                                                            for(int i=0;i<dataaccount.size();i++){
                                                                                                if(dataaccount.get(i).getAccountname().equals(choseacc.getSelectedItem().toString())) {
                                                                                                    destvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("account_balance_current", String.valueOf(destvalue+(generator.makedouble(trfvalue.getText().toString().replace(",",""))*generator.makedouble(inputrate.getText().toString().replace(",","")))));

                                                                                                    db.collection("account").document(dataaccount.get(position).getAccountdocument())
                                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                                }
                                                                                            }
                                                                                            for(int i=0;i<dataaccount.size();i++){
                                                                                                if(dataaccount.get(i).getAccountname().equals(chosenacc.getText().toString())) {
                                                                                                    srcvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("account_balance_current", String.valueOf(srcvalue-generator.makedouble(trfvalue.getText().toString().replace(",",""))));

                                                                                                    db.collection("account").document(dataaccount.get(position).getAccountdocument())
                                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        else
                                                                                        {
                                                                                            for(int i=0;i<dataaccount.size();i++){
                                                                                                if(dataaccount.get(i).getAccountname().equals(choseacc.getSelectedItem().toString())) {
                                                                                                    destvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("account_balance_current", String.valueOf(destvalue+(generator.makedouble(trfvalue.getText().toString().replace(",",""))*generator.makedouble(inputrate.getText().toString().replace(",","")))));

                                                                                                    db.collection("account").document(dataaccount.get(position).getAccountdocument())
                                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                                }
                                                                                            }
                                                                                            for(int i=0;i<dataaccount.size();i++){
                                                                                                if(dataaccount.get(i).getAccountname().equals(chosenacc.getText().toString())) {
                                                                                                    srcvalue = generator.makedouble(dataaccount.get(i).getAccountvalue().replace(",",""));
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("account_balance_current", String.valueOf(srcvalue-generator.makedouble(trfvalue.getText().toString().replace(",",""))));

                                                                                                    db.collection("account").document(dataaccount.get(position).getAccountdocument())
                                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                                }
                                                                                            }
                                                                                        }*/
                                                                                                                    }

                                                                                                                    mapdata.put("transfer_amount",trfvalue.getText().toString().replace(",",""));
                                                                                                                    mapdata.put("transfer_rate",inputrate.getText().toString().replace(",",""));
                                                                                                                    mapdata.put("transfer_dest",choseacc.getSelectedItem().toString());
                                                                                                                    mapdata.put("transfer_src",chosenacc.getText().toString());
                                                                                                                    mapdata.put("transfer_notes",notesdata.getText().toString());
                                                                                                                    mapdata.put("transfer_date",chosendate.getText().toString());
                                                                                                                    mapdata.put("transfer_datesys", finalChosendated.getTime());
                                                                                                                    mapdata.put("transfer_isdated","0");
                                                                                                                    mapdata.put("transfer_isdone", finalTemp);
                                                                                                                    // mapdata.put("transfer_repeat_time",repeattimedata);
                                                                                                                    // mapdata.put("transfer_repeat_period",repeatperioddata);
                                                                                                                    // mapdata.put("transfer_repeat_count",repeatcountdata);
                                                                                                                    mapdata.put("username", generator.userlogin);

                                                                                                                    db.collection("transfer").document(transferlis.get(position).getTransferdoc())
                                                                                                                            .set(mapdata, SetOptions.merge());

                                                                                                                    dialogInterface.dismiss();

                                                                                                                    datatransfer.clear();
                                                                                                                    adaptertransfer.notifyDataSetChanged();
                                                                                                                    reloaddata();

                                                                                                                    if(generator.adapter!=null){
                                                                                                                        generator.adapter.notifyDataSetChanged();
                                                                                                                    }
                                                                                                                    Toast.makeText(contexts, "Edited selected Transfer Data", Toast.LENGTH_SHORT).show();


                                                                                                                } else {
                                                                                                                    Log.d("Documentdata", "Error getting documents: ", task.getException());
                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                                //transfer is processed and edited to not processed
                                                                                            }
                                                                                        }

                                                                                    }




                                                                                    //marker



                                                                                    //marker
                                                                                } else {
                                                                                    Log.d("Documentdata", "Error getting documents: ", task.getException());
                                                                                }
                                                                            }
                                                                        });


                                                                //marker
                                                            }
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

                                                            if(holder.trfisdone.equals("0")){

                                                            }
                                                            else {
                                                                db.collection("account")
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                                                if (task1.isSuccessful()) {
                                                                                    for (QueryDocumentSnapshot document : task1.getResult()) {

                                                                                        Map<String, Object> data = new HashMap<>();


                                                                                        if(holder.trfsrc.getText().equals(document.getData().get("account_name"))){
                                                                                            Double result=generator.makedouble(holder.trfvalue.getText().toString().replace(",",""))*generator.makedouble(holder.trfrate.getText().toString().replace(",",""));
                                                                                            data.put("account_balance_current", String.valueOf(generator.makedouble(document.getData().get("account_balance_current").toString())+generator.makedouble(holder.trfvalue.getText().toString().replace(",",""))));


                                                                                            db.collection("account").document(document.getId())
                                                                                                    .set(data, SetOptions.merge());
                                                                                        }

                                                                                        if(holder.trfdest.getText().equals(document.getData().get("account_name"))){
                                                                                            Double result=generator.makedouble(holder.trfvalue.getText().toString().replace(",",""))*generator.makedouble(holder.trfrate.getText().toString().replace(",",""));
                                                                                            data.put("account_balance_current", String.valueOf(generator.makedouble(document.getData().get("account_balance_current").toString())-(generator.makedouble(holder.trfvalue.getText().toString().replace(",","")))*generator.makedouble(holder.trfrate.getText().toString().replace(",",""))));

                                                                                            db.collection("account").document(document.getId())
                                                                                                    .set(data, SetOptions.merge());
                                                                                        }


                                                                                    }
                                                                                } else {
                                                                                    Log.d("Documentdata", "Error getting documents: ", task1.getException());
                                                                                }
                                                                            }
                                                                        });
                                                            }


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

    private class accountdata{
        String accountdocument;
        String accountname;
        String accountvalue;

        public accountdata(){}

        public String getAccountdocument() {
            return accountdocument;
        }

        public String getAccountname() {
            return accountname;
        }

        public String getAccountvalue() {
            return accountvalue;
        }

        public void setAccountdocument(String accountdocument) {
            this.accountdocument = accountdocument;
        }

        public void setAccountname(String accountname) {
            this.accountname = accountname;
        }

        public void setAccountvalue(String accountvalue) {
            this.accountvalue = accountvalue;
        }
    }
}
