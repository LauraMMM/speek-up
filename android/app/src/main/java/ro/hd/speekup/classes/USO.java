package ro.hd.speekup.classes;

import android.content.Context;
import android.content.SharedPreferences;

public final class USO {
    public static String userId = "";
    //used to check if the notification should or should not be displayed
    private static String foregroundActivity = "";
    private static String foregroundActivityId = "";

    public static boolean isSet() {
        return !userId.isEmpty();
    }

    public static void reset() {
        userId = "";
    }


    public static void getFromPermanentStorage(Context context) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            userId = prefs.getString("userId", "");
        }
    }

    public static void saveToPermanentStorage(Context context) {
        if (context != null) {
            SharedPreferences prefs;
            prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("userId", userId);
            editor.apply();
        }
    }

    public static void setForegroundActivity(String newForegroundActivity) {
        foregroundActivity = newForegroundActivity;
    }

    public static String getForegroundActivity() {
        return foregroundActivity;
    }

    public static void setForegroundActivityId(String newForegroundActivityId) {
        foregroundActivityId = newForegroundActivityId;
    }

    public static String getForegroundActivityId() {
        return foregroundActivityId;
    }


}