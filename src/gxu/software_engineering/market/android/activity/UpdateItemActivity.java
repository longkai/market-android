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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import gxu.software_engineering.market.android.MarketApp;
import gxu.software_engineering.market.android.R;
import gxu.software_engineering.market.android.activity.NewItemActivity.CategoriesAdapter;
import gxu.software_engineering.market.android.util.C;
import gxu.software_engineering.market.android.util.Processor;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import cn.longkai.android.util.NetworkUtils;
import cn.longkai.android.util.RESTMethod;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * 更新物品信息界面。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-23
 */
public class UpdateItemActivity extends SherlockFragmentActivity implements LoaderCallbacks<Cursor> {

	private Long id;
	private Long cid;
	private CategoriesAdapter mAdapter;
	private Spinner spinner;
	private Cursor cursor;
	
	private EditText name;
	private EditText price;
	private EditText desc;
	private EditText extra;
	
	
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.new_item);
		id = getIntent().getLongExtra(C.ID, -1);
		
		cursor = getContentResolver().query(Uri.parse(C.BASE_URI + C.ITEMS + "/" + id), null, null, null, null);
		
		if (!cursor.moveToNext()) {
			Toast.makeText(this, R.string.item_not_found, Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
		mAdapter = new CategoriesAdapter(this);
		
		spinner = (Spinner) findViewById(R.id.categories);
		name = (EditText) findViewById(R.id.name);
		price = (EditText) findViewById(R.id.price);
		desc = (EditText) findViewById(R.id.description);
		extra = (EditText) findViewById(R.id.extra);
		
		name.setText(cursor.getString(cursor.getColumnIndex(C.item.NAME)));
		price.setText(cursor.getString(cursor.getColumnIndex(C.item.PRICE)));
		desc.setText(cursor.getString(cursor.getColumnIndex(C.item.DESCRIPTION)));
		String _ = cursor.getString(cursor.getColumnIndex(C.item.EXTRA));
		extra.setText(_.equals("null") ? "" : _);
		this.cid = cursor.getLong(cursor.getColumnIndex(C.item.CID));
		Log.i("cid!!", cid + "");
		getSupportActionBar().setTitle(R.string.edit);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.new_item, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		spinner.setAdapter(mAdapter);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				spinner.setSelection(UpdateItemActivity.this.cid.intValue() - 1);
			}
		}, 100);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				UpdateItemActivity.this.cid = id;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				parent.setSelection(UpdateItemActivity.this.cid.intValue());
			}
		});
		getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		long uid = MarketApp.marketApp().getPrefs().getLong(C.UID, -1);
		switch (item.getItemId()) {
		case R.id.done:
//			Toast.makeText(this, R.string.submit, Toast.LENGTH_SHORT).show();
			String[] data = resolveData();
			ContentValues values = null;
			try {
				values = new PutToCloud().execute(cid+"", uid+"", data[0], data[1], data[2], data[3]).get();
			} catch (Exception e) {
				Toast.makeText(this, R.string.publish_fail, Toast.LENGTH_SHORT).show();
			}
			if (values != null) {
				Toast.makeText(this, R.string.publish_ok, Toast.LENGTH_SHORT).show();
				getContentResolver().insert(Uri.parse(C.BASE_URI + C.ITEMS), values);
				Intent i = new Intent(this, ItemActivity.class);
				i.putExtra(C.ID, this.id);
				startActivity(i);
			} else {
				Toast.makeText(this, R.string.publish_fail, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.discard:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new CursorLoader(this, Uri.parse(C.BASE_URI + C.CATEGORIES), null, null, null, C.ASC_SORT);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		mAdapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (this.cursor != null) {
			cursor.close();
		}
	}
	
	private class PutToCloud extends AsyncTask<String, Void, ContentValues> {

		private boolean connected;
		
		@Override
		protected ContentValues doInBackground(String... params) {
			ContentValues item = null;
			if (connected) {
				String httpUri = C.DOMAIN + String.format("/items/%d/modify", UpdateItemActivity.this.id);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		        nameValuePairs.add(new BasicNameValuePair(C.CID, params[0]));
		        nameValuePairs.add(new BasicNameValuePair(C.UID, params[1]));
		        nameValuePairs.add(new BasicNameValuePair(C.item.NAME, params[2]));
		        nameValuePairs.add(new BasicNameValuePair(C.item.PRICE, params[3]));
		        nameValuePairs.add(new BasicNameValuePair(C.item.DESCRIPTION, params[4]));
		        nameValuePairs.add(new BasicNameValuePair(C.item.PRICE, params[5]));
		        Log.i("http query string!!!", nameValuePairs.toString());
		        JSONObject result = null;
				try {
					result = RESTMethod.put(httpUri, new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
					Log.i("post result", result.toString());
				} catch (UnsupportedEncodingException e) {
					Log.wtf("wtf in http post!!!", e);
					return null;
				}
				
				try {
					if (result.getInt(C.STATUS) == C.OK) {
						item = Processor.toItem(result.getJSONObject(C.ITEM));
					}
				} catch (JSONException e) {
					Log.wtf("wtf in resolving json!!!", e);
				}
			}
			return item;
		}
		@Override
		protected void onPreExecute() {
			if (NetworkUtils.connected(getApplicationContext())) {
				connected = true;
				progressDialog = new ProgressDialog(UpdateItemActivity.this);
				progressDialog.setTitle(R.string.publishing);
				progressDialog.show();
			}
		}

		@Override
		protected void onPostExecute(ContentValues result) {
			if (!connected) {
				Toast.makeText(UpdateItemActivity.this, R.string.network_down, Toast.LENGTH_SHORT).show();
				return;
			}
			progressDialog.dismiss();
			if (result == null) {
				Toast.makeText(UpdateItemActivity.this, R.string.publish_fail, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private String[] resolveData() {
//		name = (EditText) findViewById(R.id.name);
//		price = (EditText) findViewById(R.id.price);
//		desc = (EditText) findViewById(R.id.description);
//		extra = (EditText) findViewById(R.id.extra);
		
		String _name = this.name.getEditableText().toString();
		String _price = this.price.getEditableText().toString();
		String _desc = this.desc.getEditableText().toString();
		String _extra = this.extra.getEditableText().toString();
		return new String[]{_name, _price, _desc, _extra};
	}

}
