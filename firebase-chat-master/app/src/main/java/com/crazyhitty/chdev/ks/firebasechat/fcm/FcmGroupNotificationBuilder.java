package com.crazyhitty.chdev.ks.firebasechat.fcm;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crazyhitty.chdev.ks.firebasechat.FirebaseChatMainApp;
import com.crazyhitty.chdev.ks.firebasechat.models.GroupPayload;
import com.crazyhitty.chdev.ks.firebasechat.models.PayloadData;
import com.crazyhitty.chdev.ks.firebasechat.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FcmGroupNotificationBuilder {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "FcmNotificationBuilder";
    private static final String SERVER_API_KEY = "AAAAbSi3aKo:APA91bFybGAaMUMTPY1Y0LWtUEhIATOPiuSlPxwZ9DkR4GQXWYn0ih19n8mZ0KOOhAJ357GZ6JP59qXJL-LfEJyMd3cq2tEWS1FwN-e-B2S5kO3N-DV1wCkO7QOpFnFAdJoBQ4NubQEy";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTH_KEY = "key=" + SERVER_API_KEY;
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    // json related keys

    private static final String KEY_TEXT = "text";

    private static final String KEY_TO = "to";
    private static final String KEY_DATA = "data";
    private static final String KEY_SENDER = "senderid";
    private static final String KEY_MESSAGE = "message";

    private String mTo;
    private String mSender;
    private String mMessage;

    private FcmGroupNotificationBuilder() {

    }

    public static FcmGroupNotificationBuilder initialize() {
        return new FcmGroupNotificationBuilder();
    }

    public FcmGroupNotificationBuilder to(String to) {
        mTo = to;
        return this;
    }

    public FcmGroupNotificationBuilder sender(String sender) {
        mSender = sender;
        return this;
    }

    public FcmGroupNotificationBuilder message(String message) {
        mMessage = message;
        return this;
    }


    public void send() {
        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create(MEDIA_TYPE_JSON, getValidJsonBody().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(AUTHORIZATION, AUTH_KEY)
                .url(FCM_URL)
                .post(requestBody)
                .build();

        Call call = new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onGetAllUsersFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + response.body().string());
            }
        });
    }

//    public void makeJsonObjReq() {
////        PayloadData pd = new PayloadData("anudeep", "ola, world");
////        GroupPayload gp = new GroupPayload("/topics/64f3a563-a491-4243-9014-7c43a2ce21b8", pd);
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(com.android.volley.Request.Method.POST,
//                Constants.URL_POST, gp.toJSON(),
//                new com.android.volley.Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d(TAG, response.toString());
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//            }
//        }) {
//            /**
//             * Passing some request headers
//             */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                headers.put("Authorization", "key=AAAAbSi3aKo:APA91bFybGAaMUMTPY1Y0LWtUEhIATOPiuSlPxwZ9DkR4GQXWYn0ih19n8mZ0KOOhAJ357GZ6JP59qXJL-LfEJyMd3cq2tEWS1FwN-e-B2S5kO3N-DV1wCkO7QOpFnFAdJoBQ4NubQEy");
//                return headers;
//            }
//        };
//        FirebaseChatMainApp.getInstance().addToRequestQueue(jsonObjReq,"sometag");
//    }

    private JSONObject getValidJsonBody() throws JSONException {
        JSONObject jsonObjectBody = new JSONObject();
        jsonObjectBody.put(KEY_TO, "/topics/"+mTo);

        JSONObject jsonObjectData = new JSONObject();
        jsonObjectData.put(KEY_SENDER, mSender);
        jsonObjectData.put(KEY_MESSAGE, mMessage);
        jsonObjectBody.put(KEY_DATA, jsonObjectData);

        return jsonObjectBody;
    }
}
