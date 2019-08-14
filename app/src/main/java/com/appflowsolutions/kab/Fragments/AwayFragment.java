package com.appflowsolutions.kab.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
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

public class AwayFragment extends Fragment implements IVolleyResponseManager {
    VolleyManager volleyManager;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeContainer;
    AwayAdapter adapter;
    PopupWindow pw;
    ListView listView;
    int position=-1;
    String requestStatus="";
    AwayFragment awayFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_away, container, false);
        awayFragment =this;
        listView = (ListView) view.findViewById(R.id.lv_awayUser);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerAway);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), awayFragment);
            if(CommonFunctions.isInternetOn(getContext())){
                CommonFunctions.showDialog(progressDialog);
                volleyManager.volleyJsonObjectRequest(Urls.GET_AWAY_FROM_HOME_URL+Global.USERID);
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

        volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), awayFragment);
        if(CommonFunctions.isInternetOn(getContext())){
            CommonFunctions.showDialog(progressDialog);
            volleyManager.volleyJsonObjectRequest(Urls.GET_AWAY_FROM_HOME_URL+Global.USERID);
        }else{
            Toast.makeText(getContext(),"No Internet Available", Toast.LENGTH_LONG).show();
        }

        if(Global.AWAYREQUESTS==null)
        {
            Global.AWAYREQUESTS= new ArrayList<>();
        }
        adapter = new AwayAdapter(getContext());
        // Setting the adapter to the listView
        listView.setAdapter(adapter);
        return view;
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
        try {
            if (response != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResponseModel<List<AwayModel>>>() {
                }.getType();
                ResponseModel<List<AwayModel>> responseObj = gson.fromJson(response, type);
                if (responseObj.getIsSuccess() == 0) {

                    Global.AWAYREQUESTS = responseObj.getData();
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
    }

    @Override
    public void onResponseError()
    {
        swipeContainer.setRefreshing(false);
        CommonFunctions.hideDialog(progressDialog);
        Toast.makeText(getActivity().getApplicationContext(), "Error in getting help request", Toast.LENGTH_LONG).show();

    }
}
