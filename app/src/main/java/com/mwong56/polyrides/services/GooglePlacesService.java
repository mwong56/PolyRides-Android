package com.mwong56.polyrides.services;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.CharacterStyle;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by micha on 12/5/2015.
 */
public interface GooglePlacesService {

  ArrayList<AutoCompleteResult> getAutoComplete(String input);

  Observable<ArrayList<AutoCompleteResult>> getAutoCompleteAsync(final String input);

  class AutoCompleteResult {
    String description;
    String placeId;
    String primaryText;
    String secondaryText;

    public AutoCompleteResult(String description, String placeId) {
      this.description = description;
      this.placeId = placeId;
      String[] split = this.description.split(",", 2);
      primaryText = split[0];
      secondaryText = split[1];
    }

    public String getDescription() {
      return description;
    }

    public String getPlaceId() {
      return placeId;
    }

    public CharSequence getPrimaryText(CharacterStyle characterStyle) {
      SpannableString str = new SpannableString(this.primaryText);
      str.setSpan(characterStyle, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      return str;
    }

    public CharSequence getSecondaryText(CharacterStyle characterStyle) {
      SpannableString str = new SpannableString(this.secondaryText);
      str.setSpan(characterStyle, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      return str;
    }
  }
}
