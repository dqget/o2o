package com.lovesickness.o2o.service;

import com.lovesickness.o2o.entity.PersonInfo;

public interface PersonInfoService {
    /**
     * 根据用户Id获取personInfo
     *
     * @param userId
     * @return
     */
    PersonInfo getPersonInfoById(Long userId);

}
