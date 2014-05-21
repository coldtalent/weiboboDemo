package com.example.weibotest.listener;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.weibotest.ContentsFragment;
import com.example.weibotest.util.Tools;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
/**
 * 
 * @author zhangxin 
 *	contentListener
 */
public class ContentListener implements RequestListener {

	private Handler loadHandler;

	public ContentListener(Handler loadHandler) {
		this.loadHandler = loadHandler;
	}
/**
 * 微博API回调接口
 */
	public void onComplete(String response) {
		if (!TextUtils.isEmpty(response)) {
			if (response.startsWith("{\"statuses\"")) {
				StatusList statuses = StatusList.parse(response);
				if (statuses != null && statuses.total_number > 0) {
					Message msg = new Message();
					msg.obj = statuses.statusList;
					loadHandler.sendMessage(msg);
//						if (statuses.statusList != null) {
//							msg.obj = Tools
//									.modifyStutesList(statuses.statusList);
//							since_id = ((Status) statuses.statusList.get(0)).id;
//						}
//					} else if (loadstate == FOOT) {
//						if (statuses.statusList != null) {
//							int i = statuses.statusList.size() - 1;
//							max_id = ((Status) statuses.statusList.get(i)).id;
//							msg.obj = Tools
//									.modifyStutesList(statuses.statusList);
//						}
//					} else {
//						since_id = ((Status) statuses.statusList.get(0)).id;
//						int i = statuses.statusList.size() - 1;
//						max_id = ((Status) statuses.statusList.get(i)).id;
//						msg.obj = Tools
//								.modifyStutesList(statuses.statusList);
//					}
				}
			}
		}
	}

	public void onWeiboException(WeiboException e) {
		ErrorInfo info = ErrorInfo.parse(e.getMessage());
		ContentsFragment.islock = !ContentsFragment.islock;
	}

}
