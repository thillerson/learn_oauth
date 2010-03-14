package com.oreilly.android.otweet.layouts;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import twitter4j.User;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ImageLoadHelper {
  
  private static final int IO_BUFFER_SIZE = 1024;

  public Drawable getUserAvatarDrawable(User user) { 
    Drawable d = null;
    try {
      d = Drawable.createFromStream(user.getProfileImageURL().openStream(), user.getName());
    } catch (IOException e) {
      Log.e(getClass().getName(), "Could not load image.", e);
    }
    return d;
  }
  
  public Bitmap getUserAvatarBitmap(User user) {
    Bitmap bitmap = null;
    InputStream in = null;
    OutputStream out = null;
    
    try {
      in = new BufferedInputStream(user.getProfileImageURL().openStream(), IO_BUFFER_SIZE);

      final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
      out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
      byte[] b = new byte[IO_BUFFER_SIZE];
      int read;
      while ((read = in.read(b)) != -1) {
        out.write(b, 0, read);
      }
      out.flush();

      final byte[] data = dataStream.toByteArray();
      bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
    } catch (IOException e) {
      Log.e(getClass().getName(), "Could not load image.", e);
    } finally {
      try {
        if (in != null) {
          in.close();
        }
        if (out != null) {
          out.close();
        }
      } catch (IOException ioe) {
        Log.e(getClass().getName(), "Couldn't close stream after trying to load photo.", ioe);
      }
    }

    Log.d(getClass().getName(), "Loaded image for " + user.getName());
    return bitmap;
  }

}
