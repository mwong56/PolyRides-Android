package com.mwong56.polyrides.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.fragments.DateTimeFragment;
import com.mwong56.polyrides.fragments.PassengerRidesFragment;
import com.mwong56.polyrides.models.DateTime;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.views.PassengerRideViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FindRideActivity extends AppCompatActivity implements DateTimeFragment.DateTimeListener, PassengerRideViewHolder.RideListener {

  @Bind(R.id.toolbar)
  Toolbar toolbar;

  private Fragment fragment;
  private Location start;
  private Location end;
  private DateTime dateTime;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.toolbar_frame_layout);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    setTitle("Find Ride");

    this.start = (Location) getIntent().getExtras().get("start");
    this.end = (Location) getIntent().getExtras().get("end");


    if (savedInstanceState != null) {
      fragment = getSupportFragmentManager().getFragment(savedInstanceState, "content");
    }

    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.add(R.id.frame_layout, DateTimeFragment.newInstance(), "DateTimeFragment");
    fragmentTransaction.commit();
  }


  @Override
  public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    super.onSaveInstanceState(outState, outPersistentState);
    getSupportFragmentManager().putFragment(outState, "content", fragment);
  }

  @Override
  public void onDateTimeSet(DateTime dateTime) {
    this.dateTime = dateTime;
    this.fragment = PassengerRidesFragment.newInstance(start, end, dateTime);
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.frame_layout, fragment, "PassengerFragment")
        .addToBackStack("PassengerFragment")
        .commit();
  }

  @Override
  public void onRideClicked(Ride ride) {
    // TODO.
  }
}
