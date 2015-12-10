package com.mwong56.polyrides.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.services.FacebookService;
import com.mwong56.polyrides.services.FacebookServiceImpl;
import com.mwong56.polyrides.utils.Utils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

/**
 * Created by micha on 10/22/2015.
 */
public class RideDetailsView extends LinearLayout {
  @Bind(R.id.name)
  TextView nameTextView;

  @Bind(R.id.profile_image)
  CircleImageView profileImageView;

  @Bind(R.id.location)
  TextView locationTextView;

  @Bind(R.id.date)
  TextView dateTextView;

  @Bind(R.id.time)
  TextView timeTextView;

  @Bind(R.id.seat)
  TextView seatTextView;

  @Bind(R.id.cost)
  TextView costTextView;

  @Bind(R.id.note)
  TextView noteTextView;

  private FacebookService fbService = FacebookServiceImpl.get();

  public RideDetailsView(Context context) {
    super(context);
  }

  public RideDetailsView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RideDetailsView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  public void setup(Ride ride) {
    Timber.d(ride.toString());
    fbService.getUserName(AccessToken.getCurrentAccessToken(), ride.getUserId())
        .subscribe(nameTextView::setText, this::showToast);
    Picasso.with(getContext()).load(Utils.getProfileImageUrl(ride.getUserId())).into(profileImageView);
    locationTextView.setText(ride.getStart().getCity() + " → " + ride.getEnd().getCity());
    dateTextView.setText(ride.getDateTime().printDate());
    timeTextView.setText(ride.getDateTime().printTime());
    costTextView.setText("$" + ride.getCost() + " per seat");
    if (ride.getSeats() > 1) {
      seatTextView.setText(ride.getSeats() + " seats available");
    } else {
      seatTextView.setText(ride.getSeats() + " seat available");
    }
    noteTextView.setText(ride.getNote());
  }

  private void showToast(Throwable e) {
    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
  }
}
