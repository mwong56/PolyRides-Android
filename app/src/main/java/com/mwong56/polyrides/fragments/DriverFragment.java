package com.mwong56.polyrides.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;
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

  MainActivity activity;

  public static DriverFragment newInstance() {
    return new DriverFragment();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    startEndLayout.setGoogleApiClient(activity.getGoogleApiClient());
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
    Place[] places = startEndLayout.getPlaces();
    if (places[0] != null && places[1] != null) {
      Intent i = new Intent(getActivity(), NewRideActivity.class);
      i.putExtra("start", new Location(places[0]));
      i.putExtra("end", new Location(places[1]));
      startActivity(i);
    }

  }

}
