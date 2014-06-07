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

import gxu.software_engineering.market.android.util.NetworkUtils;
import gxu.software_engineering.market.android.util.RESTMethod;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import gxu.software_engineering.market.android.MarketApp;
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
import android.os.Process;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 卖家修改个人信息窗口界面片段。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-23
 */
public class EditUserInfoBoxFragment extends DialogFragment implements DialogInterface.OnClickListener {

	private ProgressDialog progressDialog;
	private View v;
	private int type;
	private MarketApp app;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = getArguments();
		type = args.getInt(C.USER_INFO_MODIFY_TYPE);
		app = MarketApp.marketApp();
		switch (type) {
		case C.CONTACT:
			v = getActivity().getLayoutInflater().inflate(R.layout.edit_contact, null);
			break;
		default:
		case C.PASSWORD:
			v = getActivity().getLayoutInflater().inflate(R.layout.edit_password, null);
			break;
		}
		return new AlertDialog.Builder(getActivity(), android.R.style.Theme_Holo_Dialog)
			.setView(v)
			.setTitle(R.string.edit_user_info)
			.setIcon(type == C.CONTACT ? R.drawable.hardware_phone : R.drawable.device_access_secure)
			.setNegativeButton(R.string.no, null)
			.setPositiveButton(R.string.submit, this)
			.create();
	}
	
	private class UpdateUserInfo extends AsyncTask<String, Void, ContentValues> {

		private boolean connected;
		
		@Override
		protected ContentValues doInBackground(String... params) {
			ContentValues user = null;
			if (connected) {
				long uid = app.getPrefs().getLong(C.UID, -1);
				String httpUri = C.DOMAIN + String.format("/users/%d/modify", uid);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				if (type == C.CONTACT) {
					nameValuePairs.add(new BasicNameValuePair("type", "0"));
				} else {
					nameValuePairs.add(new BasicNameValuePair("type", "1"));
				}
				nameValuePairs.add(new BasicNameValuePair("value", params[0]));
				
				try {
					JSONObject result = RESTMethod.put(httpUri, new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
					Log.i("update user result", result.toString());
					if (result.getInt(C.STATUS) == C.OK) {
						user = Processor.toUser(result.getJSONObject(C.USER));
					}
				} catch (Exception e) {
					Log.wtf("update user error!", e);
					user = null;
				}
			}
			return user;
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
			progressDialog.dismiss();
		}
		
	}
	
	private String resolveValue(View v) {
		EditText p = (EditText) v.findViewById(R.id.password);
		String pwd = p.getEditableText().toString();
		String _pwd = app.getPrefs().getString(C.user.PASSWORD, null);
		Log.i("pwd!!!", _pwd);
		if (!_pwd.equals(pwd)) {
			return null;
		}
		if (type == C.CONTACT) {
			EditText e1 = (EditText) v.findViewById(R.id.contact);
			String contact = e1.getEditableText().toString();
			return contact;
		} else {
			EditText p1 = (EditText) v.findViewById(R.id.new_password);
			String passwrod = p1.getEditableText().toString();
			EditText p2 = (EditText) v.findViewById(R.id.confirmed_password);
			String passwrod2 = p2.getEditableText().toString();
			if (!passwrod.trim().equals(passwrod2)) {
				Toast.makeText(getActivity(), R.string.password_not_match, Toast.LENGTH_SHORT).show();
				return null;
			}
			return passwrod;
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		String value =  resolveValue(v);
		if (value == null) {
			Toast.makeText(getActivity(), R.string.password_wrong, Toast.LENGTH_SHORT).show();
			return;
		}
		ContentValues user = null;
		try {
			 user = new UpdateUserInfo().execute(value).get();
		} catch (Exception e) {
			Toast.makeText(getActivity(), R.string.optr_fail, Toast.LENGTH_SHORT).show();
			Log.wtf("wrong with wait the asnytask reslt!!!", e);
		}
		if (user == null) {
			Toast.makeText(getActivity(), R.string.optr_fail, Toast.LENGTH_SHORT).show();
		} else {
			getActivity().getContentResolver().insert(Uri.parse(C.BASE_URI + C.USERS), user);
			if (type == C.PASSWORD) {
				app.getPrefs().edit().putString(C.user.PASSWORD, value).commit();
			}
			Toast.makeText(getActivity(), R.string.optr_ok, Toast.LENGTH_SHORT).show();
		}
	}

}
