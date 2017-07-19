package com.p2pdinner.common;

/**
 * Created by rajaniy on 7/19/15.
 */
public class Constants {
    // REQUEST CODES
    public static final int FACEBOOK_REQUEST_CODE = 100;

    public static final String PREFS_PRIVATE = "PREFS_PRIVATE";
    public static final String EMAIL_ADDRESS = "EMAIL_ADDRESS";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String USER_ID ="USER_ID";
    public static final String PROFILE_ID = "PROFILE_ID";
    public static final String CURRENT_DINNER_ITEM = "CURRENT_DINNER_ITEM";
    public static final String CURRENT_DINNER_LISTINGS = "CURRENT_DINNER_LISTINGS";
    public static final String DINNER_LISTING_VIEW_CONTENT = "DINNER_LISTING_VIEW_CONTENT";
    public static final String NO_OF_GUESTS = "NO_OF_GUESTS";
    public static final String SELL_LISTING_ITEM = "SELL_LISTING_ITEM";
    public static final String PROFILE_NAME = "PROFILE_NAME";
    public static final String IS_VALID_PROFILE = "IS_VALID_PROFILE";
    public static final String AUTHENTICATION_PROVIDER = "AUTHENTICATION_PROVIDER";
    public static final String CERTIFICATES = "CERTIFICATES";

    //Legal
    public static final String LEGAL_LOAD_URI = "LEGAL_LOAD_URI";

    //Registration messages
    public static final String REGISTRATION_COMPLETE = "REGISTRATION_COMPLETE";
    public static final String SENT_TOKEN_TO_SERVER = "SENT_TOKEN_TO_SERVER";

    //public static final String P2PDINNER_BASE_URI = "https://p2pdinner-services.herokuapp.com/api/v1";
    //public static final String P2PDINNER_BASE_URI = "https://dev-p2pdinner-services.herokuapp.com/api/v1";
    public static final String P2PDINNER_WEB_BASE_URI = "https://p2pdinner-services.herokuapp.com";

    public static final String P2PDINNER_API_TOKEN = "P2PDINNER_API_TOKEN";

    public static final String P2PDINNER_OKTA_URL = "https://dev-768670.oktapreview.com/oauth2/ausaxf4ch03nY0tMg0h7/v1";

    public static final String P2PDINNER_BASE_URI = "http://10.0.2.2:8080/api/v1";

    public static class Message {

        // DinnerListing related message
        public final static int DELIVERY_OPTIONS = 901;
        public static final int SAVE_MENU_ITEM_SUCCESS = 903;
        public static final int SEARCH_RESULTS = 906;
        // Misc
        public final static int FILE_UPLOAD_SUCCESS = 19000;
        public final static int GEOCODE_REVERSE_LOOKUP = 19001;

        public final static int UNKNOWN_ERROR = -200001;
        public final static int FILE_UPLOAD_FAILURE = -200002;



    }
}
