package com.mwong56.polyrides.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.fragments.RideDetailsFragment;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by micha on 10/22/2015.
 */
public class MyRideActivity extends BaseRxActivity {

  @Bind(R.id.toolbar)
  Toolbar toolbar;

  @Bind(R.id.toolbar_title)
  ImageView toolbarTitle;

  private Fragment fragment;
  private Ride ride;
  private final PolyRidesService polyRidesService = PolyRidesServiceImpl.get();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.toolbar_frame_layout);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    ride = Parcels.unwrap(getIntent().getExtras().getParcelable("ride"));

    if (savedInstanceState != null) {
      fragment = getSupportFragmentManager().findFragmentByTag("content");
    } else {
      fragment = RideDetailsFragment.newInstance(ride, RideDetailsFragment.REMOVE);
      FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
      fragmentTransaction.add(R.id.frame_layout, fragment, "content");
      fragmentTransaction.commit();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    getSupportFragmentManager().putFragment(outState, "content", fragment);
  }

  @Subscribe
  public void onEvent(RideDetailsFragment.RideDetailsEvent rideDetailsEvent) {
    polyRidesService.removeRide(rideDetailsEvent.ride).subscribe(onNext -> {
      showToast("Ride removed");
      Intent i = new Intent(getBaseContext(), MainActivity.class);
      startActivity(i);
      finish();
    }, this::showToast);
  }
}
