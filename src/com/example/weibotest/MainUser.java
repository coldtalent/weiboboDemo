package com.example.weibotest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;

public class MainUser extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_public);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		UserFragment userFragment = new UserFragment(this);
		fragmentTransaction.replace(R.id.sample_fragment, userFragment);
		fragmentTransaction.commit();
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(name, context, attrs);
		
		
	}
}
