package com.example.ebaysearchapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SearchPage.OnFragmentInteractionListener, WishList.OnFragmentInteractionListener{

    SharedPreferences sharedpreferences;
    String loc= "curr";
    String distance = "10";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Search"));
        tabLayout.addTab(tabLayout.newTab().setText("Wishlist"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager =(ViewPager)findViewById(R.id.view_pager);
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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

        String urll = "http://ip-api.com/json";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                (Request.Method.GET, urll, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("ipapi", response.toString());
                        TextView currZip = findViewById(R.id.hiddenZip);
                        try {
                            currZip.setText(response.getString("zip"));
                            Log.d("zipcode",response.getString("zip"));
                        } catch (JSONException e) {
                            e.printStackTrace();


                        }

                        Log.d("textset", currZip.getText().toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ipapierror", error.toString());
                    }
                });

        queue.add(jsonObjectRequest2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        AppCompatAutoCompleteTextView autoCompleteTextView = findViewById(R.id.auto_complete_edit_text);
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_curr:
                if (checked)
                   loc= "curr";

                autoCompleteTextView.setEnabled(false);

                    break;
            case R.id.radio_diff:
                if (checked)
                    // Ninjas rule
                    loc="given";

                autoCompleteTextView.setEnabled(true);
                    break;
        }
    }



    public void editcheckbox(View v){


        boolean checked = ((CheckBox) v).isChecked();
        if(checked){

                    LinearLayout searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
                    searchLayout.setVisibility(View.VISIBLE);
        }else {
            LinearLayout searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
            searchLayout.setVisibility(View.GONE);
        }

    }

    private boolean notNumeric(String strNum ){

        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return true;
        }
        return false;


    }

    private boolean performValidation(String keyword, String zipcode, String loc, boolean locbasedsearch){
        boolean keywordErr = false;
        boolean zipcodeErr = false;

        if(keyword==null ||keyword.equals(" ") || keyword.trim().isEmpty()){
            keywordErr = true;
            //keywordErro
            TextView keyErrTv = findViewById(R.id.keywordErro);
            keyErrTv.setVisibility(View.VISIBLE);


        }else {

            TextView keyErrTv = findViewById(R.id.keywordErro);
            keyErrTv.setVisibility(View.GONE);

        }
        if(locbasedsearch) {
            if (loc.equals("given")) {
                if (zipcode == null || zipcode.equals(" ") || zipcode.trim().isEmpty() || notNumeric(zipcode) || zipcode.length() != 5) {
                    zipcodeErr = true;
                    TextView zipErrTv = findViewById(R.id.zipcodeerror);
                    zipErrTv.setVisibility(View.VISIBLE);

                } else {
                    TextView zipErrTv = findViewById(R.id.zipcodeerror);
                    zipErrTv.setVisibility(View.GONE);

                }

            }
        }

        return keywordErr||zipcodeErr;
    }

   public void searhProducts(View v){

      //  final TextView tv = findViewById(R.id.tv);
       final Intent myIntent = new Intent(getBaseContext(), SearchResultsActivity.class);
        //Optional parameters

       Spinner mySpinner = (Spinner) findViewById(R.id.SpinnerCat);
       String categoryText = mySpinner.getSelectedItem().toString();

       EditText keyWordET = (EditText) findViewById(R.id.keywordTxt);
       final String keyword = keyWordET.getText().toString();

       Log.d("myspinner", categoryText);
       if(!getStringQuery().equals("")) {
           String url = "http://connectebaypankhuri.us-east-2.elasticbeanstalk.com/searchProducts/" + getStringQuery();
           //for local
           Log.d("urlcheck",url);
           RequestQueue queue = (RequestQueue) Volley.newRequestQueue(this);
           //

           JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                   (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                       @Override
                       public void onResponse(JSONObject response) {
                           //                        tv.setText("Response: " + response.toString());
                           Log.d("jsonResp", response.toString());
                           myIntent.putExtra("responseData", response.toString());
                           myIntent.putExtra("keyword",keyword);
                           startActivity(myIntent);
                       }
                   }, new Response.ErrorListener() {

                       @Override
                       public void onErrorResponse(VolleyError error) {
                           // TODO: Handle error
                           Log.d("myerror", error.toString());
                           Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                       }
                   });

           queue.add(jsonObjectRequest);

       }




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public String getStringQuery(){
        String New  = null;
        String Used = null;
        String Unspecified = null;
        String FreeShippingOnly = null;
        String LocalPickupOnly = null;


        //----------------------------------get keyword text value --------------------------------------------------------------//
        EditText keyWordET = (EditText) findViewById(R.id.keywordTxt);
        String keyword = keyWordET.getText().toString();

        //------------------------------------get Category value ---------------------------------------//

        Spinner mySpinner = (Spinner) findViewById(R.id.SpinnerCat);
        String categoryText = mySpinner.getSelectedItem().toString();
        String Category = "";
        switch (categoryText){
            case "Video Games & Consoles":
                Category = "11233";
                break;
            case "Art":
                Category ="550";
            case "Baby":
                Category="2984";
                break;
            case "Books":
                Category  = "267";
                break;
            case  "Clothing, Shoes & Accessories":
                Category = "11450";
                break;
            case "Computers/Tablets & Networking":
                Category = "58058";
                break;
            case "Health & Beauty":
                Category= "26395";
                break;
            case "Music":
                Category  = "11233";
                break;

        }


        //-------------------------------------get Condition Value----------------------------------------//

        CheckBox newCheckBox = findViewById(R.id.newCheckbox);
        if(newCheckBox.isChecked())
            New = "new";

        CheckBox oldCheckBox = findViewById(R.id.usedCheckbox);
        if(oldCheckBox.isChecked())
            Used = "use";

        CheckBox unspCheckBox = findViewById(R.id.unCheckbox);
        if(unspCheckBox.isChecked())
            Unspecified = "unspecified";

        //-----------------------------------------get shipping options------------------------------------------------//


        CheckBox freeShipChex = findViewById(R.id.freeCheckbox);
        if(freeShipChex.isChecked())
            FreeShippingOnly = "true";

        CheckBox localCheckbox = findViewById(R.id.locCheckbox);
        if(localCheckbox.isChecked())
            LocalPickupOnly = "true";

        //-------------------------------------------- get Zip values ------------------------------------------------//

        CheckBox enbNrSrchLbl = findViewById(R.id.enbNrSrchLbl);
        boolean locBasedSearch = false;
        if(enbNrSrchLbl.isChecked())
            locBasedSearch = true;


        TextView hereZipTv = findViewById(R.id.hiddenZip);
        String hereZip = hereZipTv.getText().toString();
        Log.d("i am herezip" , hereZip);


         AppCompatAutoCompleteTextView autoCompleteTextView = findViewById(R.id.auto_complete_edit_text);
         String givenZip = autoCompleteTextView.getText().toString().trim();


         //---------------------------------------------get Distnace values ---------------------------------------//
        TextView distTv = findViewById(R.id.dist);
        String distVal = distTv.getText().toString();
        if(distVal!=null&&!distVal.trim().isEmpty()){
            distance = distVal;
        }

        String strQuery = "";
        boolean error = performValidation(keyword,givenZip,loc,locBasedSearch);
        if(error){
            Toast.makeText(this,"Please fix all the fields with errors",Toast.LENGTH_SHORT).show();
        }else {


            strQuery = "?keyword=" + keyword;
            strQuery += "&Category=" + Category;
            if(locBasedSearch) {
                if (loc.equals("curr")) {
                    strQuery += "&hereZip=" + hereZip;
                    Log.d("curr", loc);
                    Log.d("here", hereZip);
                } else {
                    strQuery += "&givenZip=" + givenZip;
                }

            }
            //strQuery+="&hereZip="+"90007";
            if (New != null)
                strQuery += "&New=" + New;
            if (Used != null)
                strQuery += "&Used=" + Used;
            if (Unspecified != null)
                strQuery += "&Unspecified=" + Unspecified;
            if (FreeShippingOnly != null) {
                strQuery += "&FreeShippingOnly=" + FreeShippingOnly;
            }
            if (LocalPickupOnly != null) {
                strQuery += "&LocalPickupOnly=" + LocalPickupOnly;
            }
            strQuery += "&dist=" + distance;

        }

        return strQuery;



        // checkValidation(keyword,enableLocalSearch,zipcodevalue);

    }

    @Override
    protected void onResume() {
        super.onResume();


        Log.d("resume", "The onResume() main activity event");
    }



//    public void clearhsharedPreference(View v) {
//
//                sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.clear();
//                editor.commit();
//                Map<String, ?> ss = sharedpreferences.getAll();
//                if(ss==null)
//                    Toast.makeText(this, "clean", Toast.LENGTH_LONG).show();
//                else
//                    Toast.makeText(this,"notclean"+ ss.size(),Toast.LENGTH_LONG).show();
//
//
//            }

    public void onClear(View v){
        distance = "10";
        String loc= "curr";

        //------------------------ setting all the ui elements back to initial or empty values ------------------//
        EditText keyWordET = (EditText) findViewById(R.id.keywordTxt);
        keyWordET.setText("");



        Spinner mySpinner = (Spinner) findViewById(R.id.SpinnerCat);
        mySpinner.setSelection(0);

        RadioButton rd = findViewById(R.id.radio_curr);
        rd.setSelected(true);
        rd.setChecked(true);

        CheckBox enbNrSrchLbl = findViewById(R.id.enbNrSrchLbl);
        enbNrSrchLbl.setChecked(false);

        TextView distTv = findViewById(R.id.dist);
        distTv.setText("");

        TextView zipErrTv = findViewById(R.id.zipcodeerror);
        zipErrTv.setVisibility(View.GONE);

        TextView keyErrTv = findViewById(R.id.keywordErro);
        keyErrTv.setVisibility(View.GONE);

        AppCompatAutoCompleteTextView autoCompleteTextView = findViewById(R.id.auto_complete_edit_text);
        autoCompleteTextView.setText("");
        autoCompleteTextView.setEnabled(false);

        LinearLayout searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);










    }

}
