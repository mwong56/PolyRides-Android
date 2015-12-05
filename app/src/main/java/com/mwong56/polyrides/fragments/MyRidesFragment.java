package com.mwong56.polyrides.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.MyRideActivity;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.services.PolyRidesService;
import com.mwong56.polyrides.services.PolyRidesServiceImpl;
import com.mwong56.polyrides.views.DividerItemDecoration;
import com.mwong56.polyrides.views.PassengerRideViewHolder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import uk.co.ribot.easyadapter.EasyRecyclerAdapter;

/**
 * Created by micha on 10/9/2015.
 */
public class MyRidesFragment extends BaseRxFragment implements PassengerRideViewHolder.RideListener {

  @Bind(R.id.recycler_view)
  RecyclerView recyclerView;

  @Bind(R.id.no_my_rides)
  LinearLayout noRidesView;

  @Bind(R.id.progress_bar)
  MaterialProgressBar progressBar;

  private List<Ride> rideList;
  private EasyRecyclerAdapter<Ride> adapter;
  private final PolyRidesService polyRidesService = PolyRidesServiceImpl.get();


  public static MyRidesFragment newInstance() {
    return new MyRidesFragment();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    this.rideList = new ArrayList<>();
    this.adapter = new EasyRecyclerAdapter<>(getContext(), PassengerRideViewHolder.class, rideList, this);
    this.recyclerView.setAdapter(adapter);

    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));
  }

  @Override
  public void onResume() {
    super.onResume();
    refreshRides();
  }

  private void refreshRides() {
    rideList.clear();
    adapter.notifyDataSetChanged();

    progressBar.setVisibility(View.VISIBLE);
    noRidesView.setVisibility(View.GONE);

    polyRidesService.getMyRides()
        .compose(bindToLifecycle())
        .subscribe(rides -> {
          rideList.clear();
          rideList.addAll(rides);
          adapter.notifyDataSetChanged();
          progressBar.setVisibility(View.GONE);

          if (rides.size() == 0) {
            noRidesView.setVisibility(View.VISIBLE);
          } else {
            noRidesView.setVisibility(View.GONE);
          }
        }, this::showToast);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_reycler_view, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onRideClicked(Ride ride) {
    Intent i = new Intent(getActivity(), MyRideActivity.class);
    i.putExtra("ride", Parcels.wrap(ride));
    startActivity(i);
  }
}
