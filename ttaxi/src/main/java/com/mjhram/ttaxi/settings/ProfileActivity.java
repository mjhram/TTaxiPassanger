package com.mjhram.ttaxi.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.mjhram.ttaxi.R;
import com.mjhram.ttaxi.common.AppSettings;
import com.mjhram.ttaxi.helper.Constants;
import com.mjhram.ttaxi.helper.UploadClass;
import com.mjhram.ttaxi.helper.phpErrorMessages;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    EditText    edit_username, edit_email, edit_phone;
    //ImageView   photoImageView;
    private NetworkImageView networkImageViewUser;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString("USER_NAME", edit_username.getText().toString());
        savedInstanceState.putString("EMAIL", edit_email.getText().toString());
        String tmp = edit_phone.getText().toString();
        savedInstanceState.putString("PHONE", tmp);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        String tmp;
        tmp = savedInstanceState.getString("USER_NAME");
        edit_username.setText(tmp);
        tmp = savedInstanceState.getString("EMAIL");
        edit_email.setText(tmp);
        tmp = savedInstanceState.getString("PHONE");
        edit_phone.setText(tmp);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
                UploadClass uc = new UploadClass(ProfileActivity.this);
                uc.updateUserInfo(edit_username.getText().toString(), edit_email.getText().toString(), edit_phone.getText().toString());
            }
        });

        edit_username = (EditText) findViewById(R.id.profile_username);
        edit_email =(EditText) findViewById(R.id.profile_email);
        edit_phone =(EditText) findViewById(R.id.profile_phone);
        networkImageViewUser = (NetworkImageView) findViewById(R.id.profilePhoto);

        networkImageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        /*photoImageView=(ImageView) findViewById(R.id.profilePhoto);
        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });*/
        edit_username.setText(AppSettings.getName());
        edit_email.setText(AppSettings.getEmail());
        edit_phone.setText(AppSettings.getPhone());
        {
            //final String IMAGE_URL = "http://developer.android.com/images/training/system-ui.png";
            ImageLoader mImageLoader = AppSettings.getInstance().getImageLoader();
            networkImageViewUser.setImageUrl(Constants.URL_downloadUserPhoto+AppSettings.getPhotoId(), mImageLoader);
        }
    }

    private String getStringImage(Bitmap bmp){
        bmp = resizeImage(bmp);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    private Bitmap getImageFromString(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    private Bitmap resizeImage(Bitmap bitmap) {
        int fixWidth=200, fixHeight=200;
        float h2wRatio = 1.0F*fixHeight/fixWidth;
        Bitmap resizedBitmap = null;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int newWidth = -1;
        int newHeight = -1;
        float multFactor = -1.0F;
        float h2wRatioOrg = 1.0F*originalHeight/originalWidth;
        if (h2wRatioOrg == h2wRatio) {
            newHeight = fixHeight;
            newWidth = fixWidth;
        } if (h2wRatioOrg > h2wRatio) {
            newHeight = fixHeight;
            multFactor = (float) originalWidth / (float) originalHeight;
            newWidth = (int) (newHeight * multFactor);
        } else if (h2wRatioOrg < h2wRatio) {
            newWidth = fixWidth;
            multFactor = (float) originalHeight / (float) originalWidth;
            newHeight = (int) (newWidth * multFactor);
        }
        resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        return resizedBitmap;
    }

    private int PICK_IMAGE_REQUEST = 1;
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Photo"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri filePath = data.getData();
            try {
                Bitmap profilePhotoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                updateUserPhoto(profilePhotoBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showDialog(ProgressDialog pDialog) {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog(ProgressDialog pDialog) {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void updateUserPhoto(final Bitmap bitmap) {
        // Tag used to cancel the request
        final String tag_string = "modifyUserPhoto";
        final String TAG = tag_string;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        pDialog.setMessage(getString(R.string.uploadDlgMsgUpdatingInfo));
        showDialog(pDialog);

        final String uploadImage = getStringImage(bitmap);
        AppSettings.setPhoto(uploadImage);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.URL_uploadImage, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "updatePhoto Response: " + response);
                hideDialog(pDialog);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String newId = jObj.getString("newid");
                        AppSettings.setPhotoId(newId);
                        {
                            //final String IMAGE_URL = "http://developer.android.com/images/training/system-ui.png";
                            ImageLoader mImageLoader = AppSettings.getInstance().getImageLoader();
                            networkImageViewUser.setImageUrl(Constants.URL_downloadUserPhoto+newId, mImageLoader);
                        }
                        //photoImageView.setImageBitmap(bitmap);
                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        //String errorMsg = jObj.getString("error_msg");
                        int errorno = jObj.getInt("error_no");
                        phpErrorMessages phpErrorMsgs = AppSettings.getInstance().getPhpErrorMsg();
                        String errorMsg = phpErrorMsgs.msgMap.get(errorno);
                        Toast.makeText(ProfileActivity.this,
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
                Toast.makeText(ProfileActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog(pDialog);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", tag_string);
                params.put("image", uploadImage);
                params.put("uid", AppSettings.getUid());
                return params;
            }
        };
        // Adding request to request queue
        AppSettings ac = AppSettings.getInstance();
        ac.addToRequestQueue(strReq, tag_string);
    }
}
