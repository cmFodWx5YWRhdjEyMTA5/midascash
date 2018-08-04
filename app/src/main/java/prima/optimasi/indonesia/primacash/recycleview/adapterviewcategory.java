package prima.optimasi.indonesia.primacash.recycleview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import prima.optimasi.indonesia.primacash.R;
import prima.optimasi.indonesia.primacash.generator;
import prima.optimasi.indonesia.primacash.objects.account;

public class adapterviewcategory extends RecyclerView.Adapter<adapterviewcategory.MyViewHolder> {

    DecimalFormat formatter= new DecimalFormat("###,###,###.00");
    Context contexts;
    int times=0;
    int locationimage=0;
    int totalcircle=0;

    CircleImageView chosen;

    private List<account> imagesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView a,b,c,d,e,f,g,h,i,j;

        public MyViewHolder(View view) {
            super(view);
            a =  (CircleImageView) view.findViewById(R.id.img1);

        }
    }

    public adapterviewcategory(Context context, CircleImageView chosenimage,int totalcirclerow) {
        chosen=chosenimage;
        contexts=context;
        this.totalcircle=totalcirclerow;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final adapterviewcategory.MyViewHolder holder, int position) {
        Drawable resImg = contexts.getResources().getDrawable(generator.images[position]);
        holder.a.setImageDrawable(resImg);
        holder.a.setTag(position+1);

        holder.a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosen.setImageDrawable(holder.a.getDrawable());
                chosen.setTag(holder.a.getTag());
            }
        });

    }

    @Override
    public int getItemCount() {
        return totalcircle;
    }
}

