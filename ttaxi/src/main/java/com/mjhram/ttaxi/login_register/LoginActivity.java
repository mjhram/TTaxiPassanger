package com.mjhram.ttaxi.login_register;

/**
* Author: Ravi Tamada
* URL: www.androidhive.info
* twitter: http://twitter.com/ravitamada
* */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mjhram.ttaxi.R;
import com.mjhram.ttaxi.common.AppSettings;
import com.mjhram.ttaxi.gcm_client.Constants;
import com.mjhram.ttaxi.gcm_client.RegistrationIntentService;
import com.mjhram.ttaxi.login_register.app.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    // LogCat tag
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    //private SessionManager session;
    //private GcmUtil gcmUtil;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    //private ProgressBar mRegistrationProgressBar;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private void setButtons(boolean enabled) {
        btnLogin.setEnabled(enabled);
        btnLinkToRegister.setEnabled(enabled);
        inputEmail.setEnabled(enabled);
        inputPassword.setEnabled(enabled);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        setButtons(true);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //get GCM registeration ID:
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(Constants.SENT_TOKEN_TO_SERVER, false);

                /*if (sentToken) {
                    setButtons(true);
                } else {
                    setButtons(false);
                }*/
            }
        };
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
        // Session manager
        //session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (AppSettings.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, com.mjhram.ttaxi.GpsMainActivity.class);
            //intent.putExtra(Constants.KEY_UID, AppSettings.getUid());
            //intent.putExtra(Constants.KEY_EMAIL, AppSettings.getEmail());
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                //check for regId
                if(AppSettings.regId==null) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.loginMsgNotRegistered), Toast.LENGTH_LONG)
                            .show();
                    finish();
                }
                // Check for empty data in the form
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.loginMsgEnterCredentials), Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                    RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
        //registerReceiver(registrationStatusReceiver, new IntentFilter(Constants.ACTION_REGISTER));
        //get registeration id
        //gcmUtil = new GcmUtil(getApplicationContext());
        //mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        //mRegistrationProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onDestroy() {
        //unregisterReceiver(registrationStatusReceiver);
        //gcmUtil.cleanup();
        super.onDestroy();
    }
    /**
     * function to verify login details in mysql db
     * */
     private void checkLogin(final String email, final String password) {
     // Tag used to cancel the request
     String tag_string_req = "req_login";

     pDialog.setMessage(getString(R.string.loginDlgMsgUpdating));
     showDialog();

     StringRequest strReq = new StringRequest(Method.POST,
        AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        //String created_at = user.getString("created_at");

                        AppSettings.setLogin(true,name, email, uid);
                        AppSettings.shouldUploadRegId = false;
                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                com.mjhram.ttaxi.GpsMainActivity.class);
                        intent.putExtra(Constants.KEY_UID, AppSettings.getUid());
                        intent.putExtra(Constants.KEY_EMAIL, AppSettings.getEmail());
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "login");
                params.put("email", email);
                params.put("password", password);
                params.put("regId", AppSettings.regId);
                return params;
            }

            };

            // Adding request to request queue
         AppSettings tmp = AppSettings.getInstance();
         tmp.addToRequestQueue(strReq, tag_string_req);
        }

        private void showDialog() {
            if (!pDialog.isShowing())
            pDialog.show();
        }

        private void hideDialog() {
            if (pDialog.isShowing())
            pDialog.dismiss();
        }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /*private BroadcastReceiver registrationStatusReceiver = new  BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && Constants.ACTION_REGISTER.equals(intent.getAction())) {
                switch (intent.getIntExtra(Constants.EXTRA_STATUS, 100)) {
                    case Constants.STATUS_SUCCESS:
                        setButtons(true);
                        break;

                    case Constants.STATUS_FAILED:
                        setButtons(false);
                        break;
                }
            }
        }
    };*/
}