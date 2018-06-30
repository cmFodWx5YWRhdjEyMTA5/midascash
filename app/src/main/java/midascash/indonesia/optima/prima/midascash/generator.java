package midascash.indonesia.optima.prima.midascash;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import midascash.indonesia.optima.prima.midascash.fragment_transaction.fragment_expense_show;
import midascash.indonesia.optima.prima.midascash.fragment_transaction.fragment_expense_show_scheduled;
import midascash.indonesia.optima.prima.midascash.fragment_transaction.fragment_income;
import midascash.indonesia.optima.prima.midascash.fragment_transaction.fragment_income_show;
import midascash.indonesia.optima.prima.midascash.fragment_transaction.fragment_income_show_scheduled;
import midascash.indonesia.optima.prima.midascash.objects.expense;
import midascash.indonesia.optima.prima.midascash.objects.income;
import midascash.indonesia.optima.prima.midascash.recycleview.mainactivityviews;
import midascash.indonesia.optima.prima.midascash.transactionactivity.listexpense;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rwina on 3/26/2018.
 */

public class generator {

    private static AlertDialog dialog;
    private static AlertDialog dialogaccount;
    private static Calendar myCalendar;

    public static DecimalFormat formatter = new DecimalFormat("###,###,###.00");

    public static mainactivityviews adapter;

    public static int green =Color.parseColor("#00dd00");
    public static int red =Color.parseColor("#dd0000");
    public static int blue =Color.parseColor("#0000dd");

    public static int[] images = new int[]{R.drawable.cashicon, R.drawable.bank, R.drawable.lendresized, R.drawable.cheque, R.drawable.creditcardresized,R.drawable.food,R.drawable.electric,R.drawable.truck,R.drawable.health,R.drawable.ball
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add};

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

    public static fragment_income_show.listitemincome showadapterincome;
    public static List<income> showdataincome;

    public static fragment_income_show_scheduled.listitemincomescheduled showadapterincomesch;
    public static List<income> showdataincomesch;

    public static fragment_expense_show.listitemexpense showadapterexpense;
    public static List<expense> showdataexpense;

    public static fragment_expense_show_scheduled.listitemexpensescheduled showadapterexpensesch;
    public static List<expense> showdataexpensesch;

    /*public static fragment_income_show.listitemincome showadapterincome;
    public static List<income> showdataincome;

    public static fragment_income_show.listitemincome showadapterincome;
    public static List<income> showdataincome;*/


    public static String isdone="0";

    //----------------------income helper-------------------//

    public static EditText incfrom;
    public static EditText incnote;
    public static EditText incamount;

    public static String incaccount;
    public static String incategory;
    public static String incdate;
    public static long incdatesys;
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
    public static long incdatesys1;
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
    public static long expdatesys;
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
    public static long expdatesys1;
    public static String getExpdocument1;
    public static String expbalanceleft1;

    public static EditText exprepeatevery;
    public static EditText exprepeattime;
    public static Spinner expperiod;

    //----------------------expenselist helper-------------------//


    //----------------------expense_data_helper-----------------//

    public static int pageincome=0;

    //----------------------expense_data_helper-----------------//

    public static Double makedouble(String value){
        Double data=Double.parseDouble(value);
        return data;
    }

    public static void recallipsettings (final Activity context)
    {
        
    }

    public static void chosedate(Context context,TextView selecteddate){
        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                selecteddate.setText(sdf.format(myCalendar.getTime()));
                generator.incdatesys=myCalendar.getTimeInMillis();
                generator.incdatesys1=myCalendar.getTimeInMillis();
                generator.expdatesys=myCalendar.getTimeInMillis();
                generator.expdatesys1=myCalendar.getTimeInMillis();
            }

        };
        new DatePickerDialog(context,R.style.datepickergreen, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public static void choseaccount(Context context, TextView selectedacc, TextView changecurrency){
        Toast.makeText(context, "Loading Accounts..", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder build = new AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle);
        build.setTitle("Select Account");

        build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        List<accountobject> allaccount=new ArrayList<accountobject>();
        LayoutInflater inflater = LayoutInflater.from(context);
        View back = inflater.inflate(R.layout.dialog_search_data,null);

        ListView accountlist = back.findViewById(R.id.lvsnf);

        EditText search = back.findViewById(R.id.searchbox);

        build.setView(back);

        FirebaseFirestore fdb = FirebaseFirestore.getInstance();

        fdb.collection("account")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            allaccount.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                if(document.getId()==null){
                                    break;
                                }
                                Date c=null;
                                Object dtStart = document.getData().get("account_createdate");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                if(document.getData().get("account_status").toString().equals("1")) {
                                    accountobject object = new accountobject();
                                    object.setAccountdocument(document.getId());
                                    object.setAccountfullcurrency(document.getData().get("account_fullcurency").toString());
                                    object.setAccountname(document.getData().get("account_name").toString());
                                    object.setAccountcategory(document.getData().get("account_category").toString());
                                    object.setAccountbalance(document.getData().get("account_balance_current").toString());
                                    allaccount.add(object);
                                }
                                else {

                                }
                                Log.d("Get data account", document.getId() + " => " + document.getData());
                            }
                            myaccountlisadapter adapteraccount = new myaccountlisadapter(context,R.layout.row_layout_account,allaccount,changecurrency,selectedacc);
                            accountlist.setAdapter(adapteraccount);
                            dialogaccount=build.show();
                        } else {
                            Log.w("Get account error", "Error getting documents.", task.getException());
                        }
                    }
                });

    }
    
    public static void chosecategory(Context context,TextView passedtextview,CircleImageView image){

        final MySimpleArrayAdapter[] adapter = new MySimpleArrayAdapter[1];

        List<MyListObject> valuemyobjectlist = new ArrayList<>();
        Toast.makeText(context, "Loading Category..", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder dialog1 = new AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle)
                .setTitle("Select Category");

        LayoutInflater inflater = LayoutInflater.from(context);
        View a = inflater.inflate(R.layout.dialog_search_data,null);

        ListView list = a.findViewById(R.id.lvsnf);


        list.setDivider(null);
        list.setDividerHeight(0);

        dialog1.setView(a);

        FirebaseFirestore fdb = FirebaseFirestore.getInstance();

        fdb.collection("category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            valuemyobjectlist.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                if(document.getId()==null){
                                    break;
                                }
                                Log.e("getting data", document.getId() + " => " + document.getData());
                                MyListObject b = new MyListObject();
                                b.setCategoryname(document.getData().get("category_name").toString());
                                b.setImage(Integer.parseInt(document.getData().get("category_image").toString()));
                                b.setHiddendata(document.getId());
                                b.setCreatedate(document.getData().get("category_createdate"));
                                valuemyobjectlist.add(b);
                            }

                        } else {
                            Log.e("", "Error getting documents.", task.getException());
                        }
                        adapter[0] = new MySimpleArrayAdapter(context, R.layout.row_layout_category, valuemyobjectlist,passedtextview,image);
                        adapter[0].notifyDataSetChanged();
                        if (list.getAdapter()==null){
                            list.setAdapter(adapter[0]);
                        }

                    }
                });
        dialog1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog = dialog1.show();
    
    }

    private static class MySimpleArrayAdapter extends ArrayAdapter<MyListObject> {
        private final Context context;
        private CircleImageView image;
        private final List<MyListObject> values;
        private TextView text;

        public MySimpleArrayAdapter(Context context, int resourceID, List<MyListObject> values,TextView passedtext,CircleImageView image) {
            super(context, resourceID, values);
            this.context = context;
            this.values = values;
            text = passedtext;
            this.image=image;
        }

        private class ViewHolder {
            CircleImageView imageView;
            TextView textView;
            TextView hiddentextView;
            Object categorycreatedate;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MySimpleArrayAdapter.ViewHolder holder;
            MyListObject rowItem = getItem(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_layout_category, null);


            } else {
                holder = (MySimpleArrayAdapter.ViewHolder) convertView.getTag();
            }
            holder = new MySimpleArrayAdapter.ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.categorynameitem);
            holder.imageView =  convertView.findViewById(R.id.categoryitemimage);
            holder.hiddentextView =  convertView.findViewById(R.id.categorydatadocument);
            holder.categorycreatedate = rowItem.getCreatedate();


            holder.textView.setText(rowItem.getCategoryitem());
            Log.e("image id",String.valueOf(rowItem.getImage()));
            Drawable resImg = context.getResources().getDrawable(images[rowItem.getImage()-1]);
            holder.imageView.setImageDrawable(resImg);
            holder.imageView.setTag(rowItem.getImage());
            holder.hiddentextView.setText(rowItem.getHiddendata());

            final MySimpleArrayAdapter.ViewHolder finalHolder = holder;
            MySimpleArrayAdapter.ViewHolder finalHolder1 = holder;
            MySimpleArrayAdapter.ViewHolder finalHolder2 = holder;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image.setImageDrawable(finalHolder2.imageView.getDrawable());
                    text.setText(finalHolder2.textView.getText().toString());
                    generator.incategory=text.getText().toString();
                    dialog.dismiss();
                }
            });


            return convertView;
        }

        @Override
        public MyListObject getItem(int position) {
            return values.get(position);
        }

    }

    private static class MyListObject {
        private int image;
        private String country;
        private String hiddendata;
        private Object createdate;

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getCategoryitem() {
            return country;
        }

        public void setCategoryname(String country) {
            this.country = country;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getHiddendata() {
            return hiddendata;
        }

        public void setHiddendata(String hiddendata) {
            this.hiddendata = hiddendata;
        }

        public Object getCreatedate() {
            return createdate;
        }

        public void setCreatedate(Object createdate) {
            this.createdate = createdate;
        }
    }

    private static class myaccountlisadapter extends ArrayAdapter<accountobject> {
        private final Context context;
        private AlertDialog alert=null;
        private TextView currencies,chosentext;
        private final List<accountobject> values;
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");

        public myaccountlisadapter(Context context, int resourceID, List<accountobject> value,TextView currencychange,TextView chosentext) {
            super(context, resourceID, value);
            this.context = context;
            currencies = currencychange;
            this.values = value;
            this.chosentext=chosentext;
        }

        private class ViewHolder {
            String document;
            String balance;
            TextView accountname;
            TextView accountcategory;
            TextView accountbalance;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            myaccountlisadapter.ViewHolder holder;
            accountobject rowItem = getItem(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_layout_accountchoice, null);


            } else {
                holder = (myaccountlisadapter.ViewHolder) convertView.getTag();
            }
            holder = new myaccountlisadapter.ViewHolder();
            holder.accountname = (TextView) convertView.findViewById(R.id.accnamchoice);
            holder.accountcategory =  convertView.findViewById(R.id.acccatnamchoice);
            holder.accountbalance = convertView.findViewById(R.id.accbalval);


            holder.accountname.setText(rowItem.getAccountname());
            String string = rowItem.getAccountfullcurrency();
            String[] parts = string.split("-");
            String part1 = parts[0]; // 004
            String part2 = parts[1]; // 034556
            holder.document = rowItem.getAccountdocument();

            holder.accountbalance.setText(formatter.format(Double.parseDouble(rowItem.getAccountbalance())) +" "+ parts[0].trim());
            holder.balance=rowItem.getAccountbalance();
            if(Double.parseDouble(holder.balance)>=0){
                holder.accountbalance.setTextColor(generator.green);
            }
            else {
                holder.accountbalance.setTextColor(generator.red);
            }
            holder.accountcategory.setText("Category : "+ rowItem.getAccountcategory());


            ViewHolder finalHolder2 = holder;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currencies.setText(part2);
                    chosentext.setText(finalHolder2.accountname.getText().toString());
                    generator.incaccount=chosentext.getText().toString();
                    generator.incdocument=finalHolder2.document;
                    generator.incbalanceleft=finalHolder2.balance;
                    dialogaccount.dismiss();
                }
            });


            return convertView;
        }

        @Override
        public accountobject getItem(int position) {
            return values.get(position);
        }

    }

    private static class accountobject {
        private String accountname;
        private String accountcategory;
        private String accountbalance;
        private String accountfullcurrency;
        private String accountdocument;

        private accountobject(){
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
