package com.appflowsolutions.kab.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class EditContacts extends AppCompatActivity implements IVolleyResponseManager {

    TextView btnAddContact;
    ContactModel contactModel;
    EditText txtFirstName,txtLastName,txtMobileNumber,txtAddress;
    VolleyManager volleyManager;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contacts);
        //Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        volleyManager = VolleyManager.getInstance(getApplicationContext(), this);
        findElementFromView();
        setOnClick();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        Intent i = getIntent();
        contactModel= (ContactModel)i.getSerializableExtra(Global.EDIT_CONTACTS_OBJECT_TAG);
        findElementFromView();
        if(contactModel!=null)
        {
            txtFirstName.setText(contactModel.getFirstName());
            txtLastName.setText(contactModel.getLastName());
            txtAddress.setText(contactModel.getAddress());
            txtMobileNumber.setText(contactModel.getMobile());
        }
    }
    @Override
    protected void onResume() {
        volleyManager = VolleyManager.getInstance(getApplicationContext(), this);
        super.onResume();
    }

    private void setOnClick() {
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName=txtFirstName.getText().toString().trim();
                String lastName=txtLastName.getText().toString().trim();
                String mobileNumber=txtMobileNumber.getText().toString().trim();
                String address=txtAddress.getText().toString().trim();
                if(validateFields(address,firstName,lastName,mobileNumber))
                {
                    progressDialog.setMessage("Updating Contact...");
                    CommonFunctions.showDialog(progressDialog);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Address", address);
                    params.put("FirstName", firstName);
                    params.put("LastName", lastName);
                    params.put("Mobile", mobileNumber);
                    params.put("UserId", Global.USERID.toString());
                    if(CommonFunctions.isInternetOn(getApplicationContext())){
                        volleyManager.volleyJsonObjectPatchRequest(Urls.UPDATE_CONTACT_URL + contactModel.getId(),new JSONObject(params));
                    }else{
                        Toast.makeText(getApplicationContext(),"No Internet Available", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    private void findElementFromView() {
        btnAddContact= (TextView) findViewById(R.id.btn_add);
        txtAddress= (EditText) findViewById(R.id.address);
        txtFirstName= (EditText) findViewById(R.id.first_name);
        txtLastName= (EditText) findViewById(R.id.last_name);
        txtMobileNumber= (EditText) findViewById(R.id.mobphone);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id== android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean validateFields(String address, String firstName, String lastName, String mobileNumber) {
        boolean valid = true;
        if (address.isEmpty()) {
            txtAddress.setError("Address is empty");
            requestFocus(txtAddress);
            valid = false;
        } else {
            txtAddress.setError(null);
        }

        if (mobileNumber.isEmpty()) {
            txtMobileNumber.setError("Mobile number is empty");
            requestFocus(txtMobileNumber);
            valid = false;
        } else {
            if(mobileNumber.length()!=10){
                txtMobileNumber.setError("enter a valid mobile number");
                requestFocus(txtMobileNumber);
                valid = false;
            }
            else {
                txtMobileNumber.setError(null);
            }
        }

        if (lastName.isEmpty()) {
            txtLastName.setError("Last name is empty");
            requestFocus(txtLastName);
            valid = false;
        } else {
            txtLastName.setError(null);
        }

        if (firstName.isEmpty()) {
            txtFirstName.setError("First name is empty");
            requestFocus(txtFirstName);
            valid = false;
        } else {
            txtFirstName.setError(null);
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
                Type type = new TypeToken<ResponseModel<ContactModel>>() {
                }.getType();
                ResponseModel<ContactModel> responseObj = gson.fromJson(response, type);
                if (responseObj.getIsSuccess() == 1) {
                    if (responseObj.getData().getId() > 0)
                    {
                        ContactModel model= responseObj.getData();
                        txtAddress.setText(model.getAddress());
                        txtFirstName.setText(model.getFirstName());
                        txtLastName.setText(model.getLastName());
                        txtMobileNumber.setText(model.getMobile());
                        for(int i =0;i<Global.CONTACTS.size();i++)
                        {
                            if(Global.CONTACTS.get(i).getId()==model.getId())
                            {
                                Global.CONTACTS.set(i,model);
                                break;
                            }
                        }
                        Toast.makeText(getApplicationContext(), "Contact Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Some Error in updating contact", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Some Error in updating contact", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseError() {
        CommonFunctions.hideDialog(progressDialog);
        Toast.makeText(getApplicationContext(), "Some Error in updating contact", Toast.LENGTH_SHORT).show();




    }
}
