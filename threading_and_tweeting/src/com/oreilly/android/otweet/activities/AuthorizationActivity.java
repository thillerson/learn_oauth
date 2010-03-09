package com.oreilly.android.otweet.activities;

import com.oreilly.android.otweet.OTweetApplication;
import com.oreilly.android.otweet.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

public class AuthorizationActivity extends Activity {

  private OTweetApplication app;
  private EditText pinText;
  private WebView webView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (OTweetApplication)getApplication();
    setContentView(R.layout.authorization_view);
    setUpViews();
    String authURL = app.beginAuthorization();
    webView.loadUrl(authURL);
  }
  
  public void authorizeButtonClicked(View v) {
    String pin = pinText.getText().toString();
    boolean success = app.authorize(pin);
    if (success) {
      finish();
    } else {
      //TODO: respond to bad pin, etc
    }
  }

  private void setUpViews() {
    pinText = (EditText)findViewById(R.id.enter_pin_text);
    webView = (WebView)findViewById(R.id.web_view);
  }

}
