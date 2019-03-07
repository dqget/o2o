package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.PersonInfoDao;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 懿
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {
    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public PersonInfo getPersonInfoById(Long userId) {
        return personInfoDao.queryPersonInfoById(userId);
    }
}
