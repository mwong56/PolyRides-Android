package com.mwong56.polyrides.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.MainActivity;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;
import com.mwong56.polyrides.views.RideDetailsView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class SubmitRideFragment extends Fragment {

  @Bind(R.id.ride_details_view)
  RideDetailsView rideDetailsView;

  private Ride ride;
  private ProgressDialog dialog;
  private PolyRidesService polyRidesService = PolyRidesServiceImpl.get();

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

    rideDetailsView.setup(ride);
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
