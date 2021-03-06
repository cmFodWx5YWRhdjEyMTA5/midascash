package prima.optimasi.indonesia.primacash.recycleview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.generator;
import prima.optimasi.indonesia.primacash.objects.account;
import prima.optimasi.indonesia.primacash.transactionactivity.accountlist;

public class adapterviewaccountsmenu extends RecyclerView.Adapter<adapterviewaccountsmenu.MyViewHolder> {

    DecimalFormat formatter= new DecimalFormat("###,###,###.00");
    Context contexts;
    int times=0;
    int locationimage=0;
    int totalcircle=0;

    private List<account> accountlis;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView accname,accdetail,accbalance;
        public LinearLayout linear;

        public MyViewHolder(View view) {
            super(view);
            accname = view.findViewById(R.id.accaccitemname);
            accdetail = view.findViewById(R.id.accaccitemdatedetail);
            accbalance = view.findViewById(R.id.accaccitembalance);
            linear = view.findViewById(R.id.acclinearitem);
        }
    }

    public adapterviewaccountsmenu(Context context, List<account> lis) {
        contexts=context;
        accountlis=lis;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_recycler_accounts, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final adapterviewaccountsmenu.MyViewHolder holder, int position) { 
        holder.accname.setText(accountlis.get(position).getAccount_name());

        Date date=null;


        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        if(accountlis.get(position).getCreateorlast()==0){
            date =(Date) accountlis.get(position).getAccount_createdate();
            holder.accdetail.setText("Created : "+df.format(date));
        }
        else {
            Date data = (Date) accountlis.get(position).getLastused();
            holder.accdetail.setText("Last Used : "+df.format(data));
        }
        Log.e("data acc", "onBindViewHolder:  "+ accountlis.get(position).getAccount_balance_current() );
        holder.accbalance.setText(formatter.format(Double.parseDouble(accountlis.get(position).getAccount_balance_current()))+" "+accountlis.get(position).getAccount_currency());

        if(Double.parseDouble(accountlis.get(position).getAccount_balance_current())>=0){
            holder.accbalance.setTextColor(generator.green);
        }
        else {
            holder.accbalance.setTextColor(generator.red);
        }

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(contexts,accountlist.class);
                contexts.startActivity(a);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountlis.size();
    }
}

