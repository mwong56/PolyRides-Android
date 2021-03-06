package com.mwong56.polyrides.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.FindRideActivity;
import com.mwong56.polyrides.activities.MainActivity;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.views.StartEndView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by micha on 10/9/2015.
 */
public class PassengerFragment extends BaseRxFragment implements StartEndView.StartEndListener {

  @Bind(R.id.start_from_view)
  StartEndView startEndView;

  @Bind(R.id.find_ride_button)
  Button findRideButton;

  public static PassengerFragment newInstance() {
    return new PassengerFragment();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    startEndView.setup(this);
    startEndView.setListener(this);
    startEndView.setNextButtonTitle("Find Ride");

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

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_passenger, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onNext(Location start, Location end) {
    Intent i = new Intent(getActivity(), FindRideActivity.class);
    i.putExtra("start", start);
    i.putExtra("end", end);
    startActivity(i);
  }
}
