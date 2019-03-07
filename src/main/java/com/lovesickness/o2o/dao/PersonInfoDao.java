package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.PersonInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PersonInfoDao {

    /**
     * @param personInfoCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<PersonInfo> queryPersonInfoList(
            @Param("personInfoCondition") PersonInfo personInfoCondition,
            @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * @param personInfoCondition
     * @return
     */
    int queryPersonInfoCount(
            @Param("personInfoCondition") PersonInfo personInfoCondition);

    /**
     * 通过用户ID查询用户
     *
     * @param userId
     * @return
     */
    PersonInfo queryPersonInfoById(long userId);

    /**
     * 添加用户信息
     *
     * @param wechatAuth
     * @return
     */
    int insertPersonInfo(PersonInfo personInfo);

    /**
     * @param wechatAuth
     * @return
     */
    int updatePersonInfo(PersonInfo personInfo);

    /**
     * @param wechatAuth
     * @return
     */
    int deletePersonInfo(long userId);
}
