package com.example.ebaysearchapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchPage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SuggestionAdapter autoSuggestAdapter;
    Handler handler;

    private OnFragmentInteractionListener mListener;

    public SearchPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchPage.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchPage newInstance(String param1, String param2) {
        SearchPage fragment = new SearchPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void searhProducts(){


        //String url ="http://10.0.2.2:3000/";
//        String url = "http://10.0.2.2:3000/getItemDetail/283446534071";
//    RequestQueue queue = (RequestQueue) Volley.newRequestQueue(this.getContext());
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        tv.setText("Response is: "+ response);
//                        Log.d("myerror",response.toString());
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                tv.setText(error.toString());
//                Log.d("myerror",error.toString());
//            }
//        });

        // queue.add(stringRequest);



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

        final View v = inflater.inflate(R.layout.fragment_search_page, container, false);
        final AppCompatAutoCompleteTextView autoCompleteTextView =
                v.findViewById(R.id.auto_complete_edit_text);
        autoSuggestAdapter = new SuggestionAdapter(v.getContext(),
                android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //latest change ...... @pankhuri
//                Toast.makeText(getActivity(), autoSuggestAdapter.getObject(position) ,Toast.LENGTH_LONG
//                ).show();
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                handler.removeMessages(100);
                handler.sendEmptyMessageDelayed(100,
                        300);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });

        handler = new Handler(new Handler.Callback() {
            @Override
        public boolean handleMessage(Message msg) {
                if (msg.what == 100) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        //

                        String query = autoCompleteTextView.getText().toString();
                        final List<String> postalCodeArr = new ArrayList();
                        //String zipCodeURL = "http://10.0.2.2:3000/getZipCodes/"+query;
                        String zipCodeURL = "http://connectebaypankhuri.us-east-2.elasticbeanstalk.com/getZipCodes/"+query;
                        Log.d("urlcheck",zipCodeURL);
                        RequestQueue queue = Volley.newRequestQueue(v.getContext());
                        JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest
                                (Request.Method.GET, zipCodeURL, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {

                                        try {
                                            JSONArray postalCodes = response.getJSONArray("postalCodes");
                                            for (int j = 0; j < postalCodes.length(); j++) {
                                                String s = postalCodes.getJSONObject(j).getString("postalCode");
                                                postalCodeArr.add(s);
                                                Log.d("postal", s);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        autoSuggestAdapter.setmZipData(postalCodeArr);
                                        autoSuggestAdapter.notifyDataSetChanged();
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO: Handle error
                                        Log.d("inerror","error");
                                    }
                                });

                        queue.add(jsonObjectRequest3);
                    }
                }
                return false;
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
