package midascash.indonesia.optima.prima.midascash.fragment_list_transaction;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import midascash.indonesia.optima.prima.midascash.R;

public class listincome extends AppCompatActivity {

    RecyclerView recycler;

    List<income> datainc;

    listitemincome incadapter;

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
        setContentView(R.layout.activity_income_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = FirebaseFirestore.getInstance();

        datainc = new ArrayList<>();

        db.collection("income")
                .whereEqualTo("income_repeat_period", "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                            }
                        } else {
                            Log.w("data", "Error getting documents.", task.getException());
                        }
                    }
                });

        incadapter = new listitemincome(listincome.this,datainc,false);
    }

    private class listitemincome extends RecyclerView.Adapter<listitemincome.MyViewHolder>{

        List<income> incomelist = new ArrayList<>();
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        Context context;
        Boolean isscheduled;
        FirebaseFirestore fdb;

        public listitemincome(Context context,List<income> moviesList,Boolean isss) {
            this.incomelist = moviesList;
            this.context=context;
            fdb = FirebaseFirestore.getInstance();
            isscheduled=isss;
        }

        @Override
        public listitemincome.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_layout_account, parent, false);

            return new listitemincome.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            income incomes = incomelist.get(position);
            Drawable resImg = context.getResources().getDrawable(images[incomes.getIncome_image()-1]);
            holder.image.setImageDrawable(resImg);
            holder.image.setTag(incomes.getIncome_image());
            holder.incomevalue.setText(formatter.format(incomes.getIncome_amount()));
            holder.incomeaccount.setText(incomes.getIncome_account());
            holder.incomefrom.setText(incomes.getIncome_from());
            holder.incomedate.setText(incomes.getIncome_date());
            holder.incomenote.setText(incomes.getIncome_notes());
            holder.incomecategory.setText(incomes.getIncome_category());
        }

        @Override
        public int getItemCount() {
            return this.incomelist.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            String documenref;
            public CircleImageView image;
            public TextView incomecategory, incomefrom, incomevalue ,incomeaccount,incomedate,incomenote;

            public MyViewHolder(View view) {
                super(view);
                image = view.findViewById(R.id.incimagelistdata);

                incomecategory = view.findViewById(R.id.inccatdata);
                incomenote = view.findViewById(R.id.incnotedata);
                incomedate = view.findViewById(R.id.incdatedata);
                incomefrom = view.findViewById(R.id.incfromdata);
                incomeaccount= view.findViewById(R.id.incaccdata);
                incomevalue = view.findViewById(R.id.incamountdata);
            }
        }
    }

    private class income {
        String income_account,income_type,income_from,income_notes,income_id,username,income_category;
        int income_image;
        String income_date,income_createdate;
        double income_amount;

        public income(){}

        public income(String iacc,String itype,String ifr,String inot,String iid,String idat,String icdat,double imon,int image,String icat,String user){
            username=user;
            income_account=iacc;
            income_amount=imon;
            income_createdate=icdat;
            income_from=ifr;
            income_date=idat;
            income_type=itype;
            income_notes=inot;
            income_id=iid;
            income_image= image;
        }

        //setter


        public void setIncome_category(String income_category) {
            this.income_category = income_category;
        }

        public void setIncome_account(String iacc){
            income_account=iacc;
        }

        public void setUsername(String user){
            username=user;
        }

        public void setIncome_image(int image){ income_image=image;}

        public void setIncome_type(String itype){
            income_type=itype;
        }

        public void setIncome_from(String ifr){
            income_from=ifr;
        }

        public void setIncome_notes(String inot){
            income_notes=inot;
        }

        public void setIncome_id(String iid){
            income_id=iid;
        }

        public void setIncome_date(String idat){
            income_date=idat;
        }

        public void setIncome_createdate(String icdat){
            income_createdate=icdat;
        }
        public void setIncome_amount(double imon){
            income_amount=imon;
        }

        //getter


        public String getIncome_category() {
            return income_category;
        }

        public String getUsername() {
            return username;
        }

        public int getIncome_image() {
            return income_image;
        }



        public String getIncome_account(){
            return income_account;
        }
        public String getIncome_type(){
            return income_type;
        }
        public String getIncome_from(){
            return income_from;
        }
        public String getIncome_notes(){
            return income_notes;
        }
        public double getIncome_amount(){
            return income_amount;
        }
        public String getIncome_id(){
            return income_id;
        }
        public String getIncome_date(){
            return income_date;
        }
        public String getIncome_createdate(){
            return income_createdate;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
                super.onBackPressed();
                finish();
                return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all, menu);
        return true;
    }
}
