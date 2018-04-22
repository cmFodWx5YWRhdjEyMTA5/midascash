package midascash.indonesia.optima.prima.midascash;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.Inflater;

import midascash.indonesia.optima.prima.midascash.alarmnotification.NotificationID;
import pl.rafman.scrollcalendar.ScrollCalendar;
import pl.rafman.scrollcalendar.contract.MonthScrollListener;
import pl.rafman.scrollcalendar.contract.OnDateClickListener;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private LinearLayout mainview;
    private LayoutInflater inflate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainview = findViewById(R.id.layoutmain);

        inflate = LayoutInflater.from(MainActivity.this);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
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
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();
            }
        });
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

        if (id == R.id.action_connection) {
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
        }
        if (id == R.id.action_help) {

            return true;
        }
        if (id == R.id.action_sync) {

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
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            mainview.removeAllViews();
            // Handle the camera action
        } else if (id == R.id.nav_Transaction) {
            mainview.removeAllViews();
            View transaction = inflate.inflate(R.layout.layout_transaction_two,null);
            transactionmethod(transaction);
            mainview.addView(transaction);

        } else if (id == R.id.nav_report) {
            mainview.removeAllViews();
            View report = inflate.inflate(R.layout.layout_report_three,null);
            reportmethod(report);
            mainview.addView(report);
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_mail) {

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
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
    }

    public void transactionmethod(View v){
        Button akun,cashin,cashout,reminder,category,editereminder;

        akun = v.findViewById(R.id.twoaccounts);
        reminder = v.findViewById(R.id.tworeminder);
        category = v.findViewById(R.id.twocategory);
        cashin =v.findViewById(R.id.twocashin);
        cashout =v.findViewById(R.id.twocashout);
        editereminder =v.findViewById(R.id.twofixrem);


        akun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = getLayoutInflater();
                dialogBuilder.setTitle("Transaksi");
                View dialogView = inflater.inflate(R.layout.layout_input_akun, null);
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

                dialogBuilder.setView(dialogView);
                dialogBuilder.show();

            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = getLayoutInflater();
                dialogBuilder.setTitle("Transaksi");
                View dialogView = inflater.inflate(R.layout.layout_input_kategori, null);

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

                dialogBuilder.setView(dialogView);
                dialogBuilder.show();

            }
        });

        cashin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = getLayoutInflater();
                dialogBuilder.setTitle("New Transaction");

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

                dialogBuilder.show();

            }
        });

        cashout.setVisibility(View.GONE);

        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = getLayoutInflater();
                dialogBuilder.setTitle("Transaksi");

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
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = getLayoutInflater();
                dialogBuilder.setTitle("Transaksi");

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

    private void calendermethod(View v){
        ScrollCalendar scrollCalendar = (ScrollCalendar) v.findViewById(R.id.scrollCalendar);
        scrollCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onCalendarDayClicked(int year, int month, int day) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle).create();
                alertDialog.setTitle(String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));
                alertDialog.setMessage("No Events on "+String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });


        scrollCalendar.setMonthScrollListener(new MonthScrollListener() {
            @Override
            public boolean shouldAddNextMonth(int lastDisplayedYear, int lastDisplayedMonth) {
                // return false if you don't want to show later months
                return true;
            }
            @Override
            public boolean shouldAddPreviousMonth(int firstDisplayedYear, int firstDisplayedMonth) {
                // return false if you don't want to show previous months
                return true;
            }
        });
    }

    public void reportmethod(View v){

    }


}
