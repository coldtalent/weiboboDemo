package com.example.weibotest.util.adaptor;

import java.util.List;
import java.util.Map;

import com.example.weibotest.R;

import android.content.ClipData.Item;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentAdaptor extends BaseAdapter {

	private LayoutInflater mInflater = null;
	private List<Map<String, Object>> data;
	


	public static class ViewHolder {
		public String id;
		public String user_id;
		// public ImageView head;
		public TextView title;
		public TextView time;
		public TextView content;

	}

	public ContentAdaptor(Context context, List data) {
		this.mInflater = LayoutInflater.from(context);
		if (this.data == null) {
			this.data = data;
		} else {
			data.addAll(data);
		}
	}
	public ContentAdaptor(LayoutInflater inflate, List data) {
		this.mInflater = inflate;
		if (this.data == null) {
			this.data = data;
		} else {
			data.addAll(data);
		}
	}

	public ContentAdaptor(Context context) {
		this.mInflater = LayoutInflater.from(context);
	}
	public ContentAdaptor(LayoutInflater inflate) {
		this.mInflater = inflate;
	}

	public List<Map<String, Object>> getList() {
		return data;
	}

	public void setList(List data) {
		this.data = data;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public Object getItem(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	public long getItemId(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	public View getView(int i, View view, ViewGroup viewgroup) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = mInflater.inflate(R.layout.friendcontent, null);
			holder.content = (TextView) view.findViewById(R.id.content);
			holder.title = (TextView) view.findViewById(R.id.fromTitle);
			// holder.head = (ImageView) view.findViewById(R.id.stutes);
			holder.time = (TextView) view.findViewById(R.id.thistime);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.title.setText((String) data.get(i).get("title"));
		holder.content.setText((String) data.get(i).get("text"));
		holder.time.setText((String) data.get(i).get("time"));
		// holder.head.setImageDrawable((Drawable) data.get(i).get("head"));
		holder.id = (String) data.get(i).get("id");
		holder.user_id = (String) data.get(i).get("user_id");

		return view;
	}

}
