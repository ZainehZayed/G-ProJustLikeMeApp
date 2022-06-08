package com.example.g_pro_justlikemeapp.notifications;

import com.example.g_pro_justlikemeapp.notifications.Response;
import com.example.g_pro_justlikemeapp.notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({"Content-Type:application/json",
    "Authorization-key=AAAAX7T_38Q:APA91bFrzfQmTpxgjrP-oYjI0aUBw6uvvoNotPRQRx8oOiOeDuYfRaC67-EOXJbpcmIQGeMwVRgN2w4ME4JE0g_qG_glAAxhv-Gc-dF-3oeq71bKDSD3nrYxdKRJVZOZRAMgHfecf3Yh"
    })
    //not sure about response
    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
