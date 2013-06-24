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
import gxu.software_engineering.market.android.ui.ItemFragment;
import gxu.software_engineering.market.android.ui.UserInfoBoxFragment;
import gxu.software_engineering.market.android.util.C;
import gxu.software_engineering.market.android.util.ServiceHelper;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * 查看物品信息界面。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-22
 */
public class ItemActivity extends SherlockFragmentActivity implements OnClickListener {

	private Long uid;
	private Long cid;
	private String cname;
	
	private Cursor c;
	private MarketApp app;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
//		setContentView(cn.longkai.android.R.layout.fragment_container);
		setContentView(R.layout.item);
		
		app = MarketApp.marketApp();
		
		c = getContentResolver()
			.query(Uri.parse(C.BASE_URI + C.ITEMS + "/" + getIntent().getLongExtra(C.ID, -1)), null, null, null, null);
		
		if (!c.moveToNext()) {
			Toast.makeText(this, R.string.item_not_found, Toast.LENGTH_SHORT).show();
			return;
		}
		
		Button category = (Button) findViewById(R.id.category);
		category.setText(c.getString(c.getColumnIndex(C.item.CATEGORY)));
		Button clickTimes = (Button) findViewById(R.id.click_times);
		clickTimes.setText(c.getString(c.getColumnIndex(C.item.CLICK_TIMES)) + " 点击");
		Button name = (Button) findViewById(R.id.name);
		name.setText(c.getString(c.getColumnIndex(C.item.NAME)));
		Button seller = (Button) findViewById(R.id.seller);
		seller.setText("卖家：" + c.getString(c.getColumnIndex(C.item.SELLER)));
		Button price = (Button) findViewById(R.id.price);
		price.setText("价格：" + c.getString(c.getColumnIndex(C.item.PRICE)));
		
		long millis1 = c.getLong(c.getColumnIndex(C.item.ADDED_TIME));
		Button publicAt = (Button) findViewById(R.id.publish_at);
		publicAt.setText("发布日期：" + DateUtils.getRelativeTimeSpanString(millis1));
		
		Button desc = (Button) findViewById(R.id.desc);
		desc.setText(c.getString(c.getColumnIndex(C.item.DESCRIPTION)));
		
		String _extra = c.getString(c.getColumnIndex(C.item.EXTRA));
		Button extra = (Button) findViewById(R.id.extra);
		if (TextUtils.isEmpty(_extra) || _extra.equals("null")) {
			extra.setText(R.string.no_extra);
		} else {
			extra.setText(_extra);
		}
		
		long mills2 = c.getLong(c.getColumnIndex(C.item.LAST_MODIFIED_TIME));
		Button lastModifiedDate = (Button) findViewById(R.id.last_modified_time);
		lastModifiedDate.setText("上次修改日期：" + DateUtils.getRelativeTimeSpanString(mills2));
//		FragmentManager fm = getSupportFragmentManager();
//		if (fm.findFragmentByTag("item") == null) {
//			Fragment fragment = new ItemFragment();
//			fm.beginTransaction()
//				.replace(cn.longkai.android.R.id.fragment_container, fragment, "item")
//				.commit();
//		}
		
		this.uid = c.getLong(c.getColumnIndex(C.item.SELLER_ID));
//		this.cid = getCallingActivity().
		
		this.cid = c.getLong(c.getColumnIndex(C.item.CID));
		this.cname = c.getString(c.getColumnIndex(C.item.CATEGORY));
		
		seller.setOnClickListener(this);
		category.setOnClickListener(this);
		
		getSupportActionBar().setTitle("查看物品信息");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (this.uid == app.getPrefs().getLong(C.UID, -1)) {
			getSupportMenuInflater().inflate(R.menu.item_edit, menu);
		} else {
			getSupportMenuInflater().inflate(R.menu.contact_seller, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.edit:
			Intent intent = new Intent(this, UpdateItemActivity.class);
			intent.putExtra(C.ID, getIntent().getLongExtra(C.ID, -1));
			startActivity(intent);
			break;
		case R.id.call_seller:
			Cursor c = getContentResolver()
					.query(Uri.parse(C.BASE_URI + C.USERS + "/" + this.uid),
							new String[]{C.user.CONTACT}, null, null, null);
			if (!c.moveToNext()) {
				Toast.makeText(this, R.string.contact_not_found, Toast.LENGTH_SHORT).show();
			} else {
				Intent call = new Intent(Intent.ACTION_CALL);
				Uri data = Uri.parse("tel:" + c.getString(0));
				call.setData(data);
				startActivity(call);
			}
			break;
		case R.id.sms_seller:
			Cursor _c = getContentResolver()
			.query(Uri.parse(C.BASE_URI + C.USERS + "/" + this.uid),
					new String[]{C.user.CONTACT}, null, null, null);
			if (!_c.moveToNext()) {
				Toast.makeText(this, R.string.contact_not_found, Toast.LENGTH_SHORT).show();
			} else {
				Intent sms = new Intent(Intent.ACTION_SENDTO);
				Uri data = Uri.parse("tel:" + _c.getString(0));
				sms.setData(data);
				startActivity(sms);
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (c != null) {
			c.close();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.seller:
			UserInfoBoxFragment fragment = new UserInfoBoxFragment();
			Bundle args = new Bundle();
			args.putLong(C.ID, this.uid);
			Log.i("uid!!!", String.format("%d", uid));
			fragment.setArguments(args);
			fragment.show(getSupportFragmentManager(), C.USER);
			break;
		case R.id.category:
			Intent intent = new Intent(this, ItemsActivity.class);
			intent.putExtra(C.ITEMS_TYPE, ServiceHelper.CATEGORY_ITEMS);
			intent.putExtra(C.category.NAME, this.cname);
			intent.putExtra(C.CID, this.cid);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
}
