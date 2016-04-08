package com.mwong56.polyrides.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.views.RideDetailsView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class RideDetailsFragment extends BaseRxFragment {
  public static final int SUBMIT = 0;
  public static final int MESSAGE = 1;
  public static final int REMOVE = 2;
  private static final String SUBMIT_STRING = "Submit Ride";
  private static final String MESSAGE_STRING = "Send Message";
  private static final String REMOVE_STRING = "Remove Ride";

  @Bind(R.id.ride_details_view)
  RideDetailsView rideDetailsView;

  @Bind(R.id.ride_details_button)
  Button button;

  private Ride ride;
  private int type;

  public static RideDetailsFragment newInstance(Ride ride, int type) {
    Bundle bundle = new Bundle();
    bundle.putParcelable("ride", ride);
    bundle.putInt("type", type);
    RideDetailsFragment fragment = new RideDetailsFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Bundle args = savedInstanceState != null ? savedInstanceState : getArguments();
    if (args == null) {
      getActivity().finish();
    }

    this.ride = args.getParcelable("ride");
    Timber.d(ride.toString());
    this.type = args.getInt("type");
    Timber.d("Type : %s", this.type);

    switch (type) {
      case SUBMIT:
        this.button.setText(SUBMIT_STRING);
        break;
      case MESSAGE:
        this.button.setText(MESSAGE_STRING);
        break;
      case REMOVE:
        this.button.setText(REMOVE_STRING);
        break;
    }

    rideDetailsView.setup(ride);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable("ride", this.ride);
    outState.putInt("tye", this.type);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_submit_ride, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @OnClick(R.id.ride_details_button)
  void onClick() {
    bus.post(new RideDetailsEvent(ride));
  }

  public class RideDetailsEvent {
    public Ride ride;

    public RideDetailsEvent(Ride ride) {
      this.ride = ride;
    }
  }
}
