package com.app.web.facade;


import com.app.entity.UserModel;
import com.app.web.dto.UserDto;

import java.util.List;

public interface UserFacade {
    boolean save(UserModel userModel);
    List<UserDto> findAll();
}
