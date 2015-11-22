package ro.hd.speekup.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ro.hd.speekup.BuildConfig;
import ro.hd.speekup.classes.ServerActions;
import ro.hd.speekup.classes.USO;
import ro.hd.speekup.entities.ServerRequest;

public class ApiManagerService extends IntentService {

    //private static final boolean logRequests = BuildConfig.DEBUG;
    private static final boolean logRequests = true;

    private static final String SERVICE_NAME = ro.hd.speekup.services.ApiManagerService.class.getName();
    private static final String TAG = "ApiManagerService";

    private PowerManager.WakeLock wakeLock;

    public ApiManagerService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty()) {
            if (extras.containsKey("request")) {
                wakeLock.acquire();
                ServerRequest request = (ServerRequest) extras.getSerializable("request");

                if (!USO.isSet()) {
                    USO.getFromPermanentStorage(getApplicationContext());
                }

                if (request != null) {
                    try {
                        //todo: trustAllCerts() and trustAllHosts() are bad for your spleen, using them until server SSL gets fixed
                        HttpRequest.keepAlive(true);
                        HttpRequest response = HttpRequest
                                .post(request.getUrl())
                                //.connectTimeout(5000)
                                //.readTimeout(5000)
                                .trustAllCerts()
                                .trustAllHosts()
                                .ignoreCloseExceptions(false)
                                .form(request.getParameters());
                        if (response.ok()) {
                            String body = response.body();
                            if (logRequests) {
                                Log.d("Body", "-" + body);
                            }

                            Intent responseIntent = new Intent();
                            responseIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                            responseIntent.setAction(BuildConfig.APPLICATION_ID + "." + request.getOwner());
                            responseIntent.putExtra("type", request.getType());
                            responseIntent.putExtra("response", body);
                            responseIntent.putExtra("success", true);
                            if (request.getId() != 0) {
                                responseIntent.putExtra("id", request.getId());
                            }
                            getApplicationContext().sendBroadcast(responseIntent);

                            if (logRequests) {
                                Log.d("ApiManagerService", "Type: " + request.getType());
                                Log.d("ApiManagerService", "Code: " + Integer.toString(response.code()));
                                Log.d("ApiManagerService", "URL: " + request.getUrl());
                                Log.d("ApiManagerService: ", "Response: " + body);
                            }
                            System.gc(); //todo: check if this does anything bad
                        } else {
                            Log.e("ApiManagerService", "Type: " + request.getType());
                            Log.e("ApiManagerService", "Code: " + Integer.toString(response.code()));
                            Log.e("ApiManagerService", "URL: " + request.getUrl());
                        }

                    } catch (HttpRequest.HttpRequestException exception) {
                        Log.e("HttpRequestException", exception.getMessage());
                        exception.printStackTrace();
                        //todo: log the request to retry it later
                    }
                }
                wakeLock.release();
            }
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        //GcmBroadcastReceiver.completeWakefulIntent(intent);
        //Log.e("handleIntent", "end");
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    @Override
    public void onDestroy() {
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        //Log.e("service", "stopped");
        super.onDestroy();
    }

    private void broadcastFailure(ServerRequest request, HttpRequest response) {
        Intent responseIntent = new Intent();
        responseIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        responseIntent.setAction(BuildConfig.APPLICATION_ID + "." + request.getOwner());
        responseIntent.putExtra("type", request.getType());
        responseIntent.putExtra("response", response.code());
        responseIntent.putExtra("success", false);
        if (request.getId() != 0) {
            responseIntent.putExtra("id", request.getId());
        }
        getApplicationContext().sendBroadcast(responseIntent);
    }
}