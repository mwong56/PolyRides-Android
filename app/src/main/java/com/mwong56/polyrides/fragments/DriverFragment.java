package com.mwong56.polyrides.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.MainActivity;
import com.mwong56.polyrides.activities.NewRideActivity;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.views.StartEndLayout;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by micha on 10/9/2015.
 */
public class DriverFragment extends RxFragment {

  @Bind(R.id.start_from_layout)
  StartEndLayout startEndLayout;

  private MainActivity activity;

  public static DriverFragment newInstance() {
    return new DriverFragment();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    startEndLayout.setup(activity.getGoogleApiClient(), this);

    if (savedInstanceState != null) {
      Location[] locations = (Location[]) savedInstanceState.getParcelableArray("locations");
      if (locations[0] != null) {
        startEndLayout.setStartLocation(locations[0]);
      }

      if (locations[1] != null) {
        startEndLayout.setEndLocation(locations[1]);
      }
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Location[] locations = startEndLayout.getPlaces();
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


  @OnClick(R.id.new_ride_button)
  void newRide() {
    Location[] locations = startEndLayout.getPlaces();
    if (locations[0] != null && locations[1] != null) {
      Intent i = new Intent(getActivity(), NewRideActivity.class);
      i.putExtra("start", locations[0]);
      i.putExtra("end", locations[1]);
      startActivity(i);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      startEndLayout.onActivityResultCalled(requestCode, data);
    }
  }
}
