package com.appflowsolutions.kab.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
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

public class LoginActivity extends AppCompatActivity implements IVolleyResponseManager {

    EditText txtUserCode, txtMobileNumber;
    TextView register, btn_login;
    VolleyManager volleyManager;
    ProgressDialog progressDialog;
    UserSessionManager userSession;
    String userLoginId, userCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userSession = new UserSessionManager(getApplicationContext());

        HashMap<String, String> user = userSession.getUserDetails();
        Global.USERID = Integer.valueOf(user.get(UserSessionManager.KEY_USERID));
        Global.USERTOKEN = user.get(UserSessionManager.KEY_TOKEN);
        Global.USERLOGINID= user.get(UserSessionManager.KEY_USERLOGINID);
        Global.USERTYPE = user.get(UserSessionManager.KEY_USERTYPE);
        if (Global.USERLOGINID!= null && Global.USERTOKEN != null && Global.USERID > 0 && Global.USERTYPE != null) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            //setContentView(R.layout.activity_home_page);
        }
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        volleyManager = VolleyManager.getInstance(getApplicationContext(), this);
        findElementFromView();
        //txtUserCode.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
        setFontOnElement(custom_font, custom_font1);
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
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, Register.class);
                startActivity(it);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommonFunctions.isInternetOn(getApplicationContext()) == true) {
                    userLoginId = txtMobileNumber.getText().toString().trim();
                    userCode = txtUserCode.getText().toString().trim();
                    if (validateFields(userLoginId, userCode)) {
                        String REQUEST_TAG = "KAP_STRING";
                        progressDialog.setMessage("Logging you in...");
                        CommonFunctions.showDialog(progressDialog);
                        // Posting params to register url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("UserId", userLoginId);
                        params.put("UserCode", userCode);

                        volleyManager.volleyJsonObjectPostRequest(Urls.LOGIN_URL, new JSONObject(params));

                    } else {
                        Toast.makeText(getApplicationContext(), "Some Error in Login", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Some Error in Login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private boolean validateFields(String mobileNumber, String userCode)
        {
        boolean valid = true;
        if (userCode.isEmpty())
        {
            txtUserCode.setError("Password is empty");
            requestFocus(txtUserCode);
            valid = false;
        }
        else {
            txtUserCode.setError(null);
        }
        if (mobileNumber.isEmpty())
        {
            txtMobileNumber.setError("Login Id or Mobile number is empty");
            requestFocus(txtMobileNumber);
            valid = false;
        } else {
            if(mobileNumber.length()!=10){
                txtMobileNumber.setError("enter a valid login id or mobile number");
                requestFocus(txtMobileNumber);
                valid = false;
            }
            else {
                txtMobileNumber.setError(null);
            }
        }
        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void setFontOnElement(Typeface custom_font, Typeface custom_font1) {
       // btn_login.setTypeface(custom_font1);
        txtUserCode.setTypeface(custom_font);
        txtMobileNumber.setTypeface(custom_font);
    }

    private void findElementFromView() {
        btn_login = (TextView) findViewById(R.id.btn_login);
        register = (TextView) findViewById(R.id.btn_register);
        txtUserCode = (EditText) findViewById(R.id.userCode);
        txtMobileNumber = (EditText) findViewById(R.id.email);
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
                if (responseObj.getIsSuccess() == 1) {
                    if (responseObj.getData().getId() > 0) {
                        Global.USER = responseObj.getData();
                        Global.USERID = Global.USER.getId();
                        Global.USERLOGINID = userLoginId;
                        Global.USERTOKEN = Global.USER.getToken();
                        Global.USERTYPE = Global.USER.getUserType();

                        userSession.createUserLoginSession(Global.USERTOKEN, Global.USERLOGINID, String.valueOf(Global.USERID), Global.USERTYPE);

                        Intent it = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(it);
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),responseObj.getMessage() , Toast.LENGTH_SHORT).show();
                }
            }
       }
        catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Some Error in Login", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseError() {
        CommonFunctions.hideDialog(progressDialog);
        Toast.makeText(getApplicationContext(), "Some Error in Login", Toast.LENGTH_SHORT).show();
    }
}