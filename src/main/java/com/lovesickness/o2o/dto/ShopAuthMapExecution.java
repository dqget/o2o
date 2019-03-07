package com.lovesickness.o2o.dto;

import com.lovesickness.o2o.entity.ShopAuthMap;
import com.lovesickness.o2o.enums.LocalAuthStateEnum;
import com.lovesickness.o2o.enums.ShopAuthStateEnum;

import java.util.List;

/**
 * @author 懿
 */
public class ShopAuthMapExecution {
    /**
     * 结果状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    private int count;

    private ShopAuthMap shopAuthMap;

    private List<ShopAuthMap> shopAuthMapList;

    public ShopAuthMapExecution() {
    }

    /**
     * 失败的构造器
     */
    public ShopAuthMapExecution(ShopAuthStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    /**
     * 成功的构造器
     */
    public ShopAuthMapExecution(ShopAuthStateEnum stateEnum, ShopAuthMap shopAuthMap) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopAuthMap = shopAuthMap;
    }

    /**
     * 成功的构造器
     */

    public ShopAuthMapExecution(LocalAuthStateEnum stateEnum,
                                List<ShopAuthMap> shopAuthMapList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopAuthMapList = shopAuthMapList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ShopAuthMap getShopAuthMap() {
        return shopAuthMap;
    }

    public void setShopAuthMap(ShopAuthMap shopAuthMap) {
        this.shopAuthMap = shopAuthMap;
    }

    public List<ShopAuthMap> getShopAuthMapList() {
        return shopAuthMapList;
    }

    public void setShopAuthMapList(List<ShopAuthMap> shopAuthMapList) {
        this.shopAuthMapList = shopAuthMapList;
    }
}

