package com.mwong56.polyrides.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.MainActivity;
import com.mwong56.polyrides.activities.NewRideActivity;
import com.mwong56.polyrides.views.PlacesAutoComplete;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by micha on 10/9/2015.
 */
public class DriverFragment extends Fragment {

  @Bind(R.id.from)
  PlacesAutoComplete fromEditText;

  @Bind(R.id.start)
  PlacesAutoComplete startEditText;

  GoogleApiClient apiClient;

  public static DriverFragment newInstance() {
    return new DriverFragment();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    apiClient = new GoogleApiClient.Builder(getActivity())
        .enableAutoManage(getActivity(), 0, (MainActivity) getActivity())
            .addApi(Places.GEO_DATA_API)
            .build();

    fromEditText.setup(apiClient);
    startEditText.setup(apiClient);

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
