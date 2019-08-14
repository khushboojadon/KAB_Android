package com.appflowsolutions.kab.Utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonFunctions {

    public static void showDialog(ProgressDialog progressDialog) {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    public static void hideDialog(ProgressDialog progressDialog) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public static void LoadFontAwesome(Activity activity)
    {
        if(Global.FontAwesome ==null)
        {
            Global.FontAwesome= Typeface.createFromAsset(activity.getAssets(),  "fonts/fontawesome-webfont.ttf");
        }
    }

    public static boolean isInternetOn(Context context) {

        if (isMobileOrWifiConnectivityAvailable(context)) {
           /* try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (Exception e) {

            }*/
           return  true;
        }
        return false;
    }


    public static boolean isMobileOrWifiConnectivityAvailable(Context ctx) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;


        try {
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected()) {
                        haveConnectedWifi = true;
                    }
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected()) {
                        haveConnectedMobile = true;
                    }
            }
        } catch (Exception e) {

        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
