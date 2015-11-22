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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ro.hd.speekup.R;
import ro.hd.speekup.adapters.EventListAdapter;
import ro.hd.speekup.adapters.SuggestionAdapter;
import ro.hd.speekup.classes.ApiManager;
import ro.hd.speekup.classes.ServerActions;
import ro.hd.speekup.classes.USO;
import ro.hd.speekup.entities.ListEvent;
import ro.hd.speekup.entities.Suggestion;

public class SuggestionFragment extends Fragment {
    public static final String ARG_LIST_TYPE = "list_type";

    RecyclerView mRecyclerView;
    SuggestionAdapter mAdapter;

    private BroadcastReceiver broadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_suggestions, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_suggestions);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SimpleItemAnimator animator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(animator);

        mAdapter = new SuggestionAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        createBroadcastReceiver();

        //getEvents();

        return rootView;
    }

    @Override
    public void onResume() {
        registerBroadcastReceiver();
        //ApiManager.getSuggestions(getActivity(), getArguments().getString("eventId"), "SuggestionActivity");
        super.onResume();
    }

    @Override
    public void onPause() {
        unregisterBroadcastReceiver();
        super.onPause();
    }

    private void createBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String type = intent.getStringExtra("type");
                String response = intent.getStringExtra("response");
                Log.e("broadcast", "-" + response);
                switch (type) {
                    case ServerActions.ACTION_GET_SUGGESTIONS:
                        mAdapter.removeAll();
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            if (responseJson.has("data")) {
                                JSONArray dataArray = new JSONArray(responseJson.getString("data"));
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject suggestionJson = dataArray.getJSONObject(i);
                                    Suggestion parsedSuggestion = new Suggestion(suggestionJson);
                                    if (getArguments().getString(ARG_LIST_TYPE, "").equals(parsedSuggestion.getType())) {
                                        mAdapter.addItem(parsedSuggestion);
                                    }
                                    Log.e("suggestion", suggestionJson.toString());
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
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("ro.hd.speekup.SuggestionActivity"));
    }

    private void unregisterBroadcastReceiver() {
        if (broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }
}