package com.appflowsolutions.kab.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appflowsolutions.kab.Adapters.ContactSpinnerAdapter;
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

public class MessageActivity extends AppCompatActivity implements IVolleyResponseManager
{
    EditText txtTitle,txtDescription;
    TextView btnSaveMessage;
    VolleyManager volleyManager;
    ProgressDialog progressDialog;
    String selectedContactId;
    Spinner materialDesignSpinner;
    String[] SPINNERLIST = {"Android Material Design", "Material Design Spinner", "Spinner Using Material Library", "Material Spinner ResponseModel"};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < Global.CONTACTS.size(); i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("tvName", "" + Global.CONTACTS.get(i).getFirstName() + " " + Global.CONTACTS.get(i).getLastName());
            hm.put("tvMobile", "" + Global.CONTACTS.get(i).getMobile());
            hm.put("ivContact", Integer.toString(R.drawable.user));
            aList.add(hm);
        }
        volleyManager = VolleyManager.getInstance(getApplicationContext(), this);

        // Keys used in Hashmap
        String[] from = {"ivContact", "tvName", "tvMobile"};

        // Ids of views in listview_layout
        int[] to = {R.id.ivContact, R.id.tvName, R.id.tvMobile};

       // SimpleAdapter adapter = new SimpleAdapter(this, aList, R.layout.contact_layout, from, to);
        ContactSpinnerAdapter adapter= new ContactSpinnerAdapter(this,Global.CONTACTS);
        findElementFromView();
        materialDesignSpinner.setAdapter(adapter);
        materialDesignSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedContactId=Global.CONTACTS.get(position).getId().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSaveMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String title=txtTitle.getText().toString().trim();
                String description=txtDescription.getText().toString().trim();

                if(validateFields(title,description,selectedContactId))
                {
                    progressDialog.setMessage("Saving Message...");
                    CommonFunctions.showDialog(progressDialog);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Title", title);
                    params.put("ContactId", selectedContactId);
                    params.put("Message", description);
                    params.put("UserId", Global.USERID.toString());
                    volleyManager.volleyJsonObjectPostRequest(Urls.CREATE_MESSAGE_TEMPLATE_URL,new JSONObject(params));
                }
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }
    private boolean validateFields(String title, String description, String selectedContactId) {

        boolean valid = true;

        if (description.isEmpty()) {
            txtDescription.setError("Description is empty");
            requestFocus(txtDescription);
            valid = false;
        }
        else
        {
            txtDescription.setError(null);
        }

        if (description.isEmpty()) {
            txtDescription.setError("Description number is empty");
            requestFocus(txtDescription);
            valid = false;
        } else
            {
                txtDescription.setError(null);
            }
        if(selectedContactId.isEmpty())
        {
            Toast.makeText(this,"Select a contact", Toast.LENGTH_LONG).show();
            valid=false;
        }
        return valid;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void findElementFromView() {
        materialDesignSpinner = (Spinner)
                findViewById(R.id.messageContact);
        txtTitle= (EditText) findViewById(R.id.messageTitle);
        txtDescription= (EditText) findViewById(R.id.messageDescription);
        btnSaveMessage= (TextView) findViewById(R.id.btn_createmessage);
    }

    @Override
    public void onResponseSuccess(String response) {
        CommonFunctions.hideDialog(progressDialog);
        try {
            if (response != null) {
                Gson gson = new Gson();

                Type type = new TypeToken<ResponseModel<MessageTemplateModel>>() {
                }.getType();
                ResponseModel<MessageTemplateModel> responseObj = gson.fromJson(response, type);
                if (responseObj.getIsSuccess() == 1) {
                    if (responseObj.getData().getId() > 0)
                    {
                        MessageTemplateModel model= responseObj.getData();
                        Global.MESSAGETEMPLATES.add(model);
                        txtDescription.setText("");
                        txtTitle.setText("");
                        Toast.makeText(getApplicationContext(), "Message Saved Successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Some Error in saving message", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Some Error in saving message", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResponseError() {

    }
}
