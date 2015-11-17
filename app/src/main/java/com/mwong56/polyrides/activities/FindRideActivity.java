package com.mwong56.polyrides.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.fragments.DateTimeFragment;
import com.mwong56.polyrides.fragments.PassengerRidesFragment;
import com.mwong56.polyrides.fragments.RideDetailsFragment;
import com.mwong56.polyrides.models.DateTime;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.models.User;
import com.mwong56.polyrides.views.PassengerRideViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FindRideActivity extends BaseRxActivity implements DateTimeFragment.DateTimeListener,
    PassengerRideViewHolder.RideListener, RideDetailsFragment.RideDetailsListener {

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
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setTitle("Find Ride");

    this.start = (Location) getIntent().getExtras().get("start");
    this.end = (Location) getIntent().getExtras().get("end");

    if (savedInstanceState != null) {
      fragment = getSupportFragmentManager().getFragment(savedInstanceState, "content");
    } else {
      fragment = DateTimeFragment.newInstance();
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
  protected void onResume() {
    super.onResume();
    //TODO: Dumb hack for skipping state restoration. Activities onSaveInstanceState aren't guaranteed
    // need to save in onPause/onResume but don't have bundle.. Use shared preferences later.
    if (!(fragment instanceof DateTimeFragment)) {
      if (this.dateTime == null) {
        openMainActivity();
      }
    }
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
    replaceFragment(fragment, "content");
  }

  @Override
  public void onRideClicked(Ride ride) {
    this.fragment = RideDetailsFragment.newInstance(ride, RideDetailsFragment.MESSAGE);
    replaceFragment(fragment, "content");
  }

  @Override
  public void onDetailsButtonClicked(Ride ride) {
    String groupId = ride.getUserId().compareTo(User.getUserId()) > 0 ? User.getUserId() +
        ride.getUserId() : ride.getUserId() + User.getUserId();

    Intent i = new Intent(FindRideActivity.this, MessageActivity.class);
    i.putExtra("groupId", groupId);
    startActivity(i);
  }

  private void openMainActivity() {
    Intent i = new Intent(FindRideActivity.this, MainActivity.class);
    startActivity(i);
    finish();
  }
}
