package ro.hd.speekup.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import ro.hd.speekup.R;
import ro.hd.speekup.adapters.EventListAdapter;
import ro.hd.speekup.adapters.ImportEventAdapter;
import ro.hd.speekup.entities.ListEvent;

public class ImportEventActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    RecyclerView mRecyclerView;
    ImportEventAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mRecyclerView = (RecyclerView) findViewById(R.id.list_events);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SimpleItemAnimator animator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(animator);

        mAdapter = new ImportEventAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle("Processing...");
        mProgressDialog.setMessage("Loading Facebook events");
        mProgressDialog.setCancelable(false);

        getEvents(AccessToken.getCurrentAccessToken());
    }


    private void getEvents(final AccessToken token) {
        mProgressDialog.show();
        Bundle parameters = new Bundle();
        parameters.putString("fields", "events");

        new GraphRequest(
                token,
                "/" + token.getUserId(),
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        mProgressDialog.hide();
                        try {
                            JSONObject responseJson = response.getJSONObject();
                            JSONObject rawEvents = new JSONObject(responseJson.getString("events"));

                            JSONArray events = new JSONArray(rawEvents.getString("data"));
                            for (int i = 0; i < events.length(); i++) {
                                JSONObject eventJson = events.getJSONObject(i);
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "name,place,cover,picture,start_time,end_time,attending_count,description");

                                new GraphRequest(
                                        AccessToken.getCurrentAccessToken(),
                                        "/" + eventJson.getString("id"),
                                        parameters,
                                        HttpMethod.GET,
                                        new GraphRequest.Callback() {
                                            public void onCompleted(GraphResponse response) {
                                                boolean isError = false;
                                                JSONObject json = response.getJSONObject();
                                                ListEvent event = new ListEvent();
                                                try {
                                                    if (json.has("id") && json.has("name") && json.has("description") && json.has("place") && json.has("start_time")) {
                                                        event.setId(json.getString("id"));
                                                        event.setTitle(json.getString("name"));
                                                        event.setDescription(json.getString("description"));
                                                        JSONObject place = new JSONObject(json.getString("place"));
                                                        if (place.has("name")) {
                                                            event.setLocationName(place.getString("name"));
                                                            if (place.has("location")) {
                                                                JSONObject locationJson = new JSONObject(place.getString("location"));
                                                                if (locationJson.has("latitude") && locationJson.has("longitude")) {
                                                                    event.setLatitude(locationJson.getString("latitude"));
                                                                    event.setLongitude(locationJson.getString("longitude"));
                                                                } else {
                                                                    isError = true;
                                                                }
                                                            } else {
                                                                isError = true;
                                                            }
                                                        } else {
                                                            isError = true;
                                                        }
                                                        try {
                                                            String startTime = json.getString("start_time");
                                                            SimpleDateFormat incomingFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                                                            Date date = incomingFormat.parse(startTime);
                                                            event.setStartTime(date.getTime() / 1000);
                                                        } catch (Exception e) {

                                                        }
                                                    } else {
                                                        isError = true;
                                                    }
                                                    if (!isError) {
                                                        if (json.has("end_time")) {
                                                            //event.setEndTime(json.getString("end_time"));
                                                        }
                                                        if (json.has("attending_count")) {
                                                            event.setAttendingCount(json.getInt("attending_count"));
                                                        }
                                                        if (json.has("picture")) {
                                                            JSONObject pictureJson = new JSONObject(json.getString("picture"));
                                                            if (pictureJson.has("data")) {
                                                                JSONObject pictureDataJson = new JSONObject(pictureJson.getString("data"));
                                                                if (pictureDataJson.has("url")) {
                                                                    event.setAvatar(pictureDataJson.getString("url"));
                                                                }
                                                            }
                                                        }
                                                        if (json.has("cover")) {
                                                            JSONObject coverJson = new JSONObject(json.getString("cover"));
                                                            if (coverJson.has("source")) {
                                                                event.setCover(coverJson.getString("source"));
                                                            }
                                                        }
                                                        //Log.e("event", event.toJson());
                                                        mAdapter.addItem(event);
                                                    }

                                                } catch (JSONException e) {
                                                    Log.e("JSONException", e.getMessage());
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                ).executeAsync();


                                //Log.e("event", eventJson.toString());
                            }
                        } catch (JSONException e) {
                            Log.e("JSONException", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();

    }

}
