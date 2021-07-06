package com.ecloud.app.service;

import com.ecloud.app.pojo.ObjectType;

import java.util.List;

public interface ObjectTypeService {
    List<String> findTypes();

    void updateTypeById(String type, int id);

    ObjectType saveAndFlush(ObjectType objectType);

    void deleteById(int id);
}
