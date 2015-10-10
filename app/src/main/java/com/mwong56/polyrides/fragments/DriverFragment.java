package com.mwong56.polyrides.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.NewRideActivity;
import com.mwong56.polyrides.services.LocationService;
import com.mwong56.polyrides.services.LocationServiceSingleton;
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

  private LocationService locationService = LocationServiceSingleton.instance();

  public static DriverFragment newInstance() {
    return new DriverFragment();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);


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
    Intent i = new Intent(getActivity(), NewRideActivity.class);
    startActivity(i);
  }

}
