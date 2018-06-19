package midascash.indonesia.optima.prima.midascash.transactionactivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Collections;
import java.util.List;
import java.util.logging.Formatter;

import de.hdodenhof.circleimageview.CircleImageView;
import midascash.indonesia.optima.prima.midascash.fragment_transaction.fragment_expense_show;
import midascash.indonesia.optima.prima.midascash.fragment_transaction.fragment_expense_show_scheduled;
import midascash.indonesia.optima.prima.midascash.objects.expense;
import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.generator;

public class listexpense extends AppCompatActivity {

    RecyclerView recycler;
    RecyclerView recycler1;

    List<expense> dataexp;
    List<expense> dataexpscheduled;
    
    ViewPager vpexpenselist;

    SimpleFragmentPagerAdapter adapter;
    DecimalFormat formatter = new DecimalFormat("###,###,###,###.##");

    TabLayout tablayout;

    FirebaseFirestore db;

    int[] images = new int[]{R.drawable.cashicon, R.drawable.bank, R.drawable.lendresized, R.drawable.cheque, R.drawable.creditcardresized,R.drawable.food,R.drawable.electric,R.drawable.truck,R.drawable.health,R.drawable.ball
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        Toast.makeText(this, "Loading expense", Toast.LENGTH_SHORT).show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        
        vpexpenselist = findViewById(R.id.vpexpenselist);

        adapter = new SimpleFragmentPagerAdapter(this,getSupportFragmentManager());

        vpexpenselist.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        tablayout = findViewById(R.id.tabexpenselist);
        tablayout.setupWithViewPager(vpexpenselist);

        for (int i = 0; i < tablayout.getTabCount(); i++) {
            tablayout.getTabAt(0).setIcon(R.drawable.ic_list_white_24dp);
            tablayout.getTabAt(1).setIcon(R.drawable.ic_restore_white_24dp);
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
                return new fragment_expense_show();
            } else {
                return new fragment_expense_show_scheduled();
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

        listexpense.this.overridePendingTransition(R.anim.trans_right_in,
                R.anim.trans_right_out);

        if (vpexpenselist.getCurrentItem() == 0) {
            finish();
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.

        } else {
            // Otherwise, select the previous step.
            vpexpenselist.setCurrentItem(vpexpenselist.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            if (vpexpenselist.getCurrentItem() == 0) {
                // If the user is currently looking at the first step, allow the system to handle the
                // Back button. This calls finish() on this activity and pops the back stack.
                super.onBackPressed();
                finish();
                return true;
            } else {
                // Otherwise, select the previous step.
                vpexpenselist.setCurrentItem(vpexpenselist.getCurrentItem() - 1);
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
