package com.oreilly.android.otwitter.resources;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class Status {

  private long   id;
  private String text;
  private User   user;

  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public String toString() {
    return String.format("%s: %s", user.getScreenName(), text);
  }

  public static ArrayList<Status> listFromJSON(String json) {
    JSONObject jsonObject;
    ArrayList<Status> statii = new ArrayList<Status>();
    try {
      JSONArray jsonArray = new JSONArray(json);
      for (int i = 0; i < jsonArray.length(); i++) {
        jsonObject = jsonArray.getJSONObject(i);
        statii.add(Status.fromJSON(jsonObject));
      }
      return statii;
    } catch (Exception e) {
      Log.e(Status.class.getName(), "Error deserializing:", e);
    }
    return null;
  }

  public static Status fromJSON(JSONObject jsonObject) {
    if (null == jsonObject) {
      return null;
    }

    try {
      Status status = new Status();
      status.id = jsonObject.getLong("id");
      status.text = jsonObject.getString("text");
      status.user = User.fromJSON(jsonObject.getJSONObject("user"));
      return status;
    } catch (Exception e) {
      Log.e(Status.class.getName(), "Error deserializing:", e);
    }
    return null;
  }

}
