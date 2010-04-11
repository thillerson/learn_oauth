package com.oreilly.android.otweet.activities;

import twitter4j.Status;

import com.harrison.lee.twitpic4j.TwitPic;
import com.harrison.lee.twitpic4j.TwitPicResponse;
import com.oreilly.android.otweet.OTweetApplication;
import com.oreilly.android.otweet.R;
import com.oreilly.android.otweet.tasks.PostPhotoAsyncTask;
import com.oreilly.android.otweet.tasks.PostTweetAsyncTask;
import com.oreilly.android.otweet.tasks.PostPhotoAsyncTask.PostPhotoResponder;
import com.oreilly.android.otweet.tasks.PostTweetAsyncTask.PostTweetResponder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PostActivity extends Activity implements PostTweetResponder, PostPhotoResponder {

  private static final int REQUEST_CHOOSE_PHOTO_FROM_LIBRARY = 0;

  private OTweetApplication app;
  private TextView counterText;
  private EditText tweetContent;
  private AlertDialog alertDialog;
  private ProgressDialog progressDialog;
  private Button photoButton;

  private Uri photoURI;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    app = (OTweetApplication)getApplication();
    setContentView(R.layout.post_view);
    setUpViews();
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (REQUEST_CHOOSE_PHOTO_FROM_LIBRARY == requestCode) {
      photoToPostChosenFromLibrary(intent.getData());
    } else {
      super.onActivityResult(requestCode, resultCode, intent);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    configurePhotoButton();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return MenuHelper.openActivityFromMenuItem(this, item);
  }
  
  protected void photoToPostChosenFromLibrary(Uri uri) {
    photoURI = uri;
    String id = photoURI.getLastPathSegment();
    if (null != id) {
      photoButton.setText(null);
      Bitmap thumbBitmap = MediaStore.Images.Thumbnails.getThumbnail(
          getContentResolver(),
          Long.parseLong(id),
          MediaStore.Images.Thumbnails.MICRO_KIND,
          null);
      photoButton.setBackgroundDrawable(new BitmapDrawable(thumbBitmap));
    }
  }
  
  public void photoPosting() {
    tweetPosting();
  }
  
  public void tweetPosting() {
    progressDialog = ProgressDialog.show(
        this,
        getResources().getString(R.string.posting_title),
        getResources().getString(R.string.posting_description)
      );
  }

  public void tweetPosted(Status tweet) {
    progressDialog.dismiss();
    Toast.makeText(this, R.string.tweet_posted, Toast.LENGTH_LONG).show();
    finish();
  }

  public void photoPosted(TwitPicResponse result) {
    tweetPosted(null);
  }

  private void postValidTweetOrWarn() {
    String postText = tweetContent.getText().toString();
    int postLength = postText.length();
    if (140 < postLength) {
      alertDialog = new AlertDialog.Builder(this).
        setTitle(R.string.too_many_characters).
        setMessage(R.string.too_many_characters_description).
        setPositiveButton(android.R.string.ok, new OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            alertDialog.dismiss(); alertDialog = null;
          }
        }).
        show();
    } else if (0 == postLength) {
      alertDialog = new AlertDialog.Builder(this).
      setTitle(R.string.tweet_is_blank).
      setMessage(R.string.blank_tweet_description).
      setPositiveButton(android.R.string.ok, new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          alertDialog.dismiss(); alertDialog = null;
        }
      }).
      show();
    } else {
      if (null == photoURI) {
        new PostTweetAsyncTask(this, app.getTwitter()).execute(postText);
      } else {
        TwitPic twitpic = new TwitPic(app.getTwitPicUsername(), app.getTwitPicPassword());
        new PostPhotoAsyncTask(this, this, twitpic).execute(postText, photoURI);
      }
    }
  }

  // called when post button on view is clicked
  public void postButtonClicked(View view) {
    postValidTweetOrWarn();
  }
  
  protected void openCamera() {
    
  }

  protected void openPhotoLibrary() {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
    intent.setType("image/*");
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    startActivityForResult(intent, REQUEST_CHOOSE_PHOTO_FROM_LIBRARY);
  }

  // called when post button on view is clicked
  public void photoButtonClicked(View view) {
    if (app.hasTwitPicCredentials()) {
      new AlertDialog.Builder(this).
        setTitle("Attach Photo").
        setMessage("Choose a Photo Source").
        setPositiveButton("Camera", cameraButtonClickListener).
        setNeutralButton("Photo Library", libraryButtonClickListener).
        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        }).
        show();
    } else {
      Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
    }
  }
  
  private void configurePhotoButton() {
    if (null != photoURI) {
      photoButton.setText(null);
    } else if (app.hasTwitPicCredentials()) {
      photoButton.setText(R.string.attach_photo);
    } else {
      photoButton.setText(R.string.sign_in_to_twitpic);
    }
  }

  private void setUpViews() {
    counterText = (TextView)findViewById(R.id.counter_text);
    photoButton = (Button)findViewById(R.id.photo_button);
    configurePhotoButton();
    tweetContent = (EditText)findViewById(R.id.tweet_contents);
    tweetContent.addTextChangedListener(new TextWatcher() {
      public void afterTextChanged(Editable text) { }
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

      public void onTextChanged(CharSequence s, int start, int before, int count) {
        int charsLeft = 140 - s.length();
        counterText.setText(String.valueOf(charsLeft));
      }
    });
  }
  
  private OnClickListener cameraButtonClickListener = new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int which) {
      openCamera();
    }
  };
  private OnClickListener libraryButtonClickListener = new DialogInterface.OnClickListener() {
    public void onClick(DialogInterface dialog, int which) {
      openPhotoLibrary();
    }
  };

}
