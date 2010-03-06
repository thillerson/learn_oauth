package com.oreilly.android.otwitter.twitterapi;

import java.util.ArrayList;

import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

import com.oreilly.android.otwitter.resources.Status;

public class TwitterAPI {
	
	public ArrayList<Status> getPublicTimeline() {
		try {
			return Status.listFromJSON(get("http://api.twitter.com/1/statuses/public_timeline.json"));
		} catch (Exception e) {
			Log.e(getClass().getName(), "Error getting public timeline:", e);
		}
		return null;
	}

	private String get(String url) throws Exception {
		HttpGet get = new HttpGet(url);
		return execute(get);
	}

	private String execute(HttpRequestBase method) throws Exception {
		HttpContext localContext = new BasicHttpContext();
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		CookieStore cookieStore = new BasicCookieStore();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpProtocolParams.setContentCharset(httpClient.getParams(), "UTF-8");
		HttpProtocolParams.setHttpElementCharset(httpClient.getParams(), "UTF-8");
		
		String response = httpClient.execute(method, responseHandler, localContext);
		return response;
	}

}
