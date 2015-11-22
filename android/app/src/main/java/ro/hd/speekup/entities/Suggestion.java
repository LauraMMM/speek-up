package ro.hd.speekup.entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Suggestion implements Serializable
{
    private String id = "";
    private String type = ""; //speak / move
    private String text = "";
    private String status = ""; //voting / winner
    private String eventId = "";
    private int yesCount = 0;
    private int noCount = 0;
    private boolean hasVoted = false;

    private int score = 0;

    public Suggestion() { }

    public Suggestion(JSONObject json) {
        try {
            if (json.has("id")) { this.id = json.getString("id"); }
            if (json.has("eventId")) { this.eventId = json.getString("eventId"); }
            if (json.has("type")) { this.type = json.getString("type"); }
            if (json.has("text")) { this.text = json.getString("text"); }
            if (json.has("status")) { this.status = json.getString("status"); }
            if (json.has("yesCount")) { this.yesCount = json.getInt("yesCount"); }
            if (json.has("noCount")) { this.noCount = json.getInt("noCount"); }
            if (json.has("voted")) {
                if (json.getString("voted").equals("yes")) {
                    this.hasVoted = true;
                }
            }
            this.score = this.yesCount - this.noCount;
        } catch (JSONException e) {

        }
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getStatus() {
        return status;
    }

    public int getYesCount() {
        return yesCount;
    }

    public int getNoCount() {
        return noCount;
    }

    public int getScore() {
        return score;
    }

    public boolean hasVoted() {
        return hasVoted;
    }

    public String getEventId() {
        return eventId;
    }

    public Suggestion setId(String id) {
        this.id = id;
        return this;
    }

    public Suggestion setType(String type) {
        this.type = type;
        return this;
    }

    public Suggestion setText(String text) {
        this.text = text;
        return this;
    }

    public Suggestion setStatus(String status) {
        this.status = status;
        return this;
    }

    public Suggestion setYesCount(int yesCount) {
        this.yesCount = yesCount;
        this.score = this.yesCount - this.noCount;
        return this;
    }

    public Suggestion setNoCount(int noCount) {
        this.noCount = noCount;
        this.score = this.yesCount - this.noCount;
        return this;
    }

    public Suggestion setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
        return this;
    }

    public Suggestion setEventId(String eventId) {
        this.eventId = eventId;
        return this;
    }

    /*public String toJson() {
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
    }*/

    public boolean equals(Suggestion suggestion) {
        return this.id.equals(suggestion.getId()) &&
                this.type.equals(suggestion.getType()) &&
                this.text.equals(suggestion.getText()) &&
                this.status.equals(suggestion.getStatus()) &&
                this.yesCount == suggestion.getYesCount() &&
                this.noCount == suggestion.getNoCount();
    }
}