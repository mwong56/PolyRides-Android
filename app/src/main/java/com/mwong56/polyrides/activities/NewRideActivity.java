package com.mwong56.polyrides.activities;

import android.os.Bundle;
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

    if (savedInstanceState == null) {
      FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
      fragmentTransaction.add(R.id.frame_layout, DateTimeFragment.newInstance(), "DateTimeFragment");
      fragmentTransaction.commit();
    }

    this.start = (Location) getIntent().getExtras().get("start");
    this.end = (Location) getIntent().getExtras().get("end");

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
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.frame_layout, NotesFragment.newInstance(), "NotesFragment")
        .addToBackStack("NotesFragment")
        .commit();
  }

  @Override
  public void onNotesSet(String string) {
    this.note = string;
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.frame_layout,
            SubmitRideFragment.newInstance(start, end, date, time, cost, seats, note),
            "SubmitRideFragment")
        .addToBackStack("SubmitRideFragment")
        .commit();
  }
}
