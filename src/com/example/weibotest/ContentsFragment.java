package com.example.weibotest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.weibotest.MoreList.MyThread;
import com.example.weibotest.information.AccessTokenKeeper;
import com.example.weibotest.listener.ContentListener;
import com.example.weibotest.pojo.BundleStatus;
import com.example.weibotest.util.TimeTools;
import com.example.weibotest.util.Tools;
import com.example.weibotest.util.adaptor.ContentAdaptor;
import com.example.weibotest.util.adaptor.ContentAdaptor.ViewHolder;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

/**
 * 
 * @author zhangxin 2014.5
 */

@SuppressLint("ValidFragment")
public class ContentsFragment extends Fragment implements OnScrollListener {

	/**
	 * 取10条数据
	 */
	private static final int LIST_ITEM_COUNT = 10;
	/**
	 * 头部还是脚部刷新
	 */
	private static final int HEAD = 1;
	private static final int FOOT = 2;

	public static boolean islock = true;
	public static boolean LISTENER = true;
	// 最新微博的ID
	private String since_id;
	// 尾部微博ID
	private String max_id;

	private int loadstate = 0;

	private ContentAdaptor conadaptor;
	private LinearLayout loadingView;

	private Thread mythread;

	private StatusesAPI statusesAPI;

	private SwipeRefreshLayout mSwipeRefreshLayout;

	private ListView listView;

	private ContentListener contentListener;

	private Context context;

	private Handler loadHandle;

	private static List<Status> mListStatus;

	public ContentsFragment(Context context) {
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_sample, container, false);
		listView = (ListView) view.findViewById(R.id.friendsContentlist);
		conadaptor = new ContentAdaptor(inflater);

		// 通过授权拿到AccessToken
		statusesAPI = new StatusesAPI(MainContent.accessToken);
		loadHandle = new MyHandler();
		contentListener = new ContentListener(loadHandle);

		statusesAPI.friendsTimeline(0L, 0L, LIST_ITEM_COUNT, 1, false, 0,
				false, contentListener);

		mSwipeRefreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swiperefresh);

		mSwipeRefreshLayout.setColorScheme(R.color.swipe_color_1,
				R.color.swipe_color_2, R.color.swipe_color_3,
				R.color.swipe_color_4);

		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(context, MainComment.class);
				Bundle b = new Bundle();
				b.putInt("position", arg2);
				intent.putExtras(b);
				startActivity(intent);
				// intent.setClass(packageContext, cls);
				/*
				 * CommentsFragment commentsFragment = new CommentsFragment();
				 * commentsFragment.setStatus(mListStatus.get(arg2));
				 * FragmentTransaction ftion =
				 * ContentsFragment.this.getActivity()
				 * .getSupportFragmentManager().beginTransaction();
				 * ftion.replace(R.id.sample_content_fragment,
				 * commentsFragment); ftion.commit();
				 */

			}
		});

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mSwipeRefreshLayout
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						loadstate = HEAD;
						mythread = new MyThread();
						mythread.start();
					}
				});

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	/**
	 * hanlder消息处理
	 */
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if (LISTENER == false) {
				LISTENER = !LISTENER;
				islock = true;
				return;
			}
			List<Status> listStatus = (ArrayList<Status>) msg.obj;
			List<Map<String, Object>> list = Tools.modifyStutesList(listStatus);
			if (list == null) {
				mSwipeRefreshLayout.setRefreshing(false);
				return;
			}
			if (loadstate == HEAD) {

				since_id = (String) list.get(0).get("id");
				conadaptor.getList().addAll(0, list);
				mListStatus.addAll(0, listStatus);
				conadaptor.notifyDataSetChanged();
				mSwipeRefreshLayout.setRefreshing(false);

			} else if (loadstate == FOOT) {

				int i = list.size() - 1;
				max_id = (String) list.get(i).get("id");
				conadaptor.getList().addAll(list);
				conadaptor.notifyDataSetChanged();
				mListStatus.addAll(listStatus);
				listView.removeFooterView(loadingView);

			} else {
				int i = list.size() - 1;
				since_id = (String) list.get(0).get("id");
				max_id = (String) list.get(i).get("id");
				mListStatus = listStatus;
				conadaptor.setList(list);
				listView.setAdapter(conadaptor);
				loadstate =HEAD;
			}
			islock = true;
		}
	}

	private class MyThread extends Thread {

		@Override
		public void run() {
			if (loadstate == HEAD) {
				statusesAPI.friendsTimeline(Long.valueOf(since_id), 0L,
						LIST_ITEM_COUNT, 1, false, 0, false, contentListener);

			} else if (loadstate == FOOT) {

				statusesAPI.friendsTimeline(0L, Long.valueOf(max_id),
						LIST_ITEM_COUNT, 1, false, 0, false, contentListener);
			} else {

			}

		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (loadstate == 0)
			return;
		if (totalItemCount - (firstVisibleItem + visibleItemCount) == 0) {
			if (islock == true) {
				islock = false;
				loadingView = (LinearLayout) LayoutInflater.from(context)
						.inflate(R.layout.load_view, null);
				listView.addFooterView(loadingView);
				loadstate = FOOT;
				mythread = new MyThread();
				mythread.start();
				loadingView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						listView.removeFooterView(loadingView);
						LISTENER = !LISTENER;
					}
				});
			}
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	public static List<Status> getMyStatusList() {
		return mListStatus;
	}

}
