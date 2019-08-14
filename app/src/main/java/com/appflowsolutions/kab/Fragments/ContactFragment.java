package com.appflowsolutions.kab.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.appflowsolutions.kab.Activities.*;
import com.appflowsolutions.kab.Adapters.*;
import com.appflowsolutions.kab.Interfaces.IVolleyResponseManager;
import com.appflowsolutions.kab.Models.*;
import com.appflowsolutions.kab.R;
import com.appflowsolutions.kab.Utils.*;
import com.appflowsolutions.kab.Utils.VolleyManager;
import com.appflowsolutions.kab.Utils.VolleyManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactFragment extends Fragment implements IVolleyResponseManager {
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
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        listView = (ListView) view.findViewById(R.id.lv_contact);
        swipeContainer = (SwipeRefreshLayout)  view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(),ContactFragment.this);
                volleyManager.volleyJsonObjectRequest(Urls.CONTACT_URL+  Global.USERID);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        if(Global.CONTACTS == null){

            Global.CONTACTS=new ArrayList<>();
        }



        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(),this);
        progressDialog.setMessage("Fetching Contacts...");
        CommonFunctions.showDialog(progressDialog);
        volleyManager.volleyJsonObjectRequest(Urls.CONTACT_URL+  Global.USERID);


        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < Global.CONTACTS.size(); i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("tvName", "" + Global.CONTACTS.get(i).getFirstName() + " " + Global.CONTACTS.get(i).getLastName());
            hm.put("tvMobile", "" + Global.CONTACTS.get(i).getMobile());
            hm.put("ivContact", Integer.toString(R.drawable.user));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {"ivContact", "tvName", "tvMobile"};

        // Ids of views in listview_layout
        int[] to = {R.id.ivContact, R.id.tvName, R.id.tvMobile};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(ContactFragment.this.getActivity(), aList, R.layout.contact_layout, from, to);

        // Getting a reference to listview of main.xml layout file


        // Setting the adapter to the listView
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactModel contactModel = Global.CONTACTS.get(position);
                Intent it = new Intent(ContactFragment.this.getActivity(), ContactProfile.class);
                it.putExtra(Global.CONTACTS_OBJECT_TAG, contactModel);
                startActivity(it);
            }
        });

        FloatingActionButton fab_contact = (FloatingActionButton) view.findViewById(R.id.fab_contact);
        fab_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(ContactFragment.this.getActivity(), AddContacts.class);
                startActivity(it);
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), this);
        super.onResume();

    }



    @Override
    public void onResponseSuccess(String response)
    {
        swipeContainer.setRefreshing(false);
        CommonFunctions.hideDialog(progressDialog);
            try
            {
                if (response != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResponseModel<List<ContactModel>>>() {
                    }.getType();
                    ResponseModel<List<ContactModel>> responseObj = gson.fromJson(response, type);
                    if (responseObj.getIsSuccess() == 1) {
                        if (responseObj.getData() != null) {
                            Global.CONTACTS = responseObj.getData();
                        } else {
                            Global.CONTACTS = new ArrayList<ContactModel>();
                        }
                        ((SimpleAdapter)listView.getAdapter()).notifyDataSetChanged();
                        listView.invalidate();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Some Error in getting contact", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Toast.makeText(getActivity().getApplicationContext(), "Some Error in getting contact", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onResponseError()
    {
        swipeContainer.setRefreshing(false);

        CommonFunctions.hideDialog(progressDialog);
        Toast.makeText(getActivity().getApplicationContext(), "Some error in getting contacts", Toast.LENGTH_LONG).show();

    }
}
