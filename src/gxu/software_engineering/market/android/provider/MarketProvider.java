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
package gxu.software_engineering.market.android.provider;

import gxu.software_engineering.market.android.R;
import gxu.software_engineering.market.android.util.C;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

/**
 * 应用程序数据源。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-23
 */
public class MarketProvider extends ContentProvider {
	
	public static final String SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd." + C.AUTHORITY + ".";
	public static final String MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.dir/vnd." + C.AUTHORITY + ".";
	

	private static final int	CATEGORIES						= 0;
	private static final int	CATEGORY						= 1;
	private static final int	USERS						= 2;
	private static final int	USER						= 3;
	private static final int	ITEMS						= 4;
	private static final int	ITEM						= 5;
	
	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		matcher.addURI(C.AUTHORITY, C.CATEGORIES, CATEGORIES);
		matcher.addURI(C.AUTHORITY, C.CATEGORIES + "/#", CATEGORY);
		matcher.addURI(C.AUTHORITY, C.USERS, USERS);
		matcher.addURI(C.AUTHORITY, C.USERS + "/#", USER);
		matcher.addURI(C.AUTHORITY, C.ITEMS, ITEMS);
		matcher.addURI(C.AUTHORITY, C.ITEMS + "/#", ITEM);
	}

	private MarketData data;
	
	@Override
	public boolean onCreate() {
		data = new MarketData(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (matcher.match(uri)) {
		case CATEGORIES:
			return MULTIPLE_RECORDS_MIME_TYPE + C.CATEGORIES;
		case CATEGORY:
			return SINGLE_RECORD_MIME_TYPE + C.CATEGORY;
		case USERS:
			return MULTIPLE_RECORDS_MIME_TYPE + C.USERS;
		case USER:
			return SINGLE_RECORD_MIME_TYPE + C.USER;
		case ITEMS:
			return MULTIPLE_RECORDS_MIME_TYPE + C.ITEMS;
		case ITEM:
			return SINGLE_RECORD_MIME_TYPE + C.ITEM;
		default:
			throw new IllegalArgumentException("404 for the URI[" + uri + "]");
		}
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = data.getReadableDatabase();
		Cursor cursor = null;
		
		sortOrder = TextUtils.isEmpty(sortOrder) ? C.DESC_SORT : sortOrder;
		switch (matcher.match(uri)) {
		case CATEGORIES:
			cursor = db.query(C.CATEGORIES, projection, selection, selectionArgs, null, null, sortOrder);
			break;
		case CATEGORY:
			selection = " _id = " + uri.getLastPathSegment();
			cursor = db.query(C.CATEGORIES, projection, selection, selectionArgs, null, null, null);
			break;
		case USERS:
			cursor = db.query(C.USERS, projection, selection, selectionArgs, null, null, sortOrder);
			break;
		case USER:
			selection = " _id = " + uri.getLastPathSegment();
			cursor = db.query(C.USERS, projection, selection, selectionArgs, null, null, null);
			break;
		case ITEMS:
			cursor = db.query(C.ITEMS, projection, selection, selectionArgs, null, null, sortOrder);
			break;
		case ITEM:
			selection = " _id = " + uri.getLastPathSegment();
			cursor = db.query(C.ITEM, projection, selection, selectionArgs, null, null, null);
			break;
		default:
			throw new IllegalArgumentException("404 for the URI[" + uri + "]");
		}
		
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}


	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String table = null;
		switch (matcher.match(uri)) {
		case CATEGORIES:
			table = C.CATEGORIES;
			break;
		case USERS:
			table = C.USERS;
			break;
		case ITEMS:
			table = C.ITEMS;
			break;
		default:
			throw new IllegalArgumentException("404 for the URI[" + uri + "]");
		}
		SQLiteDatabase db = data.getWritableDatabase();
		long id = db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		getContext().getContentResolver().notifyChange(uri, null, false);
		return ContentUris.withAppendedId(uri, id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException("SORRY, CUTTENT NOT AVAILABLE!");
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		throw new UnsupportedOperationException("SORRY, CUTTENT NOT AVAILABLE!");
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		String table = null;
		switch (matcher.match(uri)) {
		case CATEGORIES:
			table = C.CATEGORIES;
			break;
		case USERS:
			table = C.USERS;
			break;
		case ITEMS:
			table = C.ITEMS;
			break;
		default:
			throw new IllegalArgumentException("404 for the URI[" + uri + "]");
		}
		
		SQLiteDatabase db = data.getWritableDatabase();
		db.beginTransaction();
		
		for (int i = 0; i < values.length; i++) {
			db.insertWithOnConflict(table, null, values[i], SQLiteDatabase.CONFLICT_REPLACE);
		}
		
		db.setTransactionSuccessful();
		db.endTransaction();
		getContext().getContentResolver().notifyChange(uri, null, false);
		return values.length;
	}

	private class MarketData extends SQLiteOpenHelper {
		
		public MarketData(Context context) {
			super(context, context.getResources().getString(R.string.db_name), null, context.getResources().getInteger(R.integer.db_version));
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			StringBuilder sql = new StringBuilder("CREATE TABLE ");
			sql.append(C.CATEGORIES).append(" (")
				.append(C._ID).append(" integer primary key, ")
				.append(C.category.NAME).append(" varchar(18), ")
				.append(C.category.EXTRA).append(" varchar(255), ")
				.append(C.category.ADDED_TIME).append(" varchar(30), ")
				.append(C.category.DESCRIPTION).append(" varchar(255))");
			db.execSQL(sql.toString());
			
			sql.setLength(0);
			sql.append("CREATE TABLE ").append(C.USERS).append(" (")
				.append(C._ID).append(" integer primary key, ")
				.append(C.user.NICK).append(" varchar(7), ")
				.append(C.user.ACCOUNT).append(" varchar(18), ")
				.append(C.user.CONTACT).append(" varchar(11), ")
				.append(C.user.REAL_NAME).append(" varchar(4), ")
				.append(C.user.LOGIN_TIMES).append(" integer, ")
				.append(C.user.REGISTER_TIME).append(" varchar(30), ")
				.append(C.user.LAST_LOGIN_TIME).append(" varchar(30))");
			db.execSQL(sql.toString());
			
			sql.setLength(0);
			sql.append("CREATE TABLE ").append(C.ITEMS).append(" (")
				.append(C._ID).append(" integer primary key, ")
				.append(C.item.ADDED_TIME).append(" varchar(30), ")
				.append(C.item.NAME).append(" varchar(18), ")
				.append(C.item.CATEGORY).append(" integer, ")
				.append(C.item.CLICK_TIMES).append(" varchar(11), ")
				.append(C.item.CLOSED).append(" varchar(5), ")
				.append(C.item.DEAL).append(" varchar(5), ")
				.append(C.item.DESCRIPTION).append(" varchar(255), ")
				.append(C.item.LAST_MODIFIED_TIME).append(" varchar(30), ")
				.append(C.item.PRICE).append(" float, ")
				.append(C.item.SELLER).append(" varchar(20), ")
				.append(C.item.SELLER_ID).append(" integer, ")
				.append(C.item.EXTRA).append(" varchar(255))");
			db.execSQL(sql.toString());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + C.CATEGORIES);
			db.execSQL("DROP TABLE IF EXISTS " + C.USERS);
			db.execSQL("DROP TABLE IF EXISTS " + C.ITEMS);
			onCreate(db);
		}
		
	}

}
