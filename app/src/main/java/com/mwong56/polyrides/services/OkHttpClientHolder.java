package com.mwong56.polyrides.services;

import com.squareup.okhttp.OkHttpClient;

/**
 * Created by micha on 12/5/2015.
 */
public class OkHttpClientHolder {
  private static final OkHttpClient client = new OkHttpClient();

  private OkHttpClientHolder() {}

  public static OkHttpClient getClient() {
    return client;
  }
}
