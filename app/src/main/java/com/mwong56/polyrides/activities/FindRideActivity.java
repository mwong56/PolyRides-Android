package com.mwong56.polyrides.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.fragments.DateTimeFragment;
import com.mwong56.polyrides.fragments.PassengerRidesFragment;
import com.mwong56.polyrides.fragments.RideDetailsFragment;
import com.mwong56.polyrides.models.Chat;
import com.mwong56.polyrides.models.DateTime;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.models.User;
import com.mwong56.polyrides.views.PassengerRideViewHolder;

import org.parceler.Parcels;

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
    replaceFragment(fragment, "PassengerFragment");
  }

  @Override
  public void onRideClicked(Ride ride) {
    this.fragment = RideDetailsFragment.newInstance(ride, RideDetailsFragment.MESSAGE);
    replaceFragment(fragment, "RideDetailsFragment");
  }

  @Override
  public void onDetailsButtonClicked(Ride ride) {
    //TODO: Launch message view.
    String groupId = ride.getUserId().compareTo(User.getUserId()) > 0 ? User.getUserId() +
        ride.getUserId() : ride.getUserId() + User.getUserId();

    Intent i = new Intent(FindRideActivity.this, MessageActivity.class);
    i.putExtra("messages", Parcels.wrap(new Chat(0, groupId, false)));
    startActivity(i);
  }
}
