package com.goyal.roboads;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.goyal.roboads.db.RoboadsDb;
import com.goyal.roboads.db.model.Users;
import com.goyal.roboads.utils.MD5Helper;
import com.goyal.roboads.utils.CommonUtils;
import com.goyal.roboads.utils.IdsUtil;
import java.util.Objects;
import screen.unified.CometChatUnified;

public class MainActivity extends AppCompatActivity {

  ConstraintLayout clLogin;
  ConstraintLayout clSignUp;
  AppCompatEditText etNameSignUp;
  AppCompatEditText etEmailSignUp;
  ProgressBar progressBar;
  private Users currentUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
  }

  private void initViews() {
    clLogin = findViewById(R.id.clLogin);
    clSignUp = findViewById(R.id.clSignup);
    etNameSignUp = findViewById(R.id.etName);
    etEmailSignUp = findViewById(R.id.etEmail);
    progressBar = findViewById(R.id.progress_bar);
    (findViewById(R.id.buttonSubmit)).setOnClickListener((v) -> signUp());
    (findViewById(R.id.buttonLogin)).setOnClickListener((v) -> loginCometUser());
    (findViewById(R.id.buttonLogout)).setOnClickListener((v) -> logout());
    getCurrentUser();
  }

  private void loginOrSignUp() {
    if (currentUser != null) {
      clLogin.setVisibility(View.VISIBLE);
      clSignUp.setVisibility(View.GONE);
      ((AppCompatTextView) findViewById(R.id.etNameLogin)).setText(currentUser.getName());
      ((AppCompatTextView) findViewById(R.id.etEmailLogin)).setText(currentUser.getEmail());
      ((AppCompatTextView) findViewById(R.id.etGender)).setText(currentUser.getGender());
    } else {
      clSignUp.setVisibility(View.VISIBLE);
      clLogin.setVisibility(View.GONE);
    }
  }

  private void createCometUser(Users users) {
    User user = new User();
    try {
      user.setUid(MD5Helper.getMd5Encrypted(users.getEmail()));
      user.setName(MD5Helper.getMd5Encrypted(users.getName()));
    } catch (Exception e) {
      e.printStackTrace();
    }
    CometChat.createUser(user, IdsUtil.APP_API_KEY, new CometChat.CallbackListener<User>() {
      @Override
      public void onSuccess(User user) {
        Toast.makeText(MainActivity.this, "User created successfully!", Toast.LENGTH_SHORT)
            .show();
        insertUser(users);
        progressBar.setVisibility(View.GONE);
        goToChatActivity();
      }

      @Override
      public void onError(CometChatException e) {
        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
            .show();
        progressBar.setVisibility(View.GONE);
      }
    });
  }

  private void loginCometUser() {
    User user = new User();
    try {
      user.setUid(MD5Helper.getMd5Encrypted(currentUser.getEmail()));
      user.setName(MD5Helper.getMd5Encrypted(currentUser.getName()));
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (CometChat.getLoggedInUser() == null) {
      CometChat.login(user.getUid(), IdsUtil.APP_API_KEY, new CometChat.CallbackListener<User>() {

        @Override
        public void onSuccess(User user) {
          Toast.makeText(MainActivity.this, "User logged in successfully!", Toast.LENGTH_SHORT)
              .show();
          goToChatActivity();
        }

        @Override
        public void onError(CometChatException e) {
          Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
              .show();
        }
      });
    } else {
      goToChatActivity();
    }
  }

  private boolean isNameValid() {
    AppCompatTextView tvErrorName = findViewById(R.id.errorName);
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
    AppCompatTextView tvErrorEmail = findViewById(R.id.errorEmail);
    tvErrorEmail.setText("");
    if (etEmailSignUp.getText().toString().isEmpty()) {
      tvErrorEmail.setText(getString(R.string.err_email_blank));
    } else if (!CommonUtils.isEmailValid(etEmailSignUp.getText().toString())) {
      tvErrorEmail.setText(getString(R.string.err_email));
    } else {
      return true;
    }
    return false;
  }

  private String getGender() {
    return (findViewById(R.id.rbMale)).isSelected() ? "Male" : "Female";
  }

  private void goToChatActivity() {
    startActivity(new Intent(MainActivity.this, CometChatUnified.class));
    finish();
  }

  private void signUp() {
    if (isNameValid() && isEmailValid()) {
      progressBar.setVisibility(View.VISIBLE);
      createCometUser(
          new Users(etEmailSignUp.getText().toString(), etNameSignUp.getText().toString(),
              Objects.requireNonNull(etEmailSignUp.getText()).toString(), 1, getGender()));
    }
  }

  private void logout() {
    updateUser(new Users(currentUser.getUid(), currentUser.getName(), currentUser.getEmail(), 0,
        currentUser.getGender()));
    currentUser = null;
    loginOrSignUp();
  }

  private void insertUser(Users user) {
    @SuppressLint("StaticFieldLeak")
    class InsertUser extends AsyncTask<Void, Void, Void> {

      @Override
      protected Void doInBackground(Void... voids) {
        RoboadsDb.getInstance(getApplicationContext()).userDao()
            .insertUser(user);
        return null;
      }
    }
    new InsertUser().execute();
  }

  private void updateUser(Users user) {
    @SuppressLint("StaticFieldLeak")
    class UpdateUser extends AsyncTask<Void, Void, Void> {

      @Override
      protected Void doInBackground(Void... voids) {
        RoboadsDb.getInstance(getApplicationContext()).userDao()
            .updateUser(user);
        return null;
      }
    }

    new UpdateUser().execute();
  }

  private void getCurrentUser() {
    @SuppressLint("StaticFieldLeak")
    class GetUser extends AsyncTask<Void, Void, Users> {

      @Override
      protected Users doInBackground(Void... voids) {
        return RoboadsDb
            .getInstance(getApplicationContext())
            .userDao()
            .getCurrentUser();
      }

      @Override
      protected void onPostExecute(Users user) {
        super.onPostExecute(user);
        currentUser = user;
        loginOrSignUp();
      }
    }

    GetUser user = new GetUser();
    user.execute();
  }
}
