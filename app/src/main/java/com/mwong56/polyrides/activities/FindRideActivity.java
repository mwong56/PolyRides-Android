package com.mwong56.polyrides.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.fragments.DateTimeFragment;
import com.mwong56.polyrides.fragments.PassengerRidesFragment;
import com.mwong56.polyrides.fragments.RideDetailsFragment;
import com.mwong56.polyrides.models.DateTime;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.models.User;
import com.mwong56.polyrides.views.PassengerRideViewHolder;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FindRideActivity extends BaseRxActivity {

  @Bind(R.id.toolbar)
  Toolbar toolbar;

  @Bind(R.id.toolbar_title)
  ImageView toolbarTitle;

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
    toolbarTitle.setVisibility(View.GONE);
    setTitle("Find Ride");

    this.start = getIntent().getExtras().getParcelable("start");
    this.end = getIntent().getExtras().getParcelable("end");

    if (savedInstanceState == null) {
      Fragment fragment = DateTimeFragment.newInstance();
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

  @Subscribe
  public void onEvent(DateTimeFragment.DateTimeEvent dateTimeEvent) {
    this.dateTime = dateTimeEvent.datetime;
    Fragment fragment = PassengerRidesFragment.newInstance(start, end, dateTime);
    replaceFragment(fragment, "content");
  }

  @Subscribe
  public void onEvent(PassengerRideViewHolder.RideEvent rideEvent) {
    Fragment fragment = RideDetailsFragment.newInstance(rideEvent.ride, RideDetailsFragment.MESSAGE);
    replaceFragment(fragment, "content");
  }

  @Subscribe
  public void onEvent(RideDetailsFragment.RideDetailsEvent rideDetailsEvent) {
    String groupId = rideDetailsEvent.ride.getUserId().compareTo(User.getUserId()) > 0 ?
        User.getUserId() + rideDetailsEvent.ride.getUserId()
        : rideDetailsEvent.ride.getUserId() + User.getUserId();

    Intent i = new Intent(FindRideActivity.this, MessageActivity.class);
    i.putExtra("groupId", groupId);
    startActivity(i);
  }
}
