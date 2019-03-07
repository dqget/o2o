package com.lovesickness.o2o.dao.split;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicDataSourceHolder {
    public static final String DB_Master = "master";
    public static final String DB_Slave = "slave";
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);
    private static ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static Object getDbType() {
        String db = contextHolder.get();
        if (db == null) {
            db = DB_Master;
        }
        return db;
    }

    public static void setDbType(String db) {
        logger.debug("所使用的数据源是：" + db);
        contextHolder.set(db);
    }

    public static void clearDbType() {
        contextHolder.remove();
    }
}
