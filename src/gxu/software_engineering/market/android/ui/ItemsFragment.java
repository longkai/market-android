/*
 * Copyright 2013 Department of Computer Science and Technology, Guangxi University
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package gxu.software_engineering.market.android.ui;

import gxu.software_engineering.market.android.activity.ItemActivity;
import gxu.software_engineering.market.android.adapter.ItemsAdapter;
import gxu.software_engineering.market.android.service.FetchService;
import gxu.software_engineering.market.android.util.C;
import gxu.software_engineering.market.android.util.ServiceHelper;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-22
 */
public class ItemsFragment extends ListFragment implements LoaderCallbacks<Cursor> {
	
	private ItemsAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new ItemsAdapter(getActivity());
		setRetainInstance(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListAdapter(mAdapter);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(getActivity(), ItemActivity.class);
		intent.putExtra(C.ID, id);
		getActivity().startActivity(intent);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		int type = getArguments().getInt(C.ITEMS_TYPE, ServiceHelper.LASTEST_ITEMS);
		Log.i("tatget!!!", String.format("%s", type));
		
		Intent intent = new Intent(getActivity(), FetchService.class);
		
		switch (type) {
		default:
		case ServiceHelper.USER_ITEMS:
			intent.putExtra(C.UID, getArguments().getLong(C.UID));
			break;
		case ServiceHelper.CATEGORY_ITEMS:
			intent.putExtra(C.CID, getArguments().getLong(C.CID));
			break;
		}
		
		Uri uri = Uri.parse(C.BASE_URI + C.ITEMS);
		intent.setData(uri);
		intent.putExtra(C.TARGET_ENTITY, type);
		getActivity().startService(intent);
		
		String orderBy = null;
		String selection = null;
		switch (type) {
		case ServiceHelper.HOTTEST_ITEMS:
			selection = C.item.CLOSED + " = 'false' and " + C.item.DEAL + " = 'false'";
			orderBy = C.item.HOTTEST_ORDER;
			break;
		case ServiceHelper.USER_ITEMS:
			selection = C.item.CLOSED + " = 'false' and " + C.item.DEAL + " = 'false' and " + C.item.SELLER_ID + " = " + getArguments().getLong(C.UID);
			break;
		case ServiceHelper.CATEGORY_ITEMS:
			selection = C.item.CLOSED + " = 'false' and " + C.item.DEAL + " = 'false' and " + C.CATEGORY + " = '" + getArguments().getString(C.category.NAME) + "'";
			break;
		case ServiceHelper.USER_CLOSED_ITEMS:
			selection = C.item.CLOSED + " = 'true' and " + C.item.DEAL + " = 'false' and " + getArguments().getLong(C.UID);
			break;
		case ServiceHelper.USER_DEAL_ITEMS:
			selection = C.item.DEAL + " = 'true' and " + getArguments().getLong(C.UID);
			break;
		default:
			selection = C.item.CLOSED + " = 'false' and " + C.item.DEAL + " = 'false'";
//			由于xx脑残收集 的数据比较蛋疼，id大的有些反而添加时间比较前，so。。。
//			orderBy = C.item.LATEST_ORDER;
			break;
		}
		return new CursorLoader(getActivity(), uri, null, selection, null, orderBy);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		mAdapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}
	
}
