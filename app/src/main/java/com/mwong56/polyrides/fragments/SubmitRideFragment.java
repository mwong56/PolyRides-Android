package com.mwong56.polyrides.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.MainActivity;
import com.mwong56.polyrides.models.Date;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.models.Time;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class SubmitRideFragment extends Fragment {

  @Bind(R.id.name)
  TextView nameTextView;

  @Bind(R.id.profile_image)
  CircleImageView profileImageView;

  @Bind(R.id.location)
  TextView locationTextView;

  @Bind(R.id.date)
  TextView dateTextView;

  @Bind(R.id.time)
  TextView timeTextView;

  @Bind(R.id.seat)
  TextView seatTextView;

  @Bind(R.id.cost)
  TextView costTextView;

  @Bind(R.id.note)
  TextView noteTextView;

  private Location start;
  private Location end;
  private Date date;
  private Time time;
  private int cost;
  private int seats;
  private String note;

  public static SubmitRideFragment newInstance(Location start, Location end, Date date, Time time, int cost, int seats, String note) {
    Bundle bundle = new Bundle();
    bundle.putParcelable("date", date);
    bundle.putParcelable("time", time);
    bundle.putInt("cost", cost);
    bundle.putInt("seats", seats);
    bundle.putString("note", note);
    SubmitRideFragment fragment = new SubmitRideFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    Bundle args = getArguments();
    if (args != null) {
      this.date = (Date) args.get("date");
      this.time = (Time) args.get("time");
      this.cost = args.getInt("cost");
      this.seats = args.getInt("seats");
      this.note = args.getString("note");
    }

    initializeView();
  }

  private void initializeView() {
    dateTextView.setText(date.toString());
    timeTextView.setText(time.toString());
    costTextView.setText("$" + cost + " per seat");
    seatTextView.setText(seats + " seat/s available");
    noteTextView.setText(note);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_submit_ride, container, false);
    ButterKnife.bind(this, view);
    return view;
  }


  @OnClick(R.id.submit_button)
  void onSubmitClicked() {
    //TODO: Save ride.

    Intent i = new Intent(getActivity(), MainActivity.class);
    startActivity(i);
    getActivity().finish();
  }


}
