package com.example.ebaysearchapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link similar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link similar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class similar extends Fragment implements MyRecylerSimAdapter.ItemListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<String> t1 = new ArrayList<>();
    ArrayList<String> t2= new ArrayList<>() ;
    private Comparator<SimlarProduct> currentComparator= null;
    private boolean asc= true;
    List<SimlarProduct> simlarProducts = new ArrayList<SimlarProduct>();

    String simItems ="previous";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public similar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment similar.
     */
    // TODO: Rename and change types and number of parameters
    public static similar newInstance(String param1, String param2) {
        similar fragment = new similar();
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
            View v = inflater.inflate(R.layout.fragment_similar, container, false);
        Bundle bundle = this.getArguments();
        String simItemResponse = bundle.getString("simitems");
        Log.d("simitems",simItemResponse);


        //----------------------------- JSON Parsing here ------------------------//


        try {
            JSONObject simItmResp = new JSONObject(simItemResponse);
            if(simItmResp.getJSONObject("getSimilarItemsResponse").getString("ack").equals("Success")){
                JSONObject itemRecommendation = simItmResp.getJSONObject("getSimilarItemsResponse").getJSONObject("itemRecommendations");
                JSONArray items = itemRecommendation.getJSONArray("item");

                if(items.length()==0){

                    Log.d("simItmErr","nosimitem becoz array length 0");
                    TextView simItmErr = v.findViewById(R.id.simItmErr);
                    simItmErr.setVisibility(View.VISIBLE);

                }else {
                    for (int k = 0; k < items.length(); k++) {

                        JSONObject simItm = items.getJSONObject(k);
                        String title = simItm.getString("title");
                        String imageURL = simItm.getString("imageURL");
                        String viewItemURL = simItm.getString("viewItemURL");
                        int timeLeft = extractDaysLeft(simItm.getString("timeLeft"));
                        Double price = Double.parseDouble(simItm.getJSONObject("buyItNowPrice").getString("__value__"));
                        Double shippingCost = Double.parseDouble(simItm.getJSONObject("shippingCost").getString("__value__"));


                        Log.d("title", title);
                        Log.d("time", "" + timeLeft);
                        Log.d("price", "" + price);
                        Log.d("shippingCost", "" + shippingCost);

                        SimlarProduct temp = new SimlarProduct(imageURL, title, shippingCost, timeLeft, price, viewItemURL);
                        simlarProducts.add(temp);

                    }
                }




            }else{

                //simItmErr
                Log.d("simItmErr","nosimitem");
                TextView simItmErr = v.findViewById(R.id.simItmErr);
                simItmErr.setVisibility(View.VISIBLE);

                // error handling

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        setRecylValues();

        RecyclerView recyclerView  = v.findViewById(R.id.myRecylerViewSim);
        final MyRecylerSimAdapter myRecyclerViewAdapter = new MyRecylerSimAdapter(simlarProducts,getActivity(),this);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        //GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        Spinner sortBy = v.findViewById(R.id.sortBy);
        final Spinner orderBy = v.findViewById(R.id.orderBy);

        sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                switch (position){
                    case 0:
                        orderBy.setEnabled(false);
                        myRecyclerViewAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        orderBy.setEnabled(true);
                        currentComparator = new Comparator<SimlarProduct>() {
                            @Override
                            public int compare(SimlarProduct o1, SimlarProduct o2) {
                                return o1.title.compareTo(o2.title);
                            }
                        };
                        if(asc)
                            Collections.sort(simlarProducts,currentComparator);
                        else
                            Collections.sort(simlarProducts, Collections.<SimlarProduct>reverseOrder(currentComparator));
                        myRecyclerViewAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        Log.d("1","1");
                        orderBy.setEnabled(true);
                        currentComparator = new Comparator<SimlarProduct>() {
                            @Override
                            public int compare(SimlarProduct o1, SimlarProduct o2) {
                                return o1.price.compareTo(o2.price);
                            }
                        };
                        if(asc)
                            Collections.sort(simlarProducts,currentComparator);
                        else
                            Collections.sort(simlarProducts, Collections.<SimlarProduct>reverseOrder(currentComparator));
                        myRecyclerViewAdapter.notifyDataSetChanged();

                        break;
                    case 3:
                        Log.d("1","1");
                        orderBy.setEnabled(true);
                        currentComparator = new Comparator<SimlarProduct>() {
                            @Override
                            public int compare(SimlarProduct o1, SimlarProduct o2) {
                                return new Integer(o1.daysLeft).compareTo(new Integer(o2.daysLeft));
                            }
                        };
                        if(asc)
                            Collections.sort(simlarProducts,currentComparator);
                        else
                            Collections.sort(simlarProducts, Collections.<SimlarProduct>reverseOrder(currentComparator));
                        myRecyclerViewAdapter.notifyDataSetChanged();

                        break;


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        orderBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (currentComparator != null) {
                    switch (position) {
                        case 0:
                            asc = true;
                            Collections.sort(simlarProducts, currentComparator);
                            myRecyclerViewAdapter.notifyDataSetChanged();
                            break;
                        case 1:
                            asc=false;
                            Collections.sort(simlarProducts, Collections.<SimlarProduct>reverseOrder(currentComparator));
                            myRecyclerViewAdapter.notifyDataSetChanged();
                            break;


                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private int extractDaysLeft(String daysLeftRaw){

        int resIndex = daysLeftRaw.indexOf('D');
        return Integer.parseInt(daysLeftRaw.substring(1, resIndex));

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private  void setRecylValues(){

        t1.add("hello");
        t1.add("hello2");
        t1.add("hello3");
        t1.add("hello4");
        t1.add("hello5");



        t2.add("hello");
        t2.add("hello2");
        t2.add("hello3");
        t2.add("hello4");
        t2.add("hello5");

       // initRecycleView();

    }


    @Override
    public void onItemClick(SimlarProduct item) {

        //Toast.makeText(getActivity(),item.title,Toast.LENGTH_LONG).show();
        Intent viewItemIntent = new Intent(Intent.ACTION_VIEW);
        viewItemIntent.setData(Uri.parse(item.viewItmURL));
        startActivity(viewItemIntent);


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
}
