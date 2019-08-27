package com.example.ebaysearchapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Html;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.wssholmes.stark.circular_score.CircularScoreView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShippingDetail.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShippingDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShippingDetail extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int countShipInfoValues =0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ShippingDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShippingDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static ShippingDetail newInstance(String param1, String param2) {
        ShippingDetail fragment = new ShippingDetail();
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
        View  vv = inflater.inflate(R.layout.fragment_shipping_detail, container, false);
        Bundle b = this.getArguments();
        String shippingInfo = b.getString("shippingInfo");
        String Storefront = b.getString("storeInfo");
        String sellerInfo = b.getString("sellerInfo");
        String ReturnPolicy = b.getString("ReturnPolicy");
        String ConditionDescription = b.getString("ConditionDescription");
        String globalShipping = b.getString("globalShip");
        Log.d("inshipping",shippingInfo);
        Log.d("inshipping", sellerInfo);
        Log.d("Storefront", Storefront);
        Log.d("ReturnPolicy",ReturnPolicy);
        TableLayout tl1 = vv.findViewById(R.id.storeTab);
        TableRow tr;
        if(!Storefront.equals("N/A")) {

            tr = vv.findViewById(R.id.storeRow);
            TextView storeName = vv.findViewById(R.id.stornamlbl);
            storeName.setText("Store Name ");
            storeName.setTextColor(Color.BLACK);
            //storeName.setTypeface();
            storeName.setPadding(8, 8, 8, 8);
            // tr.addView(storeName);
            TextView storNameVal = vv.findViewById(R.id.stornamVl);
            storNameVal.setPadding(10, 10, 10, 10);
            String storeURL = "";
            String storNameTxt = "";
            try {
                storNameTxt = new JSONObject(Storefront).getString("StoreName");
                storeURL = new JSONObject(Storefront).getString("StoreURL");
                //storNameVal.setAutoLinkMask(1);

                storNameVal.setText(Html.fromHtml(
                        "<a href=" + storeURL + ">" + storNameTxt + "</a> "));
                storNameVal.setMovementMethod(LinkMovementMethod.getInstance());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //tr.addView(storNameVal);
            //tl1.addView(tr);
            tr.setVisibility(View.VISIBLE);

        }

            //if(sellerInfo!=null)
            if(!sellerInfo.equals("N/A")){
               // JSONArray sellerInfoObj ;
                JSONObject sellerInfoObj;
                try {

                    //sellerInfoObj =  new JSONArray(sellerInfo);
                    // do error handling here @pankhuri
                    sellerInfoObj = new JSONObject(sellerInfo);
                    String feedbackscore="";
                  //  String feedbackscore = sellerInfoObj.getJSONObject(0).getJSONArray("feedbackScore").getString(0);
                    if(sellerInfoObj.has("FeedbackScore")) {
                        tr = vv.findViewById(R.id.FdbckRow);
                        feedbackscore = sellerInfoObj.getString("FeedbackScore");
                        TextView feedBckLbl = vv.findViewById(R.id.fdbcklbl);
                        feedBckLbl.setText("Feedback Score");
                        feedBckLbl.setPadding(8, 8, 8, 8);
                        feedBckLbl.setTextColor(Color.BLACK);


                        TextView feedBckVal = vv.findViewById(R.id.fdbckval);
                        feedBckVal.setText(feedbackscore);
                        feedBckVal.setPadding(10, 10, 10, 10);
                        tr.setVisibility(View.VISIBLE);
                    }



//---------------------------------------  getting popularity value here----------------------------------
                    if(sellerInfoObj.has("PositiveFeedbackPercent")) {
                        tr = vv.findViewById(R.id.poplrRow);
                        String popularity = sellerInfoObj.getString("PositiveFeedbackPercent");
                        TextView popularityLbl = vv.findViewById(R.id.poplrLbl);
                        popularityLbl.setText("Popularity");
                        popularityLbl.setTextColor(Color.BLACK);
                        popularityLbl.setPadding(8, 8, 8, 8);

                        CircularScoreView circularScoreView = (CircularScoreView) vv.findViewById(R.id.poplrval);
                        circularScoreView.setScore((int)Math.round(Double.parseDouble(popularity)));

                       tr.setVisibility(View.VISIBLE);

                    }

//----------------------------------------- getting feedback star color value here -------------------------
                   if(sellerInfoObj.has("FeedbackRatingStar")) {

                       tr = vv.findViewById(R.id.starRow);
                       //  String feedbackstarColor = sellerInfoObj.getJSONObject(0).getJSONArray("feedbackRatingStar").getString(0);
                       String feedbackstarColor = sellerInfoObj.getString("FeedbackRatingStar");
                       TextView feedbackStrLbl = vv.findViewById(R.id.starLbl);
                       feedbackStrLbl.setText("Feedback star");
                       feedbackStrLbl.setPadding(8, 8, 8, 8);
                       feedbackStrLbl.setTextColor(Color.BLACK);

//                       TextView feedbckStrVal = vv.findViewById(R.id.starVal);
//                       feedbckStrVal.setText(feedbackstarColor);
//                       feedbckStrVal.setPadding(10, 10, 10, 10);

                       ImageView feedbackResponse = (ImageView) vv.findViewById(R.id.feedbackstartext);
                       boolean starCircle = false;
                       Drawable drawable;
                       if(Integer.parseInt(feedbackscore) >= 10000){
                           feedbackResponse.setImageResource(R.drawable.starcircle);
                           drawable = ContextCompat.getDrawable(getContext(), R.drawable.starcircle);
                          // starCircle = true;
                       }else{
                           feedbackResponse.setImageResource(R.drawable.staroutline);
                           drawable = ContextCompat.getDrawable(getContext(), R.drawable.staroutline);
                       }

    //------------------------------------setting of star color here -----------------------------------------//


                      // if(starCircle) {
                          // Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.starcircle);
                           Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
                           if (feedbackstarColor.equals("yellow") || feedbackstarColor.equals("YellowShooting")) {
                               DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(getContext(), R.color.colorYellow));
                           } else if (feedbackstarColor.equals("TurquoiseShooting") || feedbackstarColor.equals("TurquoiseShooting")) {

                               DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(getContext(), R.color.colortoq));

                           } else if (feedbackstarColor.equals("SilverShooting")) {

                               DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(getContext(), R.color.colorsilver));

                           } else if (feedbackstarColor.equals("RedShooting") || feedbackstarColor.equals("Red")) {

                               DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(getContext(), R.color.colorRed));

                           } else if (feedbackstarColor.equals("PurpleShooting") || feedbackstarColor.equals("Purple")) {
                               DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(getContext(), R.color.colorpurple));


                           } else if (feedbackstarColor.equals("GreenShooting") || feedbackstarColor.equals("Green")) {

                               DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(getContext(), R.color.colorGreen));

                           } else if (feedbackstarColor.equals("Blue") || feedbackstarColor.equals("BlueShooting")) {
                               DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(getContext(), R.color.blue));

                           }

                           feedbackResponse.setImageDrawable(drawable);
                      // }



                       tr.setVisibility(View.VISIBLE);

                   }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

                LinearLayout soldByHead = vv.findViewById(R.id.soldByHead);
                soldByHead.setVisibility(View.GONE);

            }






        //--------------------------------- getting shipping data for header section -------------------------//

        if(!shippingInfo.equals("N/A")){

            TableLayout  tl2 =  vv.findViewById(R.id.shipTab);
          //  TableRow tr ;
            JSONArray shipInfo;
            try {
                    shipInfo=  new JSONArray(shippingInfo);
                         if(shipInfo!=null) {

                             if(shipInfo.getJSONObject(0).has("shippingServiceCost")) {
                                 tr = new TableRow(vv.getContext());
                                 JSONArray shipCostArr = shipInfo.getJSONObject(0).getJSONArray("shippingServiceCost");
                                 TextView shipCostLbl = new TextView(vv.getContext());
                                 shipCostLbl.setText("Shipping Cost");
                                 shipCostLbl.setPadding(8, 8, 8, 8);
                                 shipCostLbl.setTextColor(Color.BLACK);
                                 tr.addView(shipCostLbl);

                                 TextView shipCostVal = new TextView(vv.getContext());
                                 shipCostVal.setPadding(10, 10, 10, 10);


                                 String shipcost = "";
                                 if (shipCostArr != null) {
                                     shipcost = shipCostArr.getJSONObject(0).getString("__value__");

                                 }
                                 if (shipcost.equals("0.0")) {
                                     shipCostVal.setText("Free Shipping");

                                 } else {
                                     shipCostVal.setText("$" + shipcost);

                                 }
                                 tr.addView(shipCostVal);
                                 tl2.addView(tr);
                                 countShipInfoValues++;
                             }

                             if(shipInfo.getJSONObject(0).has("handlingTime")) {
                                 tr = new TableRow(vv.getContext());
                                 TextView handlingLabel = new TextView(vv.getContext());
                                 handlingLabel.setText("Hanndling Time");
                                 handlingLabel.setPadding(8, 8, 8, 8);
                                 handlingLabel.setTextColor(Color.BLACK);
                                 tr.addView(handlingLabel);

                                 TextView handlingTimeVal = new TextView(vv.getContext());

                                 String handlTime = shipInfo.getJSONObject(0).getJSONArray("handlingTime").getString(0);
                                 Log.d("handlTime", handlTime);
                                 // handle day and days here @pankhuri.........
                                 String dayString = "day";
                                 if(Integer.parseInt(handlTime)>1){
                                        dayString = "days";
                                 }
                                 handlingTimeVal.setText(handlTime+" "+ dayString);
                                 handlingTimeVal.setPadding(10, 10, 10, 10);
                                 tr.addView(handlingTimeVal);
                                 tl2.addView(tr);
                                 countShipInfoValues++;

                             }

                         }


                if(!globalShipping.equals("N/A")){
                    tr = new TableRow(vv.getContext());
                    TextView shipLbl = new TextView(vv.getContext());
                    shipLbl.setText("Global Shipping");
                    shipLbl.setPadding(8,8,8,8);
                    shipLbl.setTextColor(Color.BLACK);
                    tr.addView(shipLbl);

                    TextView shipVal = new TextView(vv.getContext());
//                    condVal.setElegantTextHeight(true);
//                    condVal.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//                    condVal.setSingleLine(false);
                    if(globalShipping.equals("true"))
                        shipVal.setText("Yes");
                    else
                        shipVal.setText("No");
                    shipVal.setPadding(10,10,10,10);


                    tr.addView(shipVal);
                    tl2.addView(tr);
                    countShipInfoValues++;


                }

                if(!ConditionDescription.equals("N/A")){

                    tr = new TableRow(vv.getContext());
                    TextView condLabl = new TextView(vv.getContext());
                    condLabl.setText("Condition");
                    condLabl.setPadding(8,8,8,8);
                    condLabl.setTextColor(Color.BLACK);
                    tr.addView(condLabl);

                    TextView condVal = new TextView(vv.getContext());
//                    condVal.setElegantTextHeight(true);
//                    condVal.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//                    condVal.setSingleLine(false);
                    condVal.setText(ConditionDescription);
                    condVal.setWidth(550);
                    condVal.setPadding(10,10,10,10);


                    tr.addView(condVal);
                    tl2.addView(tr);
                    countShipInfoValues++;


                }



            } catch (JSONException e) {
                e.printStackTrace();
            }







        }else{
            LinearLayout shipInfoHead = vv.findViewById(R.id.shipInfoHead);
            shipInfoHead.setVisibility(View.GONE);
        }

        if(!ReturnPolicy.equals("N/A")){

            TableLayout tl3 = vv.findViewById(R.id.RetTab);
            //TableRow tr = new TableRow(vv.getContext());
            tr = new TableRow(vv.getContext());
            JSONObject retObj = null;
            try {
                retObj = new JSONObject(ReturnPolicy);
                if(retObj.has("ReturnsAccepted")){
                    TextView retLabel = new TextView(vv.getContext());
                    retLabel.setText("Policy");
                    retLabel.setTextColor(Color.BLACK);
                    retLabel.setPadding(8,8,8,8);
                    tr.addView(retLabel);
                    String returnAccepted =  retObj.getString("ReturnsAccepted");
                    TextView retPolVal = new TextView(vv.getContext());
                    retPolVal.setPadding(10,10,10,10);
                    retPolVal.setText(returnAccepted);
                    tr.addView(retPolVal);
                    tl3.addView(tr);

                    if(returnAccepted.equals("Returns Accepted")){

                        tr = new TableRow(vv.getContext());
                        TextView withinlbl = new TextView(vv.getContext());
                        withinlbl.setText("Within");
                        withinlbl.setTextColor(Color.BLACK);
                        withinlbl.setPadding(8,8,8,8);
                        tr.addView(withinlbl);


                        TextView withinVal = new TextView(vv.getContext());
                        withinVal.setText(retObj.getString("ReturnsWithin"));
                        withinVal.setPadding(10,10,10,10);
                        tr.addView(withinVal);
                        tl3.addView(tr);


                        //Refund

                        if(retObj.has("Refund")){

                            tr= new TableRow(vv.getContext());
                            TextView refundModLbl = new TextView(vv.getContext());
                            refundModLbl.setText("Refund Mode");
                            refundModLbl.setPadding(8,8,8,8);
                            refundModLbl.setTextColor(Color.BLACK);
                            tr.addView(refundModLbl);

                            TextView refundModVal = new TextView(vv.getContext());
                            refundModVal.setText(retObj.getString("Refund"));
                            refundModVal.setWidth(550);
                            refundModVal.setPadding(10,10,10,10);
                            tr.addView(refundModVal);
                            tl3.addView(tr);


                        }

                        if(retObj.has("ShippingCostPaidBy")){

                            tr= new TableRow(vv.getContext());
                            TextView shipByLbl = new TextView(vv.getContext());
                            shipByLbl.setText("Shipped By");
                            shipByLbl.setPadding(8,8,8,8);
                            shipByLbl.setTextColor(Color.BLACK);
                            tr.addView(shipByLbl);

                            TextView shipByVal = new TextView(vv.getContext());
                            shipByVal.setText(retObj.getString("ShippingCostPaidBy"));
                            shipByVal.setPadding(10,10,10,10);
                            tr.addView(shipByVal);
                            tl3.addView(tr);


                        }





                    }else {

                    }


                }



            } catch (JSONException e) {
                e.printStackTrace();
            }



        }else {

            LinearLayout retPolicyHead = vv.findViewById(R.id.retPolicyHead);
            retPolicyHead.setVisibility(View.GONE);
        }





        return vv;
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
