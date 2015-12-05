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
import de.greenrobot.event.EventBus;

/**
 * Created by micha on 10/9/2015.
 */
public class PassengerFragment extends BaseTabbedFragment {

  @Bind(R.id.start_from_view)
  StartEndView startEndView;

  @Bind(R.id.find_ride_button)
  Button findRideButton;

  private MainActivity activity;

  public static PassengerFragment newInstance() {
    return new PassengerFragment();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    startEndView.setup(activity, activity.getGoogleApiClient(), this);
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

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.activity = (MainActivity) context;
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

  public void onEvent(StartEndView.StartEndEvent startEndEvent) {
    Intent i = new Intent(getActivity(), FindRideActivity.class);
    i.putExtra("start", startEndEvent.start);
    i.putExtra("end", startEndEvent.end);
    startActivity(i);
  }

  @Override
  public void onHidden() {
    EventBus.getDefault().unregister(this);
  }

  @Override
  public void onVisible() {
    EventBus.getDefault().register(this);
  }
}
