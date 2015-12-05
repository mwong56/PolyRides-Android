package com.mwong56.polyrides.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.MainActivity;
import com.mwong56.polyrides.activities.NewRideActivity;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.services.GooglePlacesService;
import com.mwong56.polyrides.services.GooglePlacesServiceImpl;
import com.mwong56.polyrides.views.StartEndView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by micha on 10/9/2015.
 */
public class DriverFragment extends BaseRxFragment {
  private static final String TAG = DriverFragment.TAG;

  @Bind(R.id.start_from_view)
  StartEndView startEndView;

  @Bind(R.id.new_ride_button)
  Button newRideButton;

  private MainActivity activity;
  private GooglePlacesService placesService = GooglePlacesServiceImpl.get();

  public static DriverFragment newInstance() {
    return new DriverFragment();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    startEndView.setup(activity, activity.getGoogleApiClient(), this);

    if (savedInstanceState != null) {
      Parcelable[] parcelables = savedInstanceState.getParcelableArray("locations");
      Location[] locations = new Location[]{(Location) parcelables[0], (Location) parcelables[1]};

      if (locations[0] != null) {
        startEndView.setStartLocation(locations[0]);
      }

      if (locations[1] != null) {
        startEndView.setEndLocation(locations[1]);
      }
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Location[] locations = startEndView.getPlaces();
    outState.putParcelableArray("locations", locations);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    this.activity = (MainActivity) activity;
  }

  @Override
  public void onDetach() {
    this.activity = null;
    super.onDetach();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_driver, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  private void findAddress(String address, boolean start) {
    placesService.getAutoCompleteAsync(address)
        .compose(bindToLifecycle())
        .subscribe(predictions -> {
          if (predictions.size() > 0) {
            final String placeId = predictions.get(0).getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(activity.getGoogleApiClient(), placeId);
            placeResult.setResultCallback(places -> {
              if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
              }
              final Place place1 = places.get(0);
              if (start) {
                startEndView.setStartLocation(new Location(place1, getContext()));
              } else {
                startEndView.setEndLocation(new Location(place1, getContext()));
              }
              newRide();
              places.release();
            });
          } else {
            showToast("The address you entered is invalid.");
          }
        }, this::showToast);
  }


  @OnClick(R.id.new_ride_button)
  void newRide() {
    Location[] locations = startEndView.getPlaces();

    if (locations[0] == null) {
      findAddress(startEndView.getStartText(), true);
    }

    if (locations[1] == null) {
      findAddress(startEndView.getEndText(), false);
    }

    if (locations[0] != null && locations[1] != null) {
      Intent i = new Intent(getActivity(), NewRideActivity.class);
      i.putExtra("start", locations[0]);
      i.putExtra("end", locations[1]);
      startActivity(i);
    }
  }
}
