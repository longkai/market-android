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
package gxu.software_engineering.market.android.activity;

import gxu.software_engineering.market.android.MarketApp;
import gxu.software_engineering.market.android.R;
import gxu.software_engineering.market.android.ui.EditUserInfoBoxFragment;
import gxu.software_engineering.market.android.ui.UserServicePagerAdapter;
import gxu.software_engineering.market.android.util.C;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * 用户服务界面。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-22
 */
public class UserServiceActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		
		FragmentManager fm = getSupportFragmentManager();
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new UserServicePagerAdapter(fm));
		pager.setCurrentItem(1);
		
		getSupportActionBar().setTitle(
				MarketApp.marketApp().getPrefs().getString(C.user.NICK, getResources().getString(R.string.app_name)));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.user_service, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_item:
			Intent intent = new Intent(this, NewItemActivity.class);
			startActivity(intent);
			break;
		case R.id.edit_ccontact:
			EditUserInfoBoxFragment contact = new EditUserInfoBoxFragment();
			Bundle contactArgs = new Bundle();
			contactArgs.putInt(C.USER_INFO_MODIFY_TYPE, C.CONTACT);
			contact.setArguments(contactArgs);
			contact.show(getSupportFragmentManager(), "contact");
			break;
		case R.id.edit_password:
			EditUserInfoBoxFragment pwd = new EditUserInfoBoxFragment();
			Bundle pwdArgs = new Bundle();
			pwdArgs.putInt(C.USER_INFO_MODIFY_TYPE, C.PASSWORD);
			pwd.setArguments(pwdArgs);
			pwd.show(getSupportFragmentManager(), "password");
			break;
		default:
			break;
		}
		return false;
	}

}
