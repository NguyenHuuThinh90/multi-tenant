package com.app.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class DefaultModelMapper extends ModelMapper {
    public DefaultModelMapper() {
        this.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }
}
