package com.appflowsolutions.kab.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appflowsolutions.kab.Interfaces.IVolleyResponseManager;
import com.appflowsolutions.kab.Models.*;
import com.appflowsolutions.kab.R;
import com.appflowsolutions.kab.Utils.*;
import com.appflowsolutions.kab.Utils.VolleyManager;

public class HospitalProfile extends AppCompatActivity {

    HospitalModel hospitalModel;
    TextView hospitalName,hospitalMobileNumber1,hospitalMobileNumber2,hospitalAddress;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_profile);

        //Add back button
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        Intent i = getIntent();
//        hospitalModel= (HospitalModel)i.getSerializableExtra(Global.HOSPITALS_OBJECT_TAG);
//
//        findElementFromView();
//        if(hospitalModel!=null)
//        {
//            hospitalName.setText(hospitalModel.getName());
//            hospitalAddress.setText(hospitalModel.getAddress());
//            hospitalMobileNumber1.setText(hospitalModel.getPhone1());
//
//            hospitalMobileNumber2.setText(hospitalModel.getPhone2());
//            if(hospitalModel.getPhone2()!=null && hospitalModel.getPhone2()!="")
//            {
//               // relativeLayout.setVisibility(View.GONE);
//            }
//        }
    }

    private void findElementFromView() {
        hospitalAddress= (TextView) findViewById(R.id.chp_hospitalAddress);
        hospitalName= (TextView) findViewById(R.id.hp_hospitalName);
        hospitalMobileNumber1= (TextView) findViewById(R.id.chp_hospitalMobileNumber1);
        hospitalMobileNumber2= (TextView) findViewById(R.id.chp_hospitalMobileNumber2);
        relativeLayout= (RelativeLayout) findViewById(R.id.phn2Layout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id== android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
