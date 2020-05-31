package com.app.web.dao;

import com.app.entity.UserModel;

import java.util.List;

public interface UserDao {
    boolean save(UserModel userModel);
    List<UserModel> findAll();
}
