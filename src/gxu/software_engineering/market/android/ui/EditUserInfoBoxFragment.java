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

import gxu.software_engineering.market.android.R;
import gxu.software_engineering.market.android.util.C;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Toast;

/**
 * 卖家修改个人信息窗口界面片段。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-23
 */
public class EditUserInfoBoxFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = getArguments();
		View v = null;
		int type = args.getInt(C.USER_INFO_MODIFY_TYPE);
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
			.setPositiveButton(R.string.submit, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(getActivity(), R.string.app_name, Toast.LENGTH_SHORT).show();
				}
			}).create();
	}

}
