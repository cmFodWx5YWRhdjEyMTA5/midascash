package midascash.indonesia.optima.prima.midascash.transactionactivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import midascash.indonesia.optima.prima.midascash.MainActivity;
import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.SQLiteHelper;
import midascash.indonesia.optima.prima.midascash.extramenuactivity.accounttransactions;
import midascash.indonesia.optima.prima.midascash.formula.calculatordialog;
import midascash.indonesia.optima.prima.midascash.generator;
import midascash.indonesia.optima.prima.midascash.objects.account;
import midascash.indonesia.optima.prima.midascash.objects.firebasedocument;

/**
 * Created by rwina on 4/23/2018.
 */

public class accountlist extends AppCompatActivity{

    FirebaseFirestore db;

    RecyclerView accountlist;

    List<account> allaccount;
    List<firebasedocument> alldoc;

    adapteraccounts adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountlist);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        accountlist = findViewById(R.id.lvaccount);

        db = FirebaseFirestore.getInstance();

        allaccount=new ArrayList<account>();
        alldoc=new ArrayList<firebasedocument>();

        db.collection("account")
                .orderBy("account_name", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if(document.getId()==null){
                                    break;
                                }
                                Date c=null;
                                Object dtStart = document.getData().get("account_createdate");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                alldoc.add(new firebasedocument(document.getId().toString()));
                                allaccount.add(new account(document.getData().get("account_name").toString(),document.getData().get("account_category").toString(),document.getData().get("account_createdate"),document.getData().get("account_balance").toString(),document.getData().get("account_balance_current").toString(),document.getData().get("username").toString(),Integer.parseInt(document.getData().get("account_status").toString()),document.getData().get("account_currency").toString(),document.getData().get("account_fullcurency").toString()));
                                Log.d("Get data account", document.getId() + " => " + document.getData());
                            }
                            adapter = new adapteraccounts(accountlist.this,accountlist.this,allaccount,alldoc);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            accountlist.setLayoutManager(mLayoutManager);
                            accountlist.setItemAnimator(new DefaultItemAnimator());
                            accountlist.setAdapter(adapter);
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
            this.finish();
        }
        if (id == R.id.action_addacc) {
            final String[] tempcurrencycode = {""};
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(accountlist.this, R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
            LayoutInflater inflater = getLayoutInflater();
            dialogBuilder.setTitle("New Account");



            View dialogView = inflater.inflate(R.layout.layout_input_akun, null);

            Button editaccount = dialogView.findViewById(R.id.accountedit);
            final Button currency = dialogView.findViewById(R.id.btnaccountcurrency);

            final EditText accountname = dialogView.findViewById(R.id.account_name);
            final EditText accountbalance = dialogView.findViewById(R.id.account_balance);

            accountbalance.addTextChangedListener(new com.fake.shopee.shopeefake.formula.commaedittext(accountbalance));

            final Spinner accountcategory = dialogView.findViewById(R.id.account_category);

            final TextView currencyicon = dialogView.findViewById(R.id.accountcurrencysymbol);

            List<String> spinneritem = new ArrayList<String>();
            spinneritem.add("Select One");
            db.collection("category")
                    .orderBy("category_name", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    Log.e("getting data", document.getId() + " => " + document.getData());
                                    spinneritem.add(document.getData().get("category_name").toString());
                                }
                            } else {
                                Log.e("", "Error getting documents.", task.getException());
                            }

                        }
                    });

            //translatedcategory.add(allcategory.get(i).getCategory_name()+allcategory.get(i).getCategory_image());

            tempcurrencycode[0]="IDR";
            currency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
                    picker.setListener(new CurrencyPickerListener() {
                        @Override
                        public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                            currency.setText(code + " - " + symbol);
                            tempcurrencycode[0] = code;
                            currencyicon.setText(code);
                            picker.dismiss();
                        }
                    });
                    picker.show(getSupportFragmentManager(), "CURRENCY_PICKER");
                }
            });

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(accountlist.this,
                    android.R.layout.simple_spinner_item, spinneritem);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            accountcategory.setAdapter(adapter);

            editaccount.setVisibility(View.GONE);

            dialogBuilder.setPositiveButton("Save", null);

            dialogBuilder.setNegativeButton("Cancel", null);

            dialogBuilder.setView(dialogView);

            final AlertDialog dialog1 = dialogBuilder.create();

            dialog1.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {

                    Button button = ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (accountname.getText().toString().equals("")) {
                                Toast.makeText(accountlist.this, "Account Name is Empty", Toast.LENGTH_SHORT).show();
                            } else {
                                if (accountcategory.getSelectedItem().toString().equals("Select One")) {
                                    Toast.makeText(accountlist.this, "Please Select Account Category", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (accountbalance.getText().toString().equals("")) {
                                        Toast.makeText(accountlist.this, "Account Balance default 0", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(accountlist.this,"Please Wait",Toast.LENGTH_SHORT).show();
                                        final int[] statuscode = {0};
                                        db.collection("account")
                                                .orderBy("account_name", Query.Direction.ASCENDING)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            int isdouble=0;
                                                            for (DocumentSnapshot document : task.getResult()) {
                                                                statuscode[0] =1;
                                                                Log.d("data account", document.getId() + " => " + document.getData());
                                                                Log.e("occured", String.valueOf(1));
                                                                if(document.getId()==null){
                                                                    break;
                                                                }
                                                                else {
                                                                    if (document.getData().get("account_name").toString().equals(accountname.getText().toString()) && document.getData().get("account_name") != null) {
                                                                        Toast.makeText(accountlist.this, accountname.getText().toString() + " is Already Registered", Toast.LENGTH_SHORT).show();
                                                                        isdouble =1;
                                                                    }
                                                                }
                                                            }
                                                            if(statuscode[0]==0 || isdouble !=1){
                                                                Date c = Calendar.getInstance().getTime();
                                                                String balance = accountbalance.getText().toString().replace(",", "");Toast.makeText(accountlist.this, "New Account Saved", Toast.LENGTH_SHORT).show();

                                                                Map<String, Object> accountsmap = new HashMap<>();
                                                                accountsmap.put("account_name", accountname.getText().toString());
                                                                accountsmap.put("account_category", accountcategory.getSelectedItem().toString());

                                                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                String formattedDate = df.format(c);

                                                                accountsmap.put("account_createdate", c);
                                                                accountsmap.put("account_balance", accountbalance.getText().toString().replace(",",""));
                                                                accountsmap.put("account_balance_current", accountbalance.getText().toString().replace(",",""));
                                                                accountsmap.put("account_currency", tempcurrencycode[0]);
                                                                accountsmap.put("account_fullcurency", currency.getText().toString());
                                                                accountsmap.put("account_status", 1);
                                                                accountsmap.put("username", generator.userlogin);

// Add a new document with a generated ID
                                                                db.collection("account")
                                                                        .add(accountsmap)
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {
                                                                                dialog1.dismiss();
                                                                                Toast.makeText(accountlist.this, "New Account Saved", Toast.LENGTH_SHORT).show();
                                                                                reloaddata();
                                                                                Log.e("added account sc 2", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(accountlist.this, "Error Occured : " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                                                Log.e("error add", "Error adding document", e);
                                                                            }
                                                                        });
                                                            }
                                                        } else {
                                                            Log.w("error get data", "Error getting documents.", task.getException());
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    });
                }
            });
            dialog1.show();

        }
        return  true;
    }

    private class adapteraccounts extends RecyclerView.Adapter<adapteraccounts.MyViewHolder> {

        DecimalFormat formatter= new DecimalFormat("###,###,###.00");

        DecimalFormat formatterdecimal = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();


        Context contexts;
        SQLiteHelper db;
        FirebaseFirestore fdb;
        Activity activity;

        private List<account> accountList;
        private List<firebasedocument> docList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            String documenref;
            int status;
            public TextView accountcategory, accountname, accountbalance,moreoptions;
            ImageView eye,summarylist,accounttrans;
            SwitchCompat isactive;

            public MyViewHolder(View view) {
                super(view);
                accountcategory =  (TextView) view.findViewById(R.id.accountcategoryitem);
                accountname = (TextView) view.findViewById(R.id.accountnameitem);
                accountbalance = (TextView) view.findViewById(R.id.accountbalanceitem);
                moreoptions = (TextView) view.findViewById(R.id.textViewOptions);

                eye = (ImageView) view.findViewById(R.id.accountsstatistics);
                summarylist = (ImageView) view.findViewById(R.id.accountstransactions);
                accounttrans = (ImageView) view.findViewById(R.id.accountstransfer);

                isactive = view.findViewById(R.id.accountsisactive);
            }
        }

        public adapteraccounts(Context context,Activity activities,List<account> moviesList,List<firebasedocument> documentref) {
            this.accountList = moviesList;
            contexts=context;
            db = new SQLiteHelper(contexts);
            activity=activities;
            fdb = FirebaseFirestore.getInstance();
            docList =documentref;

            symbols.setGroupingSeparator(',');
            formatterdecimal.setDecimalFormatSymbols(symbols);

        }

        @Override
        public adapteraccounts.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_layout_account, parent, false);

            return new adapteraccounts.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final adapteraccounts.MyViewHolder holder, int position) {
            account acc = accountList.get(position);
            firebasedocument doc =docList.get(position);
            if(acc.getAccount_status()==1){
                holder.isactive.setChecked(true);
                holder.status=1;
            }
            else {
                holder.isactive.setChecked(false);
                holder.status=0;
            }

            holder.documenref = doc.getDocument();
            holder.accountcategory.setText("Category :"+" "+acc.getAccount_category());
            String string = acc.getFullaccount_currency();
            String[] parts = string.split("-");
            String part1 = parts[0]; // 004
            String part2 = parts[1]; // 034556
            holder.accountbalance.setText(formatter.format(Double.parseDouble(acc.getAccount_balance_current()))+" "+part1.trim());
            if(Double.parseDouble(acc.getAccount_balance_current())>=0){
                holder.accountbalance.setTextColor(generator.green);
            }
            else {
                holder.accountbalance.setTextColor(generator.red);
            }
            holder.accountname.setText(acc.getAccount_name());
            holder.accountname.setTextColor(Color.BLACK);
            
            holder.accounttrans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    TextView chosenacc,currchosen,currdef,chosendate,allcurrencyselected;
                    Spinner choseacc;
                    EditText inputrate,trfvalue,notesdata;


                    ArrayList<String> account = new ArrayList<>();


                    List<ExtendedCurrency> currencies = ExtendedCurrency.getAllCurrencies(); //List of all currencies


                    ExtendedCurrency[] currencieses = ExtendedCurrency.CURRENCIES; //Array of all currencies





                    LayoutInflater inflater = LayoutInflater.from(accountlist.this);

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

                    chosenacc.setText(holder.accountname.getText().toString());

                    for (int i=0;i<currencieses.length;i++){

                        if (currencieses[i].getCode().equals(acc.getAccount_currency())){
                            currchosen.setText(currencieses[i].getSymbol().toUpperCase());
                            allcurrencyselected.setText(currencieses[i].getSymbol().toUpperCase());
                        }
                        // Log.e("Currency List", "Nama" + currencieses[i].getName() );
                        // Log.e("Currency List", "Symbol" + currencieses[i].getSymbol() );
                        // Log.e("Currency List", "Code" + currencieses[i].getCode() );
                    }

                    inputrate.addTextChangedListener(new com.fake.shopee.shopeefake.formula.commaedittext(inputrate));
                    trfvalue.addTextChangedListener(new com.fake.shopee.shopeefake.formula.commaedittext(trfvalue));

                    ImageView calc = layout.findViewById(R.id.transcalc);

                    chosendate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

                    calc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            TypedValue typedValue = new TypedValue();
                            accountlist.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
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

                            calculatordialog calculatorchoice = new calculatordialog(accountlist.this, inputrate, myList);
                            calculatorchoice.showcalculatordialog();
                        }
                    });


                    currdef.setText(generator.defaultcurrency);

                    choseacc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                           @Override
                                                           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                               fdb.collection("account")
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
                            generator.chosedate(accountlist.this,chosendate);
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

                    fdb.collection("account")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult()) {
                                            Log.e("getting data", document.getId() + " => " + document.getData());
                                            account.add(document.getData().get("account_name").toString());
                                        }
                                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(accountlist.this, android.R.layout.simple_spinner_item, account);
                                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        choseacc.setAdapter(spinnerArrayAdapter);
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
                            generator.choseaccount1(accountlist.this,chosenacc,allcurrencyselected,currchosen);
                            if(currdef.getText().toString().equals(currchosen.getText().toString().replace(" ",""))){
                                inputrate.setText("1.00");
                                inputrate.setEnabled(false);
                            }
                            else {
                                inputrate.setEnabled(true);
                            }
                        }
                    });


                    AlertDialog build = new AlertDialog.Builder(accountlist.this,R.style.AppCompatAlertDialogStyle).setNegativeButton("Cancel",null).setPositiveButton("Save", null).setTitle("Transfer").create();

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
                                        Toast.makeText(accountlist.this, "Input Transfer Amount", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        if(chosenacc.getText().toString().equals("Account")){
                                            Toast.makeText(accountlist.this, "Select Source Account by tapping Account Text", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            //Dismiss once everything is OK.
                                            Toast.makeText(accountlist.this, "Saving Transfer", Toast.LENGTH_SHORT).show();

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


                                            fdb.collection("transfer")
                                                    .add(mapdata)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
// wrong from here
                                                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                                            Date strDate = null;
                                                            try {
                                                                strDate = sdf.parse(chosendate.getText().toString());
                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }
                                                            Log.e("date 1", chosendate.getText().toString());
                                                            Log.e("date 2", sdf.format(new Date()));

                                                            if (strDate.before(new Date()) || strDate.equals(new Date())) {
                                                                Log.e("date is before", "same");

                                                                fdb.collection("account")
                                                                        .whereEqualTo("account_name", chosenacc.getText().toString())
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                        fdb.collection("account")
                                                                                                .whereEqualTo("account_name", choseacc.getSelectedItem().toString())
                                                                                                .get()
                                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                                                                        if (task1.isSuccessful()) {
                                                                                                            for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                                                                                                Double source =  Double.parseDouble(document.getData().get("account_balance_current").toString());
                                                                                                                Double destination =  Double.parseDouble(document1.getData().get("account_balance_current").toString());

                                                                                                                Double rate = Double.parseDouble(inputrate.getText().toString().replace(",",""))*Double.parseDouble(trfvalue.getText().toString().replace(",",""));


                                                                                                                Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                                datasrc1.put("account_balance_current", String.valueOf(source-Double.parseDouble(trfvalue.getText().toString().replace(",",""))));

                                                                                                                fdb.collection("account").document(document.getId())
                                                                                                                        .set(datasrc1, SetOptions.merge());

                                                                                                                Map<String, Object> datadest1 = new HashMap<>();
                                                                                                                datadest1.put("account_balance_current", String.valueOf(destination+rate) );

                                                                                                                fdb.collection("account").document(document1.getId())
                                                                                                                        .set(datadest1, SetOptions.merge());

                                                                                                                if(generator.adapter!=null){
                                                                                                                    generator.adapter.notifyDataSetChanged();
                                                                                                                }
                                                                                                                build.dismiss();
                                                                                                                Toast.makeText(accountlist.this, "Transfer Data Added", Toast.LENGTH_SHORT).show();
                                                                                                                Log.e("Add data", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                                                                            }
                                                                                                        } else {
                                                                                                            Log.d("Documentdata", "Error getting documents: ", task.getException());
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                } else {
                                                                                    Log.d("Documentdata", "Error getting documents: ", task.getException());
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                            else {
                                                                Log.e("date is after", "not same");
                                                                if(generator.adapter!=null){
                                                                    generator.adapter.notifyDataSetChanged();
                                                                }

                                                                build.dismiss();
                                                                Toast.makeText(accountlist.this, "Transfer Data Added", Toast.LENGTH_SHORT).show();
                                                                Log.e("Add data", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                            }




                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(accountlist.this, "Error : Please Contact Support or Retry", Toast.LENGTH_SHORT).show();
                                                            Log.e("error data add", "Error adding document", e);
                                                        }
                                                    });

                                        }
                                    }
                                }
                            });
                        }
                    });

                    List<MainActivity.accountobject> allaccount=new ArrayList<MainActivity.accountobject>();

                    build.setView(layout);


                    build.show();

                }
            });

            holder.isactive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        holder.status=1;
                        Map<String, Object> data = new HashMap<>();
                        data.put("account_status", holder.status);

                        fdb.collection("account").document(holder.documenref)
                                .set(data, SetOptions.merge());
                        Toast.makeText(accountlist.this,holder.accountname.getText().toString() + " Activated",Toast.LENGTH_SHORT).show();

                    }
                    else {
                        holder.status=0;
                        Map<String, Object> data = new HashMap<>();
                        data.put("account_status", holder.status);

                        fdb.collection("account").document(holder.documenref)
                                .set(data, SetOptions.merge());
                        Toast.makeText(accountlist.this,holder.accountname.getText().toString() + " Deactivated",Toast.LENGTH_SHORT).show();
                    }
                    // do something, the isChecked will be
                    // true if the switch is in the On position
                }
            });

            holder.eye.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent transacationlist = new Intent(contexts,accounttransactions.class);
                    startActivity(transacationlist);
                    finish();
                }
            });



            holder.moreoptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(contexts, holder.moreoptions);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.menu_accountitems);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.accountedititem:
                                    Toast.makeText(accountlist.this,"Loading "+holder.accountname.getText().toString()+"....",Toast.LENGTH_SHORT).show();
                                    //---------------------------------------------------------------------
                                    DocumentReference docRef = fdb.collection("account").document(holder.documenref);
                                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            String tempword="";
                                            String current_balance="";

                                            DecimalFormat formatter = new DecimalFormat("###,###,###,###.##");

                                            final String[] tempcurrencycode = {""};
                                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(contexts,R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                                            LayoutInflater inflater = (LayoutInflater) contexts.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                                            dialogBuilder.setTitle("Transaction");

                                            View dialogView = inflater.inflate(R.layout.layout_input_akun, null);

                                            final account[] thisaccount = {null};
                                            thisaccount[0] = documentSnapshot.toObject(account.class);

                                            //get temp balance for calculation
                                            Double tempbalance = generator.makedouble(thisaccount[0].getAccount_balance());


                                            Button editaccount = dialogView.findViewById(R.id.accountedit);

                                            current_balance =  thisaccount[0].getAccount_balance_current();

                                            final Button currency = dialogView.findViewById(R.id.btnaccountcurrency);
                                            Log.e("account currency data", "onSuccess: "+ thisaccount[0].getFullaccount_currency() +thisaccount[0].getAccount_currency());
                                            currency.setText(documentSnapshot.getData().get("account_fullcurency").toString());

                                            final EditText accountname = dialogView.findViewById(R.id.account_name);
                                            accountname.setText(thisaccount[0].getAccount_name());
                                            tempword= thisaccount[0].getAccount_name();

                                            final EditText accountbalance = dialogView.findViewById(R.id.account_balance);
                                            accountbalance.setText(formatter.format(Double.parseDouble(thisaccount[0].getAccount_balance())));

                                            accountbalance.addTextChangedListener(new com.fake.shopee.shopeefake.formula.commaedittext(accountbalance));

                                            final Spinner accountcategory = dialogView.findViewById(R.id.account_category);

                                            Button accountedit = dialogView.findViewById(R.id.accountedit);
                                            accountedit.setVisibility(View.GONE);

                                            final TextView currencyicon = dialogView.findViewById(R.id.accountcurrencysymbol);
                                            currencyicon.setText(documentSnapshot.getData().get("account_currency").toString());

                                            List<String> spinneritem = new ArrayList<String>();
                                            spinneritem.add("Select One");;
                                            fdb.collection("category")
                                                    .orderBy("category_name", Query.Direction.ASCENDING)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                            if (task.isSuccessful()) {
                                                                for (DocumentSnapshot document : task.getResult()) {
                                                                    Log.e("getting data", document.getId() + " => " + document.getData());
                                                                    spinneritem.add(document.getData().get("category_name").toString());
                                                                }

                                                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(contexts,
                                                                        android.R.layout.simple_spinner_item,spinneritem);
                                                                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                                                accountcategory.setAdapter(adapter);

                                                                String compareValue = documentSnapshot.getData().get("account_category").toString();

                                                                if (compareValue != null) {
                                                                    Log.e("occured compare ", "onSuccess: ");
                                                                    int spinnerPosition = adapter.getPosition(compareValue);
                                                                    accountcategory.setSelection(spinnerPosition);
                                                                }
                                                            } else {
                                                                Log.e("", "Error getting documents.", task.getException());
                                                            }

                                                        }
                                                    });
                                            tempcurrencycode[0]=thisaccount[0].getAccount_currency();
                                            currency.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
                                                    picker.setListener(new CurrencyPickerListener() {
                                                        @Override
                                                        public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                                                            currency.setText(code+" - "+ symbol);
                                                            tempcurrencycode[0] =code;
                                                            currencyicon.setText(code);
                                                            picker.dismiss();
                                                        }
                                                    });

                                                    picker.show(((FragmentActivity) contexts).getSupportFragmentManager(), "CURRENCY_PICKER");

                                                }
                                            });





                                            Log.e("sopinner data", "onSuccess: " + documentSnapshot.getData().get("account_category").toString() );



                                            editaccount.setVisibility(View.GONE);

                                            dialogBuilder.setPositiveButton("Save",null);

                                            dialogBuilder.setNegativeButton("Cancel",null);

                                            dialogBuilder.setView(dialogView);

                                            final AlertDialog dialog1 = dialogBuilder.create();

                                            final String finalTempword = tempword;
                                            String finalCurrent_balance = current_balance;
                                            dialog1.setOnShowListener(new DialogInterface.OnShowListener() {
                                                @Override
                                                public void onShow(DialogInterface dialog) {

                                                    Button button = ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_POSITIVE);
                                                    button.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if(accountname.getText().toString().equals("")){
                                                                Toast.makeText(contexts,"Account Name is Empty",Toast.LENGTH_SHORT).show();
                                                            }
                                                            else {
                                                                if(accountcategory.getSelectedItem().toString().equals("Select One")){
                                                                    Toast.makeText(contexts,"Please Select Account Category",Toast.LENGTH_SHORT).show();
                                                                }else {
                                                                    if(accountbalance.getText().toString().equals("")){
                                                                        Toast.makeText(contexts,"Account Balance default 0",Toast.LENGTH_SHORT).show();
                                                                    }else {
                                                                        Double difference = 0.0d;
                                                                        Double calculateresult = 0.0d;
                                                                        if(generator.makedouble(accountbalance.getText().toString().replace(",",""))-tempbalance!=0.0d){
                                                                            Log.e("calculate result","not same"+ accountbalance.getText().toString() +String.valueOf(tempbalance));
                                                                            if(generator.makedouble(accountbalance.getText().toString().replace(",",""))>tempbalance){
                                                                                calculateresult=generator.makedouble(finalCurrent_balance.replace(",",""))+(generator.makedouble(accountbalance.getText().toString().replace(",",""))-tempbalance);
                                                                                Log.e("calculate result",String.valueOf(calculateresult) );
                                                                            }
                                                                            else if(generator.makedouble(accountbalance.getText().toString().replace(",",""))<tempbalance){
                                                                                calculateresult=generator.makedouble(finalCurrent_balance.replace(",",""))-(tempbalance-generator.makedouble(accountbalance.getText().toString().replace(",","")));
                                                                                Log.e("calculate result",String.valueOf(calculateresult) );
                                                                            }
                                                                        }
                                                                        else {
                                                                            Log.e("calculate result","same" );
                                                                            calculateresult = generator.makedouble(accountbalance.getText().toString().replace(",",""));
                                                                        }

                                                                        Date c = Calendar.getInstance().getTime();
                                                                        Map<String, Object> accountsmap = new HashMap<>();
                                                                        accountsmap.put("account_name", accountname.getText().toString());
                                                                        accountsmap.put("account_category", accountcategory.getSelectedItem().toString());

                                                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                        String formattedDate = df.format(c);

                                                                        accountsmap.put("account_editdate", c);
                                                                        accountsmap.put("account_createdate", thisaccount[0].getAccount_createdate());
                                                                        accountsmap.put("account_balance", accountbalance.getText().toString().replace(",",""));
                                                                        accountsmap.put("account_balance_current", String.valueOf(calculateresult));
                                                                        accountsmap.put("account_currency", tempcurrencycode[0]);
                                                                        accountsmap.put("account_fullcurency", currency.getText().toString());
                                                                        accountsmap.put("account_status", holder.status);
                                                                        accountsmap.put("username", generator.userlogin);


                                                                        fdb.collection("account").document(holder.documenref)
                                                                                .set(accountsmap)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        Log.d("status write", "DocumentSnapshot successfully written!");
                                                                                        reloaddata();
                                                                                        Toast.makeText(accountlist.this,thisaccount[0].getAccount_name() +" changed into " + accountname.getText().toString(),Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        Log.w("data error fdb", "Error writing document", e);
                                                                                    }
                                                                                }); //db.updateaccount(new account(accountname.getText().toString(),accountcategory.getSelectedItem().toString(),c,Double.parseDouble(balance), generator.userlogin,1,tempcurrencycode[0].toString(),currency.getText().toString()),generator.userlogin, finalTempword);
                                                                        dialog1.dismiss();
                                                                        Toast.makeText(contexts, "Account Edited", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                }
                                                            }
                                                            db.closeDB();
                                                        }
                                                    });
                                                }
                                            });
                                            dialog1.show();

                                        }
                                    });



                                    return true;
                                case R.id.accountdeleteitem:

                                    AlertDialog.Builder alerts = new AlertDialog.Builder(contexts,R.style.AppCompatAlertDialogStyle)
                                            .setTitle("Delete " + holder.accountname.getText().toString()).setMessage("Proceed Deleting "+ holder.accountname.getText().toString());
                                    alerts.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            fdb.collection("account").document(holder.documenref)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            reloaddata();
                                                            Toast.makeText(accountlist.this,"Deleted Account "+ holder.accountname.getText().toString(),Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(accountlist.this,"Fail Delete Account "+ holder.accountname.getText().toString(),Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        }
                                    });
                                    alerts.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alerts.show();
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
            return accountList.size();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menuaccount, menu);
        return true;
    }

    public void reloaddata(){
        alldoc.clear();
        allaccount.clear();
        if(adapter!= null){
            adapter.notifyDataSetChanged();
        }
        db.collection("account")
                .orderBy("account_name", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if(document.getId()==null){
                                    break;
                                }
                                Date c=null;
                                Object dtStart = document.getData().get("account_createdate");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                alldoc.add(new firebasedocument(document.getId().toString()));
                                allaccount.add(new account(document.getData().get("account_name").toString(),document.getData().get("account_category").toString(),document.getData().get("account_createdate"),document.getData().get("account_balance").toString(),document.getData().get("account_balance_current").toString(),document.getData().get("username").toString(),Integer.parseInt(document.getData().get("account_status").toString()),document.getData().get("account_currency").toString(),document.getData().get("account_fullcurency").toString()));
                                Log.d("Get data account", document.getId() + " => " + document.getData());
                            }
                            if(adapter!= null){
                                adapter.notifyDataSetChanged();
                            }
                            if(generator.adapter!=null){
                                generator.adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w("Get account error", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
