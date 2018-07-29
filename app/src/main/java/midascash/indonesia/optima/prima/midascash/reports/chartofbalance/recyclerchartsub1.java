package midascash.indonesia.optima.prima.midascash.reports.chartofbalance;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.generator;
import midascash.indonesia.optima.prima.midascash.objects.income;
import midascash.indonesia.optima.prima.midascash.objects.incomeexpensetransfer;
import midascash.indonesia.optima.prima.midascash.objects.transfer;
import midascash.indonesia.optima.prima.midascash.objects.expense;

public class recyclerchartsub1 extends RecyclerView.Adapter<recyclerchartsub1.MyViewHolder> {

    DecimalFormat formatter= new DecimalFormat("###,###,###.00");
    Context contexts;
    Double currentbalance = 0.0d;
    Double totaldebet = 0.0d;
    Double totalkredit = 0.0d;
    Double totalall = 0.0d;

    TextView total,debet,kredit;

    private List<incomeexpensetransfer> transactionlis;
    private List<income> incomes;
    private List<expense> expenses;
    private List<transfer> transfers;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dataupdown,datadatenote,dataamount,databalance;
        public String document;

        public MyViewHolder(View view) {
            super(view);
            dataupdown = view.findViewById(R.id.chartsign);
            datadatenote = view.findViewById(R.id.chartnotes);
            dataamount = view.findViewById(R.id.chartbalance);
            databalance = view.findViewById(R.id.chartbalancing);
        }
    }

    public recyclerchartsub1(Context context, List<incomeexpensetransfer> lis,Double curbalance,TextView ttl , TextView credt, TextView debt) {
        contexts=context;
        transactionlis=lis;
        currentbalance = curbalance ;

        total = ttl;
        kredit = credt;
        debet = debt;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_layout_chartofbalancesub, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final recyclerchartsub1.MyViewHolder holder, int position) {
        if(transactionlis.get(position).getExpense_account() != null && !transactionlis.get(position).getExpense_account().equals("")){


            holder.dataupdown.setText("▼");
            holder.dataupdown.setTextColor(generator.red);

            currentbalance = currentbalance - transactionlis.get(position).getExpense_amount();

            total.setText(formatter.format(currentbalance));

            holder.dataamount.setText(formatter.format(transactionlis.get(position).getExpense_amount()));
            holder.databalance.setText(formatter.format(currentbalance));

            holder.datadatenote.setText("("+transactionlis.get(position).getExpense_date()+")"+transactionlis.get(position).getExpense_notes());

        }
        else if(transactionlis.get(position).getIncome_account() != null && !transactionlis.get(position).getIncome_account().equals("")){

            holder.dataupdown.setText("▲");
            holder.dataupdown.setTextColor(generator.green);

            currentbalance = currentbalance + transactionlis.get(position).getIncome_amount();

            total.setText(formatter.format(currentbalance));

            holder.dataamount.setText("+ " +formatter.format(transactionlis.get(position).getIncome_amount()));
            holder.databalance.setText(formatter.format(currentbalance));

            holder.datadatenote.setText("("+transactionlis.get(position).getIncome_date()+")"+transactionlis.get(position).getIncome_notes());

        }
        else if(transactionlis.get(position).getTransfer_src() != null && !transactionlis.get(position).getTransfer_src().equals("")){

            holder.dataupdown.setText("\u2B65");

            currentbalance = currentbalance - transactionlis.get(position).getTransfer_amount();

            total.setText(formatter.format(currentbalance));

            holder.dataamount.setText("- " +formatter.format(transactionlis.get(position).getTransfer_amount()));
            holder.databalance.setText(formatter.format(currentbalance));

            holder.datadatenote.setText("("+transactionlis.get(position).getTransfer_date()+")"+transactionlis.get(position).getTransfer_notes());

        }
        else if(transactionlis.get(position).getTransfer_dest() != null && !transactionlis.get(position).getTransfer_dest().equals("")){

            holder.dataupdown.setText("\u2B65");

            currentbalance = currentbalance + transactionlis.get(position).getTransfer_amount();

            total.setText(formatter.format(currentbalance));

            holder.dataamount.setText("+ " +formatter.format(transactionlis.get(position).getTransfer_amount()));
            holder.databalance.setText(formatter.format(currentbalance));

            holder.datadatenote.setText("("+transactionlis.get(position).getTransfer_date()+")"+transactionlis.get(position).getTransfer_notes());

        }
    }

    @Override
    public int getItemCount() {
        return transactionlis.size();
    }
}

