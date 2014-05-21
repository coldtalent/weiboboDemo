package com.example.weibotest.datatransform;

import java.util.List;
import java.util.Map;

import com.example.weibotest.TabControl.PageItem;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
/**
 * 
 * @author zhangxin
 *添加userfragment到tabcontrol里面
 *
 */
public class FragmentHandler extends Handler {

	private List<PageItem> mylist;
	private Context context;

	public FragmentHandler(Context context, List<PageItem> mylist) {
		this.mylist = mylist;
		this.context = context;
	}

	@Override
	public void handleMessage(Message msg) {
		Map<String,Object> map = (Map<String,Object>)msg.obj;
		String title =(String) map.get("title");
		Fragment fragment = (Fragment)map.get("fragment");
		mylist.add(new PageItem(fragment,title));
		
		
	}

}
