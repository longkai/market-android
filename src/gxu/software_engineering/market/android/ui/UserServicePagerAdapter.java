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

import gxu.software_engineering.market.android.MarketApp;
import gxu.software_engineering.market.android.util.C;
import gxu.software_engineering.market.android.util.ServiceHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * 用户自服务pager。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-22
 */
public class UserServicePagerAdapter extends FragmentStatePagerAdapter {

	private Fragment[] fragments;
	
	public UserServicePagerAdapter(FragmentManager fm) {
		super(fm);
		long uid = MarketApp.marketApp().getPrefs().getLong(C.UID, -1);
		Log.i("uid", String.format("%d", uid));
		fragments = new Fragment[C.PAGER_SIZE];
		Fragment fragment = null;
		Bundle args = null;
		for (int i = 0; i < C.PAGER_SIZE; i++) {
			switch (i) {
			default:
			case 1:
				args = new Bundle();
				args.putInt(C.ITEMS_TYPE, ServiceHelper.USER_ITEMS);
				args.putLong(C.UID, uid);
				fragment = new ItemsFragment();
				fragment.setArguments(args);
				break;
			case 0:
				args = new Bundle();
				args.putInt(C.ITEMS_TYPE, ServiceHelper.USER_CLOSED_ITEMS);
				args.putLong(C.UID, uid);
				fragment = new ItemsFragment();
				fragment.setArguments(args);
				break;
			case 2:
				args = new Bundle();
				args.putInt(C.ITEMS_TYPE, ServiceHelper.USER_DEAL_ITEMS);
				args.putLong(C.UID, uid);
				fragment = new ItemsFragment();
				fragment.setArguments(args);
				break;
			}
			fragments[i] = fragment;
		}
	}

	
	@Override
	public Fragment getItem(int arg0) {
		return fragments[arg0];
	}

	@Override
	public int getCount() {
		return C.PAGER_SIZE;
	}


	@Override
	public CharSequence getPageTitle(int position) {
		return C.USER_SERVICE_PAGER_TTILES[position];
	}
	
}
