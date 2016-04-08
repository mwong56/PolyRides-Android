package com.mwong56.polyrides.views;

import android.Manifest;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.MainActivity;
import com.mwong56.polyrides.fragments.BaseRxFragment;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.services.GooglePlacesService;
import com.mwong56.polyrides.services.GooglePlacesServiceImpl;
import com.mwong56.polyrides.services.LocationService;
import com.mwong56.polyrides.services.LocationServiceImpl;
import com.mwong56.polyrides.utils.Utils;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

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

  @Bind(R.id.start_from_next_button)
  Button nextButton;

  @Bind(R.id.progress_bar)
  MaterialProgressBar progressBar;

  private StartEndListener listener;
  private MainActivity activity;
  private BaseRxFragment fragment;
  private GooglePlacesService placesService = GooglePlacesServiceImpl.get();
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


  public void setup(BaseRxFragment fragment) {
    this.activity = (MainActivity) getContext();
    this.fragment = fragment;
    this.startEditText.setup(activity.getGoogleApiClient(), activity);
    this.endEditText.setup(activity.getGoogleApiClient(), activity);
  }

  public void setListener(StartEndListener listener) {
    this.listener = listener;
  }

  public void setNextButtonTitle(String title) {
    this.nextButton.setText(title);
  }

  /**
   * Returns an array containing the start and end place.
   *
   * @return An array containing start and end place. Index 1 is start, Index 2 is end.
   */
  public Location[] getPlaces() {
    return new Location[]{startEditText.getLocation(), endEditText.getLocation()};
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  @OnClick(R.id.start_location)
  void setStart() {
    RxPermissions.getInstance(activity)
        .request(Manifest.permission.ACCESS_FINE_LOCATION)
        .subscribe(granted -> {
          if (granted) {
            compositeSubscription.add(
                locationService.getCurrentLocation(getContext())
                    .subscribe(place -> {
                      Location newLocation = new Location(place, getContext());
                      setStartLocation(newLocation);
                      startEditText.setText(newLocation.getAddress());
                      Utils.hideKeyboard(this.activity);
                    }, error -> {
                      showToast("Could not find location");
                    }));
          } else {
            showToast("Could not find location");
          }
        });
  }

  @OnClick(R.id.end_location)
  void setEnd() {
    RxPermissions.getInstance(activity)
        .request(Manifest.permission.ACCESS_FINE_LOCATION)
        .subscribe(granted -> {
          if (granted) {
            compositeSubscription.add(
                locationService.getCurrentLocation(getContext())
                    .subscribe(place -> {
                      Location newLocation = new Location(place, getContext());
                      setEndLocation(newLocation);
                      endEditText.setText(newLocation.getAddress());
                      Utils.hideKeyboard(this.activity);
                    }, error -> {
                      showToast("Could not find location");
                    }));
          } else {
            showToast("Could not find location");
          }});
  }

  @OnClick(R.id.start_from_next_button)
  void onNextButton() {
    showProgress(true);
    Location[] locations = getPlaces();

    if (locations[0] == null) {
      findAddress(getStartText(), true);
      return;
    }

    if (locations[1] == null) {
      findAddress(getEndText(), false);
      return;
    }

    if (locations[0] != null && locations[1] != null) {
      showProgress(false);
      if (listener != null) {
        listener.onNext(locations[0], locations[1]);
      }
    }
  }

  private void findAddress(String address, boolean start) {
    placesService.getAutoCompleteAsync(address)
        .compose(fragment.bindToLifecycle())
        .subscribe(predictions -> {
          if (predictions.size() > 0) {
            final String placeId = predictions.get(0).getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(activity.getGoogleApiClient(), placeId);
            placeResult.setResultCallback(places -> {
              if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                showProgress(false);
                showToast(places.getStatus().toString());
                places.release();
                return;
              }
              final Place place1 = places.get(0);
              Location location = new Location(place1, getContext());
              if (location == null) {
                Timber.e("Location is null %s %s", place1.toString(), location.toString());
                showProgress(false);
                showToast("Error");
              } else if (start) {
                setStartLocation(location);
              } else {
                setEndLocation(location);
              }
              onNextButton();
              places.release();
            });
          } else {
            showProgress(false);
            showToast("The address you entered is invalid.");
          }
        }, error -> {
          Timber.e(error.toString());
          showToast(error.toString());
          showProgress(false);
        });
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    compositeSubscription = new CompositeSubscription();
  }

  @Override
  protected void onDetachedFromWindow() {
    compositeSubscription.unsubscribe();
    this.activity = null;
    this.fragment = null;
    super.onDetachedFromWindow();
  }

  private String getStartText() {
    return this.startEditText.getText().toString();
  }

  private String getEndText() {
    return this.endEditText.getText().toString();
  }

  private void showToast(String error) {
    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
  }

  private void showProgress(boolean show) {
    progressBar.setVisibility(show ? VISIBLE : GONE);
    nextButton.setVisibility(show ? GONE : VISIBLE);
  }

  public interface StartEndListener {
    void onNext(Location start, Location end);
  }
}
