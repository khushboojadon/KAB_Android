package com.appflowsolutions.kab.Activities;

import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.appflowsolutions.kab.Interfaces.IVolleyResponseManager;
import com.appflowsolutions.kab.Models.*;
import com.appflowsolutions.kab.R;
import com.appflowsolutions.kab.Utils.*;
import com.appflowsolutions.kab.Utils.VolleyManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class ContactProfile extends AppCompatActivity {
    ContactModel contactModel;
    TextView contactName,contactMobileNumber,contactAddress;
    FloatingActionButton editContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);

        //Add back button
        /*getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        contactModel= (ContactModel)i.getSerializableExtra(Global.CONTACTS_OBJECT_TAG);
        findElementFromView();
        if(contactModel!=null)
        {
            contactName.setText(contactModel.getFirstName()+" "+contactModel.getLastName() );
            contactAddress.setText(contactModel.getAddress());
            contactMobileNumber.setText(contactModel.getMobile());
        }

        editContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ContactProfile.this, EditContacts.class);
                it.putExtra(Global.EDIT_CONTACTS_OBJECT_TAG, contactModel);
                startActivity(it);
            }
        });*/
    }

   /* private void findElementFromView() {
        contactAddress= (TextView) findViewById(R.id.chp_hospitalAddress);
        contactName= (TextView) findViewById(R.id.cp_contactName);
        contactMobileNumber= (TextView) findViewById(R.id.chp_hospitalMobileNumber);
        editContact= (FloatingActionButton) findViewById(R.id.editContact);
    }*/
   // @Override
   /* public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id== android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }*/
}
