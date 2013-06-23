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
package gxu.software_engineering.market.android.util;

/**
 * 常量池。
 * 
 * @author longkai(龙凯)
 * @email  im.longkai@gmail.com
 * @since  2013-6-22
 */
public final class C {
	
	public static final int PAGER_SIZE = 3;
	
	public static final String[] PAGER_TITLES = {"热门物品", "最新物品", "卖家列表"};
	
	public static final String[] USER_SERVICE_PAGER_TTILES = {"已关闭物品", "代售物品", "成功交易"};
	
	public static final String USER_INFO_MODIFY_TYPE = "user_type";
	
	public static final int CONTACT = 0;
	public static final int PASSWORD = 1;
			
	public static final String		ID							= "id";
	public static final String		_ID							= "_id";
	public static final String		DESC_SORT					= " _id DESC";
	public static final String		ASC_SORT					= " _id ASC";

	public static final String		CATEGORIES					= "categories";
	public static final String		CATEGORY					= "category";
	public static final String		USERS						= "users";
	public static final String		USER						= "user";
	public static final String		ITEMS						= "items";
	public static final String		ITEM						= "item";
	public static final String		RECORDS						= "records";
	public static final String		RECORD						= "record";

	public static final String	AUTHORITY	= "gxu.software_engineering.market.provider";
	public static final String	BASE_URI	= "content://" + AUTHORITY + "/";
	
	public static final String	STATUS		= "status";
	public static final String	REASON		= "reason";
	public static final String	MSG			= "msg";
	public static final int		OK			= 1;
	public static final int		NO			= 0;
	
//	public static final String DOMAIN = "http://10.0.2.2";
	public static final String DOMAIN = "http://192.168.1.103";
	
	public static final String ACCOUNT = "account";
	public static final String PWD = "pwd";
	
	public static final String TARGET_ENTITY = "target_entity";
	public static final String HTTP_URI = "http_uri";
	
	public static final String SELLER = "seller";
	public static final String UID = "uid";
	public static final String DEAL = "deal";
	public static final String LAST_ID = "last_id";
	public static final String COUNT = "count";
	
	public static final int DEFAULT_LIST_SIZE = 50;
	
	public static final String ITEMS_TYPE = "items_type";
	
	public static final int HOT_ITEMS = 1;
	public static final int LATEST_ITEMS = 2;
//	public static final int HOT_ITEMS = 1;
	
	public static final class category {
		public static final String	NAME		= "name";
		public static final String	EXTRA		= "extra";
		public static final String	ADDED_TIME	= "added_time";
		public static final String	DESCRIPTION	= "description";
	}
	
	public static final class user {
		public static final String NICK = "nick";
		public static final String ACCOUNT = "account";
		public static final String CONTACT = "contact";
		public static final String REGISTER_TIME = "register_time";
		public static final String LAST_LOGIN_TIME = "last_login_time";
		public static final String LOGIN_TIMES = "login_times";
		public static final String REAL_NAME = "real_name";
	}
	
	public static final class item {
		public static final String NAME = "name";
		public static final String CATEGORY = "category";
		public static final String PRICE = "price";
		public static final String SELLER = "seller";
		public static final String SELLER_ID = "seller_id";
		public static final String DEAL = "deal";
		public static final String EXTRA = "extra";
		public static final String ADDED_TIME = "added_time";
		public static final String LAST_MODIFIED_TIME = "last_modified_time";
		public static final String CLICK_TIMES = "click_times";
		public static final String CLOSED = "closed";
		public static final String DESCRIPTION = "description";
		
		public static final String HOTTEST_ORDER = CLICK_TIMES + " desc";
		public static final String LATEST_ORDER = ADDED_TIME + " desc";
	}

}
