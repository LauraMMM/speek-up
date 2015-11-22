package ro.hd.speekup.entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ListEvent implements Serializable
{
    private String id = "";
    private String title = "";
    private String description = "";
    private String avatar = "";
    private String cover = "";
    private long startTime = 0;
    private long endTime = 0;
    private String locationName = "";
    private String latitude = "";
    private String longitude = "";
    private int attendingCount = 0;
    private int localCount = 0;

    private boolean isAttending = false;


    public ListEvent() { }

    public ListEvent(JSONObject json) {
        try {
            if (json.has("id")) {
                this.id = json.getString("id");
            }
            if (json.has("fbTitle")) {
                this.title = json.getString("fbTitle");
            }
            if (json.has("fbDescription")) {
                this.description = json.getString("fbDescription");
            }
            if (json.has("fbAvatar")) {
                this.avatar = json.getString("fbAvatar");
            }
            if (json.has("fbCover")) {
                this.cover = json.getString("fbCover");
            }
            if (json.has("fbStartTime")) {
                this.startTime = json.getLong("fbStartTime");
            }
            if (json.has("fbEndTime")) {
                this.endTime = json.getLong("fbEndTime");
            }
            if (json.has("fbLocationName")) {
                this.locationName = json.getString("fbLocationName");
            }
            if (json.has("fbLatitude")) {
                this.latitude = json.getString("fbLatitude");
            }
            if (json.has("fbLongitude")) {
                this.longitude = json.getString("fbLongitude");
            }
            if (json.has("fbCount")) {
                this.attendingCount = json.getInt("fbCount");
            }
            if (json.has("localCount")) {
                this.localCount = json.getInt("localCount");
            }
        } catch (JSONException e) {

        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getCover() {
        return cover;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getAttendingCount() {
        return attendingCount;
    }

    public int getLocalCount() {
        return localCount;
    }

    public boolean isAttending() {
        return isAttending;
    }

    public ListEvent setId(String id) {
        this.id = id;
        return this;
    }

    public ListEvent setTitle(String title) {
        this.title = title;
        return this;
    }

    public ListEvent setDescription(String description) {
        this.description = description;
        return this;
    }

    public ListEvent setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public ListEvent setCover(String cover) {
        this.cover = cover;
        return this;
    }

    public ListEvent setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    public ListEvent setEndTime(long endTime) {
        this.endTime = endTime;
        return this;
    }

    public ListEvent setLocationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public ListEvent setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public ListEvent setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public ListEvent setAttendingCount(int attendingCount) {
        this.attendingCount = attendingCount;
        return this;
    }

    public ListEvent setLocalCount(int localCount) {
        this.localCount = localCount;
        return this;
    }

    public ListEvent setIsAttending(boolean isAttending) {
        this.isAttending = isAttending;
        return this;
    }

    public String toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("fbid", this.id);
            json.put("fbTitle", this.title);
            json.put("fbDescription", this.description);
            json.put("fbAvatar", this.avatar);
            json.put("fbCover", this.cover);
            json.put("fbStartTime", this.startTime);
            json.put("fbEndTime", this.endTime);
            json.put("fbLocationName", this.locationName);
            json.put("fbLatitude", this.latitude);
            json.put("fbLongitude", this.longitude);
            json.put("fbCount", this.attendingCount);
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
            e.printStackTrace();
        }
        return json.toString();
    }

    public boolean equals(ListEvent event) {
        return this.id.equals(event.getId()) &&
                this.title.equals(event.getTitle()) &&
                this.description.equals(event.getDescription()) &&
                this.avatar.equals(event.getAvatar()) &&
                this.cover.equals(event.getCover()) &&
                this.locationName.equals(event.getLocationName());
    }
}