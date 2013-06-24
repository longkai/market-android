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
package gxu.software_engineering.market.android.service;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import cn.longkai.android.util.NetworkUtils;
import cn.longkai.android.util.RESTMethod;

import gxu.software_engineering.market.android.MarketApp;
import gxu.software_engineering.market.android.R;
import gxu.software_engineering.market.android.util.C;
import gxu.software_engineering.market.android.util.Processor;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-25
 */
public class SyncService extends IntentService {

	private static final String TAG = SyncService.class.getSimpleName();
	
	private Handler handler;
	private MarketApp app;
	
	public SyncService() {
		super(TAG);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = MarketApp.marketApp();
		handler = new Handler();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
//		没有网络不同步。
		if (!NetworkUtils.connected(getApplicationContext())) {
			return;
		}
//		获取上一次同步的时间。
		long lastSyncMills = app.getPrefs().getLong(C.LAST_SYNC, 0);
		if (lastSyncMills == 0L) {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, 1980);
			lastSyncMills = c.getTimeInMillis();
		}
		
		String uri = C.DOMAIN
				+ String.format("/sync?count=%d&last=%s"
						, C.DEFAULT_LIST_SIZE, lastSyncMills);
		
		JSONObject result = RESTMethod.get(uri);
		boolean nice = false;
		ContentValues[] categories = null;
		ContentValues[] users = null;
		ContentValues[] items = null;
		try {
			if (result.getInt(C.STATUS) == C.OK) {
				nice = true;
				categories = Processor.toCategories(result.getJSONArray(C.CATEGORIES));
				users = Processor.toUsers(result.getJSONArray(C.USERS));
				items = Processor.toItems(result.getJSONArray(C.ITEMS));
			} else {
				throw new RuntimeException(result.getString(C.MSG));
			}
		} catch (Exception e) {
			Log.wtf("synv json result error!", e);
			final String msg = e.getMessage();
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(),
								getResources().getString(R.string.sync_error, msg), Toast.LENGTH_SHORT).show();
				}
			});
		}
		
		if (nice) {
			getContentResolver().bulkInsert(Uri.parse(C.BASE_URI + C.CATEGORIES), categories);
			getContentResolver().bulkInsert(Uri.parse(C.BASE_URI + C.USERS), users);
			getContentResolver().bulkInsert(Uri.parse(C.BASE_URI + C.ITEMS), items);
			
			final long now = System.currentTimeMillis();
			
//			写入应用记录中
			app.getPrefs().edit().putLong(C.LAST_SYNC, now).commit();
			
//			提示用户已经更新！
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.sync_nice, DateUtils.getRelativeTimeSpanString(now)), Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

}
