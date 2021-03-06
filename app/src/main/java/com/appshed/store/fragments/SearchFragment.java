package com.appshed.store.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.appshed.store.R;
import com.appshed.store.dialogs.AppDetailDialog;
import com.appshed.store.adapters.AppAdapter;
import com.appshed.store.entities.App;
import com.appshed.store.tasks.RetrieveSearchApps;
import com.appshed.store.utils.SystemUtils;
import com.rightutils.rightutils.collections.RightList;

/**
 * Created by Anton Maniskevich on 8/22/14.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {

	private ListView listView;
	private EditText search;
	private View progressBar;
	private AppAdapter adapter;
	private View emptyList;
	private RightList<App> apps = new RightList<App>();

	public static SearchFragment newInstance() {
		SearchFragment fragment = new SearchFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search, null);
		listView = (ListView) view.findViewById(R.id.list_view);
		emptyList = view.findViewById(R.id.img_empty_list);
		search = (EditText) view.findViewById(R.id.f_search);
		progressBar = view.findViewById(R.id.progress_bar);
		search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					doSearch();
					return true;
				}
				return false;
			}
		});
		view.findViewById(R.id.img_search).setOnClickListener(this);
		showOrHideEmptyList();
		return view;
	}

	public void addApps(RightList<App> newApps) {
		this.apps = newApps;
		showOrHideEmptyList();
		adapter = new AppAdapter(getActivity(), apps, SystemUtils.cache.getAppLayout());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(getActivity(), AppDetailDialog.class).putExtra(App.class.getSimpleName(), adapter.getItem(position)));
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_search:
				doSearch();
				break;
		}
	}

	public void doSearch() {
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.search.getWindowToken(), 0);
		String query = search.getText().toString().trim();
		if (!query.isEmpty()) {
			new RetrieveSearchApps(getActivity(), progressBar, SearchFragment.this).setQuery(query).execute();
		}
	}

	public void updateListView() {
		if (adapter != null) {
			adapter.changeLayout(SystemUtils.cache.getAppLayout());
		}
	}

	private void showOrHideEmptyList() {
		if (apps.isEmpty()) {
			emptyList.setVisibility(View.VISIBLE);
		} else {
			emptyList.setVisibility(View.GONE);
		}
	}
}
