package com.appflowsolutions.kab.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
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

public class HomeFragment extends Fragment implements IVolleyResponseManager {

    private static final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 182;
    Button btnSos,btnMedicalHelp;
    VolleyManager volleyManager;
    ProgressDialog progressDialog;
    HomeFragment homeFragment;
    PopupWindow pw;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.home_fragment_layout, container, false);
        btnSos = (Button) rootView.findViewById(R.id.btn_SosHelp);
        btnMedicalHelp = (Button) rootView.findViewById(R.id.btn_MedicalHelp);
        homeFragment=this;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        CommonFunctions.LoadFontAwesome(getActivity());
        btnMedicalHelp.setTypeface(Global.FontAwesome);
        btnMedicalHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindow(rootView ,"MEDICAL");
            }
        });

        btnSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePopupWindow(rootView,"SOS");
            }
        });
        return rootView;
    }

    private void initiatePopupWindow(View v, final String userType)
    {
        try
        {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            TextView cancelButton;
            if(userType.equalsIgnoreCase("SOS"))
            {
                View layout = inflater.inflate(R.layout.sos_popup, null);
                pw = new PopupWindow(layout, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
                pw.showAtLocation(v, Gravity.CENTER, 0, 0);
                cancelButton = (TextView) layout.findViewById(R.id.btn_cancel);

                final TextView houseTheft = (TextView) layout.findViewById(R.id.btn_houseTheft);
                final TextView fire = (TextView) layout.findViewById(R.id.btn_fire);
                final TextView Accident = (TextView) layout.findViewById(R.id.btn_Accident);
                final TextView domVolience = (TextView) layout.findViewById(R.id.btn_domVolience);
                final TextView other = (TextView) layout.findViewById(R.id.btn_other);

                houseTheft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendHelpRequest(userType, houseTheft.getText().toString());
                    }
                });
                fire.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendHelpRequest(userType, fire.getText().toString());
                    }
                });
                Accident.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendHelpRequest(userType, Accident.getText().toString());
                    }
                });
                domVolience.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendHelpRequest(userType, domVolience.getText().toString());
                    }
                });
                other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendHelpRequest(userType, other.getText().toString());
                    }
                });
            }
            else
            {
                View layout = inflater.inflate(R.layout.popup_medical, null);
                pw = new PopupWindow(layout,  WindowManager.LayoutParams.MATCH_PARENT,  WindowManager.LayoutParams.MATCH_PARENT, true);
                pw.showAtLocation(v, Gravity.CENTER, 0, 0);
                cancelButton = (TextView) layout.findViewById(R.id.btn_cancel);

                final TextView chestPain = (TextView) layout.findViewById(R.id.btn_chest_pain);
                final TextView uddenAccidentalFall = (TextView) layout.findViewById(R.id.btn_udden_accidental_fall);
                final TextView shortnessOfBreath = (TextView) layout.findViewById(R.id.btn_shortness_of_breath);
                final TextView urineProblem = (TextView) layout.findViewById(R.id.btn_urine_problem);
                final TextView beeding = (TextView) layout.findViewById(R.id.btn_beeding);
                final TextView roadSideAccident = (TextView) layout.findViewById(R.id.btn_road_side_accident);
                final TextView paralysisAttack = (TextView) layout.findViewById(R.id.btn_paralysis_attack);
                final TextView other = (TextView) layout.findViewById(R.id.btn_other);

                chestPain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendHelpRequest(userType, chestPain.getText().toString());
                    }
                });
                uddenAccidentalFall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendHelpRequest(userType, uddenAccidentalFall.getText().toString());
                    }
                });
                shortnessOfBreath.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendHelpRequest(userType, shortnessOfBreath.getText().toString());
                    }
                });

                urineProblem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendHelpRequest(userType, urineProblem.getText().toString());
                    }
                });

                beeding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendHelpRequest(userType, beeding.getText().toString());
                    }
                });

                roadSideAccident.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendHelpRequest(userType, roadSideAccident.getText().toString());
                    }
                });
                paralysisAttack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendHelpRequest(userType, paralysisAttack.getText().toString());
                    }
                });

                other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendHelpRequest(userType, other.getText().toString());
                    }
                });
            }
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendHelpRequest(String requestType, String requestMessage) {
        MainActivity mainActivity =((MainActivity)getActivity());
        mainActivity.locationTrack.getLocation();
        Double lat=  mainActivity.locationTrack.getLatitude();
        Double lng=  mainActivity.locationTrack.getLongitude();
        volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(),homeFragment);
        progressDialog.setMessage("Sending Request...");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("RequestType",requestType);
        params.put("Long", lng);
        params.put("RequestMessage", requestMessage);
        params.put("Lat", lat);
        if(requestType.equalsIgnoreCase("SOS")){
            params.put("HospitalId","0");
        }
        else
        {
            params.put("HospitalId",Global.USER.getNearByHospital());
        }
        params.put("RequestStatus","CREATED");
        params.put("UserId", Global.USERID.toString());
        if(CommonFunctions.isInternetOn(getContext())){
            CommonFunctions.showDialog(progressDialog);
            volleyManager.volleyJsonObjectPostRequest(Urls.HELPREQUEST_URL,new JSONObject(params));
        }else{
            Toast.makeText(getContext(),"No Internet Available", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onResponseSuccess(String response) {
        pw.dismiss();
        CommonFunctions.hideDialog(progressDialog);
        try
        {
            if (response != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResponseModel<String>>() {
                }.getType();
                ResponseModel<String> responseObj = gson.fromJson(response, type);
                if (responseObj.getIsSuccess() == 1) {
                    Toast.makeText(getActivity().getApplicationContext(), "Request Sent Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Some error please try again", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Some error please try again", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Some error please try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseError() {
        pw.dismiss();
        CommonFunctions.hideDialog(progressDialog);
    }
}
