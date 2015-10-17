package com.mwong56.polyrides.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.MainActivity;
import com.mwong56.polyrides.models.Date;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.models.Time;
import com.mwong56.polyrides.models.User;
import com.mwong56.polyrides.services.FacebookService;
import com.mwong56.polyrides.services.FacebookServiceImpl;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

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

  private Ride ride;
  private ProgressDialog dialog;
  private PolyRidesService polyRidesService = PolyRidesServiceImpl.get();
  private FacebookService fbService = FacebookServiceImpl.get();

  public static SubmitRideFragment newInstance(Ride ride) {
    Bundle bundle = new Bundle();
    bundle.putParcelable("ride", ride);
    SubmitRideFragment fragment = new SubmitRideFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    Bundle args = getArguments();
    if (args != null) {
      this.ride = args.getParcelable("ride");
    }

    initializeView();
  }

  private void initializeView() {
    fbService.getUserName(AccessToken.getCurrentAccessToken())
        .subscribe(userName -> nameTextView.setText(userName), error -> showToast(error));
    Picasso.with(getContext()).load("https://graph.facebook.com/" + ride.getUserId() + "/picture?type=large").into(profileImageView);
    locationTextView.setText(ride.getStart().getCity() + " -> " + ride.getEnd().getCity());
    dateTextView.setText(ride.getDate().toString());
    timeTextView.setText(ride.getTime().toString());
    costTextView.setText("$" + ride.getCost() + " per seat");
    seatTextView.setText(ride.getSeats() + " seat/s available");
    noteTextView.setText(ride.getNote());
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
    dialog = ProgressDialog.show(getContext(), "Please wait", "Saving...");
    polyRidesService.saveNewRide(ride)
        .subscribe(onNext -> {
              dialog.dismiss();
              Intent i = new Intent(getActivity(), MainActivity.class);
              startActivity(i);
              getActivity().finish();
            }, error -> showToast(error)
        );
  }

  private void showToast(Throwable e) {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
      dialog = null;
    }
    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
  }

}
