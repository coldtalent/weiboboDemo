package com.example.weibotest.view;


import com.example.weibotest.R;
import com.example.weibotest.util.FragmentParameter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
/**
 * 
 * @author 张昕
 *
 */
public class TextListView extends ListView implements OnScrollListener{

	private Context context;
	private boolean islock = true;

	private boolean listen = true;
	private int loadstate; 
	private LinearLayout loadingView;
	/**
	 * 头部还是脚部刷新
	 */
	//public static final int HEAD = 1;
	//public static final int FOOT = 2;
	
	private Thread myThread;
	
	

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
				this.addFooterView(loadingView);
				loadstate = FragmentParameter.FOOT;
				if(!goThread()) Toast.makeText(context, "线程没启动", Toast.LENGTH_SHORT).show();
				loadingView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						TextListView.this.removeFooterView(loadingView);
						listen = !listen;
					}
				});
			}
		}
		
	}
	public LinearLayout getLoadingView() {
		return loadingView;
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	public void notifyAllBoolean(){
		islock = true;
		listen = true;
	}
	public void notifyIslock(){
		islock = true;
	}
	public void notifyListen(){
		listen = true;
	}
	private boolean goThread(){
		if(myThread!=null){
			myThread.start();
			return true;
		}
		else return false;
	}

	public Thread getMyThread() {
		return myThread;
	}

	public void setMyThread(Thread myThread) {
		this.myThread = myThread;
	}
	public TextListView(Context context) {
		this(context,null);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	public TextListView(Context context,AttributeSet arrs) {
		super(context,arrs);
		this.context = context;
		this.setOnScrollListener(this);
		// TODO Auto-generated constructor stub
	}
	public boolean isListen() {
		return listen;
	}
	public int getLoadstate() {
		return loadstate;
	}
	public void setLoadstate(int loadstate) {
		this.loadstate = loadstate;
	}

}
