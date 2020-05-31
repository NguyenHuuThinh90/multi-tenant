package com.app.service.impl;

import com.app.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author thinhnguyen
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Override
    public Date getCurrentDate() {
        return new Date();
    }

    @Override
    public String getCurrentUsername() {
        return "System";
    }
}
