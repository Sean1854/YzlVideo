package com.ls.libbase.manager;

import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.ls.libbase.base.BaseApplication;
import com.ls.libbase.bean.ResUser;
import com.ls.libbase.bean.UserInfo;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * 用户管理类
 */
public class UserManager {

    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_USER_ID = "key_user_id";
    private static final String KEY_NICK_NAME = "key_nick_name";
    private static final String KEY_USER_NAME = "key_user_name";
    private static final String KEY_BIO = "key_bio";
    private static final String KEY_AVATAR = "key_avatar";
    private static final String KEY_STATUS = "key_status";
    private static final String KEY_FOLLOW = "key_follow";
    private static final String KEY_FANS = "key_fans";
    private static final String KEY_MEDAL = "key_medal";
    private static UserManager instance;
    private SharedPreferences mPreferences;

    private UserManager() {
        //生成或者是获取到一个AES256数字签名算法秘钥
        try {
            String masterAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            mPreferences = EncryptedSharedPreferences.create(
                    PREFS_NAME, masterAlias, BaseApplication.getContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,//key的加密模式
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);//value的加密模式

        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static UserManager getInstance() {

        if (instance == null) {
            synchronized (UserManager.class) {
                if (instance == null) {
                    instance = new UserManager();
                }
            }
        }
        return instance;
    }

    /**
     * 保存token
     *
     * @param token
     */

    public void saveToken(String token) {
        mPreferences.edit().putString(KEY_TOKEN, token).apply();
    }

    /**
     * 获取token
     *
     * @return
     */
    public String getToken() {
        String token = mPreferences.getString(KEY_TOKEN, null);
        return token;
    }


    /**
     * 保存用户信息
     *
     * @param user
     */

    public void saveUserInfo(ResUser user) {
        UserInfo userInfo = user.getUser();
        mPreferences.edit()
                .putString(KEY_USER_ID, userInfo.getId())
                .putString(KEY_NICK_NAME, userInfo.getNickname())
                .putString(KEY_USER_NAME, userInfo.getUsername())
                .putString(KEY_AVATAR, userInfo.getAvatar())
                .putString(KEY_BIO, userInfo.getBio())
                .putString(KEY_STATUS, userInfo.getStatus())
                .putInt(KEY_FANS, user.getFans())
                .putInt(KEY_FOLLOW, user.getFollow())
                .putInt(KEY_MEDAL, user.getMedal())
                .apply();
    }


    /**
     * 获取用户信息
     */
    public ResUser getUserInfo() {
        String userId = mPreferences.getString(KEY_USER_ID, null);
        String nickName = mPreferences.getString(KEY_NICK_NAME, null);
        String userName = mPreferences.getString(KEY_USER_NAME, null);
        String bio = mPreferences.getString(KEY_BIO, null);
        String avatar = mPreferences.getString(KEY_AVATAR, null);
        String status = mPreferences.getString(KEY_STATUS, null);
        int fans = mPreferences.getInt(KEY_FANS, 0);
        int follow = mPreferences.getInt(KEY_FOLLOW, 0);
        int medal = mPreferences.getInt(KEY_MEDAL, 0);

        ResUser user = new ResUser();
        user.setFans(fans);
        user.setFollow(follow);
        user.setMedal(medal);

        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setNickname(nickName);
        userInfo.setUsername(userName);
        userInfo.setBio(bio);
        userInfo.setAvatar(avatar);
        userInfo.setStatus(status);
        user.setUser(userInfo);

        return user;
    }

    /**
     * 退出登录 清除用户信息
     */
    public void logout() {
        mPreferences.edit()
                .remove(KEY_TOKEN)
                .remove(KEY_USER_ID)
                .remove(KEY_NICK_NAME)
                .remove(KEY_USER_NAME)
                .remove(KEY_AVATAR)
                .remove(KEY_BIO)
                .remove(KEY_STATUS)
                .remove(KEY_FANS)
                .remove(KEY_FOLLOW)
                .remove(KEY_MEDAL).apply();
    }


    /**
     * @return 是否登录
     */
    public boolean isLogin() {
        String token = getToken();
        boolean isLogin = token != null && !token.isEmpty();
        return isLogin;
    }

    /**
     * 更新资料页 只能更新这三个信息
     *
     * @param avatarUrl
     * @param nickName
     * @param bio
     */
    public void updateUserInfo(String avatarUrl, String nickName, String bio) {
        mPreferences.edit()
                .putString(KEY_NICK_NAME, nickName)
                .putString(KEY_AVATAR, avatarUrl)
                .putString(KEY_BIO, bio)
                .apply();
    }
}
