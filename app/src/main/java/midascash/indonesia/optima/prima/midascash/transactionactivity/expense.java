package midascash.indonesia.optima.prima.midascash.transactionactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.fragment_transaction.fragment_expense;
import midascash.indonesia.optima.prima.midascash.fragment_transaction.fragment_expenselist;

/**
 * Created by rwina on 4/23/2018.
 */

public class expense extends AppCompatActivity {


    ViewPager vpincome;
    Button save,cancel;

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
        setContentView(R.layout.activity_expense);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        vpincome = (ViewPager) findViewById(R.id.viewpagerexpense);
        tabLayout = (TabLayout) findViewById(R.id.tabexpense);


        adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());

        save = (Button) findViewById(R.id.btnsaveexpense);
        cancel = findViewById(R.id.btncancelexpense);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(expense.this,R.style.AppCompatAlertDialogStyle)
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
            }
        });

        vpincome.setAdapter(adapter);
        tabLayout.setupWithViewPager(vpincome);

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
                return new fragment_expense();
            } else {
                return new fragment_expenselist();
            }
        }

        // This determines the number of tabs
        @Override
        public int getCount() {
            return 2;
        }

        // This determines the title for each tab
        /*@Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return "New";
                case 1:
                    return "Scheduled";
                default:
                    return null;
            }
        }
        */

    }

    @Override
    public void onBackPressed() {

        expense.this.overridePendingTransition(R.anim.trans_right_in,
                R.anim.trans_right_out);

        if (vpincome.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
            finish();
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
