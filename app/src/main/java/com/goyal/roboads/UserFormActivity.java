package com.goyal.roboads;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.goyal.roboads.utils.IdsUtil;
import timber.log.Timber;

public class UserFormActivity extends AppCompatActivity {

  private static final String TAG = UserFormActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_form);
    Log.d(TAG, "on create");
    createCometUser();
  }

  private void createCometUser() {
    User user = new User();
    user.setUid("goyal");
    user.setName("Sakshi");

    CometChat.createUser(user, IdsUtil.APP_API_KEY, new CometChat.CallbackListener<User>() {
      @Override
      public void onSuccess(User user) {
        Log.d(TAG,"createUser  " + user.toString());
        loginCometUser(user);
      }

      @Override
      public void onError(CometChatException e) {
        Log.e(TAG,"createUser" + e.getMessage());
        loginCometUser(user);
      }
    });
  }

  private void loginCometUser(User user) {

    if (CometChat.getLoggedInUser() == null) {
      CometChat.login(user.getUid(), IdsUtil.APP_API_KEY, new CometChat.CallbackListener<User>() {

        @Override
        public void onSuccess(User user) {
          Log.d(TAG,"Login Successful : " + user.toString());
        }

        @Override
        public void onError(CometChatException e) {
          Log.d(TAG,"Login failed with exception:" + e.getMessage());
        }
      });
    } else {
      Log.d(TAG, "User already logged in");
    }
  }
}
