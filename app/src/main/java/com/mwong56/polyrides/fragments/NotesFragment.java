package com.mwong56.polyrides.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class NotesFragment extends Fragment {

  @Bind(R.id.note)
  EditText noteView;

  private NotesListener listener;

  public interface NotesListener {
    void onNotesSet(String string);
  }

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

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      listener = (NotesListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString() + " must implement SeatsListener");
    }
  }

  @Override
  public void onDetach() {
    listener = null;
    super.onDetach();
  }

  @OnClick(R.id.next_button)
  void onNextClicked() {
    listener.onNotesSet(noteView.getText().toString());
  }
}
