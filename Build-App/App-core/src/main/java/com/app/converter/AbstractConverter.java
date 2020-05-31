package com.app.converter;

import org.springframework.context.annotation.Lazy;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author thinhnguyen
 * @param <S> any
 * @param <D> any
 */
@Lazy
@com.app.annotation.Converter
public class AbstractConverter<S, D> implements Converter<S, D> {

    private DefaultModelMapper modelMapper;

    protected AbstractConverter() {
        this.modelMapper = new DefaultModelMapper();
    }

    /**
     * Populate data from entity to dto
     * @param source
     * @param clazz
     * @return dto
     */
    public D populate(S source, Class<D> clazz) {
        return modelMapper.map(source, clazz);
    }

    /**
     * Convert object to dto
     * @param s
     * @param clazz
     * @return a dto
     */
    final public D convert(S s, Class<D> clazz)
    {
        return populate(s, clazz);
    }

    /**
     * Convert a list to list dto
     * @param s
     * @param clazz
     * @return a list dto
     */
    final public List<D> convert(Collection<S> s, Class<D> clazz)
    {
        return s.stream().map(e -> convert(e, clazz)).collect(Collectors.toList());
    }

    public DefaultModelMapper getModelMapper() {
        return modelMapper;
    }

    public void setModelMapper(DefaultModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
