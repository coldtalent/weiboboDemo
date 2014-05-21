package com.example.weibotest;

import java.util.List;

import com.example.weibotest.OAuthActivity.MyWeiBoListener;
import com.example.weibotest.dao.UserDao;
import com.example.weibotest.information.AccessTokenKeeper;
import com.example.weibotest.information.BaseInformation;
import com.example.weibotest.information.Constants;
import com.example.weibotest.pojo.User;
import com.example.weibotest.util.Tools;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 * @author 张昕 主程序activity
 * 
 */
public class MainActivity extends Activity {

	private ImageView enterpic;

	private WeiboAuth oAuth;

	private UserDao userDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		enterpic = (ImageView) findViewById(R.id.enterpic);
		// 透明度的动画效果参数是0.1-1.0的float类型
		ScaleAnimation sa = (ScaleAnimation) AnimationUtils.loadAnimation(this,
				R.anim.open_init);
		enterpic.setAnimation(sa);
		// 设置动画的监听来判定其他操作
		sa.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				Init();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void Init() {
		if (Tools.checkNetWork(this)) {
			userDao = new UserDao(this);
			List<User> list = userDao.findAllUser();
			User user = null;
			if (list.size() == 0) {

				oAuth = new WeiboAuth(MainActivity.this, Constants.APP_KEY,
						Constants.REDIRECT_URL, Constants.SCOPE);
				oAuth.anthorize(new MyWeiBoListener());

			} else {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getDescription().equals("1")) {
						user = list.get(i);
						//会过期
						Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken();
						oauth2AccessToken.setUid(user.getUserId());
						oauth2AccessToken.setExpiresIn(user.getExpires_in());
						oauth2AccessToken.setToken(user.getToken());
						JumpNext(oauth2AccessToken);
						break;
					}
				}
			}
		} else {

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Init();
	}

	class MyWeiBoListener implements WeiboAuthListener {

		public void onCancel() {
			// TODO Auto-generated method stub

		}

		public void onComplete(Bundle bundle) {
			Oauth2AccessToken oat = Oauth2AccessToken.parseAccessToken(bundle);

			User user = new User(oat.getUid(), oat.getToken(),
					Long.toString(oat.getExpiresTime()));
			user.setDescription("1");
			userDao.insert(user);
			if (oat.isSessionValid()) {
				JumpNext(oat);
			} else {
				String code = bundle.getString("code", "");
			}

		}

		public void onWeiboException(WeiboException weiboexception) {
			// TODO Auto-generated method stub

		}

	}

	public void JumpNext(Oauth2AccessToken oat) {
		AccessTokenKeeper.writeAccessToken(MainActivity.this, oat);
		
		startActivity(new Intent(MainActivity.this, MainContent.class));
		
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		MainActivity.this.finish();
	}

}
