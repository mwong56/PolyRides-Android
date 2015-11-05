package com.mwong56.polyrides.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.DateTime;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;
import com.mwong56.polyrides.views.PassengerRideViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.ribot.easyadapter.EasyRecyclerAdapter;

/**
 * Created by micha on 10/20/2015.
 */
public class PassengerRidesFragment extends BaseRxFragment {

  @Bind(R.id.recycler_view)
  RecyclerView recyclerView;

  private final PolyRidesService polyRidesService = PolyRidesServiceImpl.get();
  private Location start;
  private Location end;
  private DateTime dateTime;
  private EasyRecyclerAdapter<Ride> adapter;
  private List<Ride> rideList;

  public static PassengerRidesFragment newInstance(Location start, Location end, DateTime dateTime) {
    Bundle args = new Bundle();
    args.putParcelable("start", start);
    args.putParcelable("end", end);
    args.putParcelable("dateTime", dateTime);
    PassengerRidesFragment fragment = new PassengerRidesFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    this.start = getArguments().getParcelable("start");
    this.end = getArguments().getParcelable("end");
    this.dateTime = getArguments().getParcelable("dateTime");
    this.rideList = new ArrayList<>();
    this.adapter = new EasyRecyclerAdapter<>(getContext(), PassengerRideViewHolder.class, rideList, getActivity());
    this.recyclerView.setAdapter(adapter);

    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);


  }

  @Override
  public void onResume() {
    super.onResume();
    refreshRides();
  }

  private void refreshRides() {
    polyRidesService.getRides(dateTime.getDate(), false)
        .compose(bindToLifecycle())
        .subscribe(rides -> {
          rideList.clear();
          rideList.addAll(rides);
          //TODO: Sort.
          adapter.notifyDataSetChanged();
        }, error -> showToast(error));
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_reycler_view, container, false);
    ButterKnife.bind(this, view);
    return view;
  }



}
