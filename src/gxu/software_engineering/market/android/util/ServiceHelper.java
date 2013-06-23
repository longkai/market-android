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
package gxu.software_engineering.market.android.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.longkai.android.util.RESTMethod;
import gxu.software_engineering.market.android.provider.MarketProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 服务组件工具类。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-23
 */
public class ServiceHelper {
	
	public static void pre(Intent intent) {
		int target = intent.getIntExtra(C.TARGET_ENTITY, -1);
		switch (target) {
		case MarketProvider.CATEGORIES:
			intent.putExtra(C.HTTP_URI, C.DOMAIN + "/categories");
			break;
		case MarketProvider.USERS:
			intent.putExtra(C.HTTP_URI, C.DOMAIN + "/users?type=1&count=50");
			break;
		default:
			throw new IllegalArgumentException("sorry, 404 for the target[" + target + "]");
		}
	}
	
	public static void doing(ContentResolver contentResolver, Intent intent) throws JSONException {
		JSONObject data = RESTMethod.get(intent.getStringExtra(C.HTTP_URI));
		Log.i("json result", data.toString());
		JSONArray array = null;
		JSONObject object = null;
		switch (intent.getIntExtra(C.TARGET_ENTITY, -1)) {
		case MarketProvider.CATEGORIES:
			array = data.getJSONArray(C.CATEGORIES);
			ContentValues[] categories = Processor.toCategories(array);
			contentResolver.bulkInsert(intent.getData(), categories);
			break;
		case MarketProvider.USERS:
			array = data.getJSONArray(C.USERS);
			ContentValues[] users = Processor.toUsers(array);
			contentResolver.bulkInsert(intent.getData(), users);
			break;
		default:
			throw new IllegalArgumentException("sorry, 404 for the target!");
		}
	}

}
