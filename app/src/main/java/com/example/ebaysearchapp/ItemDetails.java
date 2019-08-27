package com.example.ebaysearchapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemDetails extends AppCompatActivity implements photos.OnFragmentInteractionListener, similar.OnFragmentInteractionListener,ProductDetail.OnFragmentInteractionListener,ShippingDetail.OnFragmentInteractionListener {


    Bundle bundle;
    Bundle shipBundle;
    Bundle photoBundle;
    Bundle simBundle;
    String simItems;
    SharedPreferences sharedPreferences;
    String itemTitle ="";
    String price="";
    String itemUrl="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Toolbar toolbar = findViewById(R.id.tool_bar_item);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,  ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.RIGHT);
        layoutParams.rightMargin = 40;

        LayoutInflater inflaterr = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View abv = inflaterr.inflate(R.layout.myactionbar_layout, null);
        abv.setLayoutParams(layoutParams);
        actionBar.setCustomView(abv);



        ImageView facebook = abv.findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(Intent.ACTION_VIEW);
                String appId= "Your APPID here";

                if(itemTitle!=null) {
                    if (itemTitle.indexOf('#') != -1) {
                        itemTitle = itemTitle.replace('#', ' ');

                    }
                }
                String stringToShare = "Buy"+ itemTitle+ " at " + "$" +price;
                String hashtag= "%23CSCI571Spring2019Ebay";

                if(itemUrl.equals(""))
                {
                    itemUrl="https://www.ebay.com/";
                }
              //  String url = "https://www.facebook.com/dialog/share?app_id=368212807366654&display=popup&href=https%3A%2F%2Fdevelopers.facebook.com%2Fdocs%2F&redirect_uri=https%3A%2F%2Fdevelopers.facebook.com%2Ftools%2Fexplorer";
                String url = "https://www.facebook.com/dialog/share?app_id=" + appId + "&display=popup"  + "&quote=" + stringToShare + "&href=" + itemUrl + "&hashtag="+hashtag;
                Log.d("printfb",url);
                sharingIntent.setData(Uri.parse(url));



                startActivity(sharingIntent);
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_item);
        tabLayout.addTab(tabLayout.newTab().setText("Product"));
        tabLayout.addTab(tabLayout.newTab().setText("Shipping"));
        tabLayout.addTab(tabLayout.newTab().setText("Photos"));
        tabLayout.addTab(tabLayout.newTab().setText("Similar"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager =(ViewPager)findViewById(R.id.view_pager_item);
        ItemDetailAdapter myAdapter = new ItemDetailAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(myAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.getTabAt(0).setIcon(R.drawable.proddetail);
        tabLayout.getTabAt(1).setIcon(R.drawable.shipicon);
        tabLayout.getTabAt(2).setIcon(R.drawable.google);
        tabLayout.getTabAt(3).setIcon(R.drawable.equal);



        String responseData= getIntent().getStringExtra("itemResponse");
       // String sellerInfo= getIntent().getStringExtra("sellerInfo");
        String shippingInfo = getIntent().getStringExtra("shippingInfo");
        final String itemid = getIntent().getStringExtra("itemid");
        Log.d("ItemDetailsOf", itemid);
        final String title = getIntent().getStringExtra("title");
        itemTitle = title;
        actionBar.setTitle(title);
        String simItmResponse = getIntent().getStringExtra("simItmresp");
        final String itemjson = getIntent().getStringExtra("itemjson");
        //------------------------------ get Price for facebook------------------//
        try {
            price = new JSONObject(itemjson).getString("price");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("completeItem", itemjson);
        Log.d("itemresponse",responseData);
        //Log.d("itemresponse",sellerInfo);
        Log.d("itemresponse", shippingInfo);
        Log.d("itemresponse",itemid);
        Log.d("itemresponse",simItmResponse );

//---------------------------------------------- send item response data throught bundle-----------------------------//
        bundle = new Bundle();
        bundle.putString("itemresponse", responseData);
        Log.d("completeItem",responseData);
        bundle.putString("shippingInfo",shippingInfo);

//---------------------------------------------- send shipping data through bundle to the fragment -------------------//
        shipBundle = new Bundle();
        shipBundle.putString("shippingInfo",shippingInfo);
       // shipBundle.putString("sellerInfo",sellerInfo);
        String storeInfo ="N/A";
        String ReturnPolicy = "N/A";
        String condDescription = "N/A";
        String globalShipping = "N/A";
        String sellerInfo= "N/A";
        // error handling for invalid item when item is not prsent................
        try {
            if(new JSONObject(responseData).getJSONObject("Item").has("Storefront"))
                storeInfo = new JSONObject(responseData).getJSONObject("Item").getString("Storefront");

            if(new JSONObject(responseData).getJSONObject("Item").has("Seller"))
                sellerInfo = new JSONObject(responseData).getJSONObject("Item").getString("Seller");

            if(new JSONObject(responseData).getJSONObject("Item").has("ReturnPolicy"))
                 ReturnPolicy = new JSONObject(responseData).getJSONObject("Item").getString("ReturnPolicy");

            if(new JSONObject(responseData).getJSONObject("Item").has("ConditionDescription"))
                condDescription = new JSONObject(responseData).getJSONObject("Item").getString("ConditionDescription");
            if(new JSONObject(responseData).getJSONObject("Item").has("ViewItemURLForNaturalSearch"))
                itemUrl = new JSONObject(responseData).getJSONObject("Item").getString("ViewItemURLForNaturalSearch");
            if(new JSONObject(responseData).getJSONObject("Item").has("GlobalShipping"))
                globalShipping = new JSONObject(responseData).getJSONObject("Item").getString("GlobalShipping");


        } catch (JSONException e) {
            Log.d("inexception","inexception");
            e.printStackTrace();
        }
        shipBundle.putString("sellerInfo",sellerInfo);
        shipBundle.putString("storeInfo",storeInfo);
        shipBundle.putString("ReturnPolicy",ReturnPolicy);
        shipBundle.putString("ConditionDescription",condDescription );
        shipBundle.putString("globalShip",globalShipping);

//
//        try {
//            Log.d("storeInfo", new JSONObject(responseData).getJSONObject("Item").getString("ReturnPolicy"));
//            Log.d("storeInfo", new JSONObject(responseData).getJSONObject("Item").getString("Storefront"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        //----------------------------------------- form similar items bumdle-------------
            try {
                Log.d("sim", simItems.toString());
            }catch (Exception e){

                Log.d("inexceptions", e.toString());
            }
        simBundle = new Bundle();
        simBundle.putString("simitems",simItmResponse);


        //-----------------------------------------form photos Bundle  ----------------------//

        photoBundle  = new Bundle();
        photoBundle.putString("title", title );


        //----------------------------------------- setting up of images of floating button for wishlist  based on shared prefernces----//

        final FloatingActionButton fab = findViewById(R.id.wishListItem);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.contains(itemid)){
            fab.setImageResource(R.drawable.cartremove);
        }else
            fab.setImageResource(R.drawable.cartplus);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if(sharedPreferences.contains(itemid)){
                    editor.remove(itemid);
                    editor.commit();
                    fab.setImageResource(R.drawable.cartplus);
                    Toast.makeText(ItemDetails.this, title+"removed from wishlist", Toast.LENGTH_LONG).show();

                }
                else {
                    editor.putString(itemid,itemjson);
                    editor.commit();
                    fab.setImageResource(R.drawable.cartremove);
                    Toast.makeText(ItemDetails.this, title+"added to wishlist", Toast.LENGTH_LONG).show();

                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.d("memu",""+id);
        if (id== android.R.id.home) {
            this.finish();
            Log.d("inhome","home");
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class ItemDetailAdapter extends FragmentStatePagerAdapter {

        int mNumOfTabs;
        public ItemDetailAdapter(FragmentManager fm, int NoofTabs) {
            super(fm);
            this.mNumOfTabs = NoofTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    ProductDetail productDetail = new ProductDetail();
                    productDetail.setArguments(bundle);
                    return productDetail;
                case 1:
                    ShippingDetail shippingDetail = new ShippingDetail();
                    shippingDetail.setArguments(shipBundle);
                    Log.d("shipBundle",shipBundle.toString());
                    return shippingDetail;
                case 2:
                    photos photosfrag = new photos();
                    photosfrag.setArguments(photoBundle);
                    return  photosfrag;
                case 3:
                    similar similarfrag = new similar();
                    similarfrag.setArguments(simBundle);
                    Log.d("simBundle",simBundle.toString());
                    return similarfrag;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {

            return 4;

        }
    }
}
