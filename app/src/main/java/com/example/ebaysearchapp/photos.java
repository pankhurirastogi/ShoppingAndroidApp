package com.example.ebaysearchapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link photos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link photos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class photos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<String> ImageURLs = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public photos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment photos.
     */
    // TODO: Rename and change types and number of parameters
    public static photos newInstance(String param1, String param2) {
        photos fragment = new photos();
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
        final View v = inflater.inflate(R.layout.fragment_photos, container, false);
        Bundle bundle = this.getArguments();
        final ProgressBar progressBar = v.findViewById(R.id.dataLoadPhotos);
        final TextView searchText = v.findViewById(R.id.srchResltsPhotos);

        progressBar.setVisibility(View.VISIBLE);
        searchText.setVisibility(View.VISIBLE);
        String title  = bundle.getString("title");
        final LayoutInflater ll = inflater;
        if(title!=null) {
            if (title.indexOf('/') != -1) {
                title = title.replace('/', ' ');

            }
        }
        //String urll = "http://10.0.2.2:3000/getImages/"+title;
        //http://connectebaypankhuri.us-east-2.elasticbeanstalk.com
       String urll = "http://connectebaypankhuri.us-east-2.elasticbeanstalk.com/getImages/"+title;
        Log.d("urlcheck",urll);
        RequestQueue queue = Volley.newRequestQueue(v.getContext());

        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                (Request.Method.GET, urll, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        //simItems = response.toString();
                        if(response.has("items")) {
                            try {
                                JSONArray photos = response.getJSONArray("items");
                                for(int i =0;i<photos.length();i++){
                                    ImageURLs.add(photos.getJSONObject(i).getString("link"));
                                   // Log.d("link", photos.getJSONObject(i).getString("link"));
                                }

                                LinearLayout mGallery = (LinearLayout) v.findViewById(R.id.photolayout);
                                if(ImageURLs!=null){
                                    for(int j=0;j<ImageURLs.size();j++){
                                        View view = ll.inflate(R.layout.photo_item,
                                                mGallery, false);
                                        ImageView img = (ImageView) view
                                                .findViewById(R.id.photo);
                                        Picasso.get()
                                                .load(ImageURLs.get(j)).fit()
                                                .into(img);
                                        mGallery.addView(view);


                                    }

                                }
                                ScrollView scrollView = v.findViewById(R.id.photoSroll);

                                progressBar.setVisibility(View.GONE);
                                searchText.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d("photos", response.toString());


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("inerror",error.toString());
                        TextView tvErr = v.findViewById(R.id.photoError);
                        progressBar.setVisibility(View.GONE);
                        searchText.setVisibility(View.GONE);
                        tvErr.setVisibility(View.VISIBLE);



                    }
                });

        queue.add(jsonObjectRequest2);

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
