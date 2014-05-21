/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.weibotest;

import java.util.ArrayList;
import java.util.List;

import com.example.weibotest.datatransform.FragmentHandler;
import com.example.weibotest.view.SlidingTabLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * @author zhangxin 控制页面的跳转
 * 
 */
@SuppressLint("ValidFragment")
public class TabControl extends Fragment {

	private SlidingTabLayout mSlidingTabLayout;

	private ViewPager mViewPager;

	private List<PageItem> mylist;
	private Context context;
	
	public static  FragmentHandler fragmentHandler;

	public TabControl(Context context) {
		this.context = context;

	}

	public static class PageItem {
		Fragment fragment;
		String title;

		public PageItem(Fragment fragment, String title) {
			this.fragment = fragment;
			this.title = title;
		}

		String getTitle() {
			return title;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_content, container, false);
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

		mylist = new ArrayList<PageItem>();
		
		ContentsFragment sBasicFragment = new ContentsFragment(context);
		ContentsFragment sBasicFragment2 = new ContentsFragment(context);

		mylist.add(new PageItem(sBasicFragment, getString(R.string.publicweibo)));
		mylist.add(new PageItem(sBasicFragment2,
				getString(R.string.friendsweibo)));

		mViewPager
				.setAdapter(new SamplePagerAdapter(getChildFragmentManager()));

		mSlidingTabLayout = (SlidingTabLayout) view
				.findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setViewPager(mViewPager);

	}

	class SamplePagerAdapter extends FragmentPagerAdapter {

		public SamplePagerAdapter(FragmentManager fm) {

			super(fm);

		}

		@Override
		public int getCount() {
			return mylist.size();
		}

		@Override
		public Fragment getItem(int i) {
			// TODO Auto-generated method stub
			return mylist.get(i).fragment;
		}

		public CharSequence getPageTitle(int position) {
			return mylist.get(position).getTitle();
		}
	}
}
