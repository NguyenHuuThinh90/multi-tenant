package com.app.web.converter;


import com.app.annotation.Converter;
import com.app.converter.AbstractConverter;
import com.app.entity.UserModel;
import com.app.web.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.web.context.annotation.RequestScope;

@Converter
public class UserConverter extends AbstractConverter<UserModel, UserDto> {

    /**
     * Convert to data model
     * @param source
     * @param clazz
     * @return
     */
    @Override
    public UserDto populate(UserModel source, Class<UserDto> clazz) {
        return super.populate(source, clazz);
    }
}
