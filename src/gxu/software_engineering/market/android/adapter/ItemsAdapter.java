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
package gxu.software_engineering.market.android.adapter;

import gxu.software_engineering.market.android.R;
import gxu.software_engineering.market.android.util.C;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 物品列表适配器。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-23
 */
public class ItemsAdapter extends CursorAdapter {

	public ItemsAdapter(Context context) {
		super(context, null, 0);
	}

	private static class ViewHolder {
		TextView name;
		int nameIndex;
		TextView category;
		int categoryIndex;
		TextView date;
		int dateIndex;
		TextView price;
		int priceIndex;
	}
	
	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		ViewHolder holder = (ViewHolder) arg0.getTag();
		holder.name.setText(arg2.getString(holder.nameIndex));
		holder.category.setText(arg2.getString(holder.categoryIndex));
		long mills = arg2.getLong(holder.dateIndex);
		holder.date.setText(arg1.getResources().getString(R.string.publish_at) + DateUtils.getRelativeTimeSpanString(mills));
		holder.price.setText("￥ " + arg2.getString(holder.priceIndex));
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		View v = LayoutInflater.from(arg0).inflate(R.layout.item_row, null);
		ViewHolder holder = new ViewHolder();
		holder.name = (TextView) v.findViewById(R.id.name);
		holder.category = (TextView) v.findViewById(R.id.category);
		holder.date = (TextView) v.findViewById(R.id.date);
		holder.price = (TextView) v.findViewById(R.id.price);
		
		holder.nameIndex = arg1.getColumnIndex(C.item.NAME);
		holder.categoryIndex = arg1.getColumnIndex(C.item.CATEGORY);
		holder.dateIndex = arg1.getColumnIndex(C.item.ADDED_TIME);
		holder.priceIndex = arg1.getColumnIndex(C.item.PRICE);
		
		v.setTag(holder);
		return v;
	}

}
