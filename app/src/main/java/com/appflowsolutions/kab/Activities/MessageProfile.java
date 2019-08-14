package com.appflowsolutions.kab.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appflowsolutions.kab.Interfaces.IVolleyResponseManager;
import com.appflowsolutions.kab.Models.*;
import com.appflowsolutions.kab.R;
import com.appflowsolutions.kab.Utils.*;
import com.appflowsolutions.kab.Utils.VolleyManager;

public class MessageProfile extends AppCompatActivity {

    MessageTemplateModel messageTemplateModel;
    TextView messageTitle,messageDescription,contactName,contactNumber,contactAddress;
    FloatingActionButton btnEditMessage;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        //Add back button
       getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent i = getIntent();
        messageTemplateModel= (MessageTemplateModel)i.getSerializableExtra(Global.EDIT_MESSAGE_OBJECT_TAG);

        findElementFromView();
        btnEditMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MessageProfile.this, EditMessage.class);
                it.putExtra(Global.EDIT_MESSAGE_OBJECT_TAG, messageTemplateModel);
                startActivity(it);
            }
        });
        if(messageTemplateModel!=null)
        {
            messageTitle.setText(messageTemplateModel.getTitle());
            messageDescription.setText(messageTemplateModel.getMessage());
            ContactModel contactModel =null;
            for(int j=0;j<Global.CONTACTS.size();j++)
            {
                if(messageTemplateModel.getContactId()!=null) {
                    if (Global.CONTACTS.get(j).getId().toString().equals(messageTemplateModel.getContactId().toString())) {
                        contactModel = Global.CONTACTS.get(j);
                        break;
                    }
                }
            }
            if(contactModel!=null)
            {
                contactName.setText(contactModel.getFirstName()+" "+contactModel.getLastName());
                contactNumber.setText(contactModel.getMobile());
                contactAddress.setText(contactModel.getAddress());

            }
        }
    }

    private void findElementFromView()
    {
        contactAddress= (TextView) findViewById(R.id.m_address);
        contactName= (TextView) findViewById(R.id.m_contactname);
        contactNumber= (TextView) findViewById(R.id.m_contact);
        messageTitle= (TextView) findViewById(R.id.m_messagetitle);
        messageDescription= (TextView) findViewById(R.id.m_description);
        btnEditMessage= (FloatingActionButton)findViewById(R.id.editmessage);
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
