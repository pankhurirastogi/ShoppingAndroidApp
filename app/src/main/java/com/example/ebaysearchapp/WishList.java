package com.example.ebaysearchapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WishList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WishList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishList extends Fragment implements MyRecyclerViewAdapter.ItemListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Double totalePrice;
    SharedPreferences  sharedPreferences;
    List<Product> products ;

    private OnFragmentInteractionListener mListener;

    public WishList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WishList.
     */
    // TODO: Rename and change types and number of parameters
    public static WishList newInstance(String param1, String param2) {
        WishList fragment = new WishList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wish_list, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)

        {  Log.d("visible","visible");
            totalePrice =0.0;
            int totalItems =0;
            products= new ArrayList<>();
            Gson gson = new Gson();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            Map<String,String> map = (Map<String, String>) sharedPreferences.getAll();
            RelativeLayout errRelativeLout = getView().findViewById(R.id.wshLstErrLout);
            if(map.size()==0) {

                errRelativeLout.setVisibility(View.VISIBLE);

            }else
                errRelativeLout.setVisibility(View.GONE);
                totalItems = map.size();
                for (Map.Entry<String,String> entry : map.entrySet()) {
                    Log.d("forLoop", entry.getValue());
                    Product p = gson.fromJson(entry.getValue(),Product.class);
                    products.add(p);
                    Log.d("productid", p.itemid);
                    totalePrice+=p.price;


            }
            initRecyclerView();
            TextView total =  getView().findViewById(R.id.totalShopping);
            TextView pricTv = getView().findViewById(R.id.totalShoppingPrice);
           // String totalShopLbl = "Total Shopping                                  $";
            String totalShopLbl = "Wishlist total("+totalItems+") items";
            total.setText(String.format("%-60s", totalShopLbl));
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            if(totalePrice==0.0)
                pricTv.setText("$"+totalePrice);
            else
            pricTv.setText("$"+numberFormat.format(totalePrice));
            //pricTv.setText("$"+totalePrice);



        }
        else
            Log.d("wishlist","invisivle");


    }

    @Override
   public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        Log.d("afterView","afterView");

   }


    @Override
    public void onResume(){
        super.onResume();
        Log.d("inResume","inresume");
        //OnResume Fragment

        totalePrice =0.0;
        int totalItems =0;
        products= new ArrayList<>();
        Gson gson = new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Map<String,String> map = (Map<String, String>) sharedPreferences.getAll();
        RelativeLayout errRelativeLout = getView().findViewById(R.id.wshLstErrLout);
        if(map.size()==0) {

            errRelativeLout.setVisibility(View.VISIBLE);

        }else
            errRelativeLout.setVisibility(View.GONE);
        totalItems = map.size();
        for (Map.Entry<String,String> entry : map.entrySet()) {
            Log.d("forLoop", entry.getValue());
            Product p = gson.fromJson(entry.getValue(),Product.class);
            products.add(p);
            Log.d("resumeproductid", p.itemid);
            totalePrice+=p.price;


        }
        initRecyclerView();
        TextView total =  getView().findViewById(R.id.totalShopping);
        TextView pricTv = getView().findViewById(R.id.totalShoppingPrice);

        String totalShopLbl = "Wishlist total("+totalItems+")items";
        total.setText(String.format("%-60s", totalShopLbl));
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        if(totalePrice==0.0)
            pricTv.setText("$ "+totalePrice);
        else
        pricTv.setText("$"+numberFormat.format(totalePrice));
        //pricTv.setText("$ "+totalePrice);


        //Toast.makeText(getActivity(),"size is   "+ products.size(),Toast.LENGTH_LONG).show();
    }


    public void checkEmptyWishlist(List<Product> ls){
        if(ls!=null){
            RelativeLayout errRelativeLout = getView().findViewById(R.id.wshLstErrLout);
            if(ls.size()==0) {

                errRelativeLout.setVisibility(View.VISIBLE);

            }else
                errRelativeLout.setVisibility(View.GONE);
        }

    }

    public void ChangePrice(List<Product> ls){
        Double price = 0.0;
        int totalItems =0;
        if(ls!=null) {
            for (int j = 0; j < ls.size(); j++) {
                price += ls.get(j).price;

            }
            totalItems = ls.size();
        }

        TextView tv = getView().findViewById(R.id.totalShopping);
        TextView pricTv = getView().findViewById(R.id.totalShoppingPrice);
        String totalShopLbl = "Wishlist total("+totalItems+") items";
        tv.setText(String.format("%-60s", totalShopLbl));
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        if(price==0.0)
            pricTv.setText("$ "+price);
        else
            pricTv.setText("$"+numberFormat.format(price));
        //pricTv.setText("$ "+price);



    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(Product item) {

        //Toast.makeText(getActivity(),item.title, Toast.LENGTH_SHORT).show();
        //api call for item details
        getItemDetails(item);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getItemDetails(final Product item){
        final Intent itemDetailIntent = new Intent(getActivity(), ItemDetails.class);
        Gson gson = new Gson();
        // for sending entire item so that this can be directly put to wishlist
        final String itemjson = gson.toJson(item);

        //String url = "http://10.0.2.2:3000/getItemDetail/"+item.itemid;
        String url = "http://connectebaypankhuri.us-east-2.elasticbeanstalk.com/getItemDetail/"+item.itemid;
        Log.d("urlcheck",url);

        //for testing invalid item id....

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        itemDetailIntent.putExtra("itemjson",itemjson);
                        itemDetailIntent.putExtra("itemResponse", response.toString());
                        itemDetailIntent.putExtra("sellerInfo", item.sellerInfo);
                        itemDetailIntent.putExtra("shippingInfo", item.shippingInfo);
                        itemDetailIntent.putExtra("itemid",item.itemid);
                        itemDetailIntent.putExtra("title",item.title);
                        //    startActivity(itemDetailIntent);

                        Log.d("jsonResp", response.toString());

                        // String itemId = "282389702634";
                        //for aws
                        String urll = "http://connectebaypankhuri.us-east-2.elasticbeanstalk.com/getSimilarItems/"+item.itemid;
                        //for local
                       // String urll = "http://10.0.2.2:3000/getSimilarItems/"+item.itemid;
                        Log.d("urlcheck",urll);
                        RequestQueue queue = Volley.newRequestQueue(getActivity());
                        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                                (Request.Method.GET, urll, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {

                                        //simItems = response.toString();

                                        Log.d("simItmRespssss", response.toString());
                                        itemDetailIntent.putExtra("simItmresp", response.toString());
                                        startActivity(itemDetailIntent);

                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO: Handle error

                                    }
                                });

                        queue.add(jsonObjectRequest2);


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(getActivity(), error.toString(),Toast.LENGTH_LONG).show();

                    }
                });

        queue.add(jsonObjectRequest);

    }


    public  void initRecyclerView(){
        RecyclerView recyclerView  = getView().findViewById(R.id.myRecylerViewWish);
        MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter(products,getActivity(),this,this);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);


    }
}
