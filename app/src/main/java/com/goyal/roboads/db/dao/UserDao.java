package com.goyal.roboads.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.goyal.roboads.db.model.Users;
import java.util.List;

@Dao
public interface UserDao {

  @Query("Select * from users")
  List<Users> getUsers();

  @Query("Select * from users where currentUser is 1")
  Users getCurrentUser();

  @Insert
  void insertUser(Users user);

  @Update
  void updateUser(Users user);
}
