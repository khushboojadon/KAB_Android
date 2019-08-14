package com.appflowsolutions.kab.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditUserProfile extends AppCompatActivity implements IVolleyResponseManager {

    TextView btnUpdateProfile;
    String currentRequest="";
    TextView btnCancelProfile;
    EditText txtFirstName,txtLastName,txtMobileNumber,txtAddress,txtPincode,
            firstContact_txtFirstName,firstContact_txtLastName,firstContact_txtMobileno,firstContact_txtAddress,
            secondContact_txtFirstName,secondContact_txtLastName,secondContact_txtMobileno,secondContact_txtAddress,txtFDoctorName,txtFDoctorPhoneNumber,txtFDoctorAddress;

    Spinner spinnerNearByHospital;

    String selectedHospitalId ="";
    VolleyManager volleyManager;
    HospitalSpinnerAdapter adapter;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        //Add back button
          ActionBar j=   getSupportActionBar();
          if(j != null) {
              getSupportActionBar().setDisplayShowHomeEnabled(true);
          }
        volleyManager = VolleyManager.getInstance(getApplicationContext(), this);
        findElementFromView();
        setOnClick();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        CommonFunctions.showDialog(progressDialog);
        currentRequest="FechingHospital";
        String url=Urls.HOSPITAL_URL+"0";
        volleyManager.volleyJsonObjectRequest(url);
        if(Global.HOSPITALS==null){
            Global.HOSPITALS = new ArrayList<>();
        }

        spinnerNearByHospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedHospitalId=Global.HOSPITALS.get(position).getId().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(Global.USER!=null)
        {
            txtFirstName.setText(Global.USER.getFirstName());
            txtLastName.setText(Global.USER.getLastName());
            txtAddress.setText(Global.USER.getAddress());
            txtMobileNumber.setText((Global.USER.getMobile()==null)?"":Global.USER.getMobile().toString());
            txtPincode.setText(Global.USER.getAreaPinCode());
            txtFDoctorName.setText(Global.USER.getfDoctorName());
            txtFDoctorPhoneNumber.setText((Global.USER.getfDoctorPhoneNumber()==null)?"":Global.USER.getfDoctorPhoneNumber().toString());
            txtFDoctorAddress.setText(Global.USER.getfDoctorAddress());
        }
        if(Global.USER.getFirstContact()!=null) {
            firstContact_txtFirstName.setText(Global.USER.getFirstContact().getFirstName());
            firstContact_txtLastName.setText(Global.USER.getFirstContact().getLastName());
            firstContact_txtMobileno.setText(Global.USER.getFirstContact().getMobile());
            firstContact_txtAddress.setText(Global.USER.getFirstContact().getAddress());
        }
        if(Global.USER.getSecondContact()!=null) {
            secondContact_txtFirstName.setText(Global.USER.getSecondContact().getFirstName());
            secondContact_txtLastName.setText(Global.USER.getSecondContact().getLastName());
            secondContact_txtMobileno.setText(Global.USER.getSecondContact().getMobile());
            secondContact_txtAddress.setText(Global.USER.getSecondContact().getAddress());
        }
    }
    @Override
    protected void onResume() {
        volleyManager = VolleyManager.getInstance(getApplicationContext(), this);
        super.onResume();
    }

    private void setOnClick()
    {
        btnCancelProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(EditUserProfile.this, MainActivity.class);
                startActivity(it);
            }
        });
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRequest="updateProfile";
                String firstName=txtFirstName.getText().toString().trim();
                String lastName=txtLastName.getText().toString().trim();
                String mobileNumber=txtMobileNumber.getText().toString().trim();
                String address=txtAddress.getText().toString().trim();
                String pincode=txtPincode.getText().toString().trim();

                String FDoctorName=txtFDoctorName.getText().toString().trim();
                String FDoctorPhoneNumber=txtFDoctorPhoneNumber.getText().toString().trim();
                String FDoctorAddress=txtFDoctorAddress.getText().toString().trim();
                String firstContactfirstName=firstContact_txtFirstName.getText().toString().trim();
                String firstContactlastName=firstContact_txtLastName.getText().toString().trim();
                String firstContactmobileNumber=firstContact_txtMobileno.getText().toString().trim();
                String firstContactaddress=firstContact_txtAddress.getText().toString().trim();

                String secondContactfirstName=secondContact_txtFirstName.getText().toString().trim();
                String secondContactlastName=secondContact_txtLastName.getText().toString().trim();
                String secondContactmobileNumber=secondContact_txtMobileno.getText().toString().trim();
                String secondContactaddress=secondContact_txtAddress.getText().toString().trim();

                if(validateFields(address,firstName,lastName,mobileNumber,pincode,firstContactaddress,firstContactfirstName,firstContactlastName,firstContactmobileNumber,secondContactaddress,secondContactfirstName,secondContactlastName,secondContactmobileNumber,selectedHospitalId,FDoctorName,FDoctorPhoneNumber,FDoctorAddress))
                {
                    progressDialog.setMessage("Updating Profile...");
                    CommonFunctions.showDialog(progressDialog);
                    Map<String, Object> params = new HashMap<String, Object>();
                    Map<String, String> params1 = new HashMap<String, String>();
                    Map<String, String> params2 = new HashMap<String, String>();

                    params1.put("Address", firstContactaddress);
                    params1.put("FirstName", firstContactfirstName);
                    params1.put("LastName", firstContactlastName);
                    params1.put("Mobile", firstContactmobileNumber);

                    params2.put("Address", secondContactaddress);
                    params2.put("FirstName", secondContactfirstName);
                    params2.put("LastName", secondContactlastName);
                    params2.put("Mobile", secondContactmobileNumber);

                    params.put("Address", address);
                    params.put("FirstName", firstName);
                    params.put("LastName", lastName);
                    params.put("Mobile", mobileNumber);
                    params.put("FDoctorName", FDoctorName);
                    params.put("FDoctorPhoneNumber", FDoctorPhoneNumber);
                    params.put("FDoctorAddress", FDoctorAddress);
                    params.put("UserId", Global.USERID.toString());
                    params.put("NearByHospital", selectedHospitalId);
                    params.put("AreaPinCode", pincode);
                    params.put("FirstContact", params1);
                    params.put("SecondContact", params2);


                    if(CommonFunctions.isInternetOn(getApplicationContext())){
                        volleyManager.volleyJsonObjectPatchRequest(Urls.UPDATE_USERPROFILE_URL+ Global.USERID.toString(),new JSONObject(params));
                    }else{
                        Toast.makeText(getApplicationContext(),"No Internet Available", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    private void findElementFromView()
    {
        txtAddress= (EditText) findViewById(R.id.update_address);
        txtFirstName= (EditText) findViewById(R.id.update_first_name);
        txtLastName= (EditText) findViewById(R.id.update_last_name);
        txtMobileNumber= (EditText) findViewById(R.id.update_mobphone);
        txtPincode= (EditText) findViewById(R.id.update_pincode);

        firstContact_txtAddress= (EditText) findViewById(R.id.first_contact_address);
        firstContact_txtFirstName= (EditText) findViewById(R.id.first_contact_name);
        firstContact_txtLastName= (EditText) findViewById(R.id.first_contact_last_name);
        firstContact_txtMobileno= (EditText) findViewById(R.id.first_contact_mobphone);

        secondContact_txtAddress= (EditText) findViewById(R.id.second_contact_address);
        secondContact_txtFirstName= (EditText) findViewById(R.id.second_contact_name);
        secondContact_txtLastName= (EditText) findViewById(R.id.second_contact_last_name);
        secondContact_txtMobileno= (EditText) findViewById(R.id.second_contact_mobphone);

        spinnerNearByHospital=(Spinner)findViewById(R.id.update_nearBy_hospital);
        btnUpdateProfile = (TextView) findViewById(R.id.btn_update_Profile);
        btnCancelProfile=(TextView) findViewById(R.id.btn_cancel_profile);
        txtFDoctorAddress=(EditText) findViewById(R.id.family_dr_address);
        txtFDoctorName=(EditText) findViewById(R.id.family_dr_name);
        txtFDoctorPhoneNumber=(EditText) findViewById(R.id.family_dr_mobphone);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id== android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean validateFields(String address, String firstName, String lastName, String mobileNumber, String pincode, String firstContactaddress, String firstContactfirstName, String firstContactlastName, String firstContactmobileNumber, String secondContactaddress, String secondContactfirstName, String secondContactlastName, String secondContactmobileNumber, String selectedHospitalId,String FDoctorName,String  FDoctorPhoneNumber,String  FDoctorAddress) {
        boolean valid = true;
        if(selectedHospitalId=="0" || selectedHospitalId.isEmpty())
        {
            valid =false;
            Toast.makeText(getApplicationContext(),"Please Select near by hospital", Toast.LENGTH_LONG).show();
        }

        if (FDoctorAddress.isEmpty()) {
            txtFDoctorAddress.setError("Family Doctor's address is empty");
            requestFocus(txtFDoctorAddress);
            valid = false;
        } else {
            txtFDoctorAddress.setError(null);
        }

        if (FDoctorPhoneNumber.isEmpty()) {
            txtFDoctorPhoneNumber.setError("Family Doctor's mobile number is empty");
            requestFocus(txtFDoctorPhoneNumber);
            valid = false;
        } else {
            if(FDoctorPhoneNumber.length()!=10){
                txtFDoctorPhoneNumber.setError("enter a valid mobile number");
                requestFocus(txtFDoctorPhoneNumber);
                valid = false;
            }
            else {
                txtFDoctorPhoneNumber.setError(null);
            }
        }

        if (FDoctorName.isEmpty()) {
            txtFDoctorName.setError("Family Doctor's name is empty");
            requestFocus(txtFDoctorName);
            valid = false;
        } else {
            txtFDoctorName.setError(null);
        }


        if (secondContactaddress.isEmpty()) {
            secondContact_txtAddress.setError("Second Contact's address is empty");
            requestFocus(secondContact_txtAddress);
            valid = false;
        } else {
            secondContact_txtAddress.setError(null);
        }

        if (secondContactmobileNumber.isEmpty()) {
            secondContact_txtMobileno.setError("Second Contact's mobile number is empty");
            requestFocus(secondContact_txtMobileno);
            valid = false;
        } else {
            if(secondContactmobileNumber.length()!=10){
                secondContact_txtMobileno.setError("enter a valid mobile number");
                requestFocus(secondContact_txtMobileno);
                valid = false;
            }
            else if(secondContactmobileNumber.equalsIgnoreCase(firstContactmobileNumber)){
                secondContact_txtMobileno.setError("Second contact and first contact number is same");
                requestFocus(secondContact_txtMobileno);
                valid = false;
            }
            else {
                secondContact_txtMobileno.setError(null);
            }
        }

        if (secondContactlastName.isEmpty()) {
            secondContact_txtLastName.setError("Second Contact's last name is empty");
            requestFocus(secondContact_txtLastName);
            valid = false;
        } else {
            secondContact_txtLastName.setError(null);
        }

        if (secondContactfirstName.isEmpty()) {
            secondContact_txtFirstName.setError("Second Contact's first name is empty");
            requestFocus(secondContact_txtFirstName);
            valid = false;
        } else {
            secondContact_txtFirstName.setError(null);
        }

        if (firstContactaddress.isEmpty()) {
            firstContact_txtAddress.setError("first Contact's address is empty");
            requestFocus(firstContact_txtAddress);
            valid = false;
        } else {
            firstContact_txtAddress.setError(null);
        }

        if (firstContactmobileNumber.isEmpty()) {
            firstContact_txtMobileno.setError("first Contact's mobile number is empty");
            requestFocus(firstContact_txtMobileno);
            valid = false;
        } else {
            if(firstContactmobileNumber.length()!=10){
                firstContact_txtMobileno.setError("enter a valid mobile number");
                requestFocus(firstContact_txtMobileno);
                valid = false;
            }
            else {
                firstContact_txtMobileno.setError(null);
            }
        }

        if (firstContactlastName.isEmpty()) {
            firstContact_txtLastName.setError("first Contact's last name is empty");
            requestFocus(firstContact_txtLastName);
            valid = false;
        } else {
            firstContact_txtLastName.setError(null);
        }

        if (firstContactfirstName.isEmpty()) {
            firstContact_txtFirstName.setError("first Contact's first name is empty");
            requestFocus(firstContact_txtFirstName);
            valid = false;
        } else {
            firstContact_txtFirstName.setError(null);
        }


        if (pincode.isEmpty()) {
            txtPincode.setError("Pincode is empty");
            requestFocus(txtPincode);
            valid = false;
        } else {
            txtPincode.setError(null);
        }

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
        if(currentRequest.equalsIgnoreCase("FechingHospital"))
        {
            try {
                if (response != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResponseModel<List<HospitalModel>>>() {
                    }.getType();
                    ResponseModel<List<HospitalModel>> responseObj = gson.fromJson(response, type);
                    if (responseObj.getIsSuccess() == 1)
                    {
                        Global.HOSPITALS  = responseObj.getData();
                        adapter = new HospitalSpinnerAdapter(getApplicationContext(),Global.HOSPITALS);
                        spinnerNearByHospital.setAdapter(adapter);


                    }
                    else
                    {
                        Toast.makeText(getApplicationContext().getApplicationContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Some Error in updating contact", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "Some Error in updating contact", Toast.LENGTH_SHORT).show();
            }
        }else {
            try {
                if (response != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ResponseModel<UserModel>>() {
                    }.getType();
                    ResponseModel<UserModel> responseObj = gson.fromJson(response, type);
                    if (responseObj.getIsSuccess() == 1) {
                        Global.USER = responseObj.getData();
                        Intent i= new Intent(EditUserProfile.this,MainActivity.class);
                        startActivity(i);
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
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onResponseError() {
        CommonFunctions.hideDialog(progressDialog);
        Toast.makeText(getApplicationContext(), "Some Error in updating contact", Toast.LENGTH_SHORT).show();




    }
}
