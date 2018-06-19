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
import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.formula.calculatordialog;
import midascash.indonesia.optima.prima.midascash.generator;
import midascash.indonesia.optima.prima.midascash.objects.expense;
import midascash.indonesia.optima.prima.midascash.transactionactivity.listexpense;
import midascash.indonesia.optima.prima.midascash.transactionactivity.modify.editexpense;
import midascash.indonesia.optima.prima.midascash.transactionactivity.modify.editexpensesch;

public class fragment_expense_show_scheduled extends Fragment {

    EditText editto;
    LayoutInflater inflater;
    LinearLayout contain;

    listitemexpensescheduled expadapter;

    List<expense> dataexp;

    RecyclerView recycler;

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

    FirebaseFirestore fdb;

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

        View view = inflater.inflate(R.layout.row_activity_fragment_expense_layout, container, false);
        contain = view.findViewById(R.id.llexpense);

        recycler = view.findViewById(R.id.expenselistdata);

        dataexp = new ArrayList<>();

        fdb.collection("expense")
                .whereEqualTo("expense_isdated", "1")
                .orderBy("expense_datesys", Query.Direction.DESCENDING )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.e("selectedd", "onTabSelected: 1" );
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("selectedd", "onTabSelected: 4" );
                                expense values = new expense();
                                values.setexpense_account(document.getData().get("expense_account").toString());
                                values.setexpense_amount(Double.parseDouble(document.getData().get("expense_amount").toString()));
                                values.setexpense_category(document.getData().get("expense_category").toString());
                                values.setexpense_date(document.getData().get("expense_date").toString());
                                values.setexpense_type("D");
                                values.setexpense_notes(document.getData().get("expense_notes").toString());
                                values.setexpense_to(document.getData().get("expense_to").toString());
                                fdb.collection("category")
                                        .whereEqualTo("category_name", document.getData().get("expense_category").toString())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                                        Log.e("category datas", document1.getId() + " => " + document1.getData());
                                                        values.setexpense_image(Integer.parseInt(document1.getData().get("category_image").toString()));
                                                        dataexp.add(values);
                                                    }
                                                    Collections.sort(dataexp);
                                                    Collections.reverse(dataexp);
                                                    expadapter = new listitemexpensescheduled(getActivity(),dataexp,false);
                                                    expadapter.notifyDataSetChanged();
                                                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
                                                    recycler.setLayoutManager(mLayoutManager);
                                                    recycler.setItemAnimator(new DefaultItemAnimator());
                                                    recycler.setAdapter(expadapter);
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


    public void reloaddatasch(){
        fdb.collection("expense")
                .whereEqualTo("expense_isdated", "1")
                .orderBy("expense_datesys", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                expense values = new expense();
                                values.setexpense_account(document.getData().get("expense_account").toString());
                                values.setexpense_amount(Double.parseDouble(document.getData().get("expense_amount").toString()));
                                values.setexpense_category(document.getData().get("expense_category").toString());
                                values.setexpense_date(document.getData().get("expense_date").toString());
                                values.setexpense_type("D");
                                values.setexpense_notes(document.getData().get("expense_notes").toString());
                                values.setexpense_to(document.getData().get("expense_to").toString());
                                fdb.collection("category")
                                        .whereEqualTo("category_name", document.getData().get("expense_category").toString())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                                        Log.e("category datas", document1.getId() + " => " + document1.getData());
                                                        values.setexpense_image(Integer.parseInt(document1.getData().get("category_image").toString()));
                                                        dataexp.add(values);

                                                    }
                                                    Collections.sort(dataexp);
                                                    Collections.reverse(dataexp);
                                                    expadapter.notifyDataSetChanged();

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

    private class listitemexpensescheduled extends RecyclerView.Adapter<listitemexpensescheduled.MyViewHolder>{

        List<expense> expenselist = new ArrayList<>();
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        Context context;
        Boolean isscheduled;
        FirebaseFirestore fdb;

        public listitemexpensescheduled(Context context,List<expense> moviesList,Boolean isss) {
            this.expenselist = moviesList;
            this.context=context;
            fdb = FirebaseFirestore.getInstance();
            isscheduled=isss;
        }

        @Override
        public listitemexpensescheduled.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_layout_listexpense, parent, false);

            return new listitemexpensescheduled.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            expense expenses = expenselist.get(position);
            Drawable resImg = context.getResources().getDrawable(images[expenses.getexpense_image()-1]);
            holder.image.setImageDrawable(resImg);
            holder.image.setTag(expenses.getexpense_image());
            holder.expensevalue.setText(formatter.format(expenses.getexpense_amount()));
            holder.expenseaccount.setText(expenses.getexpense_account());
            holder.color.setBackgroundColor(generator.red);
            holder.expensefrom.setText(expenses.getexpense_to());
            holder.expensedate.setText(expenses.getexpense_date());
            holder.expensenote.setText(expenses.getexpense_notes());
            holder.expensecategory.setText(expenses.getexpense_category());
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
                                    Intent editincomes = new Intent(context,editexpensesch.class);
                                    startActivity(editincomes);
                                    return true;
                                case R.id.incdeleteitem:
                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                                    builder.setTitle("Confirm");
                                    builder.setMessage("Are you sure to delete Income on "+expenses.getexpense_date()+" with "+formatter.format(expenses.getexpense_amount())+" amount and uses "+expenses.getexpense_account()+" and categorized as "+expenses.getexpense_category()+" ?");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

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
            return this.expenselist.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            String documenref;
            View color;
            LinearLayout items;
            public CircleImageView image;
            public TextView expensecategory, expensefrom, expensevalue ,expenseaccount,expensedate,expensenote;

            public MyViewHolder(View view) {
                super(view);
                items = view.findViewById(R.id.linearitemsexpense);
                image = view.findViewById(R.id.expimagelistdata);
                color = view.findViewById(R.id.colortype);
                expensecategory = view.findViewById(R.id.expcatdata);
                expensenote = view.findViewById(R.id.expnotedata);
                expensedate = view.findViewById(R.id.expdatedata);
                expensefrom = view.findViewById(R.id.expfromdata);
                expenseaccount= view.findViewById(R.id.expaccdata);
                expensevalue = view.findViewById(R.id.expamountdata);
            }
        }
    }
}
