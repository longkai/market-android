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

import java.util.concurrent.ExecutionException;

import gxu.software_engineering.market.android.MarketApp;
import gxu.software_engineering.market.android.R;
import gxu.software_engineering.market.android.activity.UserServiceActivity;
import gxu.software_engineering.market.android.util.C;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.longkai.android.util.NetworkUtils;
import cn.longkai.android.util.RESTMethod;

/**
 * 卖家登陆框。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-22
 */
public class LoginBoxFragment extends DialogFragment {

	private ProgressDialog progressDialog;
	private MarketApp app;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = MarketApp.marketApp();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final View loginForm = getActivity().getLayoutInflater().inflate(R.layout.login, null);
		return new AlertDialog.Builder(getActivity(), android.R.style.Theme_Holo_Dialog)
			.setIcon(R.drawable.social_person)
			.setTitle(R.string.login)
			.setView(loginForm)
			.setNegativeButton(R.string.no, null)
			.setPositiveButton(R.string.ok, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					app = MarketApp.marketApp();
					
					EditText e1 = (EditText) loginForm.findViewById(R.id.account);
					final String account = e1.getEditableText().toString();
					EditText e2 = (EditText) loginForm.findViewById(R.id.password);
					final String password = e2.getEditableText().toString();

					new Handler().post(new Runnable() {
						@Override
						public void run() {
							Long uid = Long.MIN_VALUE;
							try {
								uid = new Login().execute(account, password).get();
							} catch (Exception e) {
								Toast.makeText(getActivity(), R.string.login_fail, Toast.LENGTH_SHORT).show();
								return;
							}
							
							if (uid > 0) {
								app.setLoginInfo(uid, account, password);
							}
							
							if (app.hasLogedIn()) {
								Toast.makeText(getActivity(), R.string.login_ok, Toast.LENGTH_SHORT).show();
								getActivity().startActivity(new Intent(getActivity(), UserServiceActivity.class));
							} else {
								Toast.makeText(getActivity(), R.string.wrong_login_info, Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			})
			.create();
	}
	
	private class Login extends AsyncTask<String, Void, Long> {

		private boolean connected;
		
		@Override
		protected Long doInBackground(String... params) {
			if (!connected) {
				return -1L;
			}
			String uri = C.DOMAIN + String.format("/login?account=%s&password=%s", params[0], params[1]);
			Log.i("login uri", uri);
			JSONObject result = RESTMethod.get(uri);
			Log.i("login result", result.toString());
			try {
				if (result.getInt(C.STATUS) == C.OK) {
					return result.getJSONObject(C.USER).getLong(C.ID);
				} else {
					return Long.MIN_VALUE;
				}
			} catch (Exception e) {
				return Long.MIN_VALUE;
			}
		}

		@Override
		protected void onPreExecute() {
			if (NetworkUtils.connected(getActivity())) {
				connected = true;
				progressDialog = new ProgressDialog(getActivity());
				progressDialog.show();
			}
		}

		@Override
		protected void onPostExecute(Long result) {
			progressDialog.dismiss();
		}
		
	}
	
}
