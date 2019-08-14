package com.appflowsolutions.kab.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.appflowsolutions.kab.Activities.*;
import com.appflowsolutions.kab.Adapters.*;
import com.appflowsolutions.kab.Interfaces.IVolleyResponseManager;
import com.appflowsolutions.kab.Models.*;
import com.appflowsolutions.kab.R;
import com.appflowsolutions.kab.Utils.*;
import com.appflowsolutions.kab.Utils.VolleyManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class HospitalFragment extends Fragment implements IVolleyResponseManager {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    VolleyManager volleyManager;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeContainer;

    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_hospital, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(),this);
        progressDialog.setMessage("Fetching hospitals...");
        listView = (ListView) view.findViewById(R.id.lv_hospital);
        swipeContainer = (SwipeRefreshLayout)  view.findViewById(R.id.swipeContainerhospital);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(),HospitalFragment.this);
                String url=(Global.USER.getAreaPinCode()==null || Global.USER.getAreaPinCode()=="")?Urls.HOSPITAL_URL+"0":Urls.HOSPITAL_URL+Global.USER.getAreaPinCode();
                volleyManager.volleyJsonObjectRequest(url);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        CommonFunctions.showDialog(progressDialog);
        String url=(Global.USER.getAreaPinCode()==null || Global.USER.getAreaPinCode()=="")?Urls.HOSPITAL_URL+"0":Urls.HOSPITAL_URL+Global.USER.getAreaPinCode();
        volleyManager.volleyJsonObjectRequest(url);
        FloatingActionButton fab_hospital = (FloatingActionButton)view.findViewById(R.id.fab_hospital);
        fab_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "this is sos float", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

//                Intent it = new Intent(HospitalFragment.this.getActivity(), AddHospital.class);
//                startActivity(it);
                Toast.makeText(getActivity().getApplicationContext(), "Here we will add hospitals", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), this);
        super.onResume();

    }

    private void setHospitalDataInList() {

        // Each row in the list stores name, mobile and image
//        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
//
//        for(int i = 0; i< Global.HOSPITALS.size(); i++){
//            HashMap<String, String> hm = new HashMap<String,String>();
//            hm.put("tvName", "" + Global.HOSPITALS.get(i).getName());
//            if(Global.HOSPITALS.get(i).getPhone2()!="" && Global.HOSPITALS.get(i).getPhone2()!="")
//            {
//                hm.put("tvMobile","" + Global.HOSPITALS.get(i).getPhone1()+", "+Global.HOSPITALS.get(i).getPhone2());
//            }
//            else
//            {
//                hm.put("tvMobile","" + Global.HOSPITALS.get(i).getPhone1());
//            }
//            hm.put("tvAddress","" + Global.HOSPITALS.get(i).getAddress());
//            aList.add(hm);
//        }
//
//        // Keys used in Hashmap
//        String[] from = {"tvName","tvMobile", "tvAddress" };
//
//        // Ids of views in listview_layout
//        int[] to = {R.id.tvName,R.id.tvMobile,R.id.tvAddress};
//
//        // Instantiating an adapter to store each items
//        // R.layout.listview_layout defines the layout of each item
//        SimpleAdapter adapter = new SimpleAdapter(HospitalFragment.this.getActivity(), aList, R.layout.hospital_layout, from, to);
//
//        // Getting a reference to listview of main.xml layout file
//
//
//        // Setting the adapter to the listView
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                HospitalModel hospitalModel= Global.HOSPITALS.get(position);
//                Intent it = new Intent(HospitalFragment.this.getActivity(), HospitalProfile.class);
//                it.putExtra(Global.HOSPITALS_OBJECT_TAG, hospitalModel);
//                startActivity(it);
//            }
//        });
    }

    @Override
    public void onResponseSuccess(String response)
    {
        swipeContainer.setRefreshing(false);
        CommonFunctions.hideDialog(progressDialog);
        try {
            if (response != null) {
                Gson gson = new Gson();

                Type type = new TypeToken<ResponseModel<List<HospitalModel>>>() {
                }.getType();
                ResponseModel<List<HospitalModel>> responseObj = gson.fromJson(response, type);
                if (responseObj.getIsSuccess() == 1) {
                    if (responseObj.getData() != null)
                    {
                        Global.HOSPITALS = responseObj.getData();
                    } else
                    {
                        Global.HOSPITALS = new ArrayList<HospitalModel>();
                    }
                    setHospitalDataInList();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Some Error in getting hospitals", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Some Error in getting hospitals", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseError() {
        swipeContainer.setRefreshing(false);
        CommonFunctions.hideDialog(progressDialog);
        Toast.makeText(getActivity().getApplicationContext(), "Some Error in getting hospitals", Toast.LENGTH_SHORT).show();
    }
}
