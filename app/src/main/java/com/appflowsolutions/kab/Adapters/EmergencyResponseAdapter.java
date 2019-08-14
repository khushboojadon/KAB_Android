package com.appflowsolutions.kab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appflowsolutions.kab.R;
import com.appflowsolutions.kab.Utils.Global;


public class EmergencyResponseAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;

    public EmergencyResponseAdapter(Context applicationContext) {
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount()
    {
        return Global.EMERGENCY_REQUESTS.size();
    }

    @Override
    public Object getItem(int i) {
        return Global.EMERGENCY_REQUESTS.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Global.EMERGENCY_REQUESTS.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        view = inflter.inflate(R.layout.help_request_layout, null);
        TextView name = (TextView) view.findViewById(R.id.need_userName);
        TextView mobile = (TextView) view.findViewById(R.id.need_mobileno);
        TextView address = (TextView) view.findViewById(R.id.need_Address);
        TextView status= (TextView) view.findViewById(R.id.need_status);
        TextView message= (TextView) view.findViewById(R.id.need_Message);
        message.setText(Global.EMERGENCY_REQUESTS.get(i).getRequestMessage());
        mobile.setText(Global.EMERGENCY_REQUESTS.get(i).getMobile());
        name.setText(Global.EMERGENCY_REQUESTS.get(i).getFirstName() + " "+Global.EMERGENCY_REQUESTS.get(i).getLastName());
        address.setText(Global.EMERGENCY_REQUESTS.get(i).getAddress());
        if(Global.EMERGENCY_REQUESTS.get(i).getRequestStatus().equalsIgnoreCase("CREATED"))
        {
                status.setBackground( context.getResources().getDrawable(R.drawable.roundcornerbuttonred));
        }
        else if(Global.EMERGENCY_REQUESTS.get(i).getRequestStatus().equalsIgnoreCase("PROGRESSED"))
        {
            status.setBackground( context.getResources().getDrawable(R.drawable.roundcornerbuttonyellow));
        }
        else
        if(Global.EMERGENCY_REQUESTS.get(i).getRequestStatus().equalsIgnoreCase("RESOLVED"))
        {
            status.setBackground(context.getResources().getDrawable(R.drawable.roundcornerbuttongreen));
        }

        return view;
    }
}
