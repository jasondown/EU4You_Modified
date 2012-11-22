/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain	a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS,	WITHOUT	WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
	
  From _The Busy Coder's Guide to Android Development_
    http://commonsware.com/Android
 */

package com.commonsware.android.eu4you;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public class CountriesFragment extends SherlockListFragment {
	static private final String STATE_CHECKED = "com.commonsware.android.eu4you.STATE_CHECKED";

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);

		setRetainInstance(true);
		setListAdapter(new CountryAdapter());

		if (state != null) {

			int position = state.getInt(STATE_CHECKED, -1);

			if (position > -1) {
				getListView().setItemChecked(position, true);
			}
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		CountryListener listener = (CountryListener) getActivity();

		if (listener.isPersistentSelection()) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			l.setItemChecked(position, true);
		} else {
			getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
		}

		listener.onCountrySelected(Country.EU.get(position));
	}

	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);

		// This is the line that blows up when pressing the HOME button if the
		// DetailsFragment has replaced the CountriesFragment. I tried using a 
		// member variable to track the state instead, but it did not work.
		state.putInt(STATE_CHECKED, getListView().getCheckedItemPosition());
	}

	class CountryAdapter extends ArrayAdapter<Country> {
		CountryAdapter() {
			super(getActivity(), R.layout.row, R.id.name, Country.EU);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CountryViewHolder wrapper = null;

			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.row, null);
				wrapper = new CountryViewHolder(convertView);
				convertView.setTag(wrapper);
			} else {
				wrapper = (CountryViewHolder) convertView.getTag();
			}

			wrapper.populateFrom(getItem(position));

			return (convertView);
		}
	}
}
