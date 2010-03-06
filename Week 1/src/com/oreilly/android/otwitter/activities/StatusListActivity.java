package com.oreilly.android.otwitter.activities;

import java.util.ArrayList;

import com.oreilly.android.otwitter.resources.Status;
import com.oreilly.android.otwitter.twitterapi.TwitterAPI;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class StatusListActivity extends ListActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TwitterAPI api = new TwitterAPI();
    StatusListAdapter adapter = new StatusListAdapter(this, api.getPublicTimeline());
    setListAdapter(adapter);
  }

  private class StatusListAdapter extends ArrayAdapter<Status> {

    public StatusListAdapter(Context context, ArrayList<Status> statii) {
      super(context, android.R.layout.simple_list_item_1, statii);
    }

  }
}