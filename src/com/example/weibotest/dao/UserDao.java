package com.example.weibotest.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.weibotest.db.DB;
import com.example.weibotest.db.DB.DBTABLE;
import com.example.weibotest.db.DBHelper;
import com.example.weibotest.pojo.User;

/**
 * 
 * @author 张昕 user的数据操作类
 */
public class UserDao {

	private DBHelper dbHelper;
	private SQLiteDatabase sqLiteDatabase;

	private String[] columns = { DB.DBTABLE._ID, DB.DBTABLE.USER_ID,
			DB.DBTABLE.USER_NAME, DB.DBTABLE.TOKEN, DB.DBTABLE.TOKEN_SECRET,
			DB.DBTABLE.DESCRIPTION, DB.DBTABLE.EXPIRES_IN, DB.DBTABLE.USER_HEAD };

	public UserDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	// 插入user
	public boolean insert(User u) {
		sqLiteDatabase = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(DB.DBTABLE.USER_ID, u.getUserId());
		cv.put(DB.DBTABLE.USER_NAME, u.getUserName());
		cv.put(DB.DBTABLE.TOKEN, u.getToken());
		cv.put(DB.DBTABLE.TOKEN_SECRET, u.getTokenSecret());
		cv.put(DB.DBTABLE.EXPIRES_IN, u.getExpires_in());
		cv.put(DB.DBTABLE.DESCRIPTION, u.getDescription());
		if (u.getUserHead() != null) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			BitmapDrawable bitmd = (BitmapDrawable) u.getUserHead();
			bitmd.getBitmap().compress(CompressFormat.PNG, 100, os);
			cv.put(DB.DBTABLE.USER_HEAD, os.toByteArray());
		} else {
			cv.put(DB.DBTABLE.USER_HEAD, new byte[0]);
		}

		sqLiteDatabase.insert(DB.DBTABLE.TABLE_NAME, DB.DBTABLE.USER_NAME, cv);
		sqLiteDatabase.close();
		return false;
	}

	public boolean delete() {
		return false;
	}

	public boolean modify() {
		return false;
	}

	public User findUserById(int id) {
		return null;
	}

	// 查找所有的user
	public List<User> findAllUser() {
		sqLiteDatabase = dbHelper.getReadableDatabase();
		User u = null;
		List<User> list = new ArrayList<User>();
		Cursor cursor = sqLiteDatabase.query(DB.DBTABLE.TABLE_NAME, columns,
				null, null, null, null, null);
		while (cursor.moveToNext()) {
			u = new User();
			u.setId(cursor.getInt(cursor.getColumnIndex(DB.DBTABLE._ID)));
			u.setUserId(cursor.getString(cursor
					.getColumnIndex(DB.DBTABLE.USER_ID)));
			u.setUserName(cursor.getString(cursor
					.getColumnIndex(DB.DBTABLE.USER_NAME)));
			u.setToken(cursor.getString(cursor.getColumnIndex(DB.DBTABLE.TOKEN)));
			u.setTokenSecret(cursor.getString(cursor
					.getColumnIndex(DB.DBTABLE.TOKEN_SECRET)));
			u.setDescription(cursor.getString(cursor
					.getColumnIndex(DB.DBTABLE.DESCRIPTION)));
			u.setExpires_in(cursor.getString(cursor
					.getColumnIndex(DB.DBTABLE.EXPIRES_IN)));
			byte[] t = cursor.getBlob(cursor
					.getColumnIndex(DB.DBTABLE.USER_HEAD));
			ByteArrayInputStream bis = new ByteArrayInputStream(t);
			u.setUserHead(Drawable.createFromStream(bis, "userimage"));
			list.add(u);
		}

		return list;
	}
}
