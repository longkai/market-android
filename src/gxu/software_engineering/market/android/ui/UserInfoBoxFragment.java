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
import gxu.software_engineering.market.android.activity.ItemsActivity;
import gxu.software_engineering.market.android.util.C;
import gxu.software_engineering.market.android.util.ServiceHelper;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

/**
 * 用户信息界面片段。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-23
 */
public class UserInfoBoxFragment extends DialogFragment {

	public static final String[] NAMES = {"真实姓名：", "联系方式：", "加入时间："};
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		long id = getArguments().getLong(C.ID);
		final Cursor c = getActivity().getContentResolver().query(Uri.parse(C.BASE_URI + C.USERS + "/" + id), null, null, null, null);
		
		if (!c.moveToNext()) {
			throw new RuntimeException("sorry, not found this person!");
		}
		
		long mills = c.getLong(c.getColumnIndex(C.user.REGISTER_TIME));
		String[] infos = new String[NAMES.length];
		infos[0] = NAMES[0] + c.getString(c.getColumnIndex(C.user.REAL_NAME));
		infos[1] = NAMES[1] + c.getString(c.getColumnIndex(C.user.CONTACT));
		infos[2] = NAMES[2] + DateUtils.getRelativeTimeSpanString(mills).toString();
		
		builder.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, infos), null);
		builder.setTitle(c.getString(c.getColumnIndex(C.user.NICK))).setIcon(R.drawable.social_person)
			.setNegativeButton(R.string.close, null).setIcon(R.drawable.ic_launcher)
			.setPositiveButton(R.string.items_by_seller, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(getActivity(), ItemsActivity.class);
					intent.putExtra(C.ITEMS_TYPE, ServiceHelper.USER_ITEMS);
					intent.putExtra(C.UID, c.getLong(c.getColumnIndex(C._ID)));
					intent.putExtra(C.user.NICK, c.getString(c.getColumnIndex(C.user.NICK)));
					intent.putExtra(C.DEAL, 0);
					intent.putExtra(C.COUNT, C.DEFAULT_LIST_SIZE);
					getActivity().startActivity(intent);
				}
			});
		return builder.create();
	}

}
