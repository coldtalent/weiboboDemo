package com.example.weibotest;

import com.example.weibotest.dao.UserDao;
import com.example.weibotest.information.AccessTokenKeeper;
import com.example.weibotest.information.BaseInformation;
import com.example.weibotest.information.Constants;
import com.example.weibotest.pojo.User;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * 
 * @author zhangxin
 * 原来分2步的界面注册
 * 废弃
 *
 */
public class OAuthActivity extends Activity {

	private WeiboAuth oAuth;

	private UserDao userDao;

	/*
	 * private SsoHandler ssoHandler; private String state =
	 * BaseInformation.NETWORK_STATE;
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.activity_oauth);
		//View dialogView = View.inflate(this, R.layout.oauth_dialog, null);
		Dialog dialog = new Dialog(this, R.style.access);
		//dialog.setContentView(dialogView);
		dialog.show();
		//Button access = (Button) dialogView.findViewById(R.id.accessButton);
		/*access.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				oAuth = new WeiboAuth(OAuthActivity.this, Constants.APP_KEY,
						Constants.REDIRECT_URL, Constants.SCOPE);
				oAuth.anthorize(new MyWeiBoListener());
			}
		});*/
	}

	class MyWeiBoListener implements WeiboAuthListener {

		public void onCancel() {
			// TODO Auto-generated method stub

		}

		public void onComplete(Bundle bundle) {
			Oauth2AccessToken oat = Oauth2AccessToken.parseAccessToken(bundle);

			User user = new User(oat.getUid(), oat.getToken(),
					Long.toString(oat.getExpiresTime()));
			userDao.insert(user);
			if (oat.isSessionValid()) {
				AccessTokenKeeper.writeAccessToken(OAuthActivity.this, oat);
				startActivity(new Intent(OAuthActivity.this, MoreList.class));
				OAuthActivity.this.finish();
			} else {
				String code = bundle.getString("code", "");
			}

		}

		public void onWeiboException(WeiboException weiboexception) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/*
		 * if (ssoHandler != null) { ssoHandler.authorizeCallBack(requestCode,
		 * resultCode, data); } else { }
		 */
	}
}
