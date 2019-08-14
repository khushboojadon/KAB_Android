package com.appflowsolutions.kab.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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
import java.util.HashMap;
import java.util.Map;

public class EditMessage extends AppCompatActivity implements IVolleyResponseManager {

    TextView btnEditMessage;
    MessageTemplateModel messageTemplateModel;
    EditText txtTitle,txtDescription;
    Spinner spinner;
    VolleyManager volleyManager;
    ProgressDialog progressDialog;
    String selectedContactId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_message);
        //Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        volleyManager = VolleyManager.getInstance(getApplicationContext(), this);
        findElementFromView();
        setOnClick();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        Intent i = getIntent();
        messageTemplateModel= (MessageTemplateModel)i.getSerializableExtra(Global.EDIT_MESSAGE_OBJECT_TAG);
        findElementFromView();
        ContactSpinnerAdapter adapter= new ContactSpinnerAdapter(this,Global.CONTACTS);
        findElementFromView();
        spinner.setAdapter(adapter);
        if(messageTemplateModel!=null)
        {
            txtTitle.setText(messageTemplateModel.getTitle());
            for(int j =0;j<Global.CONTACTS.size();j++)
            {
                if(messageTemplateModel.getContactId()!=null) {
                    if (Global.CONTACTS.get(j).getId().toString().equals(messageTemplateModel.getContactId().toString())) {
                        spinner.setSelection(j);
                        break;
                    }
                }
            }
            txtDescription.setText(messageTemplateModel.getMessage());
        }
    }
    @Override
    protected void onResume() {
        volleyManager = VolleyManager.getInstance(getApplicationContext(), this);
        super.onResume();
    }

    private void setOnClick() {
        btnEditMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                    if(CommonFunctions.isInternetOn(getApplicationContext())){
                        volleyManager.volleyJsonObjectPatchRequest(Urls.UPDATE_MESSAGE_TEMPLATE_URL+messageTemplateModel.getId(),new JSONObject(params));
                    }else{
                        Toast.makeText(getApplicationContext(),"No Internet Available", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedContactId=Global.CONTACTS.get(position).getId().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void findElementFromView() {
        btnEditMessage= (TextView) findViewById(R.id.btn_updatemessage);
        txtTitle= (EditText) findViewById(R.id.updatemessageTitle);
        txtDescription= (EditText) findViewById(R.id.updatemessageDescription);
        spinner= (Spinner) findViewById(R.id.updatemessageContact);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id== android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean validateFields(String title, String description, String selectedContactId) {

        boolean valid = true;

        if (description.isEmpty()) {
            txtDescription.setError("Description is empty");
            requestFocus(txtDescription);
            valid = false;
        } else
        {
            txtDescription.setError(null);
        }

        if (description.isEmpty()) {
            txtDescription.setError("Description number is empty");
            requestFocus(txtDescription);
            valid = false;
        } else {
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
                        txtTitle.setText(model.getTitle());
                        txtDescription.setText(model.getMessage());
                        for(int i =0;i<Global.CONTACTS.size();i++)
                        {
                            if(model.getContactId()!=null) {
                                if (Global.CONTACTS.get(i).getId().toString().equals(model.getContactId().toString())) {
                                    spinner.setSelection(i);
                                    break;
                                }
                            }
                        }

                        for(int i =0;i<Global.MESSAGETEMPLATES.size();i++)
                        {
                            if(Global.MESSAGETEMPLATES.get(i).getId().toString().equals(model.getId().toString()))
                            {
                                Global.MESSAGETEMPLATES.set(i,model);

                                break;
                            }
                        }
                        Toast.makeText(getApplicationContext(), "Message Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Some Error in updating Message", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Some Error in updating Message", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseError() {
        CommonFunctions.hideDialog(progressDialog);
        Toast.makeText(getApplicationContext(), "Some Error in updating contact", Toast.LENGTH_SHORT).show();




    }
}
