package com.oreilly.android.otweet.settings;

import twitter4j.Twitter;
import twitter4j.http.AccessToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class OAuthHelper {

  private static final String APPLICATION_PREFERENCES = "app_prefs";
  private static final String AUTH_KEY = "auth_key";
  private static final String AUTH_SEKRET_KEY = "auth_secret_key";
  private SharedPreferences prefs;
  private AccessToken accessToken;

  public OAuthHelper(Context context) {
    prefs = context.getSharedPreferences(APPLICATION_PREFERENCES, Context.MODE_PRIVATE);
    accessToken = loadAccessToken();
  }

  public void configureOAuth(Twitter twitter) {
    twitter.setOAuthConsumer(getConsumerKey(), getConsumerSekretKey());
    twitter.setOAuthAccessToken(accessToken);
  }

  public boolean hasAccessToken() {
    return accessToken != null;
  }

  public void storeAccessToken(AccessToken accessToken) {
    Editor editor = prefs.edit();
    editor.putString(AUTH_KEY, accessToken.getToken());
    editor.putString(AUTH_SEKRET_KEY, accessToken.getTokenSecret());
    editor.commit();
    this.accessToken = accessToken;
  }

  private String getConsumerKey() {
    return "YptX556D6gCE4qi1qG4rQ";
  }

  private String getConsumerSekretKey() {
    return "QkG087N89rfU6s7ghjkdNYTaMpI2OqzOlsXGggp00";
  }

  private AccessToken loadAccessToken() {
    String token = prefs.getString(AUTH_KEY, null);
    String tokenSecret = prefs.getString(AUTH_SEKRET_KEY, null);
    if (null != token && null != tokenSecret) {
      return new AccessToken(token, tokenSecret);
    } else {
      return null;
    }
  }

}
