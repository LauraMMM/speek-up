package ro.hd.speekup.classes;

import ro.hd.speekup.BuildConfig;
import ro.hd.speekup.entities.ListEvent;
import ro.hd.speekup.entities.ServerRequest;
import ro.hd.speekup.entities.Suggestion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

public final class ApiManager {

    public static void checkServer(Context context, String owner) {
        String finalUrl = BuildConfig.SERVER_URL + "/";

        ServerRequest request = new ServerRequest(ServerActions.ACTION_CHECK_SERVER, finalUrl, owner, null);
        startApiManagerService(context, request);
    }

    public static void login(Context context, String userId, String userName, String avatarUrl, String owner) {
        String finalUrl = BuildConfig.SERVER_URL + "/userservice" + "/" + "login";

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("fbId", userId);
        parameters.put("fbName", userName);
        parameters.put("fbAvatar", avatarUrl);
        parameters.put("deviceId", PushManager.regid);

        ServerRequest request = new ServerRequest(ServerActions.ACTION_LOGIN, finalUrl, owner, parameters);
        startApiManagerService(context, request);
    }

    public static void registerEvent(Context context, ListEvent event, String owner) {
        String finalUrl = BuildConfig.SERVER_URL + "/eventservice" + "/" + "register";

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("userId", USO.userId);
        parameters.put("fbId", event.getId());
        parameters.put("fbTitle", event.getTitle());
        parameters.put("fbDescription", event.getDescription());
        parameters.put("fbAvatar", event.getAvatar());
        parameters.put("fbCover", event.getCover());
        parameters.put("fbStartTime", Long.toString(event.getStartTime()));
        parameters.put("fbEndTime", Long.toString(event.getEndTime()));
        parameters.put("fbLocationName", event.getLocationName());
        parameters.put("fbLatitude", event.getLatitude());
        parameters.put("fbLongitude", event.getLongitude());
        parameters.put("fbCount", Integer.toString(event.getAttendingCount()));

        ServerRequest request = new ServerRequest(ServerActions.ACTION_REGISTER_EVENT, finalUrl, owner, parameters);
        startApiManagerService(context, request);
    }

    public static void attendEvent(Context context, String eventId, String owner) {
        String finalUrl = BuildConfig.SERVER_URL + "/eventservice" + "/" + "attend";

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("userId", USO.userId);
        parameters.put("eventId", eventId);

        ServerRequest request = new ServerRequest(ServerActions.ACTION_ATTEND_EVENT, finalUrl, owner, parameters);
        startApiManagerService(context, request);
    }

    public static void unattendEvent(Context context, String eventId, String owner) {
        String finalUrl = BuildConfig.SERVER_URL + "/eventservice" + "/" + "unattend";

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("userId", USO.userId);
        parameters.put("eventId", eventId);

        ServerRequest request = new ServerRequest(ServerActions.ACTION_UNATTEND_EVENT, finalUrl, owner, parameters);
        startApiManagerService(context, request);
    }

    public static void getEvents(Context context, String owner) {
        String finalUrl = BuildConfig.SERVER_URL + "/eventservice" + "/" + "list";

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("userId", USO.userId);

        ServerRequest request = new ServerRequest(ServerActions.ACTION_GET_EVENTS, finalUrl, owner, parameters);
        startApiManagerService(context, request);
    }

    public static void getSuggestions(Context context, String eventId, String owner) {
        String finalUrl = BuildConfig.SERVER_URL + "/activityservice" + "/" + "list";

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("userId", USO.userId);
        parameters.put("eventId", eventId);

        ServerRequest request = new ServerRequest(ServerActions.ACTION_GET_SUGGESTIONS, finalUrl, owner, parameters);
        startApiManagerService(context, request);
    }

    public static void vote(Context context, Suggestion suggestion, boolean vote, String owner) {
        String finalUrl = BuildConfig.SERVER_URL + "/activityservice" + "/" + "vote";

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("userId", USO.userId);
        parameters.put("suggestionId", suggestion.getId());
        Log.e("sId", suggestion.getId());
        if (vote) {
            parameters.put("vote", "yes");
        } else {
            parameters.put("vote", "no");
        }

        ServerRequest request = new ServerRequest(ServerActions.ACTION_VOTE, finalUrl, owner, parameters);
        startApiManagerService(context, request);
    }

    public static void addSuggestion(Context context, String eventId, String type, String text, String owner) {
        String finalUrl = BuildConfig.SERVER_URL + "/activityservice" + "/" + "add";

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("userId", USO.userId);
        parameters.put("eventId", eventId);
        parameters.put("type", type);
        parameters.put("text", text);

        ServerRequest request = new ServerRequest(ServerActions.ACTION_VOTE, finalUrl, owner, parameters);
        startApiManagerService(context, request);
    }

    private static void startApiManagerService(Context context, ServerRequest request) {
        Intent intent = new Intent(context, ro.hd.speekup.services.ApiManagerService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("request", request);
        intent.putExtras(bundle);
        context.startService(intent);
    }
}