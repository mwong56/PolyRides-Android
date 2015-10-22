package com.mwong56.polyrides.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.fragments.DateTimeFragment;
import com.mwong56.polyrides.fragments.NotesFragment;
import com.mwong56.polyrides.fragments.RideDetailsFragment;
import com.mwong56.polyrides.fragments.SeatsFragment;
import com.mwong56.polyrides.models.DateTime;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.models.User;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewRideActivity extends BaseRxActivity implements DateTimeFragment.DateTimeListener,
    SeatsFragment.SeatsListener, NotesFragment.NotesListener, RideDetailsFragment.RideDetailsListener {

  @Bind(R.id.toolbar)
  Toolbar toolbar;

  private Fragment fragment;
  private Location start;
  private Location end;
  private DateTime dateTime;
  private int cost;
  private int seats;
  private String note;
  private PolyRidesService polyRidesService = PolyRidesServiceImpl.get();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.toolbar_frame_layout);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    setTitle("New Ride");

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
    fragment = SeatsFragment.newInstance();
    replaceFragment(fragment, "SeatsFragment");
  }

  @Override
  public void onSeatsSet(int cost, int seats) {
    this.cost = cost;
    this.seats = seats;
    fragment = NotesFragment.newInstance();
    replaceFragment(fragment, "NotesFragment");
  }

  @Override
  public void onNotesSet(String string) {
    this.note = string;
    Ride ride = new Ride(start, end, dateTime, cost, seats, note, User.getUserId());
    fragment = RideDetailsFragment.newInstance(ride, RideDetailsFragment.SUBMIT);
    replaceFragment(fragment, "RideDetailsFragment");
  }

  @Override
  public void onDetailsButtonClicked(Ride ride) {
    polyRidesService.saveNewRide(ride)
        .compose(bindToLifecycle())
        .subscribe(onNext -> openMainActivity()
            , error -> showToast(error)
        );
  }

  private void openMainActivity() {
    Intent i = new Intent(NewRideActivity.this, MainActivity.class);
    startActivity(i);
    finish();
  }

  private void replaceFragment(Fragment fragment, String tag) {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.frame_layout, fragment, tag)
        .addToBackStack(tag)
        .commit();
  }
}
