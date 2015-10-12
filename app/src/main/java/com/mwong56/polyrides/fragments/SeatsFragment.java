package com.mwong56.polyrides.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class SeatsFragment extends Fragment {

  @Bind(R.id.cost)
  EditText costEditText;

  @Bind(R.id.seats)
  NumberPicker seatsPicker;

  private SeatsListener listener;
  private int cost;
  private int seats;

  public interface SeatsListener {
    void onSeatsSet(int cost, int seats);
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
      cost = savedInstanceState.getInt("cost");
      seats = savedInstanceState.getInt("seats");
      costEditText.setText(cost + "");
      seatsPicker.setValue(seats);
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    try {
      cost = Integer.parseInt(costEditText.getText().toString());
    } catch (Exception e) {
      cost = 0;
    }
    int seats = seatsPicker.getValue();
    outState.putInt("cost", cost);
    outState.putInt("seats", seats);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_seats, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      listener = (SeatsListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString() + " must implement SeatsListener");
    }
  }

  @Override
  public void onDetach() {
    listener = null;
    super.onDetach();
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
    listener.onSeatsSet(cost, seats);
  }


}
