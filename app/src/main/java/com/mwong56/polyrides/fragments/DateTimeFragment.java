package com.mwong56.polyrides.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.Date;
import com.mwong56.polyrides.models.Time;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DateTimeFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

  private DateTimeListener listener;
  private Time time;
  private Date date;


  public interface DateTimeListener {
    void onDateTimeSet(Date date, Time time);
  }

  public static DateTimeFragment newInstance() {
    return new DateTimeFragment();
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view =  inflater.inflate(R.layout.fragment_date_time, container, false);
    ButterKnife.bind(view);
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      listener = (DateTimeListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString() + " must implement DateTimeListener");
    }
  }

  @Override
  public void onDetach() {
    listener = null;
    super.onDetach();
  }

  @Override
  public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    this.date = new Date(year, monthOfYear, dayOfMonth);

  }

  @Override
  public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
    this.time = new Time(hourOfDay, minute);
  }

}
