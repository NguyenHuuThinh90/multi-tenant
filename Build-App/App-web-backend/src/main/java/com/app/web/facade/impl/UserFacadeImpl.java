package com.app.web.facade.impl;


import com.app.annotation.Facade;
import com.app.converter.AbstractConverter;
import com.app.entity.UserModel;
import com.app.web.converter.UserConverter;
import com.app.web.dto.UserDto;
import com.app.web.facade.UserFacade;
import com.app.web.service.UserService;

import javax.annotation.Resource;
import java.util.List;

@Facade
public class UserFacadeImpl implements UserFacade {

    @Resource
    private UserService userService;

    @Resource
    private UserConverter userConverter;
    @Resource
    private AbstractConverter abstractConverter;

    @Override
    public boolean save(UserModel userModel) {
        return userService.save(userModel);
    }

    @Override
    public List<UserDto> findAll() {
        List<UserModel> userModels = userService.findAll();
        return this.abstractConverter.convert(userModels, UserDto.class);
    }
}
