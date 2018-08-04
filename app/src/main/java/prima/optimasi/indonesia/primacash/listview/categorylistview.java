package prima.optimasi.indonesia.primacash.listview;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
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

import com.google.android.gms.common.internal.FallbackServiceBroker;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.generator;

public class categorylistview extends BaseAdapter {

    Context contextdata;
    List<String> data ;
    List<Integer> image ;

    List<String> selected;
    TextView textselected;

    public categorylistview(Context context,int textViewResourceId, List<String> items , List<Integer> images , TextView Textselected) {
        super();
        selected = new ArrayList<>();
        contextdata = context;
        data = items;
        image = images;
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
            v = vi.inflate(R.layout.listview_category_selection, null);
        }

        if (data != null || image!=null) {
            CircleImageView tt1 = (CircleImageView) v.findViewById(R.id.categoryimage);
            TextView tt2 = (TextView) v.findViewById(R.id.categoryname);
            CheckBox tt3 = (CheckBox) v.findViewById(R.id.checkedcategory);
            LinearLayout line = v.findViewById(R.id.linearselectcat);

            if (tt1 != null) {
                Drawable resImg = contextdata.getResources().getDrawable(generator.images[image.get(position)-1]);
                tt1.setImageDrawable(resImg);
            }

            if (tt2 != null) {
                tt2.setText(data.get(position));
            }

            if (line != null) {
                line.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(tt3.isChecked()){
                            for(int i=0;i<selected.size();i++){
                                if(selected.get(i).equals(tt2.getText().toString())){
                                    selected.remove(i);
                                }
                            }
                            tt3.setChecked(false);

                        }
                        else {
                            selected.add(tt2.getText().toString());
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