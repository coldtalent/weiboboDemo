package com.example.weibotest;

import java.util.List;
import java.util.Map;

import com.example.weibotest.listener.CommentListener;
import com.example.weibotest.util.FragmentParameter;
import com.example.weibotest.util.TimeTools;
import com.example.weibotest.util.Tools;
import com.example.weibotest.util.adaptor.ContentAdaptor;
import com.example.weibotest.view.TextListView;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.Status;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author zhangxin 2014.5.20 微博评论
 */
@SuppressLint("ValidFragment")
public class CommentsFragment extends Fragment {

	private CommentsAPI commentApi;

	private CommentListener commentListener;

	private ContentAdaptor contentAdaptor;

	private SwipeRefreshLayout mSwipeRefreshLayout;

	private Handler loadHandler;
	private Thread myThread;
	// 评论的记录ID
	private String since_id;
	private String max_id;
	private String old_id;
	private Status status;

	private Context context;
	private TextView commentTitle;
	private TextView commentContent;
	private TextView commentTime;

	private TextListView commentList;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public CommentsFragment(Context context) {
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.comments_fragment, container,
				false);

		commentTitle = (TextView) view.findViewById(R.id.commentTitle);
		commentTime = (TextView) view.findViewById(R.id.commentTime);
		commentContent = (TextView) view.findViewById(R.id.commentContent);
		commentList = (TextListView) view.findViewById(R.id.commentList);

		commentTitle.setText(status.user.screen_name);
		commentTime.setText(TimeTools.changeTimeAndYear(status.created_at));
		commentContent.setText(status.text);
		myThread = new MyThread();
		commentList.setMyThread(myThread);
		
		contentAdaptor = new ContentAdaptor(inflater);
		loadHandler = new MyHandler();
		commentListener = new CommentListener(loadHandler);
		commentApi = new CommentsAPI(MainContent.accessToken);
		commentApi.show(Long.valueOf(status.id), 0L, 0L, 10, 1, 0,
				commentListener);
		mSwipeRefreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swiperefresh);

		mSwipeRefreshLayout.setColorScheme(R.color.swipe_color_1,
				R.color.swipe_color_2, R.color.swipe_color_3,
				R.color.swipe_color_4);

		// listView.setOnScrollListener(this);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mSwipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						commentList.setLoadstate(FragmentParameter.HEAD);
						myThread = new MyThread();
						myThread.start();
					}
				});
	}

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if(!commentList.isListen()){
				commentList.notifyAllBoolean();
				return;
			}
			List<Map<String, Object>> list = Tools
					.modifyCommentList((List<Comment>) msg.obj);
			myThread = new MyThread();
			commentList.setMyThread(myThread);
			if (list == null||list.size()==1) {
				mSwipeRefreshLayout.setRefreshing(false);
				commentList.notifyAllBoolean();
				commentList.removeFooterView(commentList.getLoadingView());
				return;
			}
			if(commentList.getLoadstate()==FragmentParameter.HEAD){
				since_id = (String) list.get(0).get("id");
				contentAdaptor.getList().addAll(0, list);
				contentAdaptor.notifyDataSetChanged();
				mSwipeRefreshLayout.setRefreshing(false);
				
			}else if(commentList.getLoadstate()==FragmentParameter.FOOT){
				int i = list.size()-1 ;
				max_id = (String) list.get(i).get("id");
				contentAdaptor.getList().addAll(list);
				contentAdaptor.notifyDataSetChanged();
				commentList.removeFooterView(commentList.getLoadingView());
				
			}else{
				int i = list.size() - 1;
				since_id = (String) list.get(0).get("id");
				max_id = (String) list.get(i).get("id");
				contentAdaptor.setList(list);
				commentList.setAdapter(contentAdaptor);
				commentList.setLoadstate(FragmentParameter.HEAD);
			}
			commentList.notifyAllBoolean();

		}

	}

	private class MyThread extends Thread {

		@Override
		public void run() {
			if (commentList.getLoadstate() == FragmentParameter.HEAD) {
				commentApi.show(Long.valueOf(status.id), Long.valueOf(since_id), 0L, 10, 1, 0,
						commentListener);

			} else if (commentList.getLoadstate() == FragmentParameter.FOOT) {
				commentApi.show(Long.valueOf(status.id), 0L, Long.valueOf(max_id), 10, 1, 0,
						commentListener);
			}

		}
	}

}
