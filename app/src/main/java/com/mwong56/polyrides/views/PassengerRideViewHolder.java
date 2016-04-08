package com.mwong56.polyrides.views;

import android.view.View;
import android.widget.TextView;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.utils.BusHolder;
import com.mwong56.polyrides.utils.Utils;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.ribot.easyadapter.ItemViewHolder;
import uk.co.ribot.easyadapter.PositionInfo;
import uk.co.ribot.easyadapter.annotations.LayoutId;
import uk.co.ribot.easyadapter.annotations.ViewId;

/**
 * Created by micha on 10/21/2015.
 */
@LayoutId(R.layout.list_item_passenger_ride)
public class PassengerRideViewHolder extends ItemViewHolder<Ride> {

  @ViewId(R.id.image)
  CircleImageView profileImage;

  @ViewId(R.id.city)
  TextView city;

  @ViewId(R.id.time)
  TextView time;

  private Bus bus = BusHolder.get();

  public PassengerRideViewHolder(View view) {
    super(view);

  }

  @Override
  public void onSetListeners() {
    getView().setOnClickListener(v -> {
      if (getItem() != null) {
        bus.post(new RideEvent(getItem()));
      }
    });
  }

  @Override
  public void onSetValues(Ride ride, PositionInfo positionInfo) {
    Picasso.with(getContext()).load(Utils.getProfileImageUrl(ride.getUserId())).into(profileImage);
    city.setText(ride.getStart().getCity() + " → " + ride.getEnd().getCity());
    time.setText(ride.getDateTime().printDate() + " at " + ride.getDateTime().printTime());
  }

  public class RideEvent {
    public Ride ride;

    public RideEvent(Ride ride) {
      this.ride = ride;
    }
  }
}
