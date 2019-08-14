package com.appflowsolutions.kab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appflowsolutions.kab.R;
import com.appflowsolutions.kab.Utils.Global;


public class AwayAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;

    public AwayAdapter(Context applicationContext) {
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount()
    {
        return Global.AWAYREQUESTS.size();
    }

    @Override
    public Object getItem(int i) {
        return Global.AWAYREQUESTS.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Global.AWAYREQUESTS.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        view = inflter.inflate(R.layout.away_request_layout, null);
        TextView name = (TextView) view.findViewById(R.id.away_userName);
        TextView mobile = (TextView) view.findViewById(R.id.away_mobileno);
        TextView address = (TextView) view.findViewById(R.id.away_from);

        if(Global.AWAYREQUESTS.get(i).getMobile() != null) {
            mobile.setText(Global.AWAYREQUESTS.get(i).getMobile().toString());
        }


        name.setText(Global.AWAYREQUESTS.get(i).getFirstName());
        address.setText(Global.AWAYREQUESTS.get(i).getFrom()+" - "+Global.AWAYREQUESTS.get(i).getTO());
        return view;
    }
}
