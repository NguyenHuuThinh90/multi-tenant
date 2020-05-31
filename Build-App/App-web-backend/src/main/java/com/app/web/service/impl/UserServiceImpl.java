package com.app.web.service.impl;


import com.app.entity.UserModel;
import com.app.web.dao.UserDao;
import com.app.web.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public boolean save(UserModel userModel) {
        return userDao.save(userModel);
    }

    @Override
    public List<UserModel> findAll() {
        return userDao.findAll();
    }
}
