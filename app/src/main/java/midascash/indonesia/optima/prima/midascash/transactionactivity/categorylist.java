package midascash.indonesia.optima.prima.midascash.transactionactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.SQLiteHelper;
import midascash.indonesia.optima.prima.midascash.generator;
import midascash.indonesia.optima.prima.midascash.recycleview.adapterviewcategory;

/**
 * Created by rwina on 4/23/2018.
 */

public class categorylist extends AppCompatActivity {

    SQLiteHelper db;

    ListView categorylist;

    FirebaseFirestore fdb;

    Map<String, Object> allcategory;

    MySimpleArrayAdapter adapter;
    List<MyListObject> values;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);

        fdb =FirebaseFirestore.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        categorylist = findViewById(R.id.lvcategory);

        db = new SQLiteHelper(categorylist.this);
        values = new ArrayList<MyListObject>();

        loaddata();
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
        if (id == R.id.action_addcat) {

            android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(categorylist.this, R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
            LayoutInflater inflater = getLayoutInflater();
            dialogBuilder.setTitle("New Category");
            View dialogView = inflater.inflate(R.layout.layout_input_kategori, null);

            final CircleImageView selected = dialogView.findViewById(R.id.imgselected);
            adapterviewcategory adapter = new adapterviewcategory(categorylist.this, selected, 20);

            RecyclerView recyclerView = dialogView.findViewById(R.id.recyclercategoryitem);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(categorylist.this, 5));

            recyclerView.setAdapter(adapter);

            final EditText categoryname = dialogView.findViewById(R.id.categoryname);

            Button buttonshowall = dialogView.findViewById(R.id.categoryedit);

            buttonshowall.setVisibility(View.GONE);

            dialogBuilder.setPositiveButton("Save", null);

            dialogBuilder.setNegativeButton("Cancel", null);

            dialogBuilder.setView(dialogView);

            final android.support.v7.app.AlertDialog dialog1 = dialogBuilder.create();
            dialog1.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Log.e("selected", "0");
                    Button button = ((android.support.v7.app.AlertDialog) dialog1).getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("selected", "1");
                            if (categoryname.getText().toString().equals("")) {
                                Toast.makeText(categorylist.this,"Category name is Missing",Toast.LENGTH_SHORT).show();
                            } else {
                                if (Integer.parseInt(selected.getTag().toString()) == 0) {
                                    Toast.makeText(categorylist.this,"Please Select Picture",Toast.LENGTH_SHORT).show();
                                } else {
                                    final int[] statuscode = {0};
                                    Log.e("selected", "2");
                                    Toast.makeText(categorylist.this,"Please Wait",Toast.LENGTH_SHORT).show();
                                    fdb.collection("category")
                                            .orderBy("category_name", Query.Direction.ASCENDING)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.e("selected", "2,5");
                                                        int isdouble=0;
                                                        for (DocumentSnapshot document : task.getResult()) {
                                                            statuscode[0] =1;
                                                            Log.e("selected", "3");
                                                            if(document.getId()==null){
                                                                break;
                                                            }
                                                            else if (document.getData().get("category_name").toString().equals(categoryname.getText().toString()) && document.getData().get("category_name")!=null) {
                                                                Toast.makeText(categorylist.this, categoryname.getText().toString() + " is Already Registered", Toast.LENGTH_SHORT).show();
                                                                isdouble = 1;
                                                            }
                                                        }
                                                        if(statuscode[0]==0 || isdouble!=1){
                                                            Date c = Calendar.getInstance().getTime();
                                                            Map<String, Object> categorymap = new HashMap<>();
                                                            categorymap.put("category_name", categoryname.getText().toString());
                                                            categorymap.put("category_image", selected.getTag());

                                                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                            String formattedDate = df.format(c);

                                                            categorymap.put("category_createdate", c);
                                                            categorymap.put("category_status",1);
                                                            categorymap.put("username",generator.userlogin);

// Add a new document with a generated ID
                                                            fdb.collection("category")
                                                                    .add(categorymap)
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                            dialog1.dismiss();
                                                                            Toast.makeText(categorylist.this, "New Category Saved", Toast.LENGTH_SHORT).show();
                                                                            reloaddata();
                                                                            Log.e("added category", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(categorylist.this, "Error Occured : "+ e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                                            Log.e("error add", "Error adding document", e);
                                                                        }
                                                                    });

                                                        }
                                                    } else {
                                                        Log.e("category error add", "Error getting documents.", task.getException());
                                                    }
                                                }
                                            });

                                }
                            }
                        }
                    });
                }
            });

            dialog1.show();

        }
        return  true;
    }

    private class MySimpleArrayAdapter extends ArrayAdapter<MyListObject> {
        private final Context context;
        private final List<MyListObject> values;

        public MySimpleArrayAdapter(Context context, int resourceID, List<MyListObject> values) {
            super(context, resourceID, values);
            this.context = context;
            this.values = values;
        }

        private class ViewHolder {
            CircleImageView imageView;
            TextView textView;
            TextView hiddentextView;
            String categorycreatedate;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            MyListObject rowItem = getItem(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_layout_category, null);


            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.categorynameitem);
            holder.imageView =  convertView.findViewById(R.id.categoryitemimage);
            holder.hiddentextView =  convertView.findViewById(R.id.categorydatadocument);
            holder.categorycreatedate = rowItem.getCreatedate();


            holder.textView.setText(rowItem.getCategoryitem());
            Log.e("image id",String.valueOf(rowItem.getImage()));
            Drawable resImg = context.getResources().getDrawable(generator.images[rowItem.getImage()-1]);
            holder.imageView.setImageDrawable(resImg);
            holder.imageView.setTag(rowItem.getImage());
            holder.hiddentextView.setText(rowItem.getHiddendata());

            final ViewHolder finalHolder = holder;
            ViewHolder finalHolder1 = holder;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CharSequence[] item = {"Edit " + finalHolder.textView.getText().toString(),"Delete "+ finalHolder.textView.getText().toString()};
                    AlertDialog.Builder dialogs = new AlertDialog.Builder(categorylist.this,R.style.AppCompatAlertDialogStyle)
                            .setTitle("Options");
                    dialogs.setItems(item,new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            if(position==0) {
                                String temp="";
                                final String tempword=finalHolder.textView.getText().toString();
                                android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(categorylist.this,R.style.AppCompatAlertDialogStyle);
// ...Irrelevant code for customizing the buttons and title
                                LayoutInflater inflater = getLayoutInflater();
                                dialogBuilder.setTitle("Edit Category");
                                View dialogView = inflater.inflate(R.layout.layout_input_kategori, null);


                                final CircleImageView selected = dialogView.findViewById(R.id.imgselected);
                                selected.setImageDrawable(finalHolder.imageView.getDrawable());
                                selected.setTag(finalHolder.imageView.getTag());

                                Button buttonshowall = dialogView.findViewById(R.id.categoryedit);
                                buttonshowall.setVisibility(View.GONE);

                                final EditText categoryname = dialogView.findViewById(R.id.categoryname);
                                categoryname.setText(finalHolder.textView.getText().toString());

                                temp=finalHolder.textView.getText().toString();

                                adapterviewcategory adapter = new adapterviewcategory(categorylist.this, selected, 20);

                                RecyclerView recyclerView = dialogView.findViewById(R.id.recyclercategoryitem);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new GridLayoutManager(categorylist.this, 5));

                                recyclerView.setAdapter(adapter);

                                dialogBuilder.setPositiveButton("Save", null);

                                dialogBuilder.setNegativeButton("Cancel", null);

                                dialogBuilder.setView(dialogView);

                                final android.support.v7.app.AlertDialog dialog1 = dialogBuilder.create();
                                String finalTemp = temp;
                                dialog1.setOnShowListener(new DialogInterface.OnShowListener() {

                                    @Override
                                    public void onShow(DialogInterface dialogInterface) {

                                        Button button = ((android.support.v7.app.AlertDialog) dialog1).getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                                        button.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View view) {
                                                if(categoryname.getText().toString().equals("")){
                                                    Toast.makeText(categorylist.this,"Category name is Missing",Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Object tag = selected.getTag();
                                                    if(Integer.parseInt(selected.getTag().toString())==0){
                                                        Toast.makeText(categorylist.this,"Please Select a Picture",Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                            Date c = Calendar.getInstance().getTime();

                                                            Map<String, Object> categorymap = new HashMap<>();
                                                            categorymap.put("category_name", categoryname.getText().toString());
                                                            categorymap.put("category_image", selected.getTag());

                                                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                            String formattedDate = df.format(c);

                                                        categorymap.put("category_createdate", finalHolder.categorycreatedate);
                                                            categorymap.put("category_editedate", formattedDate);
                                                            categorymap.put("category_status",1);
                                                            categorymap.put("username",generator.userlogin);

                                                            fdb.collection("category").document(finalHolder1.hiddentextView.getText().toString())
                                                                    .set(categorymap)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Log.d("status write", "DocumentSnapshot successfully written!");
                                                                            Toast.makeText(categorylist.this, finalTemp +" Changed into "+categoryname.getText().toString(), Toast.LENGTH_SHORT).show();
                                                                            reloaddata();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.w("data error fdb", "Error writing document", e);
                                                                        }
                                                                    });


                                                            //db.deletecategory(finalHolder.textView.getText().toString());
                                                            //db.createCategory(new category(categoryname.getText().toString(), Integer.parseInt(selected.getTag().toString()), generator.userlogin, c, 1), generator.userlogin);

                                                            //Intent reload = new Intent(categorylist.this,categorylist.class);
                                                            //startActivity(reload);
                                                            //finish();
                                                            dialog1.dismiss();
                                                            Toast.makeText(categorylist.this, "Category Edited", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        });
                                    }
                                });
                                dialog1.show();

                            }
                            if(position==1) {
                                AlertDialog.Builder alerts = new AlertDialog.Builder(categorylist.this,R.style.AppCompatAlertDialogStyle)
                                        .setTitle("Delete " + finalHolder.textView.getText().toString()).setMessage("Proceed Deleting "+ finalHolder.textView.getText().toString());
                                alerts.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        fdb.collection("category").document(finalHolder.hiddentextView.getText().toString())
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.e("Finish delete", "DocumentSnapshot successfully deleted!");
                                                        reloaddata();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("Error delete", "Error deleting document", e);
                                                    }
                                                });

                                        dialog.dismiss();
                                    }
                                });
                                alerts.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }
                                });
                                alerts.show();


                            }
                            Toast.makeText(getApplicationContext(),"selected Item:"+position, Toast.LENGTH_SHORT).show();
                        }

                    });
                    dialogs.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    });
                    dialogs.show();
                }
            });


            return convertView;
        }

        @Override
        public MyListObject getItem(int position) {
            return values.get(position);
        }

    }
    private class MyListObject {
        private int image;
        private String country;
        private String hiddendata;
        private String createdate;

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getCategoryitem() {
            return country;
        }

        public void setCategoryname(String country) {
            this.country = country;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getHiddendata() {
            return hiddendata;
        }

        public void setHiddendata(String hiddendata) {
            this.hiddendata = hiddendata;
        }

        public String getCreatedate() {
            return createdate;
        }

        public void setCreatedate(String createdate) {
            this.createdate = createdate;
        }
    }

    private void categoryicons(View v, final CircleImageView circle) {

        /*
        final CircleImageView food = v.findViewById(R.id.imgfood);
        final CircleImageView cash = v.findViewById(R.id.imgcash);
        final CircleImageView bank = v.findViewById(R.id.imgbank);
        final CircleImageView cheque = v.findViewById(R.id.imgcheque);
        final CircleImageView lend = v.findViewById(R.id.imglend);
        final CircleImageView creditcard = v.findViewById(R.id.imgcreditcard);
        final CircleImageView truck = v.findViewById(R.id.imgtruck);
        final CircleImageView electric = v.findViewById(R.id.imgelectric);
        final CircleImageView ball = v.findViewById(R.id.imgball);
        final CircleImageView health = v.findViewById(R.id.imghealth);

        Button buttonshowall = v.findViewById(R.id.categoryedit);

        buttonshowall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allcategory = new Intent(categorylist.this,categorylist.class);
                startActivity(allcategory);
            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(food.getDrawable());
                circle.setTag("6");
            }
        });

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(cash.getDrawable());
                circle.setTag("1");
            }
        });

        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(bank.getDrawable());
                circle.setTag("2");
            }
        });

        cheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(cheque.getDrawable());
                circle.setTag("4");
            }
        });

        lend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(lend.getDrawable());
                circle.setTag("3");
            }
        });

        creditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(creditcard.getDrawable());
                circle.setTag("5");
            }
        });

        electric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(electric.getDrawable());
                circle.setTag("7");
            }
        });

        truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(truck.getDrawable());
                circle.setTag("8");
            }
        });

        ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(ball.getDrawable());
                circle.setTag("10");
            }
        });

        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle.setImageDrawable(health.getDrawable());
                circle.setTag("9");
            }
        });
    }
    */
    }
    private void loaddata(){
        fdb.collection("category")
                .orderBy("category_name", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (DocumentSnapshot document : task.getResult()) {
                                if(document.getId()==null){
                                    break;
                                }
                                    Log.e("getting data", document.getId() + " => " + document.getData());
                                    MyListObject b = new MyListObject();
                                    b.setCategoryname(document.getData().get("category_name").toString());
                                    b.setImage(Integer.parseInt(document.getData().get("category_image").toString()));
                                    b.setHiddendata(document.getId());
                                    b.setCreatedate(document.getData().get("category_createdate").toString());
                                    values.add(b);
                            }

                        } else {
                            Log.e("", "Error getting documents.", task.getException());
                        }
                        adapter = new MySimpleArrayAdapter(categorylist.this, R.layout.row_layout_category, values);
                        adapter.notifyDataSetChanged();
                        if (categorylist.getAdapter()==null){
                            categorylist.setAdapter(adapter);
                        }
                        if(generator.adapter!=null){
                            generator.adapter.notifyDataSetChanged();
                        }
                    }
                });

    }
    private void reloaddata(){
        fdb.collection("category")
                .orderBy("category_name", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            values.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                if(document.getId()==null){
                                    break;
                                }
                                Log.e("getting data", document.getId() + " => " + document.getData());
                                MyListObject b = new MyListObject();
                                b.setCategoryname(document.getData().get("category_name").toString());
                                b.setImage(Integer.parseInt(document.getData().get("category_image").toString()));
                                b.setHiddendata(document.getId());
                                b.setCreatedate(document.getData().get("category_createdate").toString());
                                values.add(b);
                            }

                        } else {
                            Log.e("", "Error getting documents.", task.getException());
                        }
                        adapter.notifyDataSetChanged();
                        if(generator.adapter!=null){
                            generator.adapter.notifyDataSetChanged();
                        }
                    }
                });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menucategory, menu);
        return true;
    }
}
