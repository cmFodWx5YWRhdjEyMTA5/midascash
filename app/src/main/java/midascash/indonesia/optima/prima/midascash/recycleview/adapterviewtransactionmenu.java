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
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.generator;
import midascash.indonesia.optima.prima.midascash.objects.account;
import midascash.indonesia.optima.prima.midascash.objects.incomeexpense;

public class adapterviewtransactionmenu extends RecyclerView.Adapter<adapterviewtransactionmenu.MyViewHolder> {

    DecimalFormat formatter= new DecimalFormat("###,###,###.00");
    Context contexts;
    int times=0;
    int locationimage=0;
    int totalcircle=0;

    private List<incomeexpense> transactionlis;

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

    public adapterviewtransactionmenu(Context context, List<incomeexpense> lis) {
        contexts=context;
        transactionlis=lis;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_recycler_transaction, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final adapterviewtransactionmenu.MyViewHolder holder, int position) {
        if(transactionlis.get(position).getExpense_account()!=null){
            Drawable resImg = contexts.getResources().getDrawable(generator.images[transactionlis.get(position).getExpense_image()-1]);
            holder.dataimage.setImageDrawable(resImg);
            holder.dataimage.setTag(transactionlis.get(position).getExpense_image());
            holder.dataamount.setText(formatter.format(transactionlis.get(position).getExpense_amount()));
            holder.dataacc.setText(transactionlis.get(position).getExpense_account());
            holder.datafrom.setText(transactionlis.get(position).getExpense_to());
            holder.dataacc.setText(transactionlis.get(position).getExpense_account());
            holder.datanote.setText(transactionlis.get(position).getExpense_notes());
            holder.datadate.setText(transactionlis.get(position).getExpense_date());

            holder.color.setBackgroundColor(generator.red);
        }
        else{
            Drawable resImg = contexts.getResources().getDrawable(generator.images[transactionlis.get(position).getIncome_image()-1]);
            holder.dataimage.setImageDrawable(resImg);
            holder.dataimage.setTag(transactionlis.get(position).getIncome_image());
            holder.dataamount.setText(formatter.format(transactionlis.get(position).getIncome_amount()));
            holder.dataacc.setText(transactionlis.get(position).getIncome_account());
            holder.datafrom.setText(transactionlis.get(position).getIncome_from());
            holder.dataacc.setText(transactionlis.get(position).getIncome_account());
            holder.datanote.setText(transactionlis.get(position).getIncome_notes());
            holder.datadate.setText(transactionlis.get(position).getIncome_date());

            holder.color.setBackgroundColor(generator.green);

        }

    }

    @Override
    public int getItemCount() {
        return transactionlis.size();
    }
}

