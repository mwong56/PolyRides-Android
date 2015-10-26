package com.mwong56.polyrides;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.mwong56.polyrides.models.Messages;
import com.trello.rxlifecycle.components.RxActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by micha on 10/25/2015.
 */
public class MessageActivity extends RxActivity {

  @Bind(R.id.recycler_view)
  RecyclerView recyclerView;

  private Messages messages;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.view_recycler_view);
    ButterKnife.bind(this);

    this.messages = getIntent().getParcelableExtra("messages");


  }
}
