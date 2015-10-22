package com.mwong56.polyrides.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.FindRideActivity;
import com.mwong56.polyrides.activities.MainActivity;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.views.StartEndView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by micha on 10/9/2015.
 */
public class PassengerFragment extends Fragment {

  @Bind(R.id.start_from_view)
  StartEndView startEndView;

  private MainActivity activity;


  public static PassengerFragment newInstance() {
    return new PassengerFragment();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    startEndView.setup(activity.getGoogleApiClient(), this);

    if (savedInstanceState != null) {
      Location[] locations = (Location[]) savedInstanceState.getParcelableArray("locations");
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
    View view = inflater.inflate(R.layout.fragment_passenger, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @OnClick(R.id.find_ride_button)
  void findRide() {
    Location[] locations = startEndView.getPlaces();
    if (locations[0] != null && locations[1] != null) {
      Intent i = new Intent(getActivity(), FindRideActivity.class);
      i.putExtra("start", locations[0]);
      i.putExtra("end", locations[1]);
      startActivity(i);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      startEndView.onActivityResultCalled(requestCode, data);
    }
  }
}
