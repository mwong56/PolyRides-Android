package com.mwong56.polyrides.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.mwong56.polyrides.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.State;

/**
 * A placeholder fragment containing a simple view.
 */
public class SeatsFragment extends BaseRxFragment {

  @Bind(R.id.cost)
  EditText costEditText;

  @Bind(R.id.seats)
  NumberPicker seatsPicker;

  @State
  int cost;
  @State
  int seats;

  public class SeatsEvent {
    public int cost;
    public int seats;

    public SeatsEvent(int cost, int seats) {
      this.cost = cost;
      this.seats = seats;
    }
  }

  public static SeatsFragment newInstance() {
    return new SeatsFragment();
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    seatsPicker.setMinValue(1);
    seatsPicker.setMaxValue(100);
    seatsPicker.setWrapSelectorWheel(false);

    if (savedInstanceState != null) {
      costEditText.setText(cost + "");
      seatsPicker.setValue(seats);
    }
  }

  @Override
  public void onPause() {
    try {
      cost = Integer.parseInt(costEditText.getText().toString());
    } catch (Exception e) {
      cost = 0;
    }
    seats = seatsPicker.getValue();
    super.onPause();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_seats, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @OnClick(R.id.next_button)
  void onNextClicked() {
    try {
      cost = Integer.parseInt(costEditText.getText().toString());
    } catch (Exception e) {
      Toast.makeText(getActivity(), "Must enter a valid cost per seat.", Toast.LENGTH_SHORT).show();
      return;
    }
    int seats = seatsPicker.getValue();
    bus.post(new SeatsEvent(cost, seats));
  }
}
