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
package gxu.software_engineering.market.android.ui;

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

import cn.longkai.android.util.NetworkUtils;
import cn.longkai.android.util.RESTMethod;
import gxu.software_engineering.market.android.R;
import gxu.software_engineering.market.android.util.C;
import gxu.software_engineering.market.android.util.Processor;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 新卖家注册框界面片段。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-23
 */
public class RegisterBoxFragment extends DialogFragment {
	
	private ProgressDialog progressDialog;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Holo_Dialog);
		final View v = getActivity().getLayoutInflater().inflate(R.layout.register, null);
		builder.setIcon(R.drawable.social_add_person).setTitle(R.string.register);
		return builder.setView(v)
			.setNegativeButton(R.string.no, null)
			.setPositiveButton(R.string.ok, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String[] data = data(v);
					ContentValues user = null;
					try {
						user = new Post2Server().execute(data).get();
					} catch (Exception e) {
						Log.wtf("register error!", e);
						Toast.makeText(getActivity(), R.string.register_fail, Toast.LENGTH_SHORT).show();
					}
					Toast.makeText(getActivity(), R.string.register_ok, Toast.LENGTH_SHORT).show();
					if (user != null) {
						getActivity().getContentResolver().insert(Uri.parse(C.BASE_URI + C.USERS), user);
					} else {
						Toast.makeText(getActivity(), R.string.register_fail, Toast.LENGTH_SHORT).show();
					}
				}
			}).create();
	}
	
	private String[] data(View v) {
		EditText account = (EditText) v.findViewById(R.id.account);
		String _account = account.getEditableText().toString();
		EditText password = (EditText) v.findViewById(R.id.password);
		String _password = password.getEditableText().toString();
		EditText confirmed_password = (EditText) v.findViewById(R.id.confirmed_password);
		String _confirmed_password = confirmed_password.getEditableText().toString();
		EditText nick = (EditText) v.findViewById(R.id.nick);
		String _nick = nick.getEditableText().toString();
		EditText contact = (EditText) v.findViewById(R.id.contact);
		String _contact = contact.getEditableText().toString();
		EditText true_name = (EditText) v.findViewById(R.id.true_name);
		String _true_name = true_name.getEditableText().toString();
		return new String[]{_account, _password, _nick, _contact, _true_name, _confirmed_password};
	}
	
	private class Post2Server extends AsyncTask<String, Void, ContentValues> {

		private boolean connected;
		
		@Override
		protected ContentValues doInBackground(String... params) {
			ContentValues values = null;
			if (connected) {
				String uri = C.DOMAIN + "/register";
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		        nameValuePairs.add(new BasicNameValuePair(C.user.ACCOUNT, params[0]));
		        nameValuePairs.add(new BasicNameValuePair(C.user.PASSWORD, params[1]));
		        nameValuePairs.add(new BasicNameValuePair(C.user.NICK, params[2]));
		        nameValuePairs.add(new BasicNameValuePair(C.user.CONTACT, params[3]));
		        nameValuePairs.add(new BasicNameValuePair("realName", params[4]));
		        nameValuePairs.add(new BasicNameValuePair("pwd", params[5]));
		        Log.i("register info", nameValuePairs.toString());
		        JSONObject result = null;
				try {
					result = RESTMethod.post(uri, new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
					Log.i("register result", result.toString());
					try {
						if (result.getInt(C.STATUS) == C.OK) {
							values = Processor.toUser(result.getJSONObject(C.USER));
							return values;
						}
					} catch (JSONException e) {
						Log.wtf("resolve register result error!", e);
					}
				} catch (UnsupportedEncodingException e) {
					Log.wtf("register post error!", e);
				}
			}
			return values;
		}

		@Override
		protected void onPreExecute() {
			if (NetworkUtils.connected(getActivity())) {
				connected = true;
				progressDialog = new ProgressDialog(getActivity());
				progressDialog.setTitle(R.string.publishing);
				progressDialog.show();
			}
		}

		@Override
		protected void onPostExecute(ContentValues result) {
			if (!connected) {
				Toast.makeText(getActivity(), R.string.network_down, Toast.LENGTH_SHORT).show();
				return;
			}
			if (result != null) {
				Log.i("user valeus", result.toString());
			}
			progressDialog.dismiss();
		}
		
	}

}
