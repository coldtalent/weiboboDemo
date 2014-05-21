package com.example.weibotest;

import com.example.weibotest.information.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 
 * @author zhangxin 内容控制
 */
public class MainContent extends FragmentActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	public static boolean LOCK = true;

	private String[] mPlanetTitles = { "选项1", "天才尚", "体谬尼古丁" };

	private ActionBarDrawerToggle barToggle;
	
	public static Oauth2AccessToken accessToken;
	//让别人添加
	public TabControl tabControl;
	
	//public static FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_morelist);
		
		accessToken = AccessTokenKeeper.readAccessToken(this);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mPlanetTitles));
		// mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		tabControl = new TabControl(this);
		transaction.replace(R.id.sample_content_fragment, tabControl);
		transaction.commit();

		// 初始化 ActionBarDrawerToggle
		barToggle = new ActionBarDrawerToggle(this, 
		mDrawerLayout, 
		R.drawable.ic_launcher,
		R.string.drawer_open, 
		R.string.drawer_close 
		) {
			public void onDrawerClosed(View view) {
				LOCK=!LOCK;
				invalidateOptionsMenu(); 
			}

			public void onDrawerOpened(View drawerView) {
				LOCK=!LOCK;
				invalidateOptionsMenu(); 
			}
		};
		mDrawerLayout.setDrawerListener(barToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (LOCK == true)
				mDrawerLayout.openDrawer(mDrawerList);
			else
				mDrawerLayout.closeDrawer(mDrawerList);
				LOCK=!LOCK;
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
