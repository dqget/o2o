package com.lovesickness.o2o.dao.split;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Properties;

@Intercepts(value = {@Signature(args = {MappedStatement.class, Object.class}, method = "update", type = Executor.class),
        @Signature(args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}, method = "query", type = Executor.class)}
)
public class DynamicDataSourceIntercepor implements Interceptor {
    // \\u0020代表空格
    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*|";
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceIntercepor.class);

    /**
     * 主要的拦截方法
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //判断是不是事务
        boolean synchronizationActive = TransactionSynchronizationManager.isActualTransactionActive();
        String lookupKey = DynamicDataSourceHolder.DB_Master;//决定DataSource
        Object[] objects = invocation.getArgs();
        MappedStatement ms = (MappedStatement) objects[0];
        if (!synchronizationActive) {//如果不是事务操作
            //读方法
            if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                //selectKey为自增ID查询主键（SELECT LAST_INSERT_ID（））方法    ，  就使用主库
                if (ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                    lookupKey = DynamicDataSourceHolder.DB_Master;
                } else {
                    BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
                    String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replace("[\\t\\n\\r]", " ");
                    if (sql.matches(REGEX)) {//如果是insert delete update
                        lookupKey = DynamicDataSourceHolder.DB_Master;
                    } else {
                        lookupKey = DynamicDataSourceHolder.DB_Slave;
                    }
                }
            }
        } else {//是事物操作
            lookupKey = DynamicDataSourceHolder.DB_Master;
        }
        logger.debug(">>>>>设置方法[{}]use[{}]Strategy,SqlCommanType[{}]", ms.getId(), lookupKey
                , ms.getSqlCommandType().name());
        DynamicDataSourceHolder.setDbType(lookupKey);
        return invocation.proceed();
    }

    /**
     * 返回封装好的对象或代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    /**
     * 本类初始化时做的配置
     */
    @Override
    public void setProperties(Properties arg0) {
        // TODO Auto-generated method stub

    }

}
