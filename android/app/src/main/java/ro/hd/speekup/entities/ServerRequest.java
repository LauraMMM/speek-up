package ro.hd.speekup.entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.HashMap;

public class ServerRequest implements Serializable
{
    private String type;
    private String url;
    private String owner;
    private boolean inProgress = false;

    //private JSONObject parameters = new JSONObject();
    private HashMap<String, String> parameters;
    private long id = 0;

    public ServerRequest() { }

    public ServerRequest(String type, String url) {
        setType(type);
        setUrl(url);
    }

    public ServerRequest(String type, String url, String owner, HashMap<String, String> parameters) {
        setUrl(url);
        setType(type);
        setOwner(owner);
        if (parameters != null) {
            setParameters(parameters);
        } else {
            setParameters(new HashMap<String, String>());
        }
    }

    public ServerRequest(String type, String url, String owner, HashMap<String, String> parameters, long id) {
        setUrl(url);
        setType(type);
        setOwner(owner);
        setId(id);
        if (parameters != null) {
            setParameters(parameters);
        } else {
            setParameters(new HashMap<String, String>());
        }
    }

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) { this.owner = owner; }

    public HashMap<String, String> getParameters() {
        return this.parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }

    public void setId(long id) { this.id = id; }

    public long getId() { return this.id; }

}