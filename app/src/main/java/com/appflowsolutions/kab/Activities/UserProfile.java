package com.appflowsolutions.kab.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appflowsolutions.kab.Adapters.HospitalSpinnerAdapter;
import com.appflowsolutions.kab.Interfaces.IVolleyResponseManager;
import com.appflowsolutions.kab.Models.*;
import com.appflowsolutions.kab.R;
import com.appflowsolutions.kab.Utils.*;
import com.appflowsolutions.kab.Utils.VolleyManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity implements IVolleyResponseManager {

    TextView txtFirstName, txtLastName, txtMobileNumber, txtAddress, txtPincode,
            firstContact_txtFirstName, firstContact_txtLastName, firstContact_txtMobileno, firstContact_txtAddress,
            secondContact_txtFirstName, secondContact_txtLastName, secondContact_txtMobileno, secondContact_txtAddress, txtFDoctorName, txtFDoctorPhoneNumber, txtFDoctorAddress, txtNearByHospital;
    FloatingActionButton editProfile;
    ProgressDialog progressDialog;
    VolleyManager volleyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        findElementFromView();
        //Add back button
        ActionBar j=   getSupportActionBar();
        if(j != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        volleyManager = VolleyManager.getInstance(getApplicationContext(), this);
        CommonFunctions.showDialog(progressDialog);
        String url = Urls.HOSPITAL_URL + "0";
        volleyManager.volleyJsonObjectRequest(url);
        if (Global.HOSPITALS == null) {
            Global.HOSPITALS = new ArrayList<>();
        }
        txtFirstName.setText(Global.USER.getFirstName());
        txtLastName.setText(Global.USER.getLastName());
        txtMobileNumber.setText((Global.USER.getMobile() == null) ? "" : Global.USER.getMobile().toString());
        txtAddress.setText(Global.USER.getAddress());
        txtPincode.setText(Global.USER.getAreaPinCode());

        txtFDoctorName.setText(Global.USER.getfDoctorName());
        txtFDoctorPhoneNumber.setText((Global.USER.getfDoctorPhoneNumber() == null) ? "" : Global.USER.getfDoctorPhoneNumber().toString());
        txtFDoctorAddress.setText(Global.USER.getfDoctorAddress());

        //txtNearByHospital.setText(Global.USER.getNearByHospital().);
        if (Global.USER.getFirstContact() != null) {
            firstContact_txtFirstName.setText(Global.USER.getFirstContact().getFirstName());
            firstContact_txtLastName.setText(Global.USER.getFirstContact().getLastName());
            firstContact_txtMobileno.setText(Global.USER.getFirstContact().getMobile());
            firstContact_txtAddress.setText(Global.USER.getFirstContact().getAddress());
        }
        if (Global.USER.getSecondContact() != null) {
            secondContact_txtFirstName.setText(Global.USER.getSecondContact().getFirstName());
            secondContact_txtLastName.setText(Global.USER.getSecondContact().getLastName());
            secondContact_txtMobileno.setText(Global.USER.getSecondContact().getMobile());
            secondContact_txtAddress.setText(Global.USER.getSecondContact().getAddress());
        }
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(UserProfile.this, EditUserProfile.class);
                startActivity(it);
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void findElementFromView() {
        txtAddress = (TextView) findViewById(R.id.profile_address);
        txtFirstName = (TextView) findViewById(R.id.profile_first_name);
        txtLastName = (TextView) findViewById(R.id.profile_last_name);
        txtMobileNumber = (TextView) findViewById(R.id.profile_mobphone);
        txtPincode = (TextView) findViewById(R.id.profile_pincode);
        txtFDoctorAddress = (TextView) findViewById(R.id.family_dr_address);
        txtFDoctorName = (TextView) findViewById(R.id.family_dr_name);
        txtFDoctorPhoneNumber = (TextView) findViewById(R.id.family_dr_mobphone);
        txtNearByHospital = (TextView) findViewById(R.id.nearby_hospital);

        firstContact_txtAddress = (TextView) findViewById(R.id.profile_first_contact_address);
        firstContact_txtFirstName = (TextView) findViewById(R.id.profile_first_contact_name);
        firstContact_txtLastName = (TextView) findViewById(R.id.profile_first_contact_last_name);
        firstContact_txtMobileno = (TextView) findViewById(R.id.profile_first_contact_mobphone);

        secondContact_txtAddress = (TextView) findViewById(R.id.profile_second_contact_address);
        secondContact_txtFirstName = (TextView) findViewById(R.id.profile_second_contact_name);
        secondContact_txtLastName = (TextView) findViewById(R.id.profile_second_contact_last_name);
        secondContact_txtMobileno = (TextView) findViewById(R.id.profile_second_contact_mobphone);
        editProfile = (FloatingActionButton) findViewById(R.id.fab_editProfile);
    }

    @Override
    public void onResponseSuccess(String response) {
        CommonFunctions.hideDialog(progressDialog);

        try {
            if (response != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResponseModel<List<HospitalModel>>>() {
                }.getType();
                ResponseModel<List<HospitalModel>> responseObj = gson.fromJson(response, type);
                if (responseObj.getIsSuccess() == 1) {
                    Global.HOSPITALS = responseObj.getData();

                    HospitalModel hospitalModel = null;
                    for (int i = 0; i < Global.HOSPITALS.size(); i++) {
                        int id = Global.USER.getNearByHospital();
                        if (Global.HOSPITALS.get(i).getId().toString().equalsIgnoreCase(String.valueOf(id))) {
                            hospitalModel=Global.HOSPITALS.get(i);
                            txtNearByHospital.setText(hospitalModel.getFirstName()+"\n"+hospitalModel.getAddress()+"\n"+hospitalModel.getMobile());
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext().getApplicationContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Some Error in updating contact", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Some Error in updating contact", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseError() {

    }
}
