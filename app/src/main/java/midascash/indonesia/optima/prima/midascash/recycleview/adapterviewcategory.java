package midascash.indonesia.optima.prima.midascash.recycleview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import midascash.indonesia.optima.prima.midascash.R;
import midascash.indonesia.optima.prima.midascash.objects.account;

public class adapterviewcategory extends RecyclerView.Adapter<adapterviewcategory.MyViewHolder> {

    DecimalFormat formatter= new DecimalFormat("###,###,###.00");
    Context contexts;
    int times=0;
    int locationimage=0;
    int totalcircle=0;

    int[] images = new int[]{R.drawable.cashicon, R.drawable.bank, R.drawable.lendresized, R.drawable.cheque, R.drawable.creditcardresized,R.drawable.food,R.drawable.electric,R.drawable.truck,R.drawable.health,R.drawable.ball
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add
            ,R.drawable.add};

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
        Drawable resImg = contexts.getResources().getDrawable(images[times]);
        holder.a.setImageDrawable(resImg);
        holder.a.setTag(position+1);

        holder.a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosen.setImageDrawable(holder.a.getDrawable());
                chosen.setTag(holder.a.getTag());
            }
        });

        times = times+1;
    }

    @Override
    public int getItemCount() {
        return totalcircle;
    }
}

