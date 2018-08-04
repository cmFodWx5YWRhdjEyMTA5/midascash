package prima.optimasi.indonesia.primacash.recycleview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.generator;
import prima.optimasi.indonesia.primacash.objects.income;
import prima.optimasi.indonesia.primacash.objects.transfer;

public class adapterviewtransferlist extends RecyclerView.Adapter<adapterviewtransferlist.MyViewHolder> {

    DecimalFormat formatter = new DecimalFormat("###,###,###.00");
    Context contexts;
    int times = 0;
    FirebaseFirestore db;
    int locationimage = 0;
    int totalcircle = 0;

    private List<transfer> transferlis;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView trfsrc, trfdest, trfdate, trfvalue, trfrate, trfnotes;
        public LinearLayout linear;

        public MyViewHolder(View view) {
            super(view);
            trfsrc = view.findViewById(R.id.trfsrc);
            trfdest = view.findViewById(R.id.trfdest);
            trfdate = view.findViewById(R.id.trfdate);
            trfdate = view.findViewById(R.id.trfdate);
            trfnotes = view.findViewById(R.id.trfnotes);
            trfvalue = view.findViewById(R.id.trfvalue);
            trfrate = view.findViewById(R.id.trfrate);
            linear = view.findViewById(R.id.trflinear);
        }
    }

    public adapterviewtransferlist(Context context, List<transfer> lis) {
        contexts = context;
        transferlis = lis;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_recycler_transfer, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final adapterviewtransferlist.MyViewHolder holder, int position) {
        holder.trfrate.setText(formatter.format(transferlis.get(position).getTransfer_rate()));
        holder.trfvalue.setText(formatter.format(transferlis.get(position).getTransfer_amount()));
        holder.trfdate.setText(transferlis.get(position).getTransfer_date());
        holder.trfnotes.setText(transferlis.get(position).getTransfer_notes());
        holder.trfsrc.setText(transferlis.get(position).getTransfer_src());
        holder.trfdest.setText(transferlis.get(position).getTransfer_dest());

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(contexts, holder.linear);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_default);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edititem:
                                //handle menu1 click
                                return true;
                            case R.id.deleteitem:

                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(contexts, R.style.AppCompatAlertDialogStyle);
                                builder.setTitle("Confirm");
                                builder.setMessage("Are you sure to delete Transfer on " + transferlis.get(position).getTransfer_date() + " with " + formatter.format(transferlis.get(position).getTransfer_amount()) + " amount and uses " + transferlis.get(position).getTransfer_src() + " and " + transferlis.get(position).getTransfer_dest() + " with Currency rate " + formatter.format(transferlis.get(position).getTransfer_rate()) + " ?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        db.collection("income").document(transferlis.get(position).getTransferdoc())
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {


                                                        if (generator.adapter != null) {
                                                            generator.adapter.notifyDataSetChanged();
                                                        }
                                                        Toast.makeText(contexts, "Deleted selected Transfer Data", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(contexts, "Fail Delete selected Transfer data", Toast.LENGTH_SHORT).show();
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
        return transferlis.size();
    }
}