package midascash.indonesia.optima.prima.midascash.reports;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.generator;
import midascash.indonesia.optima.prima.midascash.objects.incomeexpensetransfer;

public class chartofbalance extends AppCompatActivity {

    RecyclerView recycler;

    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    FirebaseFirestore fdb;

    int showowntrans =0;
    int iscat = 0 ;
    int datediff=0;

    int totaldata = 0;

    List<incomeexpensetransfer> alldata;

    Date date1=null;
    Date date2=null;

    List<String> request;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartofbalance);

        fdb = FirebaseFirestore.getInstance();

        alldata = new ArrayList<>();

        request = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recycler = findViewById(R.id.recyclerbalance);

        Intent bundle = getIntent();

        request = bundle.getStringArrayListExtra("request");

        showowntrans = bundle.getIntExtra("shownown", 0);
        iscat = bundle.getIntExtra("accountorcategory", 0);
        datediff = bundle.getIntExtra("datediff", 0);



        try {
            date1= df.parse(bundle.getStringExtra("date1"));
            date2= df.parse(bundle.getStringExtra("date2"));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Log.e("request size", String.valueOf(request.size()));
        Log.e("show trans", String.valueOf(showowntrans));
        Log.e("is category", String.valueOf(iscat));
        Log.e("datediff", String.valueOf(datediff));

        charttask task = new charttask(chartofbalance.this,R.style.AppCompatAlertDialogStyle);
        task.execute("");

    }

    private class charttask extends AsyncTask<String, Integer, String> {

        private ProgressDialog dialog;
        private  int count ;
        private Boolean issuccess=false;

        private Context context;

        private String erro="";
        public charttask(Context context, int resource) {
            this.dialog = new ProgressDialog(context,resource).show(context,"Loading Report","Please wait while your report Loads");
            count =0 ;
            dialog.setCancelable(false);
        }

        // Do the task in background/non UI thread
        protected String doInBackground(String...tasks){

            if(iscat == 0){
                for (int i = 0 ; i< request.size() ; i++){
                    int finalI = i;
                    int finalI1 = i;
                    int finalI2 = i;
                    fdb.collection("income")
                            .whereEqualTo("income_account",request.get(i))
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            try {
                                                Log.e("transaction",  df.format(date1) + " " + df.format(date2) +" "+ df.format(df.parse(document.getData().get("income_date").toString())));
                                                if ((df.parse(document.getData().get("income_date").toString())).after(date1) && (df.parse(document.getData().get("income_date").toString())).before(date2)){

                                                    Log.e("income datas", document.getId() + " => " + document.getData());
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                        count = count+25;
                                        onProgressUpdate();

                                        fdb.collection("expense")
                                                .whereGreaterThanOrEqualTo("expense_datesys",date1 )
                                                .whereLessThanOrEqualTo("expense_datesys", date2)
                                                .whereEqualTo("expense_account",request.get(finalI2))
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                Log.e("expense datas", document.getId() + " => " + document.getData());


                                                            }
                                                            count = count+25;
                                                            onProgressUpdate();
                                                            fdb.collection("transfer")
                                                                    .whereGreaterThanOrEqualTo("transfer_datesys",date1)
                                                                    .whereLessThanOrEqualTo("transfer_datesys", date2)
                                                                    .whereEqualTo("transfer_src",request.get(finalI1))
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                    Log.e("trf src", document.getId() + " => " + document.getData());

                                                                                }
                                                                                count = count+25;
                                                                                onProgressUpdate();

                                                                            fdb.collection("income")
                                                                                    .whereGreaterThanOrEqualTo("transfer_datesys", date1)
                                                                                    .whereLessThanOrEqualTo("transfer_datesys", date2)
                                                                                    .whereEqualTo("transfer_desc", request.get(finalI))
                                                                                    .get()
                                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                                    Log.e("transfer dest ", document.getId() + " => " + document.getData());

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
            }
            else {

            }
            // Get the number of task
            return "";
        }

        // After each task done
        protected void onProgressUpdate(Integer... progress){
            // Display the progress on text view
            //dialog.setMessage(""+count + " %");
            // Update the progress bar
            dialog.setProgress(count);
        }

        // When all async task done
        protected void onPostExecute(Boolean issuccess){

            if(issuccess){
                dialog.dismiss();
            }
            else {
                dialog.dismiss();
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home)
            finish();

        return  true;
    }

}


