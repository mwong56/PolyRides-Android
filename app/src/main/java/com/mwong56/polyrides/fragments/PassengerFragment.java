package com.mwong56.polyrides.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.views.StartEndLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by micha on 10/9/2015.
 */
public class PassengerFragment extends Fragment {

  @Bind(R.id.start_from_layout)
  StartEndLayout startEndLayout;

  public static PassengerFragment newInstance() {
    return new PassengerFragment();
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
    Intent i = new Intent(getActivity(), FindRideActivityFragment.class);
    startActivity(i);
  }
}
