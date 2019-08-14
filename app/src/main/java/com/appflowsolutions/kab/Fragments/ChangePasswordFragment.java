package com.appflowsolutions.kab.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
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

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ChangePasswordFragment extends Fragment implements IVolleyResponseManager {

    VolleyManager volleyManager;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeContainer;
    EditText oldpassCode, newPasscode, confirmpasscode;
    TextView btnChangePassword;
    ChangePasswordFragment changePasswordFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.change_password_fragment, container, false);
        changePasswordFragment=this;
        oldpassCode= (EditText) view.findViewById(R.id.old_password);
        newPasscode= (EditText) view.findViewById(R.id.new_passcode);
        confirmpasscode= (EditText) view.findViewById(R.id.confirm_passcode);
        btnChangePassword = (TextView) view.findViewById(R.id.btn_Change_Password);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpass= oldpassCode.getText().toString().trim();
                String newpass= newPasscode.getText().toString().trim();
                String confirmpass= confirmpasscode.getText().toString().trim();
                if(validateFields(oldpass,newpass,confirmpass)){

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Id", String.valueOf(Global.USERID));
                    params.put("OldPassCode", oldpass);
                    params.put("NewPassCode", newpass);
                    params.put("ConfirmPassCode", confirmpass);

                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setCancelable(false);
                    volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(),changePasswordFragment);
                    progressDialog.setMessage("Changing Passwors...");
                    if(CommonFunctions.isInternetOn(getContext())){
                        CommonFunctions.showDialog(progressDialog);
                        volleyManager.volleyJsonObjectPostRequest(Urls.CHANGEPASSWORD_URL,new JSONObject(params));
                    }else{
                        Toast.makeText(getContext(),"No Internet Available", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        return view;
    }
    @Override
    public void onResume() {
        volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), this);
        super.onResume();

    }


    private boolean validateFields(String oldPassword, String newPassword, String confirmPassword) {

        boolean valid = true;
        if (confirmPassword.isEmpty()) {
            confirmpasscode.setError("Confirm pass code is empty");
            requestFocus(confirmpasscode);
            valid = false;
        } else {
            confirmpasscode.setError(null);
        }

        if (newPassword.isEmpty()) {
            newPasscode.setError("New Pass code is empty");
            requestFocus(newPasscode);
            valid = false;
        } else {
            newPasscode.setError(null);
        }
        if (oldPassword.isEmpty()) {
            oldpassCode.setError("Old pass code is empty");
            requestFocus(oldpassCode);
            valid = false;
        } else {
            oldpassCode.setError(null);
        }

        if(!newPassword.equals(confirmPassword)){
            confirmpasscode.setError("Pass code and confirm pass code does not match");
            requestFocus(confirmpasscode);
            valid = false;
        }else
        {
            confirmpasscode.setError(null);
        }
        return valid;
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
            try
            {
                if (response != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResponseModel<String>>() {
                    }.getType();
                    ResponseModel<String> responseObj = gson.fromJson(response, type);
                    if (responseObj.getIsSuccess() == 1) {
                        Toast.makeText(getActivity().getApplicationContext(), "Pass Code Changed", Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).LoadFragment(R.id.nav_sos);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Error in changing password", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Toast.makeText(getActivity().getApplicationContext(), "Error in changing password", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onResponseError()
    {
        swipeContainer.setRefreshing(false);

        CommonFunctions.hideDialog(progressDialog);
        Toast.makeText(getActivity().getApplicationContext(), "Error in changing password", Toast.LENGTH_LONG).show();

    }
}
