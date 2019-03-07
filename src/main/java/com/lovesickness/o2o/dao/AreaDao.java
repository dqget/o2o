package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.Area;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaDao {
    /*
     * 列出区域列表
     */
    List<Area> queryArea();
}	
