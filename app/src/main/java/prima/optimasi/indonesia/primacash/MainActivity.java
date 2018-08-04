package prima.optimasi.indonesia.primacash;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;


import com.fake.shopee.shopeefake.formula.commaedittext;
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

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
import prima.optimasi.indonesia.primacash.administrator.main_administrator;
import prima.optimasi.indonesia.primacash.listview.categorylistview;
import prima.optimasi.indonesia.primacash.recycleview.adapterviewcategory;
import prima.optimasi.indonesia.primacash.recycleview.mainactivityviews;
import prima.optimasi.indonesia.primacash.reports.chartofbalance.chartofbalance;
import prima.optimasi.indonesia.primacash.settings.SettingsActivity;
import prima.optimasi.indonesia.primacash.transactionactivity.listexpense;
import prima.optimasi.indonesia.primacash.transactionactivity.listincome;
import prima.optimasi.indonesia.primacash.transactionactivity.listtransfer;
import prima.optimasi.indonesia.primacash.reports.summary;
import prima.optimasi.indonesia.primacash.transactionactivity.accountlist;
import prima.optimasi.indonesia.primacash.transactionactivity.categorylist;
import prima.optimasi.indonesia.primacash.transactionactivity.income;
import prima.optimasi.indonesia.primacash.transactionactivity.expense;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseFirestore db;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    DecimalFormat formatter = new DecimalFormat("###,###,###.00");
    private Boolean isFabOpen = false;
    private TextView fab1txt,fab2txt,fab3txt;
    private FloatingActionButton fab,fab1,fab2,fab3;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private LinearLayout mainview;
    private LayoutInflater inflate;

    private SQLiteHelper dbase;

    SwipeRefreshLayout swipe ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(MainActivity.this);

        db = FirebaseFirestore.getInstance();

        swipe = findViewById(R.id.swiperefresh);

        generator.mainmenurefresh = 1;

        prefs = getSharedPreferences("midascash", MODE_PRIVATE);
        editor = getSharedPreferences("midascash", MODE_PRIVATE).edit();

        syncdata();
      /*  Map<String, Object> user = new HashMap<>();
        user.put("username", "administrator");
        user.put("password", "administrator");

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c);

        user.put("join_date", formattedDate);
        user.put("isadmin",1);
        user.put("status",1);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("added", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("error add", "Error adding document", e);
                    }
                });
*/
        //loginprogram();

        dbase = new SQLiteHelper(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainview = findViewById(R.id.layoutmain);

        inflate = LayoutInflater.from(MainActivity.this);

        View dashboards = inflate.inflate(R.layout.layout_main,null);
        loadmainmenu(dashboards);
        mainview.addView(dashboards);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        LayoutInflater inflater = LayoutInflater.from(this);

        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.nav_header_main,null);

        TextView usr = layout.findViewById(R.id.usernamelogin);

        usr.setText("Google");

        navigationView.setNavigationItemSelectedListener(this);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab3 = (FloatingActionButton)findViewById(R.id.fab3);

        fab1txt =(TextView) findViewById(R.id.fab1text);
        fab2txt =(TextView) findViewById(R.id.fab2text);
        fab3txt =(TextView) findViewById(R.id.fab3text);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_foward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();
                Intent a = new Intent(MainActivity.this,expense.class);
                startActivity(a);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();
                Intent a = new Intent(MainActivity.this,income.class);
                startActivity(a);
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();

                //new Transfer

                generator.newtransfer(MainActivity.this);
            }
        });
        mainview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFabOpen){
                    fab.startAnimation(rotate_backward);
                    fab1.startAnimation(fab_close);
                    fab1txt.startAnimation(fab_close);
                    fab2.startAnimation(fab_close);
                    fab2txt.startAnimation(fab_close);
                    fab3.startAnimation(fab_close);
                    fab3txt.startAnimation(fab_close);
                    fab1.setClickable(false);
                    fab2.setClickable(false);
                    isFabOpen = false;
                    Log.d("Raj", "close");
                }
            }
        });

        swipe.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(generator.mainmenurefresh == 1){
                                    mainview.removeAllViews();
                                    View dashboards = inflate.inflate(R.layout.layout_main,null);
                                    loadmainmenu(dashboards);
                                    mainview.addView(dashboards);
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Nothing to Refresh", Toast.LENGTH_SHORT).show();
                                }
                                swipe.setRefreshing(false);
                            }
                        }, 1000);

                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
/*
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle);
            dialogBuilder.setTitle("Connection Test");
            dialogBuilder.setMessage("Proceed with testing or Recheck your ip setting ?");
            dialogBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    generator.checkconnection check = new generator.checkconnection(MainActivity.this);
                    check.execute();
                }
            });

            dialogBuilder.setNegativeButton("Recheck IP", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
                    View view = getLayoutInflater().inflate(R.layout.layout_miniip, null);
                    final EditText ip;
                    ip = view.findViewById(R.id.dialogip);

                    ip.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(ip.getText().toString().length()==15){
                                ip.setText(ip.getText().toString().substring(0,ip.getText().toString().length()-1));
                                ip.setSelection(ip.getText().length());
                            }
                            else {
                                try {
                                    String[] extensionRemoved = ip.getText().toString().split("\\.");
                                    int ipnum = 0, base = 3, declare = 4;
                                    for (base = 3; base < 12; base += declare) {
                                        if (ip.getText().toString().length() <= base) {
                                            ipnum++;
                                        }
                                    }
                                    int[] ipdata = new int[ipnum];
                                    String[] parts = ip.getText().toString().split("\\.");
                                    if (Integer.parseInt(parts[parts.length-1]) > 1000) {
                                        ip.setText(ip.getText().toString().substring(0, ip.getText().toString().length() - 4));
                                        ip.setSelection(ip.getText().length());
                                    }
                                    else if(Integer.parseInt(parts[parts.length-1]) > 255) {
                                        ip.setText(ip.getText().toString().substring(0, ip.getText().toString().length() - 3));
                                        ip.setSelection(ip.getText().length());
                                    }

                                }catch (Exception e){
                                    Log.e("Eroor", e.getMessage());
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    builder.setTitle("Reset IP")
                            .setView(view)
                            .setPositiveButton("Save & Connect", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = getSharedPreferences("midascash", MODE_PRIVATE).edit();
                                    editor.putString("ip", ip.getText().toString());
                                    editor.apply();
                                    generator.checkconnection check = new generator.checkconnection(MainActivity.this);
                                    check.execute();
                                }
                            })
                            .setIcon(R.drawable.logininfocolored)
                            .show();
                }
            });

            dialogBuilder.show();
            return true;
            */
        if (id == R.id.action_help) {
            Intent a = new Intent(MainActivity.this,help.class);
            startActivity(a);

            return true;
        }
        if (id == R.id.action_logout) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);

            dialogBuilder.setMessage("Are you sure to Log out from System ?")
                    .setTitle("Confirm").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    loginprogram();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
            return true;
        }
/*-----------------------notification--------------------//
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentTitle("Midas Cash")
                    .setContentText("Preference saved");

            Intent resultIntent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
            mNotificationManager.notify(NotificationID.getID(), mBuilder.build());
            return true;
            */

        if(id==R.id.action_about){
            Intent a = new Intent(MainActivity.this,about.class);
            startActivity(a);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            generator.mainmenurefresh =1;
            mainview.removeAllViews();
            View dashboards = inflate.inflate(R.layout.layout_main,null);
            loadmainmenu(dashboards);
            mainview.addView(dashboards);

            // Handle the camera action
        } else if (id == R.id.nav_Transaction) {
            mainview.removeAllViews();
            View transaction = inflate.inflate(R.layout.layout_transaction_two,null);
            transactionmethod(transaction);
            mainview.addView(transaction);

        }else if(id == R.id.nav_expenselist){
            Intent a = new Intent(MainActivity.this, listexpense.class);
            startActivity(a);
        } else if(id == R.id.nav_incomelist){
            Intent a = new Intent(MainActivity.this, listincome.class);
            startActivity(a);
        } else if(id == R.id.nav_accountlist){
            Intent a = new Intent(MainActivity.this, accountlist.class);
            startActivity(a);
        } else if (id == R.id.nav_report) {
            mainview.removeAllViews();
            View report = inflate.inflate(R.layout.layout_report_three,null);
            reportmethod(report);
            mainview.addView(report);
        } else if (id == R.id.nav_settings) {
            Intent a = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(a);
        } else if (id == R.id.nav_transferlist) {
            Intent a = new Intent(MainActivity.this, listtransfer.class);
            startActivity(a);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_mail) {

        } else if (id == R.id.nav_supervisor) {
            Intent a = new Intent(MainActivity.this, main_administrator.class);
            startActivity(a);
        } else if (id == R.id.nav_calenderview) {
            mainview.removeAllViews();
            View calender = inflate.inflate(R.layout.layout_calender_one,null);
            calendermethod(calender);
            mainview.addView(calender);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab1txt.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab2txt.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab3txt.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab1txt.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab2txt.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab3txt.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
    }

    public void transactionmethod(View v){
        generator.mainmenurefresh = 0;
        final Button akun,cashin,listtransaction,reminder,category,editereminder;

        akun = v.findViewById(R.id.twoaccounts);
        reminder = v.findViewById(R.id.tworeminder);
        category = v.findViewById(R.id.twocategory);
        cashin =v.findViewById(R.id.twocashin);
        listtransaction =v.findViewById(R.id.twolisttransaction);
        editereminder =v.findViewById(R.id.twofixrem);

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = getLayoutInflater();
                dialogBuilder.setTitle("New Category");
                View dialogView = inflater.inflate(R.layout.layout_input_kategori, null);

                final CircleImageView selected = dialogView.findViewById(R.id.imgselected);
                adapterviewcategory adapter = new adapterviewcategory(MainActivity.this, selected, 30);

                RecyclerView recyclerView = dialogView.findViewById(R.id.recyclercategoryitem);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 5));

                recyclerView.setAdapter(adapter);

                final EditText categoryname = dialogView.findViewById(R.id.categoryname);

                Button buttonshowall = dialogView.findViewById(R.id.categoryedit);

                buttonshowall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent allcategory = new Intent(MainActivity.this,categorylist.class);
                        startActivity(allcategory);
                    }
                });

                dialogBuilder.setPositiveButton("Save", null);

                dialogBuilder.setNegativeButton("Cancel", null);

                dialogBuilder.setView(dialogView);

                final AlertDialog dialog1 = dialogBuilder.create();
                dialog1.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Log.e("selected", "0");
                        Button button = ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("selected", "1");
                                if (categoryname.getText().toString().equals("")) {
                                    Toast.makeText(MainActivity.this,"Category name is Missing",Toast.LENGTH_SHORT).show();
                                } else {
                                    if(categoryname.getText().toString().equals("-")){
                                        Toast.makeText(MainActivity.this,"Category Name Is Used By System",Toast.LENGTH_SHORT).show();
                                    }else {
                                        if (Integer.parseInt(selected.getTag().toString()) == 0) {
                                            Toast.makeText(MainActivity.this,"Please Select Picture",Toast.LENGTH_SHORT).show();
                                        } else {

                                            final int[] statuscode = {0};
                                            Log.e("selected", "2");
                                            Toast.makeText(MainActivity.this,"Please Wait",Toast.LENGTH_SHORT).show();
                                            db.collection("category")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.e("selected", "2,5");
                                                                int isdouble=0;
                                                                for (DocumentSnapshot document : task.getResult()) {
                                                                    statuscode[0] =1;
                                                                    Log.e("selected", "3");
                                                                    if(document.getId()==null){
                                                                        break;
                                                                    }
                                                                    else if (document.getData().get("category_name").toString().equals(categoryname.getText().toString()) && document.getData().get("category_name")!=null) {
                                                                        Toast.makeText(MainActivity.this, categoryname.getText().toString() + " is Already Registered", Toast.LENGTH_SHORT).show();
                                                                        isdouble = 1;
                                                                    }
                                                                }
                                                                if(statuscode[0]==0 || isdouble!=1){
                                                                    Date c = Calendar.getInstance().getTime();
                                                                    Map<String, Object> categorymap = new HashMap<>();
                                                                    categorymap.put("category_name", categoryname.getText().toString());
                                                                    categorymap.put("category_image", selected.getTag());

                                                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                    String formattedDate = df.format(c);

                                                                    categorymap.put("category_createdate", c);
                                                                    categorymap.put("category_status",1);
                                                                    categorymap.put("username",generator.userlogin);

// Add a new document with a generated ID
                                                                    db.collection("category")
                                                                            .add(categorymap)
                                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                @Override
                                                                                public void onSuccess(DocumentReference documentReference) {
                                                                                    dialog1.dismiss();
                                                                                    Toast.makeText(MainActivity.this, "New Category Saved", Toast.LENGTH_SHORT).show();
                                                                                    Log.e("added category", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(MainActivity.this, "Error Occured : "+ e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                                                    Log.e("error add", "Error adding document", e);
                                                                                }
                                                                            });

                                                                }
                                                            } else {
                                                                Log.e("category error add", "Error getting documents.", task.getException());
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
        });

                akun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String[] tempcurrencycode = {""};
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
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
                        db.collection("category")
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

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_item, spinneritem);
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        accountcategory.setAdapter(adapter);

                        editaccount.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent acountedit = new Intent(MainActivity.this, accountlist.class);
                                startActivity(acountedit);
                            }
                        });

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
                                            Toast.makeText(MainActivity.this, "Account Name is Invalid", Toast.LENGTH_SHORT).show();
                                        } else {
                                                if (accountbalance.getText().toString().equals("")) {
                                                    Toast.makeText(MainActivity.this, "Account Balance default 0", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(MainActivity.this,"Please Wait",Toast.LENGTH_SHORT).show();
                                                    final int[] statuscode = {0};
                                                    db.collection("account")
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
                                                                            Toast.makeText(MainActivity.this, accountname.getText().toString() + " is Already Registered", Toast.LENGTH_SHORT).show();
                                                                            isdouble =1;
                                                                        }
                                                                    }
                                                                }
                                                                if(statuscode[0]==0 || isdouble !=1){
                                                                    Date c = Calendar.getInstance().getTime();
                                                                    String balance = accountbalance.getText().toString().replace(",", "");Toast.makeText(MainActivity.this, "New Account Saved", Toast.LENGTH_SHORT).show();

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
                                                                                    Toast.makeText(MainActivity.this, "New Account Saved", Toast.LENGTH_SHORT).show();
                                                                                    Log.e("added account sc 2", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(MainActivity.this, "Error Occured : " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
                                });
                            }
                        });
                        dialog1.show();


                    }
                });

                cashin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = getLayoutInflater();

                        dialogBuilder.setTitle("New Transaction");

                        final CharSequence[] item = {"Income", "Expense", "Transfer"};
                        dialogBuilder.setItems(item, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                if (position == 0) {
                                    Intent a = new Intent(MainActivity.this, income.class);
                                    startActivity(a);
                                } else if (position == 1) {
                                    Intent a = new Intent(MainActivity.this, expense.class);
                                    startActivity(a);
                                } else {
                                    generator.newtransfer(MainActivity.this);
                                }
                            }

                        });


                        dialogBuilder.show();

                    }
                });

                listtransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = getLayoutInflater();

                        dialogBuilder.setTitle("New Transaction");

                        final CharSequence[] item = {"List Income", "List Expense", "List Transfer"};
                        dialogBuilder.setItems(item, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                if (position == 0) {
                                    Intent a = new Intent(MainActivity.this, listincome.class);
                                    startActivity(a);
                                } else if (position == 1) {
                                    Intent a = new Intent(MainActivity.this, listexpense.class);
                                    startActivity(a);
                                } else {
                                    Intent a = new Intent(MainActivity.this, listtransfer.class);
                                    startActivity(a);
                                }
                            }

                        });


                        dialogBuilder.show();
                    }
                });

                reminder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = getLayoutInflater();
                        dialogBuilder.setTitle("Transaction");

                        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        View dialogView = inflater.inflate(R.layout.layout_input_reminder, null);
                        dialogBuilder.setView(dialogView);
                        dialogBuilder.show();

                    }
                });

                editereminder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = getLayoutInflater();
                        dialogBuilder.setTitle("Transaction");

                        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        View dialogView = inflater.inflate(R.layout.layout_input_reminder, null);
                        dialogBuilder.setView(dialogView);
                        dialogBuilder.show();

                    }
                });

            }

            private void calendermethod(View v) {
                generator.mainmenurefresh = 0;

            }

            public void reportmethod(View v) {
                generator.mainmenurefresh = 0;
                final Button accounts, cashflow, schedulelist, chartofbalanc, summaryd;

                accounts = v.findViewById(R.id.reportaccount);
                schedulelist = v.findViewById(R.id.reportschedule);
                cashflow = v.findViewById(R.id.reportcashflow);
                chartofbalanc = v.findViewById(R.id.reportchart);
                summaryd = v.findViewById(R.id.reportsummary);

                accounts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = getLayoutInflater();

                        dialogBuilder.setTitle("Choice");

                        final CharSequence[] item = {"Accounts", "Category"};
                        dialogBuilder.setItems(item, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int position) {

                                Toast.makeText(getApplicationContext(), "selected Item:" + position, Toast.LENGTH_SHORT).show();
                            }

                        });


                        dialogBuilder.show();
                    }
                });

                cashflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = getLayoutInflater();

                        dialogBuilder.setTitle("Choice");

                        final CharSequence[] item = {"Income", "Expense", "Transfer", "All"};
                        dialogBuilder.setItems(item, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int position) {

                                Toast.makeText(getApplicationContext(), "selected Item:" + position, Toast.LENGTH_SHORT).show();
                            }

                        });
                        dialogBuilder.show();
                    }
                });

                schedulelist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = getLayoutInflater();

                        dialogBuilder.setTitle("New Transaction");

                        final CharSequence[] item = {"Scheduled", "Reminder", "All"};
                        dialogBuilder.setItems(item, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int position) {

                                Toast.makeText(getApplicationContext(), "selected Item:" + position, Toast.LENGTH_SHORT).show();
                            }

                        });


                        dialogBuilder.show();
                    }
                });

                chartofbalanc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        
                        generator.accorcat = null;

                        final int[] state = {0};

                        final int[] selftransaction = {0};

                        final int[] useinactive = {0};
                        if(generator.isadmin==0){
                            selftransaction[0]=1;
                        }

                        generator.accorcat = new ArrayList<>();

                        AlertDialog.Builder choicechart = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle);

                        choicechart.setTitle("Options");

                        choicechart.setPositiveButton("Show",null);

                        choicechart.setNegativeButton("Cancel",null);

                        View layout = inflate.inflate(R.layout.dialog_chartofbalance,null);

                        CheckBox opt1;
                        CheckBox opt2;
                        CheckBox opt3;
                        LinearLayout linearadditional;
                        final CheckBox[] optacc = new CheckBox[1];
                        TextView date1;
                        TextView date2;
                        final TextView[] selected = new TextView[1];
                        LinearLayout dynamic;

                        final Button[] accbutton = new Button[1];
                        Button catbutton;

                        dynamic = layout.findViewById(R.id.chartdynamic);

                        View account = inflate.inflate(R.layout.dialog_chart_opt1,null);

                        accbutton[0] = account.findViewById(R.id.opt1accbutton);
                        optacc[0] = account.findViewById(R.id.opt1selectall);
                        selected[0] = account.findViewById(R.id.opt1acclist);

                        List<String> data = new ArrayList<>();

                        List<String> currency = new ArrayList<>();

                        List<Double> balance = new ArrayList<>();

                        dynamic.addView(account);

                        optacc[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){

                                    optacc[0].setEnabled(false);


                                    accbutton[0].setEnabled(false);

                                    if(useinactive[0]==0){
                                        db.collection("account")
                                                .whereEqualTo("account_status",1)
                                                .orderBy("account_name", Query.Direction.ASCENDING)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        int count =0;
                                                        if (task.isSuccessful()) {
                                                            generator.accorcat.clear();
                                                            for (DocumentSnapshot document : task.getResult()) {
                                                                generator.accorcat.add(document.getData().get("account_name").toString());

                                                                if(count == 0){
                                                                    selected[0].setText("");
                                                                    selected[0].setText(document.getData().get("account_name").toString());
                                                                }
                                                                else {
                                                                    selected[0].setText(selected[0].getText().toString()+", "+document.getData().get("account_name").toString());
                                                                }
                                                                count++;
                                                            }
                                                            optacc[0].setEnabled(true);
                                                        } else {
                                                            Log.e("", "Error getting documents.", task.getException());
                                                        }
                                                        if(generator.adapter!=null){
                                                            generator.adapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                });
                                    }else {
                                        db.collection("account")
                                                .orderBy("account_name", Query.Direction.ASCENDING)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        int count =0;
                                                        if (task.isSuccessful()) {
                                                            generator.accorcat.clear();
                                                            for (DocumentSnapshot document : task.getResult()) {
                                                                generator.accorcat.add(document.getData().get("account_name").toString());

                                                                if(count == 0){
                                                                    selected[0].setText("");
                                                                    selected[0].setText(document.getData().get("account_name").toString());
                                                                }
                                                                else {
                                                                    selected[0].setText(selected[0].getText().toString()+", "+document.getData().get("account_name").toString());
                                                                }
                                                                count++;
                                                            }
                                                            optacc[0].setEnabled(true);
                                                        } else {
                                                            Log.e("", "Error getting documents.", task.getException());
                                                        }
                                                        if(generator.adapter!=null){
                                                            generator.adapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                });
                                    }

                                }
                                else {
                                    generator.accorcat.clear();
                                    accbutton[0].setEnabled(true);
                                    selected[0].setText("No Category Selected");
                                    optacc[0].setEnabled(true);
                                }
                            }
                        });

                        accbutton[0].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                generator.accorcat.clear();
                                if(useinactive[0]==0)
                                {
                                    db.collection("account")
                                            .whereEqualTo("account_status",1)
                                            .orderBy("account_name", Query.Direction.ASCENDING)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                    if (task.isSuccessful()) {
                                                        data.clear();
                                                        currency.clear();
                                                        balance.clear();
                                                        for (DocumentSnapshot document : task.getResult()) {
                                                            data.add(document.getData().get("account_name").toString());
                                                            currency.add(document.getData().get("account_currency").toString());
                                                            balance.add(Double.parseDouble(document.getData().get("account_balance_current").toString()));
                                                        }
                                                        generator.reportselectaccount(MainActivity.this,data,balance,selected[0],currency);
                                                    } else {
                                                        Log.e("", "Error getting documents.", task.getException());
                                                    }
                                                    if(generator.adapter!=null){
                                                        generator.adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            });
                                }
                                else {
                                    db.collection("account")
                                            .orderBy("account_name", Query.Direction.ASCENDING)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                    if (task.isSuccessful()) {
                                                        data.clear();
                                                        currency.clear();
                                                        balance.clear();
                                                        for (DocumentSnapshot document : task.getResult()) {
                                                            data.add(document.getData().get("account_name").toString());
                                                            currency.add(document.getData().get("account_currency").toString());
                                                            balance.add(Double.parseDouble(document.getData().get("account_balance_current").toString()));
                                                        }
                                                        generator.reportselectaccount(MainActivity.this,data,balance,selected[0],currency);
                                                    } else {
                                                        Log.e("", "Error getting documents.", task.getException());
                                                    }
                                                    if(generator.adapter!=null){
                                                        generator.adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            });

                                }
                            }
                        });

                        linearadditional = layout.findViewById(R.id.linearadditional);

                        date1 = layout.findViewById(R.id.chartdate1);
                        date2 = layout.findViewById(R.id.chartdate2);
                        opt1 = layout.findViewById(R.id.opt1);
                        opt2 = layout.findViewById(R.id.opt2);
                        opt3 = layout.findViewById(R.id.opt3);

                        if(generator.isadmin==1){
                            opt2.setVisibility(View.VISIBLE);
                            opt3.setVisibility(View.VISIBLE);
                            linearadditional.setVisibility(View.VISIBLE);
                        }
                        else {
                            opt2.setVisibility(View.GONE);
                            opt3.setVisibility(View.GONE);
                            linearadditional.setVisibility(View.GONE);
                        }

                        opt1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){
                                    generator.accorcat.clear();
                                    final categorylistview[] adapter = new categorylistview[1];

                                    List<String> data = new ArrayList<>();
                                    List<Integer> image = new ArrayList<>();

                                    state[0] =1 ;
                                    dynamic.removeAllViews();

                                    View account = inflate.inflate(R.layout.dialog_chart_opt2,null);

                                    accbutton[0] = account.findViewById(R.id.opt2catbutton);
                                    optacc[0] = account.findViewById(R.id.opt2selectall);
                                    selected[0] = account.findViewById(R.id.opt2catlist);

                                    optacc[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if(isChecked){

                                                optacc[0].setEnabled(false);
                                                accbutton[0].setEnabled(false);


                                                db.collection("category")
                                                        .orderBy("category_name", Query.Direction.ASCENDING)
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                int count =0;
                                                                if (task.isSuccessful()) {
                                                                    generator.accorcat.clear();
                                                                    for (DocumentSnapshot document : task.getResult()) {
                                                                        generator.accorcat.add(document.getData().get("category_name").toString());

                                                                        if(count == 0){
                                                                            selected[0].setText("");
                                                                            selected[0].setText(document.getData().get("category_name").toString());
                                                                        }
                                                                        else {
                                                                            selected[0].setText(selected[0].getText().toString()+", "+document.getData().get("category_name").toString());
                                                                        }
                                                                        count++;
                                                                    }
                                                                    optacc[0].setEnabled(true);
                                                                } else {
                                                                    Log.e("", "Error getting documents.", task.getException());
                                                                }
                                                                if(generator.adapter!=null){
                                                                    generator.adapter.notifyDataSetChanged();
                                                                }
                                                            }
                                                        });
                                            }
                                            else {
                                                generator.accorcat.clear();
                                                accbutton[0].setEnabled(true);
                                                selected[0].setText("No Category Selected");
                                                optacc[0].setEnabled(true);
                                            }
                                        }
                                    });

                                    accbutton[0].setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Toast.makeText(MainActivity.this, "Loading Your Category", Toast.LENGTH_SHORT).show();

                                            db.collection("category")
                                                    .orderBy("category_name", Query.Direction.ASCENDING)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                            if (task.isSuccessful()) {
                                                                data.clear();
                                                                image.clear();
                                                                for (DocumentSnapshot document : task.getResult()) {
                                                                    data.add(document.getData().get("category_name").toString());
                                                                    image.add(Integer.parseInt(document.getData().get("category_image").toString()));
                                                                }
                                                                generator.reportselectcategory(MainActivity.this,data,image,selected[0]);
                                                            } else {
                                                                Log.e("", "Error getting documents.", task.getException());
                                                            }
                                                            if(generator.adapter!=null){
                                                                generator.adapter.notifyDataSetChanged();
                                                            }
                                                        }
                                                    });
                                        }
                                    });

                                    dynamic.addView(account);

                                }
                                else {

                                    generator.accorcat.clear();


                                    state[0] = 0;
                                    dynamic.removeAllViews();

                                    balance.clear();

                                    data.clear();
                                    currency.clear();

                                    View account = inflate.inflate(R.layout.dialog_chart_opt1,null);

                                    accbutton[0] = account.findViewById(R.id.opt1accbutton);
                                    optacc[0] = account.findViewById(R.id.opt1selectall);
                                    selected[0] = account.findViewById(R.id.opt1acclist);

                                    optacc[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if(isChecked){

                                                optacc[0].setEnabled(false);
                                                accbutton[0].setEnabled(false);

                                                if(useinactive[0]==1){
                                                    db.collection("account")
                                                            .whereEqualTo("account_status",1)
                                                            .orderBy("account_name", Query.Direction.ASCENDING)
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    int count =0;
                                                                    if (task.isSuccessful()) {
                                                                        generator.accorcat.clear();
                                                                        for (DocumentSnapshot document : task.getResult()) {
                                                                            generator.accorcat.add(document.getData().get("account_name").toString());

                                                                            if(count == 0){
                                                                                selected[0].setText("");
                                                                                selected[0].setText(document.getData().get("account_name").toString());
                                                                            }
                                                                            else {
                                                                                selected[0].setText(selected[0].getText().toString()+", "+document.getData().get("account_name").toString());
                                                                            }
                                                                            count++;
                                                                        }
                                                                        optacc[0].setEnabled(true);
                                                                    } else {
                                                                        Log.e("", "Error getting documents.", task.getException());
                                                                    }
                                                                    if(generator.adapter!=null){
                                                                        generator.adapter.notifyDataSetChanged();
                                                                    }
                                                                }
                                                            });
                                                }
                                                else {
                                                    db.collection("account")
                                                            .orderBy("account_name", Query.Direction.ASCENDING)
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    int count =0;
                                                                    if (task.isSuccessful()) {
                                                                        generator.accorcat.clear();
                                                                        for (DocumentSnapshot document : task.getResult()) {
                                                                            generator.accorcat.add(document.getData().get("account_name").toString());

                                                                            if(count == 0){
                                                                                selected[0].setText("");
                                                                                selected[0].setText(document.getData().get("account_name").toString());
                                                                            }
                                                                            else {
                                                                                selected[0].setText(selected[0].getText().toString()+", "+document.getData().get("account_name").toString());
                                                                            }
                                                                            count++;
                                                                        }
                                                                        optacc[0].setEnabled(true);
                                                                    } else {
                                                                        Log.e("", "Error getting documents.", task.getException());
                                                                    }
                                                                    if(generator.adapter!=null){
                                                                        generator.adapter.notifyDataSetChanged();
                                                                    }
                                                                }
                                                            });
                                                }


                                            }
                                            else {
                                                generator.accorcat.clear();
                                                accbutton[0].setEnabled(true);
                                                selected[0].setText("No account Selected");
                                                optacc[0].setEnabled(true);
                                            }
                                        }
                                    });

                                    accbutton[0].setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                           if(useinactive[0]==0){
                                               db.collection("account")
                                                       .whereEqualTo("account_status",1)
                                                       .orderBy("account_name", Query.Direction.ASCENDING)
                                                       .get()
                                                       .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                               if (task.isSuccessful()) {
                                                                   balance.clear();
                                                                   data.clear();
                                                                   for (DocumentSnapshot document : task.getResult()) {
                                                                       data.add(document.getData().get("account_name").toString());
                                                                       currency.add(document.getData().get("account_currency").toString());
                                                                       balance.add(Double.parseDouble(document.getData().get("account_balance_current").toString()));
                                                                   }
                                                                   generator.reportselectaccount(MainActivity.this,data,balance,selected[0],currency);
                                                               } else {
                                                                   Log.e("", "Error getting documents.", task.getException());
                                                               }
                                                               if(generator.adapter!=null){
                                                                   generator.adapter.notifyDataSetChanged();
                                                               }
                                                           }
                                                       });
                                           }else {
                                               db.collection("account")
                                                       .orderBy("account_name", Query.Direction.ASCENDING)
                                                       .get()
                                                       .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                               if (task.isSuccessful()) {
                                                                   balance.clear();
                                                                   data.clear();
                                                                   for (DocumentSnapshot document : task.getResult()) {
                                                                       data.add(document.getData().get("account_name").toString());
                                                                       currency.add(document.getData().get("account_currency").toString());
                                                                       balance.add(Double.parseDouble(document.getData().get("account_balance_current").toString()));
                                                                   }
                                                                   generator.reportselectaccount(MainActivity.this,data,balance,selected[0],currency);
                                                               } else {
                                                                   Log.e("", "Error getting documents.", task.getException());
                                                               }
                                                               if(generator.adapter!=null){
                                                                   generator.adapter.notifyDataSetChanged();
                                                               }
                                                           }
                                                       });
                                           }
                                        }
                                    });
                                    dynamic.addView(account);

                                }
                            }
                        });

                        opt2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){
                                    selftransaction[0] = 1 ;
                                }
                                else {
                                    selftransaction[0] = 0;
                                }
                            }
                        });

                        opt3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){
                                    useinactive[0] = 1 ;
                                }
                                else {
                                    useinactive[0] = 0;
                                }
                            }
                        });

                        Calendar myCalendar = Calendar.getInstance();

                        DatePickerDialog.OnDateSetListener date1obj = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // TODO Auto-generated method stub
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "dd/MM/yyyy"; //In which you need put here
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                date1.setText(sdf.format(myCalendar.getTime()));
                            }

                        };

                        date1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(MainActivity.this,R.style.datepicker, date1obj, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });

                        DatePickerDialog.OnDateSetListener date2obj = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // TODO Auto-generated method stub
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "dd/MM/yyyy"; //In which you need put here
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                date2.setText(sdf.format(myCalendar.getTime()));
                            }

                        };

                        date2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(MainActivity.this,R.style.datepicker, date2obj, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });

                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                        date1.setText(df.format(new Date()));
                        date2.setText(df.format(new Date()));



                        choicechart.setView(layout);

                        AlertDialog dialog1 = choicechart.create();

                        dialog1.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                Button button = ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_POSITIVE);
                                button.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        // TODO Do something
                                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                        try {

                                            if(generator.accorcat.size()!=0){
                                                if(df.parse(date1.getText().toString()).after(df.parse(date2.getText().toString()))){
                                                    Toast.makeText(MainActivity.this, "First Date Should be before the second date ", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Intent a = new Intent(MainActivity.this, chartofbalance.class);
                                                    a.putStringArrayListExtra("request", (ArrayList<String>) generator.accorcat);
                                                    a.putExtra("shownown", selftransaction[0]);
                                                    a.putExtra("accountorcategory",state[0]);

                                                    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
                                                    DateTime dt1 = formatter.parseDateTime(date1.getText().toString());

                                                    DateTime dt2 = formatter.parseDateTime(date2.getText().toString());

                                                    int diff = Days.daysBetween(dt1.toLocalDate(), dt2.toLocalDate()).getDays();

                                                    a.putExtra("datediff",diff);
                                                    a.putExtra("date1",date1.getText().toString());
                                                    a.putExtra("date2",date2.getText().toString());

                                                    startActivity(a);

                                                }
                                            }else {
                                                if(state[0]==0){
                                                    Toast.makeText(MainActivity.this, "Please select Account", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(MainActivity.this, "Please select Category", Toast.LENGTH_SHORT).show();
                                                }

                                            }

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        //Dismiss once everything is OK.
                                    }
                                });
                            }
                        });

                        dialog1.show();

                    }
                });
                summaryd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(MainActivity.this, summary.class);
                        startActivity(a);
                    }
                });

            }

            private void categoryicons(View v, final CircleImageView circle) {

       /* final CircleImageView food = v.findViewById(R.id.imgfood);
        final CircleImageView cash = v.findViewById(R.id.imgcash);
        final CircleImageView bank = v.findViewById(R.id.imgbank);
        final CircleImageView cheque = v.findViewById(R.id.imgcheque);
        final CircleImageView lend = v.findViewById(R.id.imglend);
        final CircleImageView creditcard = v.findViewById(R.id.imgcreditcard);
        final CircleImageView truck = v.findViewById(R.id.imgtruck);
        final CircleImageView electric = v.findViewById(R.id.imgelectric);
        final CircleImageView ball = v.findViewById(R.id.imgball);
        final CircleImageView health = v.findViewById(R.id.imghealth);

        Button buttonshowall = v.findViewById(R.id.categoryedit);

        buttonshowall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allcategory = new Intent(MainActivity.this,categorylist.class);
                startActivity(allcategory);
            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(food.getDrawable());
                circle.setTag("6");
            }
        });

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(cash.getDrawable());
                circle.setTag("1");
            }
        });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(bank.getDrawable());
                circle.setTag("2");
            }
        });

        cheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(cheque.getDrawable());
                circle.setTag("4");
            }
        });

        lend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(lend.getDrawable());
                circle.setTag("3");
            }
        });

        creditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(creditcard.getDrawable());
                circle.setTag("5");
            }
        });

        electric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(electric.getDrawable());
                circle.setTag("7");
            }
        });

        truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(truck.getDrawable());
                circle.setTag("8");
            }
        });

        ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(ball.getDrawable());
                circle.setTag("10");
            }
        });

        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(health.getDrawable());
                circle.setTag("9");
            }
        });

    */
                //2 places contain this coding above
            }

            public void loginprogram() {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
                dialogBuilder.setCancelable(false);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = getLayoutInflater();
                dialogBuilder.setTitle("Login");
                View dialogView = inflater.inflate(R.layout.layout_minilogin, null);

                dialogBuilder.setPositiveButton("Login", null);

                dialogBuilder.setNegativeButton("Exit", null);

                EditText username = dialogView.findViewById(R.id.dialogusername);
                EditText password = dialogView.findViewById(R.id.dialogpassword);

                dialogBuilder.setView(dialogView);

                final AlertDialog dialog1 = dialogBuilder.create();
                dialog1.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Button button = ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

                                Toast.makeText(MainActivity.this,"Checking...",Toast.LENGTH_SHORT).show();
                                db.collection("users")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                int count=0;
                                                int declare=0;
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        count++;
                                                        declare++;
                                                        if (username.getText().toString().equals(document.getData().get("username"))) {
                                                            if (password.getText().toString().equals(document.getData().get("password"))) {
                                                                count--;
                                                                dialog1.dismiss();
                                                                generator.userlogin=document.getData().get("username").toString();
                                                                Toast.makeText(MainActivity.this, "Welcome " + document.getData().get("username"), Toast.LENGTH_SHORT).show();

                                                                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                                                                Menu nav_Menu = navigationView.getMenu();

                                                                if(document.getData().get("isadmin").toString().equals("1")) {
                                                                    generator.isadmin=1;
                                                                    nav_Menu.findItem(R.id.nav_supervisor).setVisible(true);
                                                                }else {
                                                                    generator.isadmin=0;
                                                                    nav_Menu.findItem(R.id.nav_supervisor).setVisible(false);
                                                                }

                                                            } else {
                                                                Toast.makeText(MainActivity.this, "Username or password doesn't match", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {

                                                        }
                                                    }
                                                    if(declare==count){
                                                        Toast.makeText(MainActivity.this, "Username not found ", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Connection error Occured ", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        });
                        Button button1 = ((AlertDialog) dialog1).getButton(AlertDialog.BUTTON_NEGATIVE);
                        button1.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                                finish();
                            }
                        });
                    }
                });
                dialog1.show();

            }


            private void syncdata(){
                DateFormat mainformat = new SimpleDateFormat("dd/MM/yyyy");
                if(prefs.getString("dateprocess","").equals("")){
                    db.collection("income")
                            .whereEqualTo("income_isdone", "0")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.e("data splash", document.getId() + " => " + document.getData());

                                            if(!document.getData().get("income_repeat_period").toString().equals("")){


                                            }
                                            else {
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                                Date strDate = null;
                                                try {
                                                    strDate = sdf.parse(document.getData().get("income_date").toString());
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                if (mainformat.format(new Date()).equals(mainformat.format(strDate)) || new Date().after(strDate)){
                                                    Double pendingamount= Double.parseDouble(document.getData().get("income_amount").toString());
                                                    db.collection("account")
                                                            .whereEqualTo("account_name", document.getData().get("income_account").toString())
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        for (QueryDocumentSnapshot document1 : task.getResult()) {
                                                                            Log.e("data splash", document1.getId() + " => " + document1.getData());
                                                                            Map<String, Object> data = new HashMap<>();
                                                                            data.put("account_balance_current", formatter.format(pendingamount +Double.parseDouble(document1.getData().get("account_balance_current").toString())).replace(",","") );
                                                                            db.collection("account").document(document1.getId())
                                                                                    .set(data, SetOptions.merge());

                                                                            Map<String, Object> data1 = new HashMap<>();
                                                                            data1.put("income_isdone","1");
                                                                            db.collection("income").document(document.getId())
                                                                                    .set(data1, SetOptions.merge());
                                                                        }
                                                                    } else {
                                                                        Log.d("data splash weeor", "Error getting documents: ", task.getException());
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    } else {
                                        Log.d("data splash weeor", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                    //  DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    // final String date = df.format(Calendar.getInstance().getTime());
                    // editor.putString("dateprocess",date);
                    //  editor.apply();
                    db.collection("expense")
                            .whereEqualTo("expense_isdone", "0")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.e("data splash", document.getId() + " => " + document.getData());

                                            if(!document.getData().get("expense_repeat_period").toString().equals("")){


                                            }
                                            else {
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                                Date strDate = null;
                                                try {
                                                    strDate = sdf.parse(document.getData().get("expense_date").toString());
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                if (mainformat.format(new Date()).equals(mainformat.format(strDate)) || new Date().after(strDate)){
                                                    Log.e("Date result : ","Same");
                                                    Double pendingamount= Double.parseDouble(document.getData().get("expense_amount").toString());
                                                    db.collection("account")
                                                            .whereEqualTo("account_name", document.getData().get("expense_account").toString())
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        for (QueryDocumentSnapshot document1 : task.getResult()) {
                                                                            Log.e("data splash", document1.getId() + " => " + document1.getData());
                                                                            Map<String, Object> data = new HashMap<>();
                                                                            data.put("account_balance_current", formatter.format(Double.parseDouble(document1.getData().get("account_balance_current").toString()) - pendingamount).replace(",","")  );
                                                                            db.collection("account").document(document1.getId())
                                                                                    .set(data, SetOptions.merge());

                                                                            Map<String, Object> data1 = new HashMap<>();
                                                                            data.put("expense_isdone","1" );
                                                                            db.collection("expense").document(document.getId())
                                                                                    .set(data, SetOptions.merge());
                                                                        }
                                                                    } else {
                                                                        Log.d("data splash weeor", "Error getting documents: ", task.getException());
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    } else {
                                        Log.d("data splash weeor", "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    final String date = df.format(Calendar.getInstance().getTime());
                    editor.putString("dateprocess",date);
                    editor.apply();
                }else {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date strDate = null;
                    try {
                        strDate = sdf.parse(prefs.getString("dateprocess",""));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.e("date new", "syncdata: "+sdf.format(Calendar.getInstance().getTime()));
                    Log.e("date old", "syncdata: "+prefs.getString("dateprocess",""));
                    Log.e("date old date", "syncdata: "+sdf.format(new Date()));
                    if (mainformat.format(new Date()).equals(mainformat.format(strDate))){
                        Toast.makeText(this, "Loading Preferences...", Toast.LENGTH_SHORT).show();
                    } else if(new Date().after(strDate)) {
                        Toast.makeText(this, "Loading Preferences...", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Refreshing Pending Balances...", Toast.LENGTH_SHORT).show();


                        db.collection("income")
                                .whereEqualTo("income_isdone", "0")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.e("data splash", document.getId() + " => " + document.getData());

                                                if(!document.getData().get("income_repeat_period").toString().equals("")){


                                                }
                                                else {
                                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                                    Date strDate = null;
                                                    try {
                                                        strDate = sdf.parse(document.getData().get("income_date").toString());
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    if (mainformat.format(new Date()).equals(mainformat.format(strDate)) || new Date().after(strDate)){
                                                        Double pendingamount= Double.parseDouble(document.getData().get("income_amount").toString());
                                                        db.collection("account")
                                                                .whereEqualTo("account_name", document.getData().get("income_account").toString())
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot document1 : task.getResult()) {
                                                                                Log.e("data splash", document1.getId() + " => " + document1.getData());
                                                                                Map<String, Object> data = new HashMap<>();
                                                                                data.put("account_balance_current", formatter.format(pendingamount +Double.parseDouble(document1.getData().get("account_balance_current").toString())).replace(",","") );
                                                                                db.collection("account").document(document1.getId())
                                                                                        .set(data, SetOptions.merge());

                                                                                Map<String, Object> data1 = new HashMap<>();
                                                                                data1.put("income_isdone","1");
                                                                                db.collection("income").document(document.getId())
                                                                                        .set(data1, SetOptions.merge());
                                                                            }
                                                                            if(generator.adapter!=null){
                                                                                generator.adapter.notifyDataSetChanged();
                                                                            }
                                                                        } else {
                                                                            Log.d("data splash weeor", "Error getting documents: ", task.getException());
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        } else {
                                            Log.d("data splash weeor", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                        db.collection("transfer")
                                .whereEqualTo("transfer_isdone", "0")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.e("data splash", document.getId() + " => " + document.getData());

                                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                                    Date strDate = null;
                                                    try {
                                                        strDate = sdf.parse(document.getData().get("transfer_date").toString());
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    if (mainformat.format(new Date()).equals(mainformat.format(strDate)) || new Date().after(strDate)){
                                                        Double pendingamount= Double.parseDouble(document.getData().get("transfer_amount").toString());
                                                        Double pendingamountrate= Double.parseDouble(document.getData().get("transfer_amount").toString())*generator.makedouble(document.getData().get("transfer_rate").toString());
                                                        db.collection("account")
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot document1 : task.getResult()) {

                                                                                if(document.getData().get("transfer_src").toString().equals(document1.getData().get("account_name"))){
                                                                                    Log.e("data splash", document1.getId() + " => " + document1.getData());
                                                                                    Map<String, Object> data = new HashMap<>();
                                                                                    data.put("account_balance_current", formatter.format(Double.parseDouble(document1.getData().get("account_balance_current").toString())- pendingamount).replace(",","")  );
                                                                                    db.collection("account").document(document1.getId())
                                                                                            .set(data, SetOptions.merge());

                                                                                }

                                                                                if(document.getData().get("transfer_dest").toString().equals(document1.getData().get("account_name"))){
                                                                                    Log.e("data splash", document1.getId() + " => " + document1.getData());
                                                                                    Map<String, Object> data = new HashMap<>();
                                                                                    data.put("account_balance_current", formatter.format(pendingamountrate +Double.parseDouble(document1.getData().get("account_balance_current").toString())).replace(",","") );
                                                                                    db.collection("account").document(document1.getId())
                                                                                            .set(data, SetOptions.merge());


                                                                                }


                                                                            }
                                                                            Map<String, Object> data1 = new HashMap<>();
                                                                            data1.put("transfer_isdone","1");
                                                                            db.collection("transfer").document(document.getId())
                                                                                    .set(data1, SetOptions.merge());


                                                                            if(generator.adapter!=null){
                                                                                generator.adapter.notifyDataSetChanged();
                                                                            }
                                                                        } else {
                                                                            Log.d("data splash weeor", "Error getting documents: ", task.getException());
                                                                        }
                                                                    }
                                                                });
                                                    }
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
                        db.collection("expense")
                                .whereEqualTo("expense_isdone", "0")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.e("data splash", document.getId() + " => " + document.getData());

                                                if(!document.getData().get("expense_repeat_period").toString().equals("")){


                                                }
                                                else {
                                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                                    Date strDate = null;
                                                    try {
                                                        strDate = sdf.parse(document.getData().get("expense_date").toString());
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    if (mainformat.format(new Date()).equals(mainformat.format(strDate)) || new Date().after(strDate)){
                                                        Log.e("Date result : ","Same");
                                                        Double pendingamount= Double.parseDouble(document.getData().get("expense_amount").toString());
                                                        db.collection("account")
                                                                .whereEqualTo("account_name", document.getData().get("expense_account").toString())
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot document1 : task.getResult()) {
                                                                                Log.e("data splash", document1.getId() + " => " + document1.getData());
                                                                                Map<String, Object> data = new HashMap<>();
                                                                                data.put("account_balance_current", formatter.format(Double.parseDouble(document1.getData().get("account_balance_current").toString()) - pendingamount).replace(",","")  );
                                                                                db.collection("account").document(document1.getId())
                                                                                        .set(data, SetOptions.merge());

                                                                                Map<String, Object> data1 = new HashMap<>();
                                                                                data1.put("expense_isdone","1");
                                                                                db.collection("expense").document(document.getId())
                                                                                        .set(data1, SetOptions.merge());
                                                                            }
                                                                            if(generator.adapter!=null){
                                                                                generator.adapter.notifyDataSetChanged();
                                                                            }
                                                                        } else {
                                                                            Log.d("data splash weeor", "Error getting documents: ", task.getException());
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        } else {
                                            Log.d("data splash weeor", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        final String date = df.format(Calendar.getInstance().getTime());
                        editor.putString("dateprocess",date);
                        editor.apply();

                    }else if(new Date().before(strDate)){
                        Toast.makeText(this, "Internal Error ! Fixing preferences..", Toast.LENGTH_SHORT).show();
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        final String date = df.format(Calendar.getInstance().getTime());
                        editor.putString("dateprocess",date);
                        editor.apply();

                        Intent a = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(a);
                        finish();
                    }
                }
            }

            public void loadmainmenu(View v){
                RecyclerView recycler = v.findViewById(R.id.dashboard);

                List<String> title = new ArrayList<>();
                title.add("Summary");
                title.add("Accounts");
                title.add("Income - Last 7 days");
                title.add("Expense - Last 7 days");
                title.add("Transactions");

                generator.adapter = new mainactivityviews(MainActivity.this,title);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recycler.setLayoutManager(mLayoutManager);
                recycler.setItemAnimator(new DefaultItemAnimator());
                recycler.setAdapter(generator.adapter);
            }

    public static class accountobject {
        private String accountname;
        private String accountcategory;
        private String accountbalance;
        private String accountfullcurrency;
        private String accountdocument;

        public accountobject(){
        }

        public String getAccountdocument() {
            return accountdocument;
        }

        public String getAccountbalance() {
            return accountbalance;
        }

        public String getAccountcategory() {
            return accountcategory;
        }

        public String getAccountfullcurrency() {
            return accountfullcurrency;
        }

        public String getAccountname() {
            return accountname;
        }

        public void setAccountname(String accountname) {
            this.accountname = accountname;
        }

        public void setAccountcategory(String accountcategory) {
            this.accountcategory = accountcategory;
        }

        public void setAccountbalance(String accountbalance) {
            this.accountbalance = accountbalance;
        }

        public void setAccountdocument(String accountdocument) {
            this.accountdocument = accountdocument;
        }

        public void setAccountfullcurrency(String accountfullcurrency) {
            this.accountfullcurrency = accountfullcurrency;
        }


    }
}
