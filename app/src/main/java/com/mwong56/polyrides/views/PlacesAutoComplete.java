package com.mwong56.polyrides.views;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mwong56.polyrides.activities.BaseRxActivity;
import com.mwong56.polyrides.adapters.PlacesAutoCompleteAdapter;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.utils.Utils;

import java.util.Locale;

/**
 * Created by micha on 10/9/2015.
 */
public class PlacesAutoComplete extends AppCompatAutoCompleteTextView {

  private static final String TAG = PlacesAutoComplete.class.getSimpleName();
  private PlacesAutoCompleteAdapter adapter;
  private Location location;
  private boolean watchText;
  private BaseRxActivity activity;

  private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
      new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

  private static final LatLngBounds BOUNDS_TEST = new LatLngBounds(new LatLng(32.6393, -117.004304), new LatLng(44.901184, -67.32254));

  public PlacesAutoComplete(Context context) {
    super(context);
  }

  public PlacesAutoComplete(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public PlacesAutoComplete(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setup(final GoogleApiClient client, final BaseRxActivity activity) {
    adapter = new PlacesAutoCompleteAdapter(getContext(), client, BOUNDS_TEST, null);
    this.setAdapter(adapter);
    this.activity = activity;

    this.setOnItemClickListener((parent, view, position, id) -> {
      Utils.hideKeyboard(activity);
      final AutocompletePrediction item = adapter.getItem(position);
      final String placeId = item.getPlaceId();

      PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
          .getPlaceById(client, placeId);
      placeResult.setResultCallback(places -> {
        if (!places.getStatus().isSuccess()) {
          Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
          places.release();
          return;
        }
        final Place place1 = places.get(0);
        if (isUsa(place1)) {
          setLocation(new Location(place1, getContext()));
        } else {
          activity.showToast("Place must be in USA.");
          setText("");
        }
        places.release();
      });
    });

    this.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isPerformingCompletion()) {
          return;
        }

        //User typed, reset the saved location.
        if (PlacesAutoComplete.this.location != null && watchText) {
          PlacesAutoComplete.this.location = null;
          enableSuggestions(true);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
      }
    });
  }

  private boolean isUsa(Place place) {
    Geocoder geocoder = new Geocoder(getContext(), Locale.ENGLISH);
    if (geocoder != null) {
      LatLng temp = place.getLatLng();
      Address address;
      try {
        address = geocoder.getFromLocation(temp.latitude, temp.longitude, 1).get(0);
        if (!address.getCountryCode().equals(Locale.US.getCountry())) {
          return false;
        }
      } catch (Exception e) {
        activity.showToast("Geocoder not found");
      }
    }
    return true;
  }

  public Location getLocation() {
    return this.location;
  }

  public void setLocation(Location location) {
    watchText = false;
    this.location = location;
    enableSuggestions(false);
    this.post(() -> {
      setText(location.getAddress());
      watchText = true;
    });
  }

  private void enableSuggestions(boolean enabled) {
    setThreshold(enabled ? 1 : 1000);

    if (!enabled) {
      this.clearFocus();
    }
  }

}