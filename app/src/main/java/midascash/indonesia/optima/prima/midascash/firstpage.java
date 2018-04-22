package midascash.indonesia.optima.prima.midascash;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;

import midascash.indonesia.optima.prima.midascash.alarmnotification.alarmreceiver;

/**
 * Created by rwina on 4/21/2018.
 */

public class firstpage extends Activity {

    Calendar calendar;
    View portview,ipview;

    LinearLayout lineartime;

    Button currency,save;
    ToggleButton isdaily;
    EditText port,ip,time;

    int selectedh=21,selectedm=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_firstpage);



        save =findViewById(R.id.coresave);
        currency = findViewById(R.id.corecurrency);

        portview =findViewById(R.id.coreport);
        port = portview.findViewById(R.id.dialogport);

        ipview =findViewById(R.id.coreip);
        ip =ipview.findViewById(R.id.dialogip);

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

        lineartime= findViewById(R.id.lineartime);

        time =findViewById(R.id.coretime);
        isdaily =findViewById(R.id.coredaily);

        isdaily.setChecked(true);

        isdaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isdaily.isChecked()) {
                    isdaily.setChecked(true);
                    lineartime.setVisibility(View.VISIBLE);

                } else {
                    isdaily.setChecked(false);
                    lineartime.setVisibility(View.GONE);
                }
            }
        });

        calendar = Calendar.getInstance();

        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(firstpage.this,R.style.datepicker, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        selectedh=selectedHour;
                        selectedm=selectedMinute;
                        String temp="";
                        if(String.valueOf(selectedMinute).length()==1){
                            temp="0"+String.valueOf(selectedMinute);
                        }
                        else
                        {
                            temp=String.valueOf(selectedMinute);
                        }
                        time.setText( selectedHour + ":" + temp);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(generator.ip.equals("")){

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(firstpage.this,R.style.AppCompatAlertDialogStyle);
                    dialogBuilder.setTitle("Warning");
                    dialogBuilder.setMessage("Empty connection settings will force app to offline mode proceed ?");
                    dialogBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            generator.checkconnection check = new generator.checkconnection(firstpage.this);
                            check.execute();
                        }
                    });

                    dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    dialogBuilder.show();
                }
                else {
                    SharedPreferences.Editor editor = getSharedPreferences("midascash", MODE_PRIVATE).edit();
                    if(ip.getText().toString().equals("")){

                    }else {
                        generator.ip=ip.getText().toString();
                        editor.putString("ip", ip.getText().toString());
                        if(port.getText().toString().equals("")){

                        }
                        else {
                            generator.port=port.getText().toString();
                            editor.putString("port", port.getText().toString());
                        }
                    }

                    editor.putString("currency", currency.getText().toString());
                    editor.putString("daily", isdaily.getText().toString());
                    editor.putString("firstsetup","done");



                    if(isdaily.isChecked()){
                        editor.putString("time", time.getText().toString());

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, selectedh);
                        calendar.set(Calendar.MINUTE, selectedm);
                        calendar.set(Calendar.SECOND, 0);
                        Intent intent1 = new Intent(firstpage.this, alarmreceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(firstpage.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager am = (AlarmManager) firstpage.this.getSystemService(firstpage.this.ALARM_SERVICE);
                        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                        Log.e("Alarm","Alarms set for "+selectedh+" "+selectedm);
                    }
                    else {

                    }
                    editor.apply();

                    savesettings a =new savesettings(firstpage.this);
                    a.execute();
                }



            }
        });







    }

    public class savesettings extends AsyncTask<String,String,String> {
        private TextView statusField,roleField;
        private Context context;
        private Boolean connection = false;
        private int byGetOrPost = 0;
        private String user="";
        private String pass="";
        private ProgressDialog dialog;

        //flag 0 means get and 1 means post.(By default it is get.)
           /* public Logindata(Context context,TextView statusField,TextView roleField,int flag) {
                this.context = context;
                this.statusField = statusField;
                this.roleField = roleField;
                byGetOrPost = flag;
            }*/

        public savesettings(Context context) {
            this.context = context;
            this.dialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
        }

        protected void onPreExecute(){
            this.dialog.setMessage("Saving");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String status="";
            try{

            }catch(Exception e){
                Log.e("Exception",e.getMessage());
                return new String("Exception: " + e.getMessage());
            }
            return status;
        }


        @Override
        protected void onPostExecute(String result){
            this.dialog.dismiss();

            Intent a =new Intent(firstpage.this,MainActivity.class);
            startActivity(a);
            finish();
        }
    }

}
