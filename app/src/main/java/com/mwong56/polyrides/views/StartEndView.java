package com.mwong56.polyrides.views;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.BaseRxActivity;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.services.LocationService;
import com.mwong56.polyrides.services.LocationServiceImpl;
import com.mwong56.polyrides.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by micha on 10/10/2015.
 */
public class StartEndView extends LinearLayout {

  private static final int START_RESULT = 1;
  private static final int END_RESULT = 2;
  private static final String TAG = "StartEndLayout";

  @Bind(R.id.start)
  PlacesAutoComplete startEditText;

  @Bind(R.id.end)
  PlacesAutoComplete endEditText;

  private BaseRxActivity activity;
  private Fragment fragment;
  private GoogleApiClient apiClient;
  private LocationService locationService = LocationServiceImpl.instance();
  private CompositeSubscription compositeSubscription = new CompositeSubscription();

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
  }

  public void setEndLocation(Location location) {
    this.endEditText.setLocation(location);
  }

  /**
   * Returns an array containing the start and end place.
   *
   * @return An array containing start and end place. Index 1 is start, Index 2 is end.
   */
  public Location[] getPlaces() {
    return new Location[]{startEditText.getLocation(), endEditText.getLocation()};
  }

  public void setup(BaseRxActivity activity, GoogleApiClient client, Fragment fragment) {
    this.activity = activity;
    this.apiClient = client;
    this.fragment = fragment;
    this.startEditText.setup(client, activity);
    this.endEditText.setup(client, activity);
  }

  public String getStartText() {
    return this.startEditText.getText().toString();
  }

  public String getEndText() {
    return this.endEditText.getText().toString();
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
              Location newLocation = new Location(place, getContext());
              setStartLocation(newLocation);
              startEditText.setText(newLocation.getAddress());
              Utils.hideKeyboard(this.activity);
            }, error -> showToast("Could not find location")));
  }

  @OnClick(R.id.end_location)
  void setEnd() {
    compositeSubscription.add(
        locationService.getCurrentLocation(getContext())
            .subscribe(place -> {
              Location newLocation = new Location(place, getContext());
              setEndLocation(newLocation);
              endEditText.setText(newLocation.getAddress());
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

  private void showToast(String error) {
    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
  }
}
