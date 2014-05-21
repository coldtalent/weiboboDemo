package com.example.weibotest.db;

/**
 * 
 * @author 张昕 常用变量
 */
public class DB {
	public static class DBBase {
		// 数据库名称
		public static String DB_NAME = "weibo";
		// 数据库版本
		public static int DB_VERSION = 1;
	}

	public static class DBTABLE {
		// user表名
		public static String TABLE_NAME = "user";
		// user的一些属性
		public static String _ID = "id";
		public static String USER_ID = "userid";
		public static String USER_NAME = "username";
		public static String TOKEN = "token";
		public static String TOKEN_SECRET = "tokensecret";
		public static String DESCRIPTION = "description";
		public static String USER_HEAD = "userhead";
		public static String EXPIRES_IN = "expires_in";

		// 建表语句
		public static String CREATEUSER = "create table if not exists "
				+ TABLE_NAME + "(" + _ID
				+ " Integer primary key autoincrement," + USER_ID + " text ,"
				+ USER_NAME + " text ," + TOKEN + " text , " + TOKEN_SECRET
				+ " text," + DESCRIPTION + " text ," + EXPIRES_IN + " text ,"
				+ USER_HEAD + " BOLB)";
	}

}
