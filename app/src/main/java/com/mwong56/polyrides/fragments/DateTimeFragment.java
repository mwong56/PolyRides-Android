package com.mwong56.polyrides.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.FindRideActivity;
import com.mwong56.polyrides.activities.NewRideActivity;
import com.mwong56.polyrides.models.DateTime;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

/**
 * A placeholder fragment containing a simple view.
 */
public class DateTimeFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

  @Bind(R.id.header_image)
  ImageView headerImageView;

  @Bind(R.id.chosen_date)
  TextView dateTextView;

  @Bind(R.id.chosen_time)
  TextView timeTextView;

  @State DateTime dateTime;
  @State boolean dateSet, timeSet;

  private DateTimeListener listener;
  private int year, monthOfYear, dayOfMonth, hourOfDay, minute;

  public interface DateTimeListener {
    void onDateTimeSet(DateTime dateTime);
  }

  public static DateTimeFragment newInstance() {
    return new DateTimeFragment();
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Icepick.restoreInstanceState(this, savedInstanceState);
    setTime();

    if (getActivity() instanceof FindRideActivity) {
      headerImageView.setImageResource(R.drawable.car_progress_passenger2);
    } else if (getActivity() instanceof NewRideActivity) {
      headerImageView.setImageResource(R.drawable.car_progress2);
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
    dpd.setAccentColor(ContextCompat.getColor(getContext(), R.color.PrimaryColor));
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
    tpd.setAccentColor(ContextCompat.getColor(getContext(), R.color.PrimaryColor));
    tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
  }

  private void setTime() {
    if (this.dateTime != null) {
      if (this.dateSet) {
        dateTextView.setText(this.dateTime.printDate());
      }

      if (this.timeSet) {
        timeTextView.setText(this.dateTime.printTime());
      }
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    this.dateTime = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minute);
    Icepick.saveInstanceState(this, outState);
  }


  @Override
  public void onViewStateRestored(Bundle savedInstanceState) {
    super.onViewStateRestored(savedInstanceState);
    setTime();
  }

  @OnClick(R.id.next_button)
  void onNextClicked() {
    if (this.dateSet && this.timeSet) {
      this.dateTime = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minute);
      listener.onDateTimeSet(this.dateTime);
    }
  }

  @Override
  public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    //TODO: Validate dateTime.
    this.year = year;
    this.monthOfYear = monthOfYear;
    this.dayOfMonth = dayOfMonth;
    this.dateSet = true;
    this.dateTime = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minute);
    dateTextView.setText(dateTime.printDate());
  }

  @Override
  public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
    //TODO: Validate dateTime.
    this.hourOfDay = hourOfDay;
    this.minute = minute;
    this.timeSet = true;
    this.dateTime = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minute);
    timeTextView.setText(dateTime.printTime());
  }
}
