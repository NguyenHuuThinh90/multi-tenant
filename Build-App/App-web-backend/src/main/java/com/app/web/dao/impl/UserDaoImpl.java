package com.app.web.dao.impl;

import com.app.annotation.Dao;
import com.app.common.OrderQuery;
import com.app.entity.UserModel;
import com.app.service.FlexibleSearchService;
import com.app.service.ModelService;
import com.app.web.dao.UserDao;

import javax.annotation.Resource;
import java.util.List;

@Dao
public class UserDaoImpl implements UserDao {

    @Resource
    private ModelService modelService;

    @Resource
    private FlexibleSearchService flexibleSearchService;

    @Override
    public boolean save(UserModel userModel) {
        return modelService.save(userModel) != null ? true : false;
    }

    @Override
    public List<UserModel> findAll() {
        return flexibleSearchService.create(UserModel.class)
                .orderBy("id", OrderQuery.DESC)
                .getResultList();
    }
}
