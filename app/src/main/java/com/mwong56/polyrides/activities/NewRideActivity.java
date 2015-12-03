package com.mwong56.polyrides.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

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

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class NewRideActivity extends BaseRxActivity implements DateTimeFragment.DateTimeListener,
    SeatsFragment.SeatsListener, NotesFragment.NotesListener, RideDetailsFragment.RideDetailsListener {

  @Bind(R.id.toolbar)
  Toolbar toolbar;

  @Bind(R.id.toolbar_title)
  ImageView toolbarTitle;

  @State
  Location start;
  @State
  Location end;
  @State
  int cost;
  @State
  int seats;
  @State
  String note;

  private Fragment fragment;
  private DateTime dateTime;
  private PolyRidesService polyRidesService = PolyRidesServiceImpl.get();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.toolbar_frame_layout);
    ButterKnife.bind(this);

    onRestoreInstanceState(savedInstanceState);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    toolbarTitle.setVisibility(View.GONE);
    setTitle("New Ride");

    this.start = (Location) getIntent().getExtras().get("start");
    this.end = (Location) getIntent().getExtras().get("end");

    if (savedInstanceState != null) {
      fragment = getSupportFragmentManager().findFragmentByTag("content");
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
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    Icepick.restoreInstanceState(this, savedInstanceState);
    if (savedInstanceState != null) {
      this.dateTime = Parcels.unwrap(savedInstanceState.getParcelable("dateTime"));
    }
  }


  @Override
  public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    super.onSaveInstanceState(outState, outPersistentState);
    getSupportFragmentManager().putFragment(outState, "content", fragment);
    outState.putParcelable("dateTime", Parcels.wrap(this.dateTime));
    Icepick.saveInstanceState(this, outState);
  }

  @Override
  public void onDateTimeSet(DateTime dateTime) {
    this.dateTime = dateTime;
    fragment = SeatsFragment.newInstance();
    replaceFragment(fragment, "content");
  }

  @Override
  public void onSeatsSet(int cost, int seats) {
    this.cost = cost;
    this.seats = seats;
    fragment = NotesFragment.newInstance();
    replaceFragment(fragment, "content");
  }

  @Override
  public void onNotesSet(String string) {
    this.note = string;
    Ride ride = new Ride(start, end, dateTime, cost, seats, note, User.getUserId(), null);
    fragment = RideDetailsFragment.newInstance(ride, RideDetailsFragment.SUBMIT);
    replaceFragment(fragment, "content");
  }

  @Override
  public void onDetailsButtonClicked(Ride ride) {
    polyRidesService.saveNewRide(ride)
        .compose(bindToLifecycle())
        .subscribe(onNext -> {
              showToast("Ride saved!");
              openMainActivity();
            }, this::showToast
        );
  }

  private void openMainActivity() {
    Intent i = new Intent(NewRideActivity.this, MainActivity.class);
    startActivity(i);
    finish();
  }
}
