package com.example.weibotest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.weibotest.information.AccessTokenKeeper;
import com.example.weibotest.util.TimeTools;
import com.example.weibotest.util.adaptor.ContentAdaptor;
import com.example.weibotest.util.adaptor.ContentAdaptor.ViewHolder;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
/**
 * 
 * @author 张昕
 * 淘汰了
 *
 */
public class MoreList extends Activity implements OnTouchListener,
		OnGestureListener, OnScrollListener {

	/**
	 * 初始化2种状态
	 */
	private static final int TAP_TO_REFRESH = 1;
	private static final int PULL_TO_REFRESH = 2;
	/**
	 * 头部还是脚部刷新
	 */
	private static final int HEAD = 1;
	private static final int FOOT = 2;

	private static boolean islock = true;
	private static boolean LISTENER = true;
	// 最新微博的ID
	private String since_id;
	// 尾部微博ID
	private String max_id;

	private int state = TAP_TO_REFRESH;
	private int loadstate = 0;

	private Oauth2AccessToken accessToken;
	private ContentAdaptor conadaptor;

	private LinearLayout loadingView;
	private GestureDetector mGestureDetector;
	private ListView listView;

	private Thread mythread;

	private StatusesAPI statusesAPI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_morelist);
		listView = (ListView) findViewById(R.id.friendsContent);
		mGestureDetector = new GestureDetector(this, this);
		conadaptor = new ContentAdaptor(MoreList.this);

		// 通过授权拿到AccessToken
		accessToken = AccessTokenKeeper.readAccessToken(this);
		statusesAPI = new StatusesAPI(accessToken);
		statusesAPI.friendsTimeline(0L, 0L, 10, 1, false, 0, false, mListener);
		listView.setOnTouchListener(this);
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapterview, View view,
					int i, long l) {
				/*
				 * String s = ((ViewHolder) view.getTag()).user_id;
				 * Toast.makeText(MoreList.this, s, Toast.LENGTH_SHORT).show();
				 */
				System.out.println("nihao");

			}
		});

	}

	/**
	 * 微博 OpenAPI 回调接口。
	 */
	private RequestListener mListener = new RequestListener() {
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				if (response.startsWith("{\"statuses\"")) {
					StatusList statuses = StatusList.parse(response);
					if (statuses != null && statuses.total_number > 0) {
						Message msg = new Message();
						if (loadstate == HEAD) {
							if (statuses.statusList != null) {
								msg.obj = modifyStutesList(statuses.statusList);
								since_id = ((Status) statuses.statusList.get(0)).id;
							}
						} else if (loadstate == FOOT) {
							if (statuses.statusList != null) {
								int i = statuses.statusList.size() - 1;
								max_id = ((Status) statuses.statusList.get(i)).id;
								msg.obj = modifyStutesList(statuses.statusList);
							}
						} else {
							since_id = ((Status) statuses.statusList.get(0)).id;
							int i = statuses.statusList.size() - 1;
							max_id = ((Status) statuses.statusList.get(i)).id;
							msg.obj = modifyStutesList(statuses.statusList);
						}

						loadHandle.sendMessage(msg);
					}
				}
			}
		}

		public void onWeiboException(WeiboException e) {
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			islock = true;
			Toast.makeText(MoreList.this, info.toString(), Toast.LENGTH_LONG)
					.show();
		}

	};

	/**
	 * @param list
	 *            通过传来的list来完成数据的处理
	 */
	public List<Map<String, Object>> modifyStutesList(List list) {
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
	 * hanlder消息处理
	 */
	Handler loadHandle = new Handler() {
		public void handleMessage(Message msg) {
			if (LISTENER == false) {
				LISTENER = true;
				islock = true;
				return;
			}
			if (loadstate == HEAD) {

				if (msg.obj != null) {
					conadaptor.getList().addAll(0,
							(List<HashMap<String, Object>>) msg.obj);
					conadaptor.notifyDataSetChanged();
				}
				listView.removeHeaderView(loadingView);
				state = TAP_TO_REFRESH;

			} else if (loadstate == FOOT) {

				if (msg.obj != null) {
					conadaptor.getList().addAll(
							(List<HashMap<String, Object>>) msg.obj);
					conadaptor.notifyDataSetChanged();
				}
				listView.removeFooterView(loadingView);
				state = TAP_TO_REFRESH;

			} else {
				conadaptor.setList((List<HashMap<String, Object>>) msg.obj);
				listView.setAdapter(conadaptor);
			}
			islock = true;
		}
	};

	class MyThread extends Thread {

		@Override
		public void run() {
			if (loadstate == HEAD) {
				statusesAPI.friendsTimeline(Long.valueOf(since_id), 0L, 100, 1,
						false, 0, false, mListener);

			} else if (loadstate == FOOT) {
				statusesAPI.friendsTimeline(0L, Long.valueOf(max_id), 10, 1,
						false, 0, false, mListener);
			} else {

			}

		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionevent) {
		mGestureDetector.onTouchEvent(motionevent);
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e2.getY() > e1.getY() && (e2.getY() - e1.getY()) > 400.0f
				&& state == PULL_TO_REFRESH) {
			if (islock == true) {
				islock = false;
				loadingView = (LinearLayout) LayoutInflater.from(this).inflate(
						R.layout.load_view, null);
				listView.addHeaderView(loadingView);
				loadstate = HEAD;
				mythread = new MyThread();
				mythread.start();
				loadingView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						listView.removeHeaderView(loadingView);
						LISTENER = false;
					}
				});
			}
		} else {
		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (firstVisibleItem == 0) {
			state = PULL_TO_REFRESH;
		} else if (totalItemCount - (firstVisibleItem + visibleItemCount) == 0) {
			if (islock == true) {
				islock = false;
				loadingView = (LinearLayout) LayoutInflater.from(this).inflate(
						R.layout.load_view, null);
				listView.addFooterView(loadingView);
				loadstate = FOOT;
				mythread = new MyThread();
				mythread.start();
				loadingView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						listView.removeFooterView(loadingView);
						LISTENER = false;
					}
				});
			}
		} else {
			state = TAP_TO_REFRESH;
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
