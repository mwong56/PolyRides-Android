package com.mwong56.polyrides.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.fragments.RideDetailsFragment;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by micha on 10/22/2015.
 */
public class MyRideActivity extends BaseRxActivity implements RideDetailsFragment.RideDetailsListener{

  @Bind(R.id.toolbar)
  Toolbar toolbar;

  private Fragment fragment;
  private Ride ride;
  private final PolyRidesService polyRidesService = PolyRidesServiceImpl.get();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.toolbar_frame_layout);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    ride = getIntent().getExtras().getParcelable("ride");

    if (savedInstanceState != null) {
      fragment = getSupportFragmentManager().getFragment(savedInstanceState, "content");
    }

    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.add(R.id.frame_layout, RideDetailsFragment.newInstance(ride, RideDetailsFragment.REMOVE), "RideDetailsFragment");
    fragmentTransaction.commit();
  }

  @Override
  public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    super.onSaveInstanceState(outState, outPersistentState);
    getSupportFragmentManager().putFragment(outState, "content", fragment);
  }

  @Override
  public void onDetailsButtonClicked(Ride ride) {
    polyRidesService.removeRide(ride).subscribe(onNext -> {
      Intent i = new Intent(getBaseContext(), MainActivity.class);
      startActivity(i);
      finish();
    }, onError -> showToast(onError));
  }
}
