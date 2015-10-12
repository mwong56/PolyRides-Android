package com.mwong56.polyrides.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.fragments.DateTimeFragment;
import com.mwong56.polyrides.fragments.NotesFragment;
import com.mwong56.polyrides.fragments.SeatsFragment;
import com.mwong56.polyrides.fragments.SubmitRideFragment;
import com.mwong56.polyrides.models.Date;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.models.Time;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewRideActivity extends AppCompatActivity implements DateTimeFragment.DateTimeListener,
    SeatsFragment.SeatsListener, NotesFragment.NotesListener {

  @Bind(R.id.toolbar)
  Toolbar toolbar;

  private Fragment fragment;
  private Location start;
  private Location end;
  private Date date;
  private Time time;
  private int cost;
  private int seats;
  private String note;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_ride);
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
  public void onDateTimeSet(Date date, Time time) {
    this.date = date;
    this.time = time;
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.frame_layout, SeatsFragment.newInstance(), "SeatsFragment")
        .addToBackStack("SeatsFragment")
        .commit();
  }

  @Override
  public void onSeatsSet(int cost, int seats) {
    this.cost = cost;
    this.seats = seats;
    fragment = NotesFragment.newInstance();
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.frame_layout, fragment, "NotesFragment")
        .addToBackStack("NotesFragment")
        .commit();
  }

  @Override
  public void onNotesSet(String string) {
    this.note = string;
    fragment = SubmitRideFragment.newInstance(start, end, date, time, cost, seats, note);
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.frame_layout,
            fragment,
            "SubmitRideFragment")
        .addToBackStack("SubmitRideFragment")
        .commit();
  }
}
