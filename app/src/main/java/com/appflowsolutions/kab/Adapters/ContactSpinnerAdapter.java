package com.appflowsolutions.kab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.appflowsolutions.kab.Models.ContactModel;
import com.appflowsolutions.kab.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContactSpinnerAdapter extends BaseAdapter {
    Context context;
    List<ContactModel> contacts;
    LayoutInflater inflter;

    public ContactSpinnerAdapter(Context applicationContext, List<ContactModel> contacts) {
        this.context = applicationContext;
        this.contacts= contacts;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int i) {
        return contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return contacts.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.contact_layout, null);
        CircleImageView icon = (CircleImageView)view.findViewById(R.id.ivContact);
        TextView name = (TextView) view.findViewById(R.id.tvName);
        TextView mobile = (TextView) view.findViewById(R.id.tvMobile);
        icon.setImageResource(R.drawable.user);
        name.setText(contacts.get(i).getFirstName()+" "+contacts.get(i).getLastName());
        mobile.setText(contacts.get(i).getMobile());
        return view;
    }
}

