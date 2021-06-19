package com.ecloud.app.service.impl;

import com.ecloud.app.pojo.ObjectType;
import com.ecloud.app.repository.ObjectTypeRepository;
import com.ecloud.app.service.ObjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjectTypeServiceImpl implements ObjectTypeService {
    @Autowired
    private ObjectTypeRepository objectTypeRepository;

    @Override
    public List<String> findTypes() {
        return objectTypeRepository.findTypes();
    }

    @Override
    public void updateTypeById(String type, int id) {
        objectTypeRepository.updateTypeById(type, id);
    }

    @Override
    public ObjectType saveAndFlush(ObjectType objectType) {
        return objectTypeRepository.saveAndFlush(objectType);
    }

    @Override
    public void deleteById(int id) {
        objectTypeRepository.deleteById(id);
    }
}
