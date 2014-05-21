package com.example.weibotest;

import com.sina.weibo.sdk.openapi.models.Status;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;

public class MainComment extends FragmentActivity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public);
		Intent intent = getIntent();
		int position = intent.getExtras().getInt("position");
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		CommentsFragment commentsFragment = new CommentsFragment(this);
		commentsFragment.setStatus(ContentsFragment.getMyStatusList().get(position));
		fragmentTransaction.replace(R.id.sample_fragment, commentsFragment);
		fragmentTransaction.commit();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
		
		
	}
	
}
