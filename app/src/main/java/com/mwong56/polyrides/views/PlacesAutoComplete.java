package com.mwong56.polyrides.views;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mwong56.polyrides.adapters.PlacesAutoCompleteAdapter;

/**
 * Created by micha on 10/9/2015.
 */
public class PlacesAutoComplete extends AppCompatAutoCompleteTextView {

  private static final String TAG = PlacesAutoComplete.class.getSimpleName();
  private GoogleApiClient client;
  private PlacesAutoCompleteAdapter adapter;
  private Place place;

  private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
      new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

  public PlacesAutoComplete(Context context) {
    super(context);
  }

  public PlacesAutoComplete(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public PlacesAutoComplete(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setup(GoogleApiClient client) {
    this.client = client;
    //TODO: grab user location instead of passing predefined.
    adapter = new PlacesAutoCompleteAdapter(getContext(), client, BOUNDS_GREATER_SYDNEY, null);
    this.setAdapter(adapter);
    this.setOnItemClickListener((parent, view, position, id) -> {
      final AutocompletePrediction item = adapter.getItem(position);
      final String placeId = item.getPlaceId();
      final CharSequence primaryText = item.getPrimaryText(null);

      PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
          .getPlaceById(client, placeId);
      placeResult.setResultCallback(places -> {
        if (!places.getStatus().isSuccess()) {
          Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
          places.release();
          return;
        }
        final Place place1 = places.get(0);
        Log.i(TAG, "Place details received: " + place1.getName());
        PlacesAutoComplete.this.place = place1;
        places.release();
      });

      Toast.makeText(getContext(), "Clicked: " + primaryText,
          Toast.LENGTH_SHORT).show();
      Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
    });
    this.setOnKeyListener((v, keyCode, event) -> {
      if (PlacesAutoComplete.this.place != null) {
        PlacesAutoComplete.this.place = null;
        Toast.makeText(getContext(), "Resetted place",
            Toast.LENGTH_SHORT).show();
      }
      return false;
    });
  }

  public Place getPlace() {
    return this.place;
  }

  public void setPlace(Place place) {
    this.place = place;
    setText(place.getAddress());
  }
}
