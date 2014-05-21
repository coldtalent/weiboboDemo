package com.example.weibotest;

import java.util.HashMap;
import java.util.Map;

import com.example.weibotest.datatransform.FragmentHandler;
import com.example.weibotest.view.TextListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @author zhangxin 2014.5 好友页面
 * 
 */
@SuppressLint("ValidFragment")
public class UserFragment extends Fragment {

	public static boolean MOVE;
	public static boolean FOUCS;

	private TextView headMain;
	private Button moveTo, foucsOn;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private TextListView userList;
	
	private Context context;
	public UserFragment(Context context){
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
		View view = inflater.inflate(R.layout.user_fragment, container, false);

		moveTo = (Button) view.findViewById(R.id.moveTo);
		foucsOn = (Button) view.findViewById(R.id.foucsOn);
		mSwipeRefreshLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swiperefresh);
		userList = (TextListView) view.findViewById(R.id.userList);
		mSwipeRefreshLayout.setColorScheme(R.color.swipe_color_1,
				R.color.swipe_color_2, R.color.swipe_color_3,
				R.color.swipe_color_4);
		moveTo.setOnClickListener(new ButtonListener());
		foucsOn.setOnClickListener(new ButtonListener());

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroy() {
		if(MOVE==true){
			Message msg = new Message();
			Map<String,Object> map = new HashMap<>();
			map.put("title", "tiantian");
			map.put("fragment", this);
			msg.obj = map;
			TabControl.fragmentHandler.sendMessage(msg);
		}

		
	}

	@SuppressLint("ResourceAsColor")
	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.moveTo:
				view.setBackgroundColor((MOVE == false) ? R.color.red
						: R.color.Silver);
				MOVE = !MOVE;
				break;
			case R.id.foucsOn:
				view.setBackgroundColor((FOUCS == false) ? R.color.red
						: R.color.Silver);
				FOUCS = !FOUCS;
				break;
			default:
				break;
			}
		}
	}

}
