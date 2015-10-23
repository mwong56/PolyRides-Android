package com.mwong56.polyrides.fragments;

import android.content.Context;
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
  private RideDetailsListener listener;

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

    Bundle args = getArguments();
    this.ride = args.getParcelable("ride");
    this.type = args.getInt("type");

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
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_submit_ride, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      listener = (RideDetailsListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString() + " must implement DateTimeListener");
    }
  }

  @Override
  public void onDetach() {
    listener = null;
    super.onDetach();
  }


  @OnClick(R.id.ride_details_button)
  void onClick() {
    if (listener != null) {
      listener.onDetailsButtonClicked(ride);
    }
  }

  public interface RideDetailsListener {
    void onDetailsButtonClicked(Ride ride);
  }
}
