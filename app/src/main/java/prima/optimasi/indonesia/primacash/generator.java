package prima.optimasi.indonesia.primacash;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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
import prima.optimasi.indonesia.primacash.formula.calculatordialog;
import prima.optimasi.indonesia.primacash.fragment_transaction.fragment_expense_show;
import prima.optimasi.indonesia.primacash.fragment_transaction.fragment_expense_show_scheduled;
import prima.optimasi.indonesia.primacash.fragment_transaction.fragment_income;
import prima.optimasi.indonesia.primacash.fragment_transaction.fragment_income_show;
import prima.optimasi.indonesia.primacash.fragment_transaction.fragment_income_show_scheduled;
import prima.optimasi.indonesia.primacash.listview.accountlistview;
import prima.optimasi.indonesia.primacash.listview.categorylistview;
import prima.optimasi.indonesia.primacash.objects.account;
import prima.optimasi.indonesia.primacash.objects.category;
import prima.optimasi.indonesia.primacash.objects.expense;
import prima.optimasi.indonesia.primacash.objects.income;
import prima.optimasi.indonesia.primacash.objects.transfer;
import prima.optimasi.indonesia.primacash.recycleview.mainactivityviews;
import prima.optimasi.indonesia.primacash.transactionactivity.listexpense;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rwina on 3/26/2018.
 */

public class generator {
    public static  boolean isdashboard = true;
    public static int issignedin=0;

    public static int activitychecker=0;

    public static GoogleSignInAccount acct = null;

    //Google sign in data


    //Firebase Phone AUTHentication data
    public static PhoneAuthProvider.ForceResendingToken token = null;
    public static String verificationid = null;


    private static AlertDialog dialog;
    private static AlertDialog dialogaccount;
    private static Calendar myCalendar;

    public static int mainmenurefresh=0;

    public static String newaccountrf = "";
    public static String newaccountrfsymbol = "";

    public static  List<String> accorcat ;

    public static DecimalFormat formatter = new DecimalFormat("###,###,###.00");

    public static mainactivityviews adapter;

    public static int green =Color.parseColor("#00dd00");
    public static int red =Color.parseColor("#dd0000");
    public static int blue =Color.parseColor("#0000dd");

    public static int[] images = new int[]{
            R.drawable.cashicon_resized
            , R.drawable.bank_resized
            , R.drawable.lend_resized
            , R.drawable.cheque_resized
            , R.drawable.creditcard_resized
            , R.drawable.food_resized
            , R.drawable.electric_resized
            , R.drawable.truck_resized
            , R.drawable.health_resized
            , R.drawable.ball_resized
            , R.drawable.house_resized
            , R.drawable.water_resized
            , R.drawable.clothes_resized
            , R.drawable.movie_resized
            , R.drawable.poker_resized
            , R.drawable.car_resized
            , R.drawable.accident_resized
            , R.drawable.daily_resized
            , R.drawable.tax_resized
            , R.drawable.pet_resized
            , R.drawable.computer_resized
            , R.drawable.plane_resized
            , R.drawable.gasoline_resized
            , R.drawable.garden_resized
            , R.drawable.bitcoin_resized
            , R.drawable.insurance_resized
            , R.drawable.investment_resized
            , R.drawable.fixing_resized
            , R.drawable.medical_resized
            , R.drawable.drink_resized
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.no
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

    public static String defaultcurrency="$";
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
        new DatePickerDialog(context,R.style.datepickerorange, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
        List<account> allaccount=new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(context);
        View back = inflater.inflate(R.layout.dialog_search_data,null);

        ListView accountlist = back.findViewById(R.id.lvsnf);

        EditText search = back.findViewById(R.id.searchbox);

        build.setView(back);

        FirebaseFirestore fdb = FirebaseFirestore.getInstance();

        SQLiteHelper dbase = new SQLiteHelper(context);

        allaccount.clear();

        allaccount = dbase.getAllaccount();


        myaccountlisadapter adapteraccount = new myaccountlisadapter(context,R.layout.row_layout_account,allaccount,changecurrency,selectedacc);
        accountlist.setAdapter(adapteraccount);
        dialogaccount=build.show();

    }

    public static void choseaccount1(Context context, TextView selectedacc, TextView changecurrency,TextView changecurrency1){
        Toast.makeText(context, "Loading Accounts..", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder build = new AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle);
        build.setTitle("Select Account");

        build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        List<account> allaccount=new ArrayList<account>();
        LayoutInflater inflater = LayoutInflater.from(context);
        View back = inflater.inflate(R.layout.dialog_search_data,null);

        ListView accountlist = back.findViewById(R.id.lvsnf);

        EditText search = back.findViewById(R.id.searchbox);

        build.setView(back);

        FirebaseFirestore fdb = FirebaseFirestore.getInstance();

        SQLiteHelper dbase = new SQLiteHelper(context);

        allaccount.clear();

        allaccount = dbase.getAllaccount();

        myaccountlisadapter adapteraccount = new myaccountlisadapter(context,R.layout.row_layout_account,allaccount,changecurrency,selectedacc,changecurrency1);
        accountlist.setAdapter(adapteraccount);
        dialogaccount=build.show();

    }

    
    public static void chosecategory(Context context,TextView passedtextview,CircleImageView image){

        final MySimpleArrayAdapter[] adapter = new MySimpleArrayAdapter[1];

        List<category> valuemyobjectlist = new ArrayList<>();
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

        SQLiteHelper dbase = new SQLiteHelper(context);

        valuemyobjectlist.clear();
        valuemyobjectlist = dbase.getAllcategory();

        adapter[0] = new MySimpleArrayAdapter(context, R.layout.row_layout_category, valuemyobjectlist,passedtextview,image);
        adapter[0].notifyDataSetChanged();
        if (list.getAdapter()==null){
            list.setAdapter(adapter[0]);
        }

        dialog1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog = dialog1.show();
    
    }

    private static class MySimpleArrayAdapter extends ArrayAdapter<category> {
        private final Context context;
        private CircleImageView image;
        private final List<category> values;
        private TextView text;

        public MySimpleArrayAdapter(Context context, int resourceID, List<category> values,TextView passedtext,CircleImageView image) {
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
           category rowItem = getItem(position);

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
            holder.categorycreatedate = rowItem.getCategory_createdate();


            holder.textView.setText(rowItem.getCategory_name());
            Log.e("image id",String.valueOf(rowItem.getCategory_image()));
            Drawable resImg = context.getResources().getDrawable(images[rowItem.getCategory_image()-1]);
            holder.imageView.setImageDrawable(resImg);
            holder.imageView.setTag(rowItem.getCategory_image());

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
        public category getItem(int position) {
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

    private static class myaccountlisadapter extends ArrayAdapter<account> {
        private final Context context;
        private AlertDialog alert=null;
        private TextView currencies,chosentext,chosentext1;
        private final List<account> values;
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");

        public myaccountlisadapter(Context context, int resourceID, List<account> value,TextView currencychange,TextView chosentext) {
            super(context, resourceID, value);
            this.context = context;
            currencies = currencychange;
            this.values = value;
            this.chosentext=chosentext;
        }
        public myaccountlisadapter(Context context, int resourceID, List<account> value,TextView currencychange,TextView chosentext,TextView chose) {
            super(context, resourceID, value);
            this.context = context;
            currencies = currencychange;
            this.values = value;
            this.chosentext=chosentext;
            chosentext1=chose;
        }

        private class ViewHolder {
            String incomeid;
            String balance;
            TextView accountname;
            TextView accountcategory;
            TextView accountbalance;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            myaccountlisadapter.ViewHolder holder;
            account rowItem = getItem(position);

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


            holder.accountname.setText(rowItem.getAccount_name());
            String string = rowItem.getFullaccount_currency();
            String[] parts = string.split("-");
            String part1 = parts[0]; // 004
            String part2 = parts[1]; // 034556

            holder.accountbalance.setText(formatter.format(Double.parseDouble(rowItem.getAccount_balance_current())) +" "+ parts[0].trim());
            holder.balance=rowItem.getAccount_balance();
            if(Double.parseDouble(holder.balance)>=0){
                holder.accountbalance.setTextColor(generator.green);
            }
            else {
                holder.accountbalance.setTextColor(generator.red);
            }
            holder.accountcategory.setText("Category : "+ rowItem.getAccount_category());


            ViewHolder finalHolder2 = holder;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(chosentext1!=null){
                        chosentext1.setText(part2);
                    }
                    currencies.setText(part2);
                    chosentext.setText(finalHolder2.accountname.getText().toString());
                    generator.incaccount=chosentext.getText().toString();
                    generator.incbalanceleft=finalHolder2.balance;
                    dialogaccount.dismiss();
                }
            });


            return convertView;
        }

        @Override
        public account getItem(int position) {
            return values.get(position);
        }

    }

    
    public static void newtransfer(Context context){

        SQLiteHelper dbase = new SQLiteHelper(context);



        FirebaseFirestore db = FirebaseFirestore.getInstance();

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



        LayoutInflater inflater = LayoutInflater.from(context);

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

        inputrate.addTextChangedListener(new commaedittext(inputrate));
        trfvalue.addTextChangedListener(new commaedittext(trfvalue));

        ImageView calc = layout.findViewById(R.id.transcalc);

        chosendate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TypedValue typedValue = new TypedValue();
                context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
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

                calculatordialog calculatorchoice = new calculatordialog(context, inputrate, myList);
                calculatorchoice.showcalculatordialog();
            }
        });


        currdef.setText(generator.defaultcurrency);

        choseacc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                   account acc = dbase.getaccount(choseacc.getSelectedItem().toString());

                   String[] parts = acc.getFullaccount_currency().split("-");
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
               @Override
               public void onNothingSelected(AdapterView<?> adapterView) {

               }
           }
        );






        chosendate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generator.chosedate(context,chosendate);
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

        List<account> acclis = dbase.getAllaccount();

        account.clear();

        for (int i=0;i<acclis.size();i++) {

            account.add(acclis.get(i).getAccount_name());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, account);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choseacc.setAdapter(spinnerArrayAdapter);
        if(currdef.getText().toString().equals(currchosen.getText().toString().replace(" ",""))){
            inputrate.setText("1.00");
            inputrate.setEnabled(false);
        }
        else {
            inputrate.setEnabled(true);
        }

        chosenacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generator.choseaccount1(context,chosenacc,allcurrencyselected,currchosen);
                if(currdef.getText().toString().equals(currchosen.getText().toString().replace(" ",""))){
                    inputrate.setText("1.00");
                    inputrate.setEnabled(false);
                }
                else {
                    inputrate.setEnabled(true);
                }
            }
        });

        if(generator.newaccountrf.equals("")){
            chosenacc.setTextColor(Color.BLUE);
        }
        else {
            chosenacc.setText(generator.newaccountrf);
            allcurrencyselected.setText(newaccountrfsymbol);
            currchosen.setText(newaccountrfsymbol);
            generator.newaccountrf="";
            generator.newaccountrfsymbol="";
        }


        AlertDialog build = new AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle).setNegativeButton("Cancel",null).setPositiveButton("Save", null).setTitle("Transfer").create();

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
                            Toast.makeText(context, "Input Transfer Amount", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (chosenacc.getText().toString().equals("Account")) {
                                Toast.makeText(context, "Select Source Account by tapping Account Text", Toast.LENGTH_SHORT).show();
                            } else {
                                if (choseacc.getSelectedItem().toString().equals(chosenacc.getText().toString())) {
                                    Toast.makeText(context, "Transfer source and destination can't be same", Toast.LENGTH_SHORT).show();
                                } else {

                                    account acc = dbase.getaccount(chosenacc.getText().toString());

                                    Double check = generator.makedouble(acc.getAccount_balance_current());

                                    if((generator.makedouble(inputrate.getText().toString().replace(",",""))*generator.makedouble(trfvalue.getText().toString().replace(",","")))>check){
                                        Toast.makeText(context, "Your Account Have insufficient Balance", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        //Dismiss once everything is OK.

                                        transfer trf = new transfer();


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

                                        trf.setTransfer_amount(generator.makedouble(trfvalue.getText().toString().replace(",","")));
                                        trf.setTransfer_rate(generator.makedouble(inputrate.getText().toString().replace(",","")));
                                        trf.setTransfer_dest(choseacc.getSelectedItem().toString());
                                        trf.setTransfer_src(chosenacc.getText().toString());
                                        trf.setTransfer_notes(notesdata.getText().toString());
                                        trf.setTransfer_date(chosendate.getText().toString());
                                        trf.setTransfer_isdone(Integer.parseInt(temp));
                                        // mapdata.put("transfer_repeat_time",repeattimedata);
                                        // mapdata.put("transfer_repeat_period",repeatperioddata);
                                        //  mapdata.put("transfer_repeat_count",repeatcountdata);
                                        trf.setUsernametrf(generator.userlogin);

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

                                            account acc1 = dbase.getaccount(choseacc.getSelectedItem().toString());

                                            Double source =  Double.parseDouble(acc.getAccount_balance_current());
                                            Double destination =  Double.parseDouble(acc1.getAccount_balance_current());

                                            Double rate = Double.parseDouble(inputrate.getText().toString().replace(",",""))*Double.parseDouble(trfvalue.getText().toString().replace(",",""));

                                            acc.setAccount_balance_current(String.valueOf(source-Double.parseDouble(trfvalue.getText().toString().replace(",",""))));
                                            dbase.updateaccount(acc,generator.userlogin,acc.getAccount_name());

                                            acc1.setAccount_balance_current(String.valueOf(destination+rate));
                                            dbase.updateaccount(acc1,generator.userlogin,acc1.getAccount_name());

                                            if(generator.adapter!=null){
                                                generator.adapter.notifyDataSetChanged();
                                            }
                                            build.dismiss();



                                            Toast.makeText(context, "Transfer Data Added", Toast.LENGTH_SHORT).show();

                                        }
                                        else {
                                            Log.e("date is after", "not same");
                                            if (generator.adapter != null) {
                                                generator.adapter.notifyDataSetChanged();
                                            }

                                            build.dismiss();
                                            Toast.makeText(context, "Transfer Data Added", Toast.LENGTH_SHORT).show();
                                        }

                                        dbase.createtransfer(trf,generator.userlogin);
                                    }
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

    }

    public static void reportselectcategory(Context context,List<String> data,List<Integer> image,TextView text){

        View layout = LayoutInflater.from(context).inflate(R.layout.dialog_listview,null);

        categorylistview adapter =new categorylistview(context,0,data,image,text);

        ListView list = layout.findViewById(R.id.listview);

        AlertDialog dialog = new AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle).setPositiveButton("Done",null).setPositiveButton("Save",null).setNegativeButton("Cancel",null).setNeutralButton("Clear All",null).setView(layout).setTitle("Select Category").create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String> datatext = new ArrayList<>();
                        datatext = adapter.getSelected();


                        if(datatext.size()==0){
                            text.setText("No Category Selected");
                        }
                        else {
                            for(int i=0;i<datatext.size();i++){
                                if(i==0){
                                    text.setText("");
                                    text.setText(datatext.get(i));
                                }else {
                                    text.setText(text.getText().toString()+", "+datatext.get(i));
                                }

                            }
                            generator.accorcat = datatext ;
                        }
                        dialog.dismiss();
                    }
                });

                Button button1 = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button button2 = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i=0; i<list.getChildCount();i++)
                        {
                            CheckBox cb = (CheckBox)list.getChildAt(i).findViewById(R.id.checkedcategory);
                            cb.setChecked(false);
                            adapter.setclear();
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        dialog.show();


        list.setAdapter(adapter);
    }

    public static void reportselectaccount(Context context,List<String> data,List<Double> balance,TextView text,List<String> currency){

        View layout = LayoutInflater.from(context).inflate(R.layout.dialog_listview,null);

        accountlistview adapter =new accountlistview(context,0,data,balance,text,currency);

        ListView list = layout.findViewById(R.id.listview);

        AlertDialog dialog = new AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle).setPositiveButton("Done",null).setPositiveButton("Save",null).setNegativeButton("Cancel",null).setNeutralButton("Clear All",null).setView(layout).setTitle("Select Category").create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String> datatext = new ArrayList<>();
                        datatext = adapter.getSelected();


                        if(datatext.size()==0){
                            text.setText("No Account Selected");
                        }
                        else {
                            for(int i=0;i<datatext.size();i++){
                                if(i==0){
                                    text.setText("");
                                    text.setText(datatext.get(i));
                                }else {
                                    text.setText(text.getText().toString()+", "+datatext.get(i));
                                }

                            }
                            generator.accorcat = datatext ;
                        }
                        dialog.dismiss();
                    }
                });

                Button button1 = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button button2 = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i=0; i<list.getChildCount();i++)
                        {
                            CheckBox cb = (CheckBox)list.getChildAt(i).findViewById(R.id.checkedcategory);
                            if(cb.isChecked()){
                                cb.setChecked(false);
                            }
                            adapter.setclear();
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        dialog.show();


        list.setAdapter(adapter);
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

    public static void refreshbalance(String accname){
        // refresh balance method
    }
}
