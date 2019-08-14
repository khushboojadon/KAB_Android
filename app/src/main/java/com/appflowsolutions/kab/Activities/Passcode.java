package com.appflowsolutions.kab.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appflowsolutions.kab.Interfaces.IVolleyResponseManager;
import com.appflowsolutions.kab.Models.*;
import com.appflowsolutions.kab.R;
import com.appflowsolutions.kab.Utils.*;
import com.appflowsolutions.kab.Utils.VolleyManager;

public class Passcode extends AppCompatActivity {


    private EditText passcode,confirmPasscode;
    private TextView btn_save;
    String txtPasscode, txtConfirmPasscode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        //Add back button
        ActionBar j=   getSupportActionBar();
        if(j != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }




        passcode=(EditText)findViewById(R.id.passcode);
        confirmPasscode=(EditText)findViewById(R.id.confirm_passcode);

        // Only take numeric
        passcode.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        confirmPasscode.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        btn_save=(TextView)findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPasscode=passcode.getText().toString().trim();
                txtConfirmPasscode=confirmPasscode.getText().toString().trim();
                if(txtPasscode.isEmpty()){
                    Toast.makeText(Passcode.this,"Please enter passcode", Toast.LENGTH_SHORT).show();
                }
                else if(!txtConfirmPasscode.equals(txtPasscode)){
                    Toast.makeText(Passcode.this,"Confirm passcode doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });



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
