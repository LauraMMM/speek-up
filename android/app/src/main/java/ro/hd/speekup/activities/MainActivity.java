package ro.hd.speekup.activities;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import ro.hd.speekup.R;
import ro.hd.speekup.classes.*;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver_Requests;
    private final static String TAG = "MainActivity";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createBroadcastReceiver();
        if (isNetworkEnabled()) {
            startCheckServerThread();
        } else {
            buildNoConnectionDialog(this).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterRequestBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkEnabled()) {
            startCheckServerThread();
        } else {
            buildNoConnectionDialog(this).show();
        }
        registerRequestBroadcastReceiver();
    }

    private void gotoLogin() {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    /*private void gotoChatList() {
        Intent chatListIntent = new Intent(getApplicationContext(), ChatListActivity.class);
        startActivity(chatListIntent);
        finish();
    }*/

    private void registerRequestBroadcastReceiver() {
        registerReceiver(broadcastReceiver_Requests, new IntentFilter("ro.hd.speekup.MainActivity"));
    }

    private void unregisterRequestBroadcastReceiver() {
        try {
            unregisterReceiver(broadcastReceiver_Requests);
        } catch (IllegalArgumentException e) {
            Log.w("Receiver not registered", "broadcastReceiver_Requests");
        }
    }

    private void createBroadcastReceiver() {
        broadcastReceiver_Requests = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String type = intent.getStringExtra("type");
                String response = intent.getStringExtra("response");
                boolean success = intent.getBooleanExtra("success", false);
                if (response.isEmpty()) {
                    buildServerOfflineDialog(context).show();
                } else {
                    switch (type) {
                        //response from pinging the server to test connection
                        case ServerActions.ACTION_CHECK_SERVER:
                            if (success) {
                                if (checkPlayServices()) {
                                    PushManager.gcm = GoogleCloudMessaging.getInstance(context);
                                    PushManager.regid = PushManager.getRegistrationId(context);
                                    if (PushManager.regid.isEmpty()) {
                                        PushManager.registerInBackground();
                                    }

                                    if (!USO.isSet()) {
                                        USO.getFromPermanentStorage(context);
                                    }
                                    if (USO.isSet()) {
                                        //check if USO is still valid
                                        //RequestManager.pingServer(getApplicationContext(), "StartActivity");

                                        //skip directly to chat list, will be auto logged out if USO is no longer valid
                                        //gotoChatList();
                                    } else {
                                        gotoLogin();
                                    }
                                } else {
                                    Log.i(PushManager.TAG, "No valid Google Play Services APK found.");
                                }
                            } else {
                                buildServerOfflineDialog(context).show();
                            }
                            break;
                        default:
                            break;
                    }

                }
            }
        };
        registerRequestBroadcastReceiver();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("GCM Error", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public AlertDialog.Builder buildNoConnectionDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet connection.");
        builder.setMessage("You have no internet connection");

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.setPositiveButton("Open settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                finish();
            }
        });

        return builder;
    }

    public AlertDialog.Builder buildServerOfflineDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Cannot reach server.");
        builder.setMessage("The server cannot be reached at the moment. Please try again later.");

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        return builder;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        recreate();
    }

    /**
     * Start a server check
     */
    private void startCheckServerThread() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ApiManager.checkServer(getApplicationContext(), "MainActivity");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    public boolean isNetworkEnabled() {
        ConnectivityManager connMgr = (ConnectivityManager)getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


}