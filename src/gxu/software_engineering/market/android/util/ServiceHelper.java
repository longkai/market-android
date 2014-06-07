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

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;

/**
 * 服务组件工具类。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-23
 */
public class ServiceHelper {
	
	public static final int CATEGORIES = 1;
	public static final int LASTEST_USERS = 2;
	public static final int LASTEST_ITEMS = 3;
	public static final int HOTTEST_ITEMS = 4;
	public static final int USER_ITEMS = 5;
	public static final int CATEGORY_ITEMS = 6;
	public static final int USER_CLOSED_ITEMS = 7;
	public static final int USER_DEAL_ITEMS = 8;
	
	public static void pre(Intent intent) {
		int target = intent.getIntExtra(C.TARGET_ENTITY, -1);
		long uid = Long.MIN_VALUE;
		long cid = Long.MIN_VALUE;
		switch (target) {
		case CATEGORIES:
			intent.putExtra(C.HTTP_URI, C.DOMAIN + "/categories");
			break;
		case LASTEST_USERS:
			intent.putExtra(C.HTTP_URI, C.DOMAIN + "/users?type=1&count=50");
			break;
		case LASTEST_ITEMS:
			intent.putExtra(C.HTTP_URI, C.DOMAIN + "/items?type=1&count=50");
			break;
		case HOTTEST_ITEMS:
			intent.putExtra(C.HTTP_URI, C.DOMAIN + "/items?type=6&count=20");
			break;
		case USER_ITEMS:
			uid = intent.getLongExtra(C.UID, -1);
			intent.putExtra(C.HTTP_URI, C.DOMAIN + String.format("/items?type=4&count=%d&uid=%d&deal=0&last_id=0", C.DEFAULT_LIST_SIZE, uid));
			break;
		case CATEGORY_ITEMS:
			cid = intent.getLongExtra(C.CID, -1);
			intent.putExtra(C.HTTP_URI, C.DOMAIN + String.format("/items?type=5&count=%d&cid=%d&last_id=0", C.DEFAULT_LIST_SIZE, cid));
			break;
		case USER_CLOSED_ITEMS:
			uid = intent.getLongExtra(C.UID, -1);
			intent.putExtra(C.HTTP_URI, C.DOMAIN + String.format("/items?type=7&count=%d&uid=%d&last_id=0", C.DEFAULT_LIST_SIZE, uid));
			break;
		case USER_DEAL_ITEMS:
			uid = intent.getLongExtra(C.UID, uid);
			intent.putExtra(C.HTTP_URI, C.DOMAIN + String.format("/items?type=4&count=%d&uid=%d&deal=1", C.DEFAULT_LIST_SIZE, uid));
			break;
		default:
			throw new IllegalArgumentException("sorry, 404 for the target[" + target + "]");
		}
	}
	
	public static void doing(ContentResolver contentResolver, Intent intent) throws JSONException {
		String httpUri = intent.getStringExtra(C.HTTP_URI);
		Log.i("http uri", httpUri);
		JSONObject data = null;
		try {
			data = RESTMethod.get(httpUri);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("json result", data.toString());
		JSONArray array = null;
		ContentValues[] items = null;
		switch (intent.getIntExtra(C.TARGET_ENTITY, -1)) {
		case CATEGORIES:
			array = data.getJSONArray(C.CATEGORIES);
			ContentValues[] categories = Processor.toCategories(array);
			contentResolver.bulkInsert(intent.getData(), categories);
			break;
		case LASTEST_USERS:
			array = data.getJSONArray(C.USERS);
			ContentValues[] users = Processor.toUsers(array);
			contentResolver.bulkInsert(intent.getData(), users);
			break;
		case LASTEST_ITEMS:
			array = data.getJSONArray(C.ITEMS);
			items = Processor.toItems(array);
			contentResolver.bulkInsert(intent.getData(), items);
			break;
		case HOTTEST_ITEMS:
			array = data.getJSONArray(C.ITEMS);
			items = Processor.toItems(array);
			contentResolver.bulkInsert(intent.getData(), items);
			break;
		case USER_ITEMS:
			array = data.getJSONArray(C.ITEMS);
			items = Processor.toItems(array);
			contentResolver.bulkInsert(intent.getData(), items);
			break;
		case CATEGORY_ITEMS:
			array = data.getJSONArray(C.ITEMS);
			items = Processor.toItems(array);
			contentResolver.bulkInsert(intent.getData(), items);
			break;
		case USER_CLOSED_ITEMS:
			array = data.getJSONArray(C.ITEMS);
			items = Processor.toItems(array);
			contentResolver.bulkInsert(intent.getData(), items);
			break;
		case USER_DEAL_ITEMS:
			array = data.getJSONArray(C.ITEMS);
			items = Processor.toItems(array);
			contentResolver.bulkInsert(intent.getData(), items);
			break;
		default:
			throw new IllegalArgumentException("sorry, 404 for the target!");
		}
	}

}
