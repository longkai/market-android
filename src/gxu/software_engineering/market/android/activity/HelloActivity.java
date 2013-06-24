/*
 * Copyright 2013 longkai
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

import gxu.software_engineering.market.android.MainActivity;
import gxu.software_engineering.market.android.R;
import gxu.software_engineering.market.android.service.SyncService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * 首次进入应用时出现的界面。
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-9
 */
public class HelloActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hello);
		
		Intent sync = new Intent(this, SyncService.class);
		startService(sync);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(HelloActivity.this, MainActivity.class));
			}
		}, getResources().getInteger(R.integer.splash_time));
	}
	
}
