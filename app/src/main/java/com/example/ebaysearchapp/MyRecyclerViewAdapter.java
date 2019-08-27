package com.example.ebaysearchapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
      SharedPreferences sharedPreferences;


      private List<Product>  displayProducts= new ArrayList<Product>();
      Context mcontext ;
      protected ItemListener mListener;
      WishList w;
    public MyRecyclerViewAdapter(List<Product> displayProducts, Context mcontext, ItemListener itemListener,WishList w) {

        this.displayProducts = displayProducts;
        this.mcontext = mcontext;
        this.mListener = itemListener;
        this.w = w;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        Picasso.get()
                .load(displayProducts.get(i).imageURL).resize(400, 500)
                .into(myViewHolder.imageView);
        final  int pos = i;

        myViewHolder.textView.setText(displayProducts.get(i).title.toUpperCase());
        myViewHolder.zipTxtView.setText("Zip : "+displayProducts.get(i).zip);
        myViewHolder.shipTxtView.setText(displayProducts.get(i).shipping);
        myViewHolder.priceTxtView.setText("$"+displayProducts.get(i).price);
        myViewHolder.currProduct = displayProducts.get(i);
        myViewHolder.condition.setText(displayProducts.get(i).condition);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mcontext);
         if(sharedPreferences.contains(displayProducts.get(pos).itemid)) {
             myViewHolder.wishlist.setImageResource(R.drawable.cartremove);
             Log.d("itemid", displayProducts.get(pos).itemid);
             Log.d("title", displayProducts.get(pos).title);
             Log.d("pos",""+pos);
             myViewHolder.wishlist.setColorFilter(Color.parseColor("#f57b5b"));
         }
         else {
             myViewHolder.wishlist.setImageResource(R.drawable.cartplus);
             myViewHolder.wishlist.setColorFilter(Color.parseColor("#c5c5c5"));
         }

        myViewHolder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mcontext);
                Gson gson = new Gson();
                String itemID = displayProducts.get(pos).itemid;
                String json = gson.toJson(displayProducts.get(pos));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(sharedPreferences.contains(itemID)){
                    String titleTxt = displayProducts.get(pos).title;
                    editor.remove(itemID);
                    editor.commit();
                    if(w!=null){
                        Log.d("before ", ""+displayProducts.size());
                        displayProducts.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos,displayProducts.size());
                        Log.d("after", ""+displayProducts.size());
                        w.ChangePrice(displayProducts);
                        w.checkEmptyWishlist(displayProducts);

                    }else {
                        myViewHolder.wishlist.setImageResource(R.drawable.cartplus);
                        myViewHolder.wishlist.setColorFilter(Color.parseColor("#c5c5c5"));


                    }

                    Toast.makeText(mcontext, titleTxt+"removed from wishlist",Toast.LENGTH_SHORT).show();

                }else {
                    myViewHolder.wishlist.setImageResource(R.drawable.cartremove);
                    myViewHolder.wishlist.setColorFilter(Color.parseColor("#f57b5b"));
                    editor.putString(itemID, json);
                    editor.commit();
                    Toast.makeText(mcontext,displayProducts.get(pos).title+"added to wishlist",Toast.LENGTH_SHORT).show();

                }


            }
        });
//        myViewHolder.remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//               sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mcontext);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                // use editor.clear for clearing the complete shared preferences
//               // editor.clear();
//                String  itemID= displayProducts.get(pos).itemid;
//                if(sharedPreferences.contains(itemID))
//                    editor.remove(itemID);
//                editor.commit();
//               //String ss = sharedPreferences.getString(displayProducts.get(pos).itemid,"default");
//                Map<String, ?> ss = sharedPreferences.getAll();
//                if(ss==null)
//                    Toast.makeText(mcontext, "clean", Toast.LENGTH_LONG).show();
//                else
//                    Toast.makeText(mcontext,"notclean"+ ss.size(),Toast.LENGTH_LONG).show();
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return displayProducts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{


        ImageView imageView;
        TextView textView;
        TextView zipTxtView;
        TextView shipTxtView;
        TextView priceTxtView;
        LinearLayout linearLayout;
        Product currProduct;
        ImageButton wishlist ;
        TextView condition;
        //Button remove;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

         imageView = itemView.findViewById(R.id.resImage);
          textView =  itemView.findViewById(R.id.titletxt);
          zipTxtView = itemView.findViewById(R.id.ziptxt);
          shipTxtView = itemView.findViewById(R.id.shippingtxt);
          priceTxtView = itemView.findViewById(R.id.pricetxt);
          wishlist = itemView.findViewById(R.id.wishList);
          condition=itemView.findViewById(R.id.conditionTxt);
          //remove = itemView.findViewById(R.id.wishList2);
          linearLayout = itemView.findViewById(R.id.parent_layout);
        }

        @Override
        public void onClick(View v) {
            Log.d("inclick","click");
            if (mListener != null) {
                mListener.onItemClick(currProduct);
            }
        }
    }

    public interface ItemListener {
        void onItemClick(Product item);
    }
}
