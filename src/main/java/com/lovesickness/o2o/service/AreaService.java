package com.lovesickness.o2o.service;

import com.lovesickness.o2o.entity.Area;

import java.util.List;

public interface AreaService {
    public static final String AREAKEY = "arealist";

    List<Area> getAreaList();
}
