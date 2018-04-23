package zej.o2o.dao.split;

import java.util.Locale;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.transaction.support.TransactionSynchronizationManager;

//mybatis级别的拦截器，拦截mybatis传入的sql信息，根据sql读或写使用不同数据源
//需要在mybatis配置文件配置
@Intercepts({@Signature(type=Executor.class,method="update",args= {MappedStatement.class,Object.class}),
	@Signature(type=Executor.class,method="query",args= {MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class})})
public class DynamicDataSourceInterceptor implements Interceptor{

	private static Logger logger=LoggerFactory.getLogger(DynamicDataSourceInterceptor.class);
	//sql为增删改
	private static final String REGEX=".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		//获取sql的参数
		Object[]objects=invocation.getArgs();
		//第一个参数表明是添加还是修改还是...
		MappedStatement ms=(MappedStatement)objects[0];
		String lookupKey=DynamicDataSourceHolder.DB_MASTER;//决定数据源
		//判断是否是事务
		boolean synchronizedActive=TransactionSynchronizationManager.isActualTransactionActive();
		if(synchronizedActive!=true) {//用事务管理的
			
			//读方法
			if(ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {//如果是select
				//selectKey为自增id查询主键(即mybatis调用了SELECT LAST_INSERT_ID())，使用主库
				//如果要返回主键，就说明是添加操作
				if(ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
					lookupKey=DynamicDataSourceHolder.DB_MASTER;
				}else {
					BoundSql boundSql=ms.getSqlSource().getBoundSql(objects[1]);
					String sql=boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
					if(sql.matches(REGEX)) {
						lookupKey=DynamicDataSourceHolder.DB_MASTER;
					}else {
						lookupKey=DynamicDataSourceHolder.DB_SLAVE;
					}
				}
			}
		}else {
			lookupKey=DynamicDataSourceHolder.DB_MASTER;
		}
		logger.debug("设置方法[{}] use [{}] Strategy,SqlCommanType [{}]..",ms.getId(),lookupKey,ms.getSqlCommandType().name());
		DynamicDataSourceHolder.setDbType(lookupKey);
		return invocation.proceed();
	}

	//返回代理对象
	@Override
	public Object plugin(Object target) {
		//Executor支持一系列增删改查操作，只要有增删该查操作就拦截下来通过intercept()方法决定使用的数据源
		if(target instanceof Executor) {
			return Plugin.wrap(target, this);
		}else
		{
			return target;
		}
	}

	//设置
	@Override
	public void setProperties(Properties arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
