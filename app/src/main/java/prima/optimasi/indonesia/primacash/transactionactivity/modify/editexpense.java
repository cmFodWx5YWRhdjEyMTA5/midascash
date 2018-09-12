package prima.optimasi.indonesia.primacash.transactionactivity.modify;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.SQLiteHelper;
import prima.optimasi.indonesia.primacash.commaedittext;
import prima.optimasi.indonesia.primacash.generator;
import prima.optimasi.indonesia.primacash.objects.account;
import prima.optimasi.indonesia.primacash.objects.expense;
import prima.optimasi.indonesia.primacash.objects.income;

public class editexpense extends AppCompatActivity{

    FirebaseFirestore fdb;

    View layoutitems;
    Button save,cancel;

    ImageView imagechosen;


    Double calculate=0.0d,comparer=0.0d,result=0.0d;

    DecimalFormat formatter = new DecimalFormat("###,###,###.00");

    TextView expnote,expto;

    EditText inputvalue;

    SQLiteHelper dbase;

    byte[] dataimg;

    String expenseid;

    LinearLayout accountselect,categoryselect,dateselect;
    TextView selectedaccount,selectedcategory,selectedate,currencytext;

    CircleImageView chosenimage;

    List<expense> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_edit);

        dbase = new SQLiteHelper(this);

        expenseid = getIntent().getStringExtra("expenseid");

        generator.incdatesys=0;

        data = new ArrayList<>();
        fdb = FirebaseFirestore.getInstance();

        final String[] accdoc = {""};

        final int[] isdone = {4};

        layoutitems = findViewById(R.id.viewpagerexpense);

        Button notsave = layoutitems.findViewById(R.id.btnsaveexpense);
        Button notcancel = layoutitems.findViewById(R.id.btncancelexpense);

        ImageButton camera = layoutitems.findViewById(R.id.exptpic);
        ImageButton galery = layoutitems.findViewById(R.id.exptgal);

        imagechosen = layoutitems.findViewById(R.id.expimgdata);


        notsave.setVisibility(View.GONE);
        notcancel.setVisibility(View.GONE);

        categoryselect = layoutitems.findViewById(R.id.linearcattap);
        accountselect = layoutitems.findViewById(R.id.linearacctap);
        dateselect = layoutitems.findViewById(R.id.linearaccdat);

        chosenimage = layoutitems.findViewById(R.id.expcatpic);



        selectedcategory = layoutitems.findViewById(R.id.expcat);
        selectedate = layoutitems.findViewById(R.id.expdat);
        selectedaccount = layoutitems.findViewById(R.id.expacc);

        inputvalue = layoutitems.findViewById(R.id.input_value);

        inputvalue.addTextChangedListener(new commaedittext(inputvalue));

        expto= layoutitems.findViewById(R.id.expto);
        expnote = layoutitems.findViewById(R.id.expnote);

        save = findViewById(R.id.btnsaveexpenseedit);
        cancel = findViewById(R.id.btncancelexpenseedit);


        currencytext = layoutitems.findViewById(R.id.allcurrency);



        categoryselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generator.chosecategory(editexpense.this,selectedcategory,chosenimage);
            }
        });

        accountselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generator.choseaccount(editexpense.this,selectedaccount,currencytext);
            }
        });

        dateselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generator.chosedate(editexpense.this,selectedate);
            }
        });
        galery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, 4);
                    } else {
                        try {
                            ActivityCompat.requestPermissions(editexpense.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    3);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                }


            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                int permissionCheckStorage = ContextCompat.checkSelfPermission(editexpense.this,
                        Manifest.permission.CAMERA);
                if (permissionCheckStorage == PackageManager.PERMISSION_DENIED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1004);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(inputvalue.getText().toString().equals("")){
                    Toast.makeText(editexpense.this, "Value Required", Toast.LENGTH_SHORT).show();
                }else {

                    accdoc[0] = expenseid;

                    expense datanow = dbase.getexpense(accdoc[0]);

                    account acc = dbase.getaccount(selectedaccount.getText().toString());

                    calculate = generator.makedouble(acc.getAccount_balance_current());

                    Date date = Calendar.getInstance().getTime();

                    Date today = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date chosendated=null;
                    try {
                        chosendated = format.parse(selectedate.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String temp="1";
                    if(isdone[0]==1){

                        if(chosendated.after(date)) {
                            temp = "0";
                            if(generator.makedouble(inputvalue.getText().toString().replace(",",""))>comparer){
                                result=generator.makedouble(inputvalue.getText().toString().replace(",",""))-comparer;
                                acc.setAccount_balance_current(String.valueOf(calculate+result+comparer));
                                dbase.updateaccount(acc,acc.getUsername(),acc.getAccount_name());
                            }
                            else if(generator.makedouble(inputvalue.getText().toString().replace(",",""))<comparer){
                                result= comparer - generator.makedouble(inputvalue.getText().toString().replace(",",""));
                                acc.setAccount_balance_current(String.valueOf(calculate-result+generator.makedouble(inputvalue.getText().toString().replace(",",""))));
                                dbase.updateaccount(acc,acc.getUsername(),acc.getAccount_name());
                            }
                            else {
                                acc.setAccount_balance_current(String.valueOf(calculate+generator.makedouble(inputvalue.getText().toString().replace(",",""))));
                                dbase.updateaccount(acc,acc.getUsername(),acc.getAccount_name());
                            }
                        }
                        else {
                            temp="1";
                            if(generator.makedouble(inputvalue.getText().toString().replace(",",""))>comparer){
                                result =generator.makedouble(inputvalue.getText().toString().replace(",","")) - comparer;
                                acc.setAccount_balance_current(String.valueOf(calculate-result));
                                dbase.updateaccount(acc,acc.getUsername(),acc.getAccount_name());
                            }
                            else if(generator.makedouble(inputvalue.getText().toString().replace(",",""))<comparer){

                                result =comparer - generator.makedouble(inputvalue.getText().toString().replace(",",""));
                                acc.setAccount_balance_current(String.valueOf(calculate+result));
                                dbase.updateaccount(acc,acc.getUsername(),acc.getAccount_name());

                            }
                            else {

                            }
                        }
                        isdone[0]=4;
                    }
                    else {
                        temp = "0";

                        if(chosendated.after(date)) {

                        }else {
                            temp = "1";
                            if(generator.makedouble(inputvalue.getText().toString().replace(",",""))>comparer){
                                Map<String, Object> data = new HashMap<>();
                                result=generator.makedouble(inputvalue.getText().toString().replace(",",""))-comparer;
                                data.put("account_balance_current", String.valueOf(calculate-result+comparer) );

                                fdb.collection("account").document(accdoc[0])
                                        .set(data, SetOptions.merge());



                            }
                            else if(generator.makedouble(inputvalue.getText().toString().replace(",",""))<comparer){
                                Map<String, Object> data = new HashMap<>();
                                result=comparer-generator.makedouble(inputvalue.getText().toString().replace(",",""));
                                data.put("account_balance_current", String.valueOf(calculate+result-comparer));

                                fdb.collection("account").document(accdoc[0])
                                        .set(data, SetOptions.merge());


                            }
                            else {
                                Map<String, Object> data = new HashMap<>();
                                data.put("account_balance_current", String.valueOf(calculate-comparer));
                                fdb.collection("account").document(accdoc[0])
                                        .set(data, SetOptions.merge());
                            }
                        }
                    }
                    if(isdone[0]==1){

                        if(chosendated.after(date)) {
                            temp = "0";
                            if(generator.makedouble(inputvalue.getText().toString().replace(",",""))>comparer){

                                result=generator.makedouble(inputvalue.getText().toString().replace(",",""))-comparer;
                                acc.setAccount_balance_current(String.valueOf(calculate-result+comparer));
                                dbase.updateaccount(acc,acc.getUsername(),acc.getAccount_name());

                            }
                            else if(generator.makedouble(inputvalue.getText().toString().replace(",",""))<comparer){

                                result=comparer-generator.makedouble(inputvalue.getText().toString().replace(",",""));
                                acc.setAccount_balance_current(String.valueOf(calculate-result+comparer));
                                dbase.updateaccount(acc,acc.getUsername(),acc.getAccount_name());


                            }
                            else {
                                acc.setAccount_balance_current(String.valueOf(calculate-generator.makedouble(inputvalue.getText().toString().replace(",",""))));
                                dbase.updateaccount(acc,acc.getUsername(),acc.getAccount_name());
                            }
                        }
                        else {
                            temp="1";
                            if(generator.makedouble(inputvalue.getText().toString().replace(",",""))>comparer){
                                result=generator.makedouble(inputvalue.getText().toString().replace(",",""))-comparer;
                                acc.setAccount_balance_current(String.valueOf(calculate+result));
                                dbase.updateaccount(acc,acc.getUsername(),acc.getAccount_name());
                            }
                            else if(generator.makedouble(inputvalue.getText().toString().replace(",",""))<comparer){

                                result=comparer-generator.makedouble(inputvalue.getText().toString().replace(",",""));
                                acc.setAccount_balance_current(String.valueOf(calculate-result));
                                dbase.updateaccount(acc,acc.getUsername(),acc.getAccount_name());
                            }
                            else {

                            }
                        }
                        isdone[0]=4;
                    }
                    else {
                        temp = "0";

                        if(chosendated.after(date)) {

                        }else {
                            temp = "1";
                            if(generator.makedouble(inputvalue.getText().toString().replace(",",""))>comparer){

                                result=generator.makedouble(inputvalue.getText().toString().replace(",",""))-comparer;
                                acc.setAccount_balance_current(String.valueOf(calculate+result+comparer));
                                dbase.updateaccount(acc,acc.getUsername(),acc.getAccount_name());



                            }
                            else if(generator.makedouble(inputvalue.getText().toString().replace(",",""))<comparer){

                                result=comparer-generator.makedouble(inputvalue.getText().toString().replace(",",""));
                                acc.setAccount_balance_current(String.valueOf(calculate+result));
                                dbase.updateaccount(acc,acc.getUsername(),acc.getAccount_name());


                            }
                            else {
                                acc.setAccount_balance_current(String.valueOf(calculate+comparer));
                                dbase.updateaccount(acc,acc.getUsername(),acc.getAccount_name());
                            }
                        }
                    }



                    datanow.setexpense_amount(generator.makedouble(inputvalue.getText().toString().replace(",","")));
                    datanow.setexpense_account(selectedaccount.getText().toString());
                    datanow.setexpense_category(selectedcategory.getText().toString());
                    datanow.setexpense_notes(expnote.getText().toString());
                    datanow.setexpense_isdone(Integer.parseInt(temp));
                    datanow.setexpense_date(selectedate.getText().toString());
                    datanow.setexpense_to(expto.getText().toString());
                    datanow.setexpense_imagechosen(dataimg);

                    dbase.updateexpense(datanow,expenseid,datanow.getUsername());

                    Toast.makeText(editexpense.this, "expense Edited", Toast.LENGTH_SHORT).show();
                    reloaddata();
                    if(generator.adapter!=null){
                        generator.adapter.notifyDataSetChanged();
                    }

                    /*mapdata.put("income_amount",inputvalue.getText().toString().replace(",",""));
                    mapdata.put("income_account",selectedaccount.getText().toString());
                    mapdata.put("income_category",selectedcategory.getText().toString());
                    mapdata.put("income_notes", incnote.getText().toString());
                    mapdata.put("income_isdone", temp);
                    mapdata.put("income_date",selectedate.getText().toString());
                    mapdata.put("income_datesys",generator.incdatesys);
                    mapdata.put("income_from",incfrom.getText().toString());
                    mapdata.put("income_lastedit", Calendar.getInstance().getTimeInMillis());*/

                    generator.incdatesys=0;
                    finish();
/*
                    fdb.collection("account")
                            .whereEqualTo("account_name", selectedaccount.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document1 : task.getResult()) {
                                            String temp = "1";

                                            Date date = Calendar.getInstance().getTime();

                                            Date today = new Date();
                                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                            Date chosendated=null;
                                            try {
                                                chosendated = format.parse(selectedate.getText().toString());
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            Log.d("Documentdata", document1.getId() + " => " + document1.getData());
                                            accdoc[0] = document1.getId();
                                            calculate = generator.makedouble(document1.getData().get("account_balance_current").toString());
                                            if(isdone[0]==1){

                                                if(chosendated.after(date)) {
                                                    temp = "0";
                                                    if(generator.makedouble(inputvalue.getText().toString().replace(",",""))>comparer){
                                                        Map<String, Object> data = new HashMap<>();
                                                        result=generator.makedouble(inputvalue.getText().toString().replace(",",""))-comparer;
                                                        data.put("account_balance_current", String.valueOf(calculate+result+comparer) );

                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());



                                                    }
                                                    else if(generator.makedouble(inputvalue.getText().toString().replace(",",""))<comparer){
                                                        Map<String, Object> data = new HashMap<>();
                                                        result=comparer-generator.makedouble(inputvalue.getText().toString().replace(",",""));
                                                        data.put("account_balance_current", String.valueOf(calculate-result+generator.makedouble(inputvalue.getText().toString().replace(",",""))) );

                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());


                                                    }
                                                    else {
                                                        Map<String, Object> data = new HashMap<>();
                                                        data.put("account_balance_current", String.valueOf(calculate+generator.makedouble(inputvalue.getText().toString().replace(",",""))));
                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());
                                                    }
                                                }
                                                else {
                                                    temp="1";
                                                    if(generator.makedouble(inputvalue.getText().toString().replace(",",""))>comparer){
                                                        Map<String, Object> data = new HashMap<>();
                                                        result=generator.makedouble(inputvalue.getText().toString().replace(",",""))-comparer;
                                                        data.put("account_balance_current", String.valueOf(calculate-result) );

                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());
                                                    }
                                                    else if(generator.makedouble(inputvalue.getText().toString().replace(",",""))<comparer){
                                                        Map<String, Object> data = new HashMap<>();
                                                        result=comparer-generator.makedouble(inputvalue.getText().toString().replace(",",""));
                                                        data.put("account_balance_current", String.valueOf(calculate+result) );

                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());
                                                    }
                                                    else {

                                                    }
                                                }
                                                isdone[0]=4;
                                            }
                                            else {
                                                temp = "0";

                                                if(chosendated.after(date)) {

                                                }else {
                                                    temp = "1";
                                                    if(generator.makedouble(inputvalue.getText().toString().replace(",",""))>comparer){
                                                        Map<String, Object> data = new HashMap<>();
                                                        result=generator.makedouble(inputvalue.getText().toString().replace(",",""))-comparer;
                                                        data.put("account_balance_current", String.valueOf(calculate-result+comparer) );

                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());



                                                    }
                                                    else if(generator.makedouble(inputvalue.getText().toString().replace(",",""))<comparer){
                                                        Map<String, Object> data = new HashMap<>();
                                                        result=comparer-generator.makedouble(inputvalue.getText().toString().replace(",",""));
                                                        data.put("account_balance_current", String.valueOf(calculate+result-comparer));

                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());


                                                    }
                                                    else {
                                                        Map<String, Object> data = new HashMap<>();
                                                        data.put("account_balance_current", String.valueOf(calculate-comparer));
                                                        fdb.collection("account").document(accdoc[0])
                                                                .set(data, SetOptions.merge());
                                                    }
                                                }
                                            }
                                            Toast.makeText(editexpense.this, "Income Edited", Toast.LENGTH_SHORT).show();
                                            reloaddata();
                                            if(generator.adapter!=null){
                                                generator.adapter.notifyDataSetChanged();
                                            }


                                            Map<String, Object> mapdata = new HashMap<>();
                                            mapdata.put("expense_amount",inputvalue.getText().toString().replace(",",""));
                                            mapdata.put("expense_account",selectedaccount.getText().toString());
                                            mapdata.put("expense_category",selectedcategory.getText().toString());
                                            mapdata.put("expense_notes", expnote.getText().toString());
                                            mapdata.put("expense_date",selectedate.getText().toString());
                                            mapdata.put("expense_isdone",temp);
                                            mapdata.put("expense_datesys",generator.incdatesys);
                                            mapdata.put("expense_to",expto.getText().toString());
                                            mapdata.put("expense_lastedit", Calendar.getInstance().getTimeInMillis());

                                            fdb.collection("expense").document(getIntent().getStringExtra("document"))
                                                    .set(mapdata, SetOptions.merge());


                                            finish();
                                            generator.incdatesys=0;



                                        }
                                    } else {
                                        Log.d("Documentdata", "Error getting documents: ", task.getException());
                                    }
                                }
                            });

*/
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        expense values = dbase.getexpense(expenseid);
        isdone[0] = values.getexpense_isdone();
        selectedaccount.setText(values.getexpense_account());
        comparer = values.getexpense_amount();
        String temp =formatter.format(values.getexpense_amount());
        inputvalue.setText(temp);
        Drawable resImg = editexpense.this.getResources().getDrawable(generator.images[values.getexpense_image()-1]);
        chosenimage.setImageDrawable(resImg);
        selectedcategory.setText(values.getexpense_category());
        selectedate.setText(values.getexpense_date());
        expnote.setText(values.getexpense_notes());
        expto.setText(values.getexpense_to());

        try {
            Bitmap bmp = BitmapFactory.decodeByteArray(values.getexpense_imagechosen(), 0, values.getexpense_imagechosen().length);

            imagechosen.setImageBitmap(Bitmap.createScaledBitmap(bmp, bmp.getWidth(),
                    bmp.getHeight(), false));
        }
        catch (NullPointerException e){
            Log.e("Null occured", "No image available " );
        }

        

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            this.finish();
        }
        return true;
    }

    public void reloaddata(){

        generator.showdataexpense.clear();

        List<expense> datainctemp = dbase.getAllexpense();

        for(int i = 0 ; i < datainctemp.size();i++){
            generator.showdataexpense.add(datainctemp.get(i));
            Collections.sort(generator.showdataexpense);
            Collections.reverse(generator.showdataexpense);
        }

        if(generator.showadapterexpense!=null){
            generator.showadapterexpense.notifyDataSetChanged();
        }


        /*generator.showdataexpense.clear();
        generator.showadapterexpense.notifyDataSetChanged();
        fdb.collection("expense")
                .whereEqualTo("expense_isdated", "0")
                .orderBy("expense_datesys", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                expense values = new expense();
                                values.setexpensedoc(document.getId());
                                values.setexpense_account(document.getData().get("expense_account").toString());
                                values.setexpense_amount(Double.parseDouble(document.getData().get("expense_amount").toString()));
                                values.setexpense_category(document.getData().get("expense_category").toString());
                                values.setexpense_date(document.getData().get("expense_date").toString());
                                values.setexpense_type("K");
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
                                                        generator.showdataexpense.add(values);
                                                        generator.showadapterexpense.notifyDataSetChanged();
                                                        Collections.sort(generator.showdataexpense);
                                                        Collections.reverse(generator.showdataexpense);
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
                */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1004 && resultCode == Activity.RESULT_OK) {

            Log.e("camera sucessful",requestCode + " ");
            /*ImageView imageView = (ImageView) child.findViewById(R.id.incimgdata);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);*/

            ImageView imageView = (ImageView) findViewById(R.id.incimgdata);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            dataimg = baos.toByteArray();
        }
        else if(requestCode == 4 && resultCode == Activity.RESULT_OK){
            Log.e("galery sucessful",requestCode + " ");
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.incimgdata);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            dataimg = baos.toByteArray();
        }
    }
}
