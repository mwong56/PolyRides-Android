package com.mwong56.polyrides.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mwong56.polyrides.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class NotesFragment extends BaseRxFragment {

  @Bind(R.id.note)
  EditText noteView;

  public static NotesFragment newInstance() {
    return new NotesFragment();
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_notes, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @OnClick(R.id.next_button)
  void onNextClicked() {
    bus.post(new NotesEvent(noteView.getText().toString()));
  }

  public class NotesEvent {
    public String note;

    public NotesEvent(String note) {
      this.note = note;
    }
  }
}
