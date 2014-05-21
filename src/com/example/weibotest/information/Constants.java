package com.example.weibotest.information;

/**
 * 
 * @author 张昕
 * 新浪连接平台必要参数
 *
 */
public interface Constants {
	//都是自己的
	public static final String APP_KEY="3492225219";
	public static final String REDIRECT_URL="http://www.zhangxin.com";
    public static final String SCOPE = 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String EXPIRES_IN = "expires_in";
    public static final String REMIND_IN = "remind_in";
    public static final String UID = "uid";
}
