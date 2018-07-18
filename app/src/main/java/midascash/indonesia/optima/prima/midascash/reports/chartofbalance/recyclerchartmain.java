package midascash.indonesia.optima.prima.midascash.reports.chartofbalance;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.objects.account;
import midascash.indonesia.optima.prima.midascash.objects.incomeexpensetransfer;

public class recyclerchartmain extends RecyclerView.Adapter<recyclerchartmain.MyViewHolder> {

    DecimalFormat formatter= new DecimalFormat("###,###,###.00");

    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    Context contexts;
    int iscat=0;
    FirebaseFirestore fdb;
    int datediff=0;
    int isown=0;

    Date date1 = null;
    Date date2 = null;

    private List<String> acclis;
    private List<incomeexpensetransfer> transactionlis;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView databalance,dataacc,datacred,datadebt,datatotal;
        public RecyclerView recycler;

        public MyViewHolder(View view) {
            super(view);
            recycler = view.findViewById(R.id.chartrecycleritem);
            databalance = view.findViewById(R.id.chartbalance);
            dataacc = view.findViewById(R.id.chartacc);
            datacred = view.findViewById(R.id.chartcredit);
            datadebt = view.findViewById(R.id.chartdebit);
            datatotal = view.findViewById(R.id.charttotal);
        }
    }

    public recyclerchartmain(Context context, List<String> acc, int category , int isown, int datediff, Date dt1 , Date dt2) {
        acclis = acc;
        contexts=context;
        fdb = FirebaseFirestore.getInstance();
        this.isown = isown;
        iscat = category;
        this.datediff=datediff;
        date1 = dt1;
        date2 = dt2;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_layout_chartofbalance, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final recyclerchartmain.MyViewHolder holder, int position) {
        transactionlis.clear();
        recyclerchartsub1 adapter=new recyclerchartsub1(contexts,transactionlis);

        fdb.collection("account")
                .whereEqualTo("account_name",acclis.get(position))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                holder.dataacc.setText(document.getData().get("account_name").toString());
                                holder.databalance.setText(formatter.format(document.getData().get("account_balance").toString()));
                            }
                        } else {
                        Log.w("Get account error", "Error getting documents.", task.getException());
                        }
                    }
                });

        holder.dataacc.setText("");

        charttask task = new charttask(contexts,R.style.AppCompatAlertDialogStyle,acclis.get(position),adapter,holder.datadebt,holder.datacred,holder.datatotal);
        task.execute("");

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(contexts, 1);
        holder.recycler.setLayoutManager(mLayoutManager);
        holder.recycler.setItemAnimator(new DefaultItemAnimator());
        holder.recycler.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return acclis.size();
    }

    private class charttask extends AsyncTask<String, Integer, String> {

        private ProgressDialog dialog;
        private String account="";
        private int count=0;
        private TextView debit,credit,total;
        private Double countcredit =0.0d,countdebit =0.0d,counttotal =0.0d;
        private recyclerchartsub1 adapter;
        private Boolean issuccess=false;

        private Context context;

        private String erro="";
        public charttask(Context context, int resource, String account, recyclerchartsub1 adap, TextView debt , TextView cred,TextView tota) {
            this.dialog = new ProgressDialog(context,resource).show(context,"Loading Report","Please wait while your report Loads");
            countcredit = 0.0d;
            countdebit =0.0d;
            counttotal =0.0d;
            debit = debt;
            count = 0 ;
            credit=cred;
            total = tota;
            adapter = adap;
            this.account = account;
            dialog.setCancelable(false);

            transactionlis = new ArrayList<>();
        }

        // Do the task in background/non UI thread
        protected String doInBackground(String...tasks){

            if(iscat == 0){
                    fdb.collection("income")
                            .whereEqualTo("income_account",account)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            try {
                                                if(datediff==0){
                                                    if(df.parse(document.getData().get("income_date").toString()).equals(date1)){

                                                        Log.e("income datas", document.getId() + " => " + document.getData());

                                                        incomeexpensetransfer item = new incomeexpensetransfer();
                                                        item.setIncomedoc(document.getId());
                                                        item.setIncome_account(document.getData().get("income_account").toString());
                                                        item.setIncome_amount(Double.parseDouble(document.getData().get("income_amount").toString()));
                                                        item.setIncome_category(document.getData().get("income_category").toString());
                                                        item.setIncome_date(document.getData().get("income_date").toString());
                                                        item.setDatedata(document.getData().get("income_date").toString());
                                                        item.setIncome_type("D");
                                                        item.setIncome_notes(document.getData().get("income_notes").toString());
                                                        item.setIncome_from(document.getData().get("income_from").toString());

                                                        transactionlis.add(item);
                                                        Collections.sort(transactionlis);
                                                        Collections.reverse(transactionlis);



                                                    }
                                                }
                                                else if(datediff==1){
                                                    if(df.parse(document.getData().get("income_date").toString()).equals(date1) || df.parse(document.getData().get("income_date").toString()).equals(date2)){
                                                        Log.e("income datas", document.getId() + " => " + document.getData());

                                                        incomeexpensetransfer item = new incomeexpensetransfer();
                                                        item.setIncomedoc(document.getId());
                                                        item.setIncome_account(document.getData().get("income_account").toString());
                                                        item.setIncome_amount(Double.parseDouble(document.getData().get("income_amount").toString()));
                                                        item.setIncome_category(document.getData().get("income_category").toString());
                                                        item.setIncome_date(document.getData().get("income_date").toString());
                                                        item.setDatedata(document.getData().get("income_date").toString());
                                                        item.setIncome_type("D");
                                                        item.setIncome_notes(document.getData().get("income_notes").toString());
                                                        item.setIncome_from(document.getData().get("income_from").toString());

                                                        transactionlis.add(item);
                                                        Collections.sort(transactionlis);
                                                        Collections.reverse(transactionlis);
                                                    }
                                                }

                                                else {
                                                    if ((df.parse(document.getData().get("income_date").toString())).after(date1) && (df.parse(document.getData().get("income_date").toString())).before(date2)){
                                                        Log.e("income datas", document.getId() + " => " + document.getData());

                                                        incomeexpensetransfer item = new incomeexpensetransfer();
                                                        item.setIncomedoc(document.getId());
                                                        item.setIncome_account(document.getData().get("income_account").toString());
                                                        item.setIncome_amount(Double.parseDouble(document.getData().get("income_amount").toString()));
                                                        item.setIncome_category(document.getData().get("income_category").toString());
                                                        item.setIncome_date(document.getData().get("income_date").toString());
                                                        item.setDatedata(document.getData().get("income_date").toString());
                                                        item.setIncome_type("D");
                                                        item.setIncome_notes(document.getData().get("income_notes").toString());
                                                        item.setIncome_from(document.getData().get("income_from").toString());

                                                        transactionlis.add(item);
                                                        Collections.sort(transactionlis);
                                                        Collections.reverse(transactionlis);
                                                    }
                                                }
                                                Log.e("transaction",  df.format(date1) + " " + df.format(date2) +" "+ df.format(df.parse(document.getData().get("income_date").toString())));

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                        count = count+25;
                                        onProgressUpdate();

                                        fdb.collection("expense")
                                                .whereEqualTo("expense_account",account)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                try {
                                                                    if(datediff==0){
                                                                        if(df.parse(document.getData().get("expense_date").toString()).equals(date1)){
                                                                            Log.e("expense datas", document.getId() + " => " + document.getData());
                                                                            incomeexpensetransfer item2 = new incomeexpensetransfer();
                                                                            item2.setExpensedoc(document.getId());
                                                                            item2.setExpense_account(document.getData().get("expense_account").toString());
                                                                            item2.setExpense_amount(Double.parseDouble(document.getData().get("expense_amount").toString()));
                                                                            item2.setExpense_category(document.getData().get("expense_category").toString());
                                                                            item2.setDatedata(document.getData().get("expense_date").toString());
                                                                            item2.setExpense_date(document.getData().get("expense_date").toString());
                                                                            item2.setExpense_type("K");
                                                                            item2.setExpense_notes(document.getData().get("expense_notes").toString());
                                                                            item2.setExpense_to(document.getData().get("expense_to").toString());

                                                                            transactionlis.add(item2);
                                                                            Collections.sort(transactionlis);
                                                                            Collections.reverse(transactionlis);
                                                                        }
                                                                    }
                                                                    else if(datediff==1){
                                                                        if(df.parse(document.getData().get("expense_date").toString()).equals(date1) || df.parse(document.getData().get("expense_date").toString()).equals(date2)){
                                                                            Log.e("expense datas", document.getId() + " => " + document.getData());
                                                                            incomeexpensetransfer item2 = new incomeexpensetransfer();
                                                                            item2.setExpensedoc(document.getId());
                                                                            item2.setExpense_account(document.getData().get("expense_account").toString());
                                                                            item2.setExpense_amount(Double.parseDouble(document.getData().get("expense_amount").toString()));
                                                                            item2.setExpense_category(document.getData().get("expense_category").toString());
                                                                            item2.setDatedata(document.getData().get("expense_date").toString());
                                                                            item2.setExpense_date(document.getData().get("expense_date").toString());
                                                                            item2.setExpense_type("K");
                                                                            item2.setExpense_notes(document.getData().get("expense_notes").toString());
                                                                            item2.setExpense_to(document.getData().get("expense_to").toString());

                                                                            transactionlis.add(item2);
                                                                            Collections.sort(transactionlis);
                                                                            Collections.reverse(transactionlis);
                                                                        }
                                                                    }

                                                                    else {
                                                                        if ((df.parse(document.getData().get("expense_date").toString())).after(date1) && (df.parse(document.getData().get("expense_date").toString())).before(date2)){
                                                                            Log.e("expense datas", document.getId() + " => " + document.getData());
                                                                            incomeexpensetransfer item2 = new incomeexpensetransfer();
                                                                            item2.setExpensedoc(document.getId());
                                                                            item2.setExpense_account(document.getData().get("expense_account").toString());
                                                                            item2.setExpense_amount(Double.parseDouble(document.getData().get("expense_amount").toString()));
                                                                            item2.setExpense_category(document.getData().get("expense_category").toString());
                                                                            item2.setDatedata(document.getData().get("expense_date").toString());
                                                                            item2.setExpense_date(document.getData().get("expense_date").toString());
                                                                            item2.setExpense_type("K");
                                                                            item2.setExpense_notes(document.getData().get("expense_notes").toString());
                                                                            item2.setExpense_to(document.getData().get("expense_to").toString());

                                                                            transactionlis.add(item2);
                                                                            Collections.sort(transactionlis);
                                                                            Collections.reverse(transactionlis);
                                                                        }
                                                                    }
                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                            count = count+25;
                                                            onProgressUpdate();
                                                            fdb.collection("transfer")
                                                                    .whereEqualTo("transfer_src",account)
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                    try {
                                                                                        if(datediff==0){
                                                                                            if(df.parse(document.getData().get("transfer_date").toString()).equals(date1)){
                                                                                                Log.e("transfer src", document.getId() + " => " + document.getData());

                                                                                                incomeexpensetransfer data = new incomeexpensetransfer();
                                                                                                data.setTransferdoc(document.getId());
                                                                                                data.setTransfer_amount(Double.parseDouble(document.getData().get("transfer_amount").toString()));
                                                                                                data.setTransfer_rate(Double.parseDouble(document.getData().get("transfer_rate").toString()));
                                                                                                data.setTransfer_date(document.getData().get("transfer_date").toString());
                                                                                                data.setDatedata(document.getData().get("transfer_date").toString());
                                                                                                data.setTransfer_notes(document.getData().get("transfer_notes").toString());
                                                                                                data.setTransfer_src(document.getData().get("transfer_src").toString());
                                                                                                data.setTransfer_dest(document.getData().get("transfer_dest").toString());

                                                                                                transactionlis.add(data);
                                                                                                Collections.sort(transactionlis);
                                                                                                Collections.reverse(transactionlis);

                                                                                            }
                                                                                        }
                                                                                        else if(datediff==1){
                                                                                            if(df.parse(document.getData().get("transfer_date").toString()).equals(date1) || df.parse(document.getData().get("transfer_date").toString()).equals(date2)){
                                                                                                Log.e("transfer src", document.getId() + " => " + document.getData());

                                                                                                incomeexpensetransfer data = new incomeexpensetransfer();
                                                                                                data.setTransferdoc(document.getId());
                                                                                                data.setTransfer_amount(Double.parseDouble(document.getData().get("transfer_amount").toString()));
                                                                                                data.setTransfer_rate(Double.parseDouble(document.getData().get("transfer_rate").toString()));
                                                                                                data.setTransfer_date(document.getData().get("transfer_date").toString());
                                                                                                data.setDatedata(document.getData().get("transfer_date").toString());
                                                                                                data.setTransfer_notes(document.getData().get("transfer_notes").toString());
                                                                                                data.setTransfer_src(document.getData().get("transfer_src").toString());
                                                                                                data.setTransfer_dest(document.getData().get("transfer_dest").toString());

                                                                                                transactionlis.add(data);
                                                                                                Collections.sort(transactionlis);
                                                                                                Collections.reverse(transactionlis);
                                                                                            }
                                                                                        }

                                                                                        else {
                                                                                            if ((df.parse(document.getData().get("transfer_date").toString())).after(date1) && (df.parse(document.getData().get("transfer_date").toString())).before(date2)){
                                                                                                Log.e("transfer src", document.getId() + " => " + document.getData());

                                                                                                incomeexpensetransfer data = new incomeexpensetransfer();
                                                                                                data.setTransferdoc(document.getId());
                                                                                                data.setTransfer_amount(Double.parseDouble(document.getData().get("transfer_amount").toString()));
                                                                                                data.setTransfer_rate(Double.parseDouble(document.getData().get("transfer_rate").toString()));
                                                                                                data.setTransfer_date(document.getData().get("transfer_date").toString());
                                                                                                data.setDatedata(document.getData().get("transfer_date").toString());
                                                                                                data.setTransfer_notes(document.getData().get("transfer_notes").toString());
                                                                                                data.setTransfer_src(document.getData().get("transfer_src").toString());
                                                                                                data.setTransfer_dest(document.getData().get("transfer_dest").toString());

                                                                                                transactionlis.add(data);
                                                                                                Collections.sort(transactionlis);
                                                                                                Collections.reverse(transactionlis);
                                                                                            }
                                                                                        }
                                                                                    } catch (ParseException e) {
                                                                                        e.printStackTrace();
                                                                                    }

                                                                                }
                                                                                count = count+25;
                                                                                onProgressUpdate();

                                                                                fdb.collection("transfer")
                                                                                        .whereEqualTo("transfer_desc", account)
                                                                                        .get()
                                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                                        try {
                                                                                                            if(datediff==0){
                                                                                                                if(df.parse(document.getData().get("transfer_date").toString()).equals(date1)){
                                                                                                                    Log.e("transfer dest", document.getId() + " => " + document.getData());

                                                                                                                    incomeexpensetransfer data = new incomeexpensetransfer();
                                                                                                                    data.setTransferdoc(document.getId());
                                                                                                                    data.setTransfer_amount(Double.parseDouble(document.getData().get("transfer_amount").toString()));
                                                                                                                    data.setTransfer_rate(Double.parseDouble(document.getData().get("transfer_rate").toString()));
                                                                                                                    data.setTransfer_date(document.getData().get("transfer_date").toString());
                                                                                                                    data.setDatedata(document.getData().get("transfer_date").toString());
                                                                                                                    data.setTransfer_notes(document.getData().get("transfer_notes").toString());
                                                                                                                    data.setTransfer_src(document.getData().get("transfer_src").toString());
                                                                                                                    data.setTransfer_dest(document.getData().get("transfer_dest").toString());

                                                                                                                    transactionlis.add(data);
                                                                                                                    Collections.sort(transactionlis);
                                                                                                                    Collections.reverse(transactionlis);
                                                                                                                }
                                                                                                            }
                                                                                                            else if(datediff==1){
                                                                                                                if(df.parse(document.getData().get("transfer_date").toString()).equals(date1) || df.parse(document.getData().get("transfer_date").toString()).equals(date2)){
                                                                                                                    Log.e("transfer dest", document.getId() + " => " + document.getData());

                                                                                                                    incomeexpensetransfer data = new incomeexpensetransfer();
                                                                                                                    data.setTransferdoc(document.getId());
                                                                                                                    data.setTransfer_amount(Double.parseDouble(document.getData().get("transfer_amount").toString()));
                                                                                                                    data.setTransfer_rate(Double.parseDouble(document.getData().get("transfer_rate").toString()));
                                                                                                                    data.setTransfer_date(document.getData().get("transfer_date").toString());
                                                                                                                    data.setDatedata(document.getData().get("transfer_date").toString());
                                                                                                                    data.setTransfer_notes(document.getData().get("transfer_notes").toString());
                                                                                                                    data.setTransfer_src(document.getData().get("transfer_src").toString());
                                                                                                                    data.setTransfer_dest(document.getData().get("transfer_dest").toString());

                                                                                                                    transactionlis.add(data);
                                                                                                                    Collections.sort(transactionlis);
                                                                                                                    Collections.reverse(transactionlis);
                                                                                                                }
                                                                                                            }

                                                                                                            else {
                                                                                                                if ((df.parse(document.getData().get("transfer_date").toString())).after(date1) && (df.parse(document.getData().get("transfer_date").toString())).before(date2)){
                                                                                                                    Log.e("transfer dest", document.getId() + " => " + document.getData());

                                                                                                                    incomeexpensetransfer data = new incomeexpensetransfer();
                                                                                                                    data.setTransferdoc(document.getId());
                                                                                                                    data.setTransfer_amount(Double.parseDouble(document.getData().get("transfer_amount").toString()));
                                                                                                                    data.setTransfer_rate(Double.parseDouble(document.getData().get("transfer_rate").toString()));
                                                                                                                    data.setTransfer_date(document.getData().get("transfer_date").toString());
                                                                                                                    data.setDatedata(document.getData().get("transfer_date").toString());
                                                                                                                    data.setTransfer_notes(document.getData().get("transfer_notes").toString());
                                                                                                                    data.setTransfer_src(document.getData().get("transfer_src").toString());
                                                                                                                    data.setTransfer_dest(document.getData().get("transfer_dest").toString());

                                                                                                                    transactionlis.add(data);
                                                                                                                    Collections.sort(transactionlis);
                                                                                                                    Collections.reverse(transactionlis);
                                                                                                                }
                                                                                                            }
                                                                                                        } catch (ParseException e) {
                                                                                                            e.printStackTrace();
                                                                                                        }

                                                                                                    }
                                                                                                    count = count+25;
                                                                                                    onProgressUpdate();
                                                                                                    issuccess = true;
                                                                                                    onPostExecute(issuccess);
                                                                                                    //adapter
                                                                                                } else {
                                                                                                    issuccess = false;
                                                                                                    erro= erro+task.getException();
                                                                                                    onPostExecute(issuccess);
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                            else{
                                                                                issuccess = false;
                                                                                erro= erro+task.getException();
                                                                                onPostExecute(issuccess);
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                        else{
                                                            issuccess = false;
                                                            erro= erro+task.getException();
                                                            onPostExecute(issuccess);
                                                        }
                                                    }
                                                });
                                    }
                                    else{
                                        issuccess = false;
                                        erro= erro+task.getException();
                                        onPostExecute(issuccess);
                                    }
                                }
                            });

            }
            else {

            }
            // Get the number of task
            return "";
        }

        // After each task done
        protected void onProgressUpdate(Integer... progress){
            // Display the progress on text view
            dialog.setMessage(""+count + " %");
            // Update the progress bar
            dialog.setProgress(count);
        }

        // When all async task done
        protected void onPostExecute(Boolean issuccess){

            if(issuccess){
                if(adapter!=null){
                    adapter.notifyDataSetChanged();
                }

                dialog.dismiss();

            }
            else {
                dialog.dismiss();
            }
        }
    }
}

