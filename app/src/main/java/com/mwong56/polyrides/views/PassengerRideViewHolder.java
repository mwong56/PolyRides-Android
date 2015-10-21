package com.mwong56.polyrides.views;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.utils.Utils;
import com.squareup.picasso.Picasso;

import uk.co.ribot.easyadapter.ItemViewHolder;
import uk.co.ribot.easyadapter.PositionInfo;
import uk.co.ribot.easyadapter.annotations.LayoutId;
import uk.co.ribot.easyadapter.annotations.ViewId;

/**
 * Created by micha on 10/21/2015.
 */
@LayoutId(R.layout.list_item_passenger_ride)
public class PassengerRideViewHolder extends ItemViewHolder<Ride> {

  @ViewId(R.id.card_view)
  CardView view;

  @ViewId(R.id.image)
  ImageView profileImage;

  @ViewId(R.id.city)
  TextView city;

  @ViewId(R.id.time)
  TextView time;


  public PassengerRideViewHolder(View view) {
    super(view);
  }

  @Override
  public void onSetListeners() {
    view.setOnClickListener(v -> {
      RideListener listener = getListener(RideListener.class);
      if (listener != null) {
        listener.onRideClicked(getItem());
      }
    });
  }

  @Override
  public void onSetValues(Ride ride, PositionInfo positionInfo) {
    Picasso.with(getContext()).load(Utils.getProfileImageUrl(ride.getUserId())).into(profileImage);
    city.setText(ride.getStart().getCity() + "-->" + ride.getEnd().getCity());
    time.setText(ride.getDateTime().printDate() + " at " + ride.getDateTime().printTime());
  }

  public interface RideListener {
    void onRideClicked(Ride ride);
  }

}
