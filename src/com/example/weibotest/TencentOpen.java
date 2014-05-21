package com.example.weibotest;

import org.json.JSONObject;



import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/**
 * 
 * @author zhangxin
 * 占时不要
 *
 */
public class TencentOpen extends Activity {
	String openid = "1234567896ASDFGHJKLLIUYT";
	String access_token = "2C0884DC4B930010D852D8D504FC9F4D";
	String expires_in = "7776000"; // 实际值需要通过上面介绍的方法来计算

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.activity_oauth);
	/*	View dialogView = View.inflate(this, R.layout.oauth_dialog, null);
		Dialog dialog = new Dialog(this, R.style.access);
		dialog.setContentView(dialogView);
		dialog.show();
		Button access = (Button) dialogView.findViewById(R.id.accessButton);
		access.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
			/*	Tencent mTencent = Tencent.createInstance("101086789", TencentOpen.this.getApplicationContext());
				mTencent.setOpenId(openid);
				mTencent.setAccessToken(access_token, expires_in);
			}
		});*/
	}
}
