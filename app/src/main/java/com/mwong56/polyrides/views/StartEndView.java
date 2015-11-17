package com.mwong56.polyrides.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
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
import com.mwong56.polyrides.services.LocationService;
import com.mwong56.polyrides.services.LocationServiceImpl;
import com.mwong56.polyrides.utils.OnActivityResultListener;
import com.mwong56.polyrides.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by micha on 10/10/2015.
 */
public class StartEndView extends LinearLayout implements OnActivityResultListener {

  private static final int START_RESULT = 1;
  private static final int END_RESULT = 2;
  private static final String TAG = "StartEndLayout";

  @Bind(R.id.start)
  PlacesAutoComplete startEditText;

  @Bind(R.id.end)
  PlacesAutoComplete endEditText;

  private Activity activity;
  private Fragment fragment;
  private GoogleApiClient apiClient;
  private LocationService locationService = LocationServiceImpl.instance();
  private CompositeSubscription compositeSubscription = new CompositeSubscription();
  private StartEndViewListener listener;

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
    this.startEditText.setLocation(location);
    if (listener != null)
      listener.onStartListener(location != null ? true : false);
  }

  public void setEndLocation(Location location) {
    this.endEditText.setLocation(location);
    if (listener != null)
      listener.onEndListener(location != null ? true : false);

  }

  /**
   * Returns an array containing the start and end place.
   *
   * @return An array containing start and end place. Index 1 is start, Index 2 is end.
   */
  public Location[] getPlaces() {
    return new Location[]{startEditText.getLocation(), endEditText.getLocation()};
  }

  public void setup(Activity activity, GoogleApiClient client, Fragment fragment) {
    this.activity = activity;
    this.apiClient = client;
    this.fragment = fragment;
    this.startEditText.setup(client, activity);
    this.endEditText.setup(client, activity);
  }

  public void setListener(StartEndViewListener listener) {
    this.listener = listener;
  }

  @OnLongClick(R.id.start)
  boolean onStartLongClick() {
    showPickerDialog(START_RESULT);
    return true;
  }

  @OnLongClick(R.id.end)
  boolean onEndLongClick() {
    showPickerDialog(END_RESULT);
    return true;
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  @OnClick(R.id.start_location)
  void setStart() {
    compositeSubscription.add(
        locationService.getCurrentLocation(getContext())
            .subscribe(place -> {
              setStartLocation(new Location(place, getContext()));
              Utils.hideKeyboard(this.activity);
            }, error -> showToast("Could not find location")));
  }

  @OnClick(R.id.end_location)
  void setEnd() {
    compositeSubscription.add(
        locationService.getCurrentLocation(getContext())
            .subscribe(place -> {
              setEndLocation(new Location(place, getContext()));
              Utils.hideKeyboard(this.activity);
            }, error -> showToast("Could not find location")));
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

    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    try {
      fragment.startActivityForResult(builder.build(getContext()), result);
    } catch (GooglePlayServicesRepairableException e) {
      Log.e(TAG, e.toString());
      showToast("Sorry! Something went wrong.");
    } catch (GooglePlayServicesNotAvailableException e) {
      showToast("Please install Google Play Services.");
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
