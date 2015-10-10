package com.mwong56.polyrides.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
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
public class PlacesAutoComplete extends AutoCompleteTextView {

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
    this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final AutocompletePrediction item = adapter.getItem(position);
        final String placeId = item.getPlaceId();
        final CharSequence primaryText = item.getPrimaryText(null);

        Log.i(TAG, "Autocomplete item selected: " + primaryText);

        /*
         Issue a request to the Places Geo Data API to retrieve a Place object with additional
         details about the place.
          */
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
            .getPlaceById(client, placeId);
        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
          @Override
          public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
              // Request did not complete successfully
              Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
              places.release();
              return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            Log.i(TAG, "Place details received: " + place.getName());
            PlacesAutoComplete.this.place = place;
            places.release();
          }
        });

        Toast.makeText(getContext(), "Clicked: " + primaryText,
            Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
      }
    });
    this.setOnKeyListener(new OnKeyListener() {

      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        PlacesAutoComplete.this.place = null;
        Toast.makeText(getContext(), "Resetted place",
            Toast.LENGTH_SHORT).show();
        return false;
      }
    });
  }

  public Place getPlace() {
    return this.place;
  }
}
