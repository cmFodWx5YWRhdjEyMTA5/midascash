package midascash.indonesia.optima.prima.midascash.fragment_transaction;

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

import com.fake.shopee.shopeefake.formula.commaedittext;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

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
import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.formula.calculatordialog;
import midascash.indonesia.optima.prima.midascash.generator;
import midascash.indonesia.optima.prima.midascash.objects.income;
import midascash.indonesia.optima.prima.midascash.transactionactivity.listincome;
import midascash.indonesia.optima.prima.midascash.transactionactivity.modify.editincome;

public class fragment_income_show extends Fragment {

    LayoutInflater inflater;
    FirebaseFirestore fdb;

    listitemincome incadapter;

    List<income> datainc;

    RecyclerView recycler;

    public fragment_income_show(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fdb = FirebaseFirestore.getInstance();

        datainc = new ArrayList<>();

        View view = inflater.inflate(R.layout.row_activity_fragment_income_layout, container, false);

        recycler = view.findViewById(R.id.incomelistdata);

        fdb.collection("income")
                .whereEqualTo("income_isdated", "0")
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
                                fdb.collection("category")
                                        .whereEqualTo("category_name", document.getData().get("income_category").toString())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                if (task1.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                                        Log.e("category datas", document1.getId() + " => " + document1.getData());
                                                        values.setIncome_image(Integer.parseInt(document1.getData().get("category_image").toString()));
                                                        datainc.add(values);
                                                        Collections.sort(datainc);
                                                        Collections.reverse(datainc);
                                                        incadapter = new listitemincome(getActivity(),datainc,false);
                                                        incadapter.notifyDataSetChanged();
                                                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
                                                        recycler.setLayoutManager(mLayoutManager);
                                                        recycler.setItemAnimator(new DefaultItemAnimator());
                                                        recycler.setAdapter(incadapter);
                                                    }
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

        // Inflate the layout for this fragment
        return view;
    }


    public class listitemincome extends RecyclerView.Adapter<listitemincome.MyViewHolder>{

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
                    .inflate(R.layout.row_layout_listincome, parent, false);

            return new listitemincome.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            income incomes = incomelist.get(position);
            holder.documenref = incomes.getIncomedoc();
            Drawable resImg = context.getResources().getDrawable(generator.images[incomes.getIncome_image()-1]);
            holder.image.setImageDrawable(resImg);
            holder.image.setTag(incomes.getIncome_image());
            holder.incomevalue.setText(formatter.format(incomes.getIncome_amount()));
            holder.incomeaccount.setText(incomes.getIncome_account());
            holder.incomefrom.setText(incomes.getIncome_from());
            holder.color.setBackgroundColor(generator.green);
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
                                    Intent editincomes = new Intent(context,editincome.class);
                                    editincomes.putExtra("document",holder.documenref);
                                    generator.showadapterincome=null;
                                    generator.showdataincome=null;
                                    generator.showadapterincome=incadapter;
                                    generator.showdataincome=datainc;
                                    startActivity(editincomes);
                                    return true;
                                case R.id.incdeleteitem:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle);
                                    builder.setTitle("Confirm");
                                    builder.setMessage("Are you sure to delete Income on "+incomes.getIncome_date()+" with "+formatter.format(incomes.getIncome_amount())+" amount and uses "+incomes.getIncome_account()+" and categorized as "+incomes.getIncome_category()+" ?");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            DocumentReference docRef = fdb.collection("income").document(holder.documenref);
                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            Log.d("Documentdata", "DocumentSnapshot data: " + document.getData());
                                                            if(document.getData().get("income_isdone").toString().equals("1")){
                                                                fdb.collection("account")
                                                                        .whereEqualTo("account_name", document.getData().get("income_account"))
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    for (QueryDocumentSnapshot document1 : task.getResult()) {
                                                                                        if(document.getData().get("income_isdone").toString().equals("1")){
                                                                                            Map<String, Object> data = new HashMap<>();
                                                                                            Double result=generator.makedouble(document1.getData().get("account_balance_current").toString())-generator.makedouble(document.getData().get("income_amount").toString());
                                                                                            data.put("account_balance_current", String.valueOf(result));

                                                                                            fdb.collection("account").document(document1.getId())
                                                                                                    .set(data, SetOptions.merge());

                                                                                            fdb.collection("income").document(holder.documenref)
                                                                                                    .delete()
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                            reloaddata();
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
                                                                                        else {
                                                                                            fdb.collection("income").document(holder.documenref)
                                                                                                    .delete()
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                            reloaddata();
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
                                                                                    }
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        } else {
                                                            Log.d("Documentdata", "No such document");
                                                        }
                                                    } else {
                                                        Log.d("Documentdata", "get failed with ", task.getException());
                                                    }
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
            String documenref="";
            public CircleImageView image;
            View color;
            public LinearLayout items;
            public TextView incomecategory, incomefrom, incomevalue ,incomeaccount,incomedate,incomenote;

            public MyViewHolder(View view) {
                super(view);
                items = view.findViewById(R.id.Linearitemsincome);
                image = view.findViewById(R.id.incimagelistdata);
                color= view.findViewById(R.id.colortype);
                incomecategory = view.findViewById(R.id.inccatdata);
                incomenote = view.findViewById(R.id.incnotedata);
                incomedate = view.findViewById(R.id.incdatedata);
                incomefrom = view.findViewById(R.id.incfromdata);
                incomeaccount= view.findViewById(R.id.incaccdata);
                incomevalue = view.findViewById(R.id.incamountdata);
            }
        }
    }

    public void reloaddata(){
        fdb.collection("income")
                .whereEqualTo("income_isdated", "0")
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
                                fdb.collection("category")
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
