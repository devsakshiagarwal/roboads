package com.goyal.roboads;

import android.content.Intent;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.goyal.roboads.utils.CommonUtils;
import com.goyal.roboads.utils.IdsUtil;
import java.util.UUID;
import screen.unified.CometChatUnified;

public class UserFormActivity extends AppCompatActivity {

  ConstraintLayout clLogin;
  ConstraintLayout clSignUp;
  AppCompatEditText etNameSignUp;
  AppCompatEditText etEmailSignUp;
  AppCompatTextView tvErrorName;
  AppCompatTextView tvErrorEmail;
  AppCompatTextView etNameLogin;
  AppCompatTextView etEmailLogin;
  AppCompatTextView genderLogin;
  AppCompatRadioButton rbMale;
  AppCompatRadioButton rbFemale;

  private static final String TAG = UserFormActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_form);
    initViews();
  }

  private void initViews() {
    clLogin = findViewById(R.id.clLogin);
    clSignUp = findViewById(R.id.clSignup);
    etNameSignUp = findViewById(R.id.etName);
    etEmailSignUp = findViewById(R.id.etEmail);
    tvErrorName = findViewById(R.id.errorName);
    tvErrorEmail = findViewById(R.id.errorEmail);
    etNameLogin = findViewById(R.id.etNameLogin);
    etEmailLogin = findViewById(R.id.etEmailLogin);
    genderLogin = findViewById(R.id.etGender);
    rbMale = findViewById(R.id.rbMale);
    rbFemale = findViewById(R.id.rbFemale);
    (findViewById(R.id.buttonSubmit)).setOnClickListener((v) -> signUp());
    (findViewById(R.id.buttonLogin)).setOnClickListener((v) -> login());
    (findViewById(R.id.buttonLogout)).setOnClickListener((v) -> logout());
    loginOrSignUp();
  }

  private void loginOrSignUp() {
    //TODO : if details in db login else signup
  }

  private void createCometUser() {
    User user = new User();
    user.setUid(UUID.randomUUID().toString());
    user.setName(etNameSignUp.getText().toString());
    user.setLink(etEmailSignUp.getText().toString());

    CometChat.createUser(user, IdsUtil.APP_API_KEY, new CometChat.CallbackListener<User>() {
      @Override
      public void onSuccess(User user) {
        Log.d(TAG, "createUser  " + user.toString());
        goToChatActivity();
      }

      @Override
      public void onError(CometChatException e) {
        Log.e(TAG, "createUser" + e.getMessage());
      }
    });
  }

  private void loginCometUser() {
    User user = new User();
    // TODO : get details from db here
    user.setUid(UUID.randomUUID().toString());
    user.setName(etNameLogin.getText().toString());
    user.setLink(etEmailLogin.getText().toString());
    if (CometChat.getLoggedInUser() == null) {
      CometChat.login(user.getUid(), IdsUtil.APP_API_KEY, new CometChat.CallbackListener<User>() {

        @Override
        public void onSuccess(User user) {
          Log.d(TAG, "Login Successful : " + user.toString());
        }

        @Override
        public void onError(CometChatException e) {
          Log.d(TAG, "Login failed with exception:" + e.getMessage());
        }
      });
    } else {
      Log.d(TAG, "User already logged in");
    }
  }

  private boolean isNameValid() {
    tvErrorName.setText("");
    if (etNameSignUp.getText().toString().isEmpty()) {
      tvErrorName.setText(getString(R.string.err_name_blank));
    } else if (!CommonUtils.isNameValid(etNameSignUp.getText().toString())) {
      tvErrorName.setText(getString(R.string.err_name));
    } else {
      return true;
    }
    return false;
  }

  private boolean isEmailValid() {
    tvErrorName.setText("");
    if (etEmailSignUp.getText().toString().isEmpty()) {
      tvErrorName.setText(getString(R.string.err_email_blank));
    } else if (!CommonUtils.isEmailValid(etEmailSignUp.getText().toString())) {
      tvErrorName.setText(getString(R.string.err_email));
    } else {
      return true;
    }
    return false;
  }

  private String getGender() {
    return rbMale.isSelected() ? "Male" : "Female";
  }

  private void goToChatActivity() {
    startActivity(new Intent(UserFormActivity.this, CometChatUnified.class));
  }

  private void signUp() {
    if (isNameValid() && isEmailValid()) {
      //save details to db here
      createCometUser();
    }
  }

  private void login() {

  }

  private void logout() {
    //logout from db here and finish activity
  }
}
