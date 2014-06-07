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
import gxu.software_engineering.market.android.util.C;
import gxu.software_engineering.market.android.util.NetworkUtils;
import gxu.software_engineering.market.android.util.Processor;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import gxu.software_engineering.market.android.util.RESTMethod;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * 新建物品界面。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-22
 */
public class NewItemActivity extends SherlockFragmentActivity implements LoaderCallbacks<Cursor> {

	private ProgressDialog progressDialog;
	
	private CategoriesAdapter mAdapter;
	private Spinner spinner;
	private EditText name;
	private EditText price;
	private EditText desc;
	private EditText extra;
	private Long cid;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.new_item);
		spinner = (Spinner) findViewById(R.id.categories);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				NewItemActivity.this.cid = id;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				NewItemActivity.this.cid = 1L;
			}
		});

		getSupportActionBar().setTitle(R.string.new_item);
		this.mAdapter = new CategoriesAdapter(this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setSubtitle(R.string.hello_world);
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
		getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.discard:
			finish();
			break;
		case R.id.done:
			Toast.makeText(this, R.string.submit, Toast.LENGTH_SHORT).show();
			Log.i("null???", String.format("%s", this.name == null));
			String[] data = resolveData();
			try {
				long uid = MarketApp.marketApp().getPrefs().getLong(C.UID, -1);
				ContentValues values = new PostToCloud().execute(cid+"", uid+"", data[0], data[1], data[2], data[3]).get();
				Log.i("items>>>", values.toString());
				getContentResolver().insert(Uri.parse(C.BASE_URI + C.ITEMS), values);
				Toast.makeText(this, R.string.publish_ok, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(this, ItemActivity.class);
				intent.putExtra(C.ID, values.getAsLong(C._ID));
				startActivity(intent);
			} catch (Exception e) {
				Log.wtf("receive result error!", e);
				Toast.makeText(this, R.string.publish_fail, Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
		return false;
	}
	
	private class PostToCloud extends AsyncTask<String, Void, ContentValues> {

		private boolean connected;
		
		@Override
		protected ContentValues doInBackground(String... params) {
			ContentValues item = null;
			if (connected) {
				String httpUri = C.DOMAIN + "/items/add";
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
					result = RESTMethod.post(httpUri, new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
					Log.i("post result", result.toString());
				} catch (Exception e) {
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
				progressDialog = new ProgressDialog(NewItemActivity.this);
				progressDialog.setTitle(R.string.publishing);
				progressDialog.show();
			}
		}

		@Override
		protected void onPostExecute(ContentValues result) {
			if (!connected) {
				Toast.makeText(NewItemActivity.this, R.string.network_down, Toast.LENGTH_SHORT).show();
				return;
			}
			progressDialog.dismiss();
			if (result == null) {
				Toast.makeText(NewItemActivity.this, R.string.publish_fail, Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	public static class CategoriesAdapter extends CursorAdapter {

		public CategoriesAdapter(Context context) {
			super(context, null, 0);
		}

		private static class ViewHolder {
			TextView name;
			int nameIndex;
		}
		
		@Override
		public void bindView(View arg0, Context arg1, Cursor arg2) {
			ViewHolder holder = (ViewHolder) arg0.getTag();
			holder.name.setText(arg2.getString(holder.nameIndex));
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
			View v = LayoutInflater.from(arg0).inflate(android.R.layout.simple_list_item_1, null);
			ViewHolder holder = new ViewHolder();
			holder.name = (TextView) v.findViewById(android.R.id.text1);
			holder.nameIndex = arg1.getColumnIndex(C.category.NAME);
			v.setTag(holder);
			return v;
		}
		
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
	
	private String[] resolveData() {
		name = (EditText) findViewById(R.id.name);
		price = (EditText) findViewById(R.id.price);
		desc = (EditText) findViewById(R.id.description);
		extra = (EditText) findViewById(R.id.extra);
		
		String _name = this.name.getEditableText().toString();
		String _price = this.price.getEditableText().toString();
		String _desc = this.desc.getEditableText().toString();
		String _extra = this.extra.getEditableText().toString();
		return new String[]{_name, _price, _desc, _extra};
	}
	
}
