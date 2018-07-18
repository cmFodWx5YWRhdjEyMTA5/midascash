package midascash.indonesia.optima.prima.midascash.reports.chartofbalance;

import android.content.Context;
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
import midascash.indonesia.optima.prima.midascash.objects.income;
import midascash.indonesia.optima.prima.midascash.objects.incomeexpensetransfer;
import midascash.indonesia.optima.prima.midascash.objects.transfer;
import midascash.indonesia.optima.prima.midascash.objects.expense;

public class recyclerchartsub1 extends RecyclerView.Adapter<recyclerchartsub1.MyViewHolder> {

    DecimalFormat formatter= new DecimalFormat("###,###,###.00");
    Context contexts;
    int times=0;
    int locationimage=0;
    int totalcircle=0;

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

    public recyclerchartsub1(Context context, List<incomeexpensetransfer> lis) {
        contexts=context;
        transactionlis=lis;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_layout_chartofbalancesub, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final recyclerchartsub1.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return transactionlis.size();
    }
}

