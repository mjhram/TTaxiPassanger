package com.mjhram.ttaxi.helper;

import android.content.Context;

import com.mjhram.ttaxi.R;

import java.util.HashMap;

/**
 * Created by mohammad.haider on 3/24/2016.
 */
public class phpErrorMessages {
    /*private String msgs[]; = {
            //addTrequest://10-19
            "no error. request added successfully",
            "Passenger already has active request",
            "Request couldn't be added",
            //acceptTrequest://20-
            "no error. request updated successfully",
            "Driver already has active request",
            "Requests already assigned",
            "Request couldn't be updated",
            //login_register
            "successfull",
            "Incorrect email or password!",
            "User already existed",
            "Error occured in Registartion",
            "Unknow 'tag' value",
            //logTaxiLoc
            "no error. location updated successfully",
            "no error. location inserted successfully",
            //updateRegId
            "RegId updated successfully",
            "Error while updating registeration id!",
            //updateTaxiLoc
            "no error. location updated successfully",
            "no error. location inserted successfully",
            //updateTaxiLocation
            "no error. location inserted successfully",
            "no error. location updated successfully",
            "error inserting location",
            "error updating location",
            //updateTRequest
            "Request updated successfully",
            "Error while updating the request!. Already assigned",
            "Error while updating the request!. Request is already completed",
            "Error while updating the request!",
            //updateUserInfo
            "No error, info updated successfully",
            "Error updating user info",
            //uploadImage
            "Image Uploaded Successfully",
            "Error Uploading Image"
    };*/
    private int msgno[] = {
            //addTrequest://10-
            10,11,12,
            //acceptTrequest://20-
            20,21,22,23,
            //login_register://30-
            30,31,32,33,34,
            //logTaxiLoc
            40,41,
            //updateRegId
            50,51,
            //updateTaxiLoc
            60,61,
            //updateTaxiLocation
            70, 71, 72, 73,
            //updateTRequest
            80,81,82,83,
            //updateUserInfo
            90,91,
            //uploadImage
            100,101

    };
    public HashMap<Integer, String> msgMap;

    public phpErrorMessages(Context cx){
        String[] msgs = cx.getResources().getStringArray(R.array.phpErrorMsgs);
        //int[] msgno = cx.getResources().getIntArray(R.array.phpErrorMsgNos);

        msgMap = new HashMap<Integer, String>();
        for (int i = 0; i < msgno.length; i++) {
            msgMap.put(msgno[i], msgs[i]);
        }
    }
}
