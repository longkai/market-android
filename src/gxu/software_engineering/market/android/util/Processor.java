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

import android.content.ContentValues;

/**
 * 解析器。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-23
 */
public class Processor {

	public static ContentValues toCategory(JSONObject json) throws JSONException {
		ContentValues values = new ContentValues();
		values.put(C._ID, json.getLong(C.ID));
		values.put(C.category.NAME, json.getString(C.category.NAME));
		values.put(C.category.EXTRA, json.getString(C.category.EXTRA));
		values.put(C.category.ADDED_TIME, json.getLong(C.category.ADDED_TIME));
		values.put(C.category.DESCRIPTION, json.getString(C.category.DESCRIPTION));
		return values;
	}
	
	public static ContentValues[] toCategories(JSONArray array) throws JSONException {
		ContentValues[] values = new ContentValues[array.length()];
		for (int i = 0; i < array.length(); i++) {
			values[i] = toCategory(array.getJSONObject(i));
		}
		return values;
	}
	
	public static ContentValues toUser(JSONObject json) throws JSONException {
		ContentValues values = new ContentValues();
		values.put(C._ID, json.getLong(C.ID));
		values.put(C.user.ACCOUNT, json.getString(C.user.ACCOUNT));
		values.put(C.user.CONTACT, json.getString(C.user.CONTACT));
		values.put(C.user.LAST_LOGIN_TIME, json.getLong(C.user.LAST_LOGIN_TIME));
		values.put(C.user.LOGIN_TIMES, json.getLong(C.user.LOGIN_TIMES));
		values.put(C.user.REAL_NAME, json.getString(C.user.REAL_NAME));
		values.put(C.user.NICK, json.getString(C.user.NICK));
		values.put(C.user.REGISTER_TIME, json.getLong(C.user.REGISTER_TIME));
		return values;
	}
	
	public static ContentValues[] toUsers(JSONArray array) throws JSONException {
		ContentValues[] values = new ContentValues[array.length()];
		for (int i = 0; i < values.length; i++) {
			values[i] = toUser(array.getJSONObject(i));
		}
		return values;
	}
	
	public static ContentValues toItem(JSONObject json) throws JSONException {
		ContentValues values = new ContentValues();
		JSONObject category = json.getJSONObject(C.CATEGORY);
		JSONObject seller = json.getJSONObject(C.SELLER);
		values.put(C.item.ADDED_TIME, json.getLong(C.item.ADDED_TIME));
		values.put(C.item.CATEGORY, category.getString(C.category.NAME));
		values.put(C.item.CLICK_TIMES, json.getLong(C.item.CLICK_TIMES));
		values.put(C.item.CLOSED, json.getString(C.item.CLOSED));
		values.put(C.item.DEAL, json.getString(C.item.DEAL));
		values.put(C.item.DESCRIPTION, json.getString(C.item.DESCRIPTION));
		values.put(C.item.EXTRA, json.getString(C.item.EXTRA));
		values.put(C.item.LAST_MODIFIED_TIME, json.getLong(C.item.LAST_MODIFIED_TIME));
		values.put(C.item.NAME, json.getString(C.item.NAME));
		values.put(C.item.PRICE, json.getDouble(C.item.PRICE));
		values.put(C.item.SELLER, seller.getString(C.user.NICK));
		values.put(C.item.SELLER_ID, seller.getLong(C.ID));
		return values;
	}
	
	public static ContentValues[] toItems(JSONArray array) throws JSONException {
		ContentValues[] values = new ContentValues[array.length()];
		for (int i = 0; i < values.length; i++) {
			values[i] = toItem(array.getJSONObject(i));
		}
		return values;
	}
}
