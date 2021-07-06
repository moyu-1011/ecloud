package com.ecloud.app.service.impl;

import com.ecloud.app.repository.ObjectClassicRepository;
import com.ecloud.app.service.ObjectClassicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ObjectClassicServiceImpl implements ObjectClassicService {
    @Autowired
    private ObjectClassicRepository objectClassicRepository;

    @Override
    public String findClassicByName(String name) {
        return objectClassicRepository.findClassicByName(name);
    }
}
