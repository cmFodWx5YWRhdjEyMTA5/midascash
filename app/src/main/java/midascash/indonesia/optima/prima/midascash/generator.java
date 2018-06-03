package midascash.indonesia.optima.prima.midascash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rwina on 3/26/2018.
 */

public class generator {
    public static int green =Color.parseColor("#00dd00");
    public static int red =Color.parseColor("#dd0000");
    public static int blue =Color.parseColor("#0000dd");

    public static int cashflowchoice=0;
    public static int scheduledchoice=0;
    public static int acountschoice=0;

    public static String usedcurrency="";
    public static TextView fragmentpassdate;

    public static List<String> listcategory = new ArrayList<String>();

    public static TextView statusConnection = null;
    public static String userlogin="";
    public static String ip="";
    public static String port="";
    public static int isoffline=0;
    public static int isadmin=0;
    public static String setupfirstisdone="";

    public static String isdone="0";

    //----------------------income helper-------------------//

    public static EditText incfrom;
    public static EditText incnote;
    public static EditText incamount;

    public static String incaccount;
    public static String incategory;
    public static String incdate;
    public static String incdocument;
    public static String incbalanceleft;

    //----------------------income helper-------------------//
    //----------------------incomelist helper-------------------//

    public static EditText incfrom1;
    public static EditText incnote1;
    public static EditText incamount1;

    public static String incaccount1;
    public static String incategory1;
    public static String incdate1;
    public static String incdocument1;
    public static String incbalanceleft1;

    public static EditText repeatevery;
    public static EditText repeattime;
    public static Spinner period;

    //----------------------incomelist helper-------------------//
    //----------------------expense helper-------------------//

    public static EditText expto;
    public static EditText expnote;
    public static EditText expamount;

    public static String expaccount;
    public static String expategory;
    public static String expdate;
    public static String expdocument;
    public static String expbalanceleft;

    //----------------------expense helper-------------------//
    //----------------------expenselist helper-------------------//

    public static EditText expto1;
    public static EditText expnote1;
    public static EditText expamount1;

    public static String expaccount1;
    public static String expategory1;
    public static String expdate1;
    public static String getExpdocument1;
    public static String expbalanceleft1;

    public static EditText exprepeatevery;
    public static EditText exprepeattime;
    public static Spinner expperiod;

    //----------------------expenselist helper-------------------//

    public static void recallipsettings (final Activity context)
    {

    }

    public static class checkconnection extends AsyncTask<String,String,String> {
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

        public checkconnection(Context context) {
            this.context = context;
            this.dialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
        }

        protected void onPreExecute(){
            this.dialog.setMessage("Connecting");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            try{
                String link="";
                if (generator.port.equals("")){
                    link="http://"+generator.ip+"/android-db/connection.php";
                }else {
                    link="http://"+generator.ip+":"+generator.port+"/android-db/connection.php";
                }
                Log.e("IP", generator.ip);
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                String link1="";
                if (generator.port.equals("")){
                    link1="http://"+generator.ip+"/android-db/datatest.php";
                }else {
                    link1="http://"+generator.ip+":"+generator.port+"/android-db/datatest.php";
                }

                String data1  = URLEncoder.encode("android", "UTF-8") + "=" +
                        URLEncoder.encode("android", "UTF-8");
                Log.e("IP", generator.ip);
                URL url1 = new URL(link1);
                URLConnection conn1 = url1.openConnection();
                conn1.setConnectTimeout(5000);
                conn1.setDoOutput(true);
                OutputStreamWriter wr1 = new OutputStreamWriter(conn1.getOutputStream());

                wr1.write(data1);
                wr1.flush();

                BufferedReader reader1 = new BufferedReader(new
                        InputStreamReader(conn1.getInputStream()));

                StringBuilder sb1 = new StringBuilder();
                String line1 = null;

                // Read Server Response
                while((line1 = reader1.readLine()) != null) {
                    sb1.append(line1);
                    break;
                }
                String datatest="";
                if(sb1.toString().equals("android")){
                    datatest =" Working ";
                }
                else {
                    datatest = "Error Occured";
                }
                String datatest2="";
                if(sb.toString().equals("connected")){
                    datatest2 =" Working ";
                }
                else {
                    datatest2 = "Error Occured";
                }

                String words = "Connection : "+datatest2+"\n Data : "+datatest;

                return words;
            }catch (java.net.SocketTimeoutException e) {
                return new String("Timeout : " + e.getMessage());
            } catch (java.io.IOException e) {
                return new String("Ioexception: " + e.getMessage());
            } catch(Exception e){
                Log.e("Exception",e.getMessage());
                return new String("Exception: " + e.getMessage());
            }
        }


        @Override
        protected void onPostExecute(String result){
            //this.statusField.setText("Login Successful");
            dialog.dismiss();
            //this.roleField.setText(result);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
            dialogBuilder.setTitle("Connection Result");
            dialogBuilder.setMessage(result);
            dialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            dialogBuilder.show();

            Log.e("SOCR",result);
        }
    }
}
