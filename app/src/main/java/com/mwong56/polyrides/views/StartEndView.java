package com.mwong56.polyrides.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.utils.OnActivityResultListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by micha on 10/10/2015.
 */
public class StartEndView extends LinearLayout implements OnActivityResultListener {

  private static final int START_RESULT = 1;
  private static final int END_RESULT = 2;
  private static final String TAG = "StartEndLayout";

  @Bind(R.id.start)
  AppCompatEditText startEditText;

  @Bind(R.id.end)
  AppCompatEditText endEditText;

  private Location startLocation;
  private Location endLocation;
  private Activity activity;
  private Fragment fragment;
  private GoogleApiClient apiClient;
  private CompositeSubscription compositeSubscription = new CompositeSubscription();
  private StartEndViewListener listener;
  private Intent placePicker;

  public StartEndView(Context context) {
    super(context);
  }

  public StartEndView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public StartEndView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setStartLocation(Location location) {
    this.startLocation = location;
    this.startEditText.setText(location.getAddress());
    if (listener != null)
      listener.onStartListener(location != null ? true : false);
  }

  public void setEndLocation(Location location) {
    this.endLocation = location;
    this.endEditText.setText(location.getAddress());
    if (listener != null)
      listener.onEndListener(location != null ? true : false);
  }

  /**
   * Returns an array containing the start and end place.
   *
   * @return An array containing start and end place. Index 1 is start, Index 2 is end.
   */
  public Location[] getPlaces() {
    return new Location[]{this.startLocation, this.endLocation};
  }

  public void setup(Activity activity, GoogleApiClient client, Fragment fragment) {
    this.activity = activity;
    this.apiClient = client;
    this.fragment = fragment;
    this.startEditText.setFocusable(false);
    this.endEditText.setFocusable(false);

    try {
      PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
      placePicker = builder.build(getContext());
    } catch (GooglePlayServicesRepairableException e) {
      Log.e(TAG, e.toString());
      showToast("Sorry! Something went wrong.");
    } catch (GooglePlayServicesNotAvailableException e) {
      showToast("Please install Google Play Services.");
    }
  }

  public void setListener(StartEndViewListener listener) {
    this.listener = listener;
  }

  @OnClick(R.id.start)
  void onStartClick() {
    showPickerDialog(START_RESULT);
  }

  @OnClick(R.id.end)
  void onEndClick() {
    showPickerDialog(END_RESULT);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    compositeSubscription = new CompositeSubscription();
  }

  @Override
  protected void onDetachedFromWindow() {
    compositeSubscription.unsubscribe();
    super.onDetachedFromWindow();
  }

  private void showPickerDialog(int result) {
    if (apiClient == null || !apiClient.isConnected()) {
      return;
    }

    if (placePicker == null) {
      try {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        placePicker = builder.build(getContext());
        fragment.startActivityForResult(placePicker, result);
      } catch (GooglePlayServicesRepairableException e) {
        Log.e(TAG, e.toString());
        showToast("Sorry! Something went wrong.");
      } catch (GooglePlayServicesNotAvailableException e) {
        showToast("Please install Google Play Services.");
      }
    } else {
      fragment.startActivityForResult(placePicker, result);
    }
  }

  private void showToast(String error) {
    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onActivityResultCalled(int requestCode, Intent data) {
    Location location = new Location(PlacePicker.getPlace(data, getContext()), getContext());
    if (requestCode == START_RESULT) {
      setStartLocation(location);
    } else if (requestCode == END_RESULT) {
      setEndLocation(location);
    }
  }

  public interface StartEndViewListener {
    void onStartListener(boolean set);

    void onEndListener(boolean set);
  }
}
