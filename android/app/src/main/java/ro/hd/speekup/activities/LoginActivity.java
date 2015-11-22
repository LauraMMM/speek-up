package ro.hd.speekup.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import ro.hd.speekup.R;
import ro.hd.speekup.classes.ApiManager;
import ro.hd.speekup.classes.USO;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;

    private AccessTokenTracker accessTokenTracker;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        try {
            PackageInfo info = getPackageManager().getPackageInfo("ro.hd.speekup", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle("Processing...");
        mProgressDialog.setMessage("Performing Facebook login");
        mProgressDialog.setCancelable(false);

        setContentView(R.layout.activity_login);

        loginButton = (LoginButton)findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("user_events", "user_photos"));

        AccessToken token = AccessToken.getCurrentAccessToken();

        if (token != null) {
            if (token.isExpired()) {
                AccessToken.refreshCurrentAccessTokenAsync();
            } else {
                getProfile(token);
            }
        } else {
            Log.e("Facebook", "No token");
        }

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                AccessToken.setCurrentAccessToken(currentAccessToken);
                if (currentAccessToken != null) {
                    if (currentAccessToken.isExpired()) {
                        AccessToken.refreshCurrentAccessTokenAsync();
                    } else {
                        getProfile(currentAccessToken);
                    }
                }

            }
        };
    }

    private void getProfile(final AccessToken token) {
        mProgressDialog.show();
        Bundle parameters = new Bundle();
        parameters.putString("fields", "picture,name");
        new GraphRequest(
                token,
                "/" + token.getUserId(),
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        mProgressDialog.hide();
                        String photoUrl = "";
                        String userName = "";
                        try {
                            JSONObject json = response.getJSONObject();
                            JSONObject picture = new JSONObject(json.getString("picture"));
                            JSONObject pictureData = new JSONObject(picture.getString("data"));
                            photoUrl = pictureData.getString("url");
                            userName = json.getString("name");
                            USO.userId = token.getUserId();
                            ApiManager.login(getApplicationContext(), token.getUserId(), userName, photoUrl, "LoginActivity");
                            Intent eventListIntent = new Intent(LoginActivity.this, EventsActivity.class);
                            startActivity(eventListIntent);
                            finish();
                        } catch (JSONException e) {
                            Log.e("JSONException", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

}
