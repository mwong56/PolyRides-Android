package com.mwong56.polyrides.views;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
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

  @Bind(R.id.start_from_next_button)
  Button nextButton;

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

  @OnClick(R.id.start_from_next_button)
  void onNextButton() {
    Location[] locations = getPlaces();

    if (locations[0] == null) {
      findAddress(getStartText(), true);
    }

    if (locations[1] == null) {
      findAddress(getEndText(), false);
    }

    if (locations[0] != null && locations[1] != null) {
      EventBus.getDefault().post(new StartEndEvent(locations[0], locations[1]));
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
                showToast(places.getStatus().toString());
                places.release();
                return;
              }
              final Place place1 = places.get(0);
              if (start) {
                setStartLocation(new Location(place1, getContext()));
              } else {
                setEndLocation(new Location(place1, getContext()));
              }
              onNextButton();
              places.release();
            });
          } else {
            showToast("The address you entered is invalid.");
          }
        }, this.activity::showToast);
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

  public class StartEndEvent {
    public Location start;
    public Location end;

    public StartEndEvent(Location start, Location end) {
      this.start = start;
      this.end = end;
    }
  }
}
