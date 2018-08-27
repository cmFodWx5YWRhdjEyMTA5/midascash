package prima.optimasi.indonesia.primacash.transactionactivity;

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

import prima.optimasi.indonesia.primacash.MainActivity;
import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.SQLiteHelper;
import prima.optimasi.indonesia.primacash.commaedittext;
import prima.optimasi.indonesia.primacash.extramenuactivity.accountsstatistic;
import prima.optimasi.indonesia.primacash.extramenuactivity.accounttransactions;
import prima.optimasi.indonesia.primacash.formula.calculatordialog;
import prima.optimasi.indonesia.primacash.generator;
import prima.optimasi.indonesia.primacash.objects.account;
import prima.optimasi.indonesia.primacash.objects.category;
import prima.optimasi.indonesia.primacash.objects.firebasedocument;

/**
 * Created by rwina on 4/23/2018.
 */

public class accountlist extends AppCompatActivity{

    FirebaseFirestore db;

    SQLiteHelper dbase;

    RecyclerView accountlist;

    List<account> allaccount;
    List<firebasedocument> alldoc;

    adapteraccounts adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountlist);

        dbase = new SQLiteHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        accountlist = findViewById(R.id.lvaccount);

        db = FirebaseFirestore.getInstance();

        allaccount=new ArrayList<account>();
        alldoc=new ArrayList<firebasedocument>();


        List<account> total = dbase.getAllaccount();




        for(int i=0;i<total.size();i++){

            account b = new account();
            b.setAccount_name(total.get(i).getAccount_name());
            b.setAccount_category(total.get(i).getAccount_category());
            b.setAccount_createdate(total.get(i).getAccount_createdate());
            b.setAccount_balance(total.get(i).getAccount_balance());
            b.setAccount_balance_current(total.get(i).getAccount_balance_current());
            b.setUsername(total.get(i).getUsername());
            b.setAccount_status(total.get(i).getAccount_status());
            b.setAccount_currency(total.get(i).getAccount_currency());
            b.setFullaccount_currency(total.get(i).getFullaccount_currency());

            allaccount.add(b);

        }

        adapter = new adapteraccounts(accountlist.this,accountlist.this,allaccount);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        accountlist.setLayoutManager(mLayoutManager);
        accountlist.setItemAnimator(new DefaultItemAnimator());
        accountlist.setAdapter(adapter);
/*
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

*/


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

            accountbalance.addTextChangedListener(new commaedittext(accountbalance));

            final Spinner accountcategory = dialogView.findViewById(R.id.account_category);

            final TextView currencyicon = dialogView.findViewById(R.id.accountcurrencysymbol);

            List<String> spinneritem = new ArrayList<String>();
            spinneritem.add("Select One");
            List<category> allcat = dbase.getAllcategory();
            for (int a=0;a<allcat.size();a++){
                spinneritem.add(allcat.get(a).getCategory_name());
            }

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
                            if (accountname.getText().toString().equals("") || accountname.getText().toString().equals("Empty")) {
                                Toast.makeText(accountlist.this, "Account Name is Invalid", Toast.LENGTH_SHORT).show();
                            } else {
                                    if (accountbalance.getText().toString().equals("")) {
                                        Toast.makeText(accountlist.this, "Account Balance default 0", Toast.LENGTH_SHORT).show();
                                    } else {

                                        prima.optimasi.indonesia.primacash.objects.account check = dbase.getaccount(accountname.getText().toString());

                                        if(check!=null && !check.getAccount_name().equals("")){
                                            Toast.makeText(accountlist.this, accountname.getText().toString() + " is Already Registered", Toast.LENGTH_SHORT).show();

                                        }
                                        else {
                                            check = new prima.optimasi.indonesia.primacash.objects.account();

                                            check.setAccount_name(accountname.getText().toString());
                                            if(accountcategory.getSelectedItem().toString().equals("Select One")){
                                                check.setAccount_category("-");
                                            }
                                            else {
                                                check.setAccount_category(accountcategory.getSelectedItem().toString());
                                            }
                                            check.setAccount_balance(accountbalance.getText().toString().replace(",",""));
                                            check.setAccount_balance_current(accountbalance.getText().toString().replace(",",""));
                                            check.setAccount_currency(tempcurrencycode[0]);
                                            check.setFullaccount_currency(currency.getText().toString());
                                            check.setAccount_status(1);
                                            check.setCreateorlast(0);
                                            dbase.createAccount(check,generator.userlogin);

                                            dialog1.dismiss();
                                            reloaddata();
                                            Toast.makeText(accountlist.this, "New Account Saved", Toast.LENGTH_SHORT).show();

                                        }
                                        /*

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

                                                                if(accountcategory.getSelectedItem().toString().equals("Select One")){
                                                                    accountsmap.put("account_category", "-");
                                                                }
                                                                else {
                                                                    accountsmap.put("account_category", accountcategory.getSelectedItem().toString());
                                                                }

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

                                        */
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

        public adapteraccounts(Context context,Activity activities,List<account> moviesList) {
            this.accountList = moviesList;
            contexts=context;
            db = new SQLiteHelper(contexts);
            activity=activities;
            fdb = FirebaseFirestore.getInstance();

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
            if(acc.getAccount_status()==1){
                holder.isactive.setChecked(true);
                holder.status=1;
            }
            else {
                holder.isactive.setChecked(false);
                holder.status=0;
            }

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

            holder.summarylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent transacationlist = new Intent(contexts,accountsstatistic.class);

                    Bundle bundle = new Bundle();
                    //Add your data from getFactualResults method to bundle
                    bundle.putString("account_name", holder.accountname.getText().toString());
                    //Add the bundle to the intent
                    transacationlist.putExtras(bundle);
                    startActivity(transacationlist);
                }
            });

            holder.accounttrans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<ExtendedCurrency> currencies = ExtendedCurrency.getAllCurrencies(); //List of all currencies


                    ExtendedCurrency[] currencieses = ExtendedCurrency.CURRENCIES; //Array of all currencies


                    generator.newaccountrf=holder.accountname.getText().toString();

                    for (int i=0;i<currencieses.length;i++){

                        if (currencieses[i].getCode().equals(acc.getAccount_currency())){
                            generator.newaccountrfsymbol = currencieses[i].getSymbol().toUpperCase();
                        }
                        // Log.e("Currency List", "Nama" + currencieses[i].getName() );
                        // Log.e("Currency List", "Symbol" + currencieses[i].getSymbol() );
                        // Log.e("Currency List", "Code" + currencieses[i].getCode() );
                    }
                    generator.newtransfer(accountlist.this);
                }
            });

            holder.isactive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){

                        holder.status=1;
                        account acc = accountList.get(position);

                        acc.setAccount_status(holder.status);

                        int a = dbase.updateaccountstatus(acc,generator.userlogin,acc.getAccount_name());

                        if(a>0)
                        {
                            Toast.makeText(accountlist.this,holder.accountname.getText().toString() + " Activated",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(accountlist.this,"Error occured",Toast.LENGTH_SHORT).show();
                        }



                        /*holder.status=1;
                        Map<String, Object> data = new HashMap<>();
                        data.put("account_status", holder.status);

                        fdb.collection("account").document(holder.documenref)
                                .set(data, SetOptions.merge());
                        Toast.makeText(accountlist.this,holder.accountname.getText().toString() + " Activated",Toast.LENGTH_SHORT).show();
                        */
                    }
                    else {
                        holder.status=0;
                        account acc = accountList.get(position);

                        acc.setAccount_status(holder.status);

                        int a = dbase.updateaccountstatus(acc,generator.userlogin,acc.getAccount_name());

                        if(a>0)
                        {
                            Toast.makeText(accountlist.this,holder.accountname.getText().toString() + " Deactivated",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(accountlist.this,"Error occured",Toast.LENGTH_SHORT).show();
                        }
                        /*
                        holder.status=0;
                        Map<String, Object> data = new HashMap<>();
                        data.put("account_status", holder.status);

                        fdb.collection("account").document(holder.documenref)
                                .set(data, SetOptions.merge());

                        if(generator.adapter!=null){
                            generator.adapter.notifyDataSetChanged();
                        }

                        Toast.makeText(accountlist.this,holder.accountname.getText().toString() + " Deactivated",Toast.LENGTH_SHORT).show();
                        */
                    }
                    // do something, the isChecked will be
                    // true if the switch is in the On position
                }
            });

            holder.eye.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent transacationlist = new Intent(contexts,accounttransactions.class);

                    Bundle bundle = new Bundle();
                    //Add your data from getFactualResults method to bundle
                    bundle.putString("account_name", holder.accountname.getText().toString());
                    bundle.putString("account_balance", acc.getAccount_balance());
                    //Add the bundle to the intent
                    transacationlist.putExtras(bundle);
                    startActivity(transacationlist);
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

                                    account acc = dbase.getaccount(accountList.get(position).getAccount_name());

                                    String tempword="";
                                    String current_balance="";

                                    DecimalFormat formatter = new DecimalFormat("###,###,###,###.##");

                                    final String[] tempcurrencycode = {""};
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(contexts,R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                                    LayoutInflater inflater = (LayoutInflater) contexts.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                                    dialogBuilder.setTitle("Transaction");

                                    View dialogView = inflater.inflate(R.layout.layout_input_akun, null);

                                    //get temp balance for calculation
                                    Double tempbalance = generator.makedouble(acc.getAccount_balance());


                                    Button editaccount = dialogView.findViewById(R.id.accountedit);

                                    current_balance =  acc.getAccount_balance_current();

                                    final Button currency = dialogView.findViewById(R.id.btnaccountcurrency);
                                    Log.e("account currency data", "onSuccess: "+ acc.getFullaccount_currency() +acc.getAccount_currency());
                                    currency.setText(acc.getFullaccount_currency());

                                    final EditText accountname = dialogView.findViewById(R.id.account_name);
                                    accountname.setText(acc.getAccount_name());
                                    tempword= acc.getAccount_name();

                                    final EditText accountbalance = dialogView.findViewById(R.id.account_balance);
                                    accountbalance.setText(formatter.format(Double.parseDouble(acc.getAccount_balance())));

                                    accountbalance.addTextChangedListener(new commaedittext(accountbalance));

                                    final Spinner accountcategory = dialogView.findViewById(R.id.account_category);

                                    Button accountedit = dialogView.findViewById(R.id.accountedit);
                                    accountedit.setVisibility(View.GONE);

                                    final TextView currencyicon = dialogView.findViewById(R.id.accountcurrencysymbol);
                                    currencyicon.setText(acc.getAccount_currency());

                                    List<String> spinneritem = new ArrayList<String>();
                                    spinneritem.add("Select One");

                                    List<category> allcat = dbase.getAllcategory();
                                    for (int a=0;a<allcat.size();a++){
                                        spinneritem.add(allcat.get(a).getCategory_name());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(contexts,
                                            android.R.layout.simple_spinner_item,spinneritem);
                                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                    accountcategory.setAdapter(adapter);

                                    String compareValue = acc.getAccount_category();

                                    if (compareValue != null) {
                                        Log.e("occured compare ", "onSuccess: ");
                                        int spinnerPosition = adapter.getPosition(compareValue);
                                        accountcategory.setSelection(spinnerPosition);
                                    }

                                    tempcurrencycode[0]=acc.getAccount_currency();
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


                                    editaccount.setVisibility(View.GONE);

                                    dialogBuilder.setPositiveButton("Save",null);

                                    dialogBuilder.setNegativeButton("Cancel",null);

                                    dialogBuilder.setView(dialogView);

                                    final AlertDialog dialog1 = dialogBuilder.create();

                                    final String finalTempword = tempword;
                                    String finalCurrent_balance = current_balance;
                                    String finalTempword1 = tempword;
                                    dialog1.setOnShowListener(new DialogInterface.OnShowListener() {
                                        @Override
                                        public void onShow(DialogInterface dialog) {

                                            Button button = ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_POSITIVE);
                                            button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(accountname.getText().toString().equals("") || accountname.getText().toString().equals("Empty")){
                                                        Toast.makeText(contexts,"Invalid Account Name",Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
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

                                                            if(accountcategory.getSelectedItem().toString().equals("Select One")){
                                                                accountsmap.put("account_category", "-");
                                                            }
                                                            else {
                                                                accountsmap.put("account_category", accountcategory.getSelectedItem().toString());
                                                            }

                                                            prima.optimasi.indonesia.primacash.objects.account check = dbase.getaccount(accountname.getText().toString());

                                                            if(check!=null && !check.getAccount_name().equals("") && !check.getAccount_name().equals(accountname.getText().toString())){
                                                                Toast.makeText(accountlist.this, accountname.getText().toString() + " is Already Registered", Toast.LENGTH_SHORT).show();

                                                            }
                                                            else if(check.getAccount_name().equals(accountname.getText().toString())){
                                                                check = new prima.optimasi.indonesia.primacash.objects.account();

                                                                check.setAccount_name(accountname.getText().toString());
                                                                if(accountcategory.getSelectedItem().toString().equals("Select One")){
                                                                    check.setAccount_category("-");
                                                                }
                                                                else {
                                                                    check.setAccount_category(accountcategory.getSelectedItem().toString());
                                                                }
                                                                check.setAccount_balance(accountbalance.getText().toString().replace(",",""));
                                                                check.setAccount_balance_current(accountbalance.getText().toString().replace(",",""));
                                                                check.setAccount_currency(tempcurrencycode[0]);
                                                                check.setFullaccount_currency(currency.getText().toString());
                                                                check.setAccount_createdate(acc.getAccount_createdate());
                                                                check.setAccount_status(1);
                                                                check.setCreateorlast(0);
                                                                dbase.updateaccount(check,generator.userlogin,acc.getAccount_name());

                                                                dialog1.dismiss();
                                                                reloaddata();
                                                                Toast.makeText(accountlist.this, "New Account Saved", Toast.LENGTH_SHORT).show();
                                                            }
                                                            else {
                                                                check = new prima.optimasi.indonesia.primacash.objects.account();

                                                                check.setAccount_name(accountname.getText().toString());
                                                                if(accountcategory.getSelectedItem().toString().equals("Select One")){
                                                                    check.setAccount_category("-");
                                                                }
                                                                else {
                                                                    check.setAccount_category(accountcategory.getSelectedItem().toString());
                                                                }
                                                                check.setAccount_balance(accountbalance.getText().toString().replace(",",""));
                                                                check.setAccount_balance_current(accountbalance.getText().toString().replace(",",""));
                                                                check.setAccount_currency(tempcurrencycode[0]);
                                                                check.setFullaccount_currency(currency.getText().toString());
                                                                check.setAccount_createdate(acc.getAccount_createdate());
                                                                check.setAccount_status(1);
                                                                check.setCreateorlast(0);
                                                                dbase.updateaccount(check,generator.userlogin,acc.getAccount_name());

                                                                dialog1.dismiss();
                                                                reloaddata();
                                                                Toast.makeText(accountlist.this, "New Account Saved", Toast.LENGTH_SHORT).show();

                                                            }

                                                            /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                            SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
                                                            String formattedDate = df.format(c);

                                                            accountsmap.put("account_editdate", c);
                                                            accountsmap.put("account_lastused", df1.format(c));
                                                            accountsmap.put("account_createdate", acc.getAccount_createdate());
                                                            accountsmap.put("account_balance", accountbalance.getText().toString().replace(",",""));
                                                            accountsmap.put("account_balance_current", String.valueOf(calculateresult));
                                                            accountsmap.put("account_currency", tempcurrencycode[0]);
                                                            accountsmap.put("account_fullcurency", currency.getText().toString());
                                                            accountsmap.put("account_status", holder.status);
                                                            accountsmap.put("username", generator.userlogin);



                                                            fdb.collection("account").document("fixing")
                                                                    .set(accountsmap)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Log.d("status write", "DocumentSnapshot successfully written!");

                                                                            fdb.collection("income")
                                                                                    .whereEqualTo("income_account", finalTempword1)
                                                                                    .get()
                                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("income_account",accountname.getText().toString());

                                                                                                    fdb.collection("income").document(document.getId())
                                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                                }
                                                                                            } else {
                                                                                                Log.d("data splash weeor", "Error getting documents: ", task.getException());
                                                                                            }
                                                                                        }
                                                                                    });

                                                                            fdb.collection("transfer")
                                                                                    .whereEqualTo("transfer_src", finalTempword1)
                                                                                    .get()
                                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("transfer_src",accountname.getText().toString());

                                                                                                    fdb.collection("transfer").document(document.getId())
                                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                                }
                                                                                            } else {
                                                                                                Log.d("data tf weeor", "Error getting documents: ", task.getException());
                                                                                            }
                                                                                        }
                                                                                    });
                                                                            fdb.collection("transfer")
                                                                                    .whereEqualTo("transfer_dest", finalTempword1)
                                                                                    .get()
                                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("transfer_dest",accountname.getText().toString());

                                                                                                    fdb.collection("transfer").document(document.getId())
                                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                                }
                                                                                            } else {
                                                                                                Log.d("data tf weeor", "Error getting documents: ", task.getException());
                                                                                            }
                                                                                        }
                                                                                    });
                                                                            //  DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                                                            // final String date = df.format(Calendar.getInstance().getTime());
                                                                            // editor.putString("dateprocess",date);
                                                                            //  editor.apply();
                                                                            fdb.collection("expense")
                                                                                    .whereEqualTo("expense_account", finalTempword1)
                                                                                    .get()
                                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                                    datasrc1.put("expense_account",accountname.getText().toString());

                                                                                                    fdb.collection("expense").document(document.getId())
                                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                                }
                                                                                            } else {
                                                                                                Log.d("data splash weeor", "Error getting documents: ", task.getException());
                                                                                            }
                                                                                        }
                                                                                    });

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
*/
                                                        }
                                                    }
                                                    dbase.closeDB();
                                                }
                                            });
                                        }
                                    });
                                    dialog1.show();

                                    /*
                                    //---------------------------------------------------------------------
                                    DocumentReference docRef = fdb.collection("account").document("fixing");
                                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {



                                        }
                                    });

*/

                                    return true;
                                case R.id.accountdeleteitem:

                                    final String finalTempword12=holder.accountname.getText().toString();

                                    AlertDialog.Builder alerts = new AlertDialog.Builder(contexts,R.style.AppCompatAlertDialogStyle)
                                            .setTitle("Delete " + holder.accountname.getText().toString()).setMessage("Proceed Deleting "+ holder.accountname.getText().toString() + " , Depending Account will be set to empty");
                                    alerts.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dbase.deleteaccount(holder.accountname.getText().toString());
                                            Toast.makeText(accountlist.this,"Deleted Account "+ holder.accountname.getText().toString(),Toast.LENGTH_SHORT).show();
                                            reloaddata();
                                            /*

                                            fdb.collection("account").document("fixing")
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {



                                                            fdb.collection("income")
                                                                    .whereEqualTo("income_account", finalTempword12)
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                    datasrc1.put("income_account","Empty");

                                                                                    fdb.collection("income").document(document.getId())
                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                }
                                                                            } else {
                                                                                Log.d("data splash weeor", "Error getting documents: ", task.getException());
                                                                            }
                                                                        }
                                                                    });

                                                            fdb.collection("transfer")
                                                                    .whereEqualTo("transfer_src", finalTempword12)
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                    datasrc1.put("transfer_src","Empty");

                                                                                    fdb.collection("transfer").document(document.getId())
                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                }
                                                                            } else {
                                                                                Log.d("data tf weeor", "Error getting documents: ", task.getException());
                                                                            }
                                                                        }
                                                                    });
                                                            fdb.collection("transfer")
                                                                    .whereEqualTo("transfer_dest", finalTempword12)
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                    datasrc1.put("transfer_dest","Empty");

                                                                                    fdb.collection("transfer").document(document.getId())
                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                }
                                                                            } else {
                                                                                Log.d("data tf weeor", "Error getting documents: ", task.getException());
                                                                            }
                                                                        }
                                                                    });
                                                            //  DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                                            // final String date = df.format(Calendar.getInstance().getTime());
                                                            // editor.putString("dateprocess",date);
                                                            //  editor.apply();
                                                            fdb.collection("expense")
                                                                    .whereEqualTo("expense_account", finalTempword12)
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                    Map<String, Object> datasrc1 = new HashMap<>();
                                                                                    datasrc1.put("expense_account","Empty");

                                                                                    fdb.collection("expense").document(document.getId())
                                                                                            .set(datasrc1, SetOptions.merge());
                                                                                }
                                                                            } else {
                                                                                Log.d("data splash weeor", "Error getting documents: ", task.getException());
                                                                            }
                                                                        }
                                                                    });

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
*/
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

        List<account> total = dbase.getAllaccount();




        for(int i=0;i<total.size();i++){

            account b = new account();
            b.setAccount_name(total.get(i).getAccount_name());
            b.setAccount_category(total.get(i).getAccount_category());
            b.setAccount_createdate(total.get(i).getAccount_createdate());
            b.setAccount_balance(total.get(i).getAccount_balance());
            b.setAccount_balance_current(total.get(i).getAccount_balance_current());
            b.setUsername(total.get(i).getUsername());
            b.setAccount_status(total.get(i).getAccount_status());
            b.setAccount_currency(total.get(i).getAccount_currency());
            b.setFullaccount_currency(total.get(i).getFullaccount_currency());

            allaccount.add(b);

        }

        adapter.notifyDataSetChanged();


        if(generator.adapter!=null){
            generator.adapter.notifyDataSetChanged();
        }



       /* db.collection("account")
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
                */
    }
}
