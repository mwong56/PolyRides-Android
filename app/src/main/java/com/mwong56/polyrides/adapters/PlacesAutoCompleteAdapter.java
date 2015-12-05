package com.mwong56.polyrides.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mwong56.polyrides.services.GooglePlacesService;
import com.mwong56.polyrides.services.GooglePlacesServiceImpl;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


public class PlacesAutoCompleteAdapter
    extends ArrayAdapter<GooglePlacesService.AutoCompleteResult> implements Filterable {

  private static final String TAG = PlacesAutoCompleteAdapter.class.getSimpleName();
  private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
  private static final GooglePlacesService playService = GooglePlacesServiceImpl.get();

  private CopyOnWriteArrayList<GooglePlacesService.AutoCompleteResult> mResultList;
  private LatLngBounds mBounds;
  private AutocompleteFilter mPlaceFilter;

  public PlacesAutoCompleteAdapter(Context context,
                                   LatLngBounds bounds, AutocompleteFilter filter) {
    super(context, android.R.layout.simple_expandable_list_item_2, android.R.id.text1);
    mBounds = bounds;
    mPlaceFilter = filter;
    mResultList = new CopyOnWriteArrayList<>();
  }


  @Override
  public int getCount() {
    return mResultList.size();
  }

  @Override
  public GooglePlacesService.AutoCompleteResult getItem(int position) {
    return mResultList.get(position);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View row = super.getView(position, convertView, parent);

    // Sets the primary and secondary text for a row.
    // Note that getPrimaryText() and getSecondaryText() return a CharSequence that may contain
    // styling based on the given CharacterStyle.

    //TODO: This .size() == 0 is attempting to fix a crash.
    if (this.mResultList.size() == 0 || position > this.mResultList.size()) {
      return row;
    }

    GooglePlacesService.AutoCompleteResult item = getItem(position);

    TextView textView1 = (TextView) row.findViewById(android.R.id.text1);
    TextView textView2 = (TextView) row.findViewById(android.R.id.text2);
    textView1.setText(item.getPrimaryText(STYLE_BOLD));
    textView2.setText(item.getSecondaryText(null));

    return row;
  }

  /**
   * Returns the filter for the current set of autocomplete results.
   */
  @Override
  public Filter getFilter() {
    return new Filter() {
      @Override
      protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        ArrayList<GooglePlacesService.AutoCompleteResult> resultsList = new ArrayList<>();
        // Skip the autocomplete query if no constraints are given.
        if (constraint != null) {
          // Query the autocomplete API for the (constraint) search string.
          resultsList.addAll(playService.getAutoComplete(constraint.toString()));
          if (resultsList.size() > 0) {
            // The API successfully returned results.
            results.values = resultsList;
            results.count = resultsList.size();
            return results;
          }
        }
        return null;
      }

      @Override
      protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results != null && results.count > 0) {
          // The API returned at least one result, update the data.
          mResultList.clear();
          mResultList.addAll((ArrayList) results.values);
          notifyDataSetChanged();
        } else {
          // The API did not return any results, invalidate the data set.
          notifyDataSetInvalidated();
        }
      }

      @Override
      public CharSequence convertResultToString(Object resultValue) {
        // Override this method to display a readable result in the AutocompleteTextView
        // when clicked.
        if (resultValue instanceof GooglePlacesService.AutoCompleteResult) {
          return ((GooglePlacesService.AutoCompleteResult) resultValue).getDescription();
        } else {
          return super.convertResultToString(resultValue);
        }
      }
    };
  }
}