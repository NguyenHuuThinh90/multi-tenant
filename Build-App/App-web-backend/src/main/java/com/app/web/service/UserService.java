package com.app.web.service;


import com.app.entity.UserModel;

import java.util.List;

public interface UserService {
    boolean save(UserModel userModel);
    List<UserModel> findAll();
}
