package com.appflowsolutions.kab.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageTemplateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageTemplateFragment extends Fragment implements IVolleyResponseManager {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FloatingActionButton btnAddMessageTemplate;

    VolleyManager volleyManager;
    ProgressDialog progressDialog;

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_message_template, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(),this);
        progressDialog.setMessage("Fetching Message template...");
        listView = (ListView) view.findViewById(R.id.lv_messageTemplate);
        CommonFunctions.showDialog(progressDialog);
        volleyManager.volleyJsonObjectRequest(Urls.MESSAGE_TEMPLATE_URL+Global.USERID);

         btnAddMessageTemplate = (FloatingActionButton) view.findViewById(R.id.btn_addmessagetemplate);
        btnAddMessageTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(MessageTemplateFragment.this.getActivity(), MessageActivity.class);
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


    private void setMessageDataToList() {
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        for(int i = 0; i< Global.MESSAGETEMPLATES.size(); i++)
        {
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("tvMessage", "" + Global.MESSAGETEMPLATES.get(i).getTitle());
            aList.add(hm);
        }
        // Keys used in Hashmap
        String[] from = { "tvMessage"};
        // Ids of views in listview_layout
        int[] to = {R.id.tvMessage};
        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(MessageTemplateFragment.this.getActivity(), aList, R.layout.message_template, from, to);
        // Setting the adapter to the listView
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent it = new Intent(MessageTemplateFragment.this.getActivity(), MessageProfile.class);
                it.putExtra(Global.EDIT_MESSAGE_OBJECT_TAG, Global.MESSAGETEMPLATES.get(position));
                startActivity(it);
            }
        });
    }

    @Override
    public void onResponseSuccess(String response) {
        CommonFunctions.hideDialog(progressDialog);
        try {
            if (response != null) {
                Gson gson = new Gson();

                Type type = new TypeToken<ResponseModel<List<MessageTemplateModel>>>() {
                }.getType();
                ResponseModel<List<MessageTemplateModel>> responseObj = gson.fromJson(response, type);
                if (responseObj.getIsSuccess() == 1) {
                    if (responseObj.getData() != null)
                    {
                        Global.MESSAGETEMPLATES= responseObj.getData();
                    } else {
                        Global.MESSAGETEMPLATES = new ArrayList<MessageTemplateModel>();
                    }
                    if(Global.MESSAGETEMPLATES.size()>=4)
                    {
                        btnAddMessageTemplate.setVisibility(View.GONE);
                    }
                    setMessageDataToList();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Some Error in getting message templates", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Some Error in getting message templates", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseError() {
        CommonFunctions.hideDialog(progressDialog);
        Toast.makeText(getActivity().getApplicationContext(), "Some Error in getting message templates", Toast.LENGTH_SHORT).show();

    }
}
