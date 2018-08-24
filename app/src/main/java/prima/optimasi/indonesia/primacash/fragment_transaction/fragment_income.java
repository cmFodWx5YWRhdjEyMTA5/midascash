package prima.optimasi.indonesia.primacash.fragment_transaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.commaedittext;
import prima.optimasi.indonesia.primacash.formula.calculatordialog;
import prima.optimasi.indonesia.primacash.generator;

public class fragment_income extends Fragment {

    LayoutInflater inflater;
    LinearLayout contain;

    TextView datetext;

    EditText fromtxt,notestxt;

    Calendar myCalendar = Calendar.getInstance();

    long datesys;

    String amountdata="",accountdata="",categorydata="",typedata="",datedata="",fromdata="",notesdata="",repeattime="",repeatperiod="",repeatcount="";

    MySimpleArrayAdapter adapter;
    myaccountlisadapter adapteraccount;

    AlertDialog dialog;//category
    AlertDialog dialogaccount;

    FirebaseFirestore fdb;

    ImageView viewcat;
    TextView categorytext,accounttext;

    List<MyListObject> valuemyobjectlist;
    List<accountobject> valuemyaccountobject;

    Map<String,Object> mapdata = new HashMap<>();

    LinearLayout accountchoice,categorychoice,datechoice;

    List<String> documents = new ArrayList<>();

    TextInputLayout editformcat;

    EditText editfrom;

    calculatordialog calculatorchoice;


    public void check(Context con){
        amountdata="";
        accountdata="";
        categorydata="";
        typedata="";
        datedata="";
        datesys=0L;
        fromdata="";
        notesdata="";
    }
    public fragment_income(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        clearvalues();
        LinearLayout accountselect,categoryselect,dateselect;
        TextView changedcurrency;

        fdb = FirebaseFirestore.getInstance();
        valuemyobjectlist = new ArrayList<MyListObject>();
        valuemyaccountobject = new ArrayList<accountobject>();

        View view = inflater.inflate(R.layout.fragment_income, container, false);
        contain = view.findViewById(R.id.llincome);



        View child = inflater.inflate(R.layout.layout_transactionincome, null);

        viewcat = child.findViewById(R.id.inccatpic);
        categorytext = child.findViewById(R.id.inccatname);

        accounttext = child.findViewById(R.id.incacctxt);

        generator.incfrom = child.findViewById(R.id.incfromtxt);
        generator.incnote = child.findViewById(R.id.incnotetxt);

        changedcurrency = child.findViewById(R.id.allcurrency);
        categoryselect=child.findViewById(R.id.inccattap);
        accountselect = child.findViewById(R.id.incacctap);
        dateselect = child.findViewById(R.id.incdatetap);

        invokelistenerforlinears(changedcurrency,categoryselect,accountselect,dateselect);

        generator.incamount = child.findViewById(R.id.input_value);
        generator.incamount.setSelected(false);

        generator.incamount.addTextChangedListener(new commaedittext(generator.incamount));

        editformcat = child.findViewById(R.id.input_valuecatch);
        editformcat.setSelected(false);


        ImageView calc = child.findViewById(R.id.inccalc);



        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TypedValue typedValue = new TypedValue();
                getActivity().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
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

                calculatorchoice = new calculatordialog(getActivity(),generator.incamount,myList);
                calculatorchoice.showcalculatordialog();
            }
        });



        contain.addView(child);

        // Inflate the layout for this fragment
        return view;
    }

    public void calculatebalance(){
        generator.incbalanceleft=String.valueOf(Double.parseDouble(generator.incbalanceleft.replace(",",""))+Double.parseDouble(generator.incamount.getText().toString().replace(",","")));
    }

    public Boolean issaveable(Context con){
        if(accountdata==null){
            accountdata="";
        }
        if(categorydata==null){
            categorydata="";
        }
            if (!amountdata.equals("")){
                if(!categorydata.equals("")){
                    if(!typedata.equals("")){
                        if(!datedata.equals("")){
                            if( !accountdata.equals("")) {
                                return true;
                            }     else {
                                Toast.makeText(con, "Please Select Account", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }    else {
                            Toast.makeText(con, "Please Select date", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }     else {
                        Toast.makeText(con, "Type error", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }  else {
                    Toast.makeText(con, "Please Select Category", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(con, "Value Field Required", Toast.LENGTH_SHORT).show();
                return false;
            }
    }
    public void writeobjects(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c);

        if(typedata.equals("")|| typedata==null) {
            typedata = "D";
        }

        datedata=generator.incdate;

        datesys=generator.incdatesys;

        notesdata=generator.incnote.getText().toString();

        fromdata=generator.incfrom.getText().toString();
        amountdata = generator.incamount.getText().toString().replace(",","");
        accountdata=generator.incaccount;
        categorydata=generator.incategory;

        mapdata.put("income_createdate",c);
        mapdata.put("income_amount",amountdata);
        mapdata.put("income_account",accountdata);
        mapdata.put("income_type",typedata);
        mapdata.put("income_category",categorydata);
        mapdata.put("income_notes",notesdata);
        mapdata.put("income_date",datedata);
        mapdata.put("income_isdated","0");
        mapdata.put("income_datesys",datesys);
        mapdata.put("income_from",fromdata);
        mapdata.put("income_repeat_time",repeattime);
        mapdata.put("income_repeat_period",repeatperiod);
        mapdata.put("income_repeat_count",repeatcount);
        mapdata.put("income_isdone",generator.isdone);
        mapdata.put("username", generator.userlogin);
    }

    public Map<String,Object> getthemap(){
        return mapdata;
    }

    private void invokelistenerforlinears(TextView changecurrency,LinearLayout categorylist,LinearLayout account,LinearLayout dateselect){

        datetext = dateselect.findViewById(R.id.incdateselect);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        final String date = df.format(Calendar.getInstance().getTime());

        generator.incdate= date;
        generator.incdatesys= Calendar.getInstance().getTimeInMillis();

        datetext.setText(date);

        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };

        dateselect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new DatePickerDialog(getActivity(),R.style.datepickergreen, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




        categorylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Loading Category..", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder dialog1 = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle)
                        .setTitle("Select Category");

                inflater = getActivity().getLayoutInflater();
                View a = inflater.inflate(R.layout.dialog_search_data,null);

                ListView list = a.findViewById(R.id.lvsnf);


                list.setDivider(null);
                list.setDividerHeight(0);

                dialog1.setView(a);

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
                                adapter = new MySimpleArrayAdapter(getActivity(), R.layout.row_layout_category, valuemyobjectlist);
                                adapter.notifyDataSetChanged();
                                if (list.getAdapter()==null){
                                    list.setAdapter(adapter);
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
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Loading Accounts..", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder build = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);
                build.setTitle("Select Account");

                build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                List<accountobject> allaccount=new ArrayList<accountobject>();
                inflater = getActivity().getLayoutInflater();
                View back = inflater.inflate(R.layout.dialog_search_data,null);

                ListView accountlist = back.findViewById(R.id.lvsnf);

                EditText search = back.findViewById(R.id.searchbox);

                build.setView(back);



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
                                    adapteraccount = new myaccountlisadapter(getActivity(),R.layout.row_layout_account,allaccount,changecurrency);
                                    accountlist.setAdapter(adapteraccount);
                                    dialogaccount=build.show();
                                } else {
                                    Log.w("Get account error", "Error getting documents.", task.getException());
                                }
                            }
                        });

            }
        });

    }

    private class MySimpleArrayAdapter extends ArrayAdapter<MyListObject> {
        private final Context context;
        private final List<MyListObject> values;

        public MySimpleArrayAdapter(Context context, int resourceID, List<MyListObject> values) {
            super(context, resourceID, values);
            this.context = context;
            this.values = values;
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
            Drawable resImg = context.getResources().getDrawable(generator.images[rowItem.getImage()-1]);
            holder.imageView.setImageDrawable(resImg);
            holder.imageView.setTag(rowItem.getImage());
            holder.hiddentextView.setText(rowItem.getHiddendata());

            final MySimpleArrayAdapter.ViewHolder finalHolder = holder;
            MySimpleArrayAdapter.ViewHolder finalHolder1 = holder;
            ViewHolder finalHolder2 = holder;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewcat.setImageDrawable(finalHolder2.imageView.getDrawable());
                    categorytext.setText(finalHolder2.textView.getText().toString());
                    generator.incategory=categorytext.getText().toString();
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

    private class MyListObject {
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

    private class myaccountlisadapter extends ArrayAdapter<accountobject> {
        private final Context context;
        private AlertDialog alert=null;
        private TextView currencies;
        private final List<accountobject> values;
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");

        public myaccountlisadapter(Context context, int resourceID, List<accountobject> value,TextView currencychange) {
            super(context, resourceID, value);
            this.context = context;
            currencies = currencychange;
            this.values = value;
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
                    accounttext.setText(finalHolder2.accountname.getText().toString());
                    generator.incaccount=accounttext.getText().toString();
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

    private class accountobject {
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

    private void updateLabel1() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        datetext.setText(sdf.format(myCalendar.getTime()));
        generator.incdate=sdf.format(myCalendar.getTime());
        generator.incdatesys=myCalendar.getTimeInMillis();
    }

    public void clearvalues(){
        try {
            if (generator.incfrom.getText().toString() != null)
                generator.incfrom.setText("");
            if (generator.incamount.getText().toString() != null)
                generator.incamount.setText("");
            if (generator.incnote.getText().toString() != null)
                generator.incnote.setText("");
            if (generator.incaccount != null)
                generator.incaccount = "";
            if (generator.incategory != null)
                generator.incategory = "";
            if (generator.incdate != null)
                generator.incdate = "";
            if (generator.incdatesys != 0L)
                generator.incdatesys = 0L;
            if (generator.incbalanceleft != null)
                generator.incbalanceleft = "";
        }catch (Exception e){
            Log.e("Error clear",e.getMessage());
        }
    }

}
