package com.example.ebaysearchapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyRecylerSimAdapter extends  RecyclerView.Adapter<MyRecylerSimAdapter.SimViewHolder>{
    private List<SimlarProduct> simlarProducts;
    Context mContext;
    protected MyRecylerSimAdapter.ItemListener mListener;

    public MyRecylerSimAdapter(List<SimlarProduct> sm, Context mContext,ItemListener itemListener) {
        this.simlarProducts = sm;
        this.mContext = mContext;
        this.mListener = itemListener;
    }

    @NonNull
    @Override
    public SimViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_simitm, viewGroup,false);
        SimViewHolder holder = new SimViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SimViewHolder simViewHolder, int i) {


        Picasso.get()
                .load(simlarProducts.get(i).imageURL).resize(300, 350)
                .into(simViewHolder.imageView);

        simViewHolder.currProduct=simlarProducts.get(i);

        simViewHolder.tv1.setText(simlarProducts.get(i).title.toUpperCase());
        // handle free shipping here
        if(simlarProducts.get(i).shipping==0)
            simViewHolder.tv2.setText("Free shipping");
        else
        simViewHolder.tv2.setText("$"+simlarProducts.get(i).shipping);

        if(simlarProducts.get(i).daysLeft >1)
            simViewHolder.tv3.setText(""+ simlarProducts.get(i).daysLeft + " days left");
         else
        simViewHolder.tv3.setText(""+simlarProducts.get(i).daysLeft + " dayleft");

         simViewHolder.tv4.setText("$" + simlarProducts.get(i).price);

    }

    @Override
    public int getItemCount() {
        return simlarProducts.size();
    }

    public class SimViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        ImageView imageView;
        TextView tv1 ;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        TextView t4;
        SimlarProduct currProduct;
        LinearLayout ll;

        public SimViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.resSimImage);
            tv1 = itemView.findViewById(R.id.text1);
            tv2 = itemView.findViewById(R.id.text2);
            tv3 = itemView.findViewById(R.id.text3);
            tv4 = itemView.findViewById(R.id.text4);
            ll = itemView.findViewById(R.id.simLinearLayout);
        }


        @Override
        public void onClick(View v) {
            Log.d("inclick", "similarClick");
            if (mListener != null) {
                mListener.onItemClick(currProduct);
            }
        }
    }

    public interface ItemListener {
        void onItemClick(SimlarProduct item);
    }


}
