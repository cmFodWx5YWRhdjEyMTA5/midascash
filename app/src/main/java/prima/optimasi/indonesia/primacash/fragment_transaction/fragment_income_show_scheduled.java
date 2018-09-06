package prima.optimasi.indonesia.primacash.fragment_transaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.formula.calculatordialog;
import prima.optimasi.indonesia.primacash.generator;
import prima.optimasi.indonesia.primacash.objects.income;
import prima.optimasi.indonesia.primacash.transactionactivity.modify.editincomesch;

public class fragment_income_show_scheduled extends Fragment {

    LayoutInflater inflater;

    listitemincomescheduled incadapter;

    List<income> datainc;

    RecyclerView recycler;

    FirebaseFirestore db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.row_activity_fragment_income_layout, container, false);

        recycler = view.findViewById(R.id.incomelistdata);

        datainc = new ArrayList<>();

        db.collection("income")
                .whereEqualTo("income_isdated", "1")
                .orderBy("income_datesys", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(incadapter!=null){
                                incadapter.notifyDataSetChanged();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                income values = new income();
                                values.setIncomedoc(document.getId());
                                values.setIncome_account(document.getData().get("income_account").toString());
                                values.setIncome_amount(Double.parseDouble(document.getData().get("income_amount").toString()));
                                values.setIncome_category(document.getData().get("income_category").toString());
                                values.setIncome_date(document.getData().get("income_date").toString());
                                values.setIncome_type("D");
                                values.setIncome_notes(document.getData().get("income_notes").toString());
                                values.setIncome_from(document.getData().get("income_from").toString());
                                final int[] count = {0};
                                db.collection("category")
                                        .whereEqualTo("category_name", document.getData().get("income_category").toString())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                                        Log.e("category datas", document1.getId() + " => " + document1.getData());
                                                        values.setIncome_image(Integer.parseInt(document1.getData().get("category_image").toString()));


                                                        count[0]++;
                                                    }
                                                    if(count[0]==0){
                                                        values.setIncome_image(37);
                                                    }
                                                    datainc.add(values);
                                                    Collections.sort(datainc);
                                                    Collections.reverse(datainc);
                                                    if(incadapter!=null){
                                                        incadapter.notifyDataSetChanged();
                                                    }
                                                } else {
                                                    Log.d("data", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                            }
                            incadapter = new listitemincomescheduled(getActivity(),datainc,false);
                            incadapter.notifyDataSetChanged();
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
                            recycler.setLayoutManager(mLayoutManager);
                            recycler.setItemAnimator(new DefaultItemAnimator());
                            recycler.setAdapter(incadapter);
                        } else {
                            Log.w("data", "Error getting documents.", task.getException());
                        }
                    }
                });

        // Inflate the layout for this fragment
        return view;
    }




    public class listitemincomescheduled extends RecyclerView.Adapter<listitemincomescheduled.MyViewHolder>{

        List<income> incomelist = new ArrayList<>();
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        Context context;
        Boolean isscheduled;
        FirebaseFirestore fdb;

        public listitemincomescheduled(Context context,List<income> moviesList,Boolean isss) {
            this.incomelist = moviesList;
            this.context=context;
            fdb = FirebaseFirestore.getInstance();
            isscheduled=isss;
        }

        @Override
        public listitemincomescheduled.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_layout_listincome, parent, false);

            return new listitemincomescheduled.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            income incomes = incomelist.get(position);

            Drawable resImg=null;
            if(incomes.getIncome_category().equals("-")){
                resImg = context.getResources().getDrawable(generator.images[36]);
            }
            else {
                resImg = context.getResources().getDrawable(generator.images[incomes.getIncome_image()-1]);
            }

            holder.documenref=incomes.getIncomedoc();
            holder.image.setImageDrawable(resImg);
            holder.image.setTag(incomes.getIncome_image());
            holder.incomevalue.setText(formatter.format(incomes.getIncome_amount()));
            holder.incomeaccount.setText(incomes.getIncome_account());
            holder.color.setBackgroundColor(generator.green);
            holder.incomefrom.setText(incomes.getIncome_from());
            holder.incomedate.setText(incomes.getIncome_date());
            holder.incomenote.setText(incomes.getIncome_notes());
            holder.incomecategory.setText(incomes.getIncome_category());
            holder.items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(context, holder.items);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.menu_income);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.incedititem:
                                    Intent editincomes = new Intent(context,editincomesch.class);
                                    editincomes.putExtra("document",holder.documenref);
                                    generator.showadapterincomesch=null;
                                    generator.showdataincomesch=null;
                                    generator.showadapterincomesch=incadapter;
                                    generator.showdataincomesch=datainc;
                                    startActivity(editincomes);
                                    return true;
                                case R.id.incdeleteitem:
                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle);
                                    builder.setTitle("Confirm");
                                    builder.setMessage("Are you sure to delete Income on "+incomes.getIncome_date()+" with "+formatter.format(incomes.getIncome_amount())+" amount and uses "+incomes.getIncome_account()+" and categorized as "+incomes.getIncome_category()+" ?");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {fdb.collection("income").document(holder.documenref)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        reloaddatasch();
                                                        datainc.clear();
                                                        incadapter.notifyDataSetChanged();
                                                        if(generator.adapter!=null){
                                                            generator.adapter.notifyDataSetChanged();
                                                        }
                                                        Toast.makeText(context, "Deleted selected Income", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, "Fail Delete selected Income", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        }
                                    });

                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });

                                    builder.show();
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

        }

        @Override
        public int getItemCount() {
            return this.incomelist.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            String documenref;
            View color;
            CardView items;
            public CircleImageView image;
            public TextView incomecategory, incomefrom, incomevalue ,incomeaccount,incomedate,incomenote;

            public MyViewHolder(View view) {
                super(view);
                items = view.findViewById(R.id.card_view);
                image = view.findViewById(R.id.incimagelistdata);
                color = view.findViewById(R.id.colortype);
                incomecategory = view.findViewById(R.id.inccatdata);
                incomenote = view.findViewById(R.id.incnotedata);
                incomedate = view.findViewById(R.id.incdatedata);
                incomefrom = view.findViewById(R.id.incfromdata);
                incomeaccount= view.findViewById(R.id.incaccdata);
                incomevalue = view.findViewById(R.id.incamountdata);
            }
        }
    }


    public void reloaddatasch(){
        db.collection("income")
                .whereEqualTo("income_isdated", "1")
                .orderBy("income_datesys", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(incadapter!=null){
                                incadapter.notifyDataSetChanged();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                income values = new income();
                                values.setIncomedoc(document.getId());
                                values.setIncome_account(document.getData().get("income_account").toString());
                                values.setIncome_amount(Double.parseDouble(document.getData().get("income_amount").toString()));
                                values.setIncome_category(document.getData().get("income_category").toString());
                                values.setIncome_date(document.getData().get("income_date").toString());
                                values.setIncome_type("D");
                                values.setIncome_notes(document.getData().get("income_notes").toString());
                                values.setIncome_from(document.getData().get("income_from").toString());
                                db.collection("category")
                                        .whereEqualTo("category_name", document.getData().get("income_category").toString())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                                        Log.e("category datas", document1.getId() + " => " + document1.getData());
                                                        values.setIncome_image(Integer.parseInt(document1.getData().get("category_image").toString()));
                                                        datainc.add(values);
                                                    }
                                                    incadapter.notifyDataSetChanged();
                                                    Collections.sort(datainc);
                                                    Collections.reverse(datainc);
                                                } else {
                                                    Log.d("data", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.w("data", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
