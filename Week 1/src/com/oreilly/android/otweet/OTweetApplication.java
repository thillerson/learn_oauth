package com.oreilly.android.otweet;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;
import android.app.Application;

public class OTweetApplication extends Application {

  private Twitter twitter;

  @Override
  public void onCreate() {
    super.onCreate();
    twitter = new TwitterFactory().getInstance();
    twitter.setOAuthConsumer("YptX556D6gCE4qi1qG4rQ", "QkG087N89rfU6s7ghjkdNYTaMpI2OqzOlsXGggp00");
    // if we have authorization token, set it here:
    //twitter.setOAuthAccessToken(loadAccessToken());
  }

  public Twitter getTwitter() {
    return twitter;
  }

  public boolean isAuthorized() {
    // loading saved access token will tell us if we're authorized or not
    return false;
  }
  
  public String getAuthorizationURL() {
    try {
      RequestToken requestToken = twitter.getOAuthRequestToken();
      return requestToken.getAuthorizationURL();
    } catch (TwitterException e) {
      e.printStackTrace();
    }
    return null;
  }

  private AccessToken loadAccessToken() {
    return null;
  }

}
