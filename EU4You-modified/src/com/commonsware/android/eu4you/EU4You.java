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
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class EU4You extends SherlockFragmentActivity implements CountryListener {
	private CountriesFragment countries = null;
	private DetailsFragment details = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (savedInstanceState == null) {

			countries = (CountriesFragment) getSupportFragmentManager()
					.findFragmentById(R.id.left_pane);

			if (countries == null) {
				countries = new CountriesFragment();
				getSupportFragmentManager().beginTransaction()
						.add(R.id.left_pane, countries).commit();
			}

			details = (DetailsFragment) getSupportFragmentManager()
					.findFragmentById(R.id.right_pane);

			if (details == null && findViewById(R.id.right_pane) != null) {
				details = new DetailsFragment();
				getSupportFragmentManager().beginTransaction()
						.add(R.id.right_pane, details).commit();
			}
		}
	}

	@Override
	public void onCountrySelected(Country c) {
		String url = getString(c.url);

		if (details != null && details.isVisible()) {
			details.loadUrl(url);
		} else {

			// This code is what I want to avoid. Instead I want to swap out the
			// list fragment
			// with the details fragment. I eventually want similar behaviour,
			// but
			// inside of tabs within an action bar. One activity per tab causes
			// issues.
			// Intent i=new Intent(this, DetailsActivity.class);
			//
			// i.putExtra(DetailsActivity.EXTRA_URL, url);
			// startActivity(i);

			details = new DetailsFragment();
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();

			ft.replace(R.id.left_pane, details);
			// tried the detach attach stuff, but it doesn't fix the issue.
			// ft.detach(countries);
			// ft.attach(details);

			// Removing this line fixes the HOME button issue, but then pushing
			// the BACK
			// button while displaying the DetailsFragment causes the
			// application to go
			// back to the device's home screen.
			ft.addToBackStack(null);
			ft.commit();

			// Removed the following line and changed details.loadUrl so that it
			// stores the Url and loads it when the webview becomes available.
			// getSupportFragmentManager().executePendingTransactions();
			details.loadUrl(url);
		}
	}

	@Override
	public boolean isPersistentSelection() {
		return (details != null && details.isVisible());
	}
}
