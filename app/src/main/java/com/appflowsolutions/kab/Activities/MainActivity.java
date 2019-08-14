package com.appflowsolutions.kab.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appflowsolutions.kab.Fragments.*;
import com.appflowsolutions.kab.Interfaces.IVolleyResponseManager;
import com.appflowsolutions.kab.Models.*;
import com.appflowsolutions.kab.R;
import com.appflowsolutions.kab.Utils.*;
import com.appflowsolutions.kab.Utils.VolleyManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements IVolleyResponseManager {
    public enum actions {
        GetContacts,
        GetHospitals,
        GetMessages
    }
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList();
    private ArrayList<String> permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    public LocationTrack locationTrack;
    public actions currentAction = null;
    VolleyManager volleyManager;
    ProgressDialog progressDialog;
    TextView txtUsername;
    UserSessionManager userSession;
    Boolean isUserProfileCompleted = false;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        AskForPermissions();
        locationTrack = new LocationTrack(getApplicationContext(), this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                LoadFragment(item.getItemId());
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        userSession = new UserSessionManager(getApplicationContext());
        HashMap<String, String> user = userSession.getUserDetails();
        Global.USERID = Integer.valueOf(user.get(UserSessionManager.KEY_USERID));
        Global.USERTOKEN = user.get(UserSessionManager.KEY_TOKEN);
        Global.USERLOGINID = user.get(UserSessionManager.KEY_USERLOGINID);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        volleyManager = VolleyManager.getInstance(getApplicationContext(), this);

        Map<String, String> params = new HashMap<String, String>();
        params.put("Token", Global.USERTOKEN);
        params.put("Id", Global.USERID.toString());
        if (CommonFunctions.isInternetOn(getApplicationContext())) {
            CommonFunctions.showDialog(progressDialog);
            volleyManager.volleyJsonObjectPostRequest(Urls.VALIDATETOKEN_URL, new JSONObject(params));
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Available", Toast.LENGTH_LONG).show();
        }
    }

    public void AskForPermissions() {
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 11) {
            switch (requestCode) {
                case 1:
                    locationTrack.checkforPermission();
                    break;
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

    private void makeCall(String mobile) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mobile));
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, 1
            );

        } else {
            //You already have permission
            try {
                startActivity(callIntent);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        volleyManager = VolleyManager.getInstance(getApplicationContext(), this);
        locationTrack.checkforPermission();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Are sure you want to exit")

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            MainActivity.this.finishAffinity();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
    public static String toCamelCase(String inputString) {
        String result = "";
        if (inputString.length() == 0) {
            return result;
        }
        char firstChar = inputString.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = result + firstCharToUpperCase;
        for (int i = 1; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            char previousChar = inputString.charAt(i - 1);
            if (previousChar == ' ') {
                char currentCharToUpperCase = Character.toUpperCase(currentChar);
                result = result + currentCharToUpperCase;
            } else {
                char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = result + currentCharToLowerCase;
            }
        }
        return result;
    }
    @Override
    public void onResponseSuccess(String response) {
        CommonFunctions.hideDialog(progressDialog);

        try {
            if (response != null) {
                Gson gson = new Gson();

                Type type = new TypeToken<ResponseModel<UserModel>>() {
                }.getType();
                ResponseModel<UserModel> responseObj = gson.fromJson(response, type);
                if (responseObj.getIsSuccess() == 1) {
                    if (responseObj.getData().getId() > 0)
                    {
                        Global.USER = responseObj.getData();
                        userSession.createUserLoginSession(Global.USERTOKEN, Global.USERLOGINID, String.valueOf(Global.USERID), Global.USERTYPE);

                        if (Global.USER.getUserType().equalsIgnoreCase("USER")) {

                            navigationView = (NavigationView) findViewById(R.id.nav_view);
                            Menu nav_Menu = navigationView.getMenu();
                            nav_Menu.findItem(R.id.nav_addHospital).setVisible(false);
                            nav_Menu.findItem(R.id.nav_awayResponse).setVisible(false);
                            if (Global.USER.getFirstContact() != null && Global.USER.getSecondContact() != null && Global.USER.getNearByHospital() > 0) {
                                isUserProfileCompleted = true;
                            }
                        } else if (Global.USER.getUserType().equalsIgnoreCase("HOSPITAL")) {

                            navigationView = (NavigationView) findViewById(R.id.nav_view);
                            Menu nav_Menu = navigationView.getMenu();
                            nav_Menu.findItem(R.id.nav_addHospital).setVisible(false);
                            nav_Menu.findItem(R.id.nav_profile).setVisible(false);
                            nav_Menu.findItem(R.id.nav_setAway).setVisible(false);
                            nav_Menu.findItem(R.id.nav_awayResponse).setVisible(false);
                            isUserProfileCompleted = true;
                        } else {
                            navigationView = (NavigationView) findViewById(R.id.nav_view);
                            Menu nav_Menu = navigationView.getMenu();
                            nav_Menu.findItem(R.id.nav_setAway).setVisible(false);
                            nav_Menu.findItem(R.id.nav_profile).setVisible(false);
                            isUserProfileCompleted = true;
                        }
                        if (!isUserProfileCompleted) {
                            Intent it = new Intent(MainActivity.this, EditUserProfile.class);
                            startActivity(it);
                            finish();
                        }
                        View headerView = navigationView.getHeaderView(0);
                        txtUsername = (TextView) headerView.findViewById(R.id.username);
                         String tmptext= toCamelCase( Global.USER.getFirstName() + " " + Global.USER.getLastName());
                        txtUsername.setText(tmptext);
                        LoadFragment(R.id.nav_sos);
                    }
                } else {
                    userSession.logoutUser();
                    Intent it = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(it);
                    finish();
                }
            } else {
                userSession.logoutUser();
                Intent it = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(it);
                finish();
            }
        } catch (Exception ex) {
            userSession.logoutUser();
            Intent it = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(it);
            finish();
        }
    }

    @Override
    public void onResponseError() {
        CommonFunctions.hideDialog(progressDialog);
    }

    public void LoadFragment(int id) {
        Fragment fragment = null;

        if (Global.USER.getUserType().equalsIgnoreCase("USER")) {
            switch (id)
            {
                case R.id.nav_sos:
                    fragment = new HomeFragment();
                    break;
                case R.id.nav_ChangePassword:
                    fragment = new ChangePasswordFragment();
                    break;
                case R.id.nav_setAway:
                    fragment = new AwayUserFragment();
                    break;
                case R.id.nav_profile:
                    Intent it = new Intent(MainActivity.this, UserProfile.class);
                    startActivity(it);
                    break;
                case R.id.nav_logout:
                    userSession.logoutUser();
                    Intent its = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(its);
                    break;
            }

        } else {
            switch (id) {
                case R.id.nav_sos:
                    fragment = new EmergencyResponseFragment();
                    break;
                case R.id.nav_ChangePassword:
                    fragment = new ChangePasswordFragment();
                    break;
                case R.id.nav_profile:
                    Intent it = new Intent(MainActivity.this, UserProfile.class);
                    startActivity(it);
                    break;
                case R.id.nav_addHospital:
                    fragment = new AddHospitalFragment();
                    break;
                case R.id.nav_awayResponse:
                    fragment = new AwayFragment();
                    break;
                case R.id.nav_logout:
                    userSession.logoutUser();
                    Intent its = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(its);
                    break;
            }
        }
        if (fragment == null)
            return;
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment)
                .commit();
    }

}
