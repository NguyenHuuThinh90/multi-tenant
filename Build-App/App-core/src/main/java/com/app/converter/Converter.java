package com.app.converter;

import java.util.Collection;
import java.util.List;

/**
 * @author thinhnguyen
 */
public interface Converter<S, D> {
    D populate(S s, Class<D> clazz);
    List<D> convert(Collection<S> s, Class<D> clazz);
    D convert(S s, Class<D> clazz);
}
