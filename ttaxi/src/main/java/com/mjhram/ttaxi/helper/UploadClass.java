package com.mjhram.ttaxi.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mjhram.ttaxi.R;
import com.mjhram.ttaxi.common.AppSettings;
import com.mjhram.ttaxi.common.DriverInfo;
import com.mjhram.ttaxi.common.TRequestObj;
import com.mjhram.ttaxi.common.events.ServiceEvents;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by mohammad.haider on 10/8/2015.
 */
public class UploadClass {
    private ProgressDialog pDialog;
    private Context cx;
    private static final String URL_addTRequest = "http://www.ttaxi1.com/addTRequest.php";
    private static final String URL_getPassangerState = "http://www.ttaxi1.com/getPassangerState.php";
    private static final String TAG = UploadClass.class.getSimpleName();


    public UploadClass(Context theCx) {
        cx = theCx;
        pDialog = new ProgressDialog(cx);
        pDialog.setCancelable(false);

    }

    public void updateUserInfo(final String username, final String useremail, final String userphone) {
        // Tag used to cancel the request
        String tag_string = "modifyUserInfo";

        pDialog.setMessage(cx.getString(R.string.uploadDlgMsgUpdatingInfo));
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.URL_updateUserInfo, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "updateTReq Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        AppSettings.setPhone(userphone);
                        AppSettings.setEmail(useremail);
                        AppSettings.setName(username);
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(cx,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "update User Info Error: " + error.getMessage());
                Toast.makeText(cx,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "updateUserInfo");
                params.put("username", username);
                params.put("useremail", useremail);
                params.put("userphone", userphone);
                params.put("uid", AppSettings.getUid());
                return params;
            }
        };
        // Adding request to request queue
        AppSettings ac = AppSettings.getInstance();
        ac.addToRequestQueue(strReq, tag_string);
    }

    public void getPassangerState(final String passangerId) {
        // Tag used to cancel the request
        String tag_string_req = "updatePassangerState";

        pDialog.setMessage(cx.getString(R.string.uploadDlgMsgUpdating));
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_getPassangerState, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "getPassangerState Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String tmp = jObj.getString("requests");
                        if(tmp.equalsIgnoreCase("{}")/*requests.length() == 0*/) {
                            //idle state: no requests
                            EventBus.getDefault().post(new ServiceEvents.UpdateStateEvent(null));
                        } else {
                            JSONObject requests = new JSONObject(tmp);
                            //in a task state
                            // show info: from/to/driver
                            JSONObject c = requests;//.getJSONObject(0);
                            TRequestObj treq = new TRequestObj();
                            treq.idx = c.getInt(Constants.RequestsIdx);
                            treq.passangerName = c.getString(Constants.RequestsPassangerName);
                            treq.passanger_id = passangerId;//c.getString(Constants.RequestsPassangerId);
                            treq.fromLat = c.getDouble(Constants.RequestsFromLat);
                            treq.fromLong = c.getDouble(Constants.RequestsFromLong);
                            treq.toLat = c.getDouble(Constants.RequestsToLat);
                            treq.toLong = c.getDouble(Constants.RequestsToLong);
                            treq.driverId = c.getString(Constants.RequestsDriverId);
                            treq.status = c.getString(Constants.RequestsStatus);
                            treq.time = c.getString(Constants.RequestsTime);
                            treq.secondsToNow = c.getString(Constants.RequestsSecondsToNow);
                            treq.driverName = c.getString(Constants.RequestsDriverName);
                            treq.driverPhotoUrl = Constants.URL_downloadUserPhoto + c.getString(Constants.RequestsDriverPhotoId);
                            treq.driverInfo = cx.getString(R.string.uploadDriverInfo) + c.getString(Constants.RequestsDriverEmail);
                            treq.driverPhone = c.getString(Constants.RequestsDriverPhone);
                            treq.suggestedFee = c.getString(Constants.RequestsSuggestedFee);
                            treq.noOfPassangers = c.getString(Constants.RequestsNoOfPassangers);
                            treq.additionalNotes = c.getString(Constants.RequestsAdditionalNotes);

                            EventBus.getDefault().post(new ServiceEvents.UpdateStateEvent(treq));

                            DriverInfo driverInfo = new DriverInfo();
                            driverInfo.latitude = c.getDouble("drvLat");
                            driverInfo.longitude = c.getDouble("drvLng");
                            if(driverInfo.latitude != -1) {
                                EventBus.getDefault().post(new ServiceEvents.DriverLocationUpdate(driverInfo));
                            }
                        }
                    } else {
                        //AppSettings.requestId = -1;
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(cx,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getPassangerState Error: " + error.getMessage());
                Toast.makeText(cx,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
                EventBus.getDefault().post(new ServiceEvents.ErrorConnectionEvent());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "getPassangerState");
                params.put("passangerId", passangerId);
                return params;
            }
        };
        // Adding request to request queue
        AppSettings ac = AppSettings.getInstance();
        ac.addToRequestQueue(strReq, tag_string_req);
    }

    public void setTRequestState(final String requestId, final String state) {
        // Tag used to cancel the request
        String tag_string_req = "setTRequestState";

        pDialog.setMessage(cx.getString(R.string.uploadDlgMsgUpdatingRqst));
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.URL_updateTRequest, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "updateTReq Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(cx,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "addRequest Error: " + error.getMessage());
                Toast.makeText(cx,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "updateTRequestByPassenger");
                params.put("requestId", requestId);
                params.put("state", state);
                params.put("drvId", "-1");

                return params;
            }
        };
        // Adding request to request queue
        AppSettings ac = AppSettings.getInstance();
        ac.addToRequestQueue(strReq, tag_string_req);
    }

    public void addTRequest(final String passangerId, final String email,
                            final String lat1, final String long1, final String lat2, final String long2,
                            final String suggestedFee, final String noOfPassangers, final String additionalNotes)
    {
        // Tag used to cancel the request
        String tag_string_req = "addTRequest";

        pDialog.setMessage(cx.getString(R.string.uploadDlgMsgUploadingRqst));
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_addTRequest, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "AddTReq Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        AppSettings.requestId = jObj.getInt("requestId");
                    } else {
                        AppSettings.requestId = -1;
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        //Toast.makeText(cx,
                                //errorMsg, Toast.LENGTH_LONG).show();
                        hideDialog();
                        EventBus.getDefault().post(new ServiceEvents.CancelTRequests(errorMsg));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideDialog();
                    EventBus.getDefault().post(new ServiceEvents.CancelTRequests(e.getMessage()));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "addRequest Error: " + error.getMessage());
                //Toast.makeText(cx,
                  //      error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
                EventBus.getDefault().post(new ServiceEvents.CancelTRequests(error.getMessage()));
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "addTRequest");
                params.put("email", email);
                params.put("passangerId", passangerId);
                params.put("lat", lat1);
                params.put("long", long1);
                params.put("lat2", lat2);
                params.put("long2", long2);
                params.put("suggestedFee", suggestedFee);
                params.put("noOfPassangers", noOfPassangers);
                params.put("additionalNotes", additionalNotes);
                return params;
            }
        };
        // Adding request to request queue
        AppSettings ac = AppSettings.getInstance();
                ac.addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
