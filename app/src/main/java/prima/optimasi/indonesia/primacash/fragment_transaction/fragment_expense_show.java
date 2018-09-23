package prima.optimasi.indonesia.primacash.fragment_transaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.FileUriExposedException;
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
import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.SQLiteHelper;
import prima.optimasi.indonesia.primacash.formula.calculatordialog;
import prima.optimasi.indonesia.primacash.generator;
import prima.optimasi.indonesia.primacash.objects.account;
import prima.optimasi.indonesia.primacash.objects.expense;
import prima.optimasi.indonesia.primacash.objects.income;
import prima.optimasi.indonesia.primacash.transactionactivity.modify.editexpense;

public class fragment_expense_show extends Fragment {

    EditText editto;
    LayoutInflater inflater;
    LinearLayout contain;

    FirebaseFirestore fdb;

    listitemexpense expadapter;

    SQLiteHelper dbase;

    RecyclerView recycler;

    List<expense> dataexp;

    AlertDialog dialog;//category
    AlertDialog dialogaccount;



    ImageView viewcat;
    TextView categorytext,accounttext;

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

        dbase = new SQLiteHelper(getActivity());

        View view = inflater.inflate(R.layout.row_activity_fragment_expense_layout, container, false);
        contain = view.findViewById(R.id.llexpense);

        recycler = view.findViewById(R.id.expenselistdata);

        dataexp = new ArrayList<>();

        dataexp = dbase.getAllexpense();

        if(dataexp.size()!=0){
            Log.e("test category", "onCreateView: "+ dataexp.get(0).getexpense_category() );
        }

        /*fdb.collection("expense")
                .whereEqualTo("expense_isdated", "0")
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
                                values.setexpensedoc(document.getId());
                                values.setexpense_account(document.getData().get("expense_account").toString());
                                values.setexpense_amount(Double.parseDouble(document.getData().get("expense_amount").toString()));
                                values.setexpense_category(document.getData().get("expense_category").toString());
                                values.setexpense_date(document.getData().get("expense_date").toString());
                                values.setexpense_type("K");
                                values.setexpense_notes(document.getData().get("expense_notes").toString());
                                values.setexpense_to(document.getData().get("expense_to").toString());

                                final int[] count = {0};

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

                                                        count[0]++;
                                                    }
                                                    if(count[0]==0){
                                                        values.setexpense_image(37);
                                                    }
                                                    dataexp.add(values);
                                                    Collections.sort(dataexp);
                                                    Collections.reverse(dataexp);

                                                    if(expadapter!=null){
                                                        expadapter.notifyDataSetChanged();
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
                });*/

        expadapter = new listitemexpense(getActivity(),dataexp,false);
        expadapter.notifyDataSetChanged();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recycler.setLayoutManager(mLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(expadapter);

        return view;
    }

    public void reloaddata(){
        dataexp.clear();
        List<expense> datainctemp = dbase.getAllexpense();

        for(int i = 0 ; i < datainctemp.size();i++){
            dataexp.add(datainctemp.get(i));
            Collections.sort(dataexp);
            Collections.reverse(dataexp);
        }



        if(expadapter!=null){
            expadapter.notifyDataSetChanged();
        }
    }

    public class listitemexpense extends RecyclerView.Adapter<listitemexpense.MyViewHolder>{

        List<expense> expenselist = new ArrayList<>();
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        Context context;
        Boolean isscheduled;
        FirebaseFirestore fdb;

        public listitemexpense(Context context,List<expense> moviesList,Boolean isss) {
            this.expenselist = moviesList;
            this.context=context;
            fdb = FirebaseFirestore.getInstance();
            isscheduled=isss;
        }

        @Override
        public listitemexpense.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_layout_listexpense, parent, false);

            return new listitemexpense.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            
            
            
            expense expenses = expenselist.get(position);
            Drawable resImg=null;
            if(expenses.getexpense_category().equals("-")){
                resImg = context.getResources().getDrawable(generator.images[36]);
            }
            else {
                resImg = context.getResources().getDrawable(generator.images[expenses.getexpense_image()-1]);
            }
            

            holder.documenref = expenses.getexpensedoc();
            holder.image.setImageDrawable(resImg);
            holder.image.setTag(expenses.getexpense_image());
            holder.expensevalue.setText(formatter.format(expenses.getexpense_amount()));
            holder.expenseaccount.setText(expenses.getexpense_account());
            holder.expensefrom.setText(expenses.getexpense_to());
            holder.color.setBackgroundColor(generator.red);
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
                                case R.id.incviewitem:
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle);

                                    View v = LayoutInflater.from(context).inflate(R.layout.layout_display_expense,null);

                                    int checkimage = 0;

                                    TextView dates,categories,accounts,amoount,currency,notes,from,notice;
                                    ImageView imagechosen;
                                    CircleImageView circle;

                                    currency = v.findViewById(R.id.allcurrency);
                                    amoount = v.findViewById(R.id.expamountview);
                                    categories =v.findViewById(R.id.expcatname);
                                    circle = v.findViewById(R.id.expcatpic);
                                    accounts = v.findViewById(R.id.expacctxt);
                                    dates = v.findViewById(R.id.expdateselect);

                                    notice = v.findViewById(R.id.expimagenote);
                                    notice.setVisibility(View.GONE);

                                    notes = v.findViewById(R.id.expnotetxt);
                                    from = v.findViewById(R.id.expfromtxt);

                                    imagechosen = v.findViewById(R.id.expimgdata);

                                    try {
                                        if (expenses.getexpense_imagechosen() == null) {
                                            checkimage = 0;
                                        } else {
                                            Bitmap bmp = BitmapFactory.decodeByteArray(expenses.getexpense_imagechosen(), 0, expenses.getexpense_imagechosen().length);
                                            imagechosen.setImageBitmap(Bitmap.createScaledBitmap(bmp, bmp.getWidth(),
                                                    bmp.getHeight(), false));
                                            checkimage = 1;
                                        }
                                    }catch (NullPointerException e){
                                        checkimage = 0 ;
                                    }

                                    if(checkimage == 1){
                                        notice.setVisibility(View.GONE);
                                        imagechosen.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        notice.setVisibility(View.VISIBLE);
                                        imagechosen.setVisibility(View.GONE);
                                    }


                                    accounts.setText(expenses.getexpense_account());

                                    String temp =formatter.format(expenses.getexpense_amount());
                                    amoount.setText(temp);

                                    Drawable resImg = context.getResources().getDrawable(generator.images[expenses.getexpense_image()-1]);
                                    circle.setImageDrawable(resImg);

                                    categories.setText(expenses.getexpense_category());
                                    dates.setText(expenses.getexpense_date());
                                    notes.setText(expenses.getexpense_notes());
                                    from.setText(expenses.getexpense_to());



                                    dialog.setView(v);
                                    dialog.setTitle("ID : " + expenses.getexpense_id());
                                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();




                                    return true;
                                case R.id.incedititem:
                                    Intent editexpenses = new Intent(context,editexpense.class);
                                    editexpenses.putExtra("expenseid",expenses.getexpense_id());
                                    generator.showadapterexpense=null;
                                    generator.showdataexpensesch=null;
                                    generator.showadapterexpense=expadapter;
                                    generator.showdataexpense=dataexp;
                                    startActivity(editexpenses);
                                    return true;
                                case R.id.incdeleteitem:
                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle);
                                    builder.setTitle("Confirm");
                                    builder.setMessage("Are you sure to delete expense on "+expenses.getexpense_date()+" with "+formatter.format(expenses.getexpense_amount())+" amount and uses "+expenses.getexpense_account()+" and categorized as "+expenses.getexpense_category()+" ?");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            expense exp = dbase.getexpense(expenses.getexpense_id());

                                            if(exp.getexpense_isdone()==1){
                                                account acc = dbase.getaccount(expenses.getexpense_account());

                                                acc.setAccount_balance_current(String.valueOf(generator.makedouble(acc.getAccount_balance().replace("",""))+expenses.getexpense_amount()));

                                                dbase.updateaccount(acc,acc.getUsername(),acc.getAccount_name());

                                                dbase.deleteexpense(expenses.getexpense_id());
                                                reloaddata();
                                            }
                                            else {
                                                dbase.deleteexpense(expenses.getexpense_id());
                                                reloaddata();
                                            }

                                            if(generator.adapter!=null){
                                                generator.adapter.notifyDataSetChanged();
                                            }

                                            /*DocumentReference docRef = fdb.collection("expense").document(holder.documenref);
                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            Log.d("Documentdata", "DocumentSnapshot data: " + document.getData());
                                                            if(document.getData().get("expense_isdone").toString().equals("1")){
                                                                fdb.collection("account")
                                                                        .whereEqualTo("account_name", document.getData().get("expense_account"))
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    for (QueryDocumentSnapshot document1 : task.getResult()) {
                                                                                        if(document.getData().get("expense_isdone").toString().equals("1")){
                                                                                                Map<String, Object> data = new HashMap<>();
                                                                                                Double result=generator.makedouble(document1.getData().get("account_balance_current").toString())+generator.makedouble(document.getData().get("expense_amount").toString());
                                                                                                data.put("account_balance_current", String.valueOf(result));

                                                                                                fdb.collection("account").document(document1.getId())
                                                                                                        .set(data, SetOptions.merge());

                                                                                            fdb.collection("expense").document(holder.documenref)
                                                                                                    .delete()
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                            reloaddata();
                                                                                                            dataexp.clear();
                                                                                                            expadapter.notifyDataSetChanged();
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
                                                                                            fdb.collection("expense").document(holder.documenref)
                                                                                                    .delete()
                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                            reloaddata();
                                                                                                            dataexp.clear();
                                                                                                            expadapter.notifyDataSetChanged();
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
                                            });*/


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
            LinearLayout items;
            public CircleImageView image;
            View color;
            public TextView expensecategory, expensefrom, expensevalue ,expenseaccount,expensedate,expensenote;

            public MyViewHolder(View view) {
                super(view);
                image = view.findViewById(R.id.expimagelistdata);
                color= view.findViewById(R.id.colortype);
                items = view.findViewById(R.id.linearitemsexpense);
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
