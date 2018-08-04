package prima.optimasi.indonesia.primacash.transactionactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Formatter;

import de.hdodenhof.circleimageview.CircleImageView;
import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.fragment_transaction.fragment_income_show;
import prima.optimasi.indonesia.primacash.fragment_transaction.fragment_income_show_scheduled;
import prima.optimasi.indonesia.primacash.generator;
import prima.optimasi.indonesia.primacash.objects.income;
import prima.optimasi.indonesia.primacash.transactionactivity.modify.editincome;
import prima.optimasi.indonesia.primacash.transactionactivity.modify.editincomesch;

public class listincome extends AppCompatActivity {

    ViewPager vpincomelist;


    List<income> dataincscheduled;

    SimpleFragmentPagerAdapter adapter;

    DecimalFormat formatter = new DecimalFormat("###,###,###,###.##");

    TabLayout tablayout;

    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_list);

        Toast.makeText(this, "Loading Income", Toast.LENGTH_SHORT).show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = FirebaseFirestore.getInstance();

        vpincomelist = findViewById(R.id.vpincomelist);

        adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());

        vpincomelist.setAdapter(adapter);

        tablayout = findViewById(R.id.tabincomelist);

        tablayout.setupWithViewPager(vpincomelist);

        for (int i = 0; i < tablayout.getTabCount(); i++) {
            tablayout.getTabAt(0).setIcon(R.drawable.ic_list_white_24dp);
            tablayout.getTabAt(1).setIcon(R.drawable.ic_restore_white_24dp);
        }


        //.whereEqualTo("income_repeat_period", "daily")
        //                .whereEqualTo("income_repeat_period", "weekly")
        //                .whereEqualTo("income_repeat_period", "Monthly")
        //                .whereEqualTo("income_repeat_period", "Yearly")
        //                .orderBy("income_date", Query.Direction.DESCENDING)




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
                return new fragment_income_show();
            } else {
                return new fragment_income_show_scheduled();
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

        listincome.this.overridePendingTransition(R.anim.trans_right_in,
                R.anim.trans_right_out);

        if (vpincomelist.getCurrentItem() == 0) {
            finish();
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.

        } else {
            // Otherwise, select the previous step.
            vpincomelist.setCurrentItem(vpincomelist.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            if (vpincomelist.getCurrentItem() == 0) {
                // If the user is currently looking at the first step, allow the system to handle the
                // Back button. This calls finish() on this activity and pops the back stack.
                super.onBackPressed();
                finish();
                return true;
            } else {
                // Otherwise, select the previous step.
                vpincomelist.setCurrentItem(vpincomelist.getCurrentItem() - 1);
                return true;
            }
        }
        return  true;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all, menu);
        return true;
    }




}
