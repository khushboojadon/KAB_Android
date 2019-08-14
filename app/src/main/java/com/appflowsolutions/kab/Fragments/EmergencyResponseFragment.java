package com.appflowsolutions.kab.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
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

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmergencyResponseFragment extends Fragment implements IVolleyResponseManager {
    VolleyManager volleyManager;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeContainer;
    EmergencyResponseAdapter adapter;
    PopupWindow pw;
    ListView listView;
    String currentRequest="";
    int position=-1;
    String requestStatus="";
    EmergencyResponseFragment emergencyResponseFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_emergency_response, container, false);

        emergencyResponseFragment =this;
        listView = (ListView) view.findViewById(R.id.lv_ResponseNeededUser);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerResponse);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Map<String, String> params = new HashMap<String, String>();

                if(Global.USER.getUserType().equalsIgnoreCase("ADMIN")){
                    params.put("HospitalId", "0");
                    params.put("RequestType", "SOS");

                }
                else
                {
                    params.put("HospitalId", String.valueOf(Global.USER.getId()));
                    params.put("RequestType", "MEDICAL");

                }
                currentRequest="getrequests";
                volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), emergencyResponseFragment);
                if(CommonFunctions.isInternetOn(getContext())){
                    CommonFunctions.showDialog(progressDialog);
                    volleyManager.volleyJsonObjectPostRequest(Urls.GetHELPREQUEST_URL, new JSONObject(params));
                }else{
                    Toast.makeText(getContext(),"No Internet Available", Toast.LENGTH_LONG).show();
                }

            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Request...");
        Map<String, String> params = new HashMap<String, String>();

        if(Global.USER.getUserType().equalsIgnoreCase("ADMIN")){
            params.put("HospitalId", "0");
            params.put("RequestType", "SOS");

        }
        else
        {
            params.put("HospitalId", String.valueOf(Global.USER.getId()));
            params.put("RequestType", "MEDICAL");

        }     volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), emergencyResponseFragment);
        currentRequest="getrequests";
        if(CommonFunctions.isInternetOn(getContext())){

            CommonFunctions.showDialog(progressDialog);
            volleyManager.volleyJsonObjectPostRequest(Urls.GetHELPREQUEST_URL, new JSONObject(params));
        }else{
            Toast.makeText(getContext(),"No Internet Available", Toast.LENGTH_LONG).show();
        }

        if(Global.EMERGENCY_REQUESTS==null)
        {
            Global.EMERGENCY_REQUESTS= new ArrayList<>();
        }
        adapter = new EmergencyResponseAdapter(getContext());

        // Setting the adapter to the listView
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               emergencyResponseFragment.position=position;
                initiatePopupWindow(view,position);
            }
        });


        return view;
    }

    private void initiatePopupWindow(View v , final int position) {
        try {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup,null);
         pw = new PopupWindow(layout, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT, true);
        pw.showAtLocation(v, Gravity.CENTER, 0, 0);
            TextView cancelButton = (TextView) layout.findViewById(R.id.btn_cancel);
            TextView viewOnMapButton = (TextView) layout.findViewById(R.id.btn_viewOnMap);
            TextView ProcessingButton = (TextView) layout.findViewById(R.id.btn_Processing);
            TextView resolvedButton = (TextView) layout.findViewById(R.id.btn_resolved);
            TextView removeButton = (TextView) layout.findViewById(R.id.btn_remove);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                       pw.dismiss();
                }
            });
            viewOnMapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                        String lat = Global.EMERGENCY_REQUESTS.get(position).getLat();
                        String lng = Global.EMERGENCY_REQUESTS.get(position).getLong();
                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+lng);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                }
            });
             ProcessingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Id", Global.EMERGENCY_REQUESTS.get(position).getId().toString());
                    params.put("Status", "PROGRESSED");
                    requestStatus="PROGRESSED";
                    currentRequest="changeStatus";
                    volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), emergencyResponseFragment);
                    volleyManager.volleyJsonObjectPostRequest(Urls.CHANGE_REQUEST_STATUS_URL, new JSONObject(params));
                }
            });
            resolvedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Id", Global.EMERGENCY_REQUESTS.get(position).getId().toString());
                    params.put("Status", "RESOLVED");
                    requestStatus="RESOLVED";
                    currentRequest="changeStatus";
                    volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), emergencyResponseFragment);
                    volleyManager.volleyJsonObjectPostRequest(Urls.CHANGE_REQUEST_STATUS_URL, new JSONObject(params));
                }
            });
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentRequest="deleterequest";
                    volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), emergencyResponseFragment);
                    if(CommonFunctions.isInternetOn(getContext())){
                        volleyManager.volleyJsonObjectDeleteRequest(Urls.DELETE_REQUEST_URL+Global.EMERGENCY_REQUESTS.get(position).getId().toString());
                    }else{
                        Toast.makeText(getContext(),"No Internet Available", Toast.LENGTH_LONG).show();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume()
    {
        volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), this);
        super.onResume();
    }

    @Override
    public void onResponseSuccess(String response)
    {
        swipeContainer.setRefreshing(false);
        CommonFunctions.hideDialog(progressDialog);
        if(currentRequest.equalsIgnoreCase("getrequests")) {
            try {
                if (response != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResponseModel<List<HelpRequestModel>>>() {
                    }.getType();
                    ResponseModel<List<HelpRequestModel>> responseObj = gson.fromJson(response, type);
                    if (responseObj.getIsSuccess() == 1) {
                        if (responseObj.getData() != null) {
                            Global.EMERGENCY_REQUESTS = responseObj.getData();
                        } else {
                            Global.EMERGENCY_REQUESTS = new ArrayList<HelpRequestModel>();
                        }
                        adapter.notifyDataSetChanged();
                        listView.invalidate();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Error in getting help request", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Toast.makeText(getActivity().getApplicationContext(), "Error in getting help request", Toast.LENGTH_SHORT).show();
            }
        }else if (currentRequest.equalsIgnoreCase("changeStatus"))
        {
            try {
                if (response != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResponseModel<String>>() {
                    }.getType();
                    ResponseModel<String> responseObj = gson.fromJson(response, type);
                    if (responseObj.getIsSuccess() == 1) {

                        Global.EMERGENCY_REQUESTS.get(emergencyResponseFragment.position).setRequestStatus(requestStatus);
                        adapter.notifyDataSetChanged();
                        listView.invalidate();
                            pw.dismiss();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Error in getting help request", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Toast.makeText(getActivity().getApplicationContext(), "Error in getting help request", Toast.LENGTH_SHORT).show();
            }
        }
        else if(currentRequest.equalsIgnoreCase("deleterequest")){
            try {
                if (response != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResponseModel<String>>() {
                    }.getType();
                    ResponseModel<String> responseObj = gson.fromJson(response, type);
                    if (responseObj.getIsSuccess() == 1) {
                        Global.EMERGENCY_REQUESTS.remove(emergencyResponseFragment.position) ;
                        adapter.notifyDataSetChanged();
                        listView.invalidate();
                        pw.dismiss();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Error in getting help request", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Toast.makeText(getActivity().getApplicationContext(), "Error in getting help request", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResponseError()
    {
        swipeContainer.setRefreshing(false);

        CommonFunctions.hideDialog(progressDialog);
        Toast.makeText(getActivity().getApplicationContext(), "Error in getting help request", Toast.LENGTH_LONG).show();

    }
}
