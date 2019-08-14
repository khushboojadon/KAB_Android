package com.appflowsolutions.kab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.appflowsolutions.kab.Models.HospitalModel;
import com.appflowsolutions.kab.R;
import com.appflowsolutions.kab.Utils.Global;

import java.util.List;


public class HospitalSpinnerAdapter extends BaseAdapter {
    Context context;
    List<HospitalModel> hospitals;
    LayoutInflater inflter;

    public HospitalSpinnerAdapter(Context applicationContext, List<HospitalModel> hospitals) {
        this.context = applicationContext;
        this.hospitals= hospitals;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return hospitals.size();
    }

    @Override
    public Object getItem(int i) {
        return hospitals.get(i);
    }

    @Override
    public long getItemId(int i) {
        return hospitals.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        view = inflter.inflate(R.layout.hospital_layout, null);
        TextView name = (TextView) view.findViewById(R.id.tvName);
        TextView mobile = (TextView) view.findViewById(R.id.tvMobile);
        TextView address = (TextView) view.findViewById(R.id.tvAddress);
        if(Global.HOSPITALS.get(i).getPhone()!=""&& Global.HOSPITALS.get(i).getPhone()!=null && Global.HOSPITALS.get(i).getMobile()!=""&& Global.HOSPITALS.get(i).getMobile()!=null)
        {
            mobile.setText(Global.HOSPITALS.get(i).getPhone()+", "+Global.HOSPITALS.get(i).getMobile());
        }
        else if (Global.HOSPITALS.get(i).getPhone()!=""&& Global.HOSPITALS.get(i).getPhone()!=null)
        {
            mobile.setText(Global.HOSPITALS.get(i).getPhone());
        }
        else if (Global.HOSPITALS.get(i).getMobile()!=""&& Global.HOSPITALS.get(i).getMobile()!=null){
            mobile.setText(Global.HOSPITALS.get(i).getMobile());
        }
        name.setText(hospitals.get(i).getFirstName());
        address.setText(hospitals.get(i).getAddress());
        return view;
    }
}

