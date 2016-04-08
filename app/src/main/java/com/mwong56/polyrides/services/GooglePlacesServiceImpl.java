package com.mwong56.polyrides.services;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by micha on 12/5/2015.
 */
public class GooglePlacesServiceImpl implements GooglePlacesService {
  private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
  private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
  private static final String OUT_JSON = "/json";
  private static final String WEB_API_KEY = "AIzaSyDPJT0KIoQPM5ctM5PtJm0dE9604d2RmTA";
  private static final OkHttpClient client = OkHttpClientHolder.getClient();

  private static class SingletonHolder {
    private static final GooglePlacesServiceImpl INSTANCE = new GooglePlacesServiceImpl();
  }

  public static GooglePlacesServiceImpl get() {
    return SingletonHolder.INSTANCE;
  }

  private GooglePlacesServiceImpl() {
  }

  @Override
  public ArrayList<AutoCompleteResult> getAutoComplete(String input) {
    ArrayList<AutoCompleteResult> toReturn = new ArrayList<>();
    StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
    sb.append("?key=" + WEB_API_KEY);
    sb.append("&components=country:us");
    sb.append("&input=" + input);
    Request request = new Request.Builder()
        .url(sb.toString())
        .build();

    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        return toReturn;
      }

      String jsonString = response.body().string();
      JSONObject jsonObject = new JSONObject(jsonString);
      JSONArray predsJsonArray = jsonObject.getJSONArray("predictions");
      for (int i = 0; i < predsJsonArray.length(); i++) {
        JSONObject object = predsJsonArray.getJSONObject(i);
        String fullText = object.getString("description");
        String placeId = object.getString("place_id");
        toReturn.add(new AutoCompleteResult(fullText, placeId));
      }
      return toReturn;
    } catch (Exception e) {
      return toReturn;
    }
  }

  @Override
  public Observable<ArrayList<AutoCompleteResult>> getAutoCompleteAsync(final String input) {
    return Observable.fromCallable(() -> getAutoComplete(input))
        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }
}
