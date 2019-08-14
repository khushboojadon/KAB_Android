package com.appflowsolutions.kab.Activities;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class AddHospital extends AppCompatActivity implements IVolleyResponseManager {

    TextView btnAddHospital;
    EditText txtHospitalName,txtMobileNumber,txtAddress;
    VolleyManager volleyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hospital);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        volleyManager = VolleyManager.getInstance(getApplicationContext(), this);
        findElementFromView();
        setOnClick();
    }
    @Override
    protected void onResume() {
        volleyManager = VolleyManager.getInstance(getApplicationContext(), this);
        super.onResume();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id== android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void setOnClick() {
        btnAddHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hospitalName=txtHospitalName.getText().toString().trim();

                String mobileNumber=txtMobileNumber.getText().toString().trim();
                String address=txtAddress.getText().toString().trim();

                if(validateFields(address,hospitalName,mobileNumber))
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Address", address);
                    params.put("HospitalName",hospitalName);
                    params.put("Mobile", mobileNumber);
                    params.put("UserId", Global.USERID.toString());
                    if(CommonFunctions.isInternetOn(getApplicationContext())){
                        volleyManager.volleyJsonObjectPostRequest(Urls.CREATE_CONTACT_URL,new JSONObject(params));
                    }else{
                        Toast.makeText(getApplicationContext(),"No Internet Available", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
    private void findElementFromView()
    {
        btnAddHospital= (TextView) findViewById(R.id.btn_add);
        txtAddress= (EditText) findViewById(R.id.et_address);
        txtHospitalName= (EditText) findViewById(R.id.et_hospitalName);
        txtMobileNumber= (EditText) findViewById(R.id.et_mobile);
    }
    private boolean validateFields(String address, String hospitalName, String mobileNumber) {

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
            txtMobileNumber.setError(null);
        }

        if (hospitalName.isEmpty()) {
            txtHospitalName.setError("Last name is empty");
            requestFocus(txtHospitalName);
            valid = false;
        } else {
            txtHospitalName.setError(null);
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
                        Global.CONTACTS.add(model);
                        txtAddress.setText("");
                        txtHospitalName.setText("");
                        txtMobileNumber.setText("");

                        Toast.makeText(getApplicationContext(), "Hospital Created Successfully", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(AddHospital.this, MainActivity.class);
                        startActivity(it);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Some Error in create hospital", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "Some Error in create hospital", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseError() {

    }
}
