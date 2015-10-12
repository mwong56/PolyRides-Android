package com.mwong56.polyrides.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.Date;
import com.mwong56.polyrides.models.Time;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class DateTimeFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

  @Bind(R.id.chosen_date)
  TextView dateTextView;

  @Bind(R.id.chosen_time)
  TextView timeTextView;

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

    if (this.date != null) {
      dateTextView.setText(this.date.toString());
    }
    if (this.time != null) {
      timeTextView.setText(this.time.toString());
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_date_time, container, false);
    ButterKnife.bind(this, view);
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

  @OnClick(R.id.dateButton)
  void onDateButtonClicked() {
    Calendar now = Calendar.getInstance();
    DatePickerDialog dpd = DatePickerDialog.newInstance(
        this,
        now.get(Calendar.YEAR),
        now.get(Calendar.MONTH),
        now.get(Calendar.DAY_OF_MONTH)
    );
    dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
  }

  @OnClick(R.id.timeButton)
  void onTimeButtonClicked() {
    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd = TimePickerDialog.newInstance(
        this,
        now.get(Calendar.HOUR_OF_DAY),
        now.get(Calendar.MINUTE),
        false
    );
    tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable("date", this.date);
    outState.putParcelable("time", this.time);
  }


  @Override
  public void onViewStateRestored(Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    if (savedInstanceState != null) {
      this.date = (Date) savedInstanceState.get("date");
      this.time = (Time) savedInstanceState.get("time");
      if (this.date != null) {
        dateTextView.setText(this.date.toString());
      }
      if (this.time != null) {
        timeTextView.setText(this.time.toString());
      }
    }
  }

  @OnClick(R.id.next_button)
  void onNextClicked() {
    listener.onDateTimeSet(this.date, this.time);
  }

  @Override
  public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    //TODO: Validate date.
    this.date = new Date(year, monthOfYear, dayOfMonth);
    dateTextView.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
  }

  @Override
  public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
    //TODO: Validate date.
    this.time = new Time(hourOfDay, minute);
    timeTextView.setText(hourOfDay + ":" + minute);
  }


}
