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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.longkai.android.util.NetworkUtils;
import cn.longkai.android.util.RESTMethod;
import gxu.software_engineering.market.android.R;
import gxu.software_engineering.market.android.provider.MarketProvider;
import gxu.software_engineering.market.android.util.C;
import gxu.software_engineering.market.android.util.Processor;
import gxu.software_engineering.market.android.util.ServiceHelper;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * 抓取云端数据。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-23
 */
public class FetchService extends IntentService {
	
	private static final String TAG = FetchService.class.getSimpleName();
	
	private Handler handler;
	
	public FetchService() {
		super(TAG);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		handler = new Handler();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (NetworkUtils.connected(getApplicationContext())) {
			try {
				ServiceHelper.pre(intent);
				ServiceHelper.doing(getContentResolver(), intent);
			} catch (JSONException e) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), R.string.resolve_data_error, Toast.LENGTH_SHORT).show();
					}
				});
			}
		} else {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), R.string.network_down, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

}
