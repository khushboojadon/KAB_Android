package com.appflowsolutions.kab.Fragments;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AwayUserFragment extends Fragment implements IVolleyResponseManager {
    VolleyManager volleyManager;
    ProgressDialog progressDialog;

    PopupWindow pw;
    TextView startDate, endDate;
    int position=-1;
    Calendar now;
    CheckBox checkBox;
    DatePickerDialog startDatePickerDialog;
    DatePickerDialog endDatePickerDialog;
    String requestStatus="";
    TextView btnSetAway;
    AwayUserFragment awayUserFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_set_away, container, false);
        awayUserFragment =this;
        startDate = (TextView) view.findViewById(R.id.startDate);
        checkBox = (CheckBox) view.findViewById(R.id.ChkAwayFromHome);
        endDate= (TextView) view.findViewById(R.id.endDate);
        btnSetAway = (TextView) view.findViewById(R.id.btn_Set_Away);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                   // btnSetAway.setBackgroundColor(getActivity().getColor(R.color.colorPrimary));
                    btnSetAway.setBackgroundDrawable(getActivity().getDrawable(R.drawable.button_border));
                    startDate.setEnabled(true);
                    endDate.setEnabled(true);
                }else
                {
                    btnSetAway.setBackgroundDrawable(getActivity().getDrawable(R.drawable.set_away_inactive_btn));
                   // btnSetAway.setBackgroundColor(getActivity().getColor(R.color.btnDisable));
                    startDate.setEnabled(false);
                    endDate.setEnabled(false);
                }
            }
        });

        Calendar startDateNow = Calendar.getInstance();
        startDatePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String date =  dayOfMonth + "/" + (++monthOfYear) + "/" + year;
                        startDate.setText(date);
                    }
                },
                startDateNow.get(Calendar.YEAR),
                startDateNow.get(Calendar.MONTH),
                startDateNow.get(Calendar.DAY_OF_MONTH)
        );
        startDatePickerDialog.setThemeDark(true); //set dark them for dialog?
        startDatePickerDialog.vibrate(true); //vibrate on choosing date?
        startDatePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
        startDatePickerDialog.showYearPickerFirst(false); //choose year first?
        startDatePickerDialog.setAccentColor(Color.parseColor("#9C27A0")); // custom accent color
        startDatePickerDialog.setTitle("Please select a date"); //dialog title
        String n= String.valueOf(startDateNow.get(Calendar.DATE)) +"/"+ String.valueOf(startDateNow.get(Calendar.MONTH)+1)+"/"+ String.valueOf(startDateNow.get(Calendar.YEAR));
        startDate.setText(n);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDatePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog1"); //show dialog
            }
        });






        Calendar endDateNow = Calendar.getInstance();
        endDatePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String date =  dayOfMonth + "/" + (++monthOfYear) + "/" + year;
                        endDate.setText(date);
                    }
                },
                endDateNow.get(Calendar.YEAR),
                endDateNow.get(Calendar.MONTH),
                endDateNow.get(Calendar.DAY_OF_MONTH)
        );
        endDatePickerDialog.setThemeDark(true); //set dark them for dialog?
        endDatePickerDialog.vibrate(true); //vibrate on choosing date?
        endDatePickerDialog.dismissOnPause(true); //dismiss dialog when onPause() called?
        endDatePickerDialog.showYearPickerFirst(false); //choose year first?
        endDatePickerDialog.setAccentColor(Color.parseColor("#9C27A0")); // custom accent color
        endDatePickerDialog.setTitle("Please select a date"); //dialog title
        String n1= String.valueOf(endDateNow.get(Calendar.DATE)) +"/"+ String.valueOf(endDateNow.get(Calendar.MONTH)+1)+"/"+ String.valueOf(endDateNow.get(Calendar.YEAR));
        endDate.setText(n1);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                endDatePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog2"); //show dialog
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Request...");

        volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), awayUserFragment);

        btnSetAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), awayUserFragment);
                String startDates= startDate.getText().toString().trim();
                String endDates= endDate.getText().toString().trim();
                boolean checkBoxs= checkBox.isChecked();
                if(checkBoxs) {
                    if (validateFields(startDates, endDates)) {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Id", String.valueOf(Global.USERID));
                        params.put("From", startDates);
                        params.put("TO", endDates);
                        params.put("Away", String.valueOf(checkBoxs));
                        progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setCancelable(false);
                        volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), awayUserFragment);
                        progressDialog.setMessage("Processing");
                        if (CommonFunctions.isInternetOn(getContext())) {
                            CommonFunctions.showDialog(progressDialog);
                            volleyManager.volleyJsonObjectPostRequest(Urls.AWAY_FROM_HOME_URL, new JSONObject(params));
                        } else {
                            Toast.makeText(getContext(), "No Internet Available", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
        return view;
    }
    private boolean validateFields(String sd, String ed) {

        boolean valid = true;
        if (sd.isEmpty()) {
            Toast.makeText(getContext(),"Start date is empty", Toast.LENGTH_LONG).show();
            requestFocus(startDate);
            valid = false;
        } else {
            startDate.setError(null);
        }

        if (ed.isEmpty()) {
            Toast.makeText(getContext(),"End date is empty", Toast.LENGTH_LONG).show();
            requestFocus(endDate);
            valid = false;
        } else
            {
            endDate.setError(null);
        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");


        try {
            Date date1 = format.parse(sd);
            Date date2 = format.parse(ed);

            if (date1.compareTo(date2) <= 0)
            {

            }
            else
            {
                Toast.makeText(getContext(),"Please select date after start date", Toast.LENGTH_LONG).show();
                requestFocus(endDate);
                valid= false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return valid;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void onResume()
    {
        volleyManager = VolleyManager.getInstance(getActivity().getApplicationContext(), this);
        super.onResume();
    }

    @Override
    public void onResponseSuccess(String response)
    {

        CommonFunctions.hideDialog(progressDialog);
        try {
            if (response != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<ResponseModel<AwayModel>>() {
                }.getType();
                ResponseModel<AwayModel> responseObj = gson.fromJson(response, type);
                if (responseObj.getIsSuccess() == 1)
                {
                    Toast.makeText(getContext(),"Set Away Successful", Toast.LENGTH_LONG).show();
                    checkBox.setChecked(false);
                    btnSetAway.setBackgroundColor(getActivity().getColor(R.color.btnDisable));
                    startDate.setEnabled(false);
                    endDate.setEnabled(false);

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), responseObj.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Error in getting help request", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Error in getting help request", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseError()
    {
        CommonFunctions.hideDialog(progressDialog);
        Toast.makeText(getActivity().getApplicationContext(), "Error in getting help request", Toast.LENGTH_LONG).show();

    }
}
