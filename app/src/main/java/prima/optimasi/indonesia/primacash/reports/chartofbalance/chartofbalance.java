package prima.optimasi.indonesia.primacash.reports.chartofbalance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import prima.optimasi.indonesia.primacash.MainActivity;
import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.extramenuactivity.accountsstatistic;
import prima.optimasi.indonesia.primacash.objects.incomeexpensetransfer;

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

        //generate data report

        recyclerchartmain adapter = new recyclerchartmain(chartofbalance.this,request,iscat,showowntrans,datediff,date1,date2);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(chartofbalance.this, 1);
        recycler.setLayoutManager(mLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
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


