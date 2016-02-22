package com.mjhram.ttaxi.gcm_client;

public interface Constants {

    /**
     * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
     */

	String SERVER_URL = "http://www.ttaxi1.com";

	/**
     * Google API project id registered to use GCM.
     */
	String SENDER_ID = "368880841097";

	public static final String SENDER_EMAIL 		= "senderEmail";
	public static final String RECEIVER_EMAIL 		= "receiverEmail";
	public static final String REG_ID 				= "regId";
	public static final String MESSAGE 				= "message";
    public static final String UPDATE_REG_ID        = "updateRegId";

    public static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    public static final String KEY_NAME = "name";
	public static final String KEY_PHONE = "userPhone";
	public static final String KEY_EMAIL = "key_email";
    public static final String KEY_UID = "key_uid";
	public static final String KEY_REGID = "key_RegId";
	public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
	public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String UPDATE_REQ = "updateRequest";

	public static final String TRequest_Expired = "expired";
	public static final String TRequest_Canceled = "canceled";
	public static final String URL_updateTRequest = "http://www.ttaxi1.com/updateTRequest.php";
	public static String URL_UpdateRegId = "http://www.ttaxi1.com/updateRegId.php";
	public static String RequestsIdx = "idx";
	public static String RequestsPassangerId = "passangerId";
	public static String RequestsPassangerName = "passangerName";
	public static String RequestsFromLat = "fromLat";
	public static String RequestsFromLong = "fromLong";
	public static String RequestsToLat = "toLat";
	public static String RequestsToLong = "toLong";
	public static String RequestsDriverId = "driverId";
	public static String RequestsStatus = "status";
    public static String RequestsTime = "time";
    public static String RequestsSecondsToNow = "secondsToNow";
    public static String RequestsDriverName = "driverName";
    public static String RequestsDriverEmail = "driverEmail";
	public static String RequestsSuggestedFee = "suggestedFee";
	public static String RequestsNoOfPassangers = "noOfPassangers";
	public static String RequestsAdditionalNotes = "additionalNotes";

	//public static final String ACTION_REGISTER = "com.mjhram.ttaxi.REGISTER";
	public static final String EXTRA_STATUS = "status";
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_FAILED = 0;
}
