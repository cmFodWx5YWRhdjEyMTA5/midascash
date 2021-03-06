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
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.Button;
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
import prima.optimasi.indonesia.primacash.objects.income;
import prima.optimasi.indonesia.primacash.transactionactivity.listincome;
import prima.optimasi.indonesia.primacash.transactionactivity.modify.editincome;

public class fragment_income_show extends Fragment {

    LayoutInflater inflater;
    FirebaseFirestore fdb;

    SQLiteHelper dbase;



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
        dbase = new SQLiteHelper(getActivity());
        fdb = FirebaseFirestore.getInstance();

        datainc = dbase.getAllincome();

        View view = inflater.inflate(R.layout.row_activity_fragment_income_layout, container, false);

        recycler = view.findViewById(R.id.incomelistdata);

        if(datainc.size()!=0){
            Log.e("test category", "onCreateView: "+ datainc.get(0).getIncome_category() );
        }



        /*List<income> tempinc =

        for(int i=0;i<tempinc.size();i++){
            income a = new income();

            a.setIncome_count(tempinc.get(i).getIncome_count());
            a.setIncome_period(tempinc.get(i).getIncome_period());
            a.setIncome_times(tempinc.get(i).getIncome_times());
            a.setIncome_isdated(tempinc.get(i).getIncome_isdated());
            a.setIncome_image(tempinc.get(i).getIncome_image());
            a.setIncome_imagechosen(tempinc.get(i).getIncome_imagechosen());
            a.setIncome_category(tempinc.get(i).getIncome_category());
            a.setIncome_account(tempinc.get(i).getIncome_account());
            a.setIncome_from(tempinc.get(i).getIncome_from());
            a.setIncome_id(tempinc.get(i).getIncome_id());
            a.setIncome_type(tempinc.get(i).getIncome_type());
            a.setUsername(tempinc.get(i).getUsername());
            a.setIncome_notes(tempinc.get(i).getIncome_notes());
            a.setIncome_date(tempinc.get(i).getIncome_date());
*/

            Collections.sort(datainc);
            Collections.reverse(datainc);



        if(incadapter!=null){
            incadapter.notifyDataSetChanged();
        }

        incadapter = new listitemincome(getActivity(),datainc,false);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recycler.setLayoutManager(mLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(incadapter);

        /*fdb.collection("income")
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

                                final int[] count = {0};

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

                        } else {
                            Log.w("data", "Error getting documents.", task.getException());
                        }
                    }
                });

        // Inflate the layout for this fragment
        */
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

            Drawable resImg=null;
            if(incomes.getIncome_category().equals("-")){
                resImg = context.getResources().getDrawable(generator.images[36]);
            }
            else {
                resImg = context.getResources().getDrawable(generator.images[incomes.getIncome_image()-1]);
            }
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
                                case R.id.incviewitem:
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle);

                                    View v = LayoutInflater.from(context).inflate(R.layout.layout_display_income,null);

                                    int checkimage = 0;

                                    TextView dates,categories,accounts,amoount,currency,notes,from,notice;
                                    ImageView imagechosen;
                                    CircleImageView circle;

                                    currency = v.findViewById(R.id.allcurrency);
                                    amoount = v.findViewById(R.id.incamountview);
                                    categories =v.findViewById(R.id.inccatname);
                                    circle = v.findViewById(R.id.inccatpic);
                                    accounts = v.findViewById(R.id.incacctxt);
                                    dates = v.findViewById(R.id.incdateselect);

                                    notice = v.findViewById(R.id.incimagenote);
                                    notice.setVisibility(View.GONE);

                                    notes = v.findViewById(R.id.incnotetxt);
                                    from = v.findViewById(R.id.incfromtxt);

                                    imagechosen = v.findViewById(R.id.incimgdata);

                                    try {
                                        if (incomes.getIncome_imagechosen() == null) {
                                            checkimage = 0;
                                        } else {
                                            Bitmap bmp = BitmapFactory.decodeByteArray(incomes.getIncome_imagechosen(), 0, incomes.getIncome_imagechosen().length);
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


                                    accounts.setText(incomes.getIncome_account());

                                    String temp =formatter.format(incomes.getIncome_amount());
                                    amoount.setText(temp);

                                    Drawable resImg = context.getResources().getDrawable(generator.images[incomes.getIncome_image()-1]);
                                    circle.setImageDrawable(resImg);

                                    categories.setText(incomes.getIncome_category());
                                    dates.setText(incomes.getIncome_date());
                                    notes.setText(incomes.getIncome_notes());
                                    from.setText(incomes.getIncome_from());



                                    dialog.setView(v);
                                    dialog.setTitle("ID : " + incomes.getIncome_id());
                                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();




                                    return true;
                                case R.id.incedititem:
                                    Intent editincomes = new Intent(context,editincome.class);
                                    editincomes.putExtra("incomeid",incomes.getIncome_id());
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

                                            income inc = dbase.getincome(incomes.getIncome_id());

                                            if(inc.getIncome_isdone()==1){
                                                account acc = dbase.getaccount(incomes.getIncome_account());

                                                acc.setAccount_balance_current(String.valueOf(generator.makedouble(acc.getAccount_balance().replace("",""))-incomes.getIncome_amount()));

                                                dbase.updateaccount(acc,acc.getUsername(),acc.getAccount_name());

                                                dbase.deleteincome(incomes.getIncome_id());
                                                reloaddata();
                                            }
                                            else {
                                                dbase.deleteincome(incomes.getIncome_id());
                                                reloaddata();
                                            }

                                            if(generator.adapter!=null){
                                                generator.adapter.notifyDataSetChanged();
                                            }




                                            /*DocumentReference docRef = fdb.collection("income").document(holder.documenref);
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
            return this.incomelist.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            String documenref="";
            public CircleImageView image;
            View color;
            public CardView items;
            public TextView incomecategory, incomefrom, incomevalue ,incomeaccount,incomedate,incomenote;

            public MyViewHolder(View view) {
                super(view);
                items = view.findViewById(R.id.card_view);
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

        datainc.clear();
        List<income> datainctemp = dbase.getAllincome();

        for(int i = 0 ; i < datainctemp.size();i++){
            datainc.add(datainctemp.get(i));
            Collections.sort(datainc);
            Collections.reverse(datainc);
        }



        if(incadapter!=null){
            incadapter.notifyDataSetChanged();
        }
        /*fdb.collection("income")
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
                });*/
    }
}
