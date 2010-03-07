package com.oreilly.android.otweet.activities;

import com.oreilly.android.otweet.OTweetApplication;
import com.oreilly.android.otweet.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class AuthorizationActivity extends Activity {

  private WebView webView;
  private Button authorizeButton;
  private EditText pinText;
  private OTweetApplication app;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (OTweetApplication)getApplication();
    setContentView(R.layout.authorization_view);
    setUpViews();
    webView.loadUrl(app.getAuthorizationURL());
  }
  
  public void authorizeButtonClicked(View v) {
    //blaa
  }

  private void setUpViews() {
    pinText = (EditText)findViewById(R.id.enter_pin_text);
    authorizeButton = (Button)findViewById(R.id.authorize_button);
    webView = (WebView)findViewById(R.id.web_view);
  }

}
