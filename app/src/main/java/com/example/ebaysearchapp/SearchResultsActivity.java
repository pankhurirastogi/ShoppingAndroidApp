package com.example.ebaysearchapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemListener{

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> imageURLS = new ArrayList<>();
    JSONObject obj;
    List<Product> products = new ArrayList<Product>();
    MyRecyclerViewAdapter myRecyclerViewAdapter;
    boolean err = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Search Results");

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        String responseData= getIntent().getStringExtra("responseData");
        String keyword = getIntent().getStringExtra("keyword");
        try {
            obj = new JSONObject(responseData);
            String res = obj.getString("type");

            //------------------------check for success and failure here--------------------------//

            if(res.equals("Success")){

                JSONArray productJSONArray = obj.getJSONArray("results");
                for(int i =0;i<productJSONArray.length();i++){
                    JSONObject ob = productJSONArray.getJSONObject(i);
                    String imageURL = ob.getString("image");
                    String title   =  ob.getString("title");
                    String itemid  =  ob.getString("itemid");
                    String shipping = ob.getString("shipping");
                    Double price   =  Double.parseDouble(ob.getString("price"));
                    String zip    =    ob.getString("zip");
                    String sellerName =   ob.getString("seller");
                    String sellerInfo = ob.getString("sellerInfo");
                    String shippingInfo = ob.getString("shippingInfo");
                    String condition = ob.getString("condition");

                    Product temp = new Product(imageURL,title,itemid,shipping,price,zip,sellerName,sellerInfo,shippingInfo,condition);
                    products.add(temp);
                }

                TextView textView = findViewById(R.id.totalResults);
                String coloredKeyword = "<font color='#f57b5b'>"+keyword+"</font>";
                String coloredNo = "<font color='#f57b5b'>"+products.size()+"</font>";
                textView.setText(Html.fromHtml("Showing "+coloredNo+" for "+ coloredKeyword));
               // textView.setVisibility(View.VISIBLE);

                Log.d("firstid",""+products.get(0).itemid);

            }else{
                Log.d("obj", obj.toString());
                TextView errorTv = findViewById(R.id.noResults);
                //err flag is used for denoting error which is later used
                err = true;
                errorTv.setText(obj.getString("results"));
               // errorTv.setVisibility(View.VISIBLE);
                Log.d("curr" , "in error");
            }


            Log.d("resultType" , res);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        initRecyclerView();
    }




    public  void initRecyclerView(){
        final RecyclerView recyclerView  = findViewById(R.id.myRecylerView);
        final TextView textView = findViewById(R.id.totalResults);
        final TextView errorText = findViewById(R.id.noResults);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(products,this,this,null);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        final ProgressBar pr = findViewById(R.id.dataLoad);
        final TextView searchProdtv = findViewById(R.id.srchReslts);

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

        new Thread() {
            public void run() {

                    try {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                pr.setVisibility(View.GONE);
                                searchProdtv.setVisibility(View.GONE);
                                if(err){

                                    errorText.setVisibility(View.VISIBLE);

                                }
                                else {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    textView.setVisibility(View.VISIBLE);
                                }

                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

            }
        }.start();






    }


    @Override
    public void onItemClick(Product item) {
        Log.d("here","here");
       // Toast.makeText(getApplicationContext(),item.title, Toast.LENGTH_SHORT).show();
        getItemDetails(item);

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
    protected void onResume() {
        super.onResume();
        myRecyclerViewAdapter.notifyDataSetChanged();

        Log.d("resume", "The onResume() event");
    }



    public void getItemDetails(final Product item){
        final Intent itemDetailIntent = new Intent(getBaseContext(), ItemDetails.class);
        Gson gson = new Gson();
        // for sending entire item so that this can be directly put to wishlist
        //item.itemid = "531424674674";
        final String itemjson = gson.toJson(item);
        //for local..................
       // String url = "http://10.0.2.2:3000/getItemDetail/"+item.itemid;
        // for aws.................
       // String url = "http://connectebaypankhuri.us-east-2.elasticbeanstalk.com/getItemDetail/"+item.itemid;
        //for gcp
        String url = "https://us-central1-projectir.cloudfunctions.net/myfunction";
        Log.d("urlcheck",url);

        RequestQueue queue = Volley.newRequestQueue(this);
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
                       //for testing no similar item found...........................
                       // String itemId = "282389702634";
                       //for local ....................
                      // String urll = "http://10.0.2.2:3000/getSimilarItems/"+item.itemid;

                        //for aws.......................
                        String urll = "http://connectebaypankhuri.us-east-2.elasticbeanstalk.com/getSimilarItems/"+item.itemid;
                        Log.d("urlcheck",urll);
                        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
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
                        Toast.makeText(SearchResultsActivity.this, error.toString(),Toast.LENGTH_LONG).show();

                    }
                });

        queue.add(jsonObjectRequest);

    }




}
