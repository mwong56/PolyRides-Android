package com.mwong56.polyrides.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.models.Message;
import com.mwong56.polyrides.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by micha on 10/25/2015.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

  public static final int INCOMING_MESSAGE = 0;
  public static final int OUTGOING_MESSAGE = 1;

  private Context context;
  private List<Message> messageList;

  public MessageListAdapter(Context context, List<Message> messageList) {
    this.context = context;
    this.messageList = messageList;
  }


  @Override
  public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    int resource;

    if (viewType == INCOMING_MESSAGE) {
      resource = R.layout.list_item_message_in;
    } else {
      resource = R.layout.list_item_message_out;
    }

    View view = inflater.inflate(resource, parent, false);
    return new MessageViewHolder(view);
  }

  @Override
  public void onBindViewHolder(MessageViewHolder holder, int position) {
    Message message = messageList.get(position);
    Picasso.with(context).load(Utils.getProfileImageUrl(message.getUserId())).into(holder.avatar);
    holder.message_text_view.setText(message.getText());
  }

  public void addMessage(Message message) {
    messageList.add(message);
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return messageList.size();
  }

  private Message getItem(int position) {
    return messageList.get(position);
  }

  @Override
  public int getItemViewType(int position) {
    Message item = getItem(position);
    return item.isOutgoingMessage() ? OUTGOING_MESSAGE : INCOMING_MESSAGE;
  }

  public static class MessageViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.avatar)
    protected CircleImageView avatar;

    @Bind(R.id.text_view)
    protected TextView message_text_view;

    @Bind(R.id.date_view)
    protected TextView date_text_view;

    public MessageViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

  }

}



