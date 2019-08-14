package com.appflowsolutions.kab.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


public class Register extends AppCompatActivity implements IVolleyResponseManager {

    EditText txtFirstName, txtMobileNumber, txtLastName, txtUserCode,txtAddress,txtPinCode;
    TextView btn_login, btn_register;
    VolleyManager volleyManager;
    String TAG = "Abc";
    ProgressDialog progressDialog;
    UserSessionManager userSession;
    String mobileNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userSession = new UserSessionManager(getApplicationContext());
        volleyManager = VolleyManager.getInstance(getApplicationContext(), this);
        findElementFromView();
        setFontOnElement();
        setOnClick();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onResume() {
        volleyManager = VolleyManager.getInstance(getApplicationContext(), this);
        super.onResume();
    }

    private void setOnClick() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Register.this, LoginActivity.class);
                startActivity(it);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if( CommonFunctions.isInternetOn(getApplicationContext())==true)
                {
                    String userCode = txtUserCode.getText().toString().trim();
                    String firstName = txtFirstName.getText().toString().trim();
                    String lastName = txtLastName.getText().toString().trim();
                     mobileNumber = txtMobileNumber.getText().toString().trim();
                    String address = txtAddress.getText().toString().trim();
                    String pincode = txtPinCode.getText().toString().trim();

                    if (validateFields(firstName, lastName, mobileNumber, userCode, address, pincode)) {
                        String REQUEST_TAG = "KAP_STRING";
                        progressDialog.setMessage("Please wait...");
                        CommonFunctions.showDialog(progressDialog);
                        // Posting params to register url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("FirstName", firstName);
                        params.put("LastName", lastName);
                        params.put("UserId", mobileNumber);
                        params.put("UserCode", userCode);
                        params.put("Address", address);
                        params.put("AreaPinCode", pincode);
                        params.put("UserType", "USER");

                        volleyManager.volleyJsonObjectPostRequest(Urls.REGISTER_URL, new JSONObject(params));

                    } else {

                    }
                }else
                {
                    Toast.makeText(getApplicationContext(),"No Internet Connection", Toast.LENGTH_LONG).show();;
                }
            }
        });
    }

    private boolean validateFields(String firstName, String lastName, String mobileNumber, String userCode, String address, String pincode) {

        boolean valid = true;
        if (pincode.isEmpty()) {
            txtPinCode.setError("Pincode is empty");
            requestFocus(txtPinCode);
            valid = false;
        } else {
            txtPinCode.setError(null);
        }
        if (address.isEmpty()) {
            txtAddress.setError("Address is empty");
            requestFocus(txtAddress);
            valid = false;
        } else {
            txtAddress.setError(null);
        }
        if (userCode.isEmpty()) {
            txtUserCode.setError("Password is empty");
            requestFocus(txtUserCode);
            valid = false;
        } else {
            txtUserCode.setError(null);
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

    private void setFontOnElement()
    {
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
        txtFirstName.setTypeface(custom_font);
        txtLastName.setTypeface(custom_font);
        txtUserCode.setTypeface(custom_font);
        txtMobileNumber.setTypeface(custom_font);
        txtAddress.setTypeface(custom_font);
        txtPinCode.setTypeface(custom_font);
         btn_register.setTypeface(custom_font1);
       // btn_login.setTypeface(custom_font);
    }

    private void findElementFromView() {
        btn_register = (TextView) findViewById(R.id.btn_register);
        btn_login = (TextView) findViewById(R.id.btn_login);
        txtLastName = (EditText) findViewById(R.id.last_name);
        txtFirstName = (EditText) findViewById(R.id.first_name);
        txtMobileNumber = (EditText) findViewById(R.id.mobphone);
        txtUserCode = (EditText) findViewById(R.id.userCode);
        txtAddress=(EditText) findViewById(R.id.r_address);
        txtPinCode=(EditText) findViewById(R.id.pincode);
    }

    @Override
    public void onResponseSuccess(String response) {
        CommonFunctions.hideDialog(progressDialog);
        try {
            if (response != null) {
                Gson gson = new Gson();

                Type type = new TypeToken<ResponseModel<UserModel>>() {
                }.getType();
                ResponseModel<UserModel> responseObj = gson.fromJson(response, type);
                if (responseObj.getIsSuccess() == 1)
                {
                    if (responseObj.getData().getId() > 0)
                    {
                        Global.USER = responseObj.getData();
                        Global.USERID = Global.USER.getId();
                        Global.USERLOGINID = mobileNumber;
                        Global.USERTOKEN = Global.USER.getToken();
                        Global.USERTYPE = Global.USER.getUserType();

                        userSession.createUserLoginSession(Global.USERTOKEN, Global.USERLOGINID, String.valueOf(Global.USERID), Global.USERTYPE);
                            Intent it = new Intent(Register.this, MainActivity.class);
                            startActivity(it);
                            finish();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
//            else {
//                Toast.makeText(getApplicationContext(), "Some Error in Registration", Toast.LENGTH_SHORT).show();
//            }
        }
        catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Some Error in Registration", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseError() {
        CommonFunctions.hideDialog(progressDialog);
        Toast.makeText(getApplicationContext(), "Some Error in Registration", Toast.LENGTH_SHORT).show();
    }
}
