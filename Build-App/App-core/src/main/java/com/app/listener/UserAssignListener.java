package com.app.listener;

import com.app.common.BaseModel;
import com.app.service.ItemService;
import com.app.service.impl.ItemServiceImpl;

import javax.annotation.Resource;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

public class UserAssignListener {

//    @Resource
    private ItemService itemService = new ItemServiceImpl();

    @PrePersist
    private void beforeInsert(Object obj) {
        if (obj instanceof BaseModel) {
            String userName = itemService.getCurrentUsername();
            Date date = itemService.getCurrentDate();
            BaseModel itemModel = (BaseModel) obj;
            itemModel.setCreatedAt(date);
            itemModel.setUpdatedAt(date);
            itemModel.setCreatedBy(userName);
            itemModel.setUpdatedBy(userName);
        }
    }

    @PreUpdate
    private void beforeUpdate(Object obj) {
        if (obj instanceof BaseModel) {
            BaseModel itemModel = (BaseModel) obj;
            if (itemModel.isDeleted()) {
                itemModel.setDeletedBy(itemService.getCurrentUsername());
            } else {
                itemModel.setUpdatedBy(itemService.getCurrentUsername());
            }
            itemModel.setUpdatedAt(itemService.getCurrentDate());
        }
    }
}
