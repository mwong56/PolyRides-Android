package com.mwong56.polyrides.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.services.LocationService;
import com.mwong56.polyrides.services.LocationServiceSingleton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by micha on 10/10/2015.
 */
public class StartEndLayout extends LinearLayout {

  @Bind(R.id.start)
  PlacesAutoComplete startEditText;

  @Bind(R.id.end)
  PlacesAutoComplete endEditText;

  private GoogleApiClient apiClient;
  private LocationService locationService = LocationServiceSingleton.instance();
  private CompositeSubscription compositeSubscription = new CompositeSubscription();

  public StartEndLayout(Context context) {
    super(context);
  }

  public StartEndLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public StartEndLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  /**
   * Returns an array containing the start and end place.
   * @return An array containing start and end place. Index 1 is start, Index 2 is end.
   */
  public Location[] getPlaces() {
    return new Location[]{startEditText.getLocation(), endEditText.getLocation()};
  }

  public void setStartLocation(Location location) {
    this.startEditText.setLocation(location);
  }

  public void setEndLocation(Location location) {
    this.endEditText.setLocation(location);
  }

  public void setGoogleApiClient(GoogleApiClient client) {
    this.apiClient = client;
    startEditText.setup(apiClient);
    endEditText.setup(apiClient);
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
              startEditText.setLocation(new Location(place));
            }, error -> showToast("Could not find location")));
  }

  @OnClick(R.id.end_location)
  void setEnd() {
    compositeSubscription.add(
        locationService.getCurrentLocation(getContext())
            .subscribe(place -> {
              endEditText.setLocation(new Location(place));
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
