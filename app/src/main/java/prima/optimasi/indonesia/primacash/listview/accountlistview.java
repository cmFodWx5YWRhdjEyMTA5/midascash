package prima.optimasi.indonesia.primacash.listview;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import prima.optimasi.indonesia.primacash.R;

public class accountlistview extends BaseAdapter {

    DecimalFormat formatter = new DecimalFormat("###,###,###.00");

    Context contextdata;
    List<String> data ;
    List<String> currency ;
    List<Double> balance ;
    TextView textselected;
    List<String> selected;


    public accountlistview(Context context, int textViewResourceId, List<String> items , List<Double> images , TextView Textselected, List<String> curr) {
        super();
        contextdata = context;
        data = items;
        selected = new ArrayList<>();
        balance = images;
        currency=curr;
        textselected = Textselected;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(contextdata);
            v = vi.inflate(R.layout.listview_account_selection, null);
        }

        if (data != null || balance !=null) {
            TextView tt1 = (TextView) v.findViewById(R.id.account_name);
            TextView tt2 = (TextView) v.findViewById(R.id.account_balance);
            CheckBox tt3 = (CheckBox) v.findViewById(R.id.checkecaccount);
            LinearLayout line = v.findViewById(R.id.linearselectacc);

            if (tt1 != null) {
                tt1.setText(data.get(position));
            }

            if (tt2 != null) {
                tt2.setText("Balance :"+formatter.format(balance.get(position)) + " " + currency.get(position));
            }

            if (line != null) {
                line.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(tt3.isChecked()){
                            for(int i=0;i<selected.size();i++){

                                if(selected.get(i).equals(tt1.getText().toString())){
                                    selected.remove(i);
                                }

                            }
                            tt3.setChecked(false);

                        }
                        else {
                            selected.add(tt1.getText().toString());
                            tt3.setChecked(true);
                        }
                    }
                });
            }
        }

        return v;
    }

    public void setclear(){
        selected.clear();
    }

    public List<String> getSelected() {
        return selected;
    }
}