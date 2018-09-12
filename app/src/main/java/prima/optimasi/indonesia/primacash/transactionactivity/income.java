package prima.optimasi.indonesia.primacash.transactionactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.NoCopySpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import prima.optimasi.indonesia.primacash.MainActivity;
import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.fragment_transaction.fragment_income;
import prima.optimasi.indonesia.primacash.fragment_transaction.fragment_incomelist;
import prima.optimasi.indonesia.primacash.generator;

/**
 * Created by rwina on 4/23/2018.
 */

public class income extends AppCompatActivity {

    ViewPager vpincome;

    FirebaseFirestore db;


    int positiondata;

    fragment_income incomepg1;
    fragment_incomelist incomepg2;

    // Create an adapter that knows which fragment should be shown on each page
    SimpleFragmentPagerAdapter adapter;

    // Set the adapter onto the view pager


    // Give the TabLayout the ViewPager
    TabLayout tabLayout;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        db = FirebaseFirestore.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(0);

        vpincome = (ViewPager) findViewById(R.id.viewpagerincome);
        tabLayout = (TabLayout) findViewById(R.id.tabincome);
        adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());

        vpincome.setAdapter(adapter);
        tabLayout.setupWithViewPager(vpincome);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                positiondata  = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
/*
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(positiondata==0){
                    incomepg1 = new fragment_income();
                    incomepg1.check(income.this);
                    incomepg1.writeobjects();

                    if(incomepg1.issaveable(income.this)==false){
                        Toast.makeText(income.this, "Please Check Some Fields", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(generator.incdate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (new Date().after(strDate) || new Date().equals(strDate)) {
                            incomepg1.calculatebalance();
                            generator.isdone="1";
                        }
                        incomepg1.writeobjects();
                        Toast.makeText(income.this, "Saving Income...", Toast.LENGTH_SHORT).show();
                        db.collection("income")
                                .add(incomepg1.getthemap())
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("account_balance_current", generator.incbalanceleft );

                                        db.collection("account").document(generator.incdocument)
                                                .set(data, SetOptions.merge());
                                        generator.isdone="0";
                                        if(generator.adapter!=null){
                                            generator.adapter.notifyDataSetChanged();
                                        }
                                        finish();
                                        Toast.makeText(income.this, "New Income Added", Toast.LENGTH_SHORT).show();
                                        Log.e("Add data", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("error data add", "Error adding document", e);
                                    }
                                });
                    }
                }
                else if (positiondata==1) {
                    incomepg2 = new fragment_incomelist();
                    incomepg2.check(income.this);
                    incomepg2.writeobjects();
                    if(incomepg2.issaveable(income.this)==false){
                        Toast.makeText(income.this, "Please Check Some Fields", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Toast.makeText(income.this, "Saving Scheduled Income...", Toast.LENGTH_SHORT).show();
                        db.collection("income")
                                .add(incomepg2.getthemap())
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        finish();
                                        if(generator.adapter!=null){
                                            generator.adapter.notifyDataSetChanged();
                                        }
                                        generator.isdone="0";
                                        Toast.makeText(income.this, "New Scheduled Income Added", Toast.LENGTH_SHORT).show();
                                        Log.e("Add data", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(income.this, "Error Adding Scheduled Income : "+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        Log.e("error data add", "Error adding document", e);
                                    }
                                });
                    }
                }
            }
        });*/

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_list_white_24dp);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_restore_white_24dp);
        }
    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private Context mContext;

        public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }

        // This determines the fragment for each tab
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new fragment_income();
            } else {
                return new fragment_incomelist();
            }
        }

        // This determines the number of tabs
        @Override
        public int getCount() {
            return 2;
        }

        // This determines the title for each tab


    }

    @Override
    public void onBackPressed() {

        income.this.overridePendingTransition(R.anim.trans_right_in,
                R.anim.trans_right_out);

        if (vpincome.getCurrentItem() == 0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(income.this,R.style.AppCompatAlertDialogStyle)
                    .setTitle("Cancel")
                    .setMessage("Changes made will be discarded , proceed ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.show();
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.

        } else {
            // Otherwise, select the previous step.
            vpincome.setCurrentItem(vpincome.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            if (vpincome.getCurrentItem() == 0) {
                // If the user is currently looking at the first step, allow the system to handle the
                // Back button. This calls finish() on this activity and pops the back stack.
                super.onBackPressed();
                finish();
                return true;
            } else {
                // Otherwise, select the previous step.
                vpincome.setCurrentItem(vpincome.getCurrentItem() - 1);
                return true;
            }
        }
        return  true;
    }


}
