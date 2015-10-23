package com.mwong56.polyrides.views;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.Messages;
import com.mwong56.polyrides.services.FacebookService;
import com.mwong56.polyrides.services.FacebookServiceImpl;
import com.mwong56.polyrides.utils.Utils;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.ribot.easyadapter.ItemViewHolder;
import uk.co.ribot.easyadapter.PositionInfo;
import uk.co.ribot.easyadapter.annotations.LayoutId;
import uk.co.ribot.easyadapter.annotations.ViewId;

/**
 * Created by micha on 10/23/2015.
 */
@LayoutId(R.layout.list_item_messages)
public class MessagesViewHolder extends ItemViewHolder<Messages> {

  @ViewId(R.id.conversation_list_avatar)
  CircleImageView avatar;

  @ViewId(R.id.conversation_list_name)
  TextView name;

  @ViewId(R.id.conversation_list_date)
  TextView date;

  @ViewId(R.id.conversation_list_snippet)
  TextView snippet;

  @ViewId(R.id.conversation_list_unread)
  TextView unreadCount;

  private final FacebookService facebookService = FacebookServiceImpl.get();

  public MessagesViewHolder(View view) {
    super(view);
  }

  @Override
  public void onSetValues(Messages messages, PositionInfo positionInfo) {
    Picasso.with(getContext()).load(Utils.getProfileImageUrl(messages.getUserId2())).into(avatar);
    facebookService.getUserName(AccessToken.getCurrentAccessToken(), messages.getUserId2())
        .subscribe(userName -> name.setText(userName), error -> showToast(error));
    snippet.setText(messages.getLastMessage());
    unreadCount.setText(messages.getCounter());
  }


  protected void showToast(Throwable e) {
    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
  }
}
