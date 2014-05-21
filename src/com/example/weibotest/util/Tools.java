package com.example.weibotest.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.Status;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author 张昕  工具类
 *
 */
public class Tools {
	/**
	 * @param 得到一个activity
	 * 弹出个对话框通知用户没有网络 
	 * true 有网络 false没有网络
	 */
	public static boolean checkNetWork(final Activity activity) {
		if (!isNetListen(activity)) {
			TextView msg = new TextView(activity);
			msg.setText("没有网络请设置网络");
			new AlertDialog.Builder(activity)
					.setTitle("通知")
					.setView(msg)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											Settings.ACTION_WIFI_SETTINGS);
									activity.startActivityForResult(intent, 1);

								}
							}).create().show();
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 
	 * @param 得到一个上下文
	 * @return 返回一个布尔类型变量来判断网络状态
	 * false 没有网络 true有网络
	 */
	public static boolean isNetListen(Context context) {
		ConnectivityManager ctm = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);
		if (ctm == null) {
			Toast.makeText(context, "没有网络状态", Toast.LENGTH_SHORT).show();
			return false;
		}
		NetworkInfo nwk = ctm.getActiveNetworkInfo();
		if (nwk == null) {
			Toast.makeText(context, "没有网络状态", Toast.LENGTH_SHORT).show();
			return false;
		} else if (nwk.getType() == ConnectivityManager.TYPE_WIFI) {
			Toast.makeText(context, "WIFI网络", Toast.LENGTH_SHORT).show();
			return true;
		} else if (nwk.getType() == ConnectivityManager.TYPE_MOBILE) {
			Toast.makeText(context, "2G/3G网络", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}
	
	
	/**
	 * @param list
	 *            通过传来的list来完成数据的处理
	 *            微博展示处理方法
	 */
	public static List<Map<String, Object>> modifyStutesList(List list) {
		if(list==null)return null;
		List<Map<String, Object>> si = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> h = new HashMap<String, Object>();
			h.put("title", ((Status) list.get(i)).user.screen_name);
			h.put("id", ((Status) list.get(i)).id);
			String time = ((Status) list.get(i)).created_at;
			h.put("time", TimeTools.changeTimeAndYear(time));
			h.put("text", ((Status) list.get(i)).text);
			h.put("user_id", ((Status) list.get(i)).user.id);
			si.add(h);
		}
		return si;
	}
	/**
	 * @param list
	 *            通过传来的list来完成数据的处理
	 *            微博展示处理方法
	 */
	public static List<Map<String, Object>> modifyCommentList(List list) {
		if(list==null)return null;
		List<Map<String, Object>> si = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> h = new HashMap<String, Object>();
			h.put("title", ((Comment) list.get(i)).user.screen_name);
			h.put("id", ((Comment) list.get(i)).id);
			String time = ((Comment) list.get(i)).created_at;
			h.put("time", TimeTools.changeTimeAndYear(time));
			h.put("text", ((Comment) list.get(i)).text);
			h.put("user_id", ((Comment) list.get(i)).user.id);
			si.add(h);
		}
		return si;
	}

}
