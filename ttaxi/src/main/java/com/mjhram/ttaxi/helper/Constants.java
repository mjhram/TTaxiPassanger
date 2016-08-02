package com.mjhram.ttaxi.helper;

public interface Constants {

    /**
     * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
     */

	String SERVER_URL = "http://www.ttaxi1.com";

	/**
     * Google API project id registered to use GCM.
     */
	String SENDER_ID = "368880841097";

	String SENDER_EMAIL 		= "senderEmail";
	String RECEIVER_EMAIL 		= "receiverEmail";
	String REG_ID 				= "regId";
	String MESSAGE 				= "message";
    String UPDATE_REG_ID        = "updateRegId";

    String KEY_IS_LOGGEDIN = "isLoggedIn";
	String KEY_PHOTO = "userPhoto";
	String KEY_PHOTO_ID = "userPhotoId";
    String KEY_NAME = "name";
	String KEY_PHONE = "userPhone";
	String KEY_EMAIL = "key_email";
    String KEY_UID = "key_uid";
	String KEY_REGID = "key_RegId";
	String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
	String REGISTRATION_COMPLETE = "registrationComplete";
    String UPDATE_REQ = "updateRequest";

	String URL_ads = SERVER_URL+"/images/";
	String TRequest_Expired = "expired";
	String TRequest_Canceled = "canceled";
	String URL_updateTRequest = SERVER_URL+"/updateTRequest.php";
	String URL_getDrivers = SERVER_URL+"/getDrivers.php";
	String URL_updateUserInfo = SERVER_URL+"/updateUserInfo.php";
	String URL_uploadImage = SERVER_URL+"/uploadImage.php";
	String URL_UpdateRegId = SERVER_URL+"/updateRegId.php";
	String URL_downloadUserPhoto = SERVER_URL+"/downloadImage.php?id=";
	String RequestsIdx = "idx";
	String RequestsPassangerId = "passangerId";
	String RequestsPassangerName = "passangerName";
	String RequestsFromLat = "fromLat";
	String RequestsFromLong = "fromLong";
	String RequestsToLat = "toLat";
	String RequestsToLong = "toLong";
	String RequestsDriverId = "driverId";
	String RequestsStatus = "status";
    String RequestsTime = "time";
    String RequestsSecondsToNow = "secondsToNow";
    String RequestsDriverName = "driverName";
    String RequestsDriverEmail = "driverEmail";
	String RequestsDriverPhone = "driverPhone";
	String RequestsDriverPhotoId = "driverPhotoId";
	String RequestsSuggestedFee = "suggestedFee";
	String RequestsNoOfPassangers = "noOfPassangers";
	String RequestsAdditionalNotes = "additionalNotes";

	//public static final String ACTION_REGISTER = "com.mjhram.ttaxi.REGISTER";
	String EXTRA_STATUS = "status";
	int STATUS_SUCCESS = 1;
	int STATUS_FAILED = 0;
}
