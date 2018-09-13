package prima.optimasi.indonesia.primacash.recycleview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import io.realm.Realm;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.realm.implementation.RealmBarDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.joda.time.LocalDate;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmResults;
import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.SQLiteHelper;
import prima.optimasi.indonesia.primacash.formula.MyDividerItemDecoration;
import prima.optimasi.indonesia.primacash.generator;
import prima.optimasi.indonesia.primacash.objects.account;
import prima.optimasi.indonesia.primacash.objects.dataexpset;
import prima.optimasi.indonesia.primacash.objects.dataincset;
import prima.optimasi.indonesia.primacash.objects.expense;
import prima.optimasi.indonesia.primacash.objects.income;
import prima.optimasi.indonesia.primacash.objects.incomeexpense;
import prima.optimasi.indonesia.primacash.transactionactivity.listexpense;
import prima.optimasi.indonesia.primacash.transactionactivity.listincome;

public class mainactivityviews extends RecyclerView.Adapter<mainactivityviews.MyViewHolder>{

    DecimalFormat formatter= new DecimalFormat("###,###,###.00");
    Context contexts;
    LayoutInflater inflater;
    FirebaseFirestore fdb;
    SQLiteHelper dbase;
    Calendar c;
    List<String> titles;

    String[] mParties = new String[] {
            " A", " B", " C", " D", " E", " F", " G", " H",
            " I", " J", " K", " L", " M", " N", " O", " P",
            " Q", " R", " S", " T", " U", " V", " W", " X",
            " Y", " Z"
    };

    int[] images = new int[]{R.drawable.cashicon, R.drawable.bank, R.drawable.lend_resized, R.drawable.cheque, R.drawable.creditcard_resized,R.drawable.food,R.drawable.electric,R.drawable.truck,R.drawable.health,R.drawable.ball
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

    CircleImageView chosen;

    private List<account> imagesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,buttonviewoption;
        public LinearLayout layoutextra;

        public MyViewHolder(View view) {
            super(view);
            buttonviewoption = view.findViewById(R.id.mainmenuOptions);
            title =  view.findViewById(R.id.mainmenutitle);
            layoutextra = view.findViewById(R.id.mainmenulayouts);
        }
    }

    public mainactivityviews(Context context,List<String> titles) {

        dbase = new SQLiteHelper(context);

        c = Calendar.getInstance();
        fdb = FirebaseFirestore.getInstance();
        contexts=context;
        this.titles=titles;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_mainmenu, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final mainactivityviews.MyViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        if(position==0){
            holder.layoutextra.removeAllViews();
            View summary=inflater.inflate(R.layout.recycler_layout_summary_mainmenu,null);

            TextView currentmonth = summary.findViewById(R.id.summarymonth);
            TextView inc = summary.findViewById(R.id.summaryinc);
            TextView exp = summary.findViewById(R.id.summaryexp);
            TextView total = summary.findViewById(R.id.summarytotal);

            final Double[] totaling = {0.0d};

            currentmonth.setText(getMonthForInt(c.get(Calendar.MONTH))+" "+String.valueOf(c.get(Calendar.YEAR)));

            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

            // NOTE: The order of the entries when being added to the entries array determines their position around the center of
            // the chart.

            Double inctotal=0.0d;
            Double exptotal=0.0d;

            LocalDate todaydate = LocalDate.now();

            Calendar now = Calendar.getInstance();

            String startdate = now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-1";
            String enddate = now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.getActualMaximum(Calendar.DAY_OF_MONTH);

            Log.e("onBindViewHolder: ",startdate+" "+enddate );

            List<income> incomes = dbase.getAllincome();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");

            Date dt1=null;
            Date dt2= null;
            try {
                dt1 = format.parse(startdate);
                dt2 = format.parse(enddate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for(int i = 0 ; i<incomes.size();i++) {
                if (incomes.get(i).getIncome_isdone() ==1  && incomes.get(i).getIncome_isdated()==0) {
                    try {
                        if(format1.parse(incomes.get(i).getIncome_date()).after(dt1) && format1.parse(incomes.get(i).getIncome_date()).before(dt2)){
                            Log.e("onBindViewHolder: ",format1.parse(incomes.get(i).getIncome_date())+"dt1"+dt1+"dt2"+dt2 );
                            inctotal = inctotal + incomes.get(i).getIncome_amount();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            totaling[0] = totaling[0] +inctotal;
            inc.setText(formatter.format(inctotal));
            Double finalInctotal = inctotal;
            Double finalInctotal1 = inctotal;

            List<expense> expenses = dbase.getAllexpense();

            for(int i = 0 ; i<expenses.size();i++) {
                if (expenses.get(i).getexpense_isdone() ==1  && expenses.get(i).getexpense_isdated()==0) {
                    try {
                        if(format1.parse(expenses.get(i).getexpense_date()).after(dt1) && format1.parse(expenses.get(i).getexpense_date()).before(dt2)){
                            exptotal=exptotal+expenses.get(i).getexpense_amount();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            totaling[0] = totaling[0] - exptotal;
            Float total1 = exptotal.floatValue()+ finalInctotal1.floatValue();
            float incs = exptotal.floatValue()/total1;
            float exps = finalInctotal1.floatValue()/total1;

            if(totaling[0]==0.0d){
                entries.add(new PieEntry(0.5f, "E"));
                entries.add(new PieEntry(0.5f, "I"));
            }
            else{
                entries.add(new PieEntry(incs, "E"));
                entries.add(new PieEntry(exps, "I"));
            }
            exp.setText(formatter.format(exptotal));

            total.setText(formatter.format(totaling[0]));
            if(totaling[0]>=0){
                total.setTextColor(generator.green);
            }
            else {
                total.setTextColor(generator.red);
            }



            PieChart mChart;
            mChart = summary.findViewById(R.id.chartsummary);

            mChart.setUsePercentValues(true);
            mChart.getDescription().setEnabled(false);

            mChart.setDragDecelerationFrictionCoef(0.95f);

            mChart.setRotationAngle(0);
            mChart.setDrawHoleEnabled(false);
            // enable rotation of the chart by touch
            mChart.setRotationEnabled(true);
            mChart.setHighlightPerTapEnabled(true);

            // mChart.setUnit(" €");
            // mChart.setDrawUnitsInChart(true);

            // add a selection listener

            setData(2, 100,mChart,entries);

            mChart.animateY(1400, Easing.EasingOption.EaseInOutElastic);
            // mChart.spin(2000, 0, 360);

            Legend l = mChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setEnabled(false);


            holder.buttonviewoption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(contexts, holder.buttonviewoption);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.mainmenu_menuall);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.mainmenuedit:
                                    //handle menu1 click
                                    return true;
                                case R.id.mainmenureorder:
                                    //handle menu2 click
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

            holder.layoutextra.addView(summary);
        }
        else if(position==1){
            holder.layoutextra.removeAllViews();
            View account=inflater.inflate(R.layout.recycler_layout_accounts_mainmenu,null);

            RecyclerView accountlis = account.findViewById(R.id.mainmenuaccountlistview);

            View nothing = account.findViewById(R.id.nothingaccount);

            List<account> allaccount = dbase.getAllaccount();

            if(allaccount.size()==0){
                nothing.setVisibility(View.VISIBLE);
            }
            else{
                nothing.setVisibility(View.GONE);
            }

            /*if(document.getData().get("account_lastused")==null){
                temp = 1;
                dtStart = document.getData().get("account_createdate");
            }else {
                dtStart = document.getData().get("account_lastused");
            }*/

            adapterviewaccountsmenu adapter = new adapterviewaccountsmenu(contexts,allaccount);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(contexts.getApplicationContext());
            accountlis.setLayoutManager(mLayoutManager);
            accountlis.setItemAnimator(new DefaultItemAnimator());
            accountlis.addItemDecoration(new MyDividerItemDecoration(contexts, LinearLayoutManager.VERTICAL, 16));
            accountlis.setAdapter(adapter);


            holder.buttonviewoption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(contexts, holder.buttonviewoption);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.mainmenu_menuall);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.mainmenuedit:
                                    //handle menu1 click
                                    return true;
                                case R.id.mainmenureorder:
                                    //handle menu2 click
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

            holder.layoutextra.addView(account);
        }
        else if(position==2){

            holder.layoutextra.removeAllViews();
            View incomel7d=inflater.inflate(R.layout.recycler_layout_income_mainmenu,null);
            Realm mRealm = Realm.getDefaultInstance();

            incomel7d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent a = new Intent(contexts,listincome.class);
                    contexts.startActivity(a);
                }
            });

            BarChart barChart = incomel7d.findViewById(R.id.incbarchart);

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<dataincset> rows = realm.where(dataincset.class).findAll();
                    rows.deleteAllFromRealm();
                }
            });

            Calendar calendar = Calendar.getInstance();

            int thisYear = calendar.get(Calendar.YEAR);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy ");
            Calendar cal = Calendar.getInstance();

            cal.add(Calendar.DAY_OF_YEAR, -7);

            String perios="";

            List<String> datesdata = new ArrayList<>();

// loop adding one day in each iteration
           for(int i = 0; i< 7; i++){
               cal.add(Calendar.DAY_OF_YEAR, 1);

               datesdata.add(sdf.format(cal.getTime()));
                if(i==0){
                    perios=perios+sdf.format(cal.getTime())+" - ";
                }
                if(i==6){
                    perios=perios+sdf.format(cal.getTime());
                }

                System.out.println(sdf.format(cal.getTime()));
                int finalI = i;
               Realm finalMRealm = mRealm;

            }

            final int[] count = {0};
            for(int i =0 ; i<datesdata.size();i++) {
                int finalI = i;

                Realm finalMRealm1 = mRealm;
                String finalPerios = perios;


                fdb.collection("income")
                        .whereEqualTo("income_isdone", "1")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                Double value = 0.0d;
                                if (task.isSuccessful()) {
                                    count[0]++;
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Date date1 = null;
                                        Date date2 = null;

                                        try {
                                            date1=sdf.parse(document.getData().get("income_date").toString());
                                            try {
                                                date2=sdf.parse(datesdata.get(finalI));
                                                if(date1.equals(date2)){
                                                    value = value + Double.parseDouble(document.getData().get("income_amount").toString());
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }



                                }
                                    finalMRealm1.beginTransaction();
                                    dataincset score1 = new dataincset(value.floatValue(), (float) finalI, datesdata.get(finalI));
                                    //Log.e("replace", "/" + String.valueOf(thisYear) + " " + datesdata.get(finalI));
                                    finalMRealm1.copyToRealm(score1);
                                    finalMRealm1.commitTransaction();

                                    barChart.invalidate();
                                    barChart.getAxisLeft().setDrawGridLines(false);
                                    barChart.getXAxis().setDrawGridLines(false);
                                    barChart.setExtraBottomOffset(5f);

                                    barChart.getXAxis().setLabelCount(7);
                                    barChart.getXAxis().setGranularity(1f);

                                    // no description text
                                    barChart.getDescription().setEnabled(false);

                                    // enable touch gestures
                                    barChart.setTouchEnabled(true);

                                    if (barChart instanceof BarLineChartBase) {

                                        BarLineChartBase mChart = (BarLineChartBase) barChart;

                                        mChart.setDrawGridBackground(false);

                                        // enable scaling and dragging
                                        mChart.setDragEnabled(true);
                                        mChart.setScaleEnabled(true);

                                        // if disabled, scaling can be done on x- and y-axis separately
                                        mChart.setPinchZoom(false);

                                        YAxis leftAxis = mChart.getAxisLeft();
                                        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
                                        leftAxis.setTextSize(8f);
                                        leftAxis.setTextColor(Color.BLACK);

                                        XAxis xAxis = mChart.getXAxis();
                                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                        xAxis.setTextSize(8f);
                                        xAxis.setTextColor(Color.BLACK);

                                        mChart.getAxisRight().setEnabled(false);
                                    }

                                    RealmResults<dataincset> results = mRealm.where(dataincset.class).findAll();

                                    IAxisValueFormatter formatter = new IAxisValueFormatter() {
                                        @Override
                                        public String getFormattedValue(float value, AxisBase axis) {
                                            return datesdata.get((int) value).replace("/" + String.valueOf(thisYear), "");
                                        }
                                    };
                                    barChart.getAxisLeft().setValueFormatter(new LargeValueFormatter());

                                    barChart.getXAxis().setValueFormatter(formatter);

                                    // BAR-CHART
                                    RealmBarDataSet<dataincset> barDataSet = new RealmBarDataSet<dataincset>(results, "ranges", "firebaseincome");
                                    barDataSet.setColor(generator.green);
                                    barDataSet.setLabel("Income Period " + finalPerios);

                                    ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
                                    barDataSets.add(barDataSet);

                                    BarData barData = new BarData(barDataSets);

                                    barChart.setData(barData);
                                    barChart.setFitBars(true);
                                    barChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);

                                    barChart.invalidate();

                                    if(count[0]==datesdata.size()){
                                        mRealm.close();
                                    }

                                } else {
                                    Log.w("data", "Error getting documents.", task.getException());
                                }
                            }
                        });
                count[0]++;
            }

            holder.layoutextra.addView(incomel7d);
        }
        else if(position==3){

            holder.layoutextra.removeAllViews();
            View expensel7d=inflater.inflate(R.layout.recycler_layout_expense_mainmenu,null);
            Realm mRealm = Realm.getDefaultInstance();

            expensel7d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent a = new Intent(contexts,listexpense.class);
                    contexts.startActivity(a);
                }
            });

            BarChart barChart = expensel7d.findViewById(R.id.expbarChart);

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<dataexpset> rows = realm.where(dataexpset.class).findAll();
                    rows.deleteAllFromRealm();
                }
            });

            Calendar calendar = Calendar.getInstance();

            int thisYear = calendar.get(Calendar.YEAR);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy ");
            Calendar cal = Calendar.getInstance();

            cal.add(Calendar.DAY_OF_YEAR, -7);

            String perios="";

            List<String> datesdata = new ArrayList<>();

// loop adding one day in each iteration
            for(int i = 0; i< 7; i++){
                cal.add(Calendar.DAY_OF_YEAR, 1);

                datesdata.add(sdf.format(cal.getTime()));
                if(i==0){
                    perios=perios+sdf.format(cal.getTime())+" - ";
                }
                if(i==6){
                    perios=perios+sdf.format(cal.getTime());
                }

                System.out.println(sdf.format(cal.getTime()));
                int finalI = i;
                Realm finalMRealm = mRealm;

            }

            final int[] count = {0};
            for(int i =0 ; i<datesdata.size();i++) {
                int finalI = i;

                Realm finalMRealm1 = mRealm;
                String finalPerios = perios;


                fdb.collection("expense")
                        .whereEqualTo("expense_isdone", "1")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                Double value = 0.0d;
                                if (task.isSuccessful()) {
                                    count[0]++;
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Date date1 = null;
                                        Date date2 = null;

                                        try {
                                            date1=sdf.parse(document.getData().get("expense_date").toString());
                                            try {
                                                date2=sdf.parse(datesdata.get(finalI));
                                                if(date1.equals(date2)){
                                                    value = value + Double.parseDouble(document.getData().get("expense_amount").toString());
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }



                                        Log.d("Get data expense", datesdata.get(finalI) + document.getId() + " => " + document.getData());
                                    }
                                    finalMRealm1.beginTransaction();
                                    dataexpset score1 = new dataexpset(value.floatValue(), (float) finalI, datesdata.get(finalI));
                                    //Log.e("replace", "/" + String.valueOf(thisYear) + " " + datesdata.get(finalI));
                                    finalMRealm1.copyToRealm(score1);
                                    finalMRealm1.commitTransaction();

                                    barChart.invalidate();
                                    barChart.getAxisLeft().setDrawGridLines(false);
                                    barChart.getXAxis().setDrawGridLines(false);
                                    barChart.setExtraBottomOffset(5f);

                                    barChart.getXAxis().setLabelCount(7);
                                    barChart.getXAxis().setGranularity(1f);

                                    // no description text
                                    barChart.getDescription().setEnabled(false);

                                    // enable touch gestures
                                    barChart.setTouchEnabled(true);

                                    if (barChart instanceof BarLineChartBase) {

                                        BarLineChartBase mChart = (BarLineChartBase) barChart;

                                        mChart.setDrawGridBackground(false);

                                        // enable scaling and dragging
                                        mChart.setDragEnabled(true);
                                        mChart.setScaleEnabled(true);

                                        // if disabled, scaling can be done on x- and y-axis separately
                                        mChart.setPinchZoom(false);

                                        YAxis leftAxis = mChart.getAxisLeft();
                                        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
                                        leftAxis.setTextSize(8f);
                                        leftAxis.setTextColor(Color.BLACK);

                                        XAxis xAxis = mChart.getXAxis();
                                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                        xAxis.setTextSize(8f);
                                        xAxis.setTextColor(Color.BLACK);

                                        mChart.getAxisRight().setEnabled(false);
                                    }

                                    RealmResults<dataexpset> results = mRealm.where(dataexpset.class).findAll();

                                    IAxisValueFormatter formatter = new IAxisValueFormatter() {
                                        @Override
                                        public String getFormattedValue(float value, AxisBase axis) {
                                            return datesdata.get((int) value).replace("/" + String.valueOf(thisYear), "");
                                        }
                                    };
                                    barChart.getAxisLeft().setValueFormatter(new LargeValueFormatter());

                                    barChart.getXAxis().setValueFormatter(formatter);

                                    // BAR-CHART
                                    RealmBarDataSet<dataexpset> barDataSet = new RealmBarDataSet<dataexpset>(results, "ranges", "firebaseincome");
                                    barDataSet.setColor(generator.red);
                                    barDataSet.setLabel("Expense Period " + finalPerios);

                                    ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
                                    barDataSets.add(barDataSet);

                                    BarData barData = new BarData(barDataSets);

                                    barChart.setData(barData);
                                    barChart.setFitBars(true);
                                    barChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);

                                    barChart.invalidate();

                                    if(count[0]==datesdata.size()){
                                        mRealm.close();
                                    }

                                } else {
                                    Log.w("data", "Error getting documents.", task.getException());
                                }
                            }
                        });
                count[0]++;
            }

            holder.layoutextra.addView(expensel7d);

        }
        else if(position==4){
            holder.layoutextra.removeAllViews();
            View transactions = inflater.inflate(R.layout.recycler_layout_transactions_mainmenu,null);

            final adapterviewtransactionmenu[] adapter = {null};

            RecyclerView transactionlist = transactions.findViewById(R.id.mainmenutransactionrecycler);

            List<incomeexpense> alltransaction = new ArrayList<>() ;



            fdb.collection("income")
                    .whereEqualTo("income_isdated","0")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    if(document.getId()==null){
                                        break;
                                    }
                                    incomeexpense data =new incomeexpense();
                                    data.setIncome_account(document.getData().get("income_account").toString());
                                    data.setIncome_amount(generator.makedouble(document.getData().get("income_amount").toString()));
                                    data.setIncome_category(document.getData().get("income_category").toString());
                                    data.setIncome_createdate(document.getData().get("income_createdate").toString());
                                    data.setDatedata(document.getData().get("income_date").toString());
                                    data.setIncome_date(document.getData().get("income_date").toString());


                                    fdb.collection("category")
                                            .whereEqualTo("category_name", document.getData().get("income_category").toString())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document2 : task1.getResult()) {
                                                            Log.e("category datas", document.getId() + " => " + document.getData());
                                                            data.setIncome_image(Integer.parseInt(document2.getData().get("category_image").toString()));



                                                            data.setIncomedoc(document.getId());
                                                            data.setIncome_from(document.getData().get("income_from").toString());
                                                            data.setIncome_type(document.getData().get("income_type").toString());
                                                            data.setIncome_notes(document.getData().get("income_notes").toString());

                                                            alltransaction.add(data);

                                                            if(adapter[0] !=null){
                                                                adapter[0].notifyDataSetChanged();
                                                            }

                                                        }
                                                    } else {
                                                        Log.d("data", "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });







                                    Log.d("Get data account", document.getId() + " => " + document.getData());
                                }
                                fdb.collection("expense")
                                        .whereEqualTo("expense_isdated","0")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (DocumentSnapshot document1 : task.getResult()) {
                                                        if(document1.getId()==null){
                                                            break;
                                                        }
                                                        incomeexpense data1 =new incomeexpense();
                                                        data1.setExpense_account(document1.getData().get("expense_account").toString());
                                                        data1.setExpense_amount(generator.makedouble(document1.getData().get("expense_amount").toString()));
                                                        data1.setExpense_category(document1.getData().get("expense_category").toString());
                                                        data1.setExpense_createdate(document1.getData().get("expense_createdate").toString());
                                                        data1.setDatedata(document1.getData().get("expense_date").toString());
                                                        data1.setExpense_date(document1.getData().get("expense_date").toString());


                                                        fdb.collection("category")
                                                                .whereEqualTo("category_name", document1.getData().get("expense_category").toString())
                                                                .get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot document2 : task1.getResult()) {
                                                                                Log.e("category datas", document1.getId() + " => " + document1.getData());
                                                                                data1.setExpense_image(Integer.parseInt(document2.getData().get("category_image").toString()));


                                                                                data1.setExpensedoc(document1.getId());
                                                                                data1.setExpense_to(document1.getData().get("expense_to").toString());
                                                                                data1.setExpense_type(document1.getData().get("expense_type").toString());
                                                                                data1.setExpense_notes(document1.getData().get("expense_notes").toString());

                                                                                alltransaction.add(data1);

                                                                                if(adapter[0] !=null){
                                                                                    adapter[0].notifyDataSetChanged();
                                                                                }

                                                                            }
                                                                            Collections.sort(alltransaction);
                                                                            Collections.reverse(alltransaction);

                                                                        } else {
                                                                            Log.d("data", "Error getting documents: ", task.getException());
                                                                        }
                                                                    }
                                                                });



                                                        Log.d("Get data account", document1.getId() + " => " + document1.getData());
                                                    }
                                                    adapter[0] = new adapterviewtransactionmenu(contexts,alltransaction);
                                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(contexts.getApplicationContext());
                                                    transactionlist.setLayoutManager(mLayoutManager);
                                                    transactionlist.setItemAnimator(new DefaultItemAnimator());
                                                    transactionlist.addItemDecoration(new MyDividerItemDecoration(contexts, LinearLayoutManager.VERTICAL, 16));
                                                    transactionlist.setAdapter(adapter[0]);
                                                } else {
                                                    Log.w("Get account error", "Error getting documents.", task.getException());
                                                }
                                            }
                                        });

                            } else {
                                Log.w("Get account error", "Error getting documents.", task.getException());
                            }
                        }
                    });


            holder.buttonviewoption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(contexts, holder.buttonviewoption);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.mainmenu_menuall);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.mainmenuedit:
                                    //handle menu1 click
                                    return true;
                                case R.id.mainmenureorder:
                                    //handle menu2 click
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



            holder.layoutextra.addView(transactions);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private void setData(int count, float range,PieChart mChart,ArrayList<PieEntry> entries) {

        float mult = range;


            Log.d("calculation", "setData: ");

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(generator.red);

        colors.add(generator.green);

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setUsingSliceColorAsValueLineColor(true);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    public String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    private void setup(Chart<?> chart) {

        Typeface mTf;
        mTf = Typeface.createFromAsset(contexts.getAssets(), "OpenSans-Regular.ttf");

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        if (chart instanceof BarLineChartBase) {

            BarLineChartBase mChart = (BarLineChartBase) chart;

            mChart.setDrawGridBackground(false);

            // enable scaling and dragging
            mChart.setDragEnabled(true);
            mChart.setScaleEnabled(true);

            // if disabled, scaling can be done on x- and y-axis separately
            mChart.setPinchZoom(false);

            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
            leftAxis.setTypeface(mTf);
            leftAxis.setTextSize(8f);
            leftAxis.setTextColor(Color.DKGRAY);
            leftAxis.setValueFormatter(new PercentFormatter());

            XAxis xAxis = mChart.getXAxis();
            xAxis.setTypeface(mTf);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(8f);
            xAxis.setTextColor(Color.DKGRAY);

            mChart.getAxisRight().setEnabled(false);
        }
    }

}

