package com.goyal.roboads;

import android.app.Application;

import android.util.Log;
import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.goyal.roboads.utils.IdsUtil;
import timber.log.Timber;

public class RoboAdsApplication extends Application {

  private static final String TAG = RoboAdsApplication.class.getSimpleName();

  @Override
  public void onCreate() {
    super.onCreate();
    initCometChat();
  }

  private void initCometChat() {

    CometChat.init(this, IdsUtil.APP_ID, new AppSettings.AppSettingsBuilder()
            .subscribePresenceForAllUsers().setRegion(IdsUtil.APP_REGION).build(),
        new CometChat.CallbackListener<String>() {
          @Override
          public void onSuccess(String successMessage) {
            Log.d(TAG, "CometChat initialization completed.");
          }

          @Override
          public void onError(CometChatException e) {
            Log.d(TAG, "CometChat initialization failed with exception: " + e.getMessage());
          }
        });
  }
}
