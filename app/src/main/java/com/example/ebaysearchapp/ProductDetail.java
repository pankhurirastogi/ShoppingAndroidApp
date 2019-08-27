package com.example.ebaysearchapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductDetail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    boolean itmErr = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProductDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetail newInstance(String param1, String param2) {
        ProductDetail fragment = new ProductDetail();
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
        View v = inflater.inflate(R.layout.fragment_product_detail, container, false);


        final HorizontalScrollView hsv = v.findViewById(R.id.imageScrollView);
        final LinearLayout headsect = v.findViewById(R.id.headSection);
        final LinearLayout cover = v.findViewById(R.id.prodLinLayout);
        final ProgressBar pr = v.findViewById(R.id.dataLoadProduct);
        final TextView searchProdtv = v.findViewById(R.id.srchResltsText);
//
//        runOnUiThread(new Runnable() {
//
//
//
//            @Override
//            public void run() {
//
//                // Stuff that updates the UI
//                pr.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//
//            }
//        });

//        new Thread() {
//            public void run() {
//
//                try {
//                    Thread.sleep(2000);
//                    getActivity().runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            pr.setVisibility(View.GONE);
//                            searchProdtv.setVisibility(View.GONE);
//                            hsv.setVisibility(View.VISIBLE);
//                            headsect.setVisibility(View.VISIBLE);
//                            cover.setVisibility(View.VISIBLE);
//
//                        }
//                    });
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();

        Bundle bundle = this.getArguments();
        JSONObject obj;
        JSONArray shipJSONArray ;
        TextView shipPricetv = null;
        String itemResponse = bundle.getString("itemresponse");
        String shippingInfo = bundle.getString("shippingInfo");
        Log.d("shippingInfo",shippingInfo);
        try {
            obj = new JSONObject(itemResponse);
            shipJSONArray = new JSONArray(shippingInfo);
            Log.d("vals",obj.getString("Ack"));
            if(obj.getString("Ack").equals("Success")){
                Log.d("itmsuccc","success");
                JSONObject itmObj = obj.getJSONObject("Item");
                //ItemID

                        if(shipJSONArray!=null){

                          if(shipJSONArray.getJSONObject(0).has("shippingServiceCost")) {
                              JSONArray temparr = new JSONArray(shipJSONArray.getJSONObject(0).getString("shippingServiceCost"));
                              if (temparr != null) {
                                  String shipprice = temparr.getJSONObject(0).getString("__value__");
                                  Log.d("shipprice", shipprice);
                                  shipPricetv = new TextView(v.getContext());
                                  if (shipprice.equals("0.0")) {

                                      shipPricetv.setText(" with Free shipping");

                                  } else {

                                      shipPricetv.setText("with $" + shipprice + "shipping");

                                  }
                              }
                          }

                        }


               //--------------outer most Linera layout is l-------------------------//

                LinearLayout l = v.findViewById(R.id.prodLinLayout);



                // pticyure URL Code goes here...............

                JSONArray pictureArray = itmObj.getJSONArray("PictureURL");
                ArrayList<String> prodImg = new ArrayList<>();
                for(int m=0;m<pictureArray.length();m++){

                    prodImg.add(pictureArray.getString(m));
                    Log.d("imgurl", pictureArray.getString(m));
                }

               LinearLayout mGallery = (LinearLayout) v.findViewById(R.id.id_gallery);

                for (int i = 0; i < prodImg.size(); i++)
                {

                    View view = inflater.inflate(R.layout.activity_gallery_item,
                            mGallery, false);
                    ImageView img = (ImageView) view
                            .findViewById(R.id.prodImg);
                    Picasso.get()
                            .load(prodImg.get(i)).resize(400, 500)
                            .into(img);
                    mGallery.addView(view);
                }



                LinearLayout header = v.findViewById(R.id.headSection);
                TextView tvv = new TextView(v.getContext());
                tvv.setText(itmObj.getString("Title"));
                tvv.setTextColor(Color.BLACK);
                tvv.setTypeface(null, Typeface.BOLD);
                tvv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                tvv.setMaxLines(2);
                TextView tvprice = new TextView(v.getContext());
                boolean currenPrice=false;
                if(itmObj.has("CurrentPrice")) {
                    tvprice.setText("$" + itmObj.getJSONObject("CurrentPrice").getString("Value"));
                    tvprice.setTypeface(null, Typeface.BOLD);
                    tvprice.setTextColor(0xFF6200EE);
                    currenPrice = true;
                }
                LinearLayout priceShip = new LinearLayout(v.getContext());
                priceShip.setOrientation(LinearLayout.HORIZONTAL);
                priceShip.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                header.addView(tvv);
                //l.addView(tvv);
                if(tvprice!=null && currenPrice)
                    priceShip.addView(tvprice);
                if(shipPricetv!=null)
                    priceShip.addView(shipPricetv);
               // l.addView(tvprice);
                //l.addView(shipPricetv);
                header.addView(priceShip);
                //l.addView(priceShip);
               // LinearLayout layout2 = new LinearLayout(v.getContext());
                //layout2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                //layout2.setOrientation(LinearLayout.VERTICAL);

                TableLayout tableLayout = v.findViewById(R.id.higlightTab);
                TableRow tableRow;

                if(itmObj.has("Subtitle")){
                    tableRow = new TableRow(v.getContext());
                    TextView subTitleTv = new TextView(v.getContext());
                    subTitleTv.setText(itmObj.getString("Subtitle"));
                    subTitleTv.setPadding(10,10,10,10);
                    TextView subTitlLabel = new TextView(v.getContext());
                    subTitlLabel.setText("Subtitle");
                    subTitlLabel.setTextColor(Color.BLACK);
                    subTitlLabel.setPadding(8,8,8,8);
                    tableRow.addView(subTitlLabel);
                    tableRow.addView(subTitleTv);
                    tableLayout.addView(tableRow);
                }
                tableRow = new TableRow(v.getContext());
                TextView priceValue = new TextView(v.getContext());
              //  priceValue.setTextColor(0xFF6200EE);
                if(itmObj.has("CurrentPrice")) {
                    priceValue.setText("$" + itmObj.getJSONObject("CurrentPrice").getString("Value"));
                    TextView pricLabl = new TextView(v.getContext());
                    pricLabl.setText("Price");
                    pricLabl.setPadding(8, 8, 8, 8);
                    pricLabl.setTextColor(Color.BLACK);
                    tableRow.addView(pricLabl);
                    tableRow.addView(priceValue);
                    tableLayout.addView(tableRow);

                }


               // l.addView(tableLayout);

                TableLayout tableLayout2 = v.findViewById(R.id.specsTab);
                 if(itmObj.has("ItemSpecifics")) {
                     if (itmObj.getJSONObject("ItemSpecifics") != null) {
                         if(itmObj.getJSONObject("ItemSpecifics").has("NameValueList")) {
                             JSONArray itmSpecs = itmObj.getJSONObject("ItemSpecifics").getJSONArray("NameValueList");
                             if (itmSpecs != null) {
                                 for (int j = 0; j < itmSpecs.length(); j++) {
                                     TableRow tr = new TableRow(v.getContext());
                                     //                            TextView tvname = new TextView(v.getContext());
                                     //                            tvname.setText(itmSpecs.getJSONObject(j).getString("Name"));
                                     //                            tr.addView(tvname);
                                     String specName = itmSpecs.getJSONObject(j).getString("Name");
                                     if (specName.equals("Brand")) {
                                         TableRow brandRow = new TableRow(v.getContext());
                                         TextView brandLabel = new TextView(v.getContext());
                                         brandLabel.setText("Brand");
                                         brandLabel.setPadding(8, 8, 8, 8);
                                         brandLabel.setTextColor(Color.BLACK);
                                         brandRow.addView(brandLabel);

                                         TextView brandVal = new TextView(v.getContext());
                                         brandVal.setPadding(10, 10, 10, 10);
                                         brandVal.setText(itmSpecs.getJSONObject(j).getJSONArray("Value").getString(0));
                                         brandRow.addView(brandVal);

                                         TextView brandSpec = v.findViewById(R.id.BrandValue);
                                         brandSpec.setPadding(10, 10, 10, 10);
                                         brandSpec.setText("\u2022" + itmSpecs.getJSONObject(j).getJSONArray("Value").getString(0));

                                         tableLayout.addView(brandRow);

                                     } else {
                                         TextView tvValue = new TextView(v.getContext());
                                         tvValue.setText("\u2022" + itmSpecs.getJSONObject(j).getJSONArray("Value").getString(0));
                                         tvValue.setPadding(10, 10, 10, 10);
                                         tr.addView(tvValue);
                                     }

                                     tableLayout2.addView(tr);

                                 }

                             } else {
                                 LinearLayout specHeading = v.findViewById(R.id.specHeading);
                                 specHeading.setVisibility(View.GONE);
                                 Log.d("itemspec", "null");

                             }

                         }else{

                             LinearLayout specHeading = v.findViewById(R.id.specHeading);
                             specHeading.setVisibility(View.GONE);
                             Log.d("itemspec", "null");

                         }


                     }

                 }else {
                     LinearLayout specHeading = v.findViewById(R.id.specHeading);
                     specHeading.setVisibility(View.GONE);
                     Log.d("itemspec", "null");

                 }


            }else {
                //acknowledgement failure............
                itmErr= true;

                //Toast.makeText(getActivity(),"Invalid item ",Toast.LENGTH_LONG).show();

            }
        } catch (JSONException e) {
            Log.d("json","reachedher");
            e.printStackTrace();
        }


        final TextView invalidItmTv = v.findViewById(R.id.invalidItmTv);


        new Thread() {
            public void run() {

                try {
                    Thread.sleep(2000);
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pr.setVisibility(View.GONE);
                            searchProdtv.setVisibility(View.GONE);
                            if(itmErr){
                                invalidItmTv.setVisibility(View.VISIBLE);
                            }else {
                                hsv.setVisibility(View.VISIBLE);
                                headsect.setVisibility(View.VISIBLE);
                                cover.setVisibility(View.VISIBLE);
                            }

                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();


        return v;

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
