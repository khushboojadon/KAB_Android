package com.appflowsolutions.kab.Utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.appflowsolutions.kab.Interfaces.IVolleyResponseManager;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VolleyManager {

    Context activityContext;

    IVolleyResponseManager volleyResponseManager;

    private static VolleyManager volleyManagerSingletonInstance;

    private VolleyManager(Context context) {
        activityContext = context;
    }

    public  void setResponseManager(IVolleyResponseManager volleyResponse){
        this.volleyResponseManager=volleyResponse;
    }

    public static synchronized VolleyManager getInstance(Context context, IVolleyResponseManager volleyResponse) {
        if (volleyManagerSingletonInstance == null) {
            volleyManagerSingletonInstance = new VolleyManager(context);
        }
        volleyManagerSingletonInstance.setResponseManager(volleyResponse);
        return volleyManagerSingletonInstance;
    }

    public void volleyStringRequst(String url) {
        String REQUEST_TAG = "KAP_STRING";
        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("dc", response.toString());
                volleyResponseManager.onResponseSuccess( response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
                error.printStackTrace();
                volleyResponseManager.onResponseError();
            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(activityContext).addToRequestQueue(strReq, REQUEST_TAG);
    }

    public void volleyJsonObjectRequest(String url)
    {
        String REQUEST_TAG = "KAP_JSONOBJECT";

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("dbf", response.toString());
                        volleyResponseManager.onResponseSuccess(response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ngng", "Error: " + error.getMessage());
                volleyResponseManager.onResponseError();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-type", "application/json");
                if(Global.USER!=null)
                {
                    if(Global.USER.getToken()!=null && Global.USER.getToken()!="")
                    {
                        headers.put("Authorization", Global.USER.getToken());
                    }
                }
                headers.put("Content-type", "application/json");
                return headers;
            }
        };

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(activityContext).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }

    public void volleyJsonObjectPostRequest(String url, JSONObject jsonObject)
    {
        String REQUEST_TAG = "KAP_JSONOBJECT";
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("dbf", response.toString());
                        volleyResponseManager.onResponseSuccess(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ngng", "Error: " + error.getMessage());
                volleyResponseManager.onResponseError();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-type", "application/json");
                if(Global.USER!=null)
                {
                    if(Global.USER.getToken()!=null && Global.USER.getToken()!="")
                    {
                        headers.put("Authorization", Global.USER.getToken());
                    }
                }
              //  headers.put("Content-type", "application/json");
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(activityContext).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }
    /*private void submitPost() {
        Charset encoding = Charset.forName("UTF-8");
        MultipartEntity reqEntity = new MultipartEntity();
        try {
            StringBody userId = new StringBody(AppUtils.getUserId(context), encoding);
            StringBody avtarId = new StringBody(avtarid, encoding);
            StringBody statusVisiblity = new StringBody(spinnerShareWith.getSelectedItem().toString(), encoding);
            StringBody statusType = new StringBody("TEXT", encoding);
            StringBody description = new StringBody(edt_text_post.getText().toString(), encoding);
            if (isAlbum) {
                if (arrayListPhotosAlbum.size() > 0) {
                    for (int i = 0; i < arrayListPhotosAlbum.size(); i++) {
                        FileBody filebodyimage = new FileBody(new File(arrayListPhotosAlbum.get(i).getImage()));
                        StringBody desc = new StringBody(arrayListPhotosAlbum.get(i).getImageDesc(), encoding);
                        reqEntity.addPart("statusImages[" + i + "]", filebodyimage);
                        reqEntity.addPart("statusImagesDesc[" + i + "]", desc);

                        Log.e("image_desc", "*" + arrayListPhotosAlbum.get(i).getImageDesc());
                    }
                }
                if (spinner_album.getSelectedItemPosition() == 0) {
                    StringBody name = new StringBody(edt_albumname.getText().toString(), encoding);
                    StringBody isNewAlbum = new StringBody("yes", encoding);
                    reqEntity.addPart("album", name);
                    reqEntity.addPart("isNewAlbum", isNewAlbum);
                } else {
                    StringBody name = new StringBody(listAlbumId.get(spinner_album.getSelectedItemPosition()), encoding);
                    reqEntity.addPart("album", name);
                    StringBody isNewAlbum = new StringBody("no", encoding);
                    reqEntity.addPart("isNewAlbum", isNewAlbum);
                }

            } else {
                if (arrayListPhotos.size() > 0) {
                    for (int i = 0; i < arrayListPhotos.size(); i++) {
                        FileBody filebodyimage = new FileBody(new File(arrayListPhotos.get(i).getImage()));
                        reqEntity.addPart("statusImages[" + i + "]", filebodyimage);
                    }
                }
            }


            Log.e("user", avtarid);
            Log.e("statusVisibility", spinnerShareWith.getSelectedItem().toString());
            Log.e("statusType", "TEXT");
            Log.e("description", edt_text_post.getText().toString());
            Log.e("Content-Type", "undefined");
            Log.e("Authorization", AppUtils.getAuthToken(context));

            reqEntity.addPart("avatar", avtarId);
            reqEntity.addPart("user", userId);
            reqEntity.addPart("statusVisibility", statusVisiblity);
            reqEntity.addPart("statusType", statusType);
            reqEntity.addPart("description", description);

            if (AppUtils.isNetworkAvailable(context)) {
                String url = JsonApiHelper.BASEURL + JsonApiHelper.CREATESTATUS;
                new AsyncPostDataFileResponse(context, Fragment_PostAvtarFeed.this, 1, reqEntity, url);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        }
    }*/

    public void volleyJsonObjectPatchRequest(String url, JSONObject jsonObject)
    {
        String REQUEST_TAG = "KAP_JSONOBJECT";
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.PATCH, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("dbf", response.toString());
                        volleyResponseManager.onResponseSuccess(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ngng", "Error: " + error.getMessage());
                volleyResponseManager.onResponseError();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-type", "application/json");
                if(Global.USER!=null)
                {
                    if(Global.USER.getToken()!=null && Global.USER.getToken()!="")
                    {
                        headers.put("Authorization", Global.USER.getToken());
                    }
                }
                headers.put("Content-type", "application/json");
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(activityContext).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }

    public void volleyJsonObjectDeleteRequest(String url)
    {
        String REQUEST_TAG = "KAP_JSONOBJECT";
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.DELETE,url,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("dbf", response.toString());
                        volleyResponseManager.onResponseSuccess(response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ngng", "Error: " + error.getMessage());
                volleyResponseManager.onResponseError();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-type", "application/json");
                if(Global.USER!=null)
                {
                    if(Global.USER.getToken()!=null && Global.USER.getToken()!="")
                    {
                        headers.put("Authorization", Global.USER.getToken());
                    }
                }
                headers.put("Content-type", "application/json");
                return headers;
            }
        };

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(activityContext).addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }
    public void volleyJsonArrayRequest(String url) {

        String REQUEST_TAG = "KAP_JSONARRAY";


        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("sfbg", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("dvfbf", "Error: " + error.getMessage());

            }
        });

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(activityContext).addToRequestQueue(jsonArrayReq, REQUEST_TAG);
    }

    public void volleyImageLoader(String url) {
        ImageLoader imageLoader = AppSingleton.getInstance(activityContext).getImageLoader();

        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("sdngd", "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {

                }
            }
        });
    }

    public void volleyCacheRequest(String url) {
        Cache cache = AppSingleton.getInstance(activityContext).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    public void volleyInvalidateCache(String url) {
        AppSingleton.getInstance(activityContext).getRequestQueue().getCache().invalidate(url, true);
    }

    public void volleyDeleteCache(String url) {
        AppSingleton.getInstance(activityContext).getRequestQueue().getCache().remove(url);
    }

    public void volleyClearCache() {
        AppSingleton.getInstance(activityContext).getRequestQueue().getCache().clear();
    }
}
