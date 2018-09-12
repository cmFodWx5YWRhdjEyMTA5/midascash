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
import com.google.android.gms.tasks.OnSuccessListener;
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
import prima.optimasi.indonesia.primacash.objects.income;

public class editincome extends AppCompatActivity {
    FirebaseFirestore fdb;

    View layoutitems;
    Button save,cancel;

    ImageView imagechosen ;

    income recorderincome;

    SQLiteHelper dbase;
    
    String incomeid="";

    Double calculate=0.0d,comparer=0.0d,result=0.0d;

    DecimalFormat formatter = new DecimalFormat("###,###,###.00");

    TextView incnote,incfrom;

    byte[] dataimg;

    EditText inputvalue;

    LinearLayout accountselect,categoryselect,dateselect;
    TextView selectedaccount,selectedcategory,selectedate,currencytext;

    CircleImageView chosenimage;

    List<income> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_edit);



        dbase = new SQLiteHelper(this);
        
        incomeid = getIntent().getStringExtra("incomeid");

        generator.incdatesys=0;

        data = new ArrayList<>();
        fdb = FirebaseFirestore.getInstance();

        final String[] accdoc = {""};

        final int[] isdone = {4};

        layoutitems = findViewById(R.id.viewpagerexpense);

        Button notsave = layoutitems.findViewById(R.id.btnsaveincome);
        Button notcancel = layoutitems.findViewById(R.id.btncancelincome);

        ImageButton camera = layoutitems.findViewById(R.id.inctpic);
        ImageButton galery = layoutitems.findViewById(R.id.inctgal);

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
                            ActivityCompat.requestPermissions(editincome.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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
                int permissionCheckStorage = ContextCompat.checkSelfPermission(editincome.this,
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

        imagechosen = layoutitems.findViewById(R.id.incimgdata);


        notsave.setVisibility(View.GONE);
        notcancel.setVisibility(View.GONE);


        categoryselect = layoutitems.findViewById(R.id.inccattap);
        accountselect = layoutitems.findViewById(R.id.incacctap);
        dateselect = layoutitems.findViewById(R.id.incdatetap);

        chosenimage = layoutitems.findViewById(R.id.inccatpic);

        selectedcategory = layoutitems.findViewById(R.id.inccatname);
        selectedate = layoutitems.findViewById(R.id.incdateselect);
        selectedaccount = layoutitems.findViewById(R.id.incacctxt);

        inputvalue = layoutitems.findViewById(R.id.input_value);

        inputvalue.addTextChangedListener(new commaedittext(inputvalue));

        incfrom = layoutitems.findViewById(R.id.incfromtxt);
        incnote = layoutitems.findViewById(R.id.incnotetxt);

        save = findViewById(R.id.btnsaveincomeedit);
        cancel = findViewById(R.id.btncancelincomeedit);


        currencytext = layoutitems.findViewById(R.id.allcurrency);



        categoryselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generator.chosecategory(editincome.this,selectedcategory,chosenimage);
            }
        });

        accountselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generator.choseaccount(editincome.this,selectedaccount,currencytext);
            }
        });

        dateselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generator.chosedate(editincome.this,selectedate);
            }
        });




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputvalue.getText().toString().equals("")){
                    Toast.makeText(editincome.this, "Value Required", Toast.LENGTH_SHORT).show();
                }else {

                    accdoc[0] = incomeid;
                    
                    income datanow = dbase.getincome(accdoc[0]);
                    
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



                    datanow.setIncome_amount(generator.makedouble(inputvalue.getText().toString().replace(",","")));
                    datanow.setIncome_account(selectedaccount.getText().toString());
                    datanow.setIncome_category(selectedcategory.getText().toString());
                    datanow.setIncome_notes(incnote.getText().toString());
                    datanow.setIncome_isdone(Integer.parseInt(temp));
                    datanow.setIncome_date(selectedate.getText().toString());
                    datanow.setIncome_from(incfrom.getText().toString());
                    datanow.setIncome_imagechosen(dataimg);

                    dbase.updateincome(datanow,incomeid,datanow.getUsername());

                    Toast.makeText(editincome.this, "Income Edited", Toast.LENGTH_SHORT).show();
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


                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        income values = dbase.getincome(getIntent().getStringExtra("incomeid"));

        isdone[0] = values.getIncome_isdone();

        try {
            Bitmap bmp = BitmapFactory.decodeByteArray(values.getIncome_imagechosen(), 0, values.getIncome_imagechosen().length);

            imagechosen.setImageBitmap(Bitmap.createScaledBitmap(bmp, bmp.getWidth(),
                    bmp.getHeight(), false));
        }
        catch (NullPointerException e){
            Log.e("Null occured", "No image available " );
        }
        selectedaccount.setText(values.getIncome_account());
        comparer = values.getIncome_amount();
        String temp =formatter.format(values.getIncome_amount());
        inputvalue.setText(temp);

        Drawable resImg = editincome.this.getResources().getDrawable(generator.images[values.getIncome_image()-1]);
        chosenimage.setImageDrawable(resImg);
        selectedcategory.setText(values.getIncome_category());
        selectedate.setText(values.getIncome_date());
        incnote.setText(values.getIncome_notes());
        incfrom.setText(values.getIncome_from());

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

        generator.showdataincome.clear();

        List<income> datainctemp = dbase.getAllincome();

        for(int i = 0 ; i < datainctemp.size();i++){
            generator.showdataincome.add(datainctemp.get(i));
            Collections.sort(generator.showdataincome);
            Collections.reverse(generator.showdataincome);
        }

        if(generator.showadapterincome!=null){
            generator.showadapterincome.notifyDataSetChanged();
        }

        /*fdb.collection("income")
                .whereEqualTo("income_isdated", "0")
                .orderBy("income_datesys", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            generator.showdataincome.clear();
                            if(generator.showadapterincome!=null){
                                generator.showadapterincome.notifyDataSetChanged();
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
                                                        generator.showdataincome.add(values);
                                                    }
                                                    generator.showadapterincome.notifyDataSetChanged();
                                                    Collections.sort(generator.showdataincome);
                                                    Collections.reverse(generator.showdataincome);
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
