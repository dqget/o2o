package com.lovesickness.o2o.service;

public interface CacheService {
    /**
     * 根据key前缀删除匹配模式下的所有key value
     *
     * @param keyPrefix
     */
    void removeFromCache(String keyPrefix);
}
