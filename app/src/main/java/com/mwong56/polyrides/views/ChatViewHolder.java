package com.mwong56.polyrides.views;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.Chat;
import com.mwong56.polyrides.services.FacebookService;
import com.mwong56.polyrides.services.FacebookServiceImpl;
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
 * Created by micha on 10/23/2015.
 */
@LayoutId(R.layout.list_item_chats)
public class ChatViewHolder extends ItemViewHolder<Chat> {

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

  @ViewId(R.id.conversation_list_badges)
  LinearLayout unreadView;

  private final Bus bus = BusHolder.get();
  private final FacebookService facebookService = FacebookServiceImpl.get();

  public ChatViewHolder(View view) {
    super(view);
    getView().setOnClickListener(v -> {
      ChatListenerEvent event = new ChatListenerEvent(getItem());
      bus.post(event);
    });
  }

  @Override
  public void onSetValues(Chat chat, PositionInfo positionInfo) {
    Picasso.with(getContext()).load(Utils.getProfileImageUrl(chat.getOtherUserId())).into(avatar);
    facebookService.getUserName(AccessToken.getCurrentAccessToken(), chat.getOtherUserId())
        .subscribe(name::setText, this::showToast);
    snippet.setText(chat.getLastMessage());
    if (chat.getCounter() > 0) {
      unreadView.setVisibility(View.VISIBLE);
      unreadCount.setText(chat.getCounter() + "");
    } else {
      unreadView.setVisibility(View.GONE);
    }
  }


  protected void showToast(Throwable e) {
    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
  }

  public class ChatListenerEvent {
    public Chat chat;

    public ChatListenerEvent(Chat chat) {
      this.chat = chat;
    }
  }
}
