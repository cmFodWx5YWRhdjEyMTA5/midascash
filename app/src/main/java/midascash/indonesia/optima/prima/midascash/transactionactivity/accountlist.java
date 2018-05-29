package midascash.indonesia.optima.prima.midascash.transactionactivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

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
import midascash.indonesia.optima.prima.midascash.generator;
import midascash.indonesia.optima.prima.midascash.sqlite.account;
import midascash.indonesia.optima.prima.midascash.sqlite.firebasedocument;

/**
 * Created by rwina on 4/23/2018.
 */

public class accountlist extends AppCompatActivity{

    FirebaseFirestore db;



    int[] images = new int[]{R.drawable.cashicon, R.drawable.bank, R.drawable.lendresized, R.drawable.cheque, R.drawable.creditcardresized,R.drawable.food,R.drawable.electric,R.drawable.truck,R.drawable.health,R.drawable.ball,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add};

    adapteraccounts adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountlist);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final RecyclerView accountlist = findViewById(R.id.lvaccount);

        db = FirebaseFirestore.getInstance();

        List<account> allaccount=new ArrayList<account>();
        List<firebasedocument> alldoc=new ArrayList<firebasedocument>();

        db.collection("account")
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
                                String dtStart = document.getData().get("account_createdate").toString();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    c = format.parse(dtStart);
                                    System.out.println(c);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                alldoc.add(new firebasedocument(document.getId().toString()));
                                allaccount.add(new account(document.getData().get("account_name").toString(),document.getData().get("account_category").toString(),document.getData().get("account_createdate").toString(),document.getData().get("account_balance").toString(),document.getData().get("account_balance_current").toString(),document.getData().get("username").toString(),Integer.parseInt(document.getData().get("account_status").toString()),document.getData().get("account_currency").toString(),document.getData().get("account_fullcurency").toString()));
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
            holder.accountcategory.setText(holder.accountcategory.getText().toString()+" "+acc.getAccount_category());
            String string = acc.getFullaccount_currency();
            String[] parts = string.split("-");
            String part1 = parts[0]; // 004
            String part2 = parts[1]; // 034556

            holder.accountbalance.setText(formatter.format(Double.parseDouble(acc.getAccount_balance()))+" "+part1.trim());
            holder.accountname.setText(acc.getAccount_name());

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
                                                                        Date c = Calendar.getInstance().getTime();
                                                                        Map<String, Object> accountsmap = new HashMap<>();
                                                                        accountsmap.put("account_name", accountname.getText().toString());
                                                                        accountsmap.put("account_category", accountcategory.getSelectedItem().toString());

                                                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                        String formattedDate = df.format(c);

                                                                        accountsmap.put("account_editdate", c);
                                                                        accountsmap.put("account_createdate", thisaccount[0].getAccount_createdate());
                                                                        accountsmap.put("account_balance", accountbalance.getText().toString().replace(",",""));
                                                                        accountsmap.put("account_balance_current", finalCurrent_balance.replace(",",""));
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
                                                                                        Intent reloadaccount = new Intent(accountlist.this,accountlist.class);
                                                                                        startActivity(reloadaccount);
                                                                                        finish();
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
                                            .setTitle("Delete" + holder.accountname.getText().toString()).setMessage("Proceed Deleting "+ holder.accountname.getText().toString());
                                    alerts.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            fdb.collection("account").document(holder.documenref)
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Intent reload = new Intent(accountlist.this,accountlist.class);
                                                            startActivity(reload);
                                                            finish();
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
}
