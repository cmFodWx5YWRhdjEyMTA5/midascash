package midascash.indonesia.optima.prima.midascash.recycleview;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.generator;
import midascash.indonesia.optima.prima.midascash.objects.account;
import midascash.indonesia.optima.prima.midascash.objects.incomeexpensetransfer;

public class adapterlisttransactionaccount extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    DecimalFormat formatter= new DecimalFormat("###,###,###.00");
    Context contexts;
    int times=0;
    int locationimage=0;
    int totalcircle=0;

    private List<incomeexpensetransfer> alllis;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView datacat,dataacc,datafrom,datadate,datanote,dataamount;
        CircleImageView dataimage;
        View color;
        public LinearLayout linear;

        public MyViewHolder(View view) {
            super(view);
            dataimage = view.findViewById(R.id.dataimagelistdata);
            dataacc = view.findViewById(R.id.dataaccdata);
            datacat= view.findViewById(R.id.datacatdata);
            datadate = view.findViewById(R.id.datadatedata);
            datafrom = view.findViewById(R.id.datafromdata);
            datanote = view.findViewById(R.id.datanotedata);
            dataamount = view.findViewById(R.id.dataamountdata);
            color = view.findViewById(R.id.colortype);
        }
    }

    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        public TextView datacat,dataacc,datafrom,datadate,datanote,dataamount;
        CircleImageView dataimage;
        View color;
        public LinearLayout linear;

        public MyViewHolder1(View view) {
            super(view);
            dataimage = view.findViewById(R.id.dataimagelistdata);
            dataacc = view.findViewById(R.id.dataaccdata);
            datacat= view.findViewById(R.id.datacatdata);
            datadate = view.findViewById(R.id.datadatedata);
            datafrom = view.findViewById(R.id.datafromdata);
            datanote = view.findViewById(R.id.datanotedata);
            dataamount = view.findViewById(R.id.dataamountdata);
            color = view.findViewById(R.id.colortype);
        }
    }

    public class MyViewHolder2 extends RecyclerView.ViewHolder {
        public TextView trfsrc,trfdest,trfdate,trfvalue,trfrate,trfnotes;
        public LinearLayout linear;
        String trfisdone="";

        public MyViewHolder2(View view) {
            super(view);
            trfsrc = view.findViewById(R.id.trfsrc);
            trfdest = view.findViewById(R.id.trfdest);
            trfdate= view.findViewById(R.id.trfdate);
            trfnotes = view.findViewById(R.id.trfnotes);
            trfvalue = view.findViewById(R.id.trfvalue);
            trfrate = view.findViewById(R.id.trfrate);
            linear = view.findViewById(R.id.trflinear);
        }
    }

    public adapterlisttransactionaccount(Context context, List<incomeexpensetransfer> lis) {
        contexts=context;
        alllis=lis;
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if(alllis.get(position).getIncome_type() != null && alllis.get(position).getIncome_type().equals("D")){
            return 1;
        }else if(alllis.get(position).getExpense_type() != null && alllis.get(position).getExpense_type().equals("K")){
            return 2;
        }else if(alllis.get(position).getTransfer_src() != null && !alllis.get(position).getTransfer_src().equals("")){
            return 3;
        }else {
            return 0;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==1){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_layout_recycler_transaction, parent, false);

            return new MyViewHolder(itemView);
        }
        else if(viewType==2){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_layout_recycler_transaction, parent, false);

            return new MyViewHolder1(itemView);
        }
        else if(viewType==3){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_layout_recycler_transfer, parent, false);

            return new MyViewHolder2(itemView);
        }
        else{
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_layout_recycler_transaction, parent, false);

            return new MyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if(holder.getItemViewType()==1){
            MyViewHolder viewHolder0 = (MyViewHolder) holder;

            Drawable resImg = contexts.getResources().getDrawable(generator.images[alllis.get(position).getIncome_image()-1]);
            viewHolder0.dataimage.setImageDrawable(resImg);
            viewHolder0.dataimage.setTag(alllis.get(position).getIncome_image());
            viewHolder0.dataamount.setText(formatter.format(alllis.get(position).getIncome_amount()));
            viewHolder0.dataacc.setText(alllis.get(position).getIncome_account());
            viewHolder0.datacat.setText(alllis.get(position).getIncome_category());
            viewHolder0.datafrom.setText(alllis.get(position).getIncome_from());
            viewHolder0.dataacc.setText(alllis.get(position).getIncome_account());
            viewHolder0.datanote.setText(alllis.get(position).getIncome_notes());
            viewHolder0.datadate.setText(alllis.get(position).getIncome_date());

            viewHolder0.color.setBackgroundColor(generator.green);

        }
        else if(holder.getItemViewType()==2){
            MyViewHolder1 viewHolder1 = (MyViewHolder1) holder;

            Drawable resImg = contexts.getResources().getDrawable(generator.images[alllis.get(position).getExpense_image()-1]);
            viewHolder1.dataimage.setImageDrawable(resImg);
            viewHolder1.dataimage.setTag(alllis.get(position).getExpense_image());
            viewHolder1.dataamount.setText(formatter.format(alllis.get(position).getExpense_amount()));
            viewHolder1.dataacc.setText(alllis.get(position).getExpense_account());
            viewHolder1.datacat.setText(alllis.get(position).getExpense_category());
            viewHolder1.datafrom.setText(alllis.get(position).getExpense_to());
            viewHolder1.dataacc.setText(alllis.get(position).getExpense_account());
            viewHolder1.datanote.setText(alllis.get(position).getExpense_notes());
            viewHolder1.datadate.setText(alllis.get(position).getExpense_date());

            viewHolder1.color.setBackgroundColor(generator.red);

        }
        else if(holder.getItemViewType()==3){
            MyViewHolder2 viewHolder2 = (MyViewHolder2) holder;

            viewHolder2.trfrate.setText(formatter.format(alllis.get(position).getTransfer_rate()));
            viewHolder2.trfvalue.setText(formatter.format(alllis.get(position).getTransfer_amount()));
            viewHolder2.trfdate.setText(alllis.get(position).getTransfer_date());
            viewHolder2.trfnotes.setText(alllis.get(position).getTransfer_notes());
            viewHolder2.trfsrc.setText(alllis.get(position).getTransfer_src());
            viewHolder2.trfdest.setText(alllis.get(position).getTransfer_dest());
        }
        else{
            MyViewHolder viewHolder3 = (MyViewHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return alllis.size();
    }
}


