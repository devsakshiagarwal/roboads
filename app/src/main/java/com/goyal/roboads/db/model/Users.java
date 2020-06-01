package com.goyal.roboads.db.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class Users {
  @PrimaryKey
  @NonNull
  private String uid;
  private String name;
  private String email;
  private int currentUser;
  private String gender;

  public Users(String uid, String name, String email, int currentUser, String gender) {
    this.uid = uid;
    this.name = name;
    this.email = email;
    this.currentUser = currentUser;
    this.gender = gender;
  }

  public String getUid() {
    return uid;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public int getCurrentUser() {
    return currentUser;
  }

  public String getGender() {
    return gender;
  }
}
