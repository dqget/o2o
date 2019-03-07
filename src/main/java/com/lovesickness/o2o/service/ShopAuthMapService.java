package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.ShopAuthMapExecution;
import com.lovesickness.o2o.entity.ShopAuthMap;
import com.lovesickness.o2o.exception.ShopAuthMapOperationException;

public interface ShopAuthMapService {
    /**
     * 根据店铺Id分页显示店铺授权信息
     *
     * @param shopId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ShopAuthMapExecution getShopAuthMapListByShopId(Long shopId, Integer pageIndex, Integer pageSize);

    /**
     * 根据Id返回授权信息
     *
     * @param shopAuthMapId
     * @return
     */
    ShopAuthMap getShopAuthMapById(Long shopAuthMapId);

    /**
     * 添加授权信息
     *
     * @param shopAuthMap
     * @return
     * @throws ShopAuthMapOperationException
     */
    ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;

    /**
     * 修改授权信息
     *
     * @param shopAuthMap
     * @return
     * @throws ShopAuthMapOperationException
     */
    ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;


}
