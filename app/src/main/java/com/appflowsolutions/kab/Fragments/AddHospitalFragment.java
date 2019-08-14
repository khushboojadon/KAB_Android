package com.appflowsolutions.kab.Fragments;


import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.appflowsolutions.kab.Activities.*;
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

public class AddHospitalFragment extends Fragment implements IVolleyResponseManager {


    EditText txtFirstName, txtMobileNumber, txtUserCode,txtAddress,txtPinCode,txtUserId, txtPhone;
    TextView btn_register;
    VolleyManager volleyManager;
    String TAG = "Abc";
    ProgressDialog progressDialog;
    String mobileNumber;

    AddHospitalFragment addHospitalFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.add_hospital_fragment, container, false);
        addHospitalFragment =this;
        findElementFromView(view);
        setFontOnElement();
        setOnClick();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        txtUserId.setText("HOSP_");
        Selection.setSelection(txtUserId.getText(), txtUserId.getText().length());


        txtUserId.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("HOSP_")){
                    txtUserId.setText("HOSP_");
                    Selection.setSelection(txtUserId.getText(), txtUserId.getText().length());

                }

            }
        });
        return view;
    }
    private void setOnClick() {

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( CommonFunctions.isInternetOn(getContext())==true)
                {
                    String userCode = txtUserCode.getText().toString().trim();
                    String firstName = txtFirstName.getText().toString().trim();
                    String userId= txtUserId.getText().toString().trim();
                    String phone= txtPhone.getText().toString().trim();
                    mobileNumber = txtMobileNumber.getText().toString().trim();
                    String address = txtAddress.getText().toString().trim();
                    String pincode = txtPinCode.getText().toString().trim();

                    if (validateFields(userId,phone, firstName, mobileNumber, userCode, address, pincode)) {
                        String REQUEST_TAG = "KAP_STRING";
                        progressDialog.setMessage("Please wait...");
                        CommonFunctions.showDialog(progressDialog);
                        // Posting params to register url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("FirstName", firstName);
                        params.put("LastName", "");
                        params.put("UserId", userId);
                        params.put("Phone", phone);
                        params.put("UserCode", userCode);
                        params.put("Address", address);
                        params.put("AreaPinCode", pincode);
                        params.put("UserType", "HOSPITAL");
                        volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), addHospitalFragment);
                        volleyManager.volleyJsonObjectPostRequest(Urls.REGISTER_URL, new JSONObject(params));

                    } else {

                    }
                }else
                {
                    Toast.makeText(getContext(),"No Internet Connection", Toast.LENGTH_LONG).show();;
                }
            }
        });
    }

    private void setFontOnElement()
    {
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LatoLight.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LatoRegular.ttf");
        txtFirstName.setTypeface(custom_font);
        txtUserCode.setTypeface(custom_font);
        txtUserId.setTypeface(custom_font);
        txtPhone.setTypeface(custom_font);
        txtMobileNumber.setTypeface(custom_font);
        txtAddress.setTypeface(custom_font);
        txtPinCode.setTypeface(custom_font);
        btn_register.setTypeface(custom_font1);
    }

    private void findElementFromView(View view) {

        btn_register = (TextView) view.findViewById(R.id.btn_register);
        txtPhone= (EditText) view.findViewById(R.id.phone);
        txtUserId= (EditText) view.findViewById(R.id.userId);
        txtFirstName = (EditText) view.findViewById(R.id.first_name);
        txtMobileNumber = (EditText) view.findViewById(R.id.mobphone);
        txtUserCode = (EditText) view.findViewById(R.id.userCode);
        txtAddress=(EditText) view.findViewById(R.id.r_address);
        txtPinCode=(EditText) view.findViewById(R.id.pincode);
    }

    private boolean validateFields(String userId, String phone, String firstName, String mobileNumber, String userCode, String address, String pincode) {

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
        if (phone.isEmpty()) {
            txtPhone.setError("Landline number is empty");
            requestFocus(txtPhone);
            valid = false;
        } else {
            txtPhone.setError(null);
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

        if (firstName.isEmpty()) {
            txtFirstName.setError("First name is empty");
            requestFocus(txtFirstName);
            valid = false;
        } else {
            txtFirstName.setError(null);
        }
        if (userId.equals("HOSP_")) {
            txtUserId.setError(" Please set hospital Id");
            requestFocus(txtUserId);
            valid = false;
        } else {
            if(userId.length()<10){
                txtUserId.setError("enter min 10 digit id including HOSP_");
                requestFocus(txtUserId);
                valid = false;
            }
            else {
                txtUserId.setError(null);
            }
        }

        return valid;
    }

    @Override
    public void onResume() {
        volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), this);
        super.onResume();

    }



    private void requestFocus(View view) {
        if (view.requestFocus()) {
           getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }




    @Override
    public void onResponseSuccess(String response)
    {
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
                        ((MainActivity)getActivity()).LoadFragment(R.id.nav_sos);
                    }
                }
                else
                {
                    Toast.makeText(getContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Error in adding hospital", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), "Error in adding hospital", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseError()
    {
        CommonFunctions.hideDialog(progressDialog);
        Toast.makeText(getActivity().getApplicationContext(), "Error in adding hospital", Toast.LENGTH_LONG).show();
    }
}
