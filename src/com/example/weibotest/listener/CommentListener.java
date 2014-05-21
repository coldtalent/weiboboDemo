package com.example.weibotest.listener;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 
 * @author zhangxin 2014.5 commentlistenr
 */
public class CommentListener implements RequestListener {
	
	private Handler loadHandler;

	public CommentListener(Handler loadHandler) {
		this.loadHandler = loadHandler;
	}
	@Override
	public void onComplete(String response) {
		if (!TextUtils.isEmpty(response)) {
			CommentList comments = CommentList.parse(response);
			if (comments != null && comments.total_number > 0) {
				Message msg = new Message();
				msg.obj = comments.commentList;
				loadHandler.sendMessage(msg);
			}
		}
	}

	@Override
	public void onWeiboException(WeiboException e) {
		ErrorInfo info = ErrorInfo.parse(e.getMessage());
	}

}
