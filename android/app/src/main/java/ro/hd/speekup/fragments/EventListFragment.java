package ro.hd.speekup.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ro.hd.speekup.R;
import ro.hd.speekup.adapters.EventListAdapter;
import ro.hd.speekup.classes.ServerActions;
import ro.hd.speekup.classes.USO;
import ro.hd.speekup.entities.ListEvent;

public class EventListFragment extends Fragment {
    public static final String ARG_LIST_TYPE = "list_type";

    RecyclerView mRecyclerView;
    EventListAdapter mAdapter;

    private BroadcastReceiver broadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_events);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SimpleItemAnimator animator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(animator);

        mAdapter = new EventListAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        createBroadcastReceiver();

        //getEvents();

        return rootView;
    }

    @Override
    public void onResume() {
        registerBroadcastReceiver();
        super.onResume();
    }

    @Override
    public void onPause() {
        unregisterBroadcastReceiver();
        super.onPause();
    }

    private void getEvents() {
        mAdapter.addItem(new ListEvent().setId("1")
                .setTitle("First event")
                .setLocationName("Starting point: Piata Unirii")
                .setAvatar("http://images.clipartpanda.com/cute-clip-art-cute-winter-penguin-beauty-clipart-53257653.jpg")
                .setStartTime(1448129109));
        mAdapter.addItem(new ListEvent().setId("2")
                .setTitle("Second event")
                .setLocationName("Starting point: Palatul Victoria")
                .setAvatar("http://dog-pictures.clipartonline.net/_/rsrc/1383848219294/cartoon-puppy-dogs/Dog_Cartoon_Image-3.png?height=320&width=320")
                .setStartTime(1448134109));

        if (getArguments().getString(ARG_LIST_TYPE, "").equals("current")) {
            mAdapter.addItem(new ListEvent().setId("3")
                    .setTitle("The third event")
                    .setLocationName("Starting point: Centru")
                    .setAvatar("http://www.cutecliparts.com/wp-content/uploads/2015/07/Kitty-Cat-Playing-with-Yarn.png")
                    .setStartTime(1449129109)
                    .setIsAttending(true));
            mAdapter.addItem(new ListEvent().setId("4")
                    .setTitle("Fourth and final event")
                    .setLocationName("Starting point: Piata Constitutiei")
                    .setAvatar("http://images.clipartpanda.com/bird-clipart-Little-Blue-Bird-Clip-Art.png")
                    .setStartTime(1488129109)
                    .setIsAttending(true));
        }
    }

    private void createBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String type = intent.getStringExtra("type");
                String response = intent.getStringExtra("response");
                switch (type) {
                    case ServerActions.ACTION_GET_EVENTS:
                        mAdapter.removeAll();
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            if (responseJson.has("data")) {
                                JSONArray dataArray = new JSONArray(responseJson.getString("data"));
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject event = dataArray.getJSONObject(i);
                                    ListEvent parsedEvent = new ListEvent(event);


                                    boolean amAttending = false;
                                    if (event.has("attendees")) {
                                        JSONArray attendeesArray = new JSONArray(event.getString("attendees"));
                                        for (int j = 0; j < attendeesArray.length(); j++) {
                                            if (attendeesArray.getString(j).equals(USO.userId)) {
                                                amAttending = true;
                                                parsedEvent.setIsAttending(true);
                                            }
                                        }
                                    }
                                    if (getArguments().getString(ARG_LIST_TYPE, "").equals("attending")) {
                                        if (amAttending) {
                                            mAdapter.addItem(parsedEvent);
                                        }
                                    } else {
                                        mAdapter.addItem(parsedEvent);
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            Log.e("JSONException", e.getMessage());
                            e.printStackTrace();
                        }

                        break;
                    default:
                        break;
                }

                //attendees, jsonarray de userIds
            }
        };
    }

    private void registerBroadcastReceiver() {
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("ro.hd.speekup.EventsActivity"));
    }

    private void unregisterBroadcastReceiver() {
        if (broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }
}